package com.example.perry.yoursidesystem.fragment.heathfragment;

import android.graphics.drawable.Drawable;

/**
 * Created by perry on 2017/12/18.
 */

public class HeathTitle {
    private int title;
    private int image1,image2,image3;

    public HeathTitle(int title, int image1, int image2, int image3) {
        this.title = title;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public int getTitle() {
        return title;
    }

    public int getImage1() {
        return image1;
    }

    public int getImage2() {
        return image2;
    }

    public int getImage3() {
        return image3;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public void setImage2(int image2) {
        this.image2 = image2;
    }

    public void setImage3(int image3) {
        this.image3 = image3;
    }
}
