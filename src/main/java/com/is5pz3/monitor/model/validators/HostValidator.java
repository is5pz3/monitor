package com.is5pz3.monitor.model.validators;

import org.springframework.stereotype.Service;

@Service
public class HostValidator extends GenericValidator {

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
}
