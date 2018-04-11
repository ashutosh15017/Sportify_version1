package com.example.ishmeetkaur.sportify_version1;

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

    public int getCount()
    {
        return Count;
    }
}
