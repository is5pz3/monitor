package com.is5pz3.monitor.model.converters;

import com.is5pz3.monitor.model.data.ComplexMeasurement;
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity;
import com.is5pz3.monitor.model.entities.HostEntity;
import org.springframework.stereotype.Service;

@Service
public class ComplexMeasurementConverter {

    public ComplexMeasurementEntity toComplexMeasurementEntity(ComplexMeasurement complexMeasurement, HostEntity hostEntity) {
        if (complexMeasurement == null) {
            return null;
        }

        return ComplexMeasurementEntity.builder()
                .id(complexMeasurement.getId())
                .userLogin(complexMeasurement.getUserLogin())
                .hostEntity(hostEntity)
                .timeWindow(complexMeasurement.getTimeWindow())
                .calculationFrequency(complexMeasurement.getCalculationFrequency())
                .build();
    }

    public ComplexMeasurement toComplexMeasurement(ComplexMeasurementEntity complexMeasurementEntity) {
        if (complexMeasurementEntity == null) {
            return null;
        }

        return ComplexMeasurement.builder()
                .id(complexMeasurementEntity.getId())
                .userLogin(complexMeasurementEntity.getUserLogin())
                .sensorId(complexMeasurementEntity.getHostEntity().getSensorId())
                .timeWindow(complexMeasurementEntity.getTimeWindow())
                .calculationFrequency(complexMeasurementEntity.getCalculationFrequency())
                .build();
    }

}
