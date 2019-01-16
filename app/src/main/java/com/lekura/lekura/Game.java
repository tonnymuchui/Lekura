
package com.lekura.lekura;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private Integer id;
    private String name;
    private String description;
    private String download;
    private String operatingSystem;
    private String cPU;
    private String rAM;
    private String hDDspace;
    private String language;
    private String screenshot1;
    private String screenshot2;
    private String screenshot3;
    private String gameCover;
    private String trailer;
    private String postDate;
    private String payment;
    private Integer developer;
    private List<Integer> categories = null;
    private List<Integer> platforms = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Game(String name, String description, String download, String gameCover){
        this.name = name;
        this.description = description;
        this.download = download;
        this.gameCover = gameCover;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getCPU() {
        return cPU;
    }

    public void setCPU(String cPU) {
        this.cPU = cPU;
    }

    public String getRAM() {
        return rAM;
    }

    public void setRAM(String rAM) {
        this.rAM = rAM;
    }

    public String getHDDspace() {
        return hDDspace;
    }

    public void setHDDspace(String hDDspace) {
        this.hDDspace = hDDspace;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreenshot1() {
        return screenshot1;
    }

    public void setScreenshot1(String screenshot1) {
        this.screenshot1 = screenshot1;
    }

    public String getScreenshot2() {
        return screenshot2;
    }

    public void setScreenshot2(String screenshot2) {
        this.screenshot2 = screenshot2;
    }

    public String getScreenshot3() {
        return screenshot3;
    }

    public void setScreenshot3(String screenshot3) {
        this.screenshot3 = screenshot3;
    }

    public String getGameCover() {
        return "https://unity-donald.herokuapp.com" +gameCover;
    }

    public void setGameCover(String gameCover) {
        this.gameCover = gameCover;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Integer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Integer developer) {
        this.developer = developer;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Integer> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Integer> platforms) {
        this.platforms = platforms;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}