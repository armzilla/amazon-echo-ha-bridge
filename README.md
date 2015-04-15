# amazon-echo-ha-bridge
emulates philips hue api to other home automation gateways.  The Amazon echo now supports wemo and philip hue... great news if you own any of those devices!
My house is pretty heavily invested in the z-wave using the Vera as the gateway and thought it would be nice bridge the Amazon Echo to it.

Register a device, bind to some sort of on/off (vera style) url
```
POST http://host:8080/api/devices
{
"name" : "bedroom light",
"deviceType" : "switch",
  "onUrl" : "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=1&DeviceNum=41",
  "offUrl" : "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum=41"
}
```

To run using maven
```
mvn spring-boot:run
```

somewhat hacked together for now, please excuse the hard coded values
