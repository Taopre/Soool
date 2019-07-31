package com.lpky.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;

import com.lpky.taopr.soool.Calendar.CalendarDB;
import com.lpky.taopr.soool.Calendar.SooolCalendar;
import com.lpky.taopr.soool.Object.CalendarItem;
import com.lpky.taopr.soool.Presenter.Interface.CalendarSchInter;

import java.util.Date;
import java.util.HashMap;

public class CalendarSchPresenter implements CalendarSchInter {

    private Activity activity;
    private CalendarSchPresenter.View view;
    private Context context;
    private SooolCalendar sooolCalendar = new SooolCalendar();
    private CalendarDB calendarDB;
    private String accountNo;

    public CalendarSchPresenter(Activity activity, Context context,
                                String accountNo){
        this.context = context;
        this.activity = activity;
        this.calendarDB = new CalendarDB(activity);

        this.accountNo = accountNo;
    }

    // 삭제하기 버튼 클릭 시
    // 디비에서 선택한 날짜의 이벤트 내용을 삭제
    // 삭제한 날짜의 이벤트를 hashMap(calendarItemMap) 에서도 삭제

    @Override
    public void eventDelete(Date selectedDay,HashMap<String,CalendarItem> calendarItemMap){
        String selectedDayString = sooolCalendar.getDateFormat(selectedDay);
        calendarDB.eventDelete(accountNo, selectedDayString);
        calendarItemMap.remove(selectedDayString);

        view.getDataSuccess(calendarItemMap);
    }

    @Override
    public void setView(CalendarSchPresenter.View view) {
        this.view = view;
    }


}
