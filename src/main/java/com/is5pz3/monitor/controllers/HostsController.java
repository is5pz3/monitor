package com.is5pz3.monitor.controllers;

import com.is5pz3.monitor.model.data.Host;
import com.is5pz3.monitor.services.HostsService;
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

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Host> addHost(@RequestBody Host host) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hostsService.saveHost(host));
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Host>> getHostsByHostName(@RequestParam String hostName) {
        return ResponseEntity.status(HttpStatus.OK).body(hostsService.getHostsByHostName(hostName));
    }

}
