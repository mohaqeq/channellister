package com.iptv.channellister.provider.impl;

import com.iptv.channellister.provider.ChannelProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Service()
public class IribLiveProvider implements ChannelProvider {

    private static Map<String, String> IRIB_CHANNELS_MAP = new HashMap<String, String>() {{
        put("tv1", "http://live.irib.ir/?idc=C0&idb=TV&nol=123487");
        put("tv2", "http://live.irib.ir/?idc=C0&idb=TV&nol=123488");
        put("tv3", "http://live.irib.ir/?idc=C0&idb=TV&nol=123489");
        put("tv4", "http://live.irib.ir/?idc=C0&idb=TV&nol=123490");
        put("tv5", "http://live.irib.ir/?idc=C0&idb=TV&nol=123491");
        put("irinn", "http://live.irib.ir/?idc=C0&idb=TV&nol=123492");
        put("amouzesh", "http://live.irib.ir/?idc=C0&idb=TV&nol=123493");
        put("quran", "http://live.irib.ir/?idc=C0&idb=TV&nol=123494");
        put("mostanad", "http://live.irib.ir/?idc=C0&idb=TV&nol=123495");
        put("namayesh", "http://live.irib.ir/?idc=C0&idb=TV&nol=123498");
        put("ofogh", "http://live.irib.ir/?idc=C0&idb=TV&nol=123499");
        put("varzesh", "http://live.irib.ir/?idc=C0&idb=TV&nol=123503");
        put("pouya", "http://live.irib.ir/?idc=C0&idb=TV&nol=123485");
        put("salamat", "http://live.irib.ir/?idc=C0&idb=TV&nol=123504");
        put("nasim", "http://live.irib.ir/?idc=C0&idb=TV&nol=123505");
        put("tamasha", "http://live.irib.ir/?idc=C0&idb=TV&nol=123525");
        put("omid-tv", "http://live.irib.ir/?idc=C0&idb=TV&nol=123560");
        put("shoma", "http://live.irib.ir/?idc=C0&idb=TV&nol=123496");
        put("iran-kala", "http://live.irib.ir/?idc=C0&idb=TV&nol=123497");
        put("shinamak", "http://live.irib.ir/?idc=C0&idb=TV&nol=123657");
        put("shinama", "http://live.irib.ir/?idc=C0&idb=TV&nol=123615");
        put("shiran", "http://live.irib.ir/?idc=C0&idb=TV&nol=123652");
        put("shijam", "http://live.irib.ir/?idc=C0&idb=TV&nol=123658");
    }};

    private final Logger       logger;
    private final OkHttpClient client;

    public IribLiveProvider() {
        logger = LoggerFactory.getLogger(BBCProvider.class);
        client = new OkHttpClient();
    }

    @Override
    public String provide() {
        StringBuilder channels = new StringBuilder();
        Map<String, String> channelMap = IRIB_CHANNELS_MAP.keySet()
                                                          .parallelStream()
                                                          .collect(Collectors.toMap(Function.identity(), key ->
                                                                  getChannelLink(IRIB_CHANNELS_MAP.get(key))));
        IRIB_CHANNELS_MAP.keySet().forEach(channel -> {
            channels.append("#EXTINF:-1,");
            channels.append(channel);
            channels.append("\n");
            channels.append(channelMap.get(channel));
            channels.append("\n");
        });
        return channels.toString();
    }

    @Override
    public String provide(final String tvDesc) {
        if (IRIB_CHANNELS_MAP.containsKey(tvDesc)) {
            return getChannelLink(IRIB_CHANNELS_MAP.get(tvDesc));
        }
        return "";
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
                return body.substring(startQuotIndex, endQuotIndex);
            }
        } catch (Exception e) {
            logger.debug("Error in fetching channel link", e);
        }
        return "";
    }
}
