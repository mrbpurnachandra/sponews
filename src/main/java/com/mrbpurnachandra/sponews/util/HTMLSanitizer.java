package com.mrbpurnachandra.sponews.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HTMLSanitizer {
    private static final PolicyFactory policy = new HtmlPolicyBuilder()
            .allowElements("b", "i", "u", "s", "p", "blockquote", "ol", "ul", "li", "h3", "h4")
            .toFactory();

    public static String sanitize(String html) {
        return policy.sanitize(html);
    }
}