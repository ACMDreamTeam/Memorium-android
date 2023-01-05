package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.acmdreamteam.memorium.Adapter.JournalAdapter;
import com.acmdreamteam.memorium.Model.Journal;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JournalViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private ArrayList<Journal> mJournal;

    private JournalAdapter journalAdapter;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    CircleImageView profileImage;

    ImageView add_journal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_view);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileImage = findViewById(R.id.profile_image);

        add_journal = findViewById(R.id.add_journal);


        mJournal = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

        add_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JournalViewActivity.this,JournalAddActivity.class));
            }
        });

        EventChangeListener();


    }

    private void EventChangeListener() {

        firestore.collection("journal").document(firebaseUser.getUid()).collection("journal")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        assert value != null;
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                mJournal.add(documentChange.getDocument().toObject(Journal.class));
                            }

                            journalAdapter = new JournalAdapter(getApplicationContext(),firebaseUser,mJournal);
                            recyclerView.setAdapter(journalAdapter);
                            journalAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(JournalViewActivity.this,RecollectActivity.class));
    }
}