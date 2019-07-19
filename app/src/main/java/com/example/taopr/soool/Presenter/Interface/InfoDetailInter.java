package com.example.taopr.soool.Presenter.Interface;

import android.content.Intent;

import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.InfoContentText;
import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.InfoDetailPresenter;

import java.util.ArrayList;

public interface InfoDetailInter {

    /*
    * 1. getIntent로 Fragment에서 보낸 리스트 받아오기
    * 2. Content + Bookmark 데이터 서버에서 받아오기 (postNo, accountNo 보내기)
    * 3. setView
    * 4. Bookmark 버튼 누르면 서버에 bookmark 업데이트 요청 보내기
    * 5. 공유하기 처리 (postNo)
    * 5. 댓글 관련 부분 */

    void setView(InfoDetailPresenter.View view);

    void getAdditionalData(int accountNo, int postNo);

    void getExistingData();

    void getIntentFromActivity(InfoItem infoItem);

    void updateBookmarkStatus();

    public interface View {
        // TODO : getData(success/fail), create dynamic Views, update bookmark

       /*
        * 1. getDataSuccess / getDataFail
        * 2. updateChanges

        */

       void getDataSuccess(ArrayList<InfoContentText> infoText, int bookmarkNo, boolean hasBookmarked);
       void getDataFail(String message);
       void getSessionData(int accountNo, String formattedTime);
       void notifyBookmarkChange(int postNo, int bookmarkNo, boolean hasBookmarked);
       void showLoading();
       void hideLoading();
       void backToFragments();
       boolean updateBookmarkView(boolean flag, int bookmarkCount);

    }

}
