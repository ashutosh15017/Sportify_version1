package com.example.ishmeetkaur.sportify_version1;

/**
 * Created by dell on 4/8/2018.
 */

public class Post {
    private String sport;
    private String type;
    private String byWhom;
    private String Info;
    private String isRegistrationAllowed;
    private String postKey;

    public Post(String sport, String type, String byWhom, String info, String isRegistrationAllowed) {
        this.sport = sport;
        this.type = type;
        this.byWhom = byWhom;
        Info = info;
        this.isRegistrationAllowed = isRegistrationAllowed;
    }

    public Post() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getByWhom() {
        return byWhom;
    }

    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getIsRegistrationAllowed() {
        return isRegistrationAllowed;
    }

    public void setIsRegistrationAllowed(String isRegistrationAllowed) {
        this.isRegistrationAllowed = isRegistrationAllowed;
    }
}
