package com.alinakravckenkodev.crm.objects;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alinakravckenkodev.crm.R;
import com.alinakravckenkodev.crm.Services;
import com.alinakravckenkodev.crm.data.MainDbHelper;
import com.alinakravckenkodev.crm.databinding.ActivityMapsBinding;
import com.alinakravckenkodev.crm.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;


import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;




import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

public class WindowMap {

    private AppCompatActivity activity;
    int ZOOM = 15;




    class MapManager implements OnMapReadyCallback {

        private PopupWindow popupWindow = null;
        private LayoutInflater inflater;
        private View mainView;
        private GoogleMap mMap;
        private ActivityMapsBinding binding;



        private ArrayList<Form> listForms;


        public MapManager(ArrayList<Form> listForms) {
            this.listForms = listForms;

            inflater = activity.getLayoutInflater();
            binding = ActivityMapsBinding.inflate(inflater);
            mainView = binding.getRoot();


            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            boolean focusable = true;

//            MapsInitializer.initialize(activity, MapsInitializer.Renderer.LATEST, new OnMapsSdkInitializedCallback() {
//                @Override
//                public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
//                                     Log.d("TAG", "onMapsSdkInitialized: ");
//                }
//            });


            popupWindow = new PopupWindow(mainView, width, height, focusable);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    SupportMapFragment mapFragment =  (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
                    activity.getSupportFragmentManager().beginTransaction()
                            .remove(mapFragment)
                            .commit();
                }
            });

            popupWindow.showAtLocation(activity.findViewById(R.id.tableList), Gravity.CENTER, 0, 0);


            SupportMapFragment mapFragment =  (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        }

        public GoogleMap getMap() {
            return this.mMap;
        }

        public View getView() {
            return this.mainView;
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            System.out.println("onMapReady");
            mMap = googleMap;


            for (int i=0;i<listForms.size();i++) {
                if (!listForms.get(i).geo_create.equals("")) {
                    String location = listForms.get(i).geo_create;

                    float latitude = Services.getLatitude(location);
                    float longitude = Services.getLongitude(location);

                    LatLng position = new LatLng(latitude, longitude);

                    String title = "ID = "+String.valueOf(listForms.get(i).ID)
                            + ", name = " +listForms.get(i).tsa_name
                            + ", address = " + listForms.get(i).tsa_address;

                    mMap.addMarker(new MarkerOptions().position(position).title(title));

                }
            }

            initStartCameraPlace(listForms.get(0).geo_create);

            // Клаcтеризация

//            ClusterManager<Item> clusterManager = new ClusterManager<Item>(activity, mMap);
//
////            mMap.setOnCameraIdleListener(clusterManager);
//            mMap.setOnMarkerClickListener(clusterManager);
//
//            addItems(clusterManager);
        }

        public void initStartCameraPlace(String location) {

            float latitude = Services.getLatitude(location);
            float longitude = Services.getLongitude(location);

            LatLng position = new LatLng(latitude, longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder().
                    target(position).
                    zoom(15).
                    bearing(0).
                    build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        private void addItems(ClusterManager<Item> clusterManager) {
            // Set some lat/lng coordinates to start with.
            double lat = 51.5145160;
            double lng = -0.1270060;

            // Add ten cluster items in close proximity, for purposes of this example.
            for (int i = 0; i < 5; i++) {
                double offset = i / 60d;
                lat = lat + offset;
                lng = lng + offset;
                Item offsetItem = new Item (lat, lng, "Title " + i, "snippet");
                clusterManager.addItem(offsetItem);
            }
        }
    }



    public void CreateWindow(AppCompatActivity activity) {
        this.activity = activity;
        ArrayList<Form> listForms = MainDbHelper.getListForms();

        MapManager map = new MapManager(listForms);


        FloatingActionButton fabPlus = map.getView().findViewById(R.id.button_Plus);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.getMap().animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        FloatingActionButton fabMinus = map.getView().findViewById(R.id.button_Minus);
        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.getMap().animateCamera(CameraUpdateFactory.zoomOut());
            }
        });


    }





}
