package com.is5pz3.monitor.model.validators;

import org.springframework.stereotype.Service;

@Service
public class MeasurementValidator extends GenericValidator {

    private final String TIMESTAMP_REGEX = "^\\d{10}$";
    private final String VALUE_REGEX = "^\\d{1,2}\\.\\d{1,2}$";

    public Long validateTimestamp(Long timestamp) {
        return validateValue(timestamp, TIMESTAMP_REGEX,
                createMessage("Timesatmp ", timestamp, TIMESTAMP_REGEX));
    }

    public Double validateValue(Double value) {
        return validateValue(value, VALUE_REGEX,
                createMessage("Value ", value, VALUE_REGEX));
    }
}
