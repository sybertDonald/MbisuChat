package com.example.sy.infosys_ips.Calender;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.sy.infosys_ips.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecyclerPlanActivity extends AppCompatActivity {
    Toolbar toolbar12;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("List Of Plans");
    RecyclerPlanAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_plan_layout);

        toolbar12 = findViewById(R.id.toolbar12);
        setSupportActionBar(toolbar12);
        getSupportActionBar().setTitle("Your Plans List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar12.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SetupRecyclerView();
    }

    private void SetupRecyclerView() {

        Query query = collectionReference.orderBy("date" , Query.Direction.DESCENDING);

        FirestoreRecyclerOptions <ListModel> options = new FirestoreRecyclerOptions.Builder<ListModel>()
                .setQuery(query,ListModel.class)
                .build();
        adapter = new RecyclerPlanAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerplan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                adapter.deleteitem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new RecyclerPlanAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                ListModel model12 = documentSnapshot.toObject(ListModel.class);
                String IDPLAN = documentSnapshot.getId();
                String dateretrieved = model12.getDate();
                String titleretrieved = model12.getTitle();
                String descriptionretrieved = model12.getDescription();

                Intent viewplan = new Intent(getApplicationContext(),ViewPlanActivity.class);
                viewplan.putExtra("IDkey",IDPLAN);
                viewplan.putExtra("DATEkey",dateretrieved);
                viewplan.putExtra("TITLEkey",titleretrieved);
                viewplan.putExtra("DESCkey",descriptionretrieved);
                startActivity(viewplan);



               // Toast.makeText(RecyclerPlanActivity.this, "date is " + IDPLAN + "\n" + titleretrieved, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
