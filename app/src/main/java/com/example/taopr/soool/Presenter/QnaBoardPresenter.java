package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;

import com.example.taopr.soool.Model.QnaBoardModel;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.Interface.QnaBoardInter;

public class QnaBoardPresenter implements QnaBoardInter {

    private Activity activity;
    private QnaBoardPresenter.View view;
    private QnaBoardModel qnaBoardModel;

    public QnaBoardPresenter(Activity activity, Context context) {
        this.activity = activity;
        this.qnaBoardModel = new QnaBoardModel(this, context);
    }

    @Override
    public void setView(QnaBoardPresenter.View view) {
        this.view = view;
    }

    // QnaActivity로부터 게시물 관련 데이터들을 객체로 받아서 서버로 저장하는 함수.
    @Override
    public void enrollmentBoardReq(QnaBoardItem item, QnaVoteItem qnaVoteItem, QnaItem qnaItem) {
        qnaBoardModel.enrollmentBoardReqFromView(item, qnaVoteItem, qnaItem);
    }

    // 모델의 응답을 넘겨주는 메서드
    @Override
    public void enrollmentBoardResp(int response, int vote, int voteStatus, QnaItem qnaItem) {
        view.enrollmentBoardRespGoToView(response, vote, voteStatus, qnaItem);
    }
}
