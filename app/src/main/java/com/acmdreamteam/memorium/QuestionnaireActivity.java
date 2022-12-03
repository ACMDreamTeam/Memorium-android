package com.acmdreamteam.memorium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class QuestionnaireActivity extends AppCompatActivity {


    LinearLayout married,siblings;

    RadioButton yes,no,notSure,yes_,no_,notSure_;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    SignupActivity signupActivity;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        married = findViewById(R.id.married);
        siblings = findViewById(R.id.siblings);

        married.setVisibility(View.VISIBLE);


        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        notSure = findViewById(R.id.notSure);

        yes_ = findViewById(R.id.yes_);
        no_ = findViewById(R.id.no_);
        notSure_ = findViewById(R.id.notSure_);




        //user = (User) getIntent().getExtras().getSerializable("object");

        //String name = user.getName();

       // Log.d("Data", name);


    }

    public void onQuestionAnswered(View view){
        switch (view.getId()){

            case R.id.yes:
                if (yes.isChecked()){



                    HashMap<String,String> data = new HashMap<>();
                    data.put("Married","Yes");

                    db.collection("users").document(firebaseUser.getUid()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();

                        }
                    });

                    married.setVisibility(View.INVISIBLE);
                    siblings.setVisibility(View.VISIBLE);




                }
                break;

            case R.id.no:
                if (no.isChecked()){

                    HashMap<String,String> data = new HashMap<>();
                    data.put("Married","No");

                    db.collection("users").document(firebaseUser.getUid()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();

                        }
                    });

                    married.setVisibility(View.INVISIBLE);
                    siblings.setVisibility(View.VISIBLE);






                }
                break;

            case R.id.notSure:
                if (notSure.isChecked()){


                    HashMap<String,String> data = new HashMap<>();
                    data.put("Married","Not Sure");

                    db.collection("users").document(firebaseUser.getUid()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();

                        }
                    });

                    married.setVisibility(View.INVISIBLE);
                    siblings.setVisibility(View.VISIBLE);




                }
                break;

            case R.id.yes_:
                if (yes_.isChecked()){

                    HashMap<String,String> data = new HashMap<>();
                    data.put("Have Siblings","Yes");

                    db.collection("users").document(firebaseUser.getUid()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                        }
                    });

                    startActivity(new Intent(QuestionnaireActivity.this,MainActivity.class));



                }
                break;

            case R.id.no_:
                if (no_.isChecked()){

                    HashMap<String,String> data = new HashMap<>();
                    data.put("Have Siblings","No");

                    db.collection("users").document(firebaseUser.getUid()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                        }
                    });

                    startActivity(new Intent(QuestionnaireActivity.this,MainActivity.class));



                }
                break;

            case R.id.notSure_:
                if (notSure_.isChecked()){

                    HashMap<String,String> data = new HashMap<>();
                    data.put("Have Siblings","Not Sure");

                    db.collection("users").document(firebaseUser.getUid()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                        }
                    });

                    startActivity(new Intent(QuestionnaireActivity.this,MainActivity.class));



                }
                break;



        }
    }
}