package com.hello.handicab1;

public class MarkerItem {
    double latitude;
    double longitude;
    String mark_title;
    String mark_content;
    public MarkerItem(double latitude, double longitude, String title){
        this.latitude=latitude;
        this.longitude=longitude;
        this.mark_title=title;
    }
    public MarkerItem(double latitude, double longitude, String title, String content){
        this.latitude=latitude;
        this.longitude=longitude;
        this.mark_title=title;
        this.mark_content=content;
    }
    public void setLatitude_mark(double latitude){
        this.latitude=latitude;
    }
    public double getLatitude_mark(){
        return latitude;
    }
    public void setLongitude_mark(double Longitude){
        this.longitude=Longitude;
    }
    public double getLongitude_mark(){
        return longitude;
    }
    public void setMark_title(String title){
        this.mark_title=title;
    }
    public String getMark_title(){
        return mark_title;
    }
    public void setMark_content(String content){
        this.mark_content=content;
    }
    public String getMark_content(){
        return mark_content;
    }
}