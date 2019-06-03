package com.is5pz3.monitor.model.converters

import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.ComplexMeasurement
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity
import com.is5pz3.monitor.model.entities.HostEntity
import spock.lang.Specification

class ComplexMeasurementConverterTest extends Specification {

    public static final String ID = "id"
    public static final String SENSOR_ID = "sensorId"
    public static final int TIME_WINDOW = 5
    public static final int CALCULATION_FREQUENCY = 1
    public static final String USER_LOGIN = "userLogin"
    @Subject
    ComplexMeasurementConverter complexMeasurementConverter

    def "should convert to entity"() {
        given:
        def hostEntity = new HostEntity()

        when:
        def result = complexMeasurementConverter.toComplexMeasurementEntity(new ComplexMeasurement(ID, USER_LOGIN, SENSOR_ID, TIME_WINDOW, CALCULATION_FREQUENCY),
                hostEntity)

        then:
        assert result.id == ID
        assert result.userLogin == USER_LOGIN
        assert result.hostEntity == hostEntity
        assert result.timeWindow == TIME_WINDOW
        assert result.calculationFrequency == CALCULATION_FREQUENCY
    }

    def "should return null if no complex measurement"() {
        given:
        def hostEntity = new HostEntity()

        expect:
        complexMeasurementConverter.toComplexMeasurementEntity(null, hostEntity) == null
    }

    def "should convert to complex measurement"() {
        given:
        def hostEntity = new HostEntity(sensorId: SENSOR_ID)

        when:
        def result = complexMeasurementConverter.toComplexMeasurement(new ComplexMeasurementEntity(ID, hostEntity, TIME_WINDOW, CALCULATION_FREQUENCY),)

        then:
        assert result.id == ID
        assert result.userLogin == USER_LOGIN
        assert result.sensorId == SENSOR_ID
        assert result.timeWindow == TIME_WINDOW
        assert result.calculationFrequency == CALCULATION_FREQUENCY
    }

    def "should return null if no complex measurement entity"() {
        expect:
        complexMeasurementConverter.toComplexMeasurement(null) == null
    }

}
