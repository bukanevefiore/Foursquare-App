package com.melisnur.fursquareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText kullanıcıadi_kayitol, sifre_kayitol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sifre_kayitol=findViewById(R.id.sifre_kayitol);
        kullanıcıadi_kayitol=findViewById(R.id.kullanıcıadi_kayitol);
    }

    public void kayitOl(View view){

        ParseUser user=new ParseUser();
        user.setUsername(kullanıcıadi_kayitol.getText().toString());
        user.setPassword(sifre_kayitol.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "User signed up", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void girisYap(View view) {

        ParseUser.logInInBackground(kullanıcıadi_kayitol.getText().toString(), sifre_kayitol.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "welcome"+user.getUsername(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}