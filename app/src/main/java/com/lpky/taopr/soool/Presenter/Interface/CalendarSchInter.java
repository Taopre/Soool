package com.lpky.taopr.soool.Presenter.Interface;

import com.lpky.taopr.soool.Object.CalendarItem;
import com.lpky.taopr.soool.Presenter.CalendarSchPresenter;

import java.util.Date;
import java.util.HashMap;

public interface CalendarSchInter {


    void setView(CalendarSchPresenter.View view);

    void eventDelete(Date selectedDay, HashMap<String,CalendarItem> calendarItemMap);


    public interface View{
        void getDataSuccess(HashMap<String,CalendarItem> calendarItemMap);

        void getDataFail(String message);
    }
}
