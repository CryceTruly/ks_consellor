package com.crycetruly.keepsafeconsellor.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Elia on 9/9/2017.
 */

public class Handy {

    public static int nowTime(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        String today= simpleDateFormat.format(calendar.getTime());
        return Integer.parseInt(today);
    }

    public static int today2yearsAgoTime(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        String today= simpleDateFormat.format(calendar.getTime());


        return Integer.parseInt(today);
    }
    private static final String TAG = "Handy";
    //----------------SORTING HING ISSUES ----------------------


    public static long fitnessNumber() {
        long x = System.currentTimeMillis();

        long y = -x;


        return y;
    }

    public static Double fitnessPoint(Double d) {
        Double x = d;

        Double y = -x;


        return y;
    }


    //--------------------REFRESHES THE GALLERY TO SHOW THE SAVED PCTURES--------------------//
    public static void scanFile(Context context, Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }

    //--------------------REFRESHES THE GALLERY TO SHOW THE SAVED PCTURES--------------------//
    public static boolean isAlphnuemeric(String string) {
        return string.matches("[A-Za-z0-9]+");


    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //----------------CAPITALISES LETTERS-----------JUST LIKE PHP UCWORDS--------------//
    public static String getTrimmedName(String sentence) {

        String c = (sentence != null) ? sentence.trim() : "";
        String[] words = c.split(" ");
        String result = "";
        for (String w : words) {
            result += (w.length() > 1 ? w.substring(0, 1).toUpperCase(Locale.US) + w.substring(1, w.length()).toLowerCase(Locale.US) : w) + " ";
        }
        return result.trim();

    }

    //------------CAPITALISES FIRST,ONCE IN SENTENCE
    public static String capitalize(final String line) {

        String c = (line != null) ? line.trim() : "";
        try {
            return Character.toUpperCase(c.charAt(0)) + c.substring(1);
        } catch (StringIndexOutOfBoundsException e) {
            return " ";
        }
    }

    //returns token to send to db when user turns on notifications
    public static String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }
    // Create a helper method called isNetworkAvailable() that will return true or false depending upon whether the network is available or not. It will look something like this

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            Log.d(TAG, "isNetworkAvailable: Device is connecte to internet");
            isAvailable = true;
        } else {
            Log.d(TAG, "isNetworkAvailable: Device is not connected");
        }
        return isAvailable;
    }


    public static boolean checkActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(TAG, "Error: ", e);
            }
        } else {
            Log.d(TAG, "No network present");
        }
        return false;
    }

    //------------------RETURNS THE CURRENT DISTRICT ELSE THE ---------------------//
    public static String getDistrict(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("district", Context.MODE_PRIVATE);
        return sharedPreferences.getString("district", "");
    }
public static int getVersionCode(Context context){
    Log.d(TAG, "getVersionCode: ");
    int code = 0;
        try {
            PackageInfo packageInfo=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            code=packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
}
}
