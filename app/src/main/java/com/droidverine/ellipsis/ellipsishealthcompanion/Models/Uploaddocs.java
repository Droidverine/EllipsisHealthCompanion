package com.droidverine.ellipsis.ellipsishealthcompanion.Models;

public class Uploaddocs {
  //  private String mName;
   // private String mImageUrl;
    private String name;
    private String imgurl;

    public Uploaddocs(String name, String imgurl) {
        this.name = name;
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Uploaddocs() {
        //empty constructor needed
    }

   /* public Uploaddocs(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    */
}