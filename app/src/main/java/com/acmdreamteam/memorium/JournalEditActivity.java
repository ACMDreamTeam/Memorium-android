package com.acmdreamteam.memorium;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class JournalEditActivity extends AppCompatActivity {

    EditText title,things;
    CardView date_card;
    TextView date_txt;

    Button submit;

    String JournalID;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_edit);

        title = findViewById(R.id.title);
        things = findViewById(R.id.things);
        submit = findViewById(R.id.submit);

        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();

        JournalID = intent.getStringExtra("JournalID");



        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("Select the date");
        materialDateBuilder.setPositiveButtonText("Ok");
        materialDateBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        materialDateBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();


        date_card = findViewById(R.id.date_card);
        date_txt = findViewById(R.id.date_txt);


        date_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });


        firestore.collection("user_data").document(firebaseUser.getUid()).collection("journal").document(JournalID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Title_ = document.getString("title");
                        String Date_ = document.getString("date");
                        String Things = document.getString("things");



                        title.setText(Title_);
                        date_txt.setText(Date_);
                        things.setText(Things);


                    }
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String strDate = dateFormat.format(selection);
                date_txt.setText(strDate);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData(JournalID);
            }
        });




    }

    private void submitData(String JournalID) {

        Map<String, Object> journal = new HashMap<>();
        journal.put("title",title.getText().toString());
        journal.put("things", things.getText().toString());
        journal.put("date", date_txt.getText().toString());
        journal.put("journalID",JournalID);





        firestore.collection("user_data").document(firebaseUser.getUid()).collection("journal").document(JournalID)
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