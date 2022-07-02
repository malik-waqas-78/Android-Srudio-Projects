package com.zak.clone.zakconstants

import com.zak.clone.zakmodelclasses.ZakFileSharingModel


class ZakMyConstants {

    companion object get {
        val KEY_FOLDER_NAME: String="folder_key"
        val CALEDAR_PERMISSION_REQUEST_CODE: Int=2277
        val CONTACTS_PERMISSIONS_REQUEST_CODE = 2263
        val SPRC: Int=7772
        val TESTING_TAG: String="72729"
        val STORAGE_PERMISSION = 7876
        val INSTALL_APPS = 4678
        val WIFI_TURN_ON = 9434
        val byteArraySize = 8192
        val animationTime = 1500L
        val STORE_CONTACTS = 7827
        val RESTART_JOIN=7378

        val CALENDAR = "CALENDAR"
        val CONTACTS = "CONTACTS"
        val CALLLOGS = "CALLLOGS"
        val PICS = "PICS"
        val VIDEOS = "VIDEOS"
        val APPS = "APPS"
        val Audios="AUDIOS"
        val Docments="DOCUMENTS"

        val FILE_TYPE="FILE_TYPE"
        val IS_SELECTED="IS_SELECTED"

        val FILE_TYPE_CALENDAR="Calendar"
        val FILE_TYPE_AUDIOS="Audios"
        val FILE_TYPE_DOCS="Documents"
        val FILE_TYPE_CONTACTS = "Contacts"
        val FILE_TYPE_CALLLOGS = "Call Log"
        val FILE_TYPE_PICS = "Images"
        val FILE_TYPE_VIDEOS = "Videos"
        val FILE_TYPE_APPS = "Applications"

        val SELCTION_ACTIVITY=7343

        val FOLDERNAME = "Phone Cloner"

        val isContactsSaved = "isContactsSaved"

        val FILES_TO_SHARE=ArrayList<ZakFileSharingModel>()
        val EventsFileName="CalendarEvents.csv"
        val ContactsFileName="Contacts.csv"



        val START_SENDING_DATA: String = "STARTSENDINGF"
        val SEND_NEXT: String = "SENDNEXT"
        val DETAILINFO: String = "DETAILINFO"
        val SEPERATOR = "<~?~>"
        val PERMISSIONS_REQUSTCODE = 927
        val LOCATION_PERMISIION_REQUEST_CODE = 5622
        val REQUEST_LOCATION_TURNON = 199
        val CAMERA_PERMISSIONS_REQUEST_CODE = 2263
        val HOTOSPOT_SETTINGS_CODE = 4687
        val LAUNCH_NEW_PHONE_ACTIVITY = 6397
        val LAUNCH_OLD_PHONE_ACTIVITY = 6537
        val DEVICES_CONNECTED = "CONNECTED"
        val STARTING_TO_SEND_DATA = "STARTINGTOSENDDATA"
        val FINISH_ACTIVITY_CODE = 3464
        val RECEIVER_IS_READY_To_Receive_Events = "RECEIVERISREADYTORECEIVEEVENTS"
        val RECEIVER_IS_READY_To_Receive_CALLLOGS = "RECEIVERISREADYTORECEIVECALLLOGS"
        val RECEIVER_IS_READY_To_Receive_CONTACTS = "RECEIVERISREADYTORECEIVECONTACTS"
        val RECEIVER_IS_READY_To_Receive_IMAGES = "RECEIVERISREADYTORECEIVEIMAGES"
        val RECEIVER_IS_READY_To_Receive_VIDEOS = "RECEIVERISREADYTORECEIVEVIDEOS"
        val RECEIVER_IS_READY_To_Receive_APKS = "RECEIVERISREADYToReceiveAPKS"

        val FINISHED = "FINISHED"
        val TAG = "92727"

        val EVENTS_ARE_COMING = "EVENTSARECOMING"
        val CONTACTS_ARE_COMING = "CONTACTSARECOMING"
        val CALLLOGS_OR_COMING = "CALLLOGSORCOMING"
        val IMAGES_OR_COMING = "IMAGESORCOMING"
        val VIDEOS_ARE_COMING = "VIDEOSARECOMING"
        val APKS_ARE_COMMING = "APKSARECOMMING"

        val BLUETOOTH_REQUEST_CODE = 2588

        val INFORM_USER_ABOUT_THE_ITEMS_COMMING = "ITEMSCOMING"

        const val ANDROIDR_PERMISSION=7263
    }
}