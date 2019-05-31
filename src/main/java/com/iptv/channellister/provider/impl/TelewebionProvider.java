package com.iptv.channellister.provider.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iptv.channellister.provider.ChannelProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.iptv.channellister.proxy.ProxyRequestInterceptor.REVERSE_PROXY_REGEX_PATTERN;

@Service
@ConfigurationProperties("telewebion")
public class TelewebionProvider implements ChannelProvider {

    private static final String TELEWEBION_URL       = "https://www.telewebion.com";
    private static final String TELEWEBION_API_URL   = "https://wa1.telewebion.com/v2/channels/getChannelLinks?device=desktop&channel_desc=";
    private static final String TELEWEBION_PROXY_URL = "/telewebion/$1/";

    private final Logger       logger;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    private String        proxyUrl;
    private List<Channel> channels = new ArrayList<>();

    public TelewebionProvider(@Value("${root.url:http://localhost}") String rootUrl) {
        objectMapper = new ObjectMapper();
        logger = LoggerFactory.getLogger(TelewebionProvider.class);
        client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()
                                                                   .setLevel(HttpLoggingInterceptor.Level.BODY))
                                           .build();
        proxyUrl = rootUrl + TELEWEBION_PROXY_URL;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(final List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public String provide() {
        StringBuilder channelsBuilder = new StringBuilder();
        Map<String, String> channelMap = channels.parallelStream()
                                                 .map(Channel::getName)
                                                 .collect(Collectors.toMap(Function.identity(),
                                                                           this::getChannelLink));
        channels.forEach(channel -> {
            channelsBuilder.append("#EXTINF:-1 group-title=\"Telewebion\" tvg-logo=\"");
            channelsBuilder.append(channel.getLogo());
            channelsBuilder.append("\",");
            channelsBuilder.append(channel.getName().toUpperCase());
            channelsBuilder.append("\n");
            channelsBuilder.append(channelMap.get(channel.getName()));
            channelsBuilder.append("\n");
        });
        return channelsBuilder.toString();
    }

    @Override
    public String provide(final String channelName) {
        if (channels.stream().map(Channel::getName).collect(Collectors.toList()).contains(channelName)) {
            return getChannelLink(channelName);
        }
        return "";
    }

    @Override
    public int getOrder() {
        return 300;
    }

    private String getChannelLink(final String channelDesc) {
        Request request = new Request.Builder()
                .url(TELEWEBION_API_URL + channelDesc)
                .addHeader("Referer", TELEWEBION_URL)
                .addHeader("Origin", TELEWEBION_URL)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                final JsonNode body = objectMapper.readTree(response.body().string());
                final JsonNode maxLink = StreamSupport.stream(body.get("data").get(0).get("links").spliterator(), false)
                                                      .max(Comparator.comparingInt(link -> link.get("bitrate").asInt()))
                                                      .get();
                return REVERSE_PROXY_REGEX_PATTERN.matcher(maxLink.get("link").asText()).replaceFirst(proxyUrl);
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }

    public static class Channel {

        private String name;
        private String logo;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(final String logo) {
            this.logo = logo;
        }
    }
}
