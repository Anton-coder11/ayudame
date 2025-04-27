package org.example.Dinosaurus_Database;

public class Dino{
    int id;
    String type;
    int h;
    boolean status;
    String location;
    public Dino(String type, int h, boolean status, String location) {
        this.type = type;
        this.h = h;
        this.status = status;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public int getH() {
        return h;
    }

    public boolean isStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }
}
