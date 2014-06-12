package com.example.album.app;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class getPath {
    public static boolean isRunning = true;
    public static ArrayList<ImageDetails> arrImagePath;
    public static String[] arrVideoPath;
    public static ArrayList<ImageDetails> arrOnlinePath;
    public static ArrayList<bitThumbs> mThumbs;
    public static int imagecount=0;
    public static int videocount=0;
    public static void getAllBitmapsThumb()
    {
        mThumbs = new ArrayList<bitThumbs>();

        for(int i=0;i<arrImagePath.size();i++)
        {
            bitThumbs obj = new bitThumbs();
            obj.setPosition(i);
            Bitmap bmp = Async_Images.decodeSampledBitmapFromPath(arrImagePath.get(i).getUrl(),30,30);
            obj.setBmp(bmp);
            mThumbs.add(obj);
        }
    }

    public static Bitmap addBitmapPos(int pos)
    {
        bitThumbs obj = new bitThumbs();
        obj.setPosition(pos);
        Bitmap bmp = Async_Images.decodeSampledBitmapFromPath(arrImagePath.get(pos).getUrl(),90,90);
        obj.setBmp(bmp);
        mThumbs.add(obj);
        return bmp;
    }

    public static void addBitmapThumb(int pos,Bitmap bitmap)
    {
        bitThumbs obj = new bitThumbs();
        obj.setPosition(pos);
        obj.setBmp(bitmap);
        int max_size = 50; //max_size of loading bitmap images
        if(mThumbs.size() < max_size)
        {
            mThumbs.add(obj);
        }
        else
        {
            int random = randInt(0,max_size-1);
            mThumbs.set(random,obj);
        }
    }

    public static Bitmap getBitmap(int pos)
    {
        for(int i=0;i<mThumbs.size();i++)
        {
            if(mThumbs.get(i).getPosition()==pos)
            {
                return mThumbs.get(i).getBmp();
            }
        }
        return null;
    }

    public static void getAllImages(Context mContext)
    {
        int count;

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        count = imagecursor.getCount();
        arrImagePath = new ArrayList<ImageDetails>();

        for (int i = 0; i < count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);

            ImageDetails imgDetails = new ImageDetails(imagecursor.getString(dataColumnIndex));
            arrImagePath.add(imgDetails);
        }

        imagecount = arrImagePath.size();
        Collections.sort(arrImagePath, new Comparator<ImageDetails>() {
            @Override
            public int compare(ImageDetails imageDetails, ImageDetails imageDetails2) {
                if(imageDetails2.getDate().getTime() > imageDetails.getDate().getTime())
                {
                    return 1;
                }
                else
                    return 0;
            }
        });
        imagecursor.close();
    }

   /* public static Date getDate(String filepath)
    {
        File file =new File(filepath);
        return file.lastModified();
    }*/

    public static void getAllVideos(Context mContext)
    {
        int count = 0;

        final String[] columns = { MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID };
        final String orderBy = MediaStore.Video.Media._ID;
        Cursor videocursor = mContext.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int video_column_index = videocursor.getColumnIndex(MediaStore.Video.Media._ID);
        count = videocursor.getCount();
        arrVideoPath = new String[count];

        for (int i = 0; i < count; i++) {
            videocursor.moveToPosition(i);
            int id = videocursor.getInt(video_column_index);
            int dataColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DATA);

            arrVideoPath[i]= videocursor.getString(dataColumnIndex);
        }
        videocount = arrVideoPath.length;
        videocursor.close();
    }
    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int getImagecount()
    {
        return imagecount;
    }
    public static int getVideocount()
    {
        return videocount;
    }
}

