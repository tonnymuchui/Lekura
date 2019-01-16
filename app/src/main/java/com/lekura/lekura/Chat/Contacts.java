package com.lekura.lekura.Chat;

public class Contacts {
    public String name, Status, image;

    public Contacts(){

    }
    public Contacts(String name, String Status, String image) {
        this.name = name;
        Status = Status;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        Status = Status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
