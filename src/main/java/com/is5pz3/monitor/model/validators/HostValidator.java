package com.is5pz3.monitor.model.validators;

import com.is5pz3.monitor.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class HostValidator {

    private final String SENSOR_ID_REGEX = "[a-zA-Z0-9_]{1,250}";
    private final String HOST_NAME_REGEX = "[a-zA-Z0-9_]{1,250}";
    private final String PLATFORM_REGEX = "[a-zA-Z0-9_]{1,250}";
    private final String METRIC_REGEX = "[a-zA-Z0-9_]{1,30}";
    private final String UNIT_REGEX = ".{0,20}";

    public String validateSensorId(String sensorId) {
        return validateValue(sensorId, SENSOR_ID_REGEX,
                createMessage("Sensor id ", sensorId, SENSOR_ID_REGEX));
    }

    public String validateHostName(String hostName) {
        return validateValue(hostName, HOST_NAME_REGEX,
                createMessage("Host name ", hostName, HOST_NAME_REGEX));
    }

    public String validatePlatform(String platform) {
        return validateValue(platform, PLATFORM_REGEX,
                createMessage("Platform ", platform, PLATFORM_REGEX));
    }

    public String validateMetric(String metricName) {
        return validateValue(metricName, METRIC_REGEX,
                createMessage("Metric name ", metricName, METRIC_REGEX));
    }

    public String validateUnit(String unit) {
        return validateValue(unit, UNIT_REGEX,
                createMessage("Unit ", unit, UNIT_REGEX));
    }

    private String createMessage(String valueName, String value, String regex) {
        return valueName + value + " does not match pattern: " + regex;
    }

    private String validateValue(String value, String unit_regex, String message) {
        if (!matchesPattern(unit_regex, value)) {
            throw new BadRequestException(message);
        }
        return value;
    }

    private boolean matchesPattern(String regex, String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return Pattern.compile(regex)
                .matcher(value)
                .matches();
    }

}
