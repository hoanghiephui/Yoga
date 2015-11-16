package com.android.yoga.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.yoga.R;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

/**
 * @author by Hoang Hiep on 11/16/2015.
 */
public class YouTubeUtils {
    private static final String TAG = "YouTubeUtils";

    public static void showYouTubeVideo(String videoId, Activity activity) {
        if (!TextUtils.isEmpty(videoId)) {
            Intent liveIntent;
            if (YouTubeIntents.isYouTubeInstalled(activity) && YouTubeApiServiceUtil
                    .isYouTubeApiServiceAvailable(activity)
                    == YouTubeInitializationResult.SUCCESS) {
                // YouTube service is available.
                Log.d(TAG, "YouTube service available.");
                // start the YouTube player
                liveIntent = YouTubeStandalonePlayer.createVideoIntent(activity, activity.getResources().getString(R.string.api_youtube), videoId);
            } else if (YouTubeIntents.canResolvePlayVideoIntent(activity)) {
                // The YouTube app may not be fully up-to-date but it is installed and can resolve
                // intents.
                Log.d(TAG, "YouTube can resolve the intent.");
                // Start an intent to the YouTube app
                liveIntent = YouTubeIntents.createPlayVideoIntent(activity, videoId);
            } else {
                // YouTube may not be installed or it may be disabled.
                Log.d(TAG, "Redirecting to a browser.");
                liveIntent = new Intent(Intent.ACTION_VIEW);
                liveIntent.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoId));
            }
            activity.startActivity(liveIntent);
        } else {
            Toast.makeText(activity, R.string.video_id_not_valid,
                    Toast.LENGTH_LONG).show();
        }
    }
}
