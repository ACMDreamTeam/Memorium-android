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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {


    EditText username_,age_;


    AppCompatButton submit;

    RadioButton male,female;


    String gender;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        username_ = findViewById(R.id.username);
        age_ = findViewById(R.id.age);

        submit = findViewById(R.id.submit);





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = new User(username_.getText().toString(),age_.getText().toString(),gender,"");

                Log.d("name",username_.getText().toString());


                Intent intent = new Intent();
                intent.putExtra("object",user);
                intent.setClass(SignupActivity.this,QuestionnaireActivity.class);
                startActivity(intent);





                //submit_Data();
            }
        });





    }

    public void onGenderSelect(View view){
        switch (view.getId()){

            case R.id.male:
                if (male.isChecked()){

                    gender = "male";

                }
                break;

            case R.id.female:
                if (female.isChecked()){

                    gender = "female";


                }
                break;




        }
    }

    private void submit_Data() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, String> user = new HashMap<>();
        user.put("username", username_.getText().toString());
        user.put("age",age_.getText().toString());
        user.put("gender",gender);


        db.collection("users").document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
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