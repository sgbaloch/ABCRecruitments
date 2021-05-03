package com.agbaloch.abcrecruitment.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agbaloch.abcrecruitment.Models.JobSeeker;
import com.agbaloch.abcrecruitment.R;

import java.util.List;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.ViewHolder> {

    private List<JobSeeker> list;

    public ApplicantAdapter(List<JobSeeker> jobSeekers){

        list = jobSeekers;
    }
    @NonNull
    @Override
    public ApplicantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_applicant, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantAdapter.ViewHolder holder, int position) {

        JobSeeker jobSeeker  = list.get(position);

        holder.txtName.setText(jobSeeker.getfName() + " " + jobSeeker.getlName());
        holder.txtEdu.setText(jobSeeker.getListEdu().get(0).getTitle());
        holder.txtPref.setText("Preferred job: " + jobSeeker.getJobPreference());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtEdu, txtPref;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_name);
            txtEdu = itemView.findViewById(R.id.txt_edu);
            txtPref = itemView.findViewById(R.id.txt_prefer);
        }
    }
}
