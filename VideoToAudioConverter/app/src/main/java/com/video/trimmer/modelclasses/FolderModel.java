package com.video.trimmer.modelclasses;

import java.util.ArrayList;

public class FolderModel {
    String FolderName;
    ArrayList<String> VideoPath;
    String folderPath;
    public String getFolderName() {
        return FolderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public FolderModel(String folderName , String folderPath) {
        FolderName = folderName;
        this.folderPath = folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public ArrayList<String> getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(ArrayList<String> videoPath) {
        VideoPath = videoPath;
    }
}
