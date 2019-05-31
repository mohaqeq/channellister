package com.iptv.channellister.proxy;

import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.RequestData;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelewebionRequestInterceptor implements SubRequestInterceptor {

    private static final String      TELEWEBION_CONFIG_NAME   = "telewebion";
    private static final Pattern     TELEWEBION_REGEX_PATTERN = Pattern.compile("^/telewebion/([^/]*)");
    private static final String      TELEWEBION_URL_FORMAT    = "https://%s.telewebion.com";
    private static final String      TELEWEBION_URL           = "https://www.telewebion.com";
    private static final HttpHeaders TELEWEBION_HEADERS       = new HttpHeaders() {{
        add("Referer", TELEWEBION_URL);
        add("Origin", TELEWEBION_URL);
    }};

    @Override
    public void intercept(final RequestData data, final MappingProperties mapping) {
        if (mapping.getName().equals(TELEWEBION_CONFIG_NAME)) {
            final Matcher matcher = TELEWEBION_REGEX_PATTERN.matcher(data.getUri());
            if (matcher.find()) {
                data.setUri(data.getUri().replaceFirst(matcher.group(1) + "/", ""));
                mapping.setDestinations(Collections.singletonList(String.format(TELEWEBION_URL_FORMAT, matcher.group(1))));
            }
            final HttpHeaders headers = data.getHeaders();
            headers.addAll(TELEWEBION_HEADERS);
            data.setHeaders(headers);
        }
    }
}
