package com.restaurent.swaadapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
public class MainActivity extends AppCompatActivity {
    private TextView later_log;
    private LinearLayout googleSignin;
    private final static int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private UserAccount userAccount;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        later_log = findViewById(R.id.later_log);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignin  = findViewById(R.id.google_login);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignin.setOnClickListener(v->signin());
        later_log.setOnClickListener(later->{
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        });
    }
    public void signin(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       if(currentUser!=null){
           startActivity(new Intent(getApplicationContext(),HomeActivity.class));
           finish();
       }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,task->{
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getUid());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                  userAccount = dataSnapshot1.getValue(UserAccount.class);
                                  PersonalDetails personalDetails = userAccount.getPersonalDetails();
                                  String checkEmail = personalDetails.getEmail();
                                  if(checkEmail.equals(user.getEmail())) {
//                                      Toast.makeText(getApplicationContext(),checkEmail,Toast.LENGTH_SHORT).show();
                                  return;
                                  }
                              }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
//                        if(counter==0){
                            PersonalDetails personalDetails1 = new PersonalDetails(user.getDisplayName(),user.getEmail(),user.getPhoneNumber());
                            UserAccount userAccount = new UserAccount(null,personalDetails1,null);
                            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Users").child(user.getUid());
                            databaseReference1.child("userAccount").setValue(userAccount);
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
//                        }
//                        else {
//                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                            finish();
//                        }
                 }
                    else {
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
