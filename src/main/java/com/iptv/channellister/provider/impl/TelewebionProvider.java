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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TelewebionProvider implements ChannelProvider {

    private static final String              TELEWEBION_API_URL  = "https://wa1.telewebion.com/v2/channels/getChannelLinks?device=desktop&channel_desc=";
    private static       Map<String, String> TELEWEBION_CHANNELS = new HashMap<String, String>() {{
        put("tv1", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv1.png");
        put("tv2", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv2.png");
        put("tv3", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv3.png");
        put("tv4", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tv4.png");
        put("tehran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/tehran.png");
        put("irinn", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/irinn.png");
        put("shijam", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shijam.png");
        put("nasim", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/nasim.png");
        put("shinama", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shinama.png");
        put("varzesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/varzesh.png");
        put("pooya", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/pooya.png");
        put("ifilm", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/ifilm.png");
        put("shinamak", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shinamak.png");
        put("namayesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/namayesh.png");
        put("sepehr", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/sepehr.png");
        put("shiran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shiran.png");
        put("mostanad", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/mostanad.png");
        put("amouzesh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/amouzesh.png");
        put("quran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/quran.png");
        put("salamat", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/salamat.png");
        put("jjtv1", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/jjtv1.png");
        put("hdtest", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/hdtest.png");
        put("omid", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/omid.png");
        put("ofogh", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/ofogh.png");
        put("shoma", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/shoma.png");
        put("esfahan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/esfahan.png");
        put("sahand", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/sahand.png");
        put("fars", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/fars.png");
        put("khoozestan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/khoozestan.png");
        put("khorasanrazavi", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/khorasanrazavi.png");
        put("kordestan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/kordestan.png");
        put("baran", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/baran.png");
        put("semnan", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/semnan.png");
        put("aftab", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/aftab.png");
        //put("aflak", "https://static.televebion.net/web/content_images/channel_images/thumbs/new/240/v4/aflak.png");
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
        Map<String, String> channelMap = TELEWEBION_CHANNELS.keySet()
                                                            .parallelStream()
                                                            .collect(Collectors.toMap(Function.identity(),
                                                                                      this::getChannelLink));
        TELEWEBION_CHANNELS.keySet().forEach(channel -> {
            channels.append("#EXTINF:-1 group-title=\"Internal\" tvg-logo=\"");
            channels.append(TELEWEBION_CHANNELS.get(channel));
            channels.append("\",");
            channels.append(channel.toUpperCase());
            channels.append("\n");
            channels.append(channelMap.get(channel));
            channels.append("\n");
        });
        return channels.toString();
    }

    @Override
    public String provide(final String tvDesc) {
        if (TELEWEBION_CHANNELS.containsKey(tvDesc)) {
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
