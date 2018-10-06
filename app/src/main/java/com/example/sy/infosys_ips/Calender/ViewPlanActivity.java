package com.example.sy.infosys_ips.Calender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sy.infosys_ips.R;

public class ViewPlanActivity extends AppCompatActivity {

    String IDholder,DATEholder,TITLEholder,DESCholder;
    TextView viewdate,viewtitle,viewdesc;
    Button Editview;
    Toolbar toolbar12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plan_layout);

        toolbar12 = findViewById(R.id.toolbar12);
        setSupportActionBar(toolbar12);
        getSupportActionBar().setTitle("Your Specific Plan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar12.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewdate = findViewById(R.id.viewdate);
        viewtitle = findViewById(R.id.viewtitle);
        viewdesc = findViewById(R.id.viewdescription);


        Intent intent = getIntent();
        IDholder = intent.getStringExtra("IDkey");
        DATEholder = intent.getStringExtra("DATEkey");
        TITLEholder = intent.getStringExtra("TITLEkey");
        DESCholder = intent.getStringExtra("DESCkey");

        viewdate.setText(DATEholder);
        viewtitle.setText(TITLEholder);
        viewdesc.setText(DESCholder);

    }
}
