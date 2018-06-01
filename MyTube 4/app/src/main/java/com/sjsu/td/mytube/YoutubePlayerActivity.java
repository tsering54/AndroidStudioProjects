package com.sjsu.td.mytube;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_player);

        playerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        playerView.initialize(YoutubeLinker.KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        if (!restored) {
            youTubePlayer.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
            //Toast.makeText(getApplicationContext(), "Video id: "+ getIntent().getStringExtra("VIDEO_ID"), Toast.LENGTH_SHORT).show();
        }
    }
}
