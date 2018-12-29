package com.example.gg.ocontact;

import org.litepal.crud.LitePalSupport;
import android.widget.ImageView;

public class Person extends LitePalSupport {
    private int id;
    private String name;
    private int imageId;
    private String phoneNumber;
    private String Serial;
    private String imagePath;
    private ImageView image;

    public Person(int id, String name,String imagePath, String phoneNumber)
    {
        //this.image=image;
        this.id = id;
        this.name = name;
        //this.imageId = imageId;
        this.imagePath=imagePath;
        this.phoneNumber = phoneNumber;
        this.Serial="1";
    }

    public int getId() {
        return id;
    }

    public ImageView getImage()
    {
        return image;
    }
    public String getName()
    {
        return name;
    }

    public String getImagePath()
    {
        return imagePath;
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
