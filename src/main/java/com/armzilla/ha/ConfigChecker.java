package com.armzilla.ha;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by arm on 9/16/16.
 */
@Component
public class ConfigChecker implements InitializingBean {

    @Value("${upnp.config.address}")
    private String responseAddress;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(responseAddress == null || responseAddress.isEmpty() ){
            throw new IllegalArgumentException("please provide the IP(v4) address of the interface you want the bridge to listen on using --upnp.config.address=<ipadress>");
        }
    }
}
