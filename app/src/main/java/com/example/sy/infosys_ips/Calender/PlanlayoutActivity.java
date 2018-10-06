package com.example.sy.infosys_ips.Calender;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sy.infosys_ips.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class PlanlayoutActivity extends AppCompatActivity {

    String dateholded,titleholder,descriptionholder,datehoder;
    TextView pdate;
    EditText ptitle,pwrite;
    Button pcancel,psubmit;
    ProgressDialog dialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reference = db.collection("List Of Plans");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planlayout);


        dialog = new ProgressDialog(this);
        Intent intent = getIntent();

        dateholded = intent.getStringExtra("key");

        //initialize id from the xml
        pdate = findViewById(R.id.plandate);
        ptitle = findViewById(R.id.plantitle);
        pwrite = findViewById(R.id.writeplan);
        pcancel = findViewById(R.id.cancelplan);
        psubmit = findViewById(R.id.submitplan);
        pdate.setText(dateholded);



        psubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datehoder = pdate.getText().toString().trim();
                titleholder = ptitle.getText().toString().trim();
                descriptionholder = pwrite.getText().toString().trim();
                SavePlan(datehoder,titleholder,descriptionholder);
                dialog.setMessage("Loading...");
                dialog.show();

            }
        });

    }

    public void SavePlan(String date,String title,String description){
        ListModel model = new ListModel(date,title,description);

        reference.add(model).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                dialog.dismiss();
            }
        });
        Intent gotolist = new Intent(getApplicationContext(),RecyclerPlanActivity.class);
        startActivity(gotolist);
    }

}
