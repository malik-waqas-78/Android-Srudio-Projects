package com.test.speedmeter;

import android.location.Location;

import com.microsoft.maps.Geopoint;
import com.microsoft.maps.Geoposition;

import java.util.ArrayList;

public class MyLocationRect {

    private Geoposition point1;
    private Geoposition point2;
    private Geoposition point3;
    private Geoposition point4;

    public MyLocationRect(DbLocationRect dbLocationRect) {
        point1=new Geoposition(dbLocationRect.getPoint1Lat(),dbLocationRect.getPoint1Longi());
        point2=new Geoposition(dbLocationRect.getPoint2Lat(),dbLocationRect.getPoint2Longi());
        point3=new Geoposition(dbLocationRect.getPoint3Lat(),dbLocationRect.getPoint3Longi());
        point4=new Geoposition(dbLocationRect.getPoint4Lat(),dbLocationRect.getPoint4Longi());
    }

    public MyLocationRect() {

    }

    public Geoposition getPoint1() {
        return point1;
    }

    public void setPoint1(Geoposition point1) {
        this.point1 = point1;
    }

    public Geoposition getPoint2() {
        return point2;
    }

    public void setPoint2(Geoposition point2) {
        this.point2 = point2;
    }

    public Geoposition getPoint3() {
        return point3;
    }

    public void setPoint3(Geoposition point3) {
        this.point3 = point3;
    }

    public Geoposition getPoint4() {
        return point4;
    }

    public void setPoint4(Geoposition point4) {
        this.point4 = point4;
    }

    public DbLocationRect getDbLocationRect(){
        DbLocationRect rect=new DbLocationRect();
        rect.setPoint1Lat(point1.getLatitude());
        rect.setPoint2Lat(point2.getLatitude());
        rect.setPoint3Lat(point3.getLatitude());
        rect.setPoint4Lat(point4.getLatitude());
        rect.setPoint1Longi(point1.getLongitude());
        rect.setPoint2Longi(point2.getLongitude());
        rect.setPoint3Longi(point3.getLongitude());
        rect.setPoint4Longi(point4.getLongitude());
        return rect;

    }

    public ArrayList<Geoposition> getGeoPositionsArrayList(){
        ArrayList<Geoposition> positions=new ArrayList<>();
        positions.add(point1);
        positions.add(point2);
        positions.add(point3);
        positions.add(point4);
        return positions;
    }

    public boolean isCompleted() {
        if(point1!=null&&point2!=null&&point3!=null&&point4!=null){
            return true;
        }
        return false;
    }
}
