package com.jbm.biteburgerv2.data;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String uid;
    private String name;
    private String surname;
    private int points;
    private Address direc;
    private Date fechaNac;
    private int numTelef;
    private String email;
    private boolean communications;
    private int shortId;

    public User() {}

    public User(String name, String surname, Date fechaNac, int numTelef, String email, boolean communications) {
        this.name = name;
        this.surname = surname;
        this.fechaNac = fechaNac;
        this.numTelef = numTelef;
        this.email = email;
        this.communications = communications;
    }

    public User(String name, String surname, int points, Date fechaNac, int numTelef, String email) {
        this.name = name;
        this.surname = surname;
        this.points = points;
        this.fechaNac = fechaNac;
        this.numTelef = numTelef;
        this.email = email;
    }

    public User(String uid, String name, String surname, int points, Date fechaNac, int numTelef, String email) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.points = points;
        this.fechaNac = fechaNac;
        this.numTelef = numTelef;
        this.email = email;
    }

    public User(String name, String surname, int points, Date fechaNac, int numTelef, String email, boolean communications) {
        this.name = name;
        this.surname = surname;
        this.points = points;
        this.fechaNac = fechaNac;
        this.numTelef = numTelef;
        this.email = email;
        this.communications = communications;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Address getDirec() {
        return direc;
    }

    public void setDirec(Address direc) {
        this.direc = direc;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public int getNumTelef() {
        return numTelef;
    }

    public void setNumTelef(int numTelef) {
        this.numTelef = numTelef;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCommunications() {
        return communications;
    }

    public void setCommunications(boolean communications) {
        this.communications = communications;
    }

    public int getShortId() {
        return shortId;
    }

    public void setShortId(int shortId) {
        this.shortId = shortId;
    }


    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", points=" + points +
                ", fechaNac=" + fechaNac +
                ", numTelef=" + numTelef +
                ", email='" + email + '\'' +
                '}';
    }
}
