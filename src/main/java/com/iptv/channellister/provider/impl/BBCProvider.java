package com.iptv.channellister.provider.impl;

import com.iptv.channellister.provider.ChannelProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BBCProvider implements ChannelProvider {

    private static final String BBC_API_URL = "http://wsdownload.bbc.co.uk/persian/meta/live/smp/ptv_live.xml";

    private final Logger       logger;
    private final OkHttpClient client;

    public BBCProvider() {
        logger = LoggerFactory.getLogger(BBCProvider.class);
        client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()
                                                                   .setLevel(HttpLoggingInterceptor.Level.BODY))
                                           .build();
    }

    @Override
    public String provide() {
        return "#EXTINF:-1 tvg-name=\"BBC\" tvg-logo=\"https://www.logodesignlove.com/images/evolution/bbc-logo-design.gif\" group-title=\"External\",BBC" +
                "\n" +
                getChannelLink() +
                "\n";
    }

    @Override
    public String provide(final String tvDesc) {
        if (tvDesc.equals("bbc")) {
            return getChannelLink();
        }
        return "";
    }

    @Override
    public int getOrder() {
        return 102;
    }

    private String getChannelLink() {
        Request request = new Request.Builder()
                .url(BBC_API_URL)
                .addHeader("Referer", "https://emp.bbc.com")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                final String body = response.body().string();
                final int playerUrlIndex = body.lastIndexOf("connection href=");
                final int startQuotIndex = body.indexOf("\"", playerUrlIndex) + 1;
                final int endQuotIndex = body.indexOf("\"", startQuotIndex + 1);
                return body.substring(startQuotIndex, endQuotIndex).replaceFirst("http", "https");
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }
}
