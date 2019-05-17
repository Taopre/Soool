package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Presenter.CalendarSchPresenter;
import com.example.taopr.soool.Presenter.InfoPresenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface CalendarSchInter {


    void setView(CalendarSchPresenter.View view);

    void eventDelete(Date selectedDay, HashMap<String,CalendarItem> calendarItemMap);


    public interface View{
        void getDataSuccess(HashMap<String,CalendarItem> calendarItemMap);

        void getDataFail(String message);
    }
}
