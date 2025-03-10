package by.javaguru.jd15.aggregation_service.usecasses.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvAggregatorResponseDto {
    private String status;
    private String message;
    private Map<String, Object> data;
}
