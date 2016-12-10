package com.armzilla.ha;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by arm on 9/12/15.
 */
@Component

public class TomcatConnectorBean {
    @Value("${emulator.portbase}")
    private int portBase;
    @Value("${emulator.portcount}")
    private int portCount;
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = null;
        for(int i = 0; i < portCount; i ++) {
            if(tomcat == null){
                tomcat = new TomcatEmbeddedServletContainerFactory(portBase + i);
            }else{
                tomcat.addAdditionalTomcatConnectors(createConnector(portBase + i));
            }
        }
        return tomcat;
    }

    private Connector createConnector(int portNumber) {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(portNumber);
        return connector;
    }
}
