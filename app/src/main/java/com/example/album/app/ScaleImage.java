package com.example.album.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Photo;
import com.sromku.simple.fb.entities.Privacy;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPublishListener;

import java.util.ArrayList;
import java.util.Set;

public class ScaleImage extends Activity  {

    private static final String TAG = "Album ";
    /* GET ALL IMAGE FIELS */

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> BTArrayAdapter;

    Bitmap gbmp;
    ProgressDialog mProgress;
    ViewPager myPager;
    private int count=0;
    private int current_index = 0;
    private ArrayList<ImageDetails> arrPath;
    SimpleFacebook mSimpleFacebook;
    /*------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_image);
        Intent intent = this.getIntent();
        if(intent!=null)
        {
            current_index = intent.getExtras().getInt("id");
        }

        arrPath = getPath.arrImagePath;
        this.count = arrPath.size();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getApplication(), arrPath,"Images");
        myPager = (ViewPager) this.findViewById(R.id.pager);
        if(myPager==null)
            Log.e("ViewPager","MyPager is null");
        else
        {
            myPager.setAdapter(adapter);
            myPager.setCurrentItem(current_index);

            gbmp = Async_Images.decodeSampledBitmapFromPath(arrPath.get(current_index).getUrl(),180,180);
        }

        if (savedInstanceState == null) {

        }

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scale_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();
       switch (id)
       {

           case R.id.action_settings:{

               break;
           }
           case R.id.action_share_fb:{
               current_index = myPager.getCurrentItem();
               gbmp = Async_Images.decodeSampledBitmapFromPath(arrPath.get(current_index).getUrl(),180,180);

              /* mSimpleFacebook.getPhotos(new OnPhotosListener() {
                   @Override
                   public void onComplete(List<Photo> photos) {
                       Log.i(TAG, "Number of photos = " + photos.size());
                       for(int i=0;i<photos.size();i++)
                       {
                           Photo image = photos.get(i);
                           Log.e(TAG,image.getName() + " ### " + image.getPageStoryId()+ " ### " + image.getPath() + " ### " + image.getSource());
                       }
                   }
               });*/

               if(gbmp!=null) {
                   if(!mSimpleFacebook.isLogin())
                   {
                        mSimpleFacebook.login(new OnLoginListener() {

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
                                toast("Image is ready to be uploaded!");
                            }

                            @Override
                            public void onNotAcceptingPermissions(Permission.Type type) {
                                toast("Permission denied.");
                            }
                        });

                   }
                   showCustomDialog();

               }
               else
               {
                   toast("Image is null");
               }
               break;
           }
           case R.id.action_share_twitter:{

               break;
           }
           case R.id.action_share_bluetooth:{
               if(myBluetoothAdapter==null)
               {
                   toast("Bluetooth not Supported!");
                   break;
               }
                if(!myBluetoothAdapter.isEnabled())
                {
                    Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
                    //Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,Toast.LENGTH_LONG).show();
                }
                else
                {

                }

               break;
           }
           case R.id.action_Crop: {
               Intent i =new Intent(this,CropImage.class);
               String path = getPath.arrImagePath.get(myPager.getCurrentItem()).getUrl();
               i.putExtra("id",path);
               startActivity(i);
               break;
           }
           case R.id.action_pin_image:
           {
               Intent i =new Intent(this,PinImage.class);
               String path = getPath.arrImagePath.get(myPager.getCurrentItem()).getUrl();
               i.putExtra("id",path);
               startActivity(i);
               break;
           }
       }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        mProgress = ProgressDialog.show(this, "Connecting", "Waiting for Facebook", true);

    }

    private void showDialog(String head,String msg) {
        mProgress = ProgressDialog.show(getApplicationContext(),head,msg, true);
    }

    private void hideDialog() {
        if (mProgress != null) {
            mProgress.hide();
        }
    }

    private void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleFacebook.getInstance(this).onActivityResult(this, requestCode, resultCode, data);
    }

    protected void showCustomDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(ScaleImage.this);
        dialog.setTitle("Privacy");
        dialog.setContentView(R.layout.fb_share_dialoge);
        final EditText editText_status = (EditText)dialog.findViewById(R.id.editText_status);
        final Spinner spinner_privacy = (Spinner)dialog.findViewById(R.id.spinner_privacy);
        final Button btn_OK = (Button)dialog.findViewById(R.id.btn_ok);
        final Button btn_cancal = (Button)dialog.findViewById(R.id.btn_cancel);

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                final Bitmap bitmap = gbmp;
                String status_text = editText_status.getText().toString();
                // set privacy
                Privacy privacy = null;
                if(spinner_privacy.getSelectedItem().toString()=="Only Me")
                {
                    privacy = new Privacy.Builder()
                            .setPrivacySettings(Privacy.PrivacySettings.SELF)
                            .build();
                }
                else if(spinner_privacy.getSelectedItem().toString()=="Friends")
                {
                    privacy = new Privacy.Builder()
                            .setPrivacySettings(Privacy.PrivacySettings.ALL_FRIENDS)
                            .build();
                }
                else if(spinner_privacy.getSelectedItem().toString()=="Public")
                {
                    privacy = new Privacy.Builder()
                            .setPrivacySettings(Privacy.PrivacySettings.EVERYONE)
                            .build();
                }
                // create Photo instance and add some properties
                if(status_text==null)
                {
                    status_text = "";
                }
                Photo photo = new Photo.Builder()
                        .setImage(bitmap)
                        .setName(status_text)
                        .setPlace("110619208966868")
                        .setPrivacy(privacy)
                        .build();

                if(mSimpleFacebook.isLogin()) {
                    // publish
                    mSimpleFacebook.publish(photo, new OnPublishListener() {

                        @Override
                        public void onFail(String reason) {
                            //hideDialog();
                            // insure that you are logged in before publishing
                            Log.w(TAG, "Failed to publish");
                            toast(reason);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            hideDialog();
                            Log.e(TAG, "Bad thing happened", throwable);
                        }

                        @Override
                        public void onThinking() {
                            // show progress bar or something to the user while
                            // publishing
                            showDialog();
                        }

                        @Override
                        public void onComplete(String id) {
                            hideDialog();
                            toast("Published successfully. ");
                        }
                    });

                }
            }
        });
        btn_cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dialog.hide();
            }
        });

        dialog.show();
    }
}
