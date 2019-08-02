package com.example.change.foodorderserver.Model;

public class Category {
    private String Name;
    private String Desc;
    private String Image;

    public Category() {
    }

    public Category(String name, String desc, String image) {
        Name = name;
        Desc = desc;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
