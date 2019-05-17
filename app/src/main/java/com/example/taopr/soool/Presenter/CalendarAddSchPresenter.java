package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.taopr.soool.Calendar.CalendarDB;
import com.example.taopr.soool.Calendar.SooolCalendar;
import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Presenter.Interface.CalendarAddSchInter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class CalendarAddSchPresenter implements CalendarAddSchInter {
    private Activity activity;
    private CalendarAddSchPresenter.View view;
    private Context context;
    private SooolCalendar sooolCalendar = new SooolCalendar();
    private CalendarDB calendarDB;
    private String accountNo;

    public CalendarAddSchPresenter(Activity activity, Context context ,
                                String accountNo){
        this.context = context;
        this.activity = activity;
        this.calendarDB = new CalendarDB(activity);

        this.accountNo = accountNo;
    }
    @Override
    public void setView(CalendarAddSchPresenter.View view) {
        this.view = view;
    }

    // 날짜는 'yyyymmdd' 형식으로 저장


    // 저장하기 버튼을 클릭 시
    // 작성한 데이터가 없을 경우 예외처리( 버튼 비활성화를 통해 예외처리 )
    // 이전에 작성했던 내용이 있다면 디비에 업데이트, 작성했던 내용이 없다면 디비에 추가해준다
    // 디비에 저장 시  '유저 닉네임' 이란 테이블에 작성한 데이터와 선택한 날짜를 데이터베이스에 저장한다
    // 선택한 날짜를 'yyyymmdd' 변환해서 디비에 저장
    // 저장한 후에 작성한 내용 hashMap에 추가


    @Override
    public void eventSave(String eventTextContent,Date selectedDay,HashMap<String,CalendarItem> calendarItemMap){
        String selectedDayString = sooolCalendar.getDateFormat(selectedDay);

        // 추가하기
        if(calendarItemMap.get(selectedDayString) == null) {
            calendarDB.eventInsert(accountNo, selectedDayString, eventTextContent);
        }

        // 수정하기
        else {
            calendarDB.eventUpdate(accountNo, selectedDayString, eventTextContent);

        }

        calendarItemMap.put(selectedDayString,new CalendarItem(selectedDayString,eventTextContent));
        view.getDataSuccess(calendarItemMap);
    }
}
