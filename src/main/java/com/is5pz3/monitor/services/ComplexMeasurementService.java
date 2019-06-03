package com.is5pz3.monitor.services;

import com.is5pz3.monitor.exceptions.BadRequestException;
import com.is5pz3.monitor.model.converters.ComplexMeasurementConverter;
import com.is5pz3.monitor.model.data.ComplexMeasurement;
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity;
import com.is5pz3.monitor.model.entities.HostEntity;
import com.is5pz3.monitor.repositories.ComplexMeasurementsRepository;
import com.is5pz3.monitor.repositories.HostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ComplexMeasurementService {

    @Autowired
    HostsRepository hostsRepository;

    @Autowired
    ComplexMeasurementsRepository complexMeasurementsRepository;

    @Autowired
    ComplexMeasurementConverter complexMeasurementConverter;

    public ComplexMeasurement saveComplexMeasurement(String sensorId, Integer timeWindow, Integer calculationFrequency, String userLogin) {
        return getHostBySensorId(sensorId).map(hostEntity ->
                complexMeasurementsRepository.save(buildComplexMeasurementEntity(hostEntity, timeWindow, calculationFrequency, userLogin)))
                .map(complexMeasurementConverter::toComplexMeasurement)
                .orElseThrow(() -> getBadRequestException(sensorId));
    }

    private ComplexMeasurementEntity buildComplexMeasurementEntity(HostEntity hostEntity, Integer timeWindow, Integer calculationFrequency, String userLogin) {
        return ComplexMeasurementEntity.builder()
                .hostEntity(hostEntity)
                .userLogin(userLogin)
                .timeWindow(timeWindow)
                .calculationFrequency(calculationFrequency)
                .build();
    }

    private Optional<HostEntity> getHostBySensorId(String sensorId) {
        return hostsRepository.findBySensorId(sensorId).stream()
                .findFirst();
    }

    private BadRequestException getBadRequestException(String sensorId) {
        return new BadRequestException("No host with sensor id: " + sensorId);
    }

}
