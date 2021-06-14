package com.melisnur.fursquareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView name_text_detail_activity,type_text_detail_activity,atmosphere_text_detail_activity;
    ImageView imageview_detail_activity;
    String placeName;
    String latitudeString;
    String longitudeString;
    Double latitude;
    Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetail);
        mapFragment.getMapAsync(this);

        name_text_detail_activity=findViewById(R.id.name_text_detail_activity);
        type_text_detail_activity=findViewById(R.id.type_text_detail_activity);
        atmosphere_text_detail_activity=findViewById(R.id.atmosphere_text_detail_activity);
        imageview_detail_activity=findViewById(R.id.imageview_detail_activity);

        Intent intent=getIntent();
        placeName=intent.getStringExtra("name");

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;

        getData();


    }

    public void getData(){

        ParseQuery<ParseObject> query =ParseQuery.getQuery("Placess");
        query.whereEqualTo("name",placeName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(DetailsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    if(objects.size()>0){

                        for(final  ParseObject object : objects){

                            name_text_detail_activity.setText(placeName);
                            type_text_detail_activity.setText(object.getString("type"));
                            atmosphere_text_detail_activity.setText(object.getString("atmosfer"));
                            latitudeString=object.getString("latitude");
                            longitudeString=object.getString("longitude");

                            latitude=Double.parseDouble(latitudeString);
                            longitude=Double.parseDouble(longitudeString);

                            mMap.clear();

                            LatLng placeLocation = new LatLng(latitude,longitude);
                            mMap.addMarker(new MarkerOptions().position(placeLocation).title(placeName));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));
                 /*
                            ParseFile parseFile=(ParseFile) object.get("image");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if(e != null && data != null){

                                        Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                        imageview_detail_activity.setImageBitmap(bitmap);


                                    }
                                }
                            });
                            */
                        }
                    }
                }
            }
        });
    }
}