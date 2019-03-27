package com.iptv.channellister.provider.impl;

import com.iptv.channellister.provider.ChannelProvider;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//@Service()
public class IribLiveProvider implements ChannelProvider {

    private static Map<String, String> CHANNELS_MAP = new HashMap<String, String>() {{
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
        put("shinamak2", "http://live.irib.ir/?idc=C0&idb=TV&nol=123657");
        put("shinama1", "http://live.irib.ir/?idc=C0&idb=TV&nol=123615");
        put("shiran1", "http://live.irib.ir/?idc=C0&idb=TV&nol=123652");
        put("shijam", "http://live.irib.ir/?idc=C0&idb=TV&nol=123658");
        put("logo-seda-sima", "http://live.irib.ir/?idc=C0&idb=TV&nol=123612");
    }};

    @Override
    public String provide() {
        return "";
    }

    @Override
    public String provide(final String tvDesc) {
        return "";
    }
}
