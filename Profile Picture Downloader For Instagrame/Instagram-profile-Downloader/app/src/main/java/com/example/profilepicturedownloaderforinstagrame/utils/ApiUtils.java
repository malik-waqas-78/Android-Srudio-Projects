package com.example.profilepicturedownloaderforinstagrame.utils;

public class ApiUtils {
    public static final String BASE_URL = "https://instagram.com/";
    public static final String search_url="https://api.instagram.com/v1/users/search?q=[";
    public static String getUsernameUrl(String username){
        return BASE_URL + username + "/";
    }
    public static String getUsersSugestions(String userName,String clientId){
        return  search_url+userName+"]&client_id=["+clientId+"]";
    }
}
