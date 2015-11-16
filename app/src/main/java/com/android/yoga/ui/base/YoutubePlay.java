package com.android.yoga.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.yoga.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Hoang Hiep on 9/7/2015.
 */
public class YoutubePlay extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 10;
    public static final String API_KEY = "AIzaSyCCrAmTuviLjOScT6qTcj34LUfFe_80kRI";

    //From URL -> https://www.youtube.com/watch?v=kHue-HaXXzg
    // Let It Go : "Frozen"
    public static final String VIDEO_ID = "kHue-HaXXzg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeplay);

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubeView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("YouTube Error (%1$s)",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setFullscreen(true);
        if (!wasRestored) {
            Intent intent = getIntent();
            final String VIDEOID = intent.getStringExtra(VIDEO_ID);
            player.cueVideo(VIDEOID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player);
    }

    /*private class FullScreenListener implements YouTubePlayer.OnFullscreenListener{

        @Override
        public void onFullscreen(boolean isFullscreen) {
            //Called when fullscreen mode changes.

        }

    }

    private class PlaybackListener implements YouTubePlayer.PlaybackEventListener{

        @Override
        public void onBuffering(boolean isBuffering) {
            // Called when buffering starts or ends.

        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to pause() or user action.

        }

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to play() or user action.

        }

        @Override
        public void onSeekTo(int newPositionMillis) {
            // Called when a jump in playback position occurs,
            //either due to the user scrubbing or a seek method being called

        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.

        }

    }

    private class PlayerStateListener implements YouTubePlayer.PlayerStateChangeListener{

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason reason) {
            // Called when an error occurs.

        }

        @Override
        public void onLoaded(String arg0) {
            // Called when a video has finished loading.

        }

        @Override
        public void onLoading() {
            // Called when the player begins loading a video and is not ready to accept commands affecting playback

        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.

        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.

        }

    }

    private class PlayListListener implements YouTubePlayer.PlaylistEventListener{

        @Override
        public void onNext() {
            // Called before the player starts loading the next video in the playlist.

        }

        @Override
        public void onPlaylistEnded() {
            // Called when the last video in the playlist has ended.

        }

        @Override
        public void onPrevious() {
            // Called before the player starts loading the previous video in the playlist.

        }

    }*/

}
