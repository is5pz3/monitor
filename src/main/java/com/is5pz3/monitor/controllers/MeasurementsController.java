package com.is5pz3.monitor.controllers;

import com.is5pz3.monitor.model.data.Host;
import com.is5pz3.monitor.model.data.Measurement;
import com.is5pz3.monitor.services.HostsService;
import com.is5pz3.monitor.services.MeasurementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("measurements")
public class MeasurementsController {

    @Autowired
    private HostsService hostsService;

    @Autowired
    private MeasurementsService measurementsService;

    @RequestMapping(path = "/{sensorId}", method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getMeasurementsBySensorId(@PathVariable String sensorId, @RequestParam(required=false) Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).body(measurementsService.getMeasurementsBySensorId(sensorId, limit));
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Host>> getAllHosts() {
        return ResponseEntity.status(HttpStatus.OK).body(hostsService.getAllHosts());
    }
}
