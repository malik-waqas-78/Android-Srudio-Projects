package com.recovery.data.forwhatsapp.stickerspkg;

import android.content.Context;
import android.util.Log;

import com.recovery.data.forwhatsapp.fileobserverspkg.FileObserverALLDirectoriesOKRA;

public class StickersFileObserverOKRA {
    static final String TAG="92727";
    String foldername="WhatsApp Stickers/";
    FileObserverALLDirectoriesOKRA fileObserver_for_allDirectories;
    Context context;
    public StickersFileObserverOKRA(Context context) {
        this.context=context;
        fileObserver_for_allDirectories=new FileObserverALLDirectoriesOKRA(context,foldername);
    }

    public void startObserving() {
        if(fileObserver_for_allDirectories!=null) {
            fileObserver_for_allDirectories.startObserving();
        }else{
            fileObserver_for_allDirectories=new FileObserverALLDirectoriesOKRA(context,foldername);
            fileObserver_for_allDirectories.startObserving();
        }
    }
    public boolean isObserving(){
        return fileObserver_for_allDirectories.isObserving();
    }
    public void stopObserving(){
        if(fileObserver_for_allDirectories!=null) {
            fileObserver_for_allDirectories.stopObserving();
            return;
        }
        Log.d(TAG, "stopObserving: null");
    }
}
