package com.iptv.channellister.provider.impl;

import com.iptv.channellister.provider.ChannelProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ConfigurationProperties("irib")
public class IribLiveProvider implements ChannelProvider {

    private final Logger       logger;
    private final OkHttpClient client;

    private String        proxyUrl;
    private List<Channel> channels;

    public IribLiveProvider(@Value("${root.url:http://localhost}") String rootUrl) {
        logger = LoggerFactory.getLogger(BBCProvider.class);
        client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()
                                                                   .setLevel(HttpLoggingInterceptor.Level.BODY))
                                           .build();
        proxyUrl = rootUrl + "/irib";
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
                                                 .collect(Collectors.toMap(Channel::getName,
                                                                           channel -> getChannelLink(channel.getAddress())
                                                 ));
        channels.forEach(channel -> {
            channelsBuilder.append("#EXTINF:-1, group-title=\"IRIB\" tvg-logo=\"");
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
        Optional<Channel> channel = channels.stream()
                                            .filter(ch -> ch.getName().equals(channelName))
                                            .findAny();
        return channel.map(value -> getChannelLink(value.getAddress()))
                      .orElse("");
    }

    @Override
    public int getOrder() {
        return 200;
    }

    private String getChannelLink(String pageLink) {
        Request request = new Request.Builder()
                .url(pageLink)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response != null && response.isSuccessful()) {
                final String body = response.body().string();
                final int playerUrlIndex = body.indexOf("LiveUrl");
                final int startQuotIndex = body.indexOf("'", playerUrlIndex) + 1;
                final int endQuotIndex = body.indexOf("'", startQuotIndex + 1);
                return body.substring(startQuotIndex, endQuotIndex).replace("http://cdnlive.irib.ir", proxyUrl);
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }

    public static class Channel {

        private String name;
        private String address;
        private String logo;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(final String address) {
            this.address = address;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(final String logo) {
            this.logo = logo;
        }
    }
}
