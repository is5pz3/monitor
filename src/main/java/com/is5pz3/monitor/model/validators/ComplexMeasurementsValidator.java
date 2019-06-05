package com.is5pz3.monitor.model.validators;

import com.is5pz3.monitor.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class ComplexMeasurementsValidator {

    public String validateSensorId(String sensorId) {
        return validateValuePresent(sensorId, "sensorId");
    }

    public Integer validateTimeWindow(Integer timeWindow) {
        return validateValuePresent(timeWindow, "timeWindow");
    }

    public Integer validateCalculationFrequency(Integer calculationFrequency) {
        return validateValuePresent(calculationFrequency, "calculationFrequency");
    }

    private <T> T validateValuePresent(T value, String valueName){
        if (value == null) {
            throw new BadRequestException(valueName + " cannot be empty");
        }
        return value;
    }
}
