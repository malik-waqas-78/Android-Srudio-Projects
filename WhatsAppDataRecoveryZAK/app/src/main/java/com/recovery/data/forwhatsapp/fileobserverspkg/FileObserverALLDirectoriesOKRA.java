package com.recovery.data.forwhatsapp.fileobserverspkg;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.recovery.data.forwhatsapp.voicenotespkg.SubdirectoryFileObserverInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.recovery.data.forwhatsapp.AppOKRA.FILE_SAVED_CHANNEL_ID;

public class FileObserverALLDirectoriesOKRA implements FileObserver_InterfaceOKRA {

    static final String TAG = "92727";

    public void setObserver_interface(SubdirectoryFileObserverInterfaceOKRA observer_interface) {
        this.observer_interface = observer_interface;
    }

    public SubdirectoryFileObserverInterfaceOKRA observer_interface = null;

    public FileObserverALLDirectoriesOKRA(Context context) {
        this.context = context;
    }

    Context context;

//    public String getWhatsapp_Files_Path() {
//        return whatsapp_Files_Path;
//    }

    public void setWhatsapp_Files_Path(String whatsapp_Files_Path) {
        this.whatsapp_Files_Path = this.whatsapp_Files_Path + whatsapp_Files_Path;
        Log.d(TAG, "setWhatsapp_Files_Path: " + whatsapp_Files_Path);
    }

//    public String getRecovery_Files_Path() {
//        return recovery_Files_Path;
//    }

    public void setRecovery_Files_Path(String recovery_Files_Path) {
        this.recovery_Files_Path =  Environment.getExternalStorageDirectory().getAbsolutePath() +"/WhatsAppDataRecovery/" + recovery_Files_Path;
        setRecoveryDirectory();
        Log.d(TAG, "setRecovery_Files_Path: " + recovery_Files_Path);
    }

//    public String getInternal_Files_Path() {
//        return internal_Files_Path;
//    }

    public void setInternal_Files_Path(String internal_Files_Path) {
        this.internal_Files_Path = this.internal_Files_Path + internal_Files_Path;
        setInternalFilesDirectory();
        Log.d(TAG, "setInternal_Files_Path: " + internal_Files_Path);
    }

    //boolean voiceNotes = false;
    private String whatsapp_Files_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/";
    private String recovery_Files_Path = "";
    private String internal_Files_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.MyFiles/";
    MyFileObserverClassOKRA file_observer;
    //boolean isObserving=false;
    File internalFilesDirectory;
    File whatsAppFilesDirectory;
    File recoveryDirectory;

//    public boolean isVoiceNote() {
//        return voiceNote;
//    }

    public void setVoiceNote(boolean voiceNote) {
        this.voiceNote = voiceNote;
    }

    boolean voiceNote = false;
    //ArrayList<MyFileObserverClass> fileObserverClassArrayList = new ArrayList<>();
    ArrayList<File> internalFilesList = new ArrayList<>();
    ArrayList<File> whatsAppFilesList = new ArrayList<>();

    public FileObserverALLDirectoriesOKRA(Context context, String foldername) {
        recovery_Files_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppDataRecovery/";
        whatsapp_Files_Path = whatsapp_Files_Path + foldername;
        // Log.d(TAG, "FileObserver_For_ALLDirectories: " + whatsapp_Files_Path);
        recovery_Files_Path = recovery_Files_Path + foldername;
        // Log.d(TAG, "FileObserver_For_ALLDirectories: " + recovery_Files_Path);
        internal_Files_Path = internal_Files_Path + foldername;
        // Log.d(TAG, "FileObserver_For_ALLDirectories: " + internal_Files_Path);
        this.context = context;
        register_fileObservers(whatsapp_Files_Path);
    }

    public void callRegisterFileObserver() {
        register_fileObservers(whatsapp_Files_Path);
    }

    public boolean isObserving() {
        return file_observer.isObserving();
    }

