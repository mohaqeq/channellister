package com.iptv.channellister.controller;

import com.iptv.channellister.provider.ChannelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChannelController {

    private static final String RESPONSE_START     = "#EXTM3U\n";
    private static final String RESPONSE_SEPARATOR = "\n";
    private static final String RESPONSE_TYPE      = "application/vnd.apple.mpegurl";

    private final List<? extends ChannelProvider> providers;

    @Autowired
    public ChannelController(final List<? extends ChannelProvider> providers) {
        this.providers = providers;
    }

    @GetMapping(value = "/", produces = {RESPONSE_TYPE})
    @ResponseBody
    public String getChannels(@RequestParam(name = "name", required = false, defaultValue = "Stranger") String name) {
        StringBuilder response = new StringBuilder(RESPONSE_START);
        providers.forEach(provider -> {
            response.append(provider.provide());
            response.append(RESPONSE_SEPARATOR);
        });
        return response.toString();
    }
}
