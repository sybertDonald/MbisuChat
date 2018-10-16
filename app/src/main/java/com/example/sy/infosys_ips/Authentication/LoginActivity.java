package com.example.sy.infosys_ips.Authentication;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sy.infosys_ips.CheckConnectionActivity;
import com.example.sy.infosys_ips.MainActivity;
import com.example.sy.infosys_ips.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button signupbtn,signinbtn;
    TextView passrecover;
    FirebaseAuth auth;
    ProgressDialog dialog;
    FirebaseUser firebaseUser;

    EditText emaillogin,passwordlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        emaillogin = findViewById(R.id.emaillogin);
        passwordlogin = findViewById(R.id.passwordlogin);


        signupbtn = findViewById(R.id.signupbtn);
        signupbtn.setOnClickListener(this);
        passrecover = findViewById(R.id.forgetpassword);
        passrecover.setOnClickListener(this);
        signinbtn = findViewById(R.id.signbtn);
        signinbtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signupbtn){

            Intent intentreg = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intentreg);
        }
        else if (v.getId() == R.id.forgetpassword){

            Intent intentrec = new Intent(getApplicationContext(),PasswordRecoverActivity.class);
            startActivity(intentrec);
        }

        else if (v.getId() == R.id.signbtn){

            String email = emaillogin.getText().toString().trim();
            String pass = passwordlogin.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                emaillogin.setError("fill email",getDrawable(R.drawable.unfinished));
            }
            else if (TextUtils.isEmpty(pass)){
                passwordlogin.setError("fill password",getDrawable(R.drawable.unfinished));
            }
            else
                {
                    dialog.setMessage("Login progress...");
                    dialog.show();
                loginprocess(email,pass);

            }
        }
        //end of signin button
    }


     void loginprocess(String email1, String pass1) {

        auth.signInWithEmailAndPassword(email1,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();

                if (task.isSuccessful()){
                    Intent intentre = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intentre);
                    finish();
                }
                else
                    {
                        Check();
                    Toast.makeText(LoginActivity.this, " wrong username or password", Toast.LENGTH_SHORT).show();
                }

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

        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null){
                Intent godirect = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(godirect);
                finish();
            }
        }

        else{

            Toast.makeText(this, "internet is unavailable", Toast.LENGTH_SHORT).show();
            Intent gomain = new Intent(getApplicationContext(),CheckConnectionActivity.class);
            startActivity(gomain);
        }
        }
    }


