package com.example.ishmeetkaur.sportify_version1;

/**
 * Created by shubzz on 13/4/18.
 */

public class DayAttendance
{
    private int present;
    private int absent;
    private int medical;

    public DayAttendance(int p,int a, int m)
    {
        present = p;
        absent = a;
        medical = m;
    }

    public int getPresent()
    {
        return present;
    }

    public int getAbsent()
    {
        return absent;
    }

    public int getMedical()
    {
        return medical;
    }

    public void setValue(int k)
    {
        if(k == 1)
        {
            present++;
        }
        else if(k == 0)
        {
            absent++;
        }
        else if(k == 2)
        {
            medical++;
        }
    }
}
