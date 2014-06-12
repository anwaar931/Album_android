package com.example.album.app;

import android.graphics.Bitmap;

/**
 * Created by Saad on 3/28/2014.
 */

public class bitThumbs
{
    private int position;
    private Bitmap bmp;

    public bitThumbs()
    {
        position=-1;
        bmp = null;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public int getPosition() {
        return position;
    }
}
