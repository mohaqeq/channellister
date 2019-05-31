package com.iptv.channellister.proxy;

import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.RequestData;

public interface SubRequestInterceptor {

    void intercept(RequestData data, MappingProperties mapping);
}
