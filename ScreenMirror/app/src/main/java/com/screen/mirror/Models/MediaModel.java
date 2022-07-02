package com.screen.mirror.Models;

import java.util.ArrayList;

public class MediaModel {
    String folderName;
    String folderPath;
    ArrayList<String> filesInFolder;
    int isExpanded;

    public MediaModel(String folderName, String folderPath, int isExpanded) {
        this.folderName = folderName;
        this.folderPath = folderPath;
        filesInFolder = new ArrayList<>();
        this.isExpanded = isExpanded;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public ArrayList<String> getFilesInFolder() {
        return filesInFolder;
    }

    public void setFilesInFolder(ArrayList<String> filesInFolder) {
        this.filesInFolder = filesInFolder;
    }

    public int getIsExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(int isExpanded) {
        this.isExpanded = isExpanded;
    }
}
