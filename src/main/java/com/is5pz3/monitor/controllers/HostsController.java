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
@RequestMapping("hosts")
public class HostsController {

    @Autowired
    private HostsService hostsService;

    @Autowired
    private MeasurementsService measurementsService;

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Host> addHost(@RequestBody Host host) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hostsService.saveHost(host));
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Host>> getHostsByHostName(@RequestParam String hostName) {
        return ResponseEntity.status(HttpStatus.OK).body(hostsService.getHostsByHostName(hostName));
    }

    @RequestMapping(path = "/{sensorId}", method = RequestMethod.POST)
    public ResponseEntity<Measurement> addMeasurement(@PathVariable String sensorId, @RequestBody Measurement measurement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(measurementsService.saveMeasurement(measurement, sensorId));
    }

    @RequestMapping(path = "/{sensorId}/bulk", method = RequestMethod.POST)
    public ResponseEntity<List<Measurement>> addMeasurements(@PathVariable String sensorId, @RequestBody List<Measurement> measurements) {
        return ResponseEntity.status(HttpStatus.CREATED).body(measurementsService.saveMeasurements(measurements, sensorId));
    }

}
