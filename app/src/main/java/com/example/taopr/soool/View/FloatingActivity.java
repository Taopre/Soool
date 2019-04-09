package com.example.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.taopr.soool.R;


public class FloatingActivity extends AppCompatActivity implements View.OnClickListener {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_default, fab_qnaBoard, fab_qnaVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_fab_btn);
        DoBinding();
        anim();
    }

    private void DoBinding() {
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);;

        // 뷰들 선언하는 부분입니다.
        fab_default = (FloatingActionButton) findViewById(R.id.fab_default);
        fab_qnaBoard = (FloatingActionButton) findViewById(R.id.fab_qnaBoard);
        fab_qnaVote = (FloatingActionButton) findViewById(R.id.fab_qnaVote);

        // 뷰의 리스너 선언 부분입니다.
        fab_default.setOnClickListener(this);
        fab_qnaBoard.setOnClickListener(this);
        fab_qnaVote.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_default:
                anim();

                Intent intent = new Intent(this, QnaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                finish();
                break;
            case R.id.fab_qnaBoard:
                anim();

                Intent intent1 = new Intent(this, QnaBoardActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
            case R.id.fab_qnaVote:
                anim();

                Intent intent2 = new Intent(this, QnaVoteActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
        }
    }

    public void anim() {
        if (isFabOpen) {
            fab_qnaBoard.startAnimation(fab_close);
            fab_qnaVote.startAnimation(fab_close);
            fab_qnaBoard.setClickable(false);
            fab_qnaVote.setClickable(false);
            isFabOpen = false;
        } else {
            fab_qnaBoard.startAnimation(fab_open);
            fab_qnaVote.startAnimation(fab_open);
            fab_qnaBoard.setClickable(true);
            fab_qnaVote.setClickable(true);
            isFabOpen = true;
        }
    }
}
