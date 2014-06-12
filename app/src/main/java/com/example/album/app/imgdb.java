package com.example.album.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Saad on 3/29/2014.
 */
public class imgdb {

    private final String TAG = "DatabaseHelperClass";
    private static final int databaseVersion = 1;
    private static final String databaseName = "ImageAlbum";
    public static final String TABLE_IMAGE = "ImageTable";
    // Image Table Columns names
    public static final String IMAGE_ID = "image_id";
    public static final String IMAGE_LONGITUDE = "image_longitude";
    public static final String IMAGE_LATITUDE = "image_latitude";
    public static final String IMAGE_URL = "image_url";


    private final Context mContext;
    private DatabaseHelper ourSaver;
    private SQLiteDatabase ourDatabase;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                    + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + IMAGE_LONGITUDE + " DOUBLE NOT NULL,"
                    + IMAGE_LATITUDE + " DOUBLE NOT NULL,"
                    + IMAGE_URL + " TEXT NOT NULL);";
            sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // Drop older table if existed
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
            onCreate(sqLiteDatabase);
        }
    }
    public void insertImage(ImageHelper imageHelper) {
        ContentValues values = new ContentValues();
        values.put(IMAGE_LATITUDE,imageHelper.getLatitude());
        values.put(IMAGE_LONGITUDE,imageHelper.getLongitude());
        values.put(IMAGE_URL,imageHelper.getImgURL());
        ourDatabase.insert(TABLE_IMAGE, null, values);
    }
    public ArrayList<ImageHelper> getImages() {
        ArrayList<ImageHelper> tempimagedata=new ArrayList<ImageHelper>();
        Cursor cursor2 = ourDatabase.query(TABLE_IMAGE,
                new String[] {IMAGE_ID,IMAGE_LATITUDE,IMAGE_LONGITUDE,IMAGE_URL},null, null, null, null, null);
        int iRow=cursor2.getColumnIndex(IMAGE_ID);
        int iLatitude=cursor2.getColumnIndex(IMAGE_LATITUDE);
        int iLongitude=cursor2.getColumnIndex(IMAGE_LONGITUDE);
        int iurl=cursor2.getColumnIndex(IMAGE_URL);
        ImageHelper imageHelper =null;
        if (cursor2.moveToFirst()) {
            do {
                imageHelper = new ImageHelper();
                imageHelper.setImageId(cursor2.getInt(iRow));
                imageHelper.setLatitude(cursor2.getDouble(iLatitude));
                imageHelper.setLogitude(cursor2.getDouble(iLongitude));
                imageHelper.setImgURL(cursor2.getString(iurl));
                tempimagedata.add(imageHelper);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
        return tempimagedata;
    }
    public imgdb Open() throws SQLException
    {
        ourSaver=new DatabaseHelper(mContext);
        ourDatabase=ourSaver.getWritableDatabase();
        return this;
    }
    public void Close()
    {
        ourSaver.close();
    }

    imgdb(Context c)
    {
        this.mContext=c;

    }
    public Cursor getCursor()
    {
        String columns[] =new String[]{IMAGE_ID,IMAGE_LATITUDE,IMAGE_LONGITUDE,IMAGE_URL};

        return ourDatabase.query(TABLE_IMAGE, columns,null ,null, null, null, null);
    }
}

