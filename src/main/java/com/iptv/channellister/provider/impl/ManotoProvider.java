package com.iptv.channellister.provider.impl;

import com.iptv.channellister.provider.ChannelProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ManotoProvider implements ChannelProvider {

    private static final String MANOTO_API_URL = "https://www.manototv.com/live";

    private final Logger       logger;
    private final OkHttpClient client;

    public ManotoProvider() {
        logger = LoggerFactory.getLogger(ManotoProvider.class);
        client = new OkHttpClient();
    }

    @Override
    public String provide() {
        return "#EXTINF:-1,manoto" +
                "\n" +
                getChannelLink() +
                "\n";
    }

    @Override
    public String provide(final String tvDesc) {
        if (tvDesc.equals("manoto")) {
            return getChannelLink();
        }
        return "";
    }

    private String getChannelLink() {
        Request request = new Request.Builder()
                .url(MANOTO_API_URL)
                .addHeader("Referer", MANOTO_API_URL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                final String body = response.body().string();
                final int playerUrlIndex = body.lastIndexOf("source src=");
                final int startQuotIndex = body.indexOf("\"", playerUrlIndex) + 1;
                final int endQuotIndex = body.indexOf("\"", startQuotIndex + 1);
                return body.substring(startQuotIndex, endQuotIndex);
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }
}
