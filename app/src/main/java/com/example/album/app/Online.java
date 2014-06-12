package com.example.album.app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Photo;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPhotosListener;

import java.util.ArrayList;
import java.util.List;

public class Online extends Fragment {
    private static final String TAG = "Facebook API";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    SimpleFacebook mSimpleFacebook;
    Context mContext;
    PullToRefreshGridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_online,container,false);
        mSimpleFacebook = SimpleFacebook.getInstance(getActivity());
        getAllOnlineImagesPath();

        gridView = (PullToRefreshGridView)convertView.findViewById(R.id.grid_view_online);
        final MediaPlayer mp = MediaPlayer.create(getActivity(),R.raw.refreshing_sound);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                mp.start();

                mSimpleFacebook.getPhotos(RefreshOnPhotosListener);
                imageAdapter.notifyDataSetChanged();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), com.example.album.app.Scale_OnlineImage.class);
                intent.putExtra("id", i);

                getActivity().startActivity(intent);
            }
        });

        return convertView;
    }

    public void getAllOnlineImagesPath()
    {
        if(mSimpleFacebook.isLogin())
            mSimpleFacebook.getPhotos(mOnPhotosListener);
        if(!mSimpleFacebook.isLogin())
        {
            mSimpleFacebook.login(mOnLoginListener);
        }
    }

    private OnLoginListener mOnLoginListener = new OnLoginListener() {

        @Override
        public void onFail(String reason) {

            Log.e(TAG, "Failed to login" + reason);
        }

        @Override
        public void onException(Throwable throwable) {

            Log.e(TAG, "Bad thing happened", throwable);
        }

        @Override
        public void onThinking() {
            // show progress bar or something to the user while login is
            // happening
            toast("Thinking...");
        }

        @Override
        public void onLogin() {
            // change the state of the button or do whatever you want
            toast("You are logged in");
            if(mSimpleFacebook.isLogin())
                mSimpleFacebook.getPhotos(mOnPhotosListener);
        }

        @Override
        public void onNotAcceptingPermissions(Permission.Type type) {
            toast("Permission Denied");
        }
    };

    ImageAdapter imageAdapter=null;
    OnPhotosListener mOnPhotosListener = new OnPhotosListener() {
        @Override
        public void onComplete(List<Photo> photos) {
            Log.i(TAG, "Number of photos = " + photos.size());
            getPath.arrOnlinePath = new ArrayList<ImageDetails>();

            for(int i=0;i<photos.size();i++)
            {
                ImageDetails imageDetails = new ImageDetails();
                imageDetails.setEntityId(photos.get(i).getId());
                imageDetails.setUrl(photos.get(i).getSource());
                getPath.arrOnlinePath.add(imageDetails);
                Log.e(TAG,getPath.arrOnlinePath.get(i).getUrl());
            }

            imageAdapter = new ImageAdapter(getActivity(),getPath.arrOnlinePath,true);
            gridView.setAdapter(imageAdapter);
        }
        @Override
        public void onThinking() {
            // show progress bar or something to the user while login is
            // happening
            toast("Thinking...");
        }
        @Override
        public void onFail(String reason) {

            Log.e(TAG, "Failed to Load images" + reason);
        }

        @Override
        public void onException(Throwable throwable) {

            Log.e(TAG, "Bad thing happened", throwable);
        }

    };

    OnPhotosListener RefreshOnPhotosListener = new OnPhotosListener() {
        @Override
        public void onComplete(List<Photo> photos) {
            Log.i(TAG, "Number of photos = " + photos.size());
            getPath.arrOnlinePath = new ArrayList<ImageDetails>();
            boolean check = false;
            for(int i=0;i<photos.size();i++)
            {
                for(int j=0;j<getPath.arrOnlinePath.size();j++)
                {
                    if(photos.get(i).getId()==getPath.arrOnlinePath.get(j).getEntityId()&&i!=j)
                    {
                        check = true;
                        break;
                    }
                }
                if(!check)
                {
                    ImageDetails imageDetails = new ImageDetails();
                    imageDetails.setEntityId(photos.get(i).getId());
                    imageDetails.setUrl(photos.get(i).getSource());
                    getPath.arrOnlinePath.add(imageDetails);
                    Log.e(TAG,getPath.arrOnlinePath.get(i).getUrl());
                }
                check=false;
            }
            toast("Completed");

            imageAdapter.notifyDataSetChanged();
            gridView.onRefreshComplete();
        }
        @Override
        public void onThinking() {
            // show progress bar or something to the user while login is
            // happening
            toast("Thinking...");
        }
        @Override
        public void onFail(String reason) {

            Log.e(TAG, "Failed to Load images" + reason);
        }

        @Override
        public void onException(Throwable throwable) {

            Log.e(TAG, "Bad thing happened", throwable);
        }
    };

    private void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
