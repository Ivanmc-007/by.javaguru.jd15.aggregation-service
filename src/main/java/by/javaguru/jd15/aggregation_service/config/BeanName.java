package by.javaguru.jd15.aggregation_service.config;

public interface BeanName {
    String OBJECT_MAPPER = "objectMapper";
    String TRANSACTION_MANAGER = "transactionManager";
    String KAFKA_TEMPLATE = "kafkaTemplate";
    String KAFKA_TRANSACTION_MANAGER = "kafkaTransactionManager";
    String KAFKA_PRODUCER_FACTORY = "kafkaProducerFactory";
    String KAFKA_CONSUMER_FACTORY = "kafkaConsumerFactory";
    String ERROR_KAFKA_TEMPLATE = "errorKafkaTemplate";
    String GENERAL_KAFKA_LISTENER_CONTAINER_FACTORY = "generalKafkaListenerContainerFactory";
}
