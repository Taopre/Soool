package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.Object.QnaBoardItem;

public interface QnaDrawUpInter {
    // Presenter 부분쪽에 있는 함수.

    void setView(QnaDrawUpPresenter.View view);
    // QnaDrawUpActivity로 부터 객체를 QnaDrawUpModel로 보내주는 중간다리 함수.
    void enrollmentReq(QnaBoardItem item);

    //-----------------------------------------------------------------------------------

    // View쪽에 있는 함수.
    public interface View {

    }
}
