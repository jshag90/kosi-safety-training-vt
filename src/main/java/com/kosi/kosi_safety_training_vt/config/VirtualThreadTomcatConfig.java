package com.kosi.kosi_safety_training_vt.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadTomcatConfig  {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatVirtualThreadCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            Executor virtualThreadExecutor = Executors.newThreadPerTaskExecutor(
                    Thread.ofVirtual()
                            .name("vt-", 1)  // vt-1, vt-2, vt-3... 으로 이름 보임
                            .factory()
            );

            // 이 한 줄이 핵심! 모든 HTTP 요청이 Virtual Thread에서 실행됨
            connector.getProtocolHandler().setExecutor(virtualThreadExecutor);
        });
    }
}
