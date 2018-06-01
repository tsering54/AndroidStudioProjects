package com.sjsu.td.mytube;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeLinker {
    private YouTube youtube;
    private ArrayList<VideoItem> videoItems;
    private YouTube.Search.List query;
    public static final String KEY = "AIzaSyAaLMzQrUrZkvEOu9mUeBJx6dKAyDnR2zI";
    public static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    public YoutubeLinker(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        videoItems = new ArrayList<>();
        try {
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");

        } catch (IOException e) {
            Log.d("YTC", "Could not initialize:  " + e);
        }
    }


    public ArrayList<VideoItem> search(String keywords) {
        query.setQ(keywords);
        query.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
        try {
            SearchListResponse response = query.execute();
            ArrayList<SearchResult> results = (ArrayList)response.getItems();


            for (SearchResult result : results) {
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                videoItems.add(item);
            }
            return videoItems;

        } catch (IOException e) {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }

    public ArrayList<VideoItem> getVideoItems() {
        return videoItems;
    }
}
