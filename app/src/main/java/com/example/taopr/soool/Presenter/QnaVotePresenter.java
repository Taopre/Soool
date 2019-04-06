package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;

import com.example.taopr.soool.Model.QnaVoteModel;
import com.example.taopr.soool.Object.QnaVoteItem;

public class QnaVotePresenter implements QnaVoteInter {

    private Activity activity;
    private QnaVotePresenter.View view;
    private QnaVoteModel qnaVoteModel;

    public QnaVotePresenter(Activity activity, Context context) {
        this.activity = activity;
        this.qnaVoteModel = new QnaVoteModel(this, context);
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void enrollmentVoteReq(QnaVoteItem item) {
        qnaVoteModel.enrollmentVoteReqFromView(item);
    }

    @Override
    public void enrollmentVoteResp(boolean response) {
        view.enrollmentVoteRespGoToView(response);
    }
}
