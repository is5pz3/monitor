package com.is5pz3.monitor.services

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.exceptions.BadRequestException
import com.is5pz3.monitor.model.converters.ComplexMeasurementConverter
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity
import com.is5pz3.monitor.model.entities.HostEntity
import com.is5pz3.monitor.repositories.ComplexMeasurementsRepository
import com.is5pz3.monitor.repositories.HostsRepository
import spock.lang.Specification

class ComplexMeasurementServiceTest extends Specification {
    public static final String SENSOR_ID = "sensorId"
    public static final int TIME_WINDOW = 5
    public static final int CALCULATION_FREQUENCY = 1
    public static final String USER_LOGIN = "userLogin"
    public static final String ID = "id"
    @Collaborator
    ComplexMeasurementsRepository complexMeasurementsRepository = Mock()
    @Collaborator
    ComplexMeasurementConverter complexMeasurementConverter = Mock()
    @Collaborator
    HostsRepository hostsRepository = Mock()

    @Subject
    ComplexMeasurementService complexMeasurementService

    def "should save complex measurement"() {
        given:
        def hostEntity = new HostEntity()
        def complexMeasurementEntity = createComplexMeasurementEntity(hostEntity)
        hostsRepository.findBySensorId(SENSOR_ID) >> [hostEntity]
        complexMeasurementsRepository.save(_) >> complexMeasurementEntity
        complexMeasurementConverter.toComplexMeasurement(_) >> new ComplexMeasurementEntity()

        when:
        def result = complexMeasurementService.saveComplexMeasurement(SENSOR_ID, TIME_WINDOW, CALCULATION_FREQUENCY, USER_LOGIN)

        then:
        result
    }

    def "should throw exception when no host with given sensor id"() {
        given:
        hostsRepository.findBySensorId(SENSOR_ID) >> []

        when:
        complexMeasurementService.saveComplexMeasurement(SENSOR_ID, TIME_WINDOW, CALCULATION_FREQUENCY, USER_LOGIN)

        then:
        thrown BadRequestException
        0 * complexMeasurementsRepository.save(_)
        0 * complexMeasurementConverter.toComplexMeasurement(_)
    }

    private ComplexMeasurementEntity createComplexMeasurementEntity(HostEntity hostEntity) {
        new ComplexMeasurementEntity(ID, USER_LOGIN, hostEntity, TIME_WINDOW, CALCULATION_FREQUENCY)
    }
}
