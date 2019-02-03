package com.crycetruly.keepsafeconsellor.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ava on 8/21/2017.
 */

public class GetCurTime {
    public static String getCurTime() {
        return DateFormat.getDateTimeInstance().format(new Date());

    }

    public static String todayDate() {
 /*       Calendar calendar=Calendar.getInstance();
        System.out.println(calendar.getTime());
       String time =calendar.getTime().toString();
     String finalTime= time.substring(0,12);
     return finalTime;
       *//* SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
       String time= simpleDateFormat.format(calendar.getTime());
        return time;*//*
    }*/
        String currentDate = DateFormat.getDateTimeInstance().format(new Date());
        String justTime = currentDate.substring(0, 12);
        return justTime;


    }

    //--------------CONVERTTS TIMESTAMP TO HUMAN READANLE DATE
    //TODO FIND OUT HOUR TO CHANGE TO 12HR AND AND CONTEXT
    public static String toDateTime(long value) {
        Timestamp timestamp = new Timestamp(value);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        return simpleDateFormat.format(timestamp);
    }

    public static String today() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getReversedNow() {
        Calendar c = Calendar.getInstance();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
//
//        String current = sdf.format(c.getTime());

        String input = String.valueOf(System.currentTimeMillis());
        String input2 = input.substring(0, 9);

        // getBytes() method to convert string
        // into bytes[].
        byte[] strAsByteArray = input2.getBytes();

        byte[] result
                = new byte[strAsByteArray.length];

        // Store result in reverse order into the
        // result byte[]
        for (int i = 0; i < strAsByteArray.length; i++) {
            result[i]
                    = strAsByteArray[strAsByteArray.length - i - 1];
        }

        return new String(result);
    }

}
