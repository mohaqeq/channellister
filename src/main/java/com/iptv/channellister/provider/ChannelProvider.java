package com.iptv.channellister.provider;

public interface ChannelProvider {

    String provide();

    String provide(String tvDesc);
}
