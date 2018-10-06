package com.example.sy.infosys_ips.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sy.infosys_ips.MainActivity;
import com.example.sy.infosys_ips.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    EditText username,email1,password;
    Button registernow;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        username = findViewById(R.id.usernamereg);
        email1 = findViewById(R.id.emailreg);
        password = findViewById(R.id.passreg);
        registernow = findViewById(R.id.registernow);

        registernow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registernow){
            String user = username.getText().toString().trim();
            String email2 = email1.getText().toString().trim();
            String pass = password.getText().toString().trim();
            if (TextUtils.isEmpty(user)){
                username.setError("fill username");
            }
            else if (TextUtils.isEmpty(email2)){
                email1.setError("fill email");
            }
            else if (TextUtils.isEmpty(pass)){
                password.setError("fill password");
            }
            else {

                dialog.setMessage("Register progress...");
                dialog.show();
                Registerprocess(email2,pass);
            }
        }
    }

    void Registerprocess(String email21, String pass1) {

        auth.createUserWithEmailAndPassword(email21,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String fireID =firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("USERS").child(fireID);
                    reference.keepSynced(true);

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("username",username.getText().toString());
                    hashMap.put("email",email1.getText().toString());
                    hashMap.put("imageURL","DEFAULT");
                    hashMap.put("id",fireID);

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Intent reg = new Intent(getApplicationContext(), MainActivity.class);
                                reg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(reg);
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Database fail", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
                else {
                    Toast.makeText(RegisterActivity.this, "email already registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
