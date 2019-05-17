package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaBoardPresenter;

public interface QnaBoardInter {
    // Presenter 부분쪽에 있는 함수.

    void setView(QnaBoardPresenter.View view);
    // QnaDrawUpActivity로 부터 객체를 QnaDrawUpModel로 보내주는 중간다리 함수.
    void enrollmentBoardReq(QnaBoardItem item, QnaVoteItem qnaVoteItem, QnaItem qnaItem);

    void enrollmentBoardResp(int response, int vote, int voteStatus, QnaItem qnaItem);

    //-----------------------------------------------------------------------------------

    // View쪽에 있는 함수.
    public interface View {
        void enrollmentBoardRespGoToView(int response, int vote, int voteStatus, QnaItem qnaItem);
    }
}
