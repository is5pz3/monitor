package com.is5pz3.monitor.repositories;

import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplexMeasurementsRepository extends JpaRepository<ComplexMeasurementEntity, String> {

    List<ComplexMeasurementEntity> findByHostEntitySensorId(String sensorId);

}