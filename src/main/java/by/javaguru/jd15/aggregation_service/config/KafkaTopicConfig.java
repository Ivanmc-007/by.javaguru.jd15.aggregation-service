package by.javaguru.jd15.aggregation_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic cvAggregatorRequestAboutTopic() {
        return TopicBuilder.name(TopicName.CV_AGGREGATOR_REQUEST_ABOUT_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic cvAggregatorResponseAboutTopic() {
        return TopicBuilder.name(TopicName.CV_AGGREGATOR_RESPONSE_ABOUT_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic cvAggregatorRequestContactTopic() {
        return TopicBuilder.name(TopicName.CV_AGGREGATOR_REQUEST_CONTACT_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic cvAggregatorResponseContactTopic() {
        return TopicBuilder.name(TopicName.CV_AGGREGATOR_RESPONSE_CONTACT_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic cvAggregatorRequestEducationTopic() {
        return TopicBuilder.name(TopicName.CV_AGGREGATOR_REQUEST_EDUCATION_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic cvAggregatorResponseEducationTopic() {
        return TopicBuilder.name(TopicName.CV_AGGREGATOR_RESPONSE_EDUCATION_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
