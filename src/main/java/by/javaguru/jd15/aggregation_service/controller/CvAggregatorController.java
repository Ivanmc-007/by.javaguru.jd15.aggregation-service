package by.javaguru.jd15.aggregation_service.controller;

import by.javaguru.jd15.aggregation_service.config.BeanName;
import by.javaguru.jd15.aggregation_service.config.TopicName;
import by.javaguru.jd15.aggregation_service.usecasses.CvAggregatorService;
import by.javaguru.jd15.aggregation_service.usecasses.dto.CvAggregatorIdDto;
import by.javaguru.jd15.aggregation_service.usecasses.dto.CvAggregatorRequestDto;
import by.javaguru.jd15.aggregation_service.usecasses.dto.CvAggregatorResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class CvAggregatorController {
    private final CvAggregatorService cvAggregatorService;

    @PostMapping(value = "/cv-aggregator", produces = MediaType.APPLICATION_JSON_VALUE)
    public CvAggregatorIdDto aggregateByClientId(@Valid @RequestBody CvAggregatorRequestDto requestDto) {
        Long requestId = cvAggregatorService.aggregateByClientId(requestDto);
        return CvAggregatorIdDto.builder().requestId(requestId).build();
    }

    @GetMapping(value = "/cv-aggregator", produces = MediaType.APPLICATION_JSON_VALUE)
    public CvAggregatorResponseDto getByRequestId(@RequestParam Long requestId) {
        return cvAggregatorService.findByRequestId(requestId);
    }

    @KafkaListener(
            topics = {
                    TopicName.CV_AGGREGATOR_RESPONSE_ABOUT_TOPIC,
                    TopicName.CV_AGGREGATOR_RESPONSE_CONTACT_TOPIC,
                    TopicName.CV_AGGREGATOR_RESPONSE_EDUCATION_TOPIC
            },
            containerFactory = BeanName.GENERAL_KAFKA_LISTENER_CONTAINER_FACTORY)
    public void handle(@Header(KafkaHeaders.RECEIVED_TOPIC) String topicName,
                       @Header("requestId") String requestId,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String cvUuid,
                       @Payload String jsonValue) {
        cvAggregatorService.handle(topicName, requestId, cvUuid, jsonValue);
    }
}
