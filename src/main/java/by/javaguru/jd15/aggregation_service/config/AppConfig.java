package by.javaguru.jd15.aggregation_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class AppConfig {

    @Primary
    @Bean(name = BeanName.OBJECT_MAPPER)
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Primary
    @Bean(name = BeanName.TRANSACTION_MANAGER)
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
