package com.armzilla.ha.devicemanagmeent;

import com.armzilla.ha.api.Device;
import com.armzilla.ha.dao.DeviceDescriptor;
import com.armzilla.ha.dao.DeviceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by arm on 4/13/15.
 */
@Controller
@RequestMapping("/api/devices")
public class DeviceResource {
    Log log = LogFactory.getLog(DeviceResource.class);

    @Autowired
    private DeviceRepository deviceRepository;


    @Value("${vera.ip.address}")
    private String veraIP;


    @PostConstruct
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json", value = "/autoconfigureFromVera")
    public List<DeviceDescriptor> autoconfigureFromVera() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(MessageFormat.format("http://{0}:3480/data_request?id=user_data&output_format=json", this.veraIP));
        ObjectMapper mapper = new ObjectMapper();
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            JsonNode json = mapper.readTree(response1.getEntity().getContent());
            JsonNode devices = json.get("devices");
            for (JsonNode deviceNode : devices) {
                String name = deviceNode.get("name").asText();
                String deviceType = deviceNode.get("device_type").asText();
                if (deviceType.equals("urn:schemas-upnp-org:device:BinaryLight:1")) {
                    log.debug("Adding:"+name);
                    DeviceDescriptor deviceEntry = new DeviceDescriptor();
                    String deviceId = deviceNode.get("id").asText();
                    deviceEntry.setId(deviceId);
                    deviceEntry.setName(name);
                    deviceEntry.setDeviceType("switch");
                    deviceEntry.setOnUrl(MessageFormat.format("http://{0}:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=1&DeviceNum={1}", veraIP, deviceId));
                    deviceEntry.setOffUrl(MessageFormat.format("http://{0}:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum={1}", veraIP, deviceId));
                    deviceRepository.save(deviceEntry);
                }
            }
        }
        return deviceRepository.findAll();
    }


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

    @RequestMapping(value = "/{lightId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<DeviceDescriptor> findByDevicId(@PathVariable("lightId") String id) {
        DeviceDescriptor descriptor = deviceRepository.findOne(id);
        if (descriptor == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(descriptor, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{lightId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteDeviceById(@PathVariable("lightId") String id) {
        DeviceDescriptor deleted = deviceRepository.findOne(id);
        if (deleted == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        deviceRepository.delete(deleted);
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }


}
