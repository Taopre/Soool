package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Presenter.CalendarAddSchPresenter;


import java.util.Date;
import java.util.HashMap;

public interface CalendarAddSchInter {

    void setView(CalendarAddSchPresenter.View view);

    void eventSave(String eventTextContent,Date selectedDay,HashMap<String,CalendarItem> calendarItemMap);


    public interface View{
        void getDataSuccess(HashMap<String,CalendarItem> calendarItemMap);

        void getDataFail(String message);
    }

}
