package by.javaguru.jd15.aggregation_service.usecasses;

import by.javaguru.jd15.aggregation_service.usecasses.dto.CvAggregatorRequestDto;
import by.javaguru.jd15.aggregation_service.usecasses.dto.CvAggregatorResponseDto;

public interface CvAggregatorService {
    Long aggregateByClientId(CvAggregatorRequestDto requestDto);

    CvAggregatorResponseDto findByRequestId(Long requestId);

    void handle(String topicName, String requestId, String cvUuid, String jsonValue);
}
