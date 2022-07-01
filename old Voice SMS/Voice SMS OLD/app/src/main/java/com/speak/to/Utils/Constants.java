package com.speak.to.Utils;

import com.speak.to.Models.Lang_Item;
import com.speak.to.Models.Model_Class;
import com.speak.to.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {

    // Permissions Code
    public static final int AUDIO_RECORD_PRM_CODE = 3500;

    // Navigation Drawer Resource IDs
    public static final int NAV_DRAWER_NEW_ID = R.id.nav_new;
    public static final int NAV_DRAWER_OPEN_ID = R.id.nav_open_message;
    public static final int NAV_DRAWER_SAVE_ID = R.id.nav_save_message;
    public static final int NAV_DRAWER_DELETE_ALL_ID = R.id.nav_delete_all;
    public static final int NAV_DRAWER_SHARE_ID = R.id.nav_share;
    public static final int NAV_DRAWER_RATE_ID = R.id.nav_rate_us;

    // Bottom Nav bar Resource IDs
    public static final int NAV_BOTTOM_COPY_ID = R.id.bottom_nav_copy;
    public static final int NAV_BOTTOM_IMAGE_ID = R.id.bottom_nav_image;
    public static final int NAV_BOTTOM_DOCS_ID = R.id.bottom_nav_documents;
    public static final int NAV_BOTTOM_SAVE_ID = R.id.bottom_nav_save;

    // Bottom Nav Bar Voice Search IDs
    public static final int VOICE_NAV_BOTTOM_CLEAR_ID = R.id.bottom_nav_clear_all;
    public static final int VOICE_NAV_BOTTOM_SEARCH_ID = R.id.bottom_nav_search;

    // Send Messages Activity Package Names
    public static final String MESSAGES_PACKAGE_NAME = "com.google.android.apps.messaging";
    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    public static final String MESSENGER_PACKAGE_NAME = "com.facebook.orca";
    public static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
    public static final String VIBER_PACKAGE_NAME = "com.viber.voip";
    public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";

    public static final String KEY_TXT_MESSAGE = "text_message";
    public static final String KEY_OPEN_MESSAGE = "open_message";
    public static final String URL = "url";
    public static final String NAME = "name";
    //Request Codes
    public static final int OPEN_MESSAGE_ACTIVITY_REQUEST_CODE = 1221;
    public static final int REQUEST_RECORD_AUDIO_PERMISSION = 1222;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1223;
    public static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1224;
    public static final int SELECT_DOCS_REQUEST_CODE = 1225;
    // Locale Values
    public static final Lang_Item[] locale_array_list = new Lang_Item[]{
            new Lang_Item("English", "en_001", R.drawable.united_states_of_america),
            new Lang_Item("Urdu", "ur_PK", R.drawable.pakistan),
            new Lang_Item("Hindi", "hi_IN", R.drawable.india),
            new Lang_Item("Pashto", "ps_AF", R.drawable.afghanistan),
            new Lang_Item("Persian", "fa_IR", R.drawable.iran),
            new Lang_Item("Arabic", "ar_001", R.drawable.saudi_arabia),
            new Lang_Item("Turkish", "tr_TR", R.drawable.turkey),
            new Lang_Item("Chinese", "zh_CN", R.drawable.china),
            new Lang_Item("Bengali", "bn_BD", R.drawable.bangladesh),
            new Lang_Item("German", "de_DE", R.drawable.germany),
            new Lang_Item("Afrikaans", "af_ZA", R.drawable.south_africa),
            new Lang_Item("Azerbaijan", "az_AZ", R.drawable.azerbaijan),
            new Lang_Item("Albanian", "sq_AL", R.drawable.albania),
            new Lang_Item("Armenian", "hy_AM", R.drawable.armenia),
            new Lang_Item("Belarusian", "be_BY", R.drawable.belarus),
            new Lang_Item("Burmese", "my_MM", R.drawable.myanmar),
            new Lang_Item("Bulgarian", "BG_bg", R.drawable.bulgaria),
            new Lang_Item("Bosnian", "bs_BA", R.drawable.bosnia_and_herzegovina),
            new Lang_Item("Welsh", "cy_GB", R.drawable.united_kingdom),
            new Lang_Item("Hungarian", "hu_HU", R.drawable.hungary),
            new Lang_Item("Galician", "gl_ES", R.drawable.spain),
            new Lang_Item("Dutch", "nl_NL", R.drawable.netherlands),
            new Lang_Item("Greek", "el_GR", R.drawable.greece),
            new Lang_Item("Georgian", "ka_GE", R.drawable.georgia),
            new Lang_Item("Danish (Denmark)", "da_DK", R.drawable.denmark),
            new Lang_Item("Danish (Greenland)", "da_GL", R.drawable.greenland),
            new Lang_Item("Indonesian", "in_ID", R.drawable.indonesia),
            new Lang_Item("Malay", "ms_SG", R.drawable.singapore),
            new Lang_Item("Irish", "ga_IE", R.drawable.ireland),
            new Lang_Item("Italian", "it_IT", R.drawable.italy),
            new Lang_Item("Icelandic", "is_IS", R.drawable.iceland),
            new Lang_Item("Japanese", "ja_JP", R.drawable.japan),
            new Lang_Item("Spanish", "es_ES", R.drawable.spain),
            new Lang_Item("Kazakh", "kk_KZ", R.drawable.kazakhstan),
            new Lang_Item("Catalan", "ca_AD", R.drawable.andorra),
            new Lang_Item("Kyrgyz", "ky_KG", R.drawable.kyrgyzstan),
            new Lang_Item("Croatian", "hr_HR", R.drawable.croatia),
            new Lang_Item("Czech", "cs_CZ", R.drawable.czech_republic),
            new Lang_Item("Korean", "ko_KP", R.drawable.north_korea),
            new Lang_Item("Khmer", "km_KH", R.drawable.cambodia),
            new Lang_Item("Latvian", "lv_LV", R.drawable.latvia),
            new Lang_Item("Lithuanian", "lt_LT", R.drawable.lithuania),
            new Lang_Item("Malagasy", "mg_MG", R.drawable.madagascar),
            new Lang_Item("Malayalam", "ml_IN", R.drawable.india),
            new Lang_Item("Maltese", "mt_MT", R.drawable.malta),
            new Lang_Item("Macedonian", "mk_MK", R.drawable.republic_of_macedonia),
            new Lang_Item("Marathi", "mr_IN", R.drawable.india),
            new Lang_Item("Mongolian", "mn_MN", R.drawable.mongolia),
            new Lang_Item("Nepali", "ne_NP", R.drawable.nepal),
            new Lang_Item("Punjabi", "pa_PK", R.drawable.pakistan),
            new Lang_Item("Polish", "pl_PL", R.drawable.poland),
            new Lang_Item("Portuguese", "pt_PT", R.drawable.portugal),
            new Lang_Item("Romanian", "ro_RO", R.drawable.romania),
            new Lang_Item("Russian", "ru_RU", R.drawable.russia),
            new Lang_Item("Serbian", "sr_RS", R.drawable.serbia),
            new Lang_Item("Sinhala", "si_LK", R.drawable.sri_lanka),
            new Lang_Item("Slovenian", "sl_SI", R.drawable.slovenia),
            new Lang_Item("Swahili", "sw_KE", R.drawable.kenya),
            new Lang_Item("Swedish", "sv_SE", R.drawable.sweden),
            new Lang_Item("Thai", "th_TH", R.drawable.thailand),
            new Lang_Item("Tamil", "ta_IN", R.drawable.india),
            new Lang_Item("Telugu", "te_IN", R.drawable.india),
            new Lang_Item("Uzbek", "uz_UZ", R.drawable.uzbekistn),
            new Lang_Item("Ukrainian", "uk_UA", R.drawable.ukraine),
            new Lang_Item("Finnish", "fi_FI", R.drawable.finland),
            new Lang_Item("French", "fr_FR", R.drawable.france)
    };
    // Shared Pref Helper Values
    public static final String SHARED_PREF_VOICE_SMS_NAME = "shared_preference_for_voice_sms_";
    public static final String SELECTED_LOCALE = "selected_locale_for_speech_recognition";
    public static final int DEFAULT_LOCALE = 0; // English International

    // Constants for Search List Options
    public static final String SEARCH_SHOPPING = "Search for Shopping";
    public static final String SEARCH_OTHERS = "Search in Others";
    public static final String SEARCH_WEB = "Web Search";
    public static final String SEARCH_SOCIAL = "Search on Social Apps";
    public static final String SEARCH_COMMUNICATION = "Search in Community";
    public static final String SEARCH_MODE = "Search_Mode";
    public static final String SEARCH_SOURCE_NAME = "Website Source";
    public static final String SEARCH_SOURCE_ICON = "Website ICON";
    public static final String SEARCH_QUERY = "Search Query";

    // IDs for Search Websites
    // Shopping
    public static final int AMAZON = R.string.amazon;
    public static final int ALIBABA = R.string.alibaba;
    public static final int DARAZ = R.string.daraz;
    public static final int OLX = R.string.olx;
    public static final int EBAY = R.string.ebay;
    public static final int ALIEXPRESS = R.string.aliexpress;
    // Search Engines
    public static final int GOOGLE = R.string.google;
    public static final int BING = R.string.bing;
    public static final int DUCK = R.string.duckduckgo;
    public static final int YAHOO = R.string.yahoo;
    public static final int WIKI = R.string.wikipedia;
    // Social Apps
    public static final int FACEBOOK = R.string.facebook;
    public static final int INSTAGRAM = R.string.app_instagram;
    public static final int TWITTER = R.string.app_twitter;
    public static final int YOUTUBE = R.string.youtube;
    public static final int TIKTOK = R.string.tiktok;
    public static final int PINTEREST = R.string.pinterest;
    // Others
    public static final int MEDIUM = R.string.medium;
    public static final int STACK_OVERFLOW = R.string.stack_overflow;
    public static final int IMDB = R.string.imdb;
    public static final int GOOGLE_MAPS = R.string.maps;
    // Community
    public static final int REDDIT = R.string.reddit;
    public static final int QUORA = R.string.quora;
    public static final int FLIPBOARD = R.string.flipboard;
    public static final int MSN = R.string.msn;


    // List Constants
    public static int Max_files = 10;
    public static int Files_Count = 0;
    public static ArrayList<Model_Class> files_list = new ArrayList<>();
    public static HashMap<String, String> app_values = new HashMap<>();
}
