package com.acmdreamteam.memorium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.acmdreamteam.memorium.Adapter.JournalAdapter;
import com.acmdreamteam.memorium.Adapter.PeopleAdapter;
import com.acmdreamteam.memorium.Model.Journal;
import com.acmdreamteam.memorium.Model.People;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PeoplesActivity extends AppCompatActivity {


    private ArrayList<People> mPeople;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    RecyclerView recyclerView;

    ImageView capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peoples);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        capture = findViewById(R.id.capture);

        mPeople = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PeoplesActivity.this,RecogActivity.class));
            }
        });

        EventChangeListener();


    }

    private void EventChangeListener() {

        assert firebaseUser != null;
        firestore.collection("user_data").document(firebaseUser.getUid()).collection("face_data")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        mPeople.clear();
                        assert value != null;
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                mPeople.add(documentChange.getDocument().toObject(People.class));
                            }

                            PeopleAdapter peopleAdapter = new PeopleAdapter(getApplicationContext(),firebaseUser,mPeople);
                            recyclerView.setAdapter(peopleAdapter);
                            peopleAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
