package com.example.sy.infosys_ips.Chat;



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sy.infosys_ips.R;
import com.example.sy.infosys_ips.models.Chat;
import com.example.sy.infosys_ips.models.ChatList;
import com.example.sy.infosys_ips.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class ChatFragment extends Fragment {

    RecyclerView recyclerViewchat;
    DatabaseReference reference,reference1;
    List<User> mUser;
    UserAdapter adapter;
    FirebaseUser fuser;
    List<ChatList> userList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_layout, container, false);

        recyclerViewchat = view.findViewById(R.id.recyclerchat);
        recyclerViewchat.setHasFixedSize(true);
        recyclerViewchat.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList =new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("CHATSLIST").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    ChatList chatList = snapshot.getValue(ChatList.class);
                    userList.add(chatList);
                }
                ChatListMethod();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }


        void ChatListMethod(){

        mUser = new ArrayList<>();

        reference1 = FirebaseDatabase.getInstance().getReference("USERS");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUser.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (ChatList chatList : userList){
                        if (user.getId().equals(chatList.getId())){
                            mUser.add(user);
                        }
                    }
                }

                adapter = new UserAdapter(getContext(),mUser,true);
                recyclerViewchat.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }


}
