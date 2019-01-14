package com.lekura.lekura;

public class Game {
    private String name;
    private String platform;
    private int thumbnail;

    public Game(){}

    public Game(String name, String platform, int thumbnail){
        this.name = name;
        this.platform = platform;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
