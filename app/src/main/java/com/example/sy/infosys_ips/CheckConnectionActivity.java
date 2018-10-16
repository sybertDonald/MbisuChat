package com.example.sy.infosys_ips;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sy.infosys_ips.Authentication.LoginActivity;

public class CheckConnectionActivity extends AppCompatActivity {


    boolean wificonn;
    boolean networkconn;
    Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_connection_layout);

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }




    public void Check(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Service.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            wificonn = networkInfo.getType()==ConnectivityManager.TYPE_WIFI;
            networkconn = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }

        if (wificonn && networkconn){

            Toast.makeText(this, "internet is available", Toast.LENGTH_SHORT).show();
            Intent gomain = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(gomain);
        }
        else{

            Toast.makeText(this, "internet is unavailable", Toast.LENGTH_SHORT).show();
            Intent gomain = new Intent(getApplicationContext(),CheckConnectionActivity.class);
            startActivity(gomain);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


}
