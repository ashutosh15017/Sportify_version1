package com.example.ishmeetkaur.sportify_version1;

import java.util.ArrayList;

/**
 * Created by shubzz on 11/4/18.
 */

public class TeamMemberAttendance
{
    private int Count;
    private String Name;
    private int Monday;
    private int Tuesday;
    private int Wednesday;
    private int Thursday;
    private int Friday;
    public ArrayList <Integer> attendanceArray = new ArrayList<Integer>();

    public TeamMemberAttendance()
    {

    }

    public TeamMemberAttendance(int C, String N, int M, int T, int W, int Thurs, int F)
    {
        Count = C;
        Name = N;
        Monday = M;
        Tuesday = T;
        Wednesday = W;
        Thursday = Thurs;
        Friday = F;

    }

    public String getName()
    {
        return Name;
    }

    public int getMonday()
    {
        return Monday;
    }

    public int getTuesday()
    {
        return Tuesday;
    }

    public int getWednesday()
    {
        return Wednesday;
    }

    public int getThursday()
    {
        return Thursday;
    }

    public int getFriday()
    {
        return Friday;
    }

    public int getCount()
    {
        return Count;
    }

    public void makeArray()
    {
        attendanceArray.add(Monday);
        attendanceArray.add(Tuesday);
        attendanceArray.add(Wednesday);
        attendanceArray.add(Thursday);
        attendanceArray.add(Friday);
    }


}
