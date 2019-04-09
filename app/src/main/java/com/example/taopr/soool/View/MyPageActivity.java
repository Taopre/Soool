package com.example.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.taopr.soool.R;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_tabMain, btn_tabInfo, btn_tabQna, btn_tabMypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지

    }

    private void DoBinding() {
//        qandAPreseneter = new QandAPreseneter(this, this);
//        qandAPreseneter.setView(this);

        // 뷰들 선언하는 부분입니다.
        btn_tabMain = findViewById(R.id.tabMain);
        btn_tabInfo = findViewById(R.id.tabInfo);
        btn_tabQna = findViewById(R.id.tabQna);
        btn_tabMypage = findViewById(R.id.tabMypage);

        // 뷰의 리스너 선언 부분입니다.
        btn_tabMain.setOnClickListener(this);
        btn_tabInfo.setOnClickListener(this);
        btn_tabQna.setOnClickListener(this);
        btn_tabMypage.setOnClickListener(this);
    }

    // 탭별로 애니메이션 상황 맞게 바꿔줘야 할듯.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tabMain:
                startActivity(new Intent(MyPageActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
                break;

            case R.id.tabInfo:
                startActivity(new Intent(MyPageActivity.this,  InfoActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
                break;

            case R.id.tabQna:
                startActivity(new Intent(MyPageActivity.this, QnaActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
                break;

            case R.id.tabMypage:
                startActivity(new Intent(MyPageActivity.this,  MyPageActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
                break;
        }
    }
}
