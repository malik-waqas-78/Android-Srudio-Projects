package com.niazitvpro.official.download;

import java.io.Serializable;

public class DownloadInfo implements Serializable {

    public String liveUrl,fileName;
    public int id;

    public DownloadInfo(String liveUrl,int id,String fileName){
        this.fileName = fileName;
        this.id =id;
        this.liveUrl = liveUrl;
    }
}
