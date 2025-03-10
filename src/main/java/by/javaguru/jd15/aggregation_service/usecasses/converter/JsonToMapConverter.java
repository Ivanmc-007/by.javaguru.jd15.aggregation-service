package by.javaguru.jd15.aggregation_service.usecasses.converter;

import by.javaguru.jd15.aggregation_service.config.BeanName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Converter
@RequiredArgsConstructor
public class JsonToMapConverter implements AttributeConverter<String, Map<String, Object>> {

    @Qualifier(BeanName.OBJECT_MAPPER)
    private final ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.readValue(attribute, HashMap.class);
        } catch (IOException e) {
            log.error("Convert error while trying to convert string(JSON) to map data structure. JSON: {}", attribute);
            throw new RuntimeException("Convert error while trying to convert string(JSON) to map data structure. JSON: " + attribute);
        }
    }

    @Override
    public String convertToEntityAttribute(Map<String, Object> dbData) {
        try {
            return objectMapper.writeValueAsString(dbData);
        } catch (JsonProcessingException e) {
            log.error("Could not convert map to json string.");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
