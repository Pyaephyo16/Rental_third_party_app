package com.example.rentalforu.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Util {

    public static void showToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void saveData(Context context,String key,String value){
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edt = sh.edit();
        edt.putString(key,value);
        edt.apply();
    }

    public static String getData(Context context,String key){
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sh.getString(key,"null");
        if(data!=null){
            return data;
        }else{
            return "";
        }
    }


}
