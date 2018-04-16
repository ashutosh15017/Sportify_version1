package com.example.ishmeetkaur.sportify_version1;

import java.util.ArrayList;

/**
 * Created by shubzz on 11/4/18.
 */

public class TeamMemberAttendance
{
    private int count;
    private String name;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    public ArrayList <Integer> attendanceArray = new ArrayList<Integer>();

    public TeamMemberAttendance()
    {

    }

    public TeamMemberAttendance(int C, String N, int M, int T, int W, int Thurs, int F)
    {
        count = C;
        name = N;
        monday = M;
        tuesday = T;
        wednesday = W;
        thursday = Thurs;
        friday = F;

    }

    public String getName()
    {
        return name;
    }

    public int getMonday()
    {
        return monday;
    }

    public int getTuesday()
    {
        return tuesday;
    }

    public int getWednesday()
    {
        return wednesday;
    }

    public int getThursday()
    {
        return thursday;
    }

    public int getFriday()
    {
        return friday;
    }

    public int getCount()
    {
        return count;
    }

    public void makeArray()
    {
        attendanceArray.add(monday);
        attendanceArray.add(tuesday);
        attendanceArray.add(wednesday);
        attendanceArray.add(thursday);
        attendanceArray.add(friday);
    }


}
