package com.is5pz3.monitor.controllers

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.Host
import com.is5pz3.monitor.model.data.Measurement
import com.is5pz3.monitor.services.HostsService
import com.is5pz3.monitor.services.MeasurementsService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class HostsControllerTest extends Specification {
    private static final String HOST_ID = "hostId"
    private static final String SENSOR_ID = "sensorId"
    private static final String HOST_NAME = "hostName"
    private static final String PLATFORM = "platform"
    private static final String METRIC = "metric"
    private static final String UNIT = "unit"
    private static final BigDecimal VALUE = 10.0
    private static final BigDecimal VALUE2 = 20.0
    private static final long TIMESTAMP = 123L
    private static final long TIMESTAMP2 = 456L

    @Collaborator
    HostsService hostsService = Mock()

    @Collaborator
    MeasurementsService measurementsService = Mock()

    @Subject
    HostsController hostsController

    def "should save host"() {
        given:
        def host = new Host(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT)
        hostsService.saveHost(host) >> host

        when:
        def result = hostsController.addHost(host)

        then:
        assert result.statusCode == HttpStatus.CREATED
        assert result.body == host
    }

    def "should get hosts by host name"() {
        given:
        def host = new Host(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT)
        hostsService.getHostsByHostName(HOST_NAME) >> [host]

        when:
        def result = hostsController.getHostsByHostName(HOST_NAME)

        then:
        assert result.statusCode == HttpStatus.OK
        assert result.body == [host]
    }

    def "should add measurement"() {
        given:
        def measurement = new Measurement(TIMESTAMP, VALUE)
        measurementsService.saveMeasurement(measurement, SENSOR_ID) >> measurement

        when:
        def result = hostsController.addMeasurement(SENSOR_ID, measurement)

        then:
        assert result.statusCode == HttpStatus.CREATED
        assert result.body == measurement
    }

    def "should add measurements"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurement2 = new Measurement(TIMESTAMP2, VALUE2)
        measurementsService.saveMeasurements([measurement1, measurement2], SENSOR_ID) >> [measurement1, measurement2]

        when:
        def result = hostsController.addMeasurements(SENSOR_ID, [measurement1, measurement2])

        then:
        assert result.statusCode == HttpStatus.CREATED
        assert result.body.containsAll([measurement1, measurement2])
    }

}
