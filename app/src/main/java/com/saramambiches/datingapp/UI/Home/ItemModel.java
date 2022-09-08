package com.saramambiches.datingapp.UI.Home;

import java.io.Serializable;

public class ItemModel implements Serializable {
    private String userId,image, name, age, university, description;

    public ItemModel() {
    }

    public ItemModel(String userId, String image, String name, String age, String university, String description) {
        this.image = image;
        this.name = name;
        this.age = age;
        this.university = university;
        this.userId = userId;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
