package com.armzilla.ha.hue;

import com.armzilla.ha.dao.Device;
import com.armzilla.ha.dao.DeviceDescriptor;
import com.armzilla.ha.dao.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by arm on 4/13/15.
 */
@Controller
@RequestMapping("/api/devices")
public class DeviceResource {

    @Autowired
    private DeviceRepository deviceRepository;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", headers = "Accept=application/json")
    public ResponseEntity<DeviceDescriptor> createDevice(@RequestBody Device device) {
        DeviceDescriptor deviceEntry = new DeviceDescriptor();
        deviceEntry.setId(UUID.randomUUID().toString());
        deviceEntry.setName(device.getName());
        deviceEntry.setDeviceType(device.getDeviceType());
        deviceEntry.setOnUrl(device.getOnUrl());
        deviceEntry.setOffUrl(device.getOffUrl());

        deviceRepository.save(deviceEntry);

        return new ResponseEntity<>(deviceEntry, null, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<DeviceDescriptor>> findAllDevices() {

        List<DeviceDescriptor> deviceList = deviceRepository.findAll();
        List<DeviceDescriptor> plainList = new LinkedList<>(deviceList);
        return new ResponseEntity<>(plainList, null, HttpStatus.OK);
    }
}
