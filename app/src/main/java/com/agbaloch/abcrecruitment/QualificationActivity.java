package com.agbaloch.abcrecruitment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agbaloch.abcrecruitment.Database.Schema.JobSeekerTable;
import com.agbaloch.abcrecruitment.Models.Education;
import com.agbaloch.abcrecruitment.Models.Experience;
import com.agbaloch.abcrecruitment.Models.JobSeeker;
import com.agbaloch.abcrecruitment.Models.Skill;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QualificationActivity extends AppCompatActivity {

    private Button btnExp, btnEdu, btnSkill;

    private Button btnFinish;

    private Spinner spinLevel;

    private EditText edtJobTitle, edtOrganisation, edtExpStart, edtExpEnd, edtResponse;

    private EditText edtTitle, edtInstitution, edtEduStart, edtEduEnd, edtGrade;

    private EditText edtSkill;

    private LinearLayout llExp, llEdu, llSkills;

    private DatePickerDialog.OnDateSetListener expStartListener, expEndListener;
    private DatePickerDialog.OnDateSetListener eduStartListener, eduEndListener;

    private Calendar calendar;

    private List<Experience> experienceList;
    private List<Education> educationList;
    private List<Skill> skillList;

    private  ArrayAdapter<CharSequence> adapter;

    private String docId;

    private JobSeeker jobSeeker;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);

        wireUIComponents();

        docId = getIntent().getStringExtra("DOC_ID");
        db = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();

        experienceList = new ArrayList<>();
        educationList = new ArrayList<>();
        skillList = new ArrayList<>();

        //setting listener for exp start date
        expStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(i, i1, i2);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                edtExpStart.setText(sdf.format(calendar.getTime()));
            }
        };

        //setting listener for exp end date
        expEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(i, i1, i2);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                edtExpEnd.setText(sdf.format(calendar.getTime()));
            }
        };

        //setting listener for edu start date
        eduStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(i, i1, i2);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                edtEduStart.setText(sdf.format(calendar.getTime()));
            }
        };

        //Setting listener for edu end date
        eduEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(i, i1, i2);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                edtEduEnd.setText(sdf.format(calendar.getTime()));
            }
        };

        //setting adapter to education level spinner
        adapter = ArrayAdapter.createFromResource(
                this, R.array.levels_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLevel.setAdapter(adapter);

    }

    private void wireUIComponents(){

        btnExp = findViewById(R.id.btn_addExp);
        btnEdu = findViewById(R.id.btn_addEdu);
        btnSkill = findViewById(R.id.btn_addSkill);

        btnFinish = findViewById(R.id.btn_finish);

        llExp = findViewById(R.id.linearL_experience);
        llEdu = findViewById(R.id.linearL_education);
        llSkills = findViewById(R.id.linearL_skills);

        edtExpEnd = findViewById(R.id.edt_expEnd);
        edtExpStart = findViewById(R.id.edt_expStart);
        edtJobTitle = findViewById(R.id.edt_jobTitle);
        edtOrganisation = findViewById(R.id.edt_organisation);
        edtResponse = findViewById(R.id.edt_response);

        edtTitle = findViewById(R.id.edt_title);
        edtInstitution = findViewById(R.id.edt_institution);
        edtEduStart = findViewById(R.id.edt_eduStart);
        edtEduEnd = findViewById(R.id.edt_eduEnd);
        edtGrade = findViewById(R.id.edt_grade);
        spinLevel = findViewById(R.id.spin_level);

        edtSkill = findViewById(R.id.edt_skills);

        edtExpStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(QualificationActivity.this, expStartListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edtExpEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(QualificationActivity.this, expEndListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edtEduStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(QualificationActivity.this, eduStartListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edtEduEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(QualificationActivity.this, eduEndListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //setting listener for add experience button
        btnExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isExpDetailsValid()){

                    Experience experience = new Experience();
                    experience.setJobTitle(edtJobTitle.getText().toString());
                    experience.setCompany(edtOrganisation.getText().toString());
                    experience.setStartDate(edtExpStart.getText().toString());
                    experience.setEndDate(edtExpEnd.getText().toString());
                    experience.setResponsibilities(edtResponse.getText().toString());

                    experienceList.add(experience);

                    TextView textView = new TextView(QualificationActivity.this);
                    textView.setText(experience.getJobTitle());
                    textView.setTextColor(Color.GREEN);
                    textView.setTextSize(18);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    llExp.addView(textView, 0);

                    clearExpDetails();
                    calendar = Calendar.getInstance();
                }

            }
        });

        btnEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEduDetailsValid()){

                    Education education = new Education();
                    education.setTitle(edtTitle.getText().toString());
                    education.setInstitution(edtInstitution.getText().toString());
                    education.setStartDate(edtEduStart.getText().toString());
                    education.setEndDate(edtEduEnd.getText().toString());
                    education.setLevel(spinLevel.getSelectedItem().toString());
                    education.setGrade(edtGrade.getText().toString());

                    educationList.add(education);

                    TextView textView = new TextView(QualificationActivity.this);
                    textView.setText(education.getTitle());
                    textView.setTextColor(Color.GREEN);
                    textView.setTextSize(18);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    llEdu.addView(textView, 0);

                    clearEduDetails();
                }
            }
        });

        btnSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtSkill.getText().toString().length() < 3){

                    Toast.makeText(QualificationActivity.this, "Not a valid skill name", Toast.LENGTH_LONG).show();
                }
                else {

                    Skill skill = new Skill();
                    skill.setName(edtSkill.getText().toString());

                    skillList.add(skill);

                    TextView textView = new TextView(QualificationActivity.this);
                    textView.setText(skill.getName());
                    textView.setTextColor(Color.GREEN);
                    textView.setTextSize(18);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    llSkills.addView(textView, 0);

                    edtSkill.setText("");
                }
            }
        });

        spinLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(educationList.isEmpty()){

                    Toast.makeText(QualificationActivity.this, "Please add education details", Toast.LENGTH_SHORT).show();
                }

                else{

                    db.collection(JobSeekerTable.TABLE_NAME).document(docId)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    jobSeeker = documentSnapshot.toObject(JobSeeker.class);

                                    if(!educationList.isEmpty()){

                                        jobSeeker.setListEdu(educationList);
                                    }

                                    if(!experienceList.isEmpty()){

                                        jobSeeker.setListExp(experienceList);
                                    }

                                    if(!skillList.isEmpty()){

                                        jobSeeker.setListSkill(skillList);
                                    }

                                    db.collection(JobSeekerTable.TABLE_NAME).document(docId)
                                            .set(jobSeeker, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Toast.makeText(QualificationActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });
                }
            }
        });

    }

    private boolean isExpDetailsValid(){

        boolean isValid = true;

        if(edtJobTitle.getText().toString().length() < 3){

            isValid = false;
            Toast.makeText(this, "Invalid Job title", Toast.LENGTH_LONG).show();
        }

        else if(edtOrganisation.getText().toString().length() < 3){

            isValid = false;
            Toast.makeText(this, "Invalid organisation name", Toast.LENGTH_LONG).show();
        }

        else if(edtExpStart.getText().toString().isEmpty() || edtExpEnd.getText().toString().isEmpty()){

            isValid = false;
            Toast.makeText(this, "Please input start and end date of work!", Toast.LENGTH_LONG).show();
        }

        else if (edtResponse.getText().toString().length() < 10){

            isValid = false;
            Toast.makeText(this, "Responsibility details too short!", Toast.LENGTH_LONG).show();
        }



        return isValid;
    }

    private boolean isEduDetailsValid(){

        boolean isValid = true;

        if(edtTitle.getText().toString().length() < 3){

            isValid = false;
            Toast.makeText(this, "Invalid title", Toast.LENGTH_LONG).show();
        }

        else if(edtInstitution.getText().toString().length() < 3){

            isValid = false;
            Toast.makeText(this, "Invalid institution name", Toast.LENGTH_LONG).show();
        }

        else if(edtEduStart.getText().toString().isEmpty() || edtEduEnd.getText().toString().isEmpty()){

            isValid = false;
            Toast.makeText(this, "Please enter education start and end date", Toast.LENGTH_LONG).show();
        }

        else if(spinLevel.getSelectedItem().toString().equals("Level of education")){

            isValid = false;
            Toast.makeText(this, "Please select education level", Toast.LENGTH_LONG).show();
        }

        else if(edtGrade.getText().toString().isEmpty()){

            isValid = false;
            Toast.makeText(this, "Please input grade or cgpa", Toast.LENGTH_LONG).show();
        }

        return isValid;
    }

    private void clearExpDetails(){

        edtJobTitle.setText("");
        edtOrganisation.setText("");
        edtExpStart.setText("");
        edtExpEnd.setText("");
        edtResponse.setText("");
    }

    private void clearEduDetails() {

        edtTitle.setText("");
        edtInstitution.setText("");
        edtEduStart.setText("");
        edtEduEnd.setText("");
        spinLevel.setAdapter(adapter);
        edtGrade.setText("");
    }
}
