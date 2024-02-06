package com.mrbpurnachandra.sponews.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HTMLSanitizer {
    private static final PolicyFactory policy = new HtmlPolicyBuilder()
            .allowElements("strong", "em", "u", "del", "p", "blockquote", "ol", "ul", "li", "h2", "h3")
            .toFactory();

    public static String sanitize(String html) {
        return policy.sanitize(html);
    }
}