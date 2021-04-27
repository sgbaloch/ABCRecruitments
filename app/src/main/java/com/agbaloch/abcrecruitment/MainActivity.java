package com.agbaloch.abcrecruitment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agbaloch.abcrecruitment.Models.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnRegister, btnLogIn;
    private TextView txtAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUIComponents();

        //FirebaseFirestore db = FirebaseFirestore.getInstance();


    }

    private void wireUIComponents(){

        btnLogIn = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        txtAdmin = findViewById(R.id.txt_adminLogin);
    }

    private void logInAsAdmin(){


    }

    private void createAccount(){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void logIn(){


    }

    public void logInAsAdmin(View view) {

        Toast.makeText(this, "Admin", Toast.LENGTH_LONG).show();
    }

    public void logIn(View view) {

    }

    public void createAccount(View view) {

        createAccount();
    }
}
