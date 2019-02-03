package com.crycetruly.keepsafeconsellor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Elia on 2/3/2018.
 */

public class Preferences {

    public static String getLocality(Context context){
        SharedPreferences preferences=context.getSharedPreferences("locality",MODE_PRIVATE);
        String loc=preferences.getString("locality","");
        return loc;


    }
}
