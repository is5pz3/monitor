package com.is5pz3.monitor.repositories;

import com.is5pz3.monitor.model.entities.MeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementsRepository extends JpaRepository<MeasurementEntity, String> {
    
}
