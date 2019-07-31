package com.lpky.taopr.soool.Presenter.Interface;

import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.Object.QnaItem;
import com.lpky.taopr.soool.Presenter.QnaBoardPresenter;

public interface QnaBoardInter {
    // Presenter 부분쪽에 있는 함수.

    void setView(QnaBoardPresenter.View view);
    // QnaDrawUpActivity로 부터 객체를 QnaDrawUpModel로 보내주는 중간다리 함수.
    void enrollmentBoardReq(QnaItem qnaItem);

    void modifyBoardReq(QnaBoardItem qnaBoardItem);

    void enrollmentBoardResp(int response, QnaBoardItem qnaBoardItem);

    void modifyBoardResp(int response, QnaBoardItem qnaBoardItem);

    void deleteBoardReq(int postNo);

    void deleteBoardResp(int response);

    //-----------------------------------------------------------------------------------

    // View쪽에 있는 함수.
    public interface View {
        void enrollmentBoardRespGoToView(int response, QnaBoardItem qnaBoardItem);
        void modifyBoardRespGoToView(int response, QnaBoardItem qnaBoardItem);
        void deleteBoardRespGoToView(int response);
    }
}
