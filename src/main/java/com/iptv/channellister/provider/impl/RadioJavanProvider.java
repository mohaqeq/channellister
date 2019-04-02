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
public class RadioJavanProvider implements ChannelProvider {

    private static final String RADIOJAVAN_API_URL = "https://www.radiojavan.com/tv";

    private final Logger       logger;
    private final OkHttpClient client;

    public RadioJavanProvider() {
        logger = LoggerFactory.getLogger(RadioJavanProvider.class);
        client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()
                                                                   .setLevel(HttpLoggingInterceptor.Level.BODY))
                                           .build();
    }

    @Override
    public String provide() {
        return "#EXTINF:-1 tvg-name=\"Radio Javan\" tvg-logo=\"https://www.radiojavan.com/images/nav_logo.png\" group-title=\"External\",RADIOJAVAN" +
                "\n" +
                getChannelLink() +
                "\n";
    }

    @Override
    public String provide(final String tvDesc) {
        if (tvDesc.equals("radiojavan")) {
            return getChannelLink();
        }
        return "";
    }

    @Override
    public int getOrder() {
        return 103;
    }

    private String getChannelLink() {
        Request request = new Request.Builder()
                .url(RADIOJAVAN_API_URL)
                .addHeader("Referer", RADIOJAVAN_API_URL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                final String body = response.body().string();
                final int playerUrlIndex = body.lastIndexOf("<source");
                final int startQuotIndex = body.indexOf("\"", playerUrlIndex) + 1;
                final int endQuotIndex = body.indexOf("\"", startQuotIndex + 1);
                return "https:" + body.substring(startQuotIndex, endQuotIndex);
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }
}
