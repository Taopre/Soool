package com.example.taopr.soool.Presenter.Interface;

import android.content.Intent;

import com.example.taopr.soool.Object.BoardRecommend;
import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;

import java.util.ArrayList;

public interface QnaDetailInter {

    void setView(QnaDetailPresenter.View view);

    void downloadVoteData(int accountNo, int postNo);

    void updateVoteResult(int accountNo, int postNo, int selectNum);

    void recommendOnOffReq(int accountNo, int postNo, int likeType, int btnOnOff);


    public interface View
    {
        void getDataSuccess(QnaVoteItem qnaVoteItem);
        void getDataFail(String message);
        void recommendComplete(boolean flag, BoardRecommend boardRecommend);
        void updateVoteResultComplete(boolean flag, QnaVoteItem qnaVoteItem);

    }
}
