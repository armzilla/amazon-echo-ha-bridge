package com.armzilla.ha.hue;

import com.armzilla.ha.api.hue.DeviceResponse;
import com.armzilla.ha.api.hue.HueApiResponse;
import com.armzilla.ha.dao.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arm on 4/12/15.
 */
@Controller
@RequestMapping("/api")
public class HueMulator {
    private static final Logger log = Logger.getLogger(HueMulator.class);
    protected static RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private DeviceRepository repository;


    @RequestMapping(value = "/{userId}/lights", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, String>> getUpnpConfiguration(@PathVariable(value = "userId") String userId, HttpServletRequest request) {
        log.info("hue lights list requested: " + userId + " from " + request.getRemoteAddr());
        List<DeviceDescriptor> deviceList = repository.findByDeviceType("switch");
        Map<String, String> deviceResponseMap = new HashMap<>();
        for (DeviceDescriptor device : deviceList) {
            deviceResponseMap.put(device.getId(), device.getName());
        }
        return new ResponseEntity<>(deviceResponseMap, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<HueApiResponse> getApi(@PathVariable(value = "userId") String userId, HttpServletRequest request) {
        log.info("hue api root requested: " + userId + " from " + request.getRemoteAddr());
        List<DeviceDescriptor> descriptorList = repository.findByDeviceType("switch");
        if (descriptorList == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        Map<String, DeviceResponse> deviceList = new HashMap<>();

        descriptorList.forEach(descriptor -> {
                    DeviceResponse deviceResponse = DeviceResponse.createResponse(descriptor.getName(), descriptor.getId());
                    deviceList.put(descriptor.getId(), deviceResponse);
                }
        );
        HueApiResponse apiResponse = new HueApiResponse();
        apiResponse.setLights(deviceList);

        HttpHeaders headerMap = new HttpHeaders();
        headerMap.set("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        headerMap.set("Pragma", "no-cache");
        headerMap.set("Connection", "close");
        headerMap.set("Access-Control-Max-Age", "0");
        headerMap.set("Access-Control-Allow-Origin", "*");
        headerMap.set("Access-Control-Allow-Credentials", "true");
        headerMap.set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        headerMap.set(" Access-Control-Allow-Headers", "Content-Type");

        ResponseEntity<HueApiResponse> entity = new ResponseEntity<>(apiResponse, headerMap, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/{userId}/lights/{lightId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<DeviceResponse> getLigth(@PathVariable(value = "lightId") String lightId, @PathVariable(value = "userId") String userId, HttpServletRequest request) {
        log.info("hue light requested: " + lightId + " from " + request.getRemoteAddr());
        DeviceDescriptor device = repository.findOne(lightId);
        if (device == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        } else {
            log.info("found device named: " + device.getName());
        }
        DeviceResponse lightResponse = DeviceResponse.createResponse(device.getName(), device.getId());

        HttpHeaders headerMap = new HttpHeaders();
        headerMap.set("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        headerMap.set("Pragma", "no-cache");
        headerMap.set("Connection", "close");
        headerMap.set("Access-Control-Max-Age", "0");
        headerMap.set("Access-Control-Allow-Origin", "*");
        headerMap.set("Access-Control-Allow-Credentials", "true");
        headerMap.set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        headerMap.set("Access-Control-Allow-Headers", "Content-Type");

        ResponseEntity<DeviceResponse> entity = new ResponseEntity<>(lightResponse, headerMap, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/{userId}/lights/{lightId}/state", method = RequestMethod.PUT)
    public ResponseEntity<String> stateChange(@PathVariable(value = "lightId") String lightId, @PathVariable(value = "userId") String userId, HttpServletRequest request, @RequestBody String body) {
        log.info("hue state change requested: " + userId + " from " + request.getRemoteAddr());
        log.info("hue stage change body: " + body);
        String setting;
        if (body.contains("true")) {
            setting = "[{\"success\":{\"/lights/" + lightId + "/state/on\":true}}]";
        } else {
            setting = "[{\"success\":{\"/lights/" + lightId + "/state/on\":false}}]";
        }
        DeviceDescriptor device = repository.findOne(lightId);
        if (device == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }

        String url;
        if (body.contains("true")) {
            url = device.getOnUrl();
        } else {
            url = device.getOffUrl();
        }

        String response = restTemplate.getForObject(url, String.class);

        HttpHeaders headerMap = new HttpHeaders();
        headerMap.set("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        headerMap.set("Expires", "Mon, 1 Aug 2011 09:00:00 GMT");
        headerMap.set("Pragma", "no-cache");
        headerMap.set("Connection", "close");
        headerMap.set("Access-Control-Max-Age", "3600");
        headerMap.set("Access-Control-Allow-Origin", "*");
        headerMap.set("Access-Control-Allow-Credentials", "true");
        headerMap.set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        headerMap.set("Access-Control-Allow-Headers", "Content-Type");
        headerMap.remove("Date");
        headerMap.set("Content-Type", "application/json");


        ResponseEntity<String> entity = new ResponseEntity<>(setting, headerMap, HttpStatus.OK);
        return entity;
    }
}
