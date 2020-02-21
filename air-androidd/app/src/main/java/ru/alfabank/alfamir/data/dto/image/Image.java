package ru.alfabank.alfamir.data.dto.image;

/**
 * Created by U_M0WY5 on 19.02.2018.
 */

public class Image {

    private String base64;
    private String url;
    private int weight;
    private int height;
    private boolean isNew = true;

    public Image(String base64, String url, int weight, int height){
        this.base64 = base64;
        this.url = url;
        this.weight = weight;
        this.height = height;
    }


    public String getBinaryImage() {
        return base64;
    }

    public void setBinaryImage(String base64) {
        this.base64 = base64;
    }

    public String getUrl() {
        return url;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public boolean isNew() {
        return isNew;
    }

    public void markViewed() {
        isNew = false;
    }
}
