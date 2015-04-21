package com.armzilla.ha.upnp;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by arm on 4/11/15.
 */
@Controller
@RequestMapping("/upnp")
public class UpnpSettingsResource {

    private Logger log = Logger.getLogger(UpnpSettingsResource.class);

        private String hueTemplate = "<?xml version=\"1.0\"?>\n" +
                "<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n" +
                "<specVersion>\n" +
                "<major>1</major>\n" +
                "<minor>0</minor>\n" +
                "</specVersion>\n" +
                "<URLBase>http://%s:%s/</URLBase>\n" + //hostname string
                "<device>\n" +
                "<deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType>\n" +
                "<friendlyName>Amazon-Echo-HA-Bridge (%s)</friendlyName>\n" +
                "<manufacturer>Royal Philips Electronics</manufacturer>\n" +
                "<manufacturerURL>http://www.armzilla..com</manufacturerURL>\n" +
                "<modelDescription>Hue Emulator for Amazon Echo bridge</modelDescription>\n" +
                "<modelName>Philips hue bridge 2012</modelName>\n" +
                "<modelNumber>929000226503</modelNumber>\n" +
                "<modelURL>http://www.armzilla.com/amazon-echo-ha-bridge</modelURL>\n" +
                "<serialNumber>01189998819991197253</serialNumber>\n" +
                "<UDN>uuid:88f6698f-2c83-4393-bd03-cd54a9f8595</UDN>\n" +
                "<serviceList>\n" +
                "<service>\n" +
                "<serviceType>(null)</serviceType>\n" +
                "<serviceId>(null)</serviceId>\n" +
                "<controlURL>(null)</controlURL>\n" +
                "<eventSubURL>(null)</eventSubURL>\n" +
                "<SCPDURL>(null)</SCPDURL>\n" +
                "</service>\n" +
                "</serviceList>\n" +
                "<presentationURL>index.html</presentationURL>\n" +
                "<iconList>\n" +
                "<icon>\n" +
                "<mimetype>image/png</mimetype>\n" +
                "<height>48</height>\n" +
                "<width>48</width>\n" +
                "<depth>24</depth>\n" +
                "<url>hue_logo_0.png</url>\n" +
                "</icon>\n" +
                "<icon>\n" +
                "<mimetype>image/png</mimetype>\n" +
                "<height>120</height>\n" +
                "<width>120</width>\n" +
                "<depth>24</depth>\n" +
                "<url>hue_logo_3.png</url>\n" +
                "</icon>\n" +
                "</iconList>\n" +
                "</device>\n" +
                "</root>\n";


        @RequestMapping(value = "/{deviceId}/setup.xml", method = RequestMethod.GET, produces = "application/xml")
                public ResponseEntity<String> getUpnpConfiguration(@PathVariable(value="deviceId") String deviceId, HttpServletRequest request){
                log.info("upnp device settings requested: " + deviceId + " from " +   request.getRemoteAddr());
                String hostName = request.getLocalAddr();
                String portNumber = Integer.toString(request.getLocalPort());
                String filledTemplate = String.format(hueTemplate, hostName, portNumber, hostName);

                return new ResponseEntity<>(filledTemplate, null, HttpStatus.OK);
        }

}
