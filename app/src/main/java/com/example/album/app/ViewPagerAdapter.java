package com.example.album.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    Context activity;
    ArrayList<ImageDetails> imageArray;
    String type;
    public ViewPagerAdapter(Context act, ArrayList<ImageDetails> imgArra,String type) {
        imageArray = imgArra;
        activity = act;
        this.type = type;
    }

    public int getCount() {
        return imageArray.size();
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        ImageView view = new ImageView(activity);

        Bitmap bmp = null;

        if(type == "Online")
        {
            new Async_OnlineImages(view,activity).execute(imageArray.get(position).getUrl());
        }
        else if(type=="Images")
        {
            bmp = Async_Images.decodeSampledBitmapFromPath(imageArray.get(position).getUrl(),150,150);
        }

        view.setImageBitmap(bmp);

        ((ViewPager) collection).addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
