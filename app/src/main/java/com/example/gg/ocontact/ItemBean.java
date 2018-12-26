package com.example.gg.ocontact;

import android.graphics.Bitmap;

public class ItemBean {
    private String text;
    private String hint;
    private Bitmap bitmap;
    private boolean exitMessage = false;

    public String getText() {
        return text;
    }
    public String getHint() {
        return hint;
    }
    public Bitmap getBitmap() {return bitmap;}
    public Boolean getExitMessage() { return exitMessage; }

    public void setText(String text)
    {
        this.text = text;
    }
    public void setHint(String text)
    {
        this.hint = text;
    }

    public void setBitmap(Bitmap bitmap) {
        if(this.bitmap==null||bitmap!=null)
            this.bitmap=bitmap;
    }
    public void setExitMessage(Boolean exitMessage)
    {
        this.exitMessage = exitMessage;
    }
}
