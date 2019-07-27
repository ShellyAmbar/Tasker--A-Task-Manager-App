package com.shellyAmbar.ambar.workoutplanner;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private AutoCompleteTextView InputEmail;
    private AutoCompleteTextView InputPass;
    private TextView btn_forgot_pass;
    private TextView btn_signup;
    Button mEmailSignInButton;
    private FirebaseAuth mAuth;
    private MediaPlayer click_sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        InputEmail = (AutoCompleteTextView) findViewById(R.id.email);
        InputPass = (AutoCompleteTextView) findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        btn_forgot_pass=findViewById(R.id.btn_forgot_password);
        btn_signup=findViewById(R.id.btn_layout_signup);

        click_sound = MediaPlayer.create(LoginActivity.this,R.raw.click);

        mEmailSignInButton.setOnClickListener(this);
        btn_forgot_pass.setOnClickListener(this);
        btn_signup.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_forgot_password:

                click_sound.start();
                startActivity(new Intent(LoginActivity.this, ResetPassword.class));

                break;

            case R.id.btn_layout_signup:

                click_sound.start();
                Intent intent=new Intent(LoginActivity.this,VerificationActivity.class);

                startActivity(intent);


                break;

            case R.id.email_sign_in_button:
                click_sound.start();
                progressBar.setVisibility(View.VISIBLE);

                if( InputEmail.getText().toString().equals("") || InputPass.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, R.string.blank_detail,
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }else{

                    loginUser(InputEmail.getText().toString(), InputPass.getText().toString());

                }
                break;



        }
    }

    private void loginUser(final String Email, final String Pass) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(Email, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            Toast.makeText(LoginActivity.this,R.string.success_enter,Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("publisherId", mAuth.getCurrentUser().getUid());
                            startActivity(intent);

                            progressBar.setVisibility(View.GONE);


                        } else {
                            // If sign in fails, display a message to the user.

                            if (Pass.length()<6 ){
                                Toast.makeText(LoginActivity.this, R.string.pass_length, Toast.LENGTH_SHORT).show();
                            }
                            if(!Email.contains("@")){
                                Toast.makeText(LoginActivity.this, R.string.pass_contain, Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(LoginActivity.this, R.string.error_verify,
                                    Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);

                        }

                        // ...
                    }
                });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        if(mAuth != null)
        {
           mAuth.signOut();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


}
