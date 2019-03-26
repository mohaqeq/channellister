package com.iptv.channellister.provider.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.iptv.channellister.client.TelewebionClient;
import com.iptv.channellister.provider.ChannelProvider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TelewebionProvider implements ChannelProvider {

    private static final String       TELEWEBION_API_URL  = "https://wa1.telewebion.com";
    private static final List<String> TELEWEBION_CHANNELS = Arrays.asList("tv1", "tv2", "tv3", "tv4", "tehran", "irinn",
                                                                          "shijam", "nasim", "shinama", "varzesh",
                                                                          "pooya", "ifilm", "shinamak", "namayesh",
                                                                          "sepehr", "shiran", "mostanad", "amouzesh",
                                                                          "quran", "salamat", "jjtv1", "hdtest", "omid",
                                                                          "ofogh", "shoma", "esfahan", "sahand", "fars",
                                                                          "khoozestan", "khorasanrazavi", "kordestan",
                                                                          "baran", "semnan", "aftab", "aflak");

    private final Logger           logger;
    private final TelewebionClient client;

    public TelewebionProvider() {
        logger = LoggerFactory.getLogger(TelewebionProvider.class);
        client = getClientBuilder(TELEWEBION_API_URL)
                .create(TelewebionClient.class);
    }

    @Override
    public String provide() {
        StringBuilder channels = new StringBuilder();
        Map<String, String> channelMap = TELEWEBION_CHANNELS.parallelStream()
                                                            .collect(Collectors.toMap(Function.identity(),
                                                                                      this::getChannelLinks));
        TELEWEBION_CHANNELS.forEach(channel -> {
            channels.append("#EXTINF:-1,");
            channels.append(channel);
            channels.append("\n");
            channels.append(channelMap.get(channel));
            channels.append("\n");
        });
        return channels.toString();
    }

    private String getChannelLinks(final String channelDesc) {
        try {
            Response<JsonNode> response = client.getChannelLinks(channelDesc, "desktop", 4)
                             .execute();
            if (response != null && response.isSuccessful()) {
                JsonNode maxLink = StreamSupport.stream(response.body().get("data").get(0).get("links").spliterator(), false)
                                                .max(Comparator.comparingInt(link -> link.get("bitrate").asInt()))
                                                .get();

                return maxLink.get("link").asText();
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channels", e);
        }
        return "";
    }

    private Retrofit getClientBuilder(String url) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                                .build())
                .baseUrl(url)
                .build();
    }
}
