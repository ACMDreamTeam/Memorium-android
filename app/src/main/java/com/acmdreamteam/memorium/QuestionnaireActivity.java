package com.acmdreamteam.memorium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.acmdreamteam.memorium.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class QuestionnaireActivity extends AppCompatActivity {


    LinearLayout married,spouse_name,siblings;

    EditText spouse_name_txt;

    RadioButton yes,no,notSure,yes_,no_,notSure_;

    AppCompatButton married_next,go_back,siblings_next;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    Button spouse_save;


    SignupActivity signupActivity;

    HashMap<String,String> data = new HashMap<>();

    User user;

    boolean married_boolean = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        married = findViewById(R.id.married);
        siblings = findViewById(R.id.siblings);

        married.setVisibility(View.VISIBLE);

        married_next = findViewById(R.id.married_next);
        siblings_next = findViewById(R.id.siblings_next);
        go_back = findViewById(R.id.go_back);

        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        notSure = findViewById(R.id.notSure);

        yes_ = findViewById(R.id.yes_);
        no_ = findViewById(R.id.no_);
        notSure_ = findViewById(R.id.notSure_);

        spouse_name = findViewById(R.id.spouse_name);
        spouse_save = findViewById(R.id.submit);
        spouse_name_txt = findViewById(R.id.name_txt);


        spouse_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = spouse_name_txt.getText().toString();

                data.put("Spouse_Name",name);

                spouse_name.setVisibility(View.INVISIBLE);
                siblings.setVisibility(View.VISIBLE);

            }
        });



        married_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(married_boolean){
                    married.setVisibility(View.INVISIBLE);
                    spouse_name.setVisibility(View.VISIBLE);
                }else {
                    married.setVisibility(View.INVISIBLE);
                    siblings.setVisibility(View.VISIBLE);
                }


            }
        });


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                married.setVisibility(View.VISIBLE);
                siblings.setVisibility(View.INVISIBLE);
            }
        });


        siblings_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(firebaseUser.getUid()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(QuestionnaireActivity.this,MainActivity.class));
                    }
                });
            }
        });

        //user = (User) getIntent().getExtras().getSerializable("object");

        //String name = user.getName();

       // Log.d("Data", name);


    }

    public void onQuestionAnswered(View view){

        switch (view.getId()){

            case R.id.yes:
                if (yes.isChecked()){




                    data.put("Married","Married");

                    married_boolean = true;



                }
                break;

            case R.id.no:
                if (no.isChecked()){


                    data.put("Married","Single");
                    married_boolean = false;





                }
                break;

            case R.id.notSure:
                if (notSure.isChecked()){



                    data.put("Married","Not Sure");
                    married_boolean = false;





                }
                break;

            case R.id.yes_:
                if (yes_.isChecked()){


                    data.put("Have Siblings","Yes");




                }
                break;

            case R.id.no_:
                if (no_.isChecked()){


                    data.put("Have Siblings","No");




                }
                break;

            case R.id.notSure_:
                if (notSure_.isChecked()){


                    data.put("Have Siblings","Not Sure");




                }
                break;



        }






    }
}