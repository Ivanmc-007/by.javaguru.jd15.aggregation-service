package by.javaguru.jd15.aggregation_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String producerBootstrapServers;

    @Value("${spring.kafka.producer.acks}")
    private String producerAcks;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private String producerEnableIdempotence;

    @Value("${spring.kafka.producer.transaction-id-prefix}")
    private String producerTransactionIdPrefix;

    Map<String, Object> kafkaProducerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, producerAcks);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerEnableIdempotence);
        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, producerTransactionIdPrefix);
        return config;
    }

    @Bean(name = BeanName.KAFKA_PRODUCER_FACTORY)
    public ProducerFactory<String, Object> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
    }

    @Bean(name = BeanName.KAFKA_TEMPLATE)
    public KafkaTemplate<String, Object> kafkaTemplate(
            @Qualifier(BeanName.KAFKA_PRODUCER_FACTORY) ProducerFactory<String, Object> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean(name = BeanName.KAFKA_TRANSACTION_MANAGER)
    public KafkaTransactionManager<String, Object> kafkaTransactionManager(
            @Qualifier(BeanName.KAFKA_PRODUCER_FACTORY) ProducerFactory<String, Object> kafkaProducerFactory) {
        return new KafkaTransactionManager<>(kafkaProducerFactory);
    }
}
