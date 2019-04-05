package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;

import com.example.taopr.soool.Model.QnaDrawUpModel;
import com.example.taopr.soool.Object.QnaBoardItem;

public class QnaDrawUpPresenter implements QnaDrawUpInter {

    private Activity activity;
    private QnaDrawUpPresenter.View view;
    private QnaDrawUpModel qnaDrawUpModel;

    public QnaDrawUpPresenter(Activity activity, Context context) {
        this.activity = activity;
        this.qnaDrawUpModel = new QnaDrawUpModel(this, context);
    }

    @Override
    public void setView(QnaDrawUpPresenter.View view) {
        this.view = view;
    }
    // QnaActivity로부터 게시물 관련 데이터들을 객체로 받아서 서버로 저장하는 함수.
    @Override
    public void enrollmentReq(QnaBoardItem item) {
        qnaDrawUpModel.enrollmentReqFromView(item);
    }
}
