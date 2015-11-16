package com.android.yoga.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.yoga.font.FontsOverride;
import com.android.yoga.ui.activity.MainActivity;
import com.android.yoga.volley.LruBitmapCache;
import com.parse.Parse;
import com.parse.ParseInstallation;

import java.io.File;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by Hoang Hiep on 8/28/2015.
 */
public class Applications extends Application {
    public static final String TAG = Applications.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    private static Applications mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setRestartActivityClass(MainActivity.class);
        FontsOverride.setDefaultFont(this, "DEFAULT", "roboto_condensed.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "roboto_condensed.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "roboto_condensed.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "roboto_condensed.ttf");
        Parse.initialize(this, "VUZp6egcw61eHwdMEmhEky1XfqKzhCE7cqJ4sSS3", "VyqB9WVikV0er8TXK78D4VzGOhICptyvWQHpO3hz");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static synchronized Applications getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    //clear cache
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir != null && appDir.isDirectory()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static Context getAppContext() {
        return mInstance;
    }
}
