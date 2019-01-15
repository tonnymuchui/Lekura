package com.lekura.lekura.Chat;

public class Contacts {
    public String name, Status, image;

    public Contacts(){

    }
    public Contacts(String name, String status, String image) {
        this.name = name;
        Status = status;
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

    public void setStatus(String status) {
        Status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
