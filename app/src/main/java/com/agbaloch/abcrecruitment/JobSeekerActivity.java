package com.agbaloch.abcrecruitment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agbaloch.abcrecruitment.Database.Schema.JobSeekerTable;
import com.agbaloch.abcrecruitment.Models.Education;
import com.agbaloch.abcrecruitment.Models.Experience;
import com.agbaloch.abcrecruitment.Models.JobSeeker;
import com.agbaloch.abcrecruitment.Models.Skill;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Table;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class JobSeekerActivity extends AppCompatActivity {

    private Button btnGenerateCV;
    private TextView txtLocation, txtName;

    private JobSeeker jobSeeker;

    private String docId;

    private FirebaseFirestore db;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker);

        docId = getIntent().getStringExtra("DOC_ID");

        db = FirebaseFirestore.getInstance();

        db.collection(JobSeekerTable.TABLE_NAME).document(docId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        jobSeeker = documentSnapshot.toObject(JobSeeker.class);
                        txtName.setText(jobSeeker.getfName());
                    }
                });

        btnGenerateCV = findViewById(R.id.btn_generateCV);
        txtLocation = findViewById(R.id.txt_directory);
        txtName = findViewById(R.id.txt_user);

        btnGenerateCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()){

                    try {

                        generateCV();
                        txtLocation.setText("PDF file " + jobSeeker.getfName()
                                + ".pdf saved to folder ABCRecruitments in external storage" );
                        txtLocation.setVisibility(View.VISIBLE);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                else{

                    requestPermission();
                }
            }
        });

    }

    private void generateCV() throws FileNotFoundException, DocumentException {

        String FILE = Environment.getExternalStorageDirectory().toString()
                +"/ABCRecruitments/"+ jobSeeker.getfName() + ".pdf";

        Document document = new Document(PageSize.A4);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ABCRecruitments");
        myDir.mkdirs();

        PdfWriter.getInstance(document, new FileOutputStream(FILE));

        document.open();

        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("ABC Recruiters");
        document.addCreator("Sarfaraz");

        addTitlePage(document);

        document.close();

    }

    public void addTitlePage(Document document) throws DocumentException
    {

        Font fontName = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
        Font fontPara = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL);

        //print title
        String title = jobSeeker.getfName() + " " + jobSeeker.getlName();
        Paragraph paraTitle = new Paragraph();
        paraTitle.setFont(fontName);
        paraTitle.add(title);
        document.add(paraTitle);

        //add address, contact, email, dob
        String addressText = jobSeeker.getAddress() + "\n" + jobSeeker.getContact()
                + "\n" + jobSeeker.getEmail() + "\nDate of birth: " + jobSeeker.getDob() + "\n\n\n";
        Paragraph paraAddress = new Paragraph();
        paraAddress.setFont(fontPara);
        paraAddress.add(addressText);
        document.add(paraAddress);

        //add career objective
        addHeading(document, "Career objective");
        String objective = "Secure a responsible career opportunity to fully utilize my training and skills, while making a significant contribution to the success of the company.\n\n";
        Paragraph paraObjective = new Paragraph();
        paraObjective.setFont(fontPara);
        paraObjective.add(objective);
        document.add(paraObjective);

        //add experience
        addHeading(document, "Experience");
        addExperience(document, jobSeeker.getListExp());

        //add Education
        addHeading(document, "\n\nEducation");
        addEducation(document, jobSeeker.getListEdu());

        //add Skills
        addHeading(document, "\n\nSkills");
        addSkills(document, jobSeeker.getListSkill());

        document.newPage();

    }

    private void addHeading(Document document, String heading) throws DocumentException {

        Font fontHeading = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Paragraph paraHeading = new Paragraph();
        paraHeading.setFont(fontHeading);
        paraHeading.add(heading + "\n\n");
        document.add(paraHeading);

    }

    private void addExperience(Document document, List<Experience> experienceList) throws DocumentException {

        Font fontPara = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        float[] columnWidths = {80f, 250f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100f);

        for (Experience exp: experienceList) {

            //cell 1
            Paragraph paraDate = new Paragraph();
            paraDate.setFont(fontPara);
            paraDate.add(exp.getStartDate() + " - " + exp.getEndDate());
            PdfPCell cellDate = new PdfPCell();
            cellDate.setBorder(Rectangle.NO_BORDER);
            cellDate.addElement(paraDate);
            table.addCell(cellDate);

            //cell 2
            Paragraph paraExp = new Paragraph();
            paraExp.setFont(fontPara);
            paraExp.add(exp.getJobTitle() + "\n" + exp.getCompany() + "\n" + exp.getResponsibilities());
            PdfPCell cellExp = new PdfPCell();
            cellExp.setBorder(Rectangle.NO_BORDER);
            cellExp.addElement(paraExp);
            table.addCell(cellExp);
        }

        document.add(table);
    }

    private void addEducation(Document document, List<Education> educationList) throws DocumentException {

        Font fontPara = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font fontHeading = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

        float[] columnWidths = {25f, 60f, 15f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100f);

        Paragraph paraHDate = new Paragraph();
        paraHDate.setFont(fontHeading);
        paraHDate.add("Date");
        PdfPCell cellDate = new PdfPCell();
        cellDate.setBorder(Rectangle.NO_BORDER);
        cellDate.addElement(paraHDate);
        table.addCell(cellDate);

        Paragraph paraHInstitute = new Paragraph();
        paraHInstitute.setFont(fontHeading);
        paraHInstitute.add("Title/Institution");
        PdfPCell cellInstitute = new PdfPCell();
        cellInstitute.setBorder(Rectangle.NO_BORDER);
        cellInstitute.addElement(paraHInstitute);
        table.addCell(cellInstitute);

        Paragraph paraHGrade = new Paragraph();
        paraHGrade.setFont(fontHeading);
        paraHGrade.add("Grade");
        PdfPCell cellGrad = new PdfPCell();
        cellGrad.setBorder(Rectangle.NO_BORDER);
        cellGrad.addElement(paraHGrade);
        table.addCell(cellGrad);

        for (Education edu: educationList) {

            Paragraph paraDate = new Paragraph();
            paraDate.setFont(fontPara);
            paraDate.add(edu.getStartDate() + " - " + edu.getEndDate());
            PdfPCell cellDateV = new PdfPCell();
            cellDateV.setBorder(Rectangle.NO_BORDER);
            cellDateV.addElement(paraDate);
            table.addCell(cellDateV);

            Paragraph paraInstitute = new Paragraph();
            paraInstitute.setFont(fontPara);
            paraInstitute.add(edu.getTitle() + "\n" + edu.getInstitution());
            PdfPCell cellInstituteV = new PdfPCell();
            cellInstituteV.setBorder(Rectangle.NO_BORDER);
            cellInstituteV.addElement(paraInstitute);
            table.addCell(cellInstituteV);

            Paragraph paraGrade = new Paragraph();
            paraGrade.setFont(fontPara);
            paraGrade.add(edu.getGrade());
            PdfPCell cellGradV = new PdfPCell();
            cellGradV.setBorder(Rectangle.NO_BORDER);
            cellGradV.addElement(paraGrade);
            table.addCell(cellGradV);
        }

        document.add(table);
    }

    private void addSkills(Document document, List<Skill> skillList) throws DocumentException {

        com.itextpdf.text.List list = new com.itextpdf.text.List();

        for (Skill skill: skillList) {

            list.add(skill.getName());
        }

        document.add(list);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(JobSeekerActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(JobSeekerActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(JobSeekerActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(JobSeekerActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }
}
