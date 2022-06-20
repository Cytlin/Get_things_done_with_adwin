package com.example.getthingsdonewithadwin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
        EditText registerName,registerEmail, registerPassword, confirmPassword;
        Button registerBtn;
        TextView goToLogin;
        Boolean valid=true;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName= findViewById(R.id.registerName);
        registerEmail= findViewById(R.id.registerEmail);
        registerPassword= findViewById(R.id.registerPassword);
        confirmPassword= findViewById(R.id.confirmPassword);
        registerBtn= findViewById(R.id.registerBtn);
        goToLogin= findViewById(R.id.goToLogin);

        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        //check if it's a returning user
        if(fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= registerName.getText().toString().trim();
                String email= registerEmail.getText().toString().trim();
                String password= registerPassword.getText().toString().trim();
                String confirmPass= confirmPassword.getText().toString().trim();

                checkField(registerName);
                checkField(registerEmail);
                checkField(registerPassword);
                checkField(confirmPassword);
                if(password.length()<6){
                    registerPassword.setError("Password must be longer than 6 characters");
                    return;
                }
//                if(!(confirmPass==password)){
//                    confirmPassword.setError("Passwords must match");
//                    return;
//                }
                //Start Registration process
                if(valid){
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            userId= fAuth.getCurrentUser().getUid();
                            user = fAuth.getCurrentUser();
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, " Registered Successfully", Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference= fStore.collection("users").document(userId);
                                Map<String,Object> userInfo= new HashMap<>();
                                userInfo.put("FullName",name);
                                userInfo.put("UserEmail",email);
                                documentReference.set(userInfo);
                                startActivity(new Intent(getApplicationContext(),TasksActivity.class));
                            }else{
                                Toast.makeText(Register.this, "Failed to register"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    //Check if Text fields are empty
    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("All fields are required");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}