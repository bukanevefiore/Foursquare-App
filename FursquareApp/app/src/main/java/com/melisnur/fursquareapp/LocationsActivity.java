package com.melisnur.fursquareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearSmoothScroller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LocationsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> placesName;
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_place,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.add_place){

            Intent intent=new Intent(getApplicationContext(),CreatePlaceActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.log_out) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        Toast.makeText(LocationsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent=new Intent(LocationsActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        listView=findViewById(R.id.listview);
        placesName=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,placesName);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),DetailsActivity.class);
                intent.putExtra("name",placesName.get(position));
                startActivity(intent);
            }
        });

        download();
    }

    // verileri listeleme
    public void download() {

        ParseQuery<ParseObject> query =ParseQuery.getQuery("Placess");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    placesName.clear();

                    for(ParseObject object : objects){
                        placesName.add(object.getString("name"));
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
                else{
                    Toast.makeText(LocationsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}