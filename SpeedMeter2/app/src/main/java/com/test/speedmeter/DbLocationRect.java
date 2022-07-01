package com.test.speedmeter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DbLocationRect {
    @PrimaryKey(autoGenerate = true) int id;
    @ColumnInfo(name="point1_lat")private double point1Lat;
    @ColumnInfo(name="point2_lat")private double point2Lat;
    @ColumnInfo(name="point3_lat")private double point3Lat;
    @ColumnInfo(name="point4_lat")private double point4Lat;
    @ColumnInfo(name="point1_longi")private double point1Longi;
    @ColumnInfo(name="point2_longi")private double point2Longi;
    @ColumnInfo(name="point3_longi")private double point3Longi;
    @ColumnInfo(name="point4_longi")private double point4Longi;

    public DbLocationRect(double point1Lat, double point2Lat, double point3Lat, double point4Lat,
                          double point1Longi, double point2Longi, double point3Longi,
                          double point4Longi) {
        this.point1Lat = point1Lat;
        this.point2Lat = point2Lat;
        this.point3Lat = point3Lat;
        this.point4Lat = point4Lat;
        this.point1Longi = point1Longi;
        this.point2Longi = point2Longi;
        this.point3Longi = point3Longi;
        this.point4Longi = point4Longi;
    }

    public DbLocationRect() {

    }

    public double getPoint1Lat() {
        return point1Lat;
    }

    public void setPoint1Lat(double point1Lat) {
        this.point1Lat = point1Lat;
    }

    public double getPoint2Lat() {
        return point2Lat;
    }

    public void setPoint2Lat(double point2Lat) {
        this.point2Lat = point2Lat;
    }

    public double getPoint3Lat() {
        return point3Lat;
    }

    public void setPoint3Lat(double point3Lat) {
        this.point3Lat = point3Lat;
    }

    public double getPoint4Lat() {
        return point4Lat;
    }

    public void setPoint4Lat(double point4Lat) {
        this.point4Lat = point4Lat;
    }

    public double getPoint1Longi() {
        return point1Longi;
    }

    public void setPoint1Longi(double point1Longi) {
        this.point1Longi = point1Longi;
    }

    public double getPoint2Longi() {
        return point2Longi;
    }

    public void setPoint2Longi(double point2Longi) {
        this.point2Longi = point2Longi;
    }

    public double getPoint3Longi() {
        return point3Longi;
    }

    public void setPoint3Longi(double point3Longi) {
        this.point3Longi = point3Longi;
    }

    public double getPoint4Longi() {
        return point4Longi;
    }

    public void setPoint4Longi(double point4Longi) {
        this.point4Longi = point4Longi;
    }
}
