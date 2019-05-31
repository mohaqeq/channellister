package com.iptv.channellister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ChannellisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChannellisterApplication.class, args);
    }

}
