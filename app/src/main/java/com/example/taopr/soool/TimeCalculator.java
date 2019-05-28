package com.example.taopr.soool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeCalculator {

    public String beforeTime(Date date){

        Calendar c = Calendar.getInstance();

        long now = c.getTimeInMillis();
        long dateM = date.getTime();

        long gap = now - dateM;

        String ret = "";

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
        else if(sec > 0){
            ret = "방금 전";
        }

        return ret;

    }
}
