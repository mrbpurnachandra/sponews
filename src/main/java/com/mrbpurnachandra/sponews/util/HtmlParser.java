package com.mrbpurnachandra.sponews.util;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class HtmlParser {
    public static String extractText(String html) {
        return Jsoup.parse(html).body().text();
    }
}
