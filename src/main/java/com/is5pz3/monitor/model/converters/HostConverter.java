package com.is5pz3.monitor.model.converters;

import com.is5pz3.monitor.model.data.Host;
import com.is5pz3.monitor.model.entities.HostEntity;
import com.is5pz3.monitor.model.validators.HostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostConverter {

    @Autowired
    private HostValidator hostValidator;

    public HostEntity toHostEntity(Host host) {
        return HostEntity.builder()
                .sensorId(hostValidator.validateSensorId(host.getSensorId()))
                .hostName(hostValidator.validateHostName(host.getHostName()))
                .platform(hostValidator.validatePlatform(host.getPlatform()))
                .metric(hostValidator.validateMetric(host.getMetric()))
                .unit(hostValidator.validateUnit(host.getUnit()))
                .build();
    }

    public Host toHost(HostEntity hostEntity) {
        return Host.builder()
                .id(hostEntity.getId())
                .sensorId(hostEntity.getSensorId())
                .hostName(hostEntity.getHostName())
                .platform(hostEntity.getPlatform())
                .metric(hostEntity.getMetric())
                .unit(hostEntity.getUnit())
                .build();
    }

    public List<Host> toHosts(List<HostEntity> hostEntities) {
        return hostEntities.stream()
                .map(this::toHost)
                .collect(Collectors.toList());
    }
}
