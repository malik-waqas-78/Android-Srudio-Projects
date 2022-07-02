package com.recovery.data.forwhatsapp.fileobserverspkg;

import android.os.Build;
import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;

public class MyFileObserverClassOKRA extends FileObserver {

    private static final String TAG ="92727" ;
    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    boolean voicenote=false;

    public boolean isObserving() {
        return observing;
    }

    public void setObserving(boolean observing) {
        this.observing = observing;
    }

    boolean observing=false;
    FileObserver_InterfaceOKRA fileObserver_interfaceOKRA;

    public boolean isVoicenote() {
        return voicenote;
    }

    public void setVoicenote(boolean voicenote) {
        this.voicenote = voicenote;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public MyFileObserverClassOKRA(@NonNull File file, int mask) {
        super(file,mask);
        this.path=file.getAbsolutePath();
        Log.d(TAG, "MyFileObserverClass: q");
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public MyFileObserverClassOKRA(@NonNull File file) {
        super(file);
        this.path=file.getAbsolutePath();
        Log.d(TAG, "MyFileObserverClass: q");
    }

    public void setFileObserver_interface(FileObserver_InterfaceOKRA fileObserver_interfaceOKRA) {
        this.fileObserver_interfaceOKRA = fileObserver_interfaceOKRA;
        Log.d(TAG, "setFileObserver_interface: ");
    }

    public MyFileObserverClassOKRA(String path) {
        super(path);
        this.path=path;
        Log.d(TAG, "MyFileObserverClass: ");
    }

    public MyFileObserverClassOKRA(String path, int mask) {
        super(path, mask);
        this.path=path;
        Log.d(TAG, "MyFileObserverClass: path+mask");
    }

    @Override
    public void onEvent(int i, @Nullable String s) {
        Log.d(TAG, "onEvent: "+i+"   "+s+"  "+path);
//        if(isVoicenote()&&(i!=1073741840||i!=1073741825)){
//            fileObserver_interface.onCreateEventCalled(i,s);
//        }
        if(i==FileObserver.DELETE){
            fileObserver_interfaceOKRA.onDeleteEventCalled(i,s);
        } else{
            if(Build.VERSION.SDK_INT>=30){
                if(i==CREATE){
                    fileObserver_interfaceOKRA.onCreateEventCalled(i,s);
                }
            }else{
                if(i!=FileObserver.ACCESS&&i!=FileObserver.MOVED_FROM&& i!=FileObserver.OPEN&&i!=1073741840&&i!=1073741825
                        &&i!=CLOSE_NOWRITE||(i==CREATE||i==MOVED_TO)){
                    Log.d(TAG, "onEvent: created");
                    fileObserver_interfaceOKRA.onCreateEventCalled(i,s);
                }
            }
        }
    }

    @Override
    public void startWatching() {
        super.startWatching();
        observing=true;
        Log.d(TAG, "startWatching: ");
    }

    @Override
    public void stopWatching() {
        super.stopWatching();
        observing=false;
        Log.d(TAG, "stopWatching: ");
    }

    @Override
    protected void finalize() {
        super.finalize();
        observing=false;
        Log.d(TAG, "finalize: ");
    }
}
