package com.example.album.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

	Context context;
    ArrayList<ImageDetails> items;
    int maxMemory;
	private LruCache<String, Bitmap> mMemoryCache;

	public ListAdapter(Context context) {
		this.context = context;
        items = getPath.arrImagePath;
		// Get memory class of this device, exceeding this amount will throw an
		// OutOfMemory exception.
		maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.e("Max Memory", String.valueOf(maxMemory));
		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 4;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number
				// of items.
				return bitmap.getByteCount();
			}
		};
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ImageView img;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        convertView = inflater.inflate(R.layout.images, arg2, false);
        img = (ImageView) convertView.findViewById(R.id.grid_item_image);
		img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setAdjustViewBounds(false);
        img.setTag(items.get(arg0));
        String resId = items.get(arg0).getUrl();
        int usedmemory = (int) ((Runtime.getRuntime().totalMemory() / 1024) - (Runtime.getRuntime().freeMemory()/1024));

        loadBitmap(resId, img);

		return convertView;
	}

	public void loadBitmap(String resId, ImageView imageView) {
		if (cancelPotentialWork(resId, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);

           // Executor myExecutor = Executors.newFixedThreadPool(5);
			task.execute(resId);
		}
	}

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	public static boolean cancelPotentialWork(String data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final String bitmapData = bitmapWorkerTask.data;
			if (bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return (Bitmap) mMemoryCache.get(key);
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		public String data = null;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected

			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];

			final Bitmap bitmap = decodeSampledBitmapFromPath(data,100,100);
			addBitmapToMemoryCache(params[0], bitmap);
			return bitmap;
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
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
