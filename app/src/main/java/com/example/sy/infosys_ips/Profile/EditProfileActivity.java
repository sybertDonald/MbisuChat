package com.example.sy.infosys_ips.Profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sy.infosys_ips.R;
import com.example.sy.infosys_ips.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int IMAGE_REQUEST = 1 ;
    CircleImageView editimage;
    TextView editname;
    TextView youremail;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Button changepass,changeimage;
    Uri imageurl;
    StorageTask uploadTask;
    StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);

        editimage = findViewById(R.id.editimage);
        changepass = findViewById(R.id.changepassword);
        changeimage = findViewById(R.id.changeimage);
        changepass.setOnClickListener(this);
        changeimage.setOnClickListener(this);
        editname = findViewById(R.id.editname);
        youremail = findViewById(R.id.youremail);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("UPLOADS");
        reference = FirebaseDatabase.getInstance().getReference("USERS").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                editname.setText(user.getUsername());
                youremail.setText(user.getEmail());
                if (user.getImageURL().equals("DEFAULT")){
                    editimage.setImageResource(R.mipmap.infosysimage);
                }
                else {

                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(editimage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.changeimage :
                OpenImage();
                break;
        }

    }

    private void OpenImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    public String getFileExtension(Uri uri){

        ContentResolver contentResolver =getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private  void UploadImage() {
         dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading on progress.....");
        dialog.show();
        if (imageurl != null) {

            final StorageReference filereference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageurl));

            uploadTask = filereference.putFile(imageurl);


            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {

                        Uri downloadurl = (Uri) task.getResult();
                        String murl = downloadurl.toString();

                        reference = FirebaseDatabase.getInstance().getReference("USERS").child(firebaseUser.getUid());
                        HashMap <String,Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL",murl);
                        reference.updateChildren(hashMap);
                      dialog.dismiss();

                    }
                    else {
                       dialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode ==RESULT_OK && data != null ){
            imageurl = data.getData();

        if (uploadTask !=null && uploadTask.isInProgress()){

            Toast.makeText(this, "Uploading on progress", Toast.LENGTH_SHORT).show();
        }
        else {
            UploadImage();
        }
    }
    }
}