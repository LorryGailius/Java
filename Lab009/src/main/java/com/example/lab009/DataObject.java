package com.example.lab009;

public class DataObject {

    public String first_name;
    public String last_name;
    public String email;
    public String imageLink;
    public String ip;

    public DataObject(String first_name, String last_name, String email, String imageLink, String ip) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.imageLink = imageLink;
        this.ip = ip;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return first_name + ";" + last_name + ";" + email + ";" + imageLink + ";" + ip + "\n";
    }
}
