package com.mrbpurnachandra.sponews.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "emotion-prediction")
@Component
@Data
public class EmotionPredictionProps {
    private String uri;
}
