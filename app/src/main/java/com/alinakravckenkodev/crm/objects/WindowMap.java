package com.alinakravckenkodev.crm.objects;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alinakravckenkodev.crm.MainActivity;
import com.alinakravckenkodev.crm.R;
import com.alinakravckenkodev.crm.Services;
import com.alinakravckenkodev.crm.data.MainDbHelper;
import com.alinakravckenkodev.crm.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class WindowMap {

    private AppCompatActivity activity;
    int ZOOM = 15;

    class Map implements OnMapReadyCallback {

        private PopupWindow popupWindow = null;
        private LayoutInflater inflater;
        private View mainView;

        private GoogleMap mMap;
        private ActivityMapsBinding binding;


        private ArrayList<Form> listForms;
        public Map( ArrayList<Form> listForms) {
            this.listForms = listForms;

            inflater = activity.getLayoutInflater();
            binding = ActivityMapsBinding.inflate(inflater);
            mainView = binding.getRoot();

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            boolean focusable = true;

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

                    System.out.println(i+" latitude: " + latitude);
                    System.out.println(i+" longitude: " + longitude);

                    LatLng position = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(position).title(listForms.get(i).tsa_name));
                }
            }

            String location = listForms.get(0).geo_create;
            float latitude = Services.getLatitude(location);
            float longitude = Services.getLongitude(location);
            LatLng position = new LatLng(latitude, longitude);



            CameraPosition cameraPosition = new CameraPosition.Builder().
                    target(position).
                    //tilt(60).
                    zoom(15).
                    bearing(0).
                    build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }


    public void CreateWindow(AppCompatActivity activity) {
        this.activity = activity;
        ArrayList<Form> listForms = MainDbHelper.getListForms();

        Map map = new Map(listForms);


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
