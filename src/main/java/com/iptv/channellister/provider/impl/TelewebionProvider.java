package com.iptv.channellister.provider.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iptv.channellister.provider.ChannelProvider;
import javafx.util.Pair;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String                     TELEWEBION_API_URL  = "https://wa1.telewebion.com/v2/channels/getChannelLinks?device=desktop&channel_desc=";
    private static       List<Pair<String, String>> TELEWEBION_CHANNELS = new ArrayList<Pair<String, String>>() {{
        add(new Pair<>("tv1", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv1.png"));
        add(new Pair<>("tv2", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv2.png"));
        add(new Pair<>("tv3", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv3.png"));
        add(new Pair<>("tv4", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv4.png"));
        add(new Pair<>("tehran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tehran.png"));
        add(new Pair<>("irinn", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/irinn.png"));
        add(new Pair<>("shijam", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shijam.png"));
        add(new Pair<>("nasim", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/nasim.png"));
        add(new Pair<>("shinama", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shinama.png"));
        add(new Pair<>("varzesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/varzesh.png"));
        add(new Pair<>("pooya", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/pooya.png"));
        add(new Pair<>("ifilm", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/ifilm.png"));
        add(new Pair<>("shinamak", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shinamak.png"));
        add(new Pair<>("namayesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/namayesh.png"));
        add(new Pair<>("sepehr", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/sepehr.png"));
        add(new Pair<>("shiran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shiran.png"));
        add(new Pair<>("mostanad", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/mostanad.png"));
        add(new Pair<>("amouzesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/amouzesh.png"));
        add(new Pair<>("quran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/quran.png"));
        add(new Pair<>("salamat", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/salamat.png"));
        add(new Pair<>("jjtv1", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/jjtv1.png"));
        add(new Pair<>("hdtest", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/hdtest.png"));
        add(new Pair<>("omid", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/omid.png"));
        add(new Pair<>("ofogh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/ofogh.png"));
        add(new Pair<>("shoma", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shoma.png"));
        add(new Pair<>("esfahan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/esfahan.png"));
        add(new Pair<>("sahand", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/sahand.png"));
        add(new Pair<>("fars", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/fars.png"));
        add(new Pair<>("khoozestan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/khoozestan.png"));
        add(new Pair<>("khorasanrazavi", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/khorasanrazavi.png"));
        add(new Pair<>("kordestan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/kordestan.png"));
        add(new Pair<>("baran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/baran.png"));
        add(new Pair<>("semnan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/semnan.png"));
        add(new Pair<>("aftab", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/aftab.png"));
        //add(new Pair<>("aflak", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/aflak.png"));
    }};

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
                                                            .map(Pair::getKey)
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
        if (TELEWEBION_CHANNELS.stream().map(Pair::getKey).collect(Collectors.toList()).contains(tvDesc)) {
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
