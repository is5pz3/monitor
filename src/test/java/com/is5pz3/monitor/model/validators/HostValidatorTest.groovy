package com.is5pz3.monitor.model.validators

import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.exceptions.BadRequestException
import spock.lang.Specification
import spock.lang.Unroll

class HostValidatorTest extends Specification {

    @Subject
    HostValidator hostValidator

    @Unroll
    def "should correctly validate sensor id"() {
        when:
        def result = hostValidator.validateSensorId(sensorId)

        then:
        result == sensorId

        where:
        sensorId          || _
        "validSensorId"   || _
        "validSensorId1"  || _
        "validSensorId_2" || _
    }

    @Unroll
    def "should throw exception when sensor id is invalid"() {
        when:
        hostValidator.validateSensorId(sensorId)

        then:
        thrown BadRequestException

        where:
        sensorId           || _
        null               || _
        ""                 || _
        "invalidSensorId!" || _
        "invalidSensorId@" || _
        "i" * 251          || _
    }

    @Unroll
    def "should correctly validate host name"() {
        when:
        def result = hostValidator.validateHostName(hostName)

        then:
        result == hostName

        where:
        hostName          || _
        "validHostName"   || _
        "validHostName1"  || _
        "validHostName_2" || _
    }

    @Unroll
    def "should throw exception when host name is invalid"() {
        when:
        hostValidator.validateHostName(hostName)

        then:
        thrown BadRequestException

        where:
        hostName           || _
        null               || _
        ""                 || _
        "invalidHostName!" || _
        "invalidHostName@" || _
        "i" * 251          || _
    }

    @Unroll
    def "should correctly validate platform"() {
        when:
        def result = hostValidator.validatePlatform(platform)

        then:
        result == platform

        where:
        platform          || _
        "validPlatform"   || _
        "validPlatform1"  || _
        "validPlatform_2" || _
    }

    @Unroll
    def "should throw exception when platform is invalid"() {
        when:
        hostValidator.validatePlatform(platform)

        then:
        thrown BadRequestException

        where:
        platform           || _
        null               || _
        ""                 || _
        "invalidPlatform!" || _
        "invalidPlatform@" || _
        "i" * 251          || _
    }

    @Unroll
    def "should correctly validate metric"() {
        when:
        def result = hostValidator.validateMetric(metric)

        then:
        result == metric

        where:
        metric          || _
        "validMetric"   || _
        "validMetric1"  || _
        "validMetric_2" || _
    }

    @Unroll
    def "should throw exception when metric is invalid"() {
        when:
        hostValidator.validateMetric(metric)

        then:
        thrown BadRequestException

        where:
        metric           || _
        null             || _
        ""               || _
        "invalidMetric!" || _
        "invalidMetric@" || _
        "i" * 31         || _
    }

    @Unroll
    def "should correctly validate unit"() {
        when:
        def result = hostValidator.validateUnit(unit)

        then:
        result == unit

        where:
        unit          || _
        "validUnit"   || _
        "validUnit1"  || _
        "123"         || _
        "validUnit@!" || _
        "#@^"         || _
    }

    @Unroll
    def "should throw exception when unit is invalid"() {
        when:
        hostValidator.validateUnit(unit)

        then:
        thrown BadRequestException

        where:
        unit     || _
        null     || _
        ""       || _
        "i" * 21 || _
    }

}
