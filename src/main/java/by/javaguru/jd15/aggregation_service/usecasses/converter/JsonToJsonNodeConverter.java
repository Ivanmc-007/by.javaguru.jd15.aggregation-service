package by.javaguru.jd15.aggregation_service.usecasses.converter;

import by.javaguru.jd15.aggregation_service.config.BeanName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Converter
@RequiredArgsConstructor
public class JsonToJsonNodeConverter implements AttributeConverter<String, JsonNode> {

    @Qualifier(BeanName.OBJECT_MAPPER)
    private final ObjectMapper objectMapper;

    @Override
    public JsonNode convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.readTree(attribute);
        } catch (JsonProcessingException e) {
            log.error("Convert error while trying to convert string(JSON) to JsonNode. Data structure of JSON: {}", attribute);
            throw new RuntimeException("Convert error while trying to convert string(JSON) to JsonNode. Data structure of JSON: " + attribute);
        }
    }

    @Override
    public String convertToEntityAttribute(JsonNode dbData) {
        if (dbData == null)
            return null;
        try {
            return objectMapper.writeValueAsString(dbData);
        } catch (JsonProcessingException e) {
            log.error("Could not convert JsonNode to json string. Value of string: {}", dbData);
            throw new RuntimeException("Could not convert JsonNode to json string. Value of string: " + dbData);
        }
    }
}
