package com.example.andi.sounddroid.com.example.andi.sounddroid.sub;

import retrofit.RestAdapter;

/**
 * Created by andi on 7/3/15.
 */
public class SoundCloud {

    private static final String API_URL = "http://api.soundcloud.com";

    private static final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(API_URL).build();

    private static final SoundCloudService sSoundCloudService = restadapter.create(SoundCloudService.class);

    public static SoundCloudService getSoundCloudService(){
        return sSoundCloudService;

    }
}
