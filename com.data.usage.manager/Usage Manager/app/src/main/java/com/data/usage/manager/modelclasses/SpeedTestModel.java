package com.data.usage.manager.modelclasses;

public class SpeedTestModel {
    String ping;
    String downloadSpeed;
    String uploadSpeed;
    String timestamp;

    public SpeedTestModel(String timestamp, String ping, String downloadSpeed, String uploadSpeed) {
        this.timestamp = timestamp;
        this.ping = ping;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
    }

    public SpeedTestModel(){}

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(String downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public String getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(String uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
