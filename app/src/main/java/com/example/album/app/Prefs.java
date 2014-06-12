package com.example.album.app;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by Anwaar on 03/04/2014.
 */
public class Prefs extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
