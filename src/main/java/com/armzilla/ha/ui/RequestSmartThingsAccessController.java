package com.armzilla.ha.ui;

import com.armzilla.ha.dao.DeviceDescriptor;
import com.armzilla.ha.dao.DeviceRepository;
import com.armzilla.ha.util.JsonReader;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class RequestSmartThingsAccessController {

    @Autowired
    private DeviceRepository repository;

    @Value("${application.smartthings.app.externalIP:externalIPMissing}")
    private String externalIP = "externalIPMissing";
    @Value("${server.port:serverPortMissing}")
    private String serverPort = "serverPortMissing";
    @Value("${application.smartthings.app.clientId:clientIdMissing}")
    private String clientId = "clientIdMissing";
    @Value("${application.smartthings.app.clientSecret:clientSecretMissing}")
    private String clientSecret = "clientSecretMissing";

    @RequestMapping("ui/importAuthorizedDevices")
    public String importAuthorizedDevices(@RequestParam(value="code", required=false, defaultValue="") String code,
                                          @RequestParam(value="accessToken", required=false, defaultValue="") String accessToken,
                                          Map<String, Object> model) {
        if (clientId.equals("clientIdMissing") || clientSecret.equals("clientSecretMissing")) {
            return "ui/configureOauth";
        }
        if (code.isEmpty() && accessToken.isEmpty()) {
            // Redirect to get access Code
            return "redirect:https://graph.api.smartthings.com/oauth/authorize?response_type=code&client_id=" + clientId
                    + "&redirect_uri=http://" + externalIP + ":" + serverPort + "/ui/importAuthorizedDevices"
                    + "&scope=app";
        } else {
            if (accessToken.isEmpty()) {
                // Redirect using Access code to get access token

                String getAccessTokenUrl = "https://graph.api.smartthings.com/oauth/token?grant_type=authorization_code&client_id=" + clientId
                        + "&redirect_uri=http://" + externalIP + ":" + serverPort + "/ui/importAuthorizedDevices"
                        + "&client_secret=" + clientSecret
                        + "&code=" + code
                        + "&scope=app";
                String accessTokenReturned = "";
                try {
                    JSONObject json = JsonReader.readJsonFromUrl(getAccessTokenUrl);
                    accessTokenReturned = json.getString("access_token");
                    System.out.print("Access Token=" + accessTokenReturned + "\n");
                    return "redirect:/ui/importAuthorizedDevices?accessToken=" + accessTokenReturned;
                } catch (IOException exp) {
                    System.out.print("Error Jason Reader Threw IO Exception getting access token\n");
                } catch (JSONException exp) {
                    System.out.print("Error Jason Reader Threw JSON Exception getting access token\n");
                    System.out.print("Error:" + exp.getMessage());

                }
                return "error";
            } else {
                // Ok we can aquire the data and process now
                // Uncomment here to see result from Smart Things
                //String redirectURL="https://graph.api.smartthings.com/api/smartapps/endpoints/" + clientId + "?access_token=" + accessToken;
                //System.out.print("Redirect URL=" + redirectURL);
                //return "redirect:" + redirectURL;
                getAuthorizedDevices(accessToken, clientId);
            }
        }
        return "redirect:/api/devices";
    }

    private void getAuthorizedDevices(String accessToken, String clientId) {
        String url = "https://graph.api.smartthings.com/api/smartapps/endpoints/" + clientId + "?access_token=" + accessToken;

        try {
            // Get array of endpoints
            JSONArray endpoints = JsonReader.readJsonArrayFromUrl(url);
            // For each endpoint get switches
            for(int index=0; index < endpoints.length();index++) {
                JSONObject endpoint = endpoints.getJSONObject(index);
                String endpointUrl = endpoint.getString("url");
                System.out.println("endpointUrl=" + endpointUrl);
                String switchUrl = "https://graph.api.smartthings.com" + endpointUrl + "/switches?access_token=" + accessToken;
                System.out.println("switchUrl=" + switchUrl);
                // Need to pass access Token to get added to the header
                JSONArray switches = JsonReader.readJsonArrayFromUrl(switchUrl);
                System.out.println("Switches json Returned=" + switches.toString());
                // Remove the entire list of switches first so we can rebuild it.
                // ToDo...Recode to add devices which were added
                //        Keep devices that existed
                //        Remove devices removed
                Page<DeviceDescriptor> deviceList = repository.findByDeviceType("switch", new PageRequest(0,100));
                Map<String, String> deviceResponseMap = new HashMap<>();
                for (DeviceDescriptor device : deviceList) {
                    repository.delete(device.getId());
                }
                // Now rebuild the list with the currently authorized devices
                for (int switchIndex=0; switchIndex < switches.length();switchIndex++) {
                    JSONObject aSwitch = switches.getJSONObject(switchIndex);
                    System.out.println("Switch=" + aSwitch.toString());
                    String onURL = "https://graph.api.smartthings.com"
                            + endpointUrl
                            + "/switches/" + aSwitch.getString("id")
                            + "/on?access_token=" + accessToken;
                    String offURL = "https://graph.api.smartthings.com" + endpointUrl
                            + "/switches/" + aSwitch.getString("id")
                            + "/off?access_token=" + accessToken;
                    System.out.println("onURL=" + onURL);
                    System.out.println("offURL=" + offURL);
                    DeviceDescriptor deviceEntry = new DeviceDescriptor();
                    deviceEntry.setId(UUID.randomUUID().toString());
                    String name = aSwitch.getString("label");
                    deviceEntry.setName(name);
                    deviceEntry.setDeviceType("switch");
                    deviceEntry.setOnUrl(onURL);
                    deviceEntry.setOffUrl(offURL);
                    repository.save(deviceEntry);
                }
            }
        } catch (IOException exp) {
            System.out.print("Error Jason Reader Threw IO Exception getting endpoints");
            System.out.println("Error:" + exp.getMessage());
        } catch (JSONException exp) {
            System.out.println("Error Jason Reader Threw JSON Exception getting endpoints");
            System.out.println("Error:" + exp.getMessage());
        }
    }
}

