package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.iosdialog.iOSDialog;
import com.margsapp.iosdialog.iOSDialogListener;

import java.util.Objects;

public class MedicationViewActivity extends AppCompatActivity {


    String MedID;

    Intent intent;

    TextView Med_Name,Med_create,Med_food,Med_freq,Med_time,Med_week,Med_no;

    CardView Delete;

    ImageView Med_typeImg;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_view);

        intent = getIntent();

        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        MedID = intent.getStringExtra("MedID");

        Med_Name = findViewById(R.id.med_name);
        Med_typeImg = findViewById(R.id.med_type);
        Med_create = findViewById(R.id.med_create);
        Med_food = findViewById(R.id.med_food);
        Med_freq = findViewById(R.id.med_frequency);
        Med_time = findViewById(R.id.med_time);
        Med_week = findViewById(R.id.med_weekday);
        Med_no = findViewById(R.id.med_no);

        Delete = findViewById(R.id.Delete_card);


        loadData();


        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iOSDialog.Builder
                        .with(MedicationViewActivity.this)
                        .setTitle("Delete Medicine")
                        .setMessage("Are you sure you want to delete this medicine?")
                        .setPositiveText("Yes")
                        .setPostiveTextColor(getResources().getColor(com.margsapp.iosdialog.R.color.red))
                        .setNegativeText("No")
                        .setNegativeTextColor(getResources().getColor(com.margsapp.iosdialog.R.color.company_blue))
                        .onPositiveClicked(new iOSDialogListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                firestore = FirebaseFirestore.getInstance();
                                firestore.collection("user_data").document(firebaseUser.getUid()).collection("Medicine").document(MedID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();
                                    }
                                });
                            }
                        })
                        .onNegativeClicked(new iOSDialogListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                //Do Nothing
                            }
                        })
                        .isCancellable(true)
                        .build()
                        .show();

            }
        });




    }

    private void loadData() {

        firestore.collection("user_data").document(firebaseUser.getUid()).collection("Medicine").document(MedID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name = document.getString("name");
                        String type = document.getString("med_type");

                        String food = document.getString("food");
                        String freq = document.getString("frequency");
                        String time = document.getString("time");
                        String no = document.getString("number");
                        String date = document.getString("create_date");


                        if(Objects.equals(freq, "Weekly")){
                            String week = document.getString("weekday");
                            Med_week.setText(week);
                        }




                        Med_Name.setText(name);
                        Med_create.setText("Created on " + date);


                        Med_food.setText(food);
                        Med_freq.setText(freq);
                        Med_time.setText(time);

                        if(Objects.equals(type, "Tablet")){
                            Med_typeImg.setImageDrawable(getDrawable(R.drawable.pills));
                            Med_no.setText(no);
                        }

                        if(Objects.equals(type, "Syrup")){
                            Med_typeImg.setImageDrawable(getDrawable(R.drawable.syrup));
                            Med_no.setText(no);
                        }

                        if(Objects.equals(type, "Powder")){
                            Med_typeImg.setImageDrawable(getDrawable(R.drawable.powder));
                            Med_no.setText(no);
                        }

                        if(Objects.equals(type, "Injection")){
                            Med_typeImg.setImageDrawable(getDrawable(R.drawable.injection));
                            Med_no.setText(no);
                        }

                        if(Objects.equals(type, "Oinment")){
                            Med_typeImg.setImageDrawable(getDrawable(R.drawable.ointment));
                            Med_no.setText("");
                        }

                        if(Objects.equals(type, "Other")){
                            Med_typeImg.setImageDrawable(getDrawable(R.drawable.ic_baseline_account_circle_24));
                            Med_no.setText("");
                        }



                    }
                }
            }
        });
    }
}