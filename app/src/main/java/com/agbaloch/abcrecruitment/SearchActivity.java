package com.agbaloch.abcrecruitment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agbaloch.abcrecruitment.Adapters.ApplicantAdapter;
import com.agbaloch.abcrecruitment.Database.Schema.JobSeekerTable;
import com.agbaloch.abcrecruitment.Models.Education;
import com.agbaloch.abcrecruitment.Models.JobSeeker;
import com.agbaloch.abcrecruitment.Models.Skill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText edtSearch;

    private TextView txtResult;

    private Spinner spinCriteria;

    private Button btnSearch;

    private  ArrayAdapter<CharSequence> adapter;

    private FirebaseFirestore db;

    private List<JobSeeker> jobSeekerList;

    Query query;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter rAdapter;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        wireUIComponents();

        db = FirebaseFirestore.getInstance();

        adapter = ArrayAdapter.createFromResource(
                this, R.array.criteria_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinCriteria.setAdapter(adapter);

        spinCriteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobSeekerList = new ArrayList<>();

    }

    private void wireUIComponents() {

        edtSearch = findViewById(R.id.edt_search);

        spinCriteria = findViewById(R.id.spin_criteria);

        btnSearch = findViewById(R.id.btn_search);

        linearLayout = findViewById(R.id.ll_search);

        recyclerView = findViewById(R.id.recycle_search);

        txtResult = findViewById(R.id.txt_result);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String search = edtSearch.getText().toString();

                if(search.isEmpty()){

                    Toast.makeText(SearchActivity.this, "Please type in search bar", Toast.LENGTH_LONG).show();
                }

                else{

                    switch (spinCriteria.getSelectedItemPosition()){

                        case 0:
                            query = db.collection(JobSeekerTable.TABLE_NAME)
                                    .whereEqualTo(JobSeekerTable.JOB_PREFRENCE, search);
                            break;

                        case 1:
                            query = db.collection(JobSeekerTable.TABLE_NAME)
                                    .whereArrayContains("listEdu", search);
                            break;

                        case 2:
                            query = db.collection(JobSeekerTable.TABLE_NAME)
                                    .whereArrayContains("listExp", search);
                            break;

                        case 3:
                            Education education = new Education();
                            education.setTitle(search);
                            query = db.collection(JobSeekerTable.TABLE_NAME)
                                    .whereEqualTo("listEdu.level", search);
                            break;

                        case 4:
                            Skill skill = new Skill();
                            skill.setName(search);
                            query = db.collection(JobSeekerTable.TABLE_NAME)
                                    .whereArrayContains("listSkill", skill);

                            break;
                    }

                    showResult();
                }

            }
        });
    }

    private void showResult() {
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            if(!task.getResult().isEmpty()){

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    JobSeeker jobSeeker = document.toObject(JobSeeker.class);
                                    jobSeekerList.add(jobSeeker);
                                }

                                linearLayout.setVisibility(View.GONE);

                                rAdapter = new ApplicantAdapter(jobSeekerList);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(rAdapter);
                            }

                            else{

                                txtResult.setVisibility(View.VISIBLE);

                            }


                        }

                        else {

                            txtResult.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }
}
