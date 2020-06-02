package com.example.proyecto_final_android_2019_20.bbdd;

public class ConnectionData {
    private String url, db, passwd, user;

    private int port;

    public ConnectionData() {
        this.url= "ec2-18-132-41-207.eu-west-2.compute.amazonaws.com";
        this.db= "proyecto_odoo";
        this.passwd = "admin";
        this.user = "admin";
        this.port = 8069;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
