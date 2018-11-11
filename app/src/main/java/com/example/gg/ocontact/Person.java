package com.example.gg.ocontact;

public class Person {
    private String name;
    private int imageId;

    public Person(String name, int imageId)
    {
        this.name=name;
        this.imageId=imageId;
    }

    public String getName()
    {
        return name;
    }

    public int getImageId()
    {
        return imageId;
    }
}
