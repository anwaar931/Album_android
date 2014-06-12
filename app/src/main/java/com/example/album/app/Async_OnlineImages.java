package com.example.album.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

class Async_OnlineImages extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference imageViewReference;
    private Context mContext;

    public Async_OnlineImages(ImageView imageView,Context mContext) {
        imageViewReference = new WeakReference(imageView);
        this.mContext = mContext;
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        try {
            URL url = new URL(params[0]);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(url.openConnection()
                    .getInputStream(), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options,200, 200);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(url.openConnection()
                    .getInputStream(), null, options);

        } catch (Exception ex) {
                Log.e("Async Online Images", ex.getMessage());
        }

        return null;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null) {
            ImageView imageView = (ImageView) imageViewReference.get();
            if (imageView != null) {

                if (bitmap != null) {

                    imageView.setImageBitmap(bitmap);
                    imageView.setAnimation(null);
                }
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream inputStream, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(inputStream,null,options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeStream(inputStream,null,options);

        return bmp;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }
}