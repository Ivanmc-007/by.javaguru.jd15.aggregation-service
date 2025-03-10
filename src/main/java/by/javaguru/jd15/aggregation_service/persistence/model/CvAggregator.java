package by.javaguru.jd15.aggregation_service.persistence.model;

import by.javaguru.jd15.aggregation_service.usecasses.converter.JsonToMapConverter;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cv_aggregator")
public class CvAggregator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "cv_uuid", nullable = false)
    private String cvUuid;

    @Type(JsonType.class)
    @Column(name = "information_request", nullable = false, columnDefinition = "json")
    @Convert(converter = JsonToMapConverter.class)
    private Map<String, Object> informationRequest;

    @Type(JsonType.class)
    @Column(name = "information_response", columnDefinition = "json")
    @Convert(converter = JsonToMapConverter.class)
    private Map<String, Object> informationResponse;

    @Column(name = "status")
    private String status;

    @Column(name = "status_message")
    private String statusMessage;

    @Type(JsonType.class)
    @Column(name = "cv_data", columnDefinition = "json")
    @Convert(converter = JsonToMapConverter.class)
    private Map<String, Object> cvData;

    @Column(name = "datetime_creation")
    private LocalDateTime datetimeCreation;

    @Column(name = "datetime_end")
    private LocalDateTime datetimeEnd;
}
