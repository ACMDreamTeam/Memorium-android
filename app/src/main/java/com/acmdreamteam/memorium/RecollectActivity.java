package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecollectActivity extends AppCompatActivity {

    TextView name,age,marsta,sibsta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recollect);


        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        marsta = findViewById(R.id.marsta);
        sibsta = findViewById(R.id.sibsta);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String username_ = document.getString("username");
                        String age_ = document.getString("age");
                        String marsta_ = document.getString("Married");
                        String sibsta_ = document.getString("Have Siblings");

                        name.setText("Name: " + username_);
                        age.setText("Age: " + age_);
                        marsta.setText("Marital Status: " + marsta_);
                        sibsta.setText("Has Siblings?: " + sibsta_);


                    }
                }
            }
        });


    }
}