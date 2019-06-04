package com.is5pz3.monitor.model.converters;

import com.is5pz3.monitor.model.data.ComplexMeasurement;
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity;
import org.springframework.stereotype.Service;

@Service
public class ComplexMeasurementConverter {

    public ComplexMeasurement toComplexMeasurement(ComplexMeasurementEntity complexMeasurementEntity) {
        if (complexMeasurementEntity == null) {
            return null;
        }

        return ComplexMeasurement.builder()
                .id(complexMeasurementEntity.getId())
                .build();
    }

}
