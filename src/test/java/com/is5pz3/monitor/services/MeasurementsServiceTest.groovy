package com.is5pz3.monitor.services

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.exceptions.BadRequestException
import com.is5pz3.monitor.model.converters.MeasurementConverter
import com.is5pz3.monitor.model.data.Measurement
import com.is5pz3.monitor.model.entities.HostEntity
import com.is5pz3.monitor.model.entities.MeasurementEntity
import com.is5pz3.monitor.repositories.HostsRepository
import com.is5pz3.monitor.repositories.MeasurementsRepository
import spock.lang.Specification

class MeasurementsServiceTest extends Specification {
    private static final String SENSOR_ID = "sensorId"
    private static final BigDecimal VALUE = 10.0
    private static final BigDecimal VALUE2 = 20.0
    private static final long TIMESTAMP = 123L
    private static final long TIMESTAMP2 = 456L

    @Collaborator
    MeasurementsRepository measurementsRepository = Mock()

    @Collaborator
    HostsRepository hostsRepository = Mock()

    @Collaborator
    MeasurementConverter measurementConverter = Mock()

    @Subject
    MeasurementsService measurementsService

    def "should save measurement"() {
        given:
        def measurement = new Measurement(TIMESTAMP, VALUE)
        def measurementEntity = new MeasurementEntity(timestamp: TIMESTAMP, value: VALUE)
        def host = new HostEntity(sensorId: SENSOR_ID)
        hostsRepository.findBySensorId(SENSOR_ID) >> [host]
        measurementConverter.toMeasurementEntity(measurement, host) >> measurementEntity
        measurementConverter.toMeasurement(measurementEntity) >> measurement

        when:
        def result = measurementsService.saveMeasurement(measurement, SENSOR_ID)

        then:
        1 * measurementsRepository.save(measurementEntity) >> measurementEntity
        assert result == measurement
    }

    def "should not save measurement when no host with given sensor id found"() {
        given:
        def measurement = new Measurement(TIMESTAMP, VALUE)
        hostsRepository.findBySensorId(SENSOR_ID) >> []

        when:
        measurementsService.saveMeasurement(measurement, SENSOR_ID)

        then:
        thrown BadRequestException
        0 * measurementsRepository.save(_)
    }

    def "should save measurements"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurement2 = new Measurement(TIMESTAMP2, VALUE2)
        def measurementEntity1 = new MeasurementEntity(timestamp: TIMESTAMP, value: VALUE)
        def measurementEntity2 = new MeasurementEntity(timestamp: TIMESTAMP2, value: VALUE2)
        def host = new HostEntity(sensorId: SENSOR_ID)
        hostsRepository.findBySensorId(SENSOR_ID) >> [host]
        measurementConverter.toMeasurementEntities([measurement1, measurement2], host) >> [measurementEntity1, measurementEntity2]
        measurementConverter.toMeasurements([measurementEntity1, measurementEntity2]) >> [measurement1, measurement2]

        when:
        def result = measurementsService.saveMeasurements([measurement1, measurement2], SENSOR_ID)

        then:
        1 * measurementsRepository.saveAll([measurementEntity1, measurementEntity2]) >> [measurementEntity1, measurementEntity2]
        assert result.containsAll([measurement1, measurement2])
    }

    def "should not save measurements when no host with given sensor id found"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurement2 = new Measurement(TIMESTAMP2, VALUE2)
        hostsRepository.findBySensorId(SENSOR_ID) >> []

        when:
        measurementsService.saveMeasurements([measurement1, measurement2], SENSOR_ID)

        then:
        thrown BadRequestException
        0 * measurementsRepository.saveAll(_)
    }

}
