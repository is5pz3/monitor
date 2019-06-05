package com.is5pz3.monitor.model.converters

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.ComplexMeasurementInput
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity
import com.is5pz3.monitor.model.entities.HostEntity
import com.is5pz3.monitor.model.validators.ComplexMeasurementsValidator
import spock.lang.Specification

class ComplexMeasurementConverterTest extends Specification {
    private static final long TIMESTAMP = 123L
    private static final String SENSOR_ID = "sensorId"
    public static final String ID = "id"
    private static final int TIME_WINDOW = 5
    private static final int CALCULATION_FREQUENCY = 1
    private static final String USER_LOGIN = "userLogin"
    public static final String TOKEN = "token"

    @Collaborator
    ComplexMeasurementsValidator complexMeasurementsValidator = Mock()

    @Subject
    ComplexMeasurementConverter complexMeasurementConverter

    def "should convert to complex measurement entity"() {
        given:
        def hostEntity = new HostEntity(sensorId: SENSOR_ID)
        def complexMeasurementIntput = new ComplexMeasurementInput(ID, TIME_WINDOW, CALCULATION_FREQUENCY, TOKEN)

        when:
        def result = complexMeasurementConverter.toComplexMeasurementEntity(complexMeasurementIntput, USER_LOGIN, hostEntity)

        then:
        1 * complexMeasurementsValidator.validateCalculationFrequency(CALCULATION_FREQUENCY) >> CALCULATION_FREQUENCY
        1 * complexMeasurementsValidator.validateTimeWindow(TIME_WINDOW) >> TIME_WINDOW
        assert result.timeWindow == TIME_WINDOW
        assert result.calculationFrequency == CALCULATION_FREQUENCY
        assert result.userLogin == USER_LOGIN
        assert result.hostEntity == hostEntity
    }

    def "should convert to complex measurement"() {
        given:
        def hostEntity = new HostEntity(sensorId: SENSOR_ID)

        when:
        def result = complexMeasurementConverter.toComplexMeasurement(new ComplexMeasurementEntity(ID, USER_LOGIN, hostEntity, TIME_WINDOW, CALCULATION_FREQUENCY, TIMESTAMP))

        then:
        assert result.id == ID
    }

    def "should return null if no complex measurement entity"() {
        expect:
        complexMeasurementConverter.toComplexMeasurement(null) == null
    }
}
