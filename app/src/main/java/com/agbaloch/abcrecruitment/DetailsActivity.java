package com.agbaloch.abcrecruitment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.agbaloch.abcrecruitment.Database.Schema.JobSeekerTable;
import com.agbaloch.abcrecruitment.Models.JobSeeker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    @NotEmpty
    private EditText edtDOB, edtContact, edtAddress, edtCity, edtPrefJob;

    private Button btnNext;

    private RadioGroup rGroupGender;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Calendar calendar;

    private Validator validator;

    private FirebaseFirestore db;

    private String gender, dob, address, city, prefJob;
    private String contact;
    private String docId;

    private JobSeeker jobSeeker;
    //private long dobInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        docId = getIntent().getStringExtra("DOC_ID");
        db = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();

        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {

                saveAndContinue();
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {

                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(DetailsActivity.this);

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(DetailsActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(i, i1, i2);
                updateDateOfBirth();
            }
        };

        wireUIComponents();
    }

    private void wireUIComponents(){

        edtDOB = findViewById(R.id.edt_dob);
        edtAddress = findViewById(R.id.edt_address);
        edtCity = findViewById(R.id.edt_city);
        edtContact = findViewById(R.id.edt_contact);
        edtPrefJob = findViewById(R.id.edt_prefJob);
        rGroupGender = findViewById(R.id.rdgp_gender);

        btnNext = findViewById(R.id.btn_next);

        edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(DetailsActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        rGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton selectedRBtn = findViewById(i);
                gender = selectedRBtn.getText().toString();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validator.validate();
            }
        });
    }

    private void updateDateOfBirth() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dob = sdf.format(calendar.getTime());
        edtDOB.setText(dob);
        //dobInMillis = calendar.getTimeInMillis();
    }

    private void saveAndContinue(){

        if(gender == null){

            gender = "Prefer not to say";
        }

        address = edtAddress.getText().toString();
        city = edtCity.getText().toString();
        contact = edtContact.getText().toString();
        prefJob = edtPrefJob.getText().toString();

        db.collection(JobSeekerTable.TABLE_NAME).document(docId)
        .get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                jobSeeker = documentSnapshot.toObject(JobSeeker.class);

                jobSeeker.setGender(gender);
                jobSeeker.setDob(dob);
                jobSeeker.setAddress(address);
                jobSeeker.setCity(city);
                jobSeeker.setContact(contact);
                jobSeeker.setJobPreference(prefJob);

                db.collection(JobSeekerTable.TABLE_NAME).document(docId)
                        .set(jobSeeker, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(DetailsActivity.this, "updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

}
