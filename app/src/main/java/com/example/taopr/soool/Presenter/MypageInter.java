package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Object.CalendarItemMap;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.UserProfile;

import java.util.ArrayList;

public interface MypageInter {
    void setView(MypagePresenter.View view);
    void loadMypageData(int accountNo, int i);

    // 마이페이지 북마크와 내 게시물 뷰로 보내는 함수

    public interface View{
        void getInfoBookmarkResponse(ArrayList<InfoOfSoool> infoOfSoools);
        void getQnaMyboardResponse(ArrayList<QnaBoardItem> qnaBoardItems);
      //  void getCalendarDataResponse(ArrayList<CalendarItem> calendarItems);
      void getCalendarDataResponse(CalendarItemMap calendarItemMap);
        void getDataFail(boolean response, int i);
        void getUserProfileResponse(UserProfile userProfile);
    }
}
