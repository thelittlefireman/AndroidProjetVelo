package com.ppp.esir.projetvelo.models;

/**
 * Created by thoma on 18/05/2016.
 */
public class User {
    private final String mLogin;
    private final String mEmail;
    private final String mPassword;
    private final String mNom;
    private final String mPrenom;
    private final String mAge;

    public User(String mLogin, String password, String nom, String mPrenom, String age, String email) {
        this.mLogin = mLogin;
        this.mPassword = password;
        this.mAge = age;
        this.mNom = nom;
        this.mPrenom = mPrenom;
        this.mEmail = email;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmLogin() {
        return mLogin;
    }

    public String getmPassword() {
        return mPassword;
    }

    public String getmNom() {
        return mNom;
    }

    public String getmPrenom() {
        return mPrenom;
    }

    public String getmAge() {
        return mAge;
    }
}
