package com.armzilla.ha;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeEchoSmartThingsHueBridgeController {

    @Value("${application.welcome.message:Welcome to the Echo SmartThings Hue Emulator Bridge}")
    private String message = "Welcome to the Echo SmartThings Hue Emulator Bridge";

    @RequestMapping("/")
    public String welcomeEchoSmartThingsHueBridge(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", this.message);
        return "welcomeESTHB";
    }
}
