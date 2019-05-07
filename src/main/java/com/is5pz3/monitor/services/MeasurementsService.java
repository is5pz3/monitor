package com.is5pz3.monitor.services;

import com.is5pz3.monitor.exceptions.BadRequestException;
import com.is5pz3.monitor.model.converters.MeasurementConverter;
import com.is5pz3.monitor.model.data.Measurement;
import com.is5pz3.monitor.model.entities.HostEntity;
import com.is5pz3.monitor.repositories.HostsRepository;
import com.is5pz3.monitor.repositories.MeasurementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeasurementsService {

    @Autowired
    private MeasurementsRepository measurementsRepository;

    @Autowired
    private HostsRepository hostsRepository;

    @Autowired
    private MeasurementConverter measurementConverter;

    public Measurement saveMeasurement(Measurement measurement, String sensorId) {
        return getHostBySensorId(sensorId).map(hostEntity ->
                measurementsRepository.save(measurementConverter.toMeasurementEntity(measurement, hostEntity)))
                .map(measurementConverter::toMeasurement)
                .orElseThrow(() -> getBadRequestException(sensorId));
    }

    public List<Measurement> saveMeasurements(List<Measurement> measurements, String sensorId) {
        return getHostBySensorId(sensorId).map(hostEntity ->
                measurementsRepository.saveAll(measurementConverter.toMeasurementEntities(measurements, hostEntity)))
                .map(measurementConverter::toMeasurements)
                .orElseThrow(() -> getBadRequestException(sensorId));
    }

    public List<Measurement> getMeasurementsBySensorId(String sensorId, Integer limit) {
        if (getHostBySensorId(sensorId).isEmpty()) {
            throw getBadRequestException(sensorId);
        }
        return measurementConverter.toMeasurements(measurementsRepository.findByHostEntitySensorId(sensorId)).stream()
                .sorted(Comparator.comparing(Measurement::getTimestamp).reversed())
                .limit(limit == null ? 10 : limit)
                .collect(Collectors.toList());
    }

    private BadRequestException getBadRequestException(String sensorId) {
        return new BadRequestException("No host with sensor id: " + sensorId);
    }

    private Optional<HostEntity> getHostBySensorId(String sensorId) {
        return hostsRepository.findBySensorId(sensorId).stream()
                .findFirst();
    }
}
