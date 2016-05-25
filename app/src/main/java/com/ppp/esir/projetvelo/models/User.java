package com.ppp.esir.projetvelo.models;

/**
 * Created by thoma on 18/05/2016.
 */
public class User {
    private String mLogin;
    private String mEmail;
    private String mPassword;
    private String mNom;
    private String mAdresse;
    private String mPrenom;
    private String mAge;

    public User(String mLogin, String password, String nom, String mPrenom, String age, String email) {
        this.mLogin = mLogin;
        this.mPassword = password;
        this.mAge = age;
        this.mNom = nom;
        this.mPrenom = mPrenom;
        this.mEmail = email;
    }

    public User() {
        this.mLogin = "";
        this.mPassword = "";
        this.mAge = "";
        this.mNom = "";
        this.mPrenom = "";
        this.mEmail = "";
    }

    public String getmAdresse() {
        return mAdresse;
    }

    public void setmAdresse(String mAdresse) {
        this.mAdresse = mAdresse;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmLogin() {
        return mLogin;
    }

    public void setmLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmNom() {
        return mNom;
    }

    public void setmNom(String mNom) {
        this.mNom = mNom;
    }

    public String getmPrenom() {
        return mPrenom;
    }

    public void setmPrenom(String mPrenom) {
        this.mPrenom = mPrenom;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getNomPrenom() {
        return getmNom() + " " + getmPrenom();
    }
}
