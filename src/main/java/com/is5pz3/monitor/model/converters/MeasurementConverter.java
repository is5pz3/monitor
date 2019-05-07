package com.is5pz3.monitor.model.converters;

import com.is5pz3.monitor.model.data.Measurement;
import com.is5pz3.monitor.model.entities.HostEntity;
import com.is5pz3.monitor.model.entities.MeasurementEntity;
import com.is5pz3.monitor.model.validators.MeasurementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasurementConverter {
    @Autowired
    private MeasurementValidator measurementValidator;

    public MeasurementEntity toMeasurementEntity(Measurement measurement, HostEntity hostEntity) {
        return MeasurementEntity.builder()
                .hostEntity(hostEntity)
                .timestamp(measurementValidator.validateTimestamp(measurement.getTimestamp()))
                .value(measurementValidator.validateValue(measurement.getValue()))
                .build();
    }

    public List<MeasurementEntity> toMeasurementEntities(List<Measurement> measurements, HostEntity hostEntity) {
        return measurements.stream()
                .map(measurement -> toMeasurementEntity(measurement, hostEntity))
                .collect(Collectors.toList());
    }

    public Measurement toMeasurement(MeasurementEntity measurementEntity) {
        return Measurement.builder()
                .timestamp(measurementEntity.getTimestamp())
                .value(measurementEntity.getValue())
                .build();
    }

    public List<Measurement> toMeasurements(List<MeasurementEntity> measurementEntities) {
        return measurementEntities.stream()
                .map(this::toMeasurement)
                .collect(Collectors.toList());
    }
}
