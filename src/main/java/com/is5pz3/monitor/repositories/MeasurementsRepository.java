package com.is5pz3.monitor.repositories;

import com.is5pz3.monitor.model.data.Measurement;
import com.is5pz3.monitor.model.entities.MeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementsRepository extends JpaRepository<MeasurementEntity, String> {

    List<MeasurementEntity> findByHostEntitySensorId(String sensorId);

}
