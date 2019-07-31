package com.lpky.taopr.soool.Calendar;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SooolCalendar {
    private static final String TAG = "Soool 캘린더";
    private static String monthYearPattern = "yyyyMMdd";
    private static String dayOfWeekPattern ="dd";


    // string 변수 타입인 날짜를 Date 변수타입으로 변환하기 위한 함수
    public Date getDate(String eventDate){
        int year,month,date;
        year = Integer.parseInt(eventDate.substring(0,4));
        month = Integer.parseInt(eventDate.substring(4,6))-1;  // month는 값이 0일 때 1월
        date = Integer.parseInt(eventDate.substring(6,8));

        Log.i(TAG, "getDate: " + year +"  " + month +"  " + date);
        Calendar cal = Calendar.getInstance();

        cal.set(year,month,date,0,0);
        return cal.getTime();
    }

    // Date 변수타입인 날짜를 String 으로 변환하기 위한 함수
    public String getDateFormat(Date selectedDay){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(monthYearPattern);

        String dateFormat = simpleDateFormat.format(selectedDay);

        return dateFormat;
    }

    public String getDayOfWeek(Date selectedDay){
        String dayOfWeek=null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDay);
        int dayOfWeekInt = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeekInt){
            case 1:
                dayOfWeek = "Sunday";
                break;
            case 2:
                dayOfWeek = "Monday";
                break;
            case 3:
                dayOfWeek = "Tuesday";
                break;
            case 4:
                dayOfWeek = "Wednesday";
                break;
            case 5:
                dayOfWeek = "Thursday";
                break;
            case 6:
                dayOfWeek = "Friday";
                break;
            case 7:
                dayOfWeek = "Saturday";
                break;

        }
        return dayOfWeek;
    }
}
