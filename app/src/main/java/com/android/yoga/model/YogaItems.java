package com.android.yoga.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Hoang Hiep on 8/28/2015.
 */
public class YogaItems implements Serializable {
    private int id;
    private String title, title_vn, desc, desc_vn, image, author, profilePic, url, url_vn, pic, lang;
    public int color = Color.parseColor("#1976D2");

    public YogaItems() {
    }

    public YogaItems(String title, String title_vn, String desc, String desc_vn, String image, String author, String profilePic, String url, String url_vn, String pic) {
        this.title = title;
        this.title_vn = title_vn;
        this.desc = desc;
        this.desc_vn = desc_vn;
        this.image = image;
        this.author = author;
        this.profilePic = profilePic;
        this.url = url;
        this.url_vn = url_vn;
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_vn() {
        return title_vn;
    }

    public void setTitle_vn(String title_vn) {
        this.title_vn = title_vn;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc_vn() {
        return desc_vn;
    }

    public void setDesc_vn(String desc_vn) {
        this.desc_vn = desc_vn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_vn() {
        return url_vn;
    }

    public void setUrl_vn(String url_vn) {
        this.url_vn = url_vn;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public static Intent goToGooglePlay(String id) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + id));

        return intent;
    }

    public static String getAppVersionName(Context context) {
        String res = "0.0.0.0";
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static int darker(int color, double factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));
    }
}
