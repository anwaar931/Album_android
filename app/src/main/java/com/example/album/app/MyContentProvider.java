package com.example.album.app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

/**
 * Created by Anwaar on 05/05/2014.
 */
public class MyContentProvider extends ContentProvider {

    private imgdb db;
    private static final int ALL_ROW_DATA = 1;

    private static final String AUTHORITY = "com.example.album.app.MyContentProvider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + imgdb.TABLE_IMAGE);

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, imgdb.TABLE_IMAGE, ALL_ROW_DATA);
        // uriMatcher.addURI(AUTHORITY, dbAccess.DATABASE_TABLE + "/#", SINGLE_ROW_DATA);
    }
    @Override
    public boolean onCreate() {
        db = new imgdb(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        try {
            db.Open();
            c = db.getCursor();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return c;
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
