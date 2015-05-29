package com.armzilla.ha.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class configureOauth {

    @RequestMapping("ui/configureOauth")
    public String configureOauth(Map<String, Object> model) {
        return "ui/configureOauth";
    }
}
