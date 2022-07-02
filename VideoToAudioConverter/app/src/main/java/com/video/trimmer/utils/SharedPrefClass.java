package com.video.trimmer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefClass {
    Context context;
    public String PINLOCK_KEY="audioUri";
    public String LANGUAGE_TYPE_KEY="languagetype2";
    public String LOCK_KEY="applock2";
    public String NOTIFICATION_KEY="notilock2";
    public String THEME_KEY="themekey2";
    public String SHARED_KEY="MySharedPreferences2";
    public String USER_STATUS_KEY="userstatus2";
    public String USER_MOBILE_KEY="userphonenumber2";
    public String SELECTED_BOOK_KEY="selectedBookId2";
    public String SELECTED_FILTER_KEY="selectedfilterkey2";
    public String SELECTED_DURATION_KEY="selecteddurationkey2";
    public String START_DATE_KEY="startdatekey2";
    public String END_DATE_KEY="enddatekey2";
    public String VIDEO_DURATION_KEY="videodurtation";
    public String AUDIO_DURATION_KEY="Audioduration";
    public String VIDEO_VOLUME_KEY="VideoVolumeKey";
    public String AUDIO_VOLUME_KEY="AudioVolumeKey";
    public String AUDIO_STARTPOS_KEY="AudioVoly";
    public String AUDIO_ENDPOS_KEY="AuolumeKey";
    public String VIDEO_STARTPOS_KEY="VideoStartPosKey";
    public String VIDEO_ENDPOS_KEY="VideoEndPosKey";
    public String TEMP_CREATED_VIDEO="temporarycreateedvideo";
    public String AUDIO_FILE_SIZE="nsadjfuisaduiacnashdfas";
    public String OUTPUT_URI="najdfnasdjncsdf";
    public String JUGAAD_KEY="Jugaadkeytohandlefirsttime";

    public SharedPrefClass(Context context) {
        this.context = context;
    }

    public void saveAppLockStateinShared(boolean value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putBoolean(LOCK_KEY,value);
        myEdit.apply();
    }
    public boolean getAppLockStatefromShared(){
        boolean status=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getBoolean(LOCK_KEY,false);
        return status;
    }

    public void saveNotificationStateinShared(boolean value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putBoolean(NOTIFICATION_KEY,value);
        myEdit.apply();
    }
    public boolean getNotificationStatefromShared(){
        boolean status=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getBoolean(NOTIFICATION_KEY,false);
        return status;
    }

    public void saveThemeStateinShared(boolean value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putBoolean(THEME_KEY,value);
        myEdit.apply();
    }
    public boolean getThemeStatefromShared(){
        boolean status=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getBoolean(THEME_KEY,false);
        return status;
    }
    public void saveLanguageCodeinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(LANGUAGE_TYPE_KEY,value);
        myEdit.apply();
    }
    public String getLanguageCodefromShared(String degault){
        String status=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(LANGUAGE_TYPE_KEY,degault);
        return status;
    }

    public void saveAudioUriinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(PINLOCK_KEY,value);
        myEdit.apply();
    }
    public String getAudioUrifromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(PINLOCK_KEY,null);
        return pin;
    }

    public void saveUserStatusinShared(boolean value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putBoolean(USER_STATUS_KEY,value);
        myEdit.apply();
    }
    public boolean getUserStatusfromShared(){
        boolean pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getBoolean(USER_STATUS_KEY,false);
        return pin;
    }

    public void saveUserPhoneNUmberinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(USER_MOBILE_KEY,value);
        myEdit.apply();
    }
    public String getUserPhoneNUmberfromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(USER_MOBILE_KEY,null);
        return pin;
    }

    public void saveChangedVolumeAudioPathinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(SELECTED_BOOK_KEY,value);
        myEdit.apply();
    }
    public String getChangedVolumeAudioPathfromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(SELECTED_BOOK_KEY,null);
        return pin;
    }

    public void saveChangedVolumeVideoPathinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(SELECTED_FILTER_KEY,value);
        myEdit.apply();
    }
    public String getChangedVolumeVideoPathfromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(SELECTED_FILTER_KEY,null);
        return pin;
    }

    public void saveTrimmedVideoPahtinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(SELECTED_DURATION_KEY,value);
        myEdit.apply();
    }
    public String getTrimmedVideoPathfromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(SELECTED_DURATION_KEY,null);
        return pin;
    }

    public void saveTrimmedAudioPathinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(START_DATE_KEY,value);
        myEdit.apply();
    }
    public String getTrimmedAudioPatjfromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(START_DATE_KEY,null);
        return pin;
    }

    public void saveSummedVideoinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(END_DATE_KEY,value);
        myEdit.apply();
    }
    public String getSummedVideofromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(END_DATE_KEY,null);
        return pin;
    }

    public void saveVideoDurationinShared(long value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putLong(VIDEO_DURATION_KEY,value);
        myEdit.apply();
    }
    public long getVideoDurationfromShared(){
        long pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getLong(VIDEO_DURATION_KEY,0);
        return pin;
    }

    public void saveAudioDurationinShared(long value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putLong(AUDIO_DURATION_KEY,value);
        myEdit.apply();
    }
    public long getAudioDurationfromShared(){
        long pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getLong(AUDIO_DURATION_KEY,0);
        return pin;
    }

    public void saveVideoVolumeinShared(int value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putInt(VIDEO_VOLUME_KEY,value);
        myEdit.apply();
    }
    public int getVideoVolumefromShared(){
        int pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getInt(VIDEO_VOLUME_KEY,50);
        return pin;
    }

    public void saveAudioVolumeinShared(int value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putInt(AUDIO_VOLUME_KEY,value);
        myEdit.apply();
    }
    public int getAudioVolumefromShared(){
        int pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getInt(AUDIO_VOLUME_KEY,50);
        return pin;
    }


    public void saveAudioStartPosinShared(long value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putLong(AUDIO_STARTPOS_KEY,value);
        myEdit.apply();
    }
    public long getAudioStartPosfromShared(){
        long pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getLong(AUDIO_STARTPOS_KEY,0);
        return pin;
    }

    public void saveAudioEndPosinShared(long value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putLong(AUDIO_ENDPOS_KEY,value);
        myEdit.apply();
    }
    public long getAudioEndPosfromShared(){
        long pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getLong(AUDIO_ENDPOS_KEY,0);
        return pin;
    }

    public void saveVideoStartPosinShared(int value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putInt(VIDEO_STARTPOS_KEY,value);
        myEdit.apply();
    }
    public int getVideoStartPosfromShared(){
        int pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getInt(VIDEO_STARTPOS_KEY,0);
        return pin;
    }

    public void saveVideoEndPosinShared(int value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putInt(VIDEO_ENDPOS_KEY,value);
        myEdit.apply();
    }
    public int getVideoEndPosfromShared(){
        int pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getInt(VIDEO_ENDPOS_KEY,0);
        return pin;
    }

    public void saveTempCreatedVideoinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(TEMP_CREATED_VIDEO,value);
        myEdit.apply();
    }
    public String getTempCreatedVideofromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(TEMP_CREATED_VIDEO,null);
        return pin;
    }
    public void saveAudioSizeinShared(Float value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putFloat(AUDIO_FILE_SIZE,value);
        myEdit.apply();
    }
    public Float getAudioSizefromShared(){
        Float pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getFloat(AUDIO_FILE_SIZE,0.0f);
        return pin;
    }

    public void saveOutputPathinShared(String value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putString(OUTPUT_URI,value);
        myEdit.apply();
    }
    public String getOutputPatjfromShared(){
        String pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getString(OUTPUT_URI,null);
        return pin;
    }

    public void saveJugaadStateinShared(boolean value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putBoolean(JUGAAD_KEY,value);
        myEdit.apply();
    }
    public boolean getJugaadStatefromShared(){
        boolean pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getBoolean(JUGAAD_KEY,true);
        return pin;
    }


}
