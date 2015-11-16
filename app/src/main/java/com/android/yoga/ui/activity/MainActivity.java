package com.android.yoga.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.yoga.R;
import com.android.yoga.ui.fragment.MainFragment;
import com.android.yoga.ui.fragment.level.MainLevel;
import com.android.yoga.ui.fragment.type.MainType;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    Fragment mFragment = null;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    String title = "Yoga";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        mFragment = new MainType();
        mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.all:
                                menuItem.setChecked(true);
                                title = (String) menuItem.getTitle();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                mFragment = MainFragment.newInstance("All");
                                break;
                            case R.id.type:
                                menuItem.setChecked(true);
                                title = (String) menuItem.getTitle();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                mFragment = new MainType();
                                break;
                            case R.id.level:
                                menuItem.setChecked(true);
                                title = (String) menuItem.getTitle();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                mFragment = new MainLevel();
                                break;
                            case R.id.goal:
                                menuItem.setChecked(true);
                                title = (String) menuItem.getTitle();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(new Intent(getApplicationContext(), MainGoal.class));
                                break;
                            case R.id.duration:
                                menuItem.setChecked(true);
                                title = (String) menuItem.getTitle();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                mFragment = MainFragment.newInstance("duration");
                                break;
                            case R.id.about:
                                title = (String) menuItem.getTitle();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(new Intent(getApplicationContext(), AboutsActivity.class));
                                break;
                            case R.id.setting:
                                title = (String) menuItem.getTitle();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                                break;
                        }
                        if (mFragment != null) {
                            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
                            assert getSupportActionBar() != null;
                            getSupportActionBar().setTitle(title);
                        }
                        return true;
                    }
                }
        );
    }


}
