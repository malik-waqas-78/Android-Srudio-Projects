package com.example.profilepicturedownloaderforinstagrame.models;
import android.util.Log;
public class ModelClass {
    private static final String TAG = "malik";
    String name,username;
    String profile_imge;
public ModelClass(String  name , String userName,String profile_imge){
    this.name=name;
    this.username=userName;
    this.profile_imge=profile_imge;
}
public ModelClass(){
}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Log.d("TAG", "setName: "+name);
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
        Log.d("TAG", "setUsername: "+username);
    }
    public String getProfile_imge() {
        return profile_imge;
    }
    public void setProfile_imge(String profile_imge) {
        this.profile_imge = profile_imge;
        Log.d(TAG, "setProfile_imge: "+profile_imge);
    }
}