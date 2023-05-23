package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.acmdreamteam.memorium.Adapter.JournalAdapter;
import com.acmdreamteam.memorium.Adapter.MedRemAdapter;
import com.acmdreamteam.memorium.Model.Journal;
import com.acmdreamteam.memorium.Model.MedRem;
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
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicineReminderViewActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    TextView greet,name;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    CircleImageView profile_image;


    String MedID;

    private ArrayList<MedRem> mMedrem;

    private MedRemAdapter medRemAdapter;

    ImageView add_med;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder_view);


        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        add_med = findViewById(R.id.add_med);

        //profile_image = findViewById(R.id.profile_image);
        greet = findViewById(R.id.greet);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){

            greet.setText("Good morning");

        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greet.setText("Good Afternoon");

        }else if(timeOfDay >= 16 && timeOfDay < 24){
            greet.setText("Good Evening");

        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greet.setText("Good Night");
        }





        name = findViewById(R.id.name);



        mMedrem = new ArrayList<>();



        firestore = FirebaseFirestore.getInstance();

        add_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MedicineReminderViewActivity.this,MedicineReminder.class));
            }
        });


        firestore.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Name = document.getString("username");
                        //String ImageURl = document.getString("imageURL");

                        name.setText(Name);

                        /*

                        if(Objects.equals(ImageURl, "user")){
                            Glide.with(getApplicationContext()).load(getApplicationContext().getDrawable(R.drawable.ic_baseline_account_circle_24)).into(profile_image);
                        }else {
                            Glide.with(getApplicationContext()).load(ImageURl).into(profile_image);
                        }

                         */





                    }
                }
            }
        });

        loadData();







    }

    private void loadData(){

        firestore.collection("user_data").document(firebaseUser.getUid()).collection("Medicine")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mMedrem.clear();
                        assert value != null;
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                mMedrem.add(documentChange.getDocument().toObject(MedRem.class));
                            }

                            medRemAdapter = new MedRemAdapter(getApplicationContext(),firebaseUser,mMedrem);
                            recyclerView.setAdapter(medRemAdapter);
                            medRemAdapter.notifyDataSetChanged();

                        }
                    }
                });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
}