package com.is5pz3.monitor.services;

import com.is5pz3.monitor.exceptions.BadRequestException;
import com.is5pz3.monitor.exceptions.UnauthorizedException;
import com.is5pz3.monitor.model.converters.ComplexMeasurementConverter;
import com.is5pz3.monitor.model.converters.MeasurementConverter;
import com.is5pz3.monitor.model.data.ComplexMeasurement;
import com.is5pz3.monitor.model.data.Measurement;
import com.is5pz3.monitor.model.data.MeasurementWrapper;
import com.is5pz3.monitor.model.entities.ComplexMeasurementEntity;
import com.is5pz3.monitor.model.entities.HostEntity;
import com.is5pz3.monitor.repositories.ComplexMeasurementsRepository;
import com.is5pz3.monitor.repositories.HostsRepository;
import com.is5pz3.monitor.repositories.MeasurementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementsService {

    @Autowired
    private MeasurementsRepository measurementsRepository;

    @Autowired
    private HostsRepository hostsRepository;

    @Autowired
    private MeasurementConverter measurementConverter;

    @Autowired
    private ComplexMeasurementsRepository complexMeasurementsRepository;

    @Autowired
    private ComplexMeasurementConverter complexMeasurementConverter;

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

    public ComplexMeasurement saveComplexMeasurement(String sensorId, Integer timeWindow, Integer calculationFrequency, String userLogin) {
        if (getComplexMeasurement(sensorId).isPresent()) {
            throw new BadRequestException("There is already complex measurement for sensorId: " + sensorId);
        }

        return getHostBySensorId(sensorId).map(hostEntity ->
                complexMeasurementsRepository.save(buildComplexMeasurementEntity(hostEntity, timeWindow, calculationFrequency, userLogin)))
                .map(complexMeasurementConverter::toComplexMeasurement)
                .orElseThrow(() -> getBadRequestException(sensorId));
    }

    public void removeComplexMeasurement(String sensorId, String userLogin) {
        Optional<ComplexMeasurementEntity> maybeComplexMeasurement = getComplexMeasurement(sensorId);

        if (maybeComplexMeasurement.isEmpty()) {
            throw new BadRequestException("There is no complex measurement for sensorId: " + sensorId);
        }

        ComplexMeasurementEntity complexMeasurement = maybeComplexMeasurement.get();

        if(!complexMeasurement.getUserLogin().equals(userLogin)) {
            throw new UnauthorizedException("Can't remove this complex measurement. It was created by different user.");
        }

        complexMeasurementsRepository.deleteById(complexMeasurement.getId());
    }

    public MeasurementWrapper getMeasurementsBySensorId(String sensorId, Integer limit, Long since, Long to) {
        if (getHostBySensorId(sensorId).isEmpty()) {
            throw getBadRequestException(sensorId);
        }

        List<Measurement> measurements = getMeasurements(sensorId, limit, since, to);

        MeasurementWrapper.MeasurementWrapperBuilder measurementWrapperBuilder = MeasurementWrapper.builder()
                .measurements(measurements);
        Optional<ComplexMeasurementEntity> complexMeasurementMaybe = getComplexMeasurement(sensorId);

        if (complexMeasurementMaybe.isPresent()) {
            ComplexMeasurementEntity complexMeasurement = complexMeasurementMaybe.get();

            List<Measurement> measurementsForComplexAnalysis = getMeasurementsForAnalysis(sensorId, to, complexMeasurement);

            List<Measurement> complexMeasurements = getComplexMeasurements(to, complexMeasurement, measurementsForComplexAnalysis)
                    .stream().filter(measurement -> isInBetween(getSince(since), getTo(to), measurement))
                    .sorted(Comparator.comparing(Measurement::getTimestamp).reversed())
                    .limit(limit == null ? 10 : limit)
                    .collect(Collectors.toList());

            measurementWrapperBuilder.complexMeasurement(ComplexMeasurement.builder()
                    .id(complexMeasurement.getId())
                    .measurements(complexMeasurements)
                    .build());
        }
        return measurementWrapperBuilder.build();
    }

    private List<Measurement> getComplexMeasurements(Long to, ComplexMeasurementEntity complexMeasurement, List<Measurement> measurementsForComplexAnalysis) {
        List<Measurement> complexMeasurements = new LinkedList<>();
        long analysisEndDate = to != null ? to : new Date().getTime() / 1000;
        for (long startDate = complexMeasurement.getTimestamp() - 60 * complexMeasurement.getTimeWindow(); startDate <= analysisEndDate; startDate += 60 * complexMeasurement.getCalculationFrequency()) {
            long windowStartDate = startDate;
            long windowEndDate = startDate + 60 * complexMeasurement.getTimeWindow();
            OptionalDouble average = measurementsForComplexAnalysis.stream()
                    .filter(measurement -> isInBetween(windowStartDate, windowEndDate, measurement))
                    .map(Measurement::getValue)
                    .mapToDouble(a -> a)
                    .average();

            complexMeasurements.add(new Measurement(windowEndDate, average.isPresent() ? average.getAsDouble() : 0));
        }
        return complexMeasurements;
    }

    private List<Measurement> getMeasurementsForAnalysis(String sensorId, Long to, ComplexMeasurementEntity complexMeasurement) {
        return measurementConverter.toMeasurements(
                measurementsRepository.findByHostEntitySensorIdAndTimestampBetween(sensorId, complexMeasurement.getTimestamp() - 60 * complexMeasurement.getTimeWindow(), getTo(to))).stream()
                .sorted(Comparator.comparing(Measurement::getTimestamp))
                .collect(Collectors.toList());
    }

    private List<Measurement> getMeasurements(String sensorId, Integer limit, Long since, Long to) {
        return measurementConverter.toMeasurements(measurementsRepository.findByHostEntitySensorIdAndTimestampBetween(sensorId, getSince(since), getTo(to))).stream()
                .sorted(Comparator.comparing(Measurement::getTimestamp).reversed())
                .limit(limit == null ? 10 : limit)
                .collect(Collectors.toList());
    }

    private boolean isInBetween(long startDate, long endDate, Measurement measurement) {
        return measurement.getTimestamp() >= startDate && measurement.getTimestamp() < endDate;
    }

    private ComplexMeasurementEntity buildComplexMeasurementEntity(HostEntity hostEntity, Integer timeWindow, Integer calculationFrequency, String userLogin) {
        return ComplexMeasurementEntity.builder()
                .hostEntity(hostEntity)
                .userLogin(userLogin)
                .timeWindow(timeWindow)
                .calculationFrequency(calculationFrequency)
                .timestamp(new Date().getTime() / 1000)
                .build();
    }

    private Long getSince(Long since) {
        return since != null ? since : 0;
    }

    private Long getTo(Long to) {
        return to != null ? to : Long.MAX_VALUE;
    }

    private BadRequestException getBadRequestException(String sensorId) {
        return new BadRequestException("No host with sensor id: " + sensorId);
    }

    private Optional<ComplexMeasurementEntity> getComplexMeasurement(String sensorId) {
        return complexMeasurementsRepository.findByHostEntitySensorId(sensorId).stream()
                .findFirst();
    }

    private Optional<HostEntity> getHostBySensorId(String sensorId) {
        return hostsRepository.findBySensorId(sensorId).stream()
                .findFirst();
    }
}
