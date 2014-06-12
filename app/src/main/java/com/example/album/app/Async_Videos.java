package com.example.album.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

class Async_Videos extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference thumbViewReference;
    private String imgPath;
    public Async_Videos(ImageView thumbView) {
        thumbViewReference = new WeakReference(thumbView);
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        imgPath = params[0];
        return decodeSampledBitmapFromPath(params[0], 100,100);
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (thumbViewReference != null) {
            ImageView imageView = (ImageView) thumbViewReference.get();
            if (imageView != null) {

                if (bitmap != null&& (imgPath.equals(imageView.getTag()))) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setAnimation(null);
                }
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {

      /*  final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
*/
        // Decode bitmap with inSampleSize set
       // options.inJustDecodeBounds = false;
       // Bitmap bmp = BitmapFactory.decodeFile(path, options);
        Bitmap bmp = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
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