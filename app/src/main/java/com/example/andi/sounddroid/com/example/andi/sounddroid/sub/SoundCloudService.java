package com.example.andi.sounddroid.com.example.andi.sounddroid.sub;

import com.example.andi.sounddroid.com.example.andi.sounddroid.sub.Track;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 7/2/15.
 */
public interface SoundCloudService {

    static final String CLIENT_ID = "b1ecb77ec9923b918f182b09a224b424";

    @GET("/tracks?client_id="+CLIENT_ID)
    public void searchSongs(@Query("q") String query, Callback<List<Track>> cb);

    @GET("/tracks?client_id="+CLIENT_ID)
    public void getRecentSongs(@Query("created_at[from]") String date, Callback<List<Track>> cb);

}