    public void register_fileObservers(String file_path) {
        Log.d(TAG, "register_fileObservers: begin" + file_path);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            File files_Directory = new File(file_path);
            file_observer = new MyFileObserverClassOKRA(files_Directory);
        } else {
            file_observer = new MyFileObserverClassOKRA(file_path);
        }
        file_observer.setFileObserver_interface(this);
        file_observer.startWatching();
        Log.d(TAG, "register_fileObservers: end" + file_path);
        //images_observer.get(0).onEvent(new O);
//           if(Build.VERSION.SDK_INT>=21){
//               setFilesDirectory();
//               store_Files_Form_External_to_Internal_Storage();
//           }
    }

    public void startObserving() {
        if (file_observer != null) {
            file_observer.startWatching();
        } else {
            register_fileObservers(whatsapp_Files_Path);
        }
    }

    public void stopObserving() {

        if (file_observer != null) {
            file_observer.stopWatching();
        }

    }

    @Override
    public void onDeleteEventCalled(int i, String s) {
        Log.d("malik", "onDeleteEventCalled: msg :: " + internal_Files_Path + s);
        //storefiles to external recovery folder
        setRecoveryDirectory();
        storeFiles(internal_Files_Path, recovery_Files_Path);
        Log.d("malik", "onDeleteEventCalled: success");
        // Log.d(TAG, "onDeleteEventCalled: " + i + "    " + s);
    }


    private void setRecoveryDirectory() {
        if (recoveryDirectory == null) {
            recoveryDirectory = new File(recovery_Files_Path);
        }
        if (!recoveryDirectory.exists()) {
            recoveryDirectory.mkdirs();
        }
    }

    @Override
    public void onCreateEventCalled(int i, String s) {
        if (voiceNote) {
            observer_interface.subdirectoryRegister(i, s);
            return;
        }
        setFilesDirectory();
        store_Files_Form_External_to_Internal_Storage();
        Log.d(TAG, "onCreateEventCalled: recovery :: " + recovery_Files_Path);
        Log.d(TAG, "onCreateEventCalled: internal :: " + internal_Files_Path);

    }


    public void setFilesDirectory() {
        Log.d(TAG, "setFilesDirectory: ");
        //internal_File_Path=context.getFilesDir().getAbsolutePath().toString();
        setInternalFilesDirectory();
        //  setWhatsAppFilesDirectory();
    }


    public void setInternalFilesDirectory() {

        File internalFilesDirectory = new File(internal_Files_Path);
        if (!internalFilesDirectory.exists()) {
            internalFilesDirectory.mkdirs();
        }
    }

    public void store_Files_Form_External_to_Internal_Storage() {
        ArrayList<File> filesToStore = compareAndGetFilesToStore();
        Log.d(TAG, "store_Files_Form_External_to_Internal_Storage: size :" + filesToStore.size());
        //ArrayList<File> filesToRecover = compareAndGetFilesToRecover();
        if (filesToStore.size() != 0) {
            try {
                storeFilesToInternalStorage(filesToStore);
                if (filesToStore.size() != 0)
                    //storeFilesToRecoveryDirectory(filesToRecover);
                    Log.d(TAG, "store_Files_Form_External_to_Internal_Storage: success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "store_Files_Form_External_to_Internal_Storage: exception inn source,dest");
            }
        } else {
            Log.d(TAG, "store_Files_Form_External_to_Internal_Storage: no changes detected");
        }
    }
//
//    private void storeFilesToRecoveryDirectory(ArrayList<File> files) {
//        for (File f : files) {
//                storeFiles(internal_Files_Path, recovery_Files_Path, f.getName());
//        }
//    }


    public void storeFilesToInternalStorage(ArrayList<File> files) throws IOException {
        Log.d(TAG, "storeFilesToInternalStorage: files to store :: " + files.size());
        for (File f : files) {
            File file = new File(internal_Files_Path, f.getName());
            if (file.exists()) {
                Log.d(TAG, "storeFilesToInternalStorage: continued");
                continue;
            } else {
                file.createNewFile();
                FileChannel source = null;
                FileChannel destination = null;
                FileInputStream in;
                FileOutputStream out;
                try {
                    in = new FileInputStream(f);
                    out = new FileOutputStream(file);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        FileUtils.copy(in, out);
                    } else {
                        source = in.getChannel();
                        destination = out.getChannel();
                        destination.transferFrom(source, 0, source.size());
                    }
                    in.close();
                    out.close();
                    Log.d(TAG, "storeFilesToInternalStorage: file saved :: " + f.getName());
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "storeFilesToInternalStorage: exception 1");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(TAG, "storeFilesToInternalStorage: exception 2");
                    e.printStackTrace();
                } finally {
                    if (source != null) {
                        source.close();
                    } else {
                        Log.d(TAG, "storeFilesToInternalStorage: no source");
                    }
                    if (destination != null) {
                        destination.close();
                    } else {
                        Log.d(TAG, "storeFilesToInternalStorage: no destination");
                    }
                }

            }
        }
    }

    public ArrayList<File> compareAndGetFilesToStore() {
        ArrayList<File> filesNotStored = new ArrayList<>();
        if (internalFilesList == null && whatsAppFilesList == null) {
            setInternalFilesDirectory();
            internalFilesList = new ArrayList<>();
            whatsAppFilesList = new ArrayList<>();
            //setWhatsAppFilesDirectory();
        }
        getAllDirectoriesFiles();
        filesNotStored = compareAndGet();
        Log.d(TAG, "compareAndGetFilesToStore: Files To Store :: " + filesNotStored.size());
        return filesNotStored;
    }

