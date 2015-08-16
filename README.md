![codeship status](https://codeship.com/projects/998e16f0-ca03-0132-6689-76c03995407a/status?branch=master)


# amazon-echo-ha-bridge
emulates philips hue api to other home automation gateways.  The Amazon echo now supports wemo and philip hue... great news if you own any of those devices!
My house is pretty heavily invested in the z-wave using the Vera as the gateway and thought it would be nice bridge the Amazon Echo to it.

Run (with docker)
-----------------
```bash
docker build -t amazon-echo-ha-bridge .
docker run -ti --rm --net=host amazon-echo-ha-bridge
```

Run
---
To run the pre-built jar using maven:
```
mvn spring-boot:run
```
It's somewhat hacked together for now, please excuse the hard coded values

Build
-----
The server defaults to running on port 8080. If you're already running a server (like openHAB) on 8080, edit ```server.port``` in ```src/main/resources/application.properties``` to your desired port before building the jar. Alternately you can pass in a command line argument to override ```server.port```.

To customize and build it yourself, build a new jar with maven:
```
mvn install
```
Then locate the jar and start the server with:
```
java -jar target/amazon-echo-bridge-0.X.Y.jar --upnp.config.address=192.168.1.Z
```
replace the --upnp.config.address value with the server ipv4 address.

Then configure by going to the /configurator.html url 
```
http://192.168.1.240:8080/configurator.html
```

If using openHAB, use URLs of the form:
```
http://user:password@192.168.1.Z:8080/CMD?light_garage=ON
```

or Register a device, via REST by binding some sort of on/off (vera style) url
```
POST http://host:8080/api/devices
{
"name" : "bedroom light",
"deviceType" : "switch",
  "onUrl" : "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=1&DeviceNum=41",
  "offUrl" : "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum=41"
}
```

Dimming is also supported by using the expessions ${intensity.percent} or ${intensity.byte} for 0-100 and 0-255 respectively.  
e.g.
```
{
    "name": "entry light",
    "deviceType": "switch",
    "offUrl": "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum=31",
    "onUrl": "http://192.168.1.201:3480/data_request?id=action&output_format=json&DeviceNum=31&serviceId=urn:upnp-org:serviceId:Dimming1&action=SetLoadLevelTarget&newLoadlevelTarget=${intensity.percent}"
}
```
See the echo's documentation for the dimming phrase.

POST/PUT support
-----
added optinal fields
contentType (currently un-validated)
httpVerb (POST/PUT/GET only supported
contentBody your post/put body here
e.g: 
```
{
    "name": "test device",
    "deviceType": "switch",
    "offUrl": "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum=31",
    "onUrl": "http://192.168.1.201:3480/data_request?id=action&output_format=json&DeviceNum=31&serviceId=urn:upnp-org:serviceId:Dimming1&action=SetLoadLevelTarget&newLoadlevelTarget=${intensity.percent}",
  "contentType" : "application/json",
  "httpVerb":"POST",
  "contentBody" : "{\"fooBar\":\"baz\"}"
}
```

After this Tell Alexa: "Alexa, discover my devices"

Then you can say "Alexa, Turn on the office light" or whatever name you have given your configured devices.

To view or remove devices that Alexa knows about, you can use the mobile app Menu / Settings / Connected Home
