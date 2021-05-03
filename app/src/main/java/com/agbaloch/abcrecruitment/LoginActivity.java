package com.agbaloch.abcrecruitment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agbaloch.abcrecruitment.Database.Schema.JobSeekerTable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;

    private Button btnLogin;

    private FirebaseFirestore db;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireUIComponents();

        db = FirebaseFirestore.getInstance();
    }

    private void wireUIComponents() {

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection(JobSeekerTable.TABLE_NAME)
                        .whereEqualTo(JobSeekerTable.EMAIL, edtEmail.getText().toString())
                        .whereEqualTo(JobSeekerTable.PASSWORD, edtPassword.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){

                                    if(!task.getResult().isEmpty()){

                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        docId = documentSnapshot.getId();

                                        Intent intent = new Intent(LoginActivity.this, JobSeekerActivity.class);
                                        intent.putExtra("DOC_ID", docId);
                                        startActivity(intent);
                                    }

                                    else{

                                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }
}
