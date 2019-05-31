package com.iptv.channellister.proxy;

import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.ForwardedRequestInterceptor;
import com.github.mkopylec.charon.core.http.RequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class ProxyRequestInterceptor implements ForwardedRequestInterceptor {

    public static final Pattern REVERSE_PROXY_REGEX_PATTERN = Pattern.compile("^https://([^.]*)\\.([^/]*)/");

    private final List<? extends SubRequestInterceptor> subInterceptors;

    @Autowired
    public ProxyRequestInterceptor(final List<? extends SubRequestInterceptor> subInterceptors) {
        this.subInterceptors = subInterceptors;
    }

    @Override
    public void intercept(final RequestData data, final MappingProperties mapping) {
        subInterceptors.forEach(interceptor -> interceptor.intercept(data, mapping));
    }
}
