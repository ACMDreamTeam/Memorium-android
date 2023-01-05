package com.acmdreamteam.memorium;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MedicineReminder extends AppCompatActivity {

    EditText Med_Name,number,Duration;


    RadioButton Daily,weekly,before_food,After_food;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    String frequency;
    String food;
    String time;

    String med_type_txt,weekday_txt;

    Spinner getTypeOfMed,getWeekday;

    RelativeLayout weekday_layout;

    String[] type_of_med = {"Tablet", "Syrup","Powder","Injection","Oinment","Other"};
    String[] weekday_list = {"Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};



    AppCompatButton submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);



        Med_Name = findViewById(R.id.name_txt);
        number = findViewById(R.id.number);
        Duration = findViewById(R.id.duration);

        Daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        before_food = findViewById(R.id.bfood);
        After_food = findViewById(R.id.afood);



        getWeekday = findViewById(R.id.weekday);
        getTypeOfMed = findViewById(R.id.typ_txt);

        weekday_layout = findViewById(R.id.weekday_layout);



        ArrayAdapter med_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type_of_med);

        ArrayAdapter weekday_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, weekday_list);

        med_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        weekday_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getWeekday.setAdapter(weekday_adapter);
        getTypeOfMed.setAdapter(med_adapter);

        getWeekday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                weekday_txt = weekday_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        getTypeOfMed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                med_type_txt = type_of_med[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder().build();

        Duration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    materialTimePicker.show(getSupportFragmentManager(),"Time picker");
                }

            }



        });




        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hour = String.valueOf(materialTimePicker.getHour());
                String minute = String.valueOf(materialTimePicker.getMinute());
                time = hour + " : " + minute;
                Duration.setText(time);
            }
        });

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
                    weekday_layout.setVisibility(View.GONE);

                }
                break;

            case R.id.weekly:
                if (weekly.isChecked()) {

                    frequency = "Weekly";
                    weekday_layout.setVisibility(View.VISIBLE);



                }
                break;

            case R.id.bfood:
                if (before_food.isChecked()) {

                    food = "false";


                }
                break;
            case R.id.afood:
                if (After_food.isChecked()) {

                    food = "true";


                }
                break;



        }
    }




    private void submitData() {

        String med_id = Randomizer(10);

        Map<String, Object> journal = new HashMap<>();
        journal.put("name", Med_Name.getText().toString());
        journal.put("number", number.getText().toString());
        journal.put("duration",Duration.getText().toString());
        journal.put("med_id",med_id);
        journal.put("med_type",med_type_txt);
        journal.put("weekday",weekday_txt);
        journal.put("frequency", frequency);
        journal.put("food",food);


        db.collection("user_data").document(firebaseUser.getUid()).collection("Medicine").document(med_id)
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

    private String Randomizer(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890" + "abcdefghijklmnopqrstuvxyz";



        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string

        for(int i = 0; i < n; i++) {

            // generate random index number
            int index = random.nextInt(AlphaNumericString.length());

            // get character specified by index
            // from the string
            char randomChar = AlphaNumericString.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }


        return sb.toString();
    }

}