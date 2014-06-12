package com.example.album.app;

/**
 * Created by Saad on 4/3/2014.
 */
public class GeoPoint {
    private int latitudeE6;
    private int longitudeE6;

    public GeoPoint(int latitudeE6, int longitudeE6) {
        this.latitudeE6 = latitudeE6;
        this.longitudeE6 = longitudeE6;
    }
    GeoPoint(double latitude, double longitude) {
        this((int)(latitude * 1e6), (int)(longitude * 1e6));
    }

    public int getLatitudeE6() {
        return latitudeE6;
    }

    public int getLongitudeE6() {
        return longitudeE6;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "GeoPoint: Latitude: " + latitudeE6 + ", Longitude: " + longitudeE6;
    }
}