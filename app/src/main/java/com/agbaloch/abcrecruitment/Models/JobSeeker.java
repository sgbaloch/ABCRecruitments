package com.agbaloch.abcrecruitment.Models;

import java.util.List;

public class JobSeeker {

    private String fName, lName, email, password, address, city, gender
            , jobPreference,  dob, contact;

    private List<Education> listEdu;

    private List<Experience> listExp;

    private List<Skill> listSkill;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobPreference() {
        return jobPreference;
    }

    public void setJobPreference(String jobPreference) {
        this.jobPreference = jobPreference;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<Education> getListEdu() {
        return listEdu;
    }

    public void setListEdu(List<Education> listEdu) {
        this.listEdu = listEdu;
    }

    public List<Experience> getListExp() {
        return listExp;
    }

    public void setListExp(List<Experience> listExp) {
        this.listExp = listExp;
    }

    public List<Skill> getListSkill() {
        return listSkill;
    }

    public void setListSkill(List<Skill> listSkill) {
        this.listSkill = listSkill;
    }
}
