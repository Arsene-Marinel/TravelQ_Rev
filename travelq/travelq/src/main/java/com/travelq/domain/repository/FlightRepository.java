package com.travelq.domain.repository;

import com.travelq.domain.model.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
    List<FlightEntity> findByOriginAndDestination(String origin, String destination);
    List<FlightEntity> findByStopovers(int stopovers);
}
