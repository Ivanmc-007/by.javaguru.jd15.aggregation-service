package by.javaguru.jd15.aggregation_service.persistence.repository;

import by.javaguru.jd15.aggregation_service.persistence.model.CvAggregator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvAggregatorRepo extends JpaRepository<CvAggregator, Long> {
}
