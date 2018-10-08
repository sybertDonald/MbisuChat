package com.example.sy.infosys_ips.Authentication;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sy.infosys_ips.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecoverActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailrecover;
    Button submitrecover;
    TextView smsrecover;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_recover_layout);

        auth = FirebaseAuth.getInstance();
        emailrecover = findViewById(R.id.emailreset);
        submitrecover = findViewById(R.id.submitrecover);
        smsrecover = findViewById(R.id.messagerecover);
        dialog = new ProgressDialog(this);

        submitrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String emailre = emailrecover.getText().toString().trim();

                if (TextUtils.isEmpty(emailre)){
                    emailrecover.setError("Fill email");
                }
                else {


                    dialog.setMessage("Recover on progress...");
                    dialog.show();
                    auth.sendPasswordResetEmail(emailre).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                smsrecover.setText("The Link of Confirmation has been sent to " + emailre);
                                emailrecover.setText("");
                                dialog.dismiss();
                            }
                            else {
                                smsrecover.setText(task.getException().getMessage());
                                emailrecover.setText("");
                                dialog.dismiss();
                            }

                        }
                    });
                }
            }
        });

    }
}
