package com.example.sy.infosys_ips.Calender;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.sy.infosys_ips.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class CalenderActivity extends AppCompatActivity {

    String datehold;
    AlertDialog dialog;
    TextView planlist;
    android.support.v7.widget.Toolbar toolbar12;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("List Of Plans");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_layout);
        toolbar12 = findViewById(R.id.toolbar12);
        setSupportActionBar(toolbar12);
        getSupportActionBar().setTitle("Your Plans And Calender");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar12.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        int month = cal.get(Calendar.MONTH);


        MaterialCalendarView materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(year, month, dayofweek))
                .setMaximumDate(CalendarDay.from(10000, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {


            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


                int month =Integer.parseInt(String.valueOf(date.getMonth()));
                int summonth =1+month;
                datehold = date.getDay() + "-" + summonth + "-" +date.getYear();

                Intent planactivity = new Intent(getApplicationContext(),PlanlayoutActivity.class);
                planactivity.putExtra("key",datehold);
                startActivity(planactivity);




                /* AlertDialog.Builder builder =new AlertDialog.Builder(CalenderActivity.this);

                View mview =getLayoutInflater().inflate(R.layout.planlayout,null);


                plandate = mview.findViewById(R.id.plandate);
                plantitle = mview.findViewById(R.id.plantitle);
                writeplan = mview.findViewById(R.id.writeplan);
                cancelplan = mview.findViewById(R.id.cancelplan);
                submitplan = mview.findViewById(R.id.submitplan);

                int month =Integer.parseInt(String.valueOf(date.getMonth()));
                int summonth =1+month;
                String datehold = date.getDay() + "-" + summonth + "-" +date.getYear();
                plandate.setText(datehold);


                builder.setView(mview);
                builder.setIcon(R.drawable.calender);

                dialog =builder.create();
                dialog.show();


                submitplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SavePlan(plandate.getText().toString(),plantitle.getText().toString(),writeplan.getText().toString());

                    }
                });

              */  //Toast.makeText(CalenderActivity.this, date.getDay() + "-" +date.getMonth() + "-" + date.getYear(), Toast.LENGTH_SHORT).show();
            }
        });

        
        planlist =findViewById(R.id.planlist);
        planlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  startlist = new Intent(getApplicationContext(),RecyclerPlanActivity.class);
                startActivity(startlist);
            }
        });
    }



    public void SavePlan(String date,String title,String description){
        ListModel model = new ListModel(date,title,description);
        collectionReference.add(model);
        dialog.dismiss();
        Intent gotolist = new Intent(getApplicationContext(),RecyclerPlanActivity.class);
        startActivity(gotolist);
    }

    }

