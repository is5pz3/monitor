package com.is5pz3.monitor.model.converters

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.Measurement
import com.is5pz3.monitor.model.entities.HostEntity
import com.is5pz3.monitor.model.entities.MeasurementEntity
import com.is5pz3.monitor.model.validators.MeasurementValidator
import spock.lang.Specification

class MeasurementConverterTest extends Specification {
    private static final BigDecimal VALUE = 10.0
    private static final BigDecimal VALUE2 = 12.0
    private static final long TIMESTAMP = 123L
    private static final long TIMESTAMP2 = 456L
    private static final String SENSOR_ID = "sensorId"

    @Collaborator
    MeasurementValidator measurementValidator = Mock()

    @Subject
    MeasurementConverter measurementConverter

    def "should correctly convert to measurement entity"() {
        given:
        def hostEntity = new HostEntity()
        measurementValidator.validateTimestamp(TIMESTAMP) >> TIMESTAMP
        measurementValidator.validateValue(VALUE) >> VALUE

        when:
        def result = measurementConverter.toMeasurementEntity(new Measurement(TIMESTAMP, VALUE), hostEntity)

        then:
        assert result.hostEntity == hostEntity
        assert result.timestamp == TIMESTAMP
        assert result.value == VALUE
    }

    def "should correctly convert to measurement entities"() {
        given:
        def hostEntity = new HostEntity()
        measurementValidator.validateTimestamp(TIMESTAMP) >> TIMESTAMP
        measurementValidator.validateTimestamp(TIMESTAMP2) >> TIMESTAMP2
        measurementValidator.validateValue(VALUE) >> VALUE
        measurementValidator.validateValue(VALUE2) >> VALUE2

        when:
        def result = measurementConverter.toMeasurementEntities([new Measurement(TIMESTAMP, VALUE),
                                                                 new Measurement(TIMESTAMP2, VALUE2)], hostEntity)

        then:
        assert result.size() == 2
        assert result.get(0).hostEntity == hostEntity
        assert result.get(0).timestamp == TIMESTAMP
        assert result.get(0).value == VALUE
        assert result.get(1).hostEntity == hostEntity
        assert result.get(1).timestamp == TIMESTAMP2
        assert result.get(1).value == VALUE2
    }

    def "should correctly convert to measurement"() {
        when:
        def result = measurementConverter.toMeasurement(new MeasurementEntity(timestamp: TIMESTAMP, value: VALUE))

        then:
        assert result.timestamp == TIMESTAMP
        assert result.value == VALUE
    }

    def "should correctly convert to measurements"() {
        when:
        def result = measurementConverter.toMeasurements([new MeasurementEntity(timestamp: TIMESTAMP,value: VALUE),
                                                                 new MeasurementEntity(timestamp: TIMESTAMP2, value: VALUE2)])

        then:
        assert result.size() == 2
        assert result.get(0).timestamp == TIMESTAMP
        assert result.get(0).value == VALUE
        assert result.get(1).timestamp == TIMESTAMP2
        assert result.get(1).value == VALUE2
    }

}
