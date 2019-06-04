package com.is5pz3.monitor.services

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.exceptions.BadRequestException
import com.is5pz3.monitor.model.converters.MeasurementConverter
import com.is5pz3.monitor.model.data.Measurement
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity
import com.is5pz3.monitor.model.entities.HostEntity
import com.is5pz3.monitor.model.entities.MeasurementEntity
import com.is5pz3.monitor.repositories.ComplexMeasurementsRepository
import com.is5pz3.monitor.repositories.HostsRepository
import com.is5pz3.monitor.repositories.MeasurementsRepository
import spock.lang.Specification

class MeasurementsServiceTest extends Specification {
    private static final String SENSOR_ID = "sensorId"
    private static final BigDecimal VALUE = 10.0
    private static final BigDecimal VALUE2 = 20.0
    private static final BigDecimal VALUE3 = 30.0
    private static final long TIMESTAMP = 1L
    private static final long TIMESTAMP2 = 2L
    private static final long TIMESTAMP3 = 3L

    @Collaborator
    MeasurementsRepository measurementsRepository = Mock()

    @Collaborator
    ComplexMeasurementsRepository complexMeasurementsRepository = Mock()

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

    def "should get measurements by sensor id"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurement2 = new Measurement(TIMESTAMP2, VALUE2)
        def measurementEntity1 = new MeasurementEntity(timestamp: TIMESTAMP, value: VALUE)
        def measurementEntity2 = new MeasurementEntity(timestamp: TIMESTAMP2, value: VALUE2)
        def host = new HostEntity(sensorId: SENSOR_ID)
        hostsRepository.findBySensorId(SENSOR_ID) >> [host]
        measurementConverter.toMeasurements([measurementEntity1, measurementEntity2]) >> [measurement1, measurement2]
        complexMeasurementsRepository.findByHostEntitySensorId(SENSOR_ID) >> []

        when:
        def result = measurementsService.getMeasurementsBySensorId(SENSOR_ID, null, null, null)

        then:
        1 * measurementsRepository.findByHostEntitySensorIdAndTimestampBetween(SENSOR_ID, 0, Long.MAX_VALUE) >> [measurementEntity1, measurementEntity2]
        result.measurements.size() == 2
        result.complexMeasurement == null
        result.measurements.containsAll([measurement1, measurement2])
    }

    def "should get measurements by sensor id with limit"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurementEntity1 = new MeasurementEntity(timestamp: TIMESTAMP, value: VALUE)
        def measurementEntity2 = new MeasurementEntity(timestamp: TIMESTAMP2, value: VALUE2)
        def host = new HostEntity(sensorId: SENSOR_ID)
        hostsRepository.findBySensorId(SENSOR_ID) >> [host]
        measurementConverter.toMeasurements([measurementEntity1, measurementEntity2]) >> [measurement1]
        complexMeasurementsRepository.findByHostEntitySensorId(SENSOR_ID) >> []

        when:
        def result = measurementsService.getMeasurementsBySensorId(SENSOR_ID, 1, null, null)

        then:
        1 * measurementsRepository.findByHostEntitySensorIdAndTimestampBetween(SENSOR_ID, 0, Long.MAX_VALUE) >> [measurementEntity1, measurementEntity2]
        result.measurements.size() == 1
        result.complexMeasurement == null
        result.measurements.containsAll([measurement1])
    }

    def "should get measurements by sensor id with limit and date constrains"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurementEntity1 = new MeasurementEntity(timestamp: TIMESTAMP, value: VALUE)
        def host = new HostEntity(sensorId: SENSOR_ID)
        hostsRepository.findBySensorId(SENSOR_ID) >> [host]
        measurementConverter.toMeasurements([measurementEntity1]) >> [measurement1]
        complexMeasurementsRepository.findByHostEntitySensorId(SENSOR_ID) >> []

        when:
        def result = measurementsService.getMeasurementsBySensorId(SENSOR_ID, 2, 1, 2)

        then:
        1 * measurementsRepository.findByHostEntitySensorIdAndTimestampBetween(SENSOR_ID, 1, 2) >> [measurementEntity1]
        result.measurements.size() == 1
        result.complexMeasurement == null
        result.measurements.containsAll([measurement1])
    }

    def "should get measurements by sensor id with complex measurements"() {
        given:
        def measurement1 = new Measurement(TIMESTAMP, VALUE)
        def measurement2 = new Measurement(TIMESTAMP2, VALUE2)
        def measurement3 = new Measurement(TIMESTAMP3, VALUE3)
        def measurementEntity1 = new MeasurementEntity(timestamp: TIMESTAMP, value: VALUE)
        def measurementEntity2 = new MeasurementEntity(timestamp: TIMESTAMP2, value: VALUE2)
        def measurementEntity3 = new MeasurementEntity(timestamp: TIMESTAMP3, value: VALUE3)
        def complexMeasurement = new ComplexMeasurementEntity(timeWindow: 4, calculationFrequency: 2, timestamp: 5)
        def host = new HostEntity(sensorId: SENSOR_ID)
        hostsRepository.findBySensorId(SENSOR_ID) >> [host]
        measurementConverter.toMeasurements([measurementEntity1, measurementEntity2, measurementEntity3]) >> [measurement1, measurement2, measurement3]
        complexMeasurementsRepository.findByHostEntitySensorId(SENSOR_ID) >> [complexMeasurement]

        when:
        def result = measurementsService.getMeasurementsBySensorId(SENSOR_ID, null, null, null)

        then:
        2 * measurementsRepository.findByHostEntitySensorIdAndTimestampBetween(SENSOR_ID, *_) >> [measurementEntity1, measurementEntity2, measurementEntity3]
        result.measurements.size() == 3
        result.complexMeasurement.measurements.size() == 10
        result.measurements.containsAll([measurement1, measurement2, measurement3])
    }

}
