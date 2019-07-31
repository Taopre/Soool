package com.lpky.taopr.soool.Presenter.Interface;

import android.app.Activity;

import com.lpky.taopr.soool.Object.CalendarItemMap;
import com.lpky.taopr.soool.Presenter.CalendarFmPresenter;

public interface CalendarFmInter {

    void setView(CalendarFmPresenter.View view);
    public void getCalendarItem(Activity mActivity, String accountNo);

    public interface View{
        void getCalendarDataResponse(CalendarItemMap calendarItemMap);
        void getDataFail(boolean response, int i);
    }
}
