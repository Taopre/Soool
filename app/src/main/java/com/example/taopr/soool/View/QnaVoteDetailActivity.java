package com.example.taopr.soool.View;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.R;

public class QnaVoteDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "QnaVoteDetailActivity";

//    TextView tv_qnaboardTag, tv_qnaboardTitle, tv_qnaboardWriter, tv_qnaboardContent, tv_qnaboardDate, tv_qnaboardCommentCount, tv_qnaboardViewCount;
//    ImageView iv_qnaboardImage;
//    Button btn_qnaboardCommentShow;

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
//        tv_qnaboardTag = findViewById(R.id.qnaboardTag);
//        tv_qnaboardTitle = findViewById(R.id.qnaboardTitle);
//        tv_qnaboardWriter = findViewById(R.id.qnaboardWriter);
//        tv_qnaboardContent = findViewById(R.id.qnaboardContent);
//        tv_qnaboardDate = findViewById(R.id.qnaboardDate);
//        tv_qnaboardCommentCount = findViewById(R.id.qnaboardCommentCount);
//        tv_qnaboardViewCount = findViewById(R.id.qnaboardViewCount);

        // 뷰의 리스너 선언 부분입니다.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.qna_detail_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        ImageButton ib_drawupBack = findViewById(R.id.drawupBack);
        TextView tv_drawupTitle = findViewById(R.id.drawupTitle);
        Button btn_drawupReport = findViewById(R.id.drawupReport);

        tv_drawupTitle.setText("Board");
        ib_drawupBack.setOnClickListener(this);
        btn_drawupReport.setOnClickListener(this);

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawupBack:
                break;
            case R.id.drawupEnroll:
                break;
        }
    }
}
