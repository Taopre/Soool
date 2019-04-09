package com.example.taopr.soool.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.R;

public class QnaVoteDetailActivity extends AppCompatActivity {

    String TAG = "QnaVoteDetailActivity";

    QnaVoteItem qnaVoteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnavote_detail);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지

        qnaVoteItem = (QnaVoteItem) getIntent().getSerializableExtra("QnaVoteItem");
//        Log.d(TAG, "onCreate: "+qnaVoteItem.getTitle()+qnaVoteItem.getContent()+qnaVoteItem.getFirst_image() +qnaVoteItem.getSecond_image());
        // 값이 넘어왔으므로 이제 뷰들 만들어서 넣어주는 작업하면 될거같다.
    }

    private void DoBinding() {
//        qnaBoardPresenter = new QnaBoardPresenter(this, this);
//        qnaBoardPresenter.setView(this);

        // 뷰들 선언하는 부분입니다.


        // 뷰의 리스너 선언 부분입니다.

    }
}
