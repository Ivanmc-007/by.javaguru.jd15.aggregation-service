package by.javaguru.jd15.aggregation_service.usecasses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvAggregatorRequestDto {
    @NotNull
    private String cvUuid;

    @NotEmpty
    @Schema(example = "[\"about\", \"contact\", \"education\"]")
    private Set<String> informationBlocks;
}
