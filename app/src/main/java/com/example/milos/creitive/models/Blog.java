package com.example.milos.creitive.models;

/**
 * Created by milos on 04/02/2018.
 */

public class Blog {

    private long id;

    private String title;

    private String image_url;

    private String description;

    public Blog(long id, String title, String image_url, String description) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
