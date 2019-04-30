package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.QnaBoardItem;

import java.util.ArrayList;

public interface MypageInter {
    void setView(MypagePresenter.View view);
    void uploadSchedule();

    // 마이페이지 북마크와 내 게시물 뷰로 보내는 함수

    public interface View{
        void getInfoBookmarkResponse(ArrayList<InfoOfSoool> infoOfSoools);
        void getQnaMyboardResponse(ArrayList<QnaBoardItem> qnaBoardItems);
        void getDataFail(boolean response, int i);
    }
}
