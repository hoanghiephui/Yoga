package com.android.yoga.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.android.yoga.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdWrapper extends FrameLayout {

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public AdWrapper(Context context) {
        super(context);
        init(context);
    }

    public AdWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.ads_wrapper, this, true);
        //Ads
        //if(!Utils.isTelevision(context)) {
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setAdListener(adListener);
        //} else {
        // mInterstitialAd = new InterstitialAd(context);
        //mInterstitialAd.setAdUnitId("ca-app-pub-2257698129050878/4313141545");
        //requestNewInterstitial();
        //}
    }

    private void requestNewInterstitial() {
        if (null != mInterstitialAd) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    private void showInterstitial() {
        if (null != mInterstitialAd) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //showInterstitial();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            return;
        }
        //Fixes GPS AIOB Exception
        try {
            if (null != mAdView) {
                mAdView.loadAd(new AdRequest.Builder().build());
            }
        } catch (Exception ignored) {
        }
    }

    AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            mAdView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            mAdView.setVisibility(View.GONE);
        }
    };
}
