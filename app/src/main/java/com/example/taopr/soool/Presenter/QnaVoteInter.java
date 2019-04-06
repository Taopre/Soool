package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.Object.QnaVoteItem;

public interface QnaVoteInter {
    // Presenter 부분쪽에 있는 함수.

    void setView(QnaVotePresenter.View view);

    // 뷰에서 모델로 객체를 전달하기 위해 만든 메서드

    void enrollmentVoteReq(QnaVoteItem item);

    // 모델에서 뷰로 응답을 전달하기 위해 만든 메서드

    void enrollmentVoteResp(boolean response);

    //-----------------------------------------------------------------------------------

    // View쪽에 있는 함수.
    public interface View {
        void enrollmentVoteRespGoToView(boolean response);
    }
}
