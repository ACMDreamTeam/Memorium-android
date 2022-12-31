package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.acmdreamteam.memorium.Adapter.JournalAdapter;
import com.acmdreamteam.memorium.Model.Journal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecollectActivity extends AppCompatActivity {

    TextView name,age,marsta,sibsta;

    RecyclerView recyclerView;


    private ArrayList<Journal> mJournal;

    private JournalAdapter journalAdapter;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recollect);




        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        marsta = findViewById(R.id.marsta);
        sibsta = findViewById(R.id.sibsta);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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



        mJournal = new ArrayList<>();

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
}