package com.iptv.channellister.provider.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iptv.channellister.provider.ChannelProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TelewebionProvider implements ChannelProvider {

    private static final String       TELEWEBION_API_URL  = "https://wa1.telewebion.com/v2/channels/getChannelLinks?device=desktop&channel_desc=";
    private static final List<String> TELEWEBION_CHANNELS = Arrays.asList("tv1", "tv2", "tv3", "tv4", "tehran", "irinn",
                                                                          "shijam", "nasim", "shinama", "varzesh",
                                                                          "pooya", "ifilm", "shinamak", "namayesh",
                                                                          "sepehr", "shiran", "mostanad", "amouzesh",
                                                                          "quran", "salamat", "jjtv1", "hdtest", "omid",
                                                                          "ofogh", "shoma", "esfahan", "sahand", "fars",
                                                                          "khoozestan", "khorasanrazavi", "kordestan",
                                                                          "baran", "semnan", "aftab", "aflak");

    private final Logger       logger;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public TelewebionProvider() {
        objectMapper = new ObjectMapper();
        logger = LoggerFactory.getLogger(TelewebionProvider.class);
        client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()
                                                                   .setLevel(HttpLoggingInterceptor.Level.BODY))
                                           .build();
    }

    @Override
    public String provide() {
        StringBuilder channels = new StringBuilder();
        Map<String, String> channelMap = TELEWEBION_CHANNELS.parallelStream()
                                                            .collect(Collectors.toMap(Function.identity(),
                                                                                      this::getChannelLink));
        TELEWEBION_CHANNELS.forEach(channel -> {
            channels.append("#EXTINF:-1 group-title=\"Internal\",");
            channels.append(channel.toUpperCase());
            channels.append("\n");
            channels.append(channelMap.get(channel));
            channels.append("\n");
        });
        return channels.toString();
    }

    @Override
    public String provide(final String tvDesc) {
        if (TELEWEBION_CHANNELS.contains(tvDesc)) {
            return getChannelLink(tvDesc);
        }
        return "";
    }

    @Override
    public int getOrder() {
        return 200;
    }

    private String getChannelLink(final String channelDesc) {
        Request request = new Request.Builder()
                .url(TELEWEBION_API_URL + channelDesc)
                .addHeader("Referer", "https://www.telewebion.com")
                .addHeader("Origin", "https://www.telewebion.com")

                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                final JsonNode body = objectMapper.readTree(response.body().string());
                final JsonNode maxLink = StreamSupport.stream(body.get("data").get(0).get("links").spliterator(), false)
                                                      .max(Comparator.comparingInt(link -> link.get("bitrate").asInt()))
                                                      .get();
                return maxLink.get("link").asText().replaceFirst("^https://([^/]*)/", "http://channellister.herokuapp.com/telewebion/");
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }
}
