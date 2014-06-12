package com.example.album.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Saad on 5/3/2014.
 */
public class textAdapter extends BaseAdapter {
    private Context mContext;

    private int count;
    boolean isOnline;
    private String[] comments;
    // Keep all Images in array

    // Constructor
    public textAdapter(Context c,String[] comments){

        mContext = c;
        this.comments = comments;
        this.count = this.comments.length;
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

        final LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        convertView = inflater.inflate(R.layout.list_fb_item, parent, false);

        TextView textView = (TextView)convertView.findViewById(R.id.tv_list_item);
        textView.setText(comments[position]);

        return convertView;
    }
}
