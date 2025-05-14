package com.cucicov.proiectb.configuration;

import com.cucicov.proiectb.utils.AppConfigurationProperties;
import com.cucicov.proiectb.utils.Utils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

    private final AppConfigurationProperties appConfigurationProperties;

    public UtilsConfig(AppConfigurationProperties appConfigurationProperties) {
        this.appConfigurationProperties = appConfigurationProperties;
    }

    @PostConstruct
    public void init() {
        Utils.setAppConfigurationProperties(appConfigurationProperties);
    }

}
