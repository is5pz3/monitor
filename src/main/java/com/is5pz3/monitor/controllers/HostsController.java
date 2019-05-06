package com.is5pz3.monitor.controllers;

import com.is5pz3.monitor.model.data.Host;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hosts")
public class HostsController {

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<Host> addHost(@RequestBody Host host) {
        return ResponseEntity.ok(host);
    }

}
