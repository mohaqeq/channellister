package com.iptv.channellister.controller;

import com.iptv.channellister.provider.ChannelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class ChannelController {

    private static final String RESPONSE_START     = "#EXTM3U\n";
    private static final String RESPONSE_SEPARATOR = "\n";
    private static final String RESPONSE_TYPE      = "application/vnd.apple.mpegurl";

    private final List<? extends ChannelProvider> providers;

    @Autowired
    public ChannelController(final List<? extends ChannelProvider> providers) {
        this.providers = providers;
    }

    @GetMapping(value = "/player/{tvDesc}")
    public String getPlayer(@PathVariable(value = "tvDesc") String tvDesc, Model model) {
        final Optional<String> srcUrl = providers.parallelStream()
                                                 .map(provider -> provider.provide(tvDesc))
                                                 .filter(link -> !link.isEmpty())
                                                 .findAny();
        srcUrl.ifPresent(s -> model.addAttribute("srcUrl", s));
        return "player";
    }

    @GetMapping(value = "/playlist.m3u", produces = {RESPONSE_TYPE})
    @ResponseBody
    public String getChannels(@RequestParam(name = "name", required = false, defaultValue = "Stranger") String name) {
        StringBuilder response = new StringBuilder(RESPONSE_START);
        providers.stream()
                 .sorted(Comparator.comparingInt(ChannelProvider::getOrder))
                 .forEach(provider -> {
                     response.append(RESPONSE_SEPARATOR);
                     response.append(provider.provide());
                 });
        return response.toString();
    }
}
