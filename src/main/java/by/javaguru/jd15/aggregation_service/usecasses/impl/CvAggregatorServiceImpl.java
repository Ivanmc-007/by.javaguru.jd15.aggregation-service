package by.javaguru.jd15.aggregation_service.usecasses.impl;

import by.javaguru.jd15.aggregation_service.config.BeanName;
import by.javaguru.jd15.aggregation_service.config.TopicName;
import by.javaguru.jd15.aggregation_service.persistence.model.CvAggregator;
import by.javaguru.jd15.aggregation_service.persistence.repository.CvAggregatorRepo;
import by.javaguru.jd15.aggregation_service.usecasses.CvAggregatorService;
import by.javaguru.jd15.aggregation_service.usecasses.converter.JsonToJsonNodeConverter;
import by.javaguru.jd15.aggregation_service.usecasses.dto.CvAggregatorRequestDto;
import by.javaguru.jd15.aggregation_service.usecasses.dto.CvAggregatorResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CvAggregatorServiceImpl implements CvAggregatorService {

    private final CvAggregatorRepo cvAggregatorRepo;

    private final JsonToJsonNodeConverter jsonToJsonNodeConverter;

    @Qualifier(BeanName.KAFKA_TEMPLATE)
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final Map<String, String> informationBlockRequestMap = new HashMap<>() {
        {
            this.put("about", TopicName.CV_AGGREGATOR_REQUEST_ABOUT_TOPIC);
            this.put("contact", TopicName.CV_AGGREGATOR_REQUEST_CONTACT_TOPIC);
            this.put("education", TopicName.CV_AGGREGATOR_REQUEST_EDUCATION_TOPIC);
        }
    };

    private final Map<String, String> informationBlockResponseMap = new HashMap<>() {
        {
            this.put(TopicName.CV_AGGREGATOR_RESPONSE_ABOUT_TOPIC, "about");
            this.put(TopicName.CV_AGGREGATOR_RESPONSE_CONTACT_TOPIC, "contact");
            this.put(TopicName.CV_AGGREGATOR_RESPONSE_EDUCATION_TOPIC, "education");
        }
    };

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long aggregateByClientId(CvAggregatorRequestDto requestDto) {
        Map<String, Object> informationRequest = new HashMap<>();
        List<String> topicNames = new ArrayList<>();
        List<String> unknownBlocks = new ArrayList<>();
        for (String informationBlock : requestDto.getInformationBlocks()) {
            informationRequest.put(informationBlock, "ok");
            String topicName = informationBlockRequestMap.get(informationBlock);
            if (topicName != null) topicNames.add(topicName);
            else unknownBlocks.add(informationBlock);
        }
        if (!unknownBlocks.isEmpty()) {
            CvAggregator cvAggregator = CvAggregator.builder()
                    .cvUuid(requestDto.getCvUuid())
                    .informationRequest(informationRequest)
                    .status("error")
                    .statusMessage("Unknown informationBlocks: " + unknownBlocks)
                    .datetimeCreation(LocalDateTime.now())
                    .datetimeEnd(LocalDateTime.now())
                    .build();
            cvAggregator = cvAggregatorRepo.save(cvAggregator);
            return cvAggregator.getRequestId();
        }

        CvAggregator cvAggregator = CvAggregator.builder()
                .cvUuid(requestDto.getCvUuid())
                .informationRequest(informationRequest)
                .status("processing")
                .datetimeCreation(LocalDateTime.now())
                .build();
        cvAggregator = cvAggregatorRepo.save(cvAggregator);

        for (String topicName : topicNames) {
            ProducerRecord<String, Object> record =
                    new ProducerRecord<>(topicName, requestDto.getCvUuid(), null);
            record.headers().add("requestId", cvAggregator.getRequestId().toString().getBytes());
            kafkaTemplate.send(record);
        }
        return cvAggregator.getRequestId();
    }

    @Transactional(readOnly = true)
    @Override
    public CvAggregatorResponseDto findByRequestId(Long requestId) {
        return cvAggregatorRepo.findById(requestId).map(cvAggregator -> {
            if ("ok".equals(cvAggregator.getStatus())) {
                return CvAggregatorResponseDto.builder()
                        .status("ok")
                        .data(cvAggregator.getCvData())
                        .build();
            } else if ("processing".equals(cvAggregator.getStatus())) {
                return CvAggregatorResponseDto.builder()
                        .status("processing")
                        .build();
            }
            return CvAggregatorResponseDto.builder()
                    .status("error")
                    .message("Ошибка при агрегации данных. " + cvAggregator.getStatusMessage())
                    .build();
        }).orElse(
                CvAggregatorResponseDto.builder()
                        .status("error")
                        .message("Неизвестный requestId: " + requestId)
                        .build());
    }

    @Transactional
    @Override
    public void handle(String topicName, String requestId, String cvUuid, String jsonValue) {
        cvAggregatorRepo.findById(Long.valueOf(requestId)).ifPresent(cvAggregator -> {
            if ("error".equals(cvAggregator.getStatus()) || "ok".equals(cvAggregator.getStatus()))
                return;
            String informationBlock = informationBlockResponseMap.get(topicName);
            Map<String, Object> informationResponse = Optional.ofNullable(cvAggregator.getInformationResponse())
                    .orElse(new HashMap<>());
            try {
                informationResponse.put(informationBlock, "ok");
                cvAggregator.setInformationResponse(informationResponse);
                Map<String, Object> cvData = Optional.ofNullable(cvAggregator.getCvData()).orElse(new HashMap<>());
                cvData.put(informationBlock, jsonToJsonNodeConverter.convertToDatabaseColumn(jsonValue));
                cvAggregator.setCvData(cvData);
                if (cvAggregator.getInformationRequest().keySet().equals(informationResponse.keySet())) {
                    cvAggregator.setDatetimeEnd(LocalDateTime.now());
                    if (cvAggregator.getInformationRequest().equals(cvAggregator.getInformationResponse())) {
                        cvAggregator.setStatus("ok");
                    } else cvAggregator.setStatus("error");
                }
                cvAggregatorRepo.save(cvAggregator);
            } catch (Exception e) {
                cvAggregator.setStatus("error");
                cvAggregator.setStatusMessage(e.getMessage());
                informationResponse.put(informationBlock, "error");
                cvAggregator.setDatetimeEnd(LocalDateTime.now());
                cvAggregatorRepo.save(cvAggregator);
            }
        });
    }
}
