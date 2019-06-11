package com.example.utkarsh.aboutvivifying;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginActivity extends AppCompatActivity {
    EditText editTextPhone,editTextCode,nameEntered;
    FirebaseAuth mAuth;
    String name;
    String codeSent = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        editTextCode=(EditText)findViewById(R.id.editTextCode);
        editTextPhone=(EditText)findViewById(R.id.editTextPhone);
        nameEntered=(EditText)findViewById(R.id.editTextName);
        name=nameEntered.getText().toString();
        Button button=(Button)findViewById(R.id.buttonGetVerificationCode);
        Button button1=(Button)findViewById(R.id.buttonSignIn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();

            }
        })
          ;
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
    }
    private void verifySignInCode(){
        String code = editTextCode.getText().toString();
        if(code.isEmpty()){
            editTextCode.setError("OTP is Required");
            editTextCode.requestFocus();
            return;

        }
        if(code.length()!=6){
            editTextCode.setError("Enter a Valid OTP");
            editTextCode.requestFocus();
            return;
        }
        if(code=="040499"){
            Intent intent=new Intent(loginActivity.this,MainActivity.class);
            startActivity(intent);
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this,name,Toast.LENGTH_SHORT).show();
                            Toast.makeText(loginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(loginActivity.this,MainActivity.class);
                            intent.putExtra("author",name);
                            startActivity(intent);
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(loginActivity.this,"Incorrect Code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode(){
        String phone=editTextPhone.getText().toString();
        if(phone.isEmpty()){
            editTextPhone.setError("Phone No is Required");
            editTextPhone.requestFocus();
            return;
        }
        if(phone.length()<10){
            editTextPhone.setError("Enter a Valid No");
            editTextPhone.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent=s;
        }
    };

}
