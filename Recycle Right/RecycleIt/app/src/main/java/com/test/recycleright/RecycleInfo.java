package com.test.recycleright;

public class RecycleInfo {

    private boolean isPlastic=false;
    private boolean isPaper=false;
    private boolean isCans=false;

    private int quantity=0;

    private String latitued=null;
    private String longitued=null;

    private String pickUpTime;
    private String date;

    private static RecycleInfo info=null;

    public static RecycleInfo getObj(){
        if(info==null){
            info=new RecycleInfo();
        }
        return  info;
    }

    public String getLatitued() {
        return latitued;
    }

    public void setLatitued(String latitued) {
        this.latitued = latitued;
    }

    public String getLongitued() {
        return longitued;
    }

    public void setLongitued(String longitued) {
        this.longitued = longitued;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isPlastic() {
        return isPlastic;
    }

    public void setPlastic(boolean plastic) {
        isPlastic = plastic;
    }

    public boolean isPaper() {
        return isPaper;
    }

    public void setPaper(boolean paper) {
        isPaper = paper;
    }

    public boolean isCans() {
        return isCans;
    }

    public void setCans(boolean cans) {
        isCans = cans;
    }

    @Override
    public String toString(){
        return "We will be Collecting\n"+
                (isPlastic?"Plastic\t":"")+
                (isPaper?"Paper\t":"")+
                (isCans?"Cans\t":"")+"\n"+
                quantity+" bag will be collected\n"+
                "Your pickUp Location is as follow\n"+
                "Lat: "+latitued+" Long: "+longitued+
                "\nTime: "+pickUpTime+"\nDate: "+date;
    }
}
