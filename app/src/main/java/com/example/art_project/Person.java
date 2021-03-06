package com.example.art_project;

public class Person {
    private int ID;          // Person ID
    private int Status;      // 0 - My Entry, 1 - Their Entry
    private String Name;     // Person's name

    Person(int status, String name) {
        this.Status = status;
        this.Name = name;
    }

    Person(int id, int status, String name) {
        this.ID = id;
        this.Status = status;
        this.Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
