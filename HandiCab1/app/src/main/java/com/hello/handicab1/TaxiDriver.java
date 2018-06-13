package com.hello.handicab1;

public class TaxiDriver {
    String driverAvailable;
    Double driverLatitude;
    Double driverLongitude;
    String driverName;
    String driverPhone;

    public  TaxiDriver(){

    }

    public TaxiDriver(String driverAvailable, Double driverLatitude, Double driverLongitude, String driverName, String driverPhone) {
        this.driverAvailable = driverAvailable;
        this.driverLatitude = driverLatitude;
        this.driverLongitude = driverLongitude;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
    }

    public String getDriverAvailable() {
        return driverAvailable;
    }

    public void setDriverAvailable(String driverAvailable) {
        this.driverAvailable = driverAvailable;
    }

    public Double getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(Double driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public Double getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(Double driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
}
