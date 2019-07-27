package com.shellyAmbar.ambar.workoutplanner;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView InputEmail;
    private AutoCompleteTextView InputPass;
    private Button signUp;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private MediaPlayer click_sound;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(ContextCompat.getDrawable(VerificationActivity.this,R.drawable.ic_arrow_back_black_24dp));
        progressBar = findViewById(R.id.progressBar);
        InputEmail=findViewById(R.id.email);
        InputPass=findViewById(R.id.password);
        signUp=findViewById(R.id.sign_up_button);
        click_sound = MediaPlayer.create(VerificationActivity.this,R.raw.click);

        mAuth=FirebaseAuth.getInstance();
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sign_up_button:
                click_sound.start();
                progressBar.setVisibility(View.VISIBLE);
                if(InputEmail.getText().toString().equals("")||InputPass.getText().toString().equals("") ){
                    Toast.makeText(VerificationActivity.this, R.string.blank_detail,
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }else{

                    signupUser( InputEmail.getText().toString(),InputPass.getText().toString());

                }


                break;



        }


    }

    private void signupUser(final String email, final String pass) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    if(pass.length()<6 ) {
                        Toast toast= Toast.makeText(VerificationActivity.this, R.string.pass_length,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if(!email.contains("@")){
                        Toast toast=  Toast.makeText(VerificationActivity.this, R.string.pass_contain,Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    Toast.makeText(VerificationActivity.this,R.string.error_signup, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else{
//if person didnt add gender

                    progressBar.setVisibility(View.GONE);

                    Intent intent=new Intent(VerificationActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();


                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
