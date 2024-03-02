package com.mrbpurnachandra.sponews.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public DateFormat dateFormat() {
        return new SimpleDateFormat("EEE, d MMM yyyy", Locale.forLanguageTag("ne"));
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
