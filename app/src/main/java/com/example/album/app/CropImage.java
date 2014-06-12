package com.example.album.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Anwaar on 01/04/2014.
 */


public class CropImage extends Activity {
    private Uri mImageCaptureUri=null;
    private static final int CROP_IMAGE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        mImageCaptureUri=Uri.parse(i.getExtras().getString("id"));
        performCropImage();


    }
    private Uri mCropImagedUri;
    /**Crop the image
     * @return returns <tt>true</tt> if crop supports by the device,otherwise false*/
    private boolean performCropImage(){
        try {
            if(mImageCaptureUri!=null){
                //call the standard crop action intent (the user device may not support it)
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                //indicate image type and Uri
                cropIntent.setDataAndType(mImageCaptureUri, "image/*");
                //set crop properties
                cropIntent.putExtra("crop", "true");
                //indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("scale", true);
                //indicate output X and Y
                cropIntent.putExtra("outputX", 500);
                cropIntent.putExtra("outputY", 500);
                //retrieve data on return
                cropIntent.putExtra("return-data", false);

                File f = createNewFile("CROP_");
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    Log.e("io", ex.getMessage());
                }

                ///mCropImagedUri = Uri.fromFile(f);
                ///cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                //start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, CROP_IMAGE);
                return true;
            }
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return false;
    }

    private File createNewFile(String prefix){
        if(prefix==null || "".equalsIgnoreCase(prefix)){
            prefix="IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory()+"/mypics/");
        if(!newDirectory.exists()){
            if(newDirectory.mkdir()){
                ///Log.d(this.getName(), newDirectory.getAbsolutePath() + " directory created");
            }
        }
        File file = new File(newDirectory,(prefix+System.currentTimeMillis()+".jpg"));
        if(file.exists()){
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
