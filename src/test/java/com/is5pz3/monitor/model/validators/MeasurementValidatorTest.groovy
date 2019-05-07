package com.is5pz3.monitor.model.validators

import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.exceptions.BadRequestException
import spock.lang.Specification
import spock.lang.Unroll

class MeasurementValidatorTest extends Specification {

    @Subject
    MeasurementValidator measurementValidator

    @Unroll
    def "should correctly validate timestamp"() {
        when:
        def result = measurementValidator.validateTimestamp(timestamp)

        then:
        result == timestamp

        where:
        timestamp  || _
        1556888808 || _
        1556888781 || _
        1556889027 || _
    }

    @Unroll
    def "should throw exception when timestamp is invalid"() {
        when:
        measurementValidator.validateTimestamp(timestamp)

        then:
        thrown BadRequestException

        where:
        timestamp || _
        null      || _
        0         || _
        10        || _
        1 * 100   || _
    }

    @Unroll
    def "should correctly validate value"() {
        when:
        def result = measurementValidator.validateValue(value)

        then:
        result == value

        where:
        value || _
        10.00 || _
        2.1   || _
        2.34  || _
        10.05 || _
    }

    @Unroll
    def "should throw exception when value is invalid"() {
        when:
        measurementValidator.validateValue(value)

        then:
        thrown BadRequestException

        where:
        value || _
        100.00 || _
        2.1234   || _
    }
}
