package com.agbaloch.abcrecruitment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.agbaloch.abcrecruitment.Adapters.ApplicantAdapter;
import com.agbaloch.abcrecruitment.Database.Schema.JobSeekerTable;
import com.agbaloch.abcrecruitment.Models.JobSeeker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<JobSeeker> jobSeekerList;

    private TextView txtSearch;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycle_applicants);
        txtSearch = findViewById(R.id.txt_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        jobSeekerList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection(JobSeekerTable.TABLE_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                JobSeeker jobSeeker = document.toObject(JobSeeker.class);
                                jobSeekerList.add(jobSeeker);
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            mAdapter = new ApplicantAdapter(jobSeekerList);
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
