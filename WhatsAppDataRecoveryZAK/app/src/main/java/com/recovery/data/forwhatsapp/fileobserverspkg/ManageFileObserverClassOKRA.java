package com.recovery.data.forwhatsapp.fileobserverspkg;


import android.content.Context;
import android.util.Log;

import com.recovery.data.forwhatsapp.audiopkg.AudiosFileObserverClassOKRA;
import com.recovery.data.forwhatsapp.documentspkg.DocumentsFileObserverClassOKRA;
import com.recovery.data.forwhatsapp.gifspkg.GifsFileObserverClassOKRA;
import com.recovery.data.forwhatsapp.imagespkg.ImagesFileObserverClassOKRA;
import com.recovery.data.forwhatsapp.stickerspkg.StickersFileObserverOKRA;
import com.recovery.data.forwhatsapp.videospkg.VideosFileObserverClassOKRA;
import com.recovery.data.forwhatsapp.voicenotespkg.VoiceNotesFileObserverClassOKRA;

public class ManageFileObserverClassOKRA {

    private ImagesFileObserverClassOKRA images_observer;
    private VideosFileObserverClassOKRA videos_observer;
    private GifsFileObserverClassOKRA gifs_observer;
    private StickersFileObserverOKRA stickers_observer;
    private DocumentsFileObserverClassOKRA documents_observer;
    private VoiceNotesFileObserverClassOKRA voiceNotes_observer;
    private AudiosFileObserverClassOKRA audios_observer;
    private static final String TAG ="92727" ;

    Context context;
    public ManageFileObserverClassOKRA(Context context) {
        this.context=context;
       images_observer=new ImagesFileObserverClassOKRA(context);
       videos_observer=new VideosFileObserverClassOKRA(context);
       gifs_observer=new GifsFileObserverClassOKRA(context);
       stickers_observer=new StickersFileObserverOKRA(context);
       documents_observer =new DocumentsFileObserverClassOKRA(context);
       voiceNotes_observer=new VoiceNotesFileObserverClassOKRA(context);
       audios_observer=new AudiosFileObserverClassOKRA(context);
        Log.d(TAG, "ManageFileObserverClass: ");
    }

    public void startObserving() {
        if(images_observer!=null) {
            images_observer.startObserving();
        }else{
            images_observer=new ImagesFileObserverClassOKRA(context);
            images_observer.startObserving();
        }
        if(videos_observer!=null) {
            videos_observer.startObserving();
        }else{
            videos_observer=new VideosFileObserverClassOKRA(context);
            videos_observer.startObserving();
        }
        if(gifs_observer!=null) {
            gifs_observer.startObserving();
        }else{
            gifs_observer=new GifsFileObserverClassOKRA(context);
            gifs_observer.startObserving();
        }
        if(stickers_observer!=null){
            stickers_observer.startObserving();
        }else{
            stickers_observer=new StickersFileObserverOKRA(context);
            stickers_observer.startObserving();
        }
        if(documents_observer !=null){
            documents_observer.startObserving();
        }else{
            documents_observer =new DocumentsFileObserverClassOKRA(context);
            documents_observer.startObserving();
        }
        if(voiceNotes_observer !=null){
            voiceNotes_observer.startObserving();
        }else{
            voiceNotes_observer =new VoiceNotesFileObserverClassOKRA(context);
            voiceNotes_observer.startObserving();
        }
        if(audios_observer !=null){
            audios_observer.startObserving();
        }else{
            audios_observer =new AudiosFileObserverClassOKRA(context);
            audios_observer.startObserving();
        }
    }
    public void stopObserving(){
        if(images_observer!=null) {
            images_observer.stopObserving();
        }

        if(videos_observer!=null) {
            videos_observer.stopObserving();
        }

        if(gifs_observer!=null) {
            gifs_observer.stopObserving();
        }

        if(stickers_observer!=null){
            stickers_observer.stopObserving();
        }

        if(documents_observer !=null){
            documents_observer.stopObserving();
        }
        if(voiceNotes_observer !=null){
            voiceNotes_observer.stopObserving();
        }
        if(audios_observer!=null){
            audios_observer.stopObserving();
        }
    }

    public boolean isObserving() {
        boolean is_observing=false;
        if(images_observer!=null){
            is_observing=images_observer.isObserving();
        }else{
            images_observer=new ImagesFileObserverClassOKRA(context);
            is_observing=images_observer.isObserving();
        }
        if(videos_observer!=null){
            is_observing=is_observing&&videos_observer.isObserving();
        }else{
            videos_observer=new VideosFileObserverClassOKRA(context);
            is_observing=is_observing&&videos_observer.isObserving();
        }
        if(gifs_observer!=null){
            is_observing=is_observing&&gifs_observer.isObserving();
        }else{
            gifs_observer=new GifsFileObserverClassOKRA(context);
            is_observing=is_observing&&gifs_observer.isObserving();
        }
        if(stickers_observer!=null){
            is_observing=is_observing&&stickers_observer.isObserving();
        }else{
            stickers_observer=new StickersFileObserverOKRA(context);
            is_observing=is_observing&&stickers_observer.isObserving();
        }
        if(documents_observer !=null){
            is_observing=is_observing&& documents_observer.isObserving();
        }else{
            documents_observer =new DocumentsFileObserverClassOKRA(context);
            is_observing=is_observing&& documents_observer.isObserving();
        }
        if(audios_observer !=null){
            is_observing=is_observing&& audios_observer.isObserving();
        }else{
            audios_observer =new AudiosFileObserverClassOKRA(context);
            is_observing=is_observing&& audios_observer.isObserving();
        }
        return is_observing;
    }

}
