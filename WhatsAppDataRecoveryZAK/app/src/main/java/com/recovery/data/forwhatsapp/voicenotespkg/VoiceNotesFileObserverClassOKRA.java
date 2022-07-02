package com.recovery.data.forwhatsapp.voicenotespkg;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.recovery.data.forwhatsapp.fileobserverspkg.FileObserverALLDirectoriesOKRA;

import java.io.File;
import java.util.ArrayList;

public class VoiceNotesFileObserverClassOKRA implements SubdirectoryFileObserverInterfaceOKRA {
    static final String TAG="92727";
    String foldername="WhatsApp Voice Notes/";
    FileObserverALLDirectoriesOKRA fileObserver_for_allDirectories;
    ArrayList<FileObserverALLDirectoriesOKRA> arrayList_of_observers=new ArrayList<>();
    Context context=null;
    public VoiceNotesFileObserverClassOKRA(Context context) {
        this.context=context;
        fileObserver_for_allDirectories=new FileObserverALLDirectoriesOKRA(this.context,foldername);
        fileObserver_for_allDirectories.startObserving();
        fileObserver_for_allDirectories.setVoiceNote(true);
        fileObserver_for_allDirectories.setObserver_interface(this);
        setObserverForSubDirectories();
        Log.d(TAG, "VoiceNotes_FileObserver_Class: ");
    }
    private void setObserverForSubDirectories() {
        String mainPath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/"+foldername;
        File f=new File(mainPath);
        ArrayList<String> folders=getFolders(f);
        for(String file:folders){
            FileObserverALLDirectoriesOKRA observer=new FileObserverALLDirectoriesOKRA(this.context);
            observer.setWhatsapp_Files_Path(foldername+file);
            observer.setRecovery_Files_Path(foldername);
            observer.setInternal_Files_Path(foldername);
            observer.callRegisterFileObserver();
            observer.startObserving();
            Log.d(TAG, "setObserverForSubDirectories: "+foldername+file);
            arrayList_of_observers.add(observer);
        }
    }

    private ArrayList<String> getFolders(File f) {
        File[] files=f.listFiles();
        ArrayList<String> folders = new ArrayList<>();
        if(files!=null){
            for(File file:files){
                if(file.isDirectory()){
                    folders.add(file.getName());
                    Log.d(TAG, "setObserverForSubDirectories: "+file.getName());
                }
            }
        }
        return folders;
    }

    public void startObserving() {
        if(context==null){
            Log.d(TAG, "startObserving: context null ");
        }
        if(fileObserver_for_allDirectories!=null) {
            fileObserver_for_allDirectories.startObserving();
        }else{
            fileObserver_for_allDirectories=new FileObserverALLDirectoriesOKRA(context,foldername);
            fileObserver_for_allDirectories.startObserving();
        }
    }
    public void stopObserving(){
        if(fileObserver_for_allDirectories!=null) {
            fileObserver_for_allDirectories.stopObserving();
            return;
        }
        Log.d(TAG, "stopObserving: null");
    }

    @Override
    public void subdirectoryRegister(int i, String s) {
        FileObserverALLDirectoriesOKRA observer=new FileObserverALLDirectoriesOKRA(this.context);
        observer.setWhatsapp_Files_Path(foldername+s);
        observer.setRecovery_Files_Path(foldername);
        observer.setInternal_Files_Path(foldername);
        observer.callRegisterFileObserver();
        observer.startObserving();
        Log.d(TAG, "setObserverForSubDirectories: "+foldername+s);
        arrayList_of_observers.add(observer);
    }
    public boolean isObserving(){
        boolean isObserving=fileObserver_for_allDirectories.isObserving();
        if(!isObserving){
            return isObserving;
        }
        for(FileObserverALLDirectoriesOKRA observer:arrayList_of_observers){
            isObserving=isObserving&&observer.isObserving();
        }
        return isObserving;
    }
}
