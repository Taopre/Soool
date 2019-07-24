package com.example.taopr.soool;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeCalculator {
    private final String TAG = "큐앤에이 시간 계산";
    private static String monthYearPattern = "yyyyMMddHHmmss";

    public String getbeforeTime(String dateString){

        Calendar c = Calendar.getInstance();


        long now = c.getTimeInMillis();
        long dateM = getLongDate(dateString);

        long gap = now - dateM;

        String ret = "";
        Log.i(TAG, "beforeTime: " + now + "  "+ "  "+dateM + "  " + gap );

//        초       분   시
//        1000    60  60
        gap = (long)(gap/1000);
        long hour = gap/3600;
        gap = gap%3600;
        long min = gap/60;
        long sec = gap%60;

        if(hour >= 24){
            long day = hour / 24;
            if(day >= 30){
                long month = day / 30;
                if (month >= 24){
                    long year = month / 24;
                    ret = String.valueOf(year) + "년 전";
                }
                else{
                    ret = String.valueOf(month) + "달 전";
                }

            }
            else{
                ret = String.valueOf(day) + "일 전";
            }
        }
        else if(hour > 0){
            ret = String.valueOf(hour)+"시간 전";
        }
        else if(min > 0){
            ret = String.valueOf(min)+"분 전";
        }
        else{
            ret = "방금 전";
        }

        return ret;

    }


    // string 으로된 날짜 값을 Date 변수로 변환
    public Long getLongDate(String dateString){
        int year,month,date,hour,min,sec;
        year = Integer.parseInt(dateString.substring(0,4));
        month = Integer.parseInt(dateString.substring(5,7))-1;  // month는 값이 0일 때 1월
        date = Integer.parseInt(dateString.substring(8,10));
        hour =  Integer.parseInt(dateString.substring(11,13));
        min =  Integer.parseInt(dateString.substring(14,16));
        sec =  Integer.parseInt(dateString.substring(17,19));


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(monthYearPattern);
        Calendar cal = Calendar.getInstance();


        cal.set(year,month,date,hour,min,sec);


        return cal.getTimeInMillis();
    }
}