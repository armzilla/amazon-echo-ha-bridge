# Amazon Echo Bridge &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ![codeship status](https://codeship.com/projects/998e16f0-ca03-0132-6689-76c03995407a/status?branch=master)

Amazon Echo Bridge allows you to quickly emulate a Phillips Hue bridge, bringing the ability to seamlessly integrate an Amazon Echo into various home automation systems.  

Also, with an easy to use POST/PUT REST API, it's never been easier before to get your devices up and running with the Amazon Echo with your own embedded applications!

## Quick Start

There are currently three different ways to run the pre-built jar file:

**Java -** ```java -jar amazon-echo-bridge-*.jar```

**Maven -** ```mvn spring-boot:run```

**Docker -** ```docker build -t amazon-echo-ha-bridge .
docker run -ti --rm --net=host amazon-echo-ha-bridge```

 Additionally, it's also recommended you pass the command line arguments ```--upnp.config.address``` and ```--server.port``` to override the hardcoded values currently implemented.

**Examples:**
```--upnp.config.address=192.168.1.240 --server.port=8081```

After the application is started and running, you can access the configurator by accessing http://YOURIP:PORT/configurator.html. 

Input your devices using the form at the bottom of the page, add command URLs to parse (useful if you use a system like OpenHAB), and save.

Instruct your Amazon Echo to learn about your devices by saying "Alexa, discover my devices" and your all set!

## Using

You can now control devices with your Amazon Echo by saying "Alexa, Turn on the office light" or other names you have given your configured devices.

To view or remove devices that Alexa knows about, you can use the mobile app Menu / Settings / Connected Home, This is needed if you remove a device from the Amazon Echo Bridge.

## Build

In case you would like to internally configure your own build of the Amazon Echo Bridge, a few requisites are required.

### Install Maven: 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Ubuntu/Linux** - ```sudo apt-get install maven```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
**OS X** - Install [Homebrew](http://brew.sh/) and run ```brew install maven```

### Make Changes:
For instance, the server defaults to running on port 8080. If you're already running a server (like openHAB) on 8080, you could edit ```server.port``` in ```src/main/resources/application.properties``` to your desired port before building the jar. 

Alternatively you could also pass in a command line argument to override ```server.port```.

### Compile:
To build the jar file yourself, make your changes and simply run Maven like this:
```
mvn install
```

Then locate the jar and start the server using the instructions above. By default maven will put the jar file in the target directory. ```java -jar target/amazon-echo-bridge-*.jar``` 

## POST/PUT REST API

Along with registering devices via the Configurator page, you can also push device information via REST to the Amazon Echo Bridge like so:
```
POST http://host:port/api/devices
{
"name" : "bedroom light",
"deviceType" : "switch",
  "onUrl" : "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=1&DeviceNum=41",
  "offUrl" : "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum=41"
}
```

Dimming is also supported by using the expression ```${intensity.percent}``` with a value of 0-100 or ```${intensity.byte}``` with a value 0-255, respectively.

**Example:**

```
{
    "name": "entry light",
    "deviceType": "switch",
    "offUrl": "http://192.168.1.201:3480/data_request?id=action&output_format=json&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=0&DeviceNum=31",
    "onUrl": "http://192.168.1.201:3480/data_request?id=action&output_format=json&DeviceNum=31&serviceId=urn:upnp-org:serviceId:Dimming1&action=SetLoadLevelTarget&newLoadlevelTarget=${intensity.percent}"
}
```

You can also push additional optional fields, such as:

 * contentType, which currently isn't validated

 * httpVerb, Only POST/PUT/GET supported

 * contentBody: Your POST/PUT body

Like so:
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
