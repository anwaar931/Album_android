package com.example.album.app;

/**
 * Created by Saad on 3/29/2014.
 */
public class ImageHelper {
    private int imageId;
    private String imgURL;
    private double longitude;
    private double latitude;

    public ImageHelper()
    {
        latitude = -1;
        longitude = -1;

        imgURL = null;
        imageId = -1;
    }

    public int getImageId() {
        return imageId;
    }
    public String getImgURL()
    {
        return this.imgURL;
    }
    public void setImgURL(String url)
    {
        this.imgURL = url;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public double getLongitude()
    {
        return this.longitude;
    }
    public double getLatitude()
    {
        return this.latitude;
    }
    public void setLogitude(double val)
    {
        this.longitude = val;
    }
    public void setLatitude(double val)
    {
        this.latitude = val;
    }
}