package com.lpky.taopr.soool.Presenter.Interface;

import com.lpky.taopr.soool.Object.BoardRecommend;
import com.lpky.taopr.soool.Object.QnaVoteItem;
import com.lpky.taopr.soool.Presenter.QnaDetailPresenter;

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
