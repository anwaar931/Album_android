package com.example.album.app;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter {
    private Context mContext;

    private int count;

    private String[] arrPath;

    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    String fileType;
    // Keep all Images in array

    // Constructor
    public VideoAdapter(Context c,String fileType){
        mContext = c;
        this.fileType = fileType;

         /*something*/
         arrPath = getPath.arrVideoPath;
        this.count = arrPath.length;
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
        ViewHolder holder;

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.videos, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_video);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            holder.imageView.setAdjustViewBounds(false);

            convertView.setTag(holder);


        String imgFile = arrPath[position];
        if(imgFile != null && holder.imageView!=null)
        {
            holder.imageView.setTag(imgFile);
            new Async_Videos(holder.imageView).execute(imgFile);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}