package com.example.ishmeetkaur.sportify_version1;

/**
 * Created by shubzz on 14/4/18.
 */

public class Suggestion_class
{
    private int type;
    private String text;

    public Suggestion_class()
    {

    }

    public Suggestion_class(int t, String c)
    {
        type = t;
        text = c;
    }

    public String getText()
    {

        return text;

    }

    public int getType()
    {
        return  type;
    }
}
