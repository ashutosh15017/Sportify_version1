package com.example.ishmeetkaur.sportify_version1;

/**
 * Created by shubzz on 14/4/18.
 */

public class Suggestion_class
{
    private int Type;
    private String Text;

    public Suggestion_class()
    {

    }

    public Suggestion_class(int t, String c)
    {
        Type = t;
        Text = c;
    }

    public String getText()
    {

        return Text;

    }

    public int getType()
    {
        return  Type;
    }
}
