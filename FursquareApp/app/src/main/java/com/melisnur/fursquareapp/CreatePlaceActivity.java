package com.melisnur.fursquareapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.mbms.StreamingServiceInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class CreatePlaceActivity extends AppCompatActivity {

    EditText place_name,place_type,place_atmosfer;
    ImageView place_image;
    Bitmap secilenresim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);

        place_atmosfer=findViewById(R.id.place_atmosfer);
        place_image=findViewById(R.id.place_image);
        place_name=findViewById(R.id.place_name);
        place_type=findViewById(R.id.place_type);
    }

    public void next(View view){

        PlacesClass placesClass=PlacesClass.getInstance();

        String placeName=place_name.getText().toString();
        String placeType=place_type.getText().toString();
        String placeAtmosfer=place_atmosfer.getText().toString();

        placesClass.setName(placeName);
        placesClass.setType(placeType);
        placesClass.setAtmosfer(placeAtmosfer);
        placesClass.setImage(secilenresim);

        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);

    }


    public void placeImage(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri uri = data.getData();

            try {
                secilenresim = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                place_image.setImageBitmap(secilenresim);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}