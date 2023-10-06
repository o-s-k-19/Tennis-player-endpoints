package com.tennis.player.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="application")
public record ApplicationCustomProperties(String swaggerUiPath,String basePath) {

}
