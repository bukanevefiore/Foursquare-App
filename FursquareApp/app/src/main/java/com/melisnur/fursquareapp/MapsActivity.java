package com.melisnur.fursquareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    String latitudeString;
    String longitudeString;

    //  menüyü entegre etme



    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_places,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.save_places) {
            upload();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @SuppressLint("ServiceCast")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

       locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

       locationListener = new LocationListener() {
           @Override
           public void onLocationChanged(@NonNull Location location) {

               SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences(" com.melisnur.fursquareapp", MODE_PRIVATE);
               boolean firsttimeCheck=sharedPreferences.getBoolean("firstTime", false);

               if(!firsttimeCheck) {
                   LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                   sharedPreferences.edit().putBoolean("firstTime",true).apply();
               }


           }

           @Override
           public void onStatusChanged(String provider, int status, Bundle extras) {

           }

           @Override
           public void onProviderEnabled(@NonNull String provider) {

           }

           @Override
           public void onProviderDisabled(@NonNull String provider) {
               
           }
       };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 2);

        }
        else {
            //  get location
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            mMap.clear();

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastKnownLocation != null){
                LatLng lastUserLocation = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0) {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    //  get location
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    mMap.clear();

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if(lastKnownLocation != null){
                        LatLng lastUserLocation = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
                    }

                }
                }
            }
        }

        //  haritada uzun tıklama
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

        Double latitude = latLng.latitude;
        Double longitude = latLng.longitude;

        latitudeString = latitude.toString();
        longitudeString = longitude.toString();

        mMap.addMarker(new MarkerOptions().title("New Place").position(latLng));

        Toast.makeText(this, "Click on save", Toast.LENGTH_SHORT).show();
    }

    public void upload() {

        //   parse upload
        PlacesClass placesClass = PlacesClass.getInstance();

        String placeName = placesClass.getName();
        String placesType = placesClass.getType();
        String placesAtmosfer = placesClass.getAtmosfer();
        Bitmap placeImage = placesClass.getImage();
/*
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        placeImage.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ParseFile parseFile = new ParseFile("image.pnp",bytes);
*/
        ParseObject object = new ParseObject("Placess");
        object.put("name",placeName);
        object.put("type",placesType);
        object.put("atmosfer",placesAtmosfer);
        object.put("latitude",latitudeString);
        object.put("longitude",longitudeString);
      //  object.put("image",parseFile);
        object.put("username", ParseUser.getCurrentUser().getUsername());

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(MapsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(getApplicationContext(),LocationsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
