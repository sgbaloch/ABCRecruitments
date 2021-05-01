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

import com.agbaloch.abcrecruitment.Models.Education;
import com.agbaloch.abcrecruitment.Models.Experience;
import com.agbaloch.abcrecruitment.Models.Skill;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QualificationActivity extends AppCompatActivity {

    private Button btnExp, btnEdu, btnSkill;

    private Spinner spinLevel;

    private EditText edtJobTitle, edtOrganisation, edtExpStart, edtExpEnd, edtResponse;

    private LinearLayout llExp, llEdu, llSkills;

    private DatePickerDialog.OnDateSetListener expStartListener, expEndListener;

    private Calendar calendar;

    private List<Experience> experienceList;
    private List<Education> educationList;
    private List<Skill> skillList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);

        wireUIComponents();

        calendar = Calendar.getInstance();

        experienceList = new ArrayList<>();
        educationList = new ArrayList<>();
        skillList = new ArrayList<>();

        //setting listener for exp start date
        expStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(i, i1, i2);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                edtExpStart.setText(sdf.format(calendar.getTime()));
            }
        };

        //setting listener for exp end date
        expEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                calendar.set(i, i1, i2);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                edtExpEnd.setText(sdf.format(calendar.getTime()));
            }
        };

        //setting adapter to education level spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.levels_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLevel.setAdapter(adapter);

    }

    private void wireUIComponents(){

        btnExp = findViewById(R.id.btn_addExp);
        llExp = findViewById(R.id.linearL_experience);
        spinLevel = findViewById(R.id.spin_level);

        edtExpEnd = findViewById(R.id.edt_expEnd);
        edtExpStart = findViewById(R.id.edt_expStart);
        edtJobTitle = findViewById(R.id.edt_jobTitle);
        edtOrganisation = findViewById(R.id.edt_organisation);
        edtResponse = findViewById(R.id.edt_response);

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
                    textView.setTextColor(Color.BLUE);
                    textView.setTextSize(20);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    llExp.addView(textView, 0);
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
}
