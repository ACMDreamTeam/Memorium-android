package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    FirebaseAuth firebaseAuth;

    GoogleSignInClient googleSignInClient;

    ProgressBar googleSignLoader;

    AppCompatButton login;


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);





        googleSignLoader = findViewById(R.id.googleSignLoader);
        googleSignLoader.setVisibility(View.GONE);

        SignInButton signInButton = findViewById(R.id.signin);

        firebaseAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.login);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("909712011152-b500qorah01hr3f5ln7nq6nsie6git5u.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(StartActivity.this,googleSignInOptions);

        signInButton.setOnClickListener(v -> {
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent,100);
            googleSignLoader.setVisibility(View.VISIBLE);

        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartActivity.this, "Coming soon! Please Sign in with Google", Toast.LENGTH_SHORT).show();
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==100){
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            if(signInAccountTask.isSuccessful()){
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);

                    if(googleSignInAccount != null){
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);


                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        String s = "Google Authentication successful";
                                        googleSignLoader.setVisibility(View.INVISIBLE);
                                        displayToast(s);


                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        assert firebaseUser != null;

                                        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                                        rootRef.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @SuppressLint("CheckResult")
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        startActivity(new Intent(StartActivity.this,MainActivity.class));

                                                    }else {
                                                        startActivity(new Intent(StartActivity.this,SignupActivity.class));
                                                    }
                                                }
                                            }
                                        });






                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void displayToast(String s) {

        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}