package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.Object.MypageBookmarkItem;
import com.example.taopr.soool.Object.MypageMyboardItem;
import com.example.taopr.soool.Object.QnaBoardItem;

import java.util.ArrayList;

public interface MypageInter {
    void setView(MypagePresenter.View view);
    void uploadSchedule();

    // 마이페이지 북마크와 내 게시물 뷰로 보내는 함수

    public interface View{
        void getBookmarkResponse(ArrayList<MypageBookmarkItem> mypageBookmarkItems);
        void getMyboardResponse(ArrayList<MypageMyboardItem> mypageMyboardItems);
        void getQnaMyboardResponse(ArrayList<QnaBoardItem> qnaBoardItems);
    }
}
