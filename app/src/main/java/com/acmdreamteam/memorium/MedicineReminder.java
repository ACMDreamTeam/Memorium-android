package com.acmdreamteam.memorium;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
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

import java.util.HashMap;
import java.util.Map;

public class MedicineReminder extends AppCompatActivity {

    EditText name,number,dur;

    RadioButton Daily,weekly,before_food,After_food;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    String frequency;
    boolean food;

    AppCompatButton submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);


        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        dur = findViewById(R.id.duration);

        Daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        before_food = findViewById(R.id.bfood);
        After_food = findViewById(R.id.afood);

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });


    }

    @SuppressLint("NonConstantResourceId")
    public void onReminderSelect(View view){
        switch (view.getId()) {

            case R.id.daily:
                if (Daily.isChecked()) {

                    frequency = "Daily";

                }
                break;

            case R.id.weekly:
                if (weekly.isChecked()) {

                    frequency = "Weekly";


                }
                break;

            case R.id.bfood:
                if (before_food.isChecked()) {

                    food = false;


                }
                break;
            case R.id.afood:
                if (After_food.isChecked()) {

                    food = true;


                }
                break;



        }
    }




    private void submitData() {


        Map<String, Object> journal = new HashMap<>();
        journal.put("name", name.getText().toString());
        journal.put("number", number.getText().toString());
        journal.put("dur",dur.getText().toString());
        journal.put("frequency", frequency);
        journal.put("food",food);


        db.collection("Medications").document(firebaseUser.getUid())
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