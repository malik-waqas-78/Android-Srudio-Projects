package com.photo.recovery.constants

class MyConstantsAAT {
    companion object{
        const val TAG="92727"
        const val NL_TAG="586243"
        const val OBS_TAG="1998"

        //PARENT DIR
        const val MEDIA_FOLDER_NAME="MEDIA"
        const val DOC_FOLDER_NAME="DOCS"
        //MEDIA CHILD DIR
        const val IMAGE_FOLDER_NAME="IMAGES"
        const val VIDEOS_FOLDER_NAME="VIDEOS"
        const val AUDIOS_FOLDER_NAME="AUDIOS"
        //other files
        const val OTHERS_FOLDER_NAME="OTHERS"

        var ManageAllFilePermissionRequestCode: Int? = 2296

        val Images_EXT= arrayOf("png","jpg","bmp","webp","heif","heic","gif","tiff","tif","svg","jpeg")
        val Videos_Ext=arrayOf("mp4","3gp","mkv","webm","wmv","flv","mpg","mpeg","mov","avi")
        val Audios_EXT= arrayOf("opus","mp3","ogg","ota","aac","m4a","oga","wav","wma","flac")
        val Doc_EXT= arrayOf("doc","docx","zip","rar","ppt","pptx","pptm","pps","ppsm","ppsx","xls","xlsx","pdf","psd","ai","eps","txt", "csv", "rtf", "odt", "md","apk","aab")

        val FILE_TYPE_IMAGE="IMAGE"
        val FILE_TYPE_VIDEO="VIDEO"
        val FILE_TYPE_AUDIO="AUDIO"
        val FILE_TYPE_DOCUMENT="DOC"
        val FILE_TYPE_OTHER="OTHER"
        const val GB_WHATSAPP_PACKAGE_NAME="com.gbwhatsapp"
        const val WHATSAPP_PACKAGE_NAME="com.whatsapp"
        const val WHATSAPP_BUSINESS_PACKAGE_NAME="com.whatsapp.w4b"
        const val MESSENGER_PACKAGE_NAME="com.facebook.orca"
        const val MESSENGER_LITE_PACKAGE_NAME="com.facebook.mlite"
        const val MAIL_LITE_PACKAGE_NAME="com.google.android.gm.lite"
        const val MAIL_PACKAGE_NAME="com.google.android.gm"

        const val FILE_TYPE_KEY="FILE_TYPE_KEY"
        const val CHAT_TYPE_KEY="CHAT_TYPE_KEY"
        const val PROFILE_NAME_KEY="PROFILE_NAME"

        const val DOC_FILE_TYPE="DOCFILE"
        const val MEDIA_FILE_TYPE="MEDIAFILE"
        const val IMAGE_FILE_TYPE="IMAGEFILE"
        const val VIDEO_FILE_TYPE="VIDEOFILE"
        const val AUDIO_FILE_TYPE="AUDIOFILE"
        const val PDF_FILE_TYPE="PDFFILE"
        const val WORD_FILE_TYPE="WORDFILE"
        const val OTHER_FILE_TYPE="OTHERFILE"

        const val TYPE_WHATSAPP="WHATSAPP"
        const val TYPE_MESSENGER="MESSENGER"
        const val TYPE_MAIL="MAIL"

        const val ONGOING_NOTIFICATION_ID=92727

        const val STORAGE_PERMISSION_REQUEST_CODE=7867
        const val PERMISSION_SETTINGS_STORAGE=7378
        const val NOTIFICATION_SETTINGS=9876
        var rootDir=""

        var PERMISSIONS_REQUSTCODE2 = 937


    }
}