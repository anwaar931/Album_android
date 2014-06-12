package com.example.album.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Like;
import com.sromku.simple.fb.listeners.OnCommentsListener;
import com.sromku.simple.fb.listeners.OnLikesListener;

import java.util.ArrayList;
import java.util.List;

public class Scale_OnlineImage extends Activity {

    SimpleFacebook mSimpleFacebook;
    ListView listView;
    TextView tv_likes;
    TextView tv_comments;
    int count;
    boolean comVisible=false;
    boolean likesVisible=false;
    ViewPager myPager;
    Bitmap gbmp;
    int current_index=0;
    ArrayList<ImageDetails> arrPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale__online_image);
        Intent intent = this.getIntent();
        if(intent!=null)
        {
            current_index = intent.getExtras().getInt("id");
        }

        listView = (ListView)findViewById(R.id.lv_comments);
        tv_likes = (TextView)findViewById(R.id.txtlikes);
        tv_comments = (TextView)findViewById(R.id.txtcomments);
        listView.setVisibility(View.GONE);

        tv_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSimpleFacebook.getLikes(arrPath.get(current_index).getEntityId(),onLikesListener);
                if(!likesVisible)
                    listView.setVisibility(View.VISIBLE);
                else
                    listView.setVisibility(View.GONE);
                likesVisible = !likesVisible;
            }
        });

        tv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSimpleFacebook.getComments(arrPath.get(current_index).getEntityId(),onCommentsListener);
                if(!comVisible)
                    listView.setVisibility(View.VISIBLE);
                else
                    listView.setVisibility(View.GONE);
                comVisible = !comVisible;
            }
        });

        arrPath = getPath.arrOnlinePath;
        this.count = arrPath.size();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getApplication(), arrPath,"Online");

        myPager = (ViewPager) this.findViewById(R.id.pager_online);

        if(myPager==null)
            Log.e("ViewPager", "MyPager is null");
        else
        {
            myPager.setAdapter(adapter);
            myPager.setCurrentItem(current_index);

            gbmp = Async_Images.decodeSampledBitmapFromPath(arrPath.get(current_index).getUrl(),150,150);
        }

        mSimpleFacebook = SimpleFacebook.getInstance(this);

        mSimpleFacebook.getLikes(arrPath.get(current_index).getEntityId(),onLikesListener);
        mSimpleFacebook.getComments(arrPath.get(current_index).getEntityId(),onCommentsListener);

        if (savedInstanceState == null) {

        }
    }

    String TAG = "FACEBOOK";

    OnLikesListener onLikesListener = new OnLikesListener() {
        @Override
        public void onComplete(List<Like> likes) {

            tv_likes.setText(likes.size()+ " Likes" );
            ArrayList<String> arrayList = new ArrayList<String>();
            for(int i=0;i<likes.size();i++)
            {
                arrayList.add(likes.get(i).getUser().getName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(arrayAdapter);
            Log.i(TAG, "Number of likes = " + likes.size());
        }

    /*
     * You can override other methods here:
     * onThinking(), onFail(String reason), onException(Throwable throwable)
     */
    };

    OnCommentsListener onCommentsListener = new OnCommentsListener() {
        @Override
        public void onComplete(List<Comment> comments) {

            tv_comments.setText(comments.size() + " Comments");
            ArrayList<String> arrayList = new ArrayList<String>();
            for(int i=0;i<comments.size();i++)
            {
                arrayList.add(comments.get(i).getMessage());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(arrayAdapter);
            Log.i(TAG, "Number of comments = " + comments.size());
        }

    /*
     * You can override other methods here:
     * onThinking(), onFail(String reason), onException(Throwable throwable)
     */
    };

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scale__online_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
