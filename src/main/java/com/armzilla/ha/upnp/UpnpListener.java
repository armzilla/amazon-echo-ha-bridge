package com.armzilla.ha.upnp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;

/**
 * Created by arm on 4/11/15.
 */
@Component
public class UpnpListener {
    private Logger log = Logger.getLogger(UpnpListener.class);
    private static final int UPNP_DISCOVERY_PORT = 1900;
    private static final String UPNP_MULTICAST_ADDRESS = "239.255.255.250";

    @Value("${upnp.response.port}")
    private int upnpResponsePort;

    @Value("${server.port}")
    private int httpServerPort;

    @Value("${upnp.config.address}")
    private String responseAddress;

    @Scheduled(fixedDelay = Integer.MAX_VALUE)
    public void startListening(){
        log.info("Starting UPNP Discovery Listener");

        try (DatagramSocket responseSocket = new DatagramSocket(upnpResponsePort);
             MulticastSocket upnpMulticastSocket  = new MulticastSocket(UPNP_DISCOVERY_PORT)) {

            InetAddress upnpGroupAddress = InetAddress.getByName(UPNP_MULTICAST_ADDRESS);
            upnpMulticastSocket.joinGroup(upnpGroupAddress);

            while(true){ //trigger shutdown here
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                upnpMulticastSocket.receive(packet);
                String packetString = new String(packet.getData());
                if(isSSDPDiscovery(packetString)){
                    log.info("Got SSDP Discovery packet from " +  packet.getAddress().getHostAddress() + ":" + packet.getPort());
                    sendUpnpResponse(responseSocket, packet.getAddress(), packet.getPort());
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("UPNP Discovery Listener Stopped");

    }

    protected boolean isSSDPDiscovery(String body){
        if(body.startsWith("M-SEARCH * HTTP/1.1") && body.contains("MAN: \"ssdp:discover\"")){
            return true;
        }
        return false;
    }

    String discoveryTemplate = "HTTP/1.1 200 OK\r\n" +
            "CACHE-CONTROL: max-age=86400\r\n" +
            "DATE: Thu, 09 Apr 2015 19:49:04 GMT\r\n" +
            "EXT:\r\n" +
            "LOCATION: http://%s:%s/upnp/cheese/setup.xml\r\n" +
            "OPT: \"http://schemas.upnp.org/upnp/1/0/\"; ns=01\r\n" +
            "01-NLS: 86f81bce-1dd2-11b2-a14f-ab8000ac1b49\r\n" +
            "SERVER: Unspecified, UPnP/1.0, Unspecified\r\n" +
            "X-User-Agent: redsonic\r\n" +
            "ST: urn:Belkin:device:**\r\n" +
            "USN: uuid:Socket-1_0-221438K0100073::urn:Belkin:device:**\r\n\r\n";
    protected boolean sendUpnpResponse(DatagramSocket socket, InetAddress requester, int sourcePort){
        String discoveryResponse = String.format(discoveryTemplate, responseAddress, httpServerPort);
        DatagramPacket response = new DatagramPacket(discoveryResponse.getBytes(), discoveryResponse.length(), requester, sourcePort);
        try {
            socket.send(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
