package com.droidverine.ellipsis.ellipsishealthcompanion.Models;

public class UserDetails {
    public UserDetails() {
    }
    String username,height,weight,contactno;

    public UserDetails(String username, String height, String weight, String contactno) {
        this.username = username;
        this.height = height;
        this.weight = weight;
        this.contactno = contactno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }
}
