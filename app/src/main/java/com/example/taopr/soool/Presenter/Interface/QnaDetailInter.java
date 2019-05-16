package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;

public interface QnaDetailInter {

    void setView(QnaDetailPresenter.View view);

    void downloadVoteData(int accountNo, int postNo);

    void updateVoteResult(int accountNo, int postNo, int selectNum);

    public interface View {
        void getDataSuccess(QnaVoteItem qnaVoteItem);
        void getDataFail(String message);
    }
}
