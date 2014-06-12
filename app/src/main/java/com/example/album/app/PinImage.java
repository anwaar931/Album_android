package com.example.album.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Anwaar on 02/04/2014.
 */
public class PinImage extends Activity implements GoogleMap.OnMapClickListener, View.OnClickListener {
    MapView mapView;
    GoogleMap map;
    String path;
    Bitmap gbmp;
    Marker imgmarker = null;
    Button pin, cancel;
    imgdb setimageonmap = null;
    Double imglatitude = 0.0, imglongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinlocation);
        pin = (Button) findViewById(R.id.btpinthis);
        pin.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.btcancel);
        cancel.setOnClickListener(this);
        mapView = (MapView) findViewById(R.id.pinmapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this);

        Intent i = getIntent();
        path = i.getExtras().getString("id");
        gbmp = Async_Images.decodeSampledBitmapFromPath(path, 30, 30);
        map.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(imgmarker==null) {

            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(gbmp);
            imgmarker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(latLng.latitude, latLng.longitude))
                    .title("Image")
                    .icon(descriptor));
            imgmarker.setDraggable(true);
            this.imglatitude=latLng.latitude;
            this.imglongitude=latLng.longitude;
        }
        else
        {
            imgmarker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
        }
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btpinthis:
                if(imgmarker!=null)
                {
                    ImageHelper thisimage=new ImageHelper();
                    thisimage.setLatitude(imglatitude);
                    thisimage.setLogitude(imglongitude);
                    thisimage.setImgURL(path);
                    setimageonmap=new imgdb(this);
                    try {
                        setimageonmap.Open();
                        setimageonmap.insertImage(thisimage);
                        setimageonmap.Close();
                    }
                    catch (Exception a)
                    {
                        String error= a.toString();
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    }


                    this.onDestroy();
                }
                break;
            case R.id.btcancel:
                this.onDestroy();
                break;
        }
    }

}
