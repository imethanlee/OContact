package com.example.gg.ocontact;

import org.litepal.crud.LitePalSupport;

public class Person extends LitePalSupport {
    private int id;
    private String name;
    private int imageId;
    private String phoneNumber;
    private String Serial;

    public Person(int id, String name, int imageId)
    {
        this.id = id;
        this.name=name;
        this.imageId=imageId;
        this.phoneNumber="10086";
        this.Serial="1";
    }

    public int getId() {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getImageId()
    {
        return imageId;
    }
    public String getSerial()
    {
        return Serial;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }
}
