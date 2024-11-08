package com.example.learnjava;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class Manager_Cache {

    private static Manager_Cache managerCache;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private Manager_Cache(Context context) {
        sharedPreferences = context.getSharedPreferences(Config_cacheData.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static Manager_Cache getPreferences(Context context) {
        if (managerCache == null) managerCache = new Manager_Cache(context);
        return managerCache;
    }

    /**
     * getters and setters
     */

    public void setUserID(String userID){
        editor.putString(Config_cacheData.USER_ID, userID);
        editor.apply();
    }

    public String getUserID(){
        return sharedPreferences.getString(Config_cacheData.USER_ID, "");
    }

    public void setUserEmail(String userEmail){
        editor.putString(Config_cacheData.USER_EMAIL, userEmail);
        editor.apply();
    }

    public String getUserEmail(){
        return sharedPreferences.getString(Config_cacheData.USER_EMAIL, "");
    }

    public void setUserFName(String userFName){
        editor.putString(Config_cacheData.USER_FNAME, userFName);
        editor.apply();
    }

    public String getUserFName(){
        return sharedPreferences.getString(Config_cacheData.USER_FNAME, "");
    }

    public void setUserLName(String userLName){
        editor.putString(Config_cacheData.USER_LNAME, userLName);
        editor.apply();
    }

    public String getUserLName(){
        return sharedPreferences.getString(Config_cacheData.USER_LNAME, "");
    }

    public void setUserMobile(String userMobile){
        editor.putString(Config_cacheData.USER_MOBILE, userMobile);
        editor.apply();
    }

    public String getUserMobile(){
        return sharedPreferences.getString(Config_cacheData.USER_MOBILE, "");
    }

    public void setUserPassword(String userPassword){
        editor.putString(Config_cacheData.USER_PASSWORD, userPassword);
        editor.apply();
    }

    public String getUserPassword(){
        return sharedPreferences.getString(Config_cacheData.USER_PASSWORD, "");
    }

    public void setUserImage(Uri userImage){
        editor.putString(Config_cacheData.USER_IMAGE, String.valueOf(userImage));
        editor.apply();


    }

    public Uri getUserImage(){
        return Uri.parse(sharedPreferences.getString(Config_cacheData.USER_IMAGE, ""));
    }

}
