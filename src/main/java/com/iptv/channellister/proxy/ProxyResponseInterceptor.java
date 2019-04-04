package com.iptv.channellister.proxy;

import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.ReceivedResponseInterceptor;
import com.github.mkopylec.charon.core.http.ResponseData;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class ProxyResponseInterceptor implements ReceivedResponseInterceptor {

    @Override
    public void intercept(final ResponseData data, final MappingProperties mapping) {
        final HttpHeaders headers = data.getHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        data.setHeaders(headers);
    }
}
