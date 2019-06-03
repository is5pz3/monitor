package com.is5pz3.monitor.controllers

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.ComplexMeasurement
import com.is5pz3.monitor.model.data.Host
import com.is5pz3.monitor.model.data.Measurement
import com.is5pz3.monitor.services.AuthorizationService
import com.is5pz3.monitor.services.ComplexMeasurementService
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
    public static final int TIME_WINDOW = 5
    public static final int CALCULATION_FREQUENCY = 1
    public static final String TOKEN = "token"
    public static final String USER_LOGIN = "userLogin"

    @Collaborator
    HostsService hostsService = Mock()

    @Collaborator
    MeasurementsService measurementsService = Mock()

    @Collaborator
    AuthorizationService authorizationService = Mock();

    @Collaborator
    ComplexMeasurementService complexMeasurementService = Mock()

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

    def "should post complex measurements"() {
        given:
        def complexMeasurement = new ComplexMeasurement()

        when:
        def result = measurementsController.saveComplexMeasurement(SENSOR_ID, TIME_WINDOW, CALCULATION_FREQUENCY, TOKEN)

        then:
        1 * authorizationService.getUserLogin(TOKEN) >> USER_LOGIN
        1 * complexMeasurementService.saveComplexMeasurement(SENSOR_ID, TIME_WINDOW, CALCULATION_FREQUENCY, USER_LOGIN) >> complexMeasurement
        result.statusCode == HttpStatus.OK
        result.body== complexMeasurement
    }

}
