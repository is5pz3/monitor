package com.is5pz3.monitor.model.converters;

import com.is5pz3.monitor.model.data.ComplexMeasurement;
import com.is5pz3.monitor.model.data.ComplexMeasurementInput;
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity;
import com.is5pz3.monitor.model.entities.HostEntity;
import com.is5pz3.monitor.model.validators.ComplexMeasurementsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ComplexMeasurementConverter {

    @Autowired
    ComplexMeasurementsValidator complexMeasurementsValidator;

    public ComplexMeasurementEntity toComplexMeasurementEntity(ComplexMeasurementInput complexMeasurementInput, String userLogin, HostEntity hostEntity) {
        return ComplexMeasurementEntity.builder()
                .timestamp(new Date().getTime()/1000)
                .userLogin(userLogin)
                .calculationFrequency(complexMeasurementsValidator.validateCalculationFrequency(complexMeasurementInput.getCalculationFrequency()))
                .timeWindow(complexMeasurementsValidator.validateTimeWindow(complexMeasurementInput.getTimeWindow()))
                .hostEntity(hostEntity)
                .build();
    }

    public ComplexMeasurement toComplexMeasurement(ComplexMeasurementEntity complexMeasurementEntity) {
        if (complexMeasurementEntity == null) {
            return null;
        }

        return ComplexMeasurement.builder()
                .id(complexMeasurementEntity.getId())
                .build();
    }

}
