package com.example.album.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * create an instance of this fragment.
 *
 */
public class images extends Fragment {

    // TODO: Rename and change types and number of parameters
    AdView mAdView;
    AdRequest adRequest;
    int myLastVisiblePos;
    private static final String TEST_DEVICE_ID = "...";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_images, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_images);
        gridView.setAdapter(new ImageAdapter(getActivity(),getPath.arrImagePath,false));
       //// gridView.setAdapter(new ListAdapter(getActivity()));
        gridView.setClickable(true);
        myLastVisiblePos = gridView.getFirstVisiblePosition();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(),com.example.album.app.ScaleImage.class);
                intent.putExtra("id", i);

                getActivity().startActivity(intent);

            }
        });
        mAdView = (AdView) rootView.findViewById(R.id.ad);
        //// mAdView.setAdListener(new MyAdListener());

        // = new AdRequest();
        this.adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE_ID).build();
        // adRequest.addKeyword("ad keywords");

        // publishing your application.
        // adRequest.addTestDevice(TEST_DEVICE_ID);
        mAdView.loadAd(adRequest);

        return rootView;
    }
}
