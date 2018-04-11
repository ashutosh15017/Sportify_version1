package com.example.ishmeetkaur.sportify_version1;

import java.io.Serializable;

/**
 * Created by dell on 4/8/2018.
 */

public class Coord implements Serializable{
    private String Coordname;
    private String Coordemail;
    private String CoordNumber;
    private String CoordSport;
    private String CoordGender;

    public Coord(String coordname, String coordemail, String coordNumber, String coordSport, String coordGender) {
        Coordname = coordname;
        Coordemail = coordemail;
        CoordNumber = coordNumber;
        CoordSport = coordSport;
        CoordGender = coordGender;
    }

    public Coord() {
    }

    public String getCoordname() {
        return Coordname;
    }

    public void setCoordname(String coordname) {
        Coordname = coordname;
    }

    public String getCoordemail() {
        return Coordemail;
    }

    public void setCoordemail(String coordemail) {
        Coordemail = coordemail;
    }

    public String getCoordNumber() {
        return CoordNumber;
    }

    public void setCoordNumber(String coordNumber) {
        CoordNumber = coordNumber;
    }

    public String getCoordSport() {
        return CoordSport;
    }

    public void setCoordSport(String coordSport) {
        CoordSport = coordSport;
    }

    public String getCoordGender() {
        return CoordGender;
    }

    public void setCoordGender(String coordGender) {
        CoordGender = coordGender;
    }
}
