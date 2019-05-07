package com.is5pz3.monitor.services

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.exceptions.BadRequestException
import com.is5pz3.monitor.model.converters.HostConverter
import com.is5pz3.monitor.model.data.Host
import com.is5pz3.monitor.model.entities.HostEntity
import com.is5pz3.monitor.repositories.HostsRepository
import spock.lang.Specification

class HostsServiceTest extends Specification {
    private static final String HOST_ID = "hostId"
    private static final String SENSOR_ID = "sensorId"
    private static final String HOST_NAME = "hostName"
    private static final String PLATFORM = "platform"
    private static final String METRIC = "metric"
    private static final String UNIT = "unit"

    @Collaborator
    HostsRepository hostsRepository = Mock()

    @Collaborator
    HostConverter hostConverter = Mock()

    @Subject
    HostsService hostsService

    private static def host
    private static def hostEntity

    def setupSpec() {
        host = new Host(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT)
        hostEntity = new HostEntity(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT)
    }

    def "should save host"() {
        given:
        hostsRepository.findBySensorId(SENSOR_ID) >> []
        hostsRepository.findByHostNameAndPlatformAndMetricAndUnit(HOST_NAME, PLATFORM, METRIC, UNIT) >> []

        when:
        hostsService.saveHost(host)

        then:
        1 * hostConverter.toHostEntity(host) >> hostEntity
        1 * hostsRepository.save(hostEntity)
    }

    def "should throw exception if host with given sensor id already exists"() {
        given:
        hostsRepository.findBySensorId(SENSOR_ID) >> [hostEntity]

        when:
        hostsService.saveHost(host)

        then:
        thrown BadRequestException
        0 * hostConverter.toHostEntity(host) >> hostEntity
        0 * hostsRepository.save(hostEntity)
    }

    def "should throw exception if host with given sensor parameters already exists"() {
        given:
        hostsRepository.findBySensorId(SENSOR_ID) >> []
        hostsRepository.findByHostNameAndPlatformAndMetricAndUnit(HOST_NAME, PLATFORM, METRIC, UNIT) >> [hostEntity]

        when:
        hostsService.saveHost(host)

        then:
        thrown BadRequestException
        0 * hostConverter.toHostEntity(host) >> hostEntity
        0 * hostsRepository.save(hostEntity)
    }

    def "should get list of hosts by host name"() {
        when:
        def result = hostsService.getHostsByHostName(HOST_NAME)

        then:
        1 * hostsRepository.findByHostNameContaining(HOST_NAME) >> [hostEntity]
        1 * hostConverter.toHosts([hostEntity]) >> [host]
        assert result == [host]
    }

    def "should get list of all hosts "() {
        when:
        def result = hostsService.getAllHosts()

        then:
        1 * hostsRepository.findAll() >> [hostEntity]
        1 * hostConverter.toHosts([hostEntity]) >> [host]
        assert result == [host]
    }

}
