package com.shellyAmbar.ambar.workoutplanner.Models;

public class ModelCategory {
    private String imageSRC;
    private String imageBack;
    private String imageTitle;
    private String keyId;

    public ModelCategory(String imageSRC, String imageBack, String imageTitle, String keyId) {
        this.imageSRC = imageSRC;
        this.imageBack = imageBack;
        this.imageTitle = imageTitle;
        this.keyId = keyId;
    }

    public ModelCategory() {
    }

    public String getImageSRC() {
        return imageSRC;
    }

    public void setImageSRC(String imageSRC) {
        this.imageSRC = imageSRC;
    }

    public String getImageBack() {
        return imageBack;
    }

    public void setImageBack(String imageBack) {
        this.imageBack = imageBack;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
