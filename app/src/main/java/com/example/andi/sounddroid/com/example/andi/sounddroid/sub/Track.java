package com.example.andi.sounddroid.com.example.andi.sounddroid.sub;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andi on 7/2/15.
 */
public class Track {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("id")
    private int mID;

    @SerializedName("artwork_url")
    private String mArtWorkURL;

    public String getTitle() {
        return mTitle;
    }

    public String getArtWorkURL() {
        return mArtWorkURL;
    }

    public String getAvatarURL(){
        String avatarURL = mArtWorkURL;
        if (avatarURL != null){
            avatarURL = avatarURL.replace("large","tiny");
        }
        return avatarURL;
    }


    public String getStreamURL() {
        return mStreamURL;
    }



    public int getID() {
        return mID;
    }





}
