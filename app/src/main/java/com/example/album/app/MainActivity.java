package com.example.album.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


//import com.bugsense.trace.BugSenseHandler;
import com.example.album.app.utils.NavDrawerItem;
import com.example.album.app.utils.NavDrawerListAdapter;
import com.example.album.app.utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.ArrayList;

public class MainActivity extends Activity{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;
//used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
   // public static ArrayList<String> ImageURL_List = new ArrayList<String>();
    /**
     * Used to store the last screen title. For use in
     */

    Bundle instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///BugSenseHandler.initAndStartSession(this, "a9bd2bae");
        instance=savedInstanceState;
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        Utils.updateLanguage(this, "en");
        Utils.printHashKey(this);

        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        FB_Settings fb_settings = new FB_Settings();
        fb_settings.onCreate();

        getPath.getAllImages(this);
        getPath.getAllVideos(this);

       /// BugSenseHandler.startSession(MainActivity.this);

        /*try{
            String a = null;
            a.toString();
        }catch(Exception ex) {
            Log.e("Bugs",ex.getMessage());
            ex.printStackTrace(); // in case you want to see the stacktrace in your log cat output
            BugSenseHandler.sendException(ex);
        }*/

        ///BugSenseHandler.closeSession(MainActivity.this);

        getPath.mThumbs = new ArrayList<bitThumbs>();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home

        int imagecount=getPath.getImagecount();
        int videocount=getPath.getVideocount();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1),true,String.valueOf(imagecount)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1),true,String.valueOf(videocount)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // settings
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, 1)));

        ///Publish App
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, 1)));

        //Google plus
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6],navMenuIcons.getResourceId(6,1)));
        //Google Drive
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7],navMenuIcons.getResourceId(7,1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        PreferenceActivity act=null;
        Activity publish=null;
        Activity google_plus=null;
        Activity google_drive=null;
        switch (position) {
            case 0:
                fragment = new images();
                break;
            case 1:
                fragment = new Videos();
                break;
            case 2:
                fragment = new Map();
                break;
            case 3:
                fragment = new Online();
                break;
            case 4:
                google_drive=new GoogleDrive();
                break;
            case 5:
                publish=new Publish();
                break;
            case 6:
                google_plus=new Google_plus();
                break;
            case 7:
            act = new Prefs();
            break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerLayout.openDrawer(mDrawerList);
        }
        else if(act!=null)
        {
            Intent i=new Intent(this,Prefs.class);
            startActivity(i);
           /// this.onCreate(this.instance);
        }
        else if(publish!=null)
        {
            Intent i=new Intent(this,Publish.class);
            startActivity(i);
        }
        else if(google_plus!=null)
        {
            Intent i=new Intent(this,Google_plus.class);
            startActivity(i);
        }
        else if(google_drive!=null)
        {
            Intent i=new Intent(this,GoogleDrive.class);
            startActivity(i);
        }
        else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.open_Camera:
                Intent i;
                i=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(i);
               break;

            case R.id.report:

                break;
            case R.id.exit:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /* *
    * Called when invalidateOptionsMenu() is triggered
    */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        ///menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(instance);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
