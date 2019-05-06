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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TelewebionProvider implements ChannelProvider {

    private static final String                          TELEWEBION_URL      = "https://www.telewebion.com";
    private static final String                          TELEWEBION_API_URL  = "https://wa1.telewebion.com/v2/channels/getChannelLinks?device=desktop&channel_desc=";
    private static final List<Map.Entry<String, String>> TELEWEBION_CHANNELS =
            new ArrayList<Map.Entry<String, String>>() {{
                add(new HashMap.SimpleEntry<>("tv1", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv1.png"));
                add(new HashMap.SimpleEntry<>("tv2", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv2.png"));
                add(new HashMap.SimpleEntry<>("tv3", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv3.png"));
                add(new HashMap.SimpleEntry<>("tv4", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv4.png"));
                add(new HashMap.SimpleEntry<>("tehran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tehran.png"));
                add(new HashMap.SimpleEntry<>("irinn", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/irinn.png"));
                add(new HashMap.SimpleEntry<>("shijam", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shijam.png"));
                add(new HashMap.SimpleEntry<>("nasim", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/nasim.png"));
                add(new HashMap.SimpleEntry<>("shinama", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shinama.png"));
                add(new HashMap.SimpleEntry<>("varzesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/varzesh.png"));
                add(new HashMap.SimpleEntry<>("pooya", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/pooya.png"));
                add(new HashMap.SimpleEntry<>("ifilm", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/ifilm.png"));
                add(new HashMap.SimpleEntry<>("shinamak", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shinamak.png"));
                add(new HashMap.SimpleEntry<>("namayesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/namayesh.png"));
                add(new HashMap.SimpleEntry<>("sepehr", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/sepehr.png"));
                add(new HashMap.SimpleEntry<>("shiran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shiran.png"));
                add(new HashMap.SimpleEntry<>("mostanad", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/mostanad.png"));
                add(new HashMap.SimpleEntry<>("amouzesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/amouzesh.png"));
                add(new HashMap.SimpleEntry<>("quran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/quran.png"));
                add(new HashMap.SimpleEntry<>("salamat", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/salamat.png"));
                add(new HashMap.SimpleEntry<>("jjtv1", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/jjtv1.png"));
                add(new HashMap.SimpleEntry<>("hdtest", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/hdtest.png"));
                add(new HashMap.SimpleEntry<>("omid", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/omid.png"));
                add(new HashMap.SimpleEntry<>("ofogh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/ofogh.png"));
                add(new HashMap.SimpleEntry<>("shoma", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shoma.png"));
                add(new HashMap.SimpleEntry<>("esfahan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/esfahan.png"));
                add(new HashMap.SimpleEntry<>("sahand", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/sahand.png"));
                add(new HashMap.SimpleEntry<>("fars", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/fars.png"));
                add(new HashMap.SimpleEntry<>("khoozestan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/khoozestan.png"));
                add(new HashMap.SimpleEntry<>("khorasanrazavi", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/khorasanrazavi.png"));
                add(new HashMap.SimpleEntry<>("kordestan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/kordestan.png"));
                add(new HashMap.SimpleEntry<>("baran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/baran.png"));
                add(new HashMap.SimpleEntry<>("semnan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/semnan.png"));
                add(new HashMap.SimpleEntry<>("aftab", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/aftab.png"));
                //add(new HashMap.SimpleEntry<>("aflak", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/aflak.png"));
            }};
    private static final String                          REVERSE_PROXY_REGEX = "^https://([^.]*)\\.([^/]*)/";

    private final Logger       logger;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    @Value("${cl.proxy.url.tw:http://localhost/telewebion/$1/}")
    private String channellisterProxyUrl;

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
                                                            .map(Map.Entry::getKey)
                                                            .collect(Collectors.toMap(Function.identity(),
                                                                                      this::getChannelLink));
        TELEWEBION_CHANNELS.forEach(channel -> {
            channels.append("#EXTINF:-1 group-title=\"Internal\" tvg-logo=\"");
            channels.append(channel.getValue());
            channels.append("\",");
            channels.append(channel.getKey().toUpperCase());
            channels.append("\n");
            channels.append(channelMap.get(channel.getKey()));
            channels.append("\n");
        });
        return channels.toString();
    }

    @Override
    public String provide(final String tvDesc) {
        if (TELEWEBION_CHANNELS.stream().map(Map.Entry::getKey).collect(Collectors.toList()).contains(tvDesc)) {
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
                .addHeader("Referer", TELEWEBION_URL)
                .addHeader("Origin", TELEWEBION_URL)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                final JsonNode body = objectMapper.readTree(response.body().string());
                final JsonNode maxLink = StreamSupport.stream(body.get("data").get(0).get("links").spliterator(), false)
                                                      .max(Comparator.comparingInt(link -> link.get("bitrate").asInt()))
                                                      .get();
                return maxLink.get("link").asText().replaceFirst(REVERSE_PROXY_REGEX, channellisterProxyUrl);
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }
}
