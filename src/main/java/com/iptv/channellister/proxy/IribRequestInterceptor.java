package com.iptv.channellister.proxy;

import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.RequestData;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class IribRequestInterceptor implements SubRequestInterceptor {

    private static final String      IRIB_CONFIG_NAME = "irib";
    private static final String      IRIB_URL         = "http://live.irib.ir";
    private static final HttpHeaders IRIB_HEADERS     = new HttpHeaders() {{
        add("Referer", IRIB_URL);
        add("Origin", IRIB_URL);
    }};

    @Override
    public void intercept(final RequestData data, final MappingProperties mapping) {
        if (mapping.getName().equals(IRIB_CONFIG_NAME)) {
            data.setHeaders(IRIB_HEADERS);
        }
    }
}
