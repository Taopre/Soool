package com.lpky.taopr.soool.Presenter;

import android.app.Activity;
import android.util.Log;

import com.lpky.taopr.soool.Calendar.CalendarDB;
import com.lpky.taopr.soool.Object.CalendarItem;
import com.lpky.taopr.soool.Object.CalendarItemMap;
import com.lpky.taopr.soool.Presenter.Interface.CalendarFmInter;

import java.util.ArrayList;

public class CalendarFmPresenter extends BasePresenter implements CalendarFmInter{
    private final String TAG = "마이페이지 캘린더 프래그먼트";
    private View view;

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void getCalendarItem(Activity mActivity, String accountNo) {
        Log.i(TAG, "getCalendarItem: ");
        CalendarDB calendarDB = new CalendarDB(mActivity);
        ArrayList<CalendarItem> calendarItems = new ArrayList<CalendarItem>(calendarDB.eventSelect(accountNo));

        // HashMap<이벤트 작성 날짜(key) : calendar 객체(value)>  구조를 가진 CalendarItemMap 객체에 추가한다.
        // HashMap 으로 만든 이유는 캘린더에 날짜 선택 시 선택한 날짜에 작성한 내용을 보기위해서는 선택한 날짜 값으로
        // select 을 하기 위해서

        CalendarItemMap calendarItemMap = new CalendarItemMap();

        for(int a=0; a<calendarItems.size(); a++){
            calendarItemMap.put(calendarItems.get(a).getEventDate(),calendarItems.get(a));
        }

        view.getCalendarDataResponse(calendarItemMap);
    }
}
