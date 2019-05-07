package com.is5pz3.monitor.controllers

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.Host
import com.is5pz3.monitor.model.data.Measurement
import com.is5pz3.monitor.services.HostsService
import com.is5pz3.monitor.services.MeasurementsService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class MeasurementsControllerTest extends Specification {
    private static final int LIMIT = 2
    private static final String SENSOR_ID = "sensorId"
    private static final BigDecimal VALUE = 10.0
    private static final BigDecimal VALUE2 = 20.0
    private static final long TIMESTAMP = 123L
    private static final long TIMESTAMP2 = 456L

    @Collaborator
    HostsService hostsService = Mock()

    @Collaborator
    MeasurementsService measurementsService = Mock()

    @Subject
    MeasurementsController measurementsController

    def "should get all measurements by sensor id"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurement2 = new Measurement(TIMESTAMP2, VALUE2)
        measurementsService.getMeasurementsBySensorId(SENSOR_ID, LIMIT) >> [measurement1, measurement2]

        when:
        def result = measurementsController.getMeasurementsBySensorId(SENSOR_ID, LIMIT)

        then:
        result.statusCode == HttpStatus.OK
        result.body.size() == 2
        result.body.containsAll([measurement1, measurement2])
    }

    def "should get all measurements (info about hosts)"() {
        given:
        def host = new Host(sensorId: SENSOR_ID)
        hostsService.allHosts >> [host]

        when:
        def result = measurementsController.getAllHosts()

        then:
        result.statusCode == HttpStatus.OK
        result.body.size() == 1
        result.body.contains(host)
    }

}
