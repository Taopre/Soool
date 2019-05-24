package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.CalendarItemMap;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.UserProfile;
import com.example.taopr.soool.Presenter.MypageFmPresenter;

import java.util.ArrayList;

public interface MypageFmInter {
    void setView(MypageFmPresenter.View view);
    void loadMypageData(int accountNo, int i);

    public interface View{
        void getInfoBookmarkResponse(ArrayList<InfoOfSoool> infoOfSoools);
        void getQnaMyboardResponse(ArrayList<QnaBoardItem> qnaBoardItems);
        //  void getCalendarDataResponse(ArrayList<CalendarItem> calendarItems);
        void getCalendarDataResponse(CalendarItemMap calendarItemMap);
        void getDataFail(boolean response, int i);
        void getUserProfileResponse(UserProfile userProfile);
    }
}
