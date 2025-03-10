package by.javaguru.jd15.aggregation_service.usecasses.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {JsonToMapConverter.class, ObjectMapper.class})
public class JsonToMapConverterTest {

    @Autowired
    private JsonToMapConverter jsonToMapConverter;

    @Test
    void convertToDatabaseColumnWhenNullThenCorrect() {
        // Act
        Map<String, Object> map = jsonToMapConverter.convertToDatabaseColumn(null);
        // Assert
        assertNull(map);
    }

    @Test
    void convertToDatabaseColumnWhenEmptyThenCorrect() {
        // Act
        Map<String, Object> map = jsonToMapConverter.convertToDatabaseColumn("{}");
        // Assert
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void convertToDatabaseColumnWhenDataThenCorrect() {
        // Arrange
        String jsonValue = "{\"about\": {\"clientId\": 111, \"clientName\": \"John\"}}";
        // Act
        Map<String, Object> map = jsonToMapConverter.convertToDatabaseColumn(jsonValue);
        // Assert
        assertNotNull(map);
        assertTrue(map.containsKey("about"));
        Map<String, Object> mapInner = (LinkedHashMap<String, Object>) map.get("about");
        assertEquals("John", mapInner.get("clientName"));
    }
}
