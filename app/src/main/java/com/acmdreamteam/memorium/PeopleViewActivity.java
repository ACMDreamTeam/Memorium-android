package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleViewActivity extends AppCompatActivity {

    Intent intent;

    String nameID;

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    TextView name,phone,notes;

    CircleImageView face_view;

    AppCompatButton back,delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_view);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        notes = findViewById(R.id.notes);
        face_view = findViewById(R.id.face_view);
        intent = getIntent();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        nameID = intent.getStringExtra("name");

        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePerson();
            }
        });

        loadData();
    }

    private void loadData() {


        firestore.collection("user_data").document(firebaseUser.getUid()).collection("face_data").document(nameID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name_ = document.getString("name");
                        String phone_ = document.getString("phoneNo");
                        String notes_ = document.getString("notes");
                        String ImageURL = document.getString("imageURL");


                        name.setText(name_);
                        phone.setText(phone_);
                        notes.setText(notes_);
                        Glide.with(getApplicationContext()).load(ImageURL).into(face_view);


                    }
                }
            }
        });
    }

    private void deletePerson(){
        firestore.collection("user_data").document(firebaseUser.getUid()).collection("face_data").document(nameID).delete();
    }
}
