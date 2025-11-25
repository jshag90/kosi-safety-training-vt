package com.kosi.kosi_safety_training_vt.config;

import org.apache.catalina.Executor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadTomcatConfig  {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            // JDK 21 Virtual Thread Executor 적용
            connector.getProtocolHandler().setExecutor(
                    Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())
            );
        });
    }
}
