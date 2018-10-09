package com.example.sy.infosys_ips.Chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sy.infosys_ips.MainActivity;
import com.example.sy.infosys_ips.R;
import com.example.sy.infosys_ips.models.Chat;
import com.example.sy.infosys_ips.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView messageimage;
    TextView messageusername;
    Intent intent;
    FirebaseUser userid;
    DatabaseReference reference ,referenceRead;
    Toolbar toolbarsms;
    ImageButton btnsend;
    EditText smssend;
    RecyclerView recyclerView;
    List<Chat> mchat;
    MessageAdapter adapter;
    String idco;
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        toolbarsms = findViewById(R.id.toolbarsms);
        recyclerView = findViewById(R.id.recyclersms);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);



        btnsend = findViewById(R.id.btnsend);
        smssend = findViewById(R.id.smssend);
        setSupportActionBar(toolbarsms);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarsms.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        messageimage = findViewById(R.id.messageimage);
        messageusername = findViewById(R.id.messageusername);
        intent = getIntent();
        idco = intent.getStringExtra("idmessage");
        userid = FirebaseAuth.getInstance().getCurrentUser();

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagego = smssend.getText().toString().trim();
                if (!messagego.equals("")){
                    SendMessage(userid.getUid(),idco,messagego);
                }
                else {
                    Toast.makeText(MessageActivity.this, "you cannot send empty sms", Toast.LENGTH_SHORT).show();
                }
                smssend.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("USERS").child(idco);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                messageusername.setText(user.getUsername());
                if (user.getImageURL().equals("DEFAULT")){
                    messageimage.setImageResource(R.mipmap.infosysimage);
                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(messageimage);
                }
                ReadMessage(userid.getUid(),idco,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        seenMessage(idco);
    }


    void seenMessage(final String useridnow){
         reference = FirebaseDatabase.getInstance().getReference("CHATS");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(userid.getUid()) && chat.getSender().equals(useridnow)){

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

   void SendMessage(String sender ,String receiver,String message){
        DatabaseReference referencesms = FirebaseDatabase.getInstance().getReference();
       HashMap<String,Object> hashMap = new HashMap<>();
       hashMap.put("sender",sender);
       hashMap.put("receiver",receiver);
       hashMap.put("message",message);
       hashMap.put("isseen",false);

       referencesms.child("CHATS").push().setValue(hashMap);

       final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("CHATSLIST")
               .child(userid.getUid()).child(idco);
       chatref.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (!dataSnapshot.exists()){
                   chatref.child("id").setValue(idco);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


       final DatabaseReference chatref2 = FirebaseDatabase.getInstance().getReference("CHATSLIST")
               .child(idco).child(userid.getUid());
       chatref2.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (!dataSnapshot.exists()){
                   chatref2.child("id").setValue(userid.getUid());
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


    }


    void ReadMessage(final String myid, final String userid, final String imageurl){

        mchat =new ArrayList<>();
        referenceRead = FirebaseDatabase.getInstance().getReference("CHATS");
        referenceRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(myid) && chat.getReceiver().equals(userid) ||
                            chat.getSender().equals(userid) && chat.getReceiver().equals(myid)){
                        mchat.add(chat);
                    }
                    adapter = new MessageAdapter(getApplicationContext(),mchat,imageurl);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void Status(String status){

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("USERS").child(userid.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference2.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        Status("offline");
    }
}
