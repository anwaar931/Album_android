package com.example.album.app;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * create an instance of this fragment.
 *
 */
public class Map extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBE
    MapView mapView;
    GoogleMap map;
    imgdb settingimages=null;
    Bitmap image;
    Double lattitude,longitude;
    String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        // Inflate the layout for this fragment
        setting_images();
        return v;
    }

    private void setting_images() {
        ArrayList<ImageHelper> pinnedImagesdata;
        settingimages =new imgdb(this.getActivity());
        settingimages.Open();
        pinnedImagesdata=settingimages.getImages();
        settingimages.Close();
        for (int i=0;i<pinnedImagesdata.size();i++) {

            image = Async_Images.decodeSampledBitmapFromPath(pinnedImagesdata.get(i).getImgURL(), 35, 35);
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(image);
            MarkerOptions a=new MarkerOptions();
            a.position(new LatLng(pinnedImagesdata.get(i).getLatitude(), pinnedImagesdata.get(i).getLongitude()));
            a.title("New Image");
            a.icon(descriptor);
            try {
                map.addMarker(a);
            }
            catch (Exception e)
            {

            }
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
        mapView.onLowMemory();
    }
}
