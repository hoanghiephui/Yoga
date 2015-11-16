/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.yoga.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.yoga.R;
import com.android.yoga.model.YogaItems;

public class AboutsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setInitialConfiguration();
        setScreenElements();

    }

    private void setInitialConfiguration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_about);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(YogaItems.darker(getResources().getColor(R.color.colorPrimary), 0.8));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }


    }

    private void setScreenElements() {
        TextView header = (TextView) findViewById(R.id.header);
        TextView appNameVersion = (TextView) findViewById(R.id.app_name);
        CardView about_1 = (CardView) findViewById(R.id.about_1);
        CardView about_2 = (CardView) findViewById(R.id.about_google);

        CardView about_googleplay = (CardView) findViewById(R.id.about_googleplay);

        header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        appNameVersion.setText(getResources().getString(R.string.app_name) + " v" + YogaItems.getAppVersionName(getApplicationContext()));
        about_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=RAT")));
            }
        });
        about_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+HoàngHiệp176/")));
            }
        });
        about_googleplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(YogaItems.goToGooglePlay("net.tichluy.newsrss"));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

}