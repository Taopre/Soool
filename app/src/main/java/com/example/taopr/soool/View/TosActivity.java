package com.example.taopr.soool.View;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taopr.soool.R;

public class TosActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView subActionBarLeftImage ;
    private TextView subActionBarRight;
    private TextView subActionBarLeft;
    private TextView subActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();


        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
        actionBar.setElevation(0);

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.sub_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        subActionBarLeftImage = findViewById(R.id.subActionBarLeftImage);
        subActionBarRight = findViewById(R.id.subActionBarRight);
        subActionBarLeft = findViewById(R.id.subActionBarLeft);
        subActionBarTitle = findViewById(R.id.subActionBarTitle);

        subActionBarLeft.setVisibility(View.GONE);
        subActionBarRight.setVisibility(View.INVISIBLE);

        subActionBarTitle.setVisibility(View.VISIBLE);
        subActionBarTitle.setText(getString(R.string.drawer_tos));

        subActionBarLeftImage.setVisibility(View.VISIBLE);
        subActionBarLeftImage.setOnClickListener(this);

        return true;
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
