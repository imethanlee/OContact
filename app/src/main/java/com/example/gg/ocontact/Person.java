package com.example.gg.ocontact;

import org.litepal.crud.DataSupport;

public class Person extends DataSupport {
    private String name;
    private int imageId;
    private String phoneNumber;
    private String Serial;

    public Person(String name, int imageId)
    {
        this.name=name;
        this.imageId=imageId;
        this.phoneNumber="10086";
        this.Serial="1";
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
