package com.example.taopr.soool.View;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.R;

import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;


// 탭 부분을 BaseActivity에서 선언(바인딩, 클릭리스너)을 하고, 상속을 받게 하기 위해서

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    public Activity mActivity;
    ViewGroup btn_tabMain, btn_tabInfo, btn_tabQna, btn_tabMypage;
    private String TAG = "베이스 액티비티 ";
    private int tabMenu;
    ImageView tabMainImage , tabInfoImage, tabQnaImage, tabMypageImage;
    TextView tabMainText, tabInfoText, tabQnaText, tabMypageText;


    // tab의 메뉴별로 각각의 처리를 추가로 해주기 위해 tabMenu를 추가로 받음
    // 0 = main , 1 = info , 2 = qna , 3 = mypage
    // 현재 페이지로 이동하는 탭 아이콘 색 변경
    // 같은 페이지 이동 막기 위해서 onClick에서 TabMenu 값으로 예외처리

    // view를 받아오는 부분
    public void setContentView(@LayoutRes int layoutResID,int tabMenu) {
        super.setContentView(layoutResID);
        mActivity = this;
        this.tabMenu = tabMenu;
        viewBinding();
        tabSetting();
        ButterKnife.bind(this);

    }

    // 같은 페이지 이동 막기 위해서 버튼 비활성화
    // 추가로 나중에 같은 페이지로 이동하는 버튼의 아이콘 변경
    private void tabSetting() {
        switch (tabMenu){
            case 0:
                btn_tabMain.setClickable(false);
                tabMainImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_home_off));
                tabMainText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                break;
            case 1:
                btn_tabInfo.setClickable(false);
                tabInfoImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_home_off));
                tabInfoText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                break;
            case 2:
                btn_tabQna.setClickable(false);
                tabQnaImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_home_off));
                tabQnaText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                break;
            case 3:
                btn_tabMypage.setClickable(false);
                tabMypageImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_home_off));
                tabMypageText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                break;
        }
    }

    private void viewBinding(){
        btn_tabMain = findViewById(R.id.tabMain);
        btn_tabInfo = findViewById(R.id.tabInfo);
        btn_tabQna = findViewById(R.id.tabQna);
        btn_tabMypage = findViewById(R.id.tabMypage);
        tabMainImage = findViewById(R.id.tabMainImage);
        tabMainText = findViewById(R.id.tabMainText);
        tabInfoImage = findViewById(R.id.tabInfoImage);
        tabInfoText = findViewById(R.id.tabInfoText);
        tabQnaImage = findViewById(R.id.tabQnaImage);
        tabQnaText = findViewById(R.id.tabQnaText);
        tabMypageImage = findViewById(R.id.tabMypageImage);
        tabMypageText = findViewById(R.id.tabMypageText);

        btn_tabMain.setOnClickListener(this);
        btn_tabInfo.setOnClickListener(this);
        btn_tabQna.setOnClickListener(this);
        btn_tabMypage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tabMain:
                intent = new Intent(mActivity, MainActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;

            case R.id.tabInfo:
                intent = new Intent(mActivity, InfoActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;

            case R.id.tabQna:
                intent = new Intent(mActivity, QnaActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;

            case R.id.tabMypage:
                intent = new Intent(mActivity, MyPageActivity.class);

                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

        }
    }
}