//    public ArrayList<File> compareAndGetFilesToRecover() {
//        ArrayList<File> filesNotStored = new ArrayList<>();
//        if (internalFilesList == null && whatsAppFilesList == null) {
//            setInternalFilesDirectory();
//            internalFilesList=new ArrayList<>();
//            whatsAppFilesList=new ArrayList<>();
//            //setWhatsAppFilesDirectory();
//        }
//        getAllDirectoriesFiles();
//        filesNotStored = compareAndGetRecoverFiles();
//        Log.d(TAG, "compareAndGetFilesToStore: Files To recover :: " + filesNotStored.size());
//        return filesNotStored;
//    }

//    public ArrayList<File> compareAndGetRecoverFiles() {
//        ArrayList<File> files = new ArrayList<>();
//        if (internalFilesList.size() == 0) {
//            Log.d(TAG, "compareAndGet: NO files Avaailable :: ");
//            return files;
//        }
//        if (whatsAppFilesList.size() == 0) {
//            files.addAll(internalFilesList);
//            return files;
//        } else
//            for (int i = 0; i < internalFilesList.size(); i++) {
//                File f = internalFilesList.get(i);
//                boolean found = false;
//                for (int j = 0; j < whatsAppFilesList.size(); j++) {
//                    if (f.getName().equals(whatsAppFilesList.get(j).getName())) {
//                        Log.d(TAG, "compareAndGet: File already present:: " + f.getName());
//                        found = true;
//                        break;
//                    }
//                }
//                if (!found) {
//                    files.add(f);
//                    Log.d(TAG, "compareAndGet: Missing File Added:: " + f.getName());
//                }
//            }
//
//        return files;
//    }
//

    public ArrayList<File> compareAndGet() {

        //actual work
        ArrayList<File> files = new ArrayList<>();
        if (whatsAppFilesList.size() == 0) {
            Log.d(TAG, "compareAndGet: NO files Avaailable :: ");
            return files;
        }
        if (internalFilesList.size() == 0) {
            files.addAll(whatsAppFilesList);
            return files;
        } else
            for (int i = 0; i < whatsAppFilesList.size(); i++) {
                File f = whatsAppFilesList.get(i);
                boolean found = false;
                for (int j = 0; j < internalFilesList.size(); j++) {
                    if (f.getName().equals(internalFilesList.get(j).getName())) {
                        Log.d(TAG, "compareAndGet: File already present:: " + f.getName());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    files.add(f);
                    Log.d(TAG, "compareAndGet: Missing File Added:: " + f.getName());
                }
            }

        return files;
    }

    public ArrayList<File> compareAndGetRecveryFiles() {
        if (internalFilesList == null && whatsAppFilesList == null) {
            setInternalFilesDirectory();
            internalFilesList = new ArrayList<>();
            whatsAppFilesList = new ArrayList<>();
            //setWhatsAppFilesDirectory();
        }
        getAllDirectoriesFiles();
        //files
        ArrayList<File> files = new ArrayList<>();
        if (internalFilesList.size() == 0) {
            Log.d("malik", "compareAndGet: NO files Avaailable :: ");
            return files;
        }
        if (whatsAppFilesList.size() == 0) {
            files.addAll(internalFilesList);
            Log.d(TAG, "compareAndGetRecveryFiles: All files added");
            return files;
        } else
            for (int i = 0; i < internalFilesList.size(); i++) {
                File f = internalFilesList.get(i);
                boolean found = false;
                for (int j = 0; j < whatsAppFilesList.size(); j++) {
                    if (f.getName().equals(whatsAppFilesList.get(j).getName())) {
                        Log.d("malik", "compareAndGet: File already present:: " + f.getName());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    files.add(f);
                    Log.d("malik", "compareAndGet: Missing File Added:: " + f.getName());
                }
            }

        return files;
    }

    public void getAllDirectoriesFiles() {
        getInternalFiles();
        getExternalFiles();
    }


    public void getInternalFiles() {
        if (internalFilesDirectory == null) {
            internalFilesDirectory = new File(internal_Files_Path);
        }
        if (!internalFilesDirectory.exists()) {
            setInternalFilesDirectory();
        }
        if (internalFilesList != null) {
            internalFilesList = getFilesList(internalFilesDirectory);
        } else {
            internalFilesList = new ArrayList<>();
            internalFilesList = getFilesList(internalFilesDirectory);
        }
    }

    public void getExternalFiles() {
        if (whatsAppFilesDirectory == null) {
            whatsAppFilesDirectory = new File(whatsapp_Files_Path);
        }
        if (whatsAppFilesList != null) {
            whatsAppFilesList = getFilesList(whatsAppFilesDirectory);
        } else {
            whatsAppFilesList = new ArrayList<>();
            whatsAppFilesList = getFilesList(whatsAppFilesDirectory);
        }
    }


    public ArrayList<File> getFilesList(File file) {
        File[] files = file.listFiles();
        // Log.d(TAG, "getFilesList: path :: " + file.getAbsolutePath());
        ArrayList<File> filesList = new ArrayList<>();
        if (files == null) {
            return filesList;
        }
        for (File f : files) {
            if (!f.isDirectory()) {
                //  Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                //   Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                filesList.add(f);
            }
        }
        return filesList;
    }


    public void storeFiles(String src_FilePath, String des_FilePath) {
        ArrayList<File> files=compareAndGetRecveryFiles();
        for(File f:files){
            storethisFile(src_FilePath,des_FilePath,f.getName());
        }
    }

    public void storethisFile(String src_FilePath, String des_FilePath, String filesname) {


        boolean success = false;
        File fileIn = new File(src_FilePath, filesname);
        if (!fileIn.exists()) {
            Log.d("malik", "storeFiles: filein do not exist");
            return;
        }
        File fileOut = new File(des_FilePath, filesname);
        Log.d("malik", "storeFiles: des path" + des_FilePath + "\n" + fileOut.getAbsolutePath());
        if (fileOut.exists()) {
            Log.d("malik", "storeFilesToInternalStorage: file already exists");
        } else {
            try {
                fileOut.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileChannel source = null;
            FileChannel destination = null;
            FileInputStream in;
            FileOutputStream out;
            try {
                in = new FileInputStream(fileIn);
                out = new FileOutputStream(fileOut);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    FileUtils.copy(in, out);
                } else {
                    source = in.getChannel();
                    destination = out.getChannel();
                    destination.transferFrom(source, 0, source.size());
                }

                Log.d("malik", "storeFilesToInternalStorage: file saved :: " + fileOut.getName());
                success = true;

            } catch (FileNotFoundException e) {
                Log.d("malik", "storeFilesToInternalStorage: exception 1" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("malik", "storeFilesToInternalStorage: exception 2" + e.getMessage());
                e.printStackTrace();
            } finally {
                if (source != null) {
                    try {
                        source.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("malik", "storeFiles: exc source closing");
                    }
                } else {
                    Log.d("malik", "storeFilesToInternalStorage: no source");
                }
                if (destination != null) {
                    try {
                        destination.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("malik", "storeFiles: excep destination closing");
                    }
                } else {
                    Log.d("malik", "storeFilesToInternalStorage: no destination");
                }
            }
            if (success) {
                fileIn.delete();
                System.gc();
                showNotification();
            }

        }
    }

        private void showNotification() {
            // NotificationManagerCompat notificationManagerCompat;
            //notificationManagerCompat = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
            int notificationId = 92728;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, notificationBuilder().build());
        }
    private NotificationCompat.Builder notificationBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, FILE_SAVED_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(context.getString(R.string.file_retrived))
                .setContentText(context.getString(R.string.file_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return builder;

    }

}
