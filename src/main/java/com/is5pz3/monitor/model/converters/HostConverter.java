package com.is5pz3.monitor.model.converters;

import com.is5pz3.monitor.model.data.Host;
import com.is5pz3.monitor.model.entities.HostEntity;
import com.is5pz3.monitor.model.validators.HostValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class HostConverter {

    @Autowired
    HostValidator hostValidator;

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
                .sensorId(hostEntity.getSensorId())
                .hostName(hostEntity.getHostName())
                .platform(hostEntity.getPlatform())
                .metric(hostEntity.getMetric())
                .unit(hostEntity.getUnit())
                .build();
    }

}
