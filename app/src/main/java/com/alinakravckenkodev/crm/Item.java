package com.alinakravckenkodev.crm;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Item implements ClusterItem {

    private final LatLng position;
    private final String title;
    private final String snippet;

    public Item (double lat, double lng, String title, String snippet) {
        this.snippet = snippet;
        position = new LatLng(lat, lng);
        this.title = title;

    }


    @Override
    public LatLng getPosition() {
        return null;
    }

    public String getTitle() {
        return title;
    }



}
