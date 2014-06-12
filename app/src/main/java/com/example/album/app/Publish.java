package com.example.album.app;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Like;
import com.sromku.simple.fb.listeners.OnCommentsListener;
import com.sromku.simple.fb.listeners.OnLikesListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPublishListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anwaar on 21/04/2014.
 */
public class Publish extends Activity {


    private ProgressDialog mProgress;
    private SimpleFacebook mSimpleFacebook;
    protected static final String TAG = MainActivity.class.getName();
    ListView lv_comments;
    private static String APP_ID = "1435860576652630"; // Replace with your App ID
    String post_id=null;
    Button mButtonPublishFeed,mButtonGetComments,mButtonLogin,mButtonGetLikes;
    TextView like_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_post);
        initialize();
        loginExample();
        publishFeedExample();
        getCommentsExample();
        getLikesExample();
    }

    private void initialize() {
        mButtonPublishFeed=(Button) findViewById(R.id.new_post);
        mButtonGetComments=(Button) findViewById(R.id.comments);
        mButtonLogin=(Button) findViewById(R.id.login_btn);
        lv_comments=(ListView)findViewById(R.id.listView_comments);
        mButtonGetLikes=(Button) findViewById(R.id.btn_likes);
        like_textview=(TextView) findViewById(R.id.likes);

    }

    // Login listener
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
            toast("Processing");
        }

        @Override
        public void onLogin() {
            // change the state of the button or do whatever you want
            toast("You are logged in");
        }

        @Override
        public void onNotAcceptingPermissions(Permission.Type type) {
            toast(String.format("You didn't accept %s permissions", type.name()));
        }
    };

    private void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void publishFeedExample() {
        // listener for publishing action
        final OnPublishListener onPublishListener = new OnPublishListener() {

            @Override
            public void onFail(String reason) {
                hideDialog();
                // insure that you are logged in before publishing
                Log.w(TAG, "Failed to publish");
            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();
            }

            @Override
            public void onThinking() {
                // show progress bar or something to the user while publishing
                toast("Processing");
                showDialog();
            }

            @Override
            public void onComplete(String postId) {
                hideDialog();
                toast("Published successfully. The new post id = " + postId);
                post_id=postId;
            }
        };

        // feed builder
        final Feed feed = new Feed.Builder()
                .setMessage("Album")
                .setName("Album android App")
                .setCaption("Its all about sharing moments")
                .setDescription("This app provides you to share your photos. Like App official page.")
                .setPicture("http://img1.wikia.nocookie.net/__cb20140420205329/ssb/images/b/bf/Abc.png").setLink("https://www.facebook.com/pages/Album/1399781043626089").build();

        // click on button and publish
        mButtonPublishFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleFacebook.publish(feed, true, onPublishListener);
            }
        });
    }
    private void showDialog() {
        mProgress = ProgressDialog.show(this, "Thinking", "Waiting for Facebook", true);
    }
    private void hideDialog() {
        if (mProgress != null) {
            mProgress.hide();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }
    private void getCommentsExample() {
        toast("id is: "+post_id);
        final OnCommentsListener onCommentsListener = new OnCommentsListener() {

            @Override
            public void onFail(String reason) {
                hideDialog();
                Log.w(TAG, reason);
            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();
                Log.e(TAG, "Bad thing happened", throwable);
            }

            @Override
            public void onThinking() {
                showDialog();
                Log.i(TAG, "Thinking...");
            }

            @Override
            public void onComplete(List<Comment> response) {
                hideDialog();
                Log.i(TAG, "Number of comments = " + response.size());
                toast("Number of comments = " + response.size());
                ArrayList<String> comments=new ArrayList<String>();
                for(int i=0;i<response.size();i++)
                {
                    comments.add(response.get(i).getFrom()+"by:"+response.get(i).getMessage()+"at "+response.get(i).getCreatedTime());
                }
                ArrayAdapter<String> mycomments=new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_dropdown_item,comments);
                lv_comments.setAdapter(mycomments);
                toast("Comment: "+ response);
                Log.i(TAG, "comments : " + response.get(0).getMessage());

            }
        };

        mButtonGetComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Post id = " + post_id);
                mSimpleFacebook.getComments(post_id, onCommentsListener);
            }
        });
    }
    private void getLikesExample() {
        final String entityId = "14104316802_522484207864952";
        final OnLikesListener onLikesListener = new OnLikesListener() {

            @Override
            public void onFail(String reason) {
                hideDialog();
                Log.w(TAG, reason);
            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();
                Log.e(TAG, "Bad thing happened", throwable);
            }

            @Override
            public void onThinking() {
                showDialog();
                Log.i(TAG, "Thinking...");
            }

            @Override
            public void onComplete(List<Like> response) {
                hideDialog();
                Log.i(TAG, "Number of likes = " + response.size());
                toast("Number of likes = " + response.size());
                like_textview.setText(response.size());
            }
        };

        mButtonGetLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleFacebook.getLikes(post_id, onLikesListener);
            }
        });
    }

    private void loginExample() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSimpleFacebook.login(mOnLoginListener);
                toast("OUT");
            }
        });
    }

}
