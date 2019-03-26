package com.iptv.channellister.client;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TelewebionClient {

    @GET("/v2/channels/getChannelLinks")
    @Headers({
            "Origin: https://www.telewebion.com",
            "Referer: https://www.telewebion.com"
    })
    Call<JsonNode> getChannelLinks(@Query("channel_desc") String channelDesc,
                                   @Query("device") String device,
                                   @Query("logo_version") int logoVersion);
}
