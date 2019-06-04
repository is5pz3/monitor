package com.is5pz3.monitor.controllers;

import com.is5pz3.monitor.exceptions.BadRequestException;
import com.is5pz3.monitor.model.data.ComplexMeasurement;
import com.is5pz3.monitor.model.data.Host;
import com.is5pz3.monitor.model.data.Measurement;
import com.is5pz3.monitor.model.data.MeasurementWrapper;
import com.is5pz3.monitor.services.AuthorizationService;
import com.is5pz3.monitor.services.HostsService;
import com.is5pz3.monitor.services.MeasurementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("measurements")
public class MeasurementsController {

    @Autowired
    private HostsService hostsService;

    @Autowired
    private MeasurementsService measurementsService;

    @Autowired
    AuthorizationService authorizationService;

    @RequestMapping(path = "/{sensorId}", method = RequestMethod.GET)
    public ResponseEntity<MeasurementWrapper> getMeasurementsBySensorId(@PathVariable String sensorId,
                                                                        @RequestParam(required = false) Integer data_count,
                                                                        @RequestParam(required = false) Long since,
                                                                        @RequestParam(required = false) Long to) {

        validateSinceToDates(since, to);
        return ResponseEntity.status(HttpStatus.OK).body(measurementsService.getMeasurementsBySensorId(sensorId, data_count, since, to));
    }

    private void validateSinceToDates(Long since, Long to) {
        if(to != null && since != null && to < since) {
            throw new BadRequestException("Since date must be before to date");
        }
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Host>> getAllHosts() {
        return ResponseEntity.status(HttpStatus.OK).body(hostsService.getAllHosts());
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<ComplexMeasurement> saveComplexMeasurement(@RequestParam @NonNull String sensorId,
                                                                     @RequestParam @NonNull Integer timeWindow,
                                                                     @RequestParam @NonNull Integer calculationFrequency,
                                                                     @RequestParam @NonNull String token) {
        return ResponseEntity.status(HttpStatus.OK).body(measurementsService.saveComplexMeasurement(sensorId,
                timeWindow, calculationFrequency, authorizationService.getUserLogin(token)));
    }

    @RequestMapping(path = "/{sensorId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteMeasurementsBySensorId(@PathVariable String sensorId, @RequestParam @NonNull String token) {

        measurementsService.removeComplexMeasurement(sensorId, authorizationService.getUserLogin(token));
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
