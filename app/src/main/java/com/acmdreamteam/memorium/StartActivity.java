package com.acmdreamteam.memorium;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    FirebaseAuth firebaseAuth;

    GoogleSignInClient googleSignInClient;

    ProgressBar googleSignLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){

        }


        googleSignLoader = findViewById(R.id.googleSignLoader);
        googleSignLoader.setVisibility(View.GONE);

        SignInButton signInButton = findViewById(R.id.signin);

        firebaseAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("909712011152-b500qorah01hr3f5ln7nq6nsie6git5u.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(StartActivity.this,googleSignInOptions);

        signInButton.setOnClickListener(v -> {
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent,100);

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
                                        googleSignLoader.setVisibility(View.VISIBLE);
                                        displayToast(s);

                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        assert firebaseUser != null;


                                        startActivity(new Intent(StartActivity.this,SignupActivity.class));
                                        /*
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                                        String userid = firebaseUser.getDisplayName();
                                        String username = firebaseUser.getDisplayName();
                                        String imageurl;

                                        Calendar calendar = Calendar.getInstance();
                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                                        String timestamp = simpleDateFormat.format(calendar.getTime());

                                        Uri imageuri = firebaseUser.getPhotoUrl();
                                        assert imageuri != null;
                                        imageurl = imageuri.toString();
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if(!snapshot.exists()){
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("id", userid);
                                                    hashMap.put("username", username);
                                                    hashMap.put("imageURL", imageurl);
                                                    hashMap.put("joined_on",timestamp);
                                                    reference.updateChildren(hashMap).addOnCompleteListener(task1 -> startActivity(new Intent(StartActivity.this, google_setupActivity.class)
                                                            .putExtra("method","google")
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
                                                }else {
                                                    startActivity(new Intent(StartActivity.this,google_setupActivity.class)
                                                            .putExtra("method","google")
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                         */




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