package com.is5pz3.monitor.controllers

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.is5pz3.monitor.model.data.Host
import com.is5pz3.monitor.services.HostsService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class HostsControllerTest extends Specification {
    private static final String HOST_ID = "hostId"
    private static final String SENSOR_ID = "sensorId"
    private static final String HOST_NAME = "hostName"
    private static final String PLATFORM = "platform"
    private static final String METRIC = "metric"
    private static final String UNIT = "unit"

    @Collaborator
    HostsService hostsService = Mock()

    @Subject
    HostsController hostsController

    def "should save host" () {
        given:
        def host = new Host(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT)
        hostsService.saveHost(host) >> host

        when:
        def result = hostsController.addHost(host)

        then:
        assert result.statusCode == HttpStatus.CREATED
        assert result.body == host
    }

    def "should get hosts by host name" () {
        given:
        def host = new Host(HOST_ID, SENSOR_ID, HOST_NAME, PLATFORM, METRIC, UNIT)
        hostsService.getHostsByHostName(HOST_NAME) >> [host]

        when:
        def result = hostsController.getHostsByHostName(HOST_NAME)

        then:
        assert result.statusCode == HttpStatus.OK
        assert result.body == [host]
    }

}
