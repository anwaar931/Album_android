package com.example.album.app;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private int count;
    boolean isOnline;
    private ArrayList<ImageDetails> arrPath;
    ActivityManager activityManager;
    ActivityManager.MemoryInfo mi;
    // Keep all Images in array

    // Constructor
    public ImageAdapter(Context c,ArrayList<ImageDetails> arrimage,boolean isOnline){

        mContext = c;
        this.isOnline = isOnline;
         /*something*/
        arrPath = arrimage;
        this.count = arrimage.size();
           /* ------------*/
    }

    @Override
    public int getCount() {

        return this.count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        final LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        convertView = inflater.inflate(R.layout.images, parent, false);
        holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.imageView.setAdjustViewBounds(false);
        String imgFile = arrPath.get(position).getUrl();
        final int pos = position;
        if(imgFile != null && holder.imageView!=null )
        {
            if(isOnline)
            {
                Executor myExecutor = Executors.newFixedThreadPool(4);
                try {
                    new Async_OnlineImages(holder.imageView,mContext).executeOnExecutor(myExecutor,imgFile);
                    // new Async_OnlineImages(holder.imageView,mContext).execute(imgFile);
                }
                catch (Exception ex)
                {
                   /* BugSenseHandler.startSession(mContext);

                    Log.e("Bugs", ex.getMessage());
                    ex.printStackTrace(); // in case you want to see the stacktrace in your log cat output
                    BugSenseHandler.sendException(ex);

                    BugSenseHandler.closeSession(mContext);*/
                    ex.getMessage();
                }
            }
            else
            {
                // Executor myExecutor = Executors.newFixedThreadPool(1);
                new Async_Images(holder.imageView,mContext,pos).execute(pos);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}