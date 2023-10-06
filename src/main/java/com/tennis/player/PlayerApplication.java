package com.tennis.player;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.tennis.player.config.ApplicationCustomProperties;
import com.tennis.player.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties({ RsaKeyProperties.class, ApplicationCustomProperties.class })
public class PlayerApplication {

    public static void main(String[] args) {
	SpringApplication.run(PlayerApplication.class, args);
    }
}