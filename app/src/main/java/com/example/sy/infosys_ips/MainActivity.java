package com.example.sy.infosys_ips;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sy.infosys_ips.Authentication.LoginActivity;
import com.example.sy.infosys_ips.Calender.CalenderActivity;
import com.example.sy.infosys_ips.Chat.ChatActivity;
import com.example.sy.infosys_ips.Profile.EditProfileActivity;
import com.example.sy.infosys_ips.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button calender1,chat,editprofile;
    FirebaseAuth auth;
    CircleImageView profileimage;
    TextView profilename1;
    FirebaseUser firebaseUser;
    DatabaseReference reference1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editprofile = findViewById(R.id.editprofile);
        editprofile.setOnClickListener(this);
        chat = findViewById(R.id.chatbtn);
        chat.setOnClickListener(this);
        calender1 = findViewById(R.id.calenderbtn);
        calender1.setOnClickListener(this);

        profileimage = findViewById(R.id.profile_image);
        profilename1 = findViewById(R.id.profilename);
        firebaseUser = auth.getCurrentUser();

        reference1 = FirebaseDatabase.getInstance().getReference("USERS").child(firebaseUser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user2 = dataSnapshot.getValue(User.class);
                profilename1.setText(user2.getUsername());
                if (user2.getImageURL().equals("DEFAULT")){
                    profileimage.setImageResource(R.mipmap.infosysimage);
                }
                else {

                    Glide.with(getApplicationContext()).load(user2.getImageURL()).into(profileimage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "no changes", Toast.LENGTH_SHORT).show();
            }
        });


        //setSupportActionBar(toolbar);



        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.logout_user){

            FirebaseAuth.getInstance().signOut();
            Intent returnlogin = new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnlogin);

            return true;
        }

        return super.onOptionsItemSelected(item);

        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.calenderbtn:
                Intent calendercard = new Intent(getApplicationContext(),CalenderActivity.class);
                startActivity(calendercard);
                break;

            case R.id.chatbtn:
                Intent chatgo = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(chatgo);
                break;

            case R.id.editprofile:
                Intent editp = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(editp);
                break;


                }
        }

        void Status(String status){

            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("USERS").child(firebaseUser.getUid());
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("status",status);
            reference2.updateChildren(hashMap);
        }

    @Override
    protected void onResume() {
        super.onResume();
        Status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Status("Offline");
    }
}


