package com.example.sy.infosys_ips.Chat;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sy.infosys_ips.R;
import com.example.sy.infosys_ips.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    UserAdapter adapter;
    List<User> muser;
    EditText searchuser;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_layout, container, false);
        recyclerView = view.findViewById(R.id.recycleruser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
     muser = new ArrayList<>();

     ReadUser();
       searchuser = view.findViewById(R.id.searchuser);
       searchuser.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

               SearchUser(s.toString());
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        return view;
    }

    private void SearchUser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("USERS").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                muser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    if (!firebaseUser.getUid().equals(user.getId())){
                        muser.add(user);
                    }
                }

                adapter = new UserAdapter(getContext(),muser,false);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }




    public void ReadUser(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USERS");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (searchuser.getText().toString().equals(""))
                {

                muser.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);
                    assert user !=null;
                    if (!user.getId().equals(firebaseUser.getUid())){
                        muser.add(user);
                    }

                }

                adapter = new UserAdapter(getContext(),muser,false);
                recyclerView.setAdapter(adapter);

            }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
