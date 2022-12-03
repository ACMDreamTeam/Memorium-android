package com.acmdreamteam.memorium;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {


    EditText username_,age_;


    AppCompatButton submit;

    Spinner genderspinner;

    String gender;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        username_ = findViewById(R.id.username);
        age_ = findViewById(R.id.age);

        submit = findViewById(R.id.submit);

        genderspinner =findViewById(R.id.gender_item);

        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.Gender, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        genderspinner.setAdapter(adapter);


        if(genderspinner != null){
            genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item_position = String.valueOf(position);
                    int itemposition = Integer.parseInt(item_position);
                    String gender = String.valueOf(genderspinner.getAdapter().getItem(position));
                    Log.e("selected position",""+itemposition);
                    Log.e("selected Text",gender);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                submit_Data();
            }
        });





    }

    private void submit_Data() {

        Map<String, String> user = new HashMap<>();
        user.put("username", username_.getText().toString());
        user.put("age",age_.getText().toString());
        user.put("gender",gender);


        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        startActivity(new Intent(SignupActivity.this,QuestionnaireActivity.class));
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