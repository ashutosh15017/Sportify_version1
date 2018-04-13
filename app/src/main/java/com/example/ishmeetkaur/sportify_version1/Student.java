package com.example.ishmeetkaur.sportify_version1;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ishmeetkaur on 14/04/18.
 */

public class Student {
    private String name;
    private String email;
    private String phoneno;
    private String gender;
    private ArrayList<String> team;



    public Student(String name, String email, String phoneno, String gender, ArrayList<String> team) {
        this.name = name;
        this.email = email;
        this.phoneno = phoneno;
        this.gender = gender;
        this.team=team;


    }

    public Student () {
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getphoneno() {
        return phoneno;
    }

    public void setphoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getgender() {
        return gender;
    }

    public void setgender(String gender) {
        this.gender = gender;
    }

    public void setteam(ArrayList<String> team)
    {
        this.team = team;
    }
    public ArrayList<String> getteams()
    {
        return this.team;
    }

    @Override
    public String toString() {
        return name + "," + email + "," + phoneno + "," + gender;
    }
}
