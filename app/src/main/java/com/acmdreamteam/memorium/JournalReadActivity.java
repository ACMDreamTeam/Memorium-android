package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class JournalReadActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    TextView title,date,things;

    String journalID;

    Intent intent;

    CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_read);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore = FirebaseFirestore.getInstance();

        intent = getIntent();
        journalID = intent.getStringExtra("journalID");

        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        things = findViewById(R.id.things);

        profileImage = findViewById(R.id.profile_image);

        firestore.collection("journal").document(firebaseUser.getUid()).collection("journal").document(journalID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Title = document.getString("title");
                        String Date = document.getString("date");
                        String Things = document.getString("things");



                        title.setText(Title);
                        date.setText(Date);
                        things.setText(Things);


                    }
                }
            }
        });

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String Imageurl = document.getString("imageURL");



                        Glide.with(getApplicationContext()).load(Imageurl).into(profileImage);


                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(JournalReadActivity.this,JournalViewActivity.class));
    }
}