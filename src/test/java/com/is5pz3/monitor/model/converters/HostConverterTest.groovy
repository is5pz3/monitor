package com.is5pz3.monitor.model.converters

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.Host
import com.is5pz3.monitor.model.entities.HostEntity
import com.is5pz3.monitor.model.validators.HostValidator
import spock.lang.Specification

class HostConverterTest extends Specification {
    private static final String HOST_ID = "hostId"
    private static final String HOST_ID2 = "hostId2"
    private static final String SENSOR_ID = "sensorId"
    private static final String HOST_NAME = "hostName"
    private static final String PLATFORM = "platform"
    private static final String METRIC = "metric"
    private static final String UNIT = "unit"

    @Collaborator
    HostValidator hostValidator = Mock()

    @Subject
    HostConverter hostConverter

    def "should correctly convert to host entity"() {
        given:
        hostValidator.validateSensorId(SENSOR_ID) >> SENSOR_ID
        hostValidator.validateHostName(HOST_NAME) >> HOST_NAME
        hostValidator.validatePlatform(PLATFORM) >> PLATFORM
        hostValidator.validateMetric(METRIC) >> METRIC
        hostValidator.validateUnit(UNIT) >> UNIT

        when:
        def result = hostConverter.toHostEntity(new Host(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT))

        then:
        assert result.sensorId == SENSOR_ID
        assert result.hostName == HOST_NAME
        assert result.platform == PLATFORM
        assert result.metric == METRIC
        assert result.unit == UNIT
    }

    def "should correctly convert to host"() {
        when:
        def result = hostConverter.toHost(new HostEntity(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT))

        then:
        assert result.id == HOST_ID
        assert result.sensorId == SENSOR_ID
        assert result.hostName == HOST_NAME
        assert result.platform == PLATFORM
        assert result.metric == METRIC
        assert result.unit == UNIT
    }

    def "should correctly convert to hosts"() {
        when:
        def result = hostConverter.toHosts([new HostEntity(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT),
                                            new HostEntity(HOST_ID2, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT)])

        then:
        assert result.size() == 2
        assert result.get(0).id == HOST_ID
        assert result.get(0).sensorId == SENSOR_ID
        assert result.get(0).hostName == HOST_NAME
        assert result.get(0).platform == PLATFORM
        assert result.get(0).metric == METRIC
        assert result.get(0).unit == UNIT
        assert result.get(1).id == HOST_ID2
        assert result.get(1).sensorId == SENSOR_ID
        assert result.get(1).hostName == HOST_NAME
        assert result.get(1).platform == PLATFORM
        assert result.get(1).metric == METRIC
        assert result.get(1).unit == UNIT
    }
}
