package com.is5pz3.monitor.model.validators

import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.exceptions.BadRequestException
import spock.lang.Specification


class ComplexMeasurementsValidatorTest extends Specification {
    private static final String SENSOR_ID = "sensorId"
    private static final int TIME_WINDOW = 5
    private static final int CALCULATION_FREQUENCY = 1

    @Subject
    ComplexMeasurementsValidator complexMeasurementsValidator

    def "should validate sensorId"() {
        expect:
        complexMeasurementsValidator.validateSensorId(SENSOR_ID) == SENSOR_ID
    }

    def "should validate sensorId and throw exception"() {
        when:
        complexMeasurementsValidator.validateSensorId(null)

        then:
        thrown BadRequestException
    }

    def "should validate timeWindow"() {
        expect:
        complexMeasurementsValidator.validateTimeWindow(TIME_WINDOW) == TIME_WINDOW
    }

    def "should validate timeWindow and throw exception"() {
        when:
        complexMeasurementsValidator.validateTimeWindow(null)

        then:
        thrown BadRequestException
    }

    def "should validate calculationFrequency"() {
        expect:
        complexMeasurementsValidator.validateCalculationFrequency(CALCULATION_FREQUENCY) == CALCULATION_FREQUENCY
    }

    def "should validate calculationFrequency and throw exception"() {
        when:
        complexMeasurementsValidator.validateCalculationFrequency(null)

        then:
        thrown BadRequestException
    }
}
