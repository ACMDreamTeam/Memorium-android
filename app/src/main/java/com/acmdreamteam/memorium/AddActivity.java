package com.acmdreamteam.memorium;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser firebaseUser;

    EditText username;

    RadioButton journal,reminder,low,medium,high;

    String type;

    String priority;

    DateFormat df;

    AppCompatButton submit;

    String sDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        username = findViewById(R.id.username);

        Date d = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        sDate = df.format(d);

        journal = findViewById(R.id.journal);
        reminder = findViewById(R.id.reminder);

        low = findViewById(R.id.low);
        medium = findViewById(R.id.medium);
        high = findViewById(R.id.high);

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });




    }

    @SuppressLint("NonConstantResourceId")
    public void onPrioritySelect(View view){
        switch (view.getId()) {

            case R.id.low:
                if (low.isChecked()) {

                    priority = "1";

                }
                break;

            case R.id.medium:
                if (medium.isChecked()) {

                    priority = "2";


                }
                break;
            case R.id.high:
                if (high.isChecked()) {

                    priority = "3";


                }
                break;



        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onReminderSelect(View view){
        switch (view.getId()) {

            case R.id.journal:
                if (journal.isChecked()) {

                    type = "journal";

                }
                break;

            case R.id.reminder:
                if (reminder.isChecked()) {

                    type = "reminder";


                }
                break;



        }
    }

    private void submitData() {


        Map<String, Object> journal = new HashMap<>();
        journal.put("things", username.getText().toString());
        journal.put("date", sDate);
        journal.put("type",type);
        journal.put("priority",priority);


        db.collection("journal").document(firebaseUser.getUid()).collection(type).document()
                .set(journal)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        finish();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}