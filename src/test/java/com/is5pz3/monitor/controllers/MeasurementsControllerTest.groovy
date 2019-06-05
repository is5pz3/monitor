package com.is5pz3.monitor.controllers

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.ComplexMeasurement
import com.is5pz3.monitor.model.data.ComplexMeasurementInput
import com.is5pz3.monitor.model.data.Host
import com.is5pz3.monitor.model.data.Measurement
import com.is5pz3.monitor.model.data.MeasurementWrapper
import com.is5pz3.monitor.services.AuthorizationService

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
    private static final int TIME_WINDOW = 5
    private static final int CALCULATION_FREQUENCY = 1
    private static final String TOKEN = "token"
    private static final String USER_LOGIN = "userLogin"

    @Collaborator
    HostsService hostsService = Mock()

    @Collaborator
    MeasurementsService measurementsService = Mock()

    @Collaborator
    AuthorizationService authorizationService = Mock();

    @Subject
    MeasurementsController measurementsController

    def "should get all measurements by sensor id"() {
        given:
        def measurementWrapper = new MeasurementWrapper()
        measurementsService.getMeasurementsBySensorId(SENSOR_ID, LIMIT, null, null) >> measurementWrapper

        when:
        def result = measurementsController.getMeasurementsBySensorId(SENSOR_ID, LIMIT, null, null)

        then:
        result.statusCode == HttpStatus.OK
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
        def complexMeasurementInput = new ComplexMeasurementInput(SENSOR_ID, TIME_WINDOW, CALCULATION_FREQUENCY, TOKEN)

        when:
        def result = measurementsController.saveComplexMeasurement(complexMeasurementInput)

        then:
        1 * authorizationService.getUserLogin(TOKEN) >> USER_LOGIN
        1 * measurementsService.saveComplexMeasurement(complexMeasurementInput, USER_LOGIN) >> complexMeasurement
        result.statusCode == HttpStatus.OK
        result.body== complexMeasurement
    }

    def "should delete complex measurements"() {
        when:
        def result = measurementsController.deleteMeasurementsBySensorId(SENSOR_ID, TOKEN)

        then:
        1 * authorizationService.getUserLogin(TOKEN) >> USER_LOGIN
        1 * measurementsService.removeComplexMeasurement(SENSOR_ID, USER_LOGIN)
        result.statusCode == HttpStatus.CREATED
    }

}
