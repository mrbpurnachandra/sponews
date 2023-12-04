package com.mrbpurnachandra.sponews.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public DateFormat dateFormat() {
        return new SimpleDateFormat("EEE, d MMM yyyy", Locale.of("ne"));
    }
}
