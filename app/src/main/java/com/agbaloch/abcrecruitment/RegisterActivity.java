package com.agbaloch.abcrecruitment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agbaloch.abcrecruitment.Database.Schema.JobSeekerTable;
import com.agbaloch.abcrecruitment.Models.JobSeeker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Button btnCreateAccount;

    @NotEmpty()
    private EditText edtFName;
    @NotEmpty
    private EditText edtLName;
    @NotEmpty
    @Email
    private EditText edtEmail;
    @NotEmpty
    @Password(min = 8)
    private EditText edtPassword;

    @ConfirmPassword
    private EditText edtCPassword;

    private Validator validator;
    private JobSeeker jobSeeker;
    private FirebaseFirestore db;
    private boolean isRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        wireUIComponents();
        validator = new Validator(this);
        db = FirebaseFirestore.getInstance();

        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {

                addUserIfNotAlreadyRegistered(edtEmail.getText().toString());
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {

                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(RegisterActivity.this);

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void addJobSeeker() {
        jobSeeker = new JobSeeker();
        jobSeeker.setfName(edtFName.getText().toString());
        jobSeeker.setlName(edtLName.getText().toString());
        jobSeeker.setEmail(edtEmail.getText().toString());
        jobSeeker.setPassword(edtPassword.getText().toString());

        db.collection(JobSeekerTable.TABLE_NAME)
                .add(jobSeeker)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void wireUIComponents(){

        edtEmail = findViewById(R.id.edt_email);
        edtFName = findViewById(R.id.edt_fName);
        edtLName = findViewById(R.id.edt_lName);
        edtPassword = findViewById(R.id.edt_pass);
        edtCPassword = findViewById(R.id.edt_cPass);
        btnCreateAccount = findViewById(R.id.btn_createAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validator.validate();
            }
        });
    }

    private void addUserIfNotAlreadyRegistered(String email){


        db.collection(JobSeekerTable.TABLE_NAME)
                .whereEqualTo(JobSeekerTable.EMAIL, email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.getResult().size() == 0){

                            addJobSeeker();
                        }
                        else{

                            Toast.makeText(RegisterActivity.this, "Email already exist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        
    }
}
