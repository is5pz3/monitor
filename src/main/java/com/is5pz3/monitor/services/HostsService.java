package com.is5pz3.monitor.services;

import com.is5pz3.monitor.exceptions.BadRequestException;
import com.is5pz3.monitor.model.converters.HostConverter;
import com.is5pz3.monitor.model.data.Host;
import com.is5pz3.monitor.repositories.HostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostsService {

    @Autowired
    private HostsRepository hostsRepository;

    @Autowired
    private HostConverter hostConverter;

    public Host saveHost(Host host) {
        validateIfHostForSensorDoesNotExists(host);
        return hostConverter.toHost(hostsRepository.save(hostConverter.toHostEntity(host)));
    }

    public List<Host> getHostsByHostName(String hostName) {
        return hostsRepository.findByHostNameContaining(hostName).stream()
                .map(hostConverter::toHost)
                .collect(Collectors.toList());
    }

    private void validateIfHostForSensorDoesNotExists(Host host) {
        if (!hostsRepository.findBySensorId(host.getSensorId()).isEmpty()) {
            throw new BadRequestException("Host for sensor with id: " + host.getSensorId() + " already exists.");
        }

        if (!hostsRepository.findByHostNameAndPlatformAndMetricAndUnit(host.getHostName(), host.getPlatform(), host.getMetric(), host.getUnit()).isEmpty()) {
            throw new BadRequestException("Host for sensor with these parameters already exists.");
        }
    }

}
