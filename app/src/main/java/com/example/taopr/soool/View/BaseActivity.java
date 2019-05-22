package com.example.taopr.soool.View;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
    private int tabMenu,previousTab;
    private String PREVIOUS_TAB_KEY = "previousTab";
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
                tabMainImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_home_on));
                tabMainText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                break;
            case 1:
                btn_tabInfo.setClickable(false);
                tabInfoImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_info_on));
                tabInfoText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                break;
            case 2:
                btn_tabQna.setClickable(false);
                tabQnaImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_qna_on));
                tabQnaText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                break;
            case 3:
                btn_tabMypage.setClickable(false);
                tabMypageImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_mypage_on));
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


    // 왼쪽 탭에서 오른쪽 탭 이동시
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tabMain:
                intent = new Intent(mActivity, MainActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(PREVIOUS_TAB_KEY,tabMenu);
                startActivity(intent);
                break;

            case R.id.tabInfo:
                intent = new Intent(mActivity, InfoActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(PREVIOUS_TAB_KEY,tabMenu);
                startActivity(intent);
                break;

            case R.id.tabQna:
                intent = new Intent(mActivity, QnaActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(PREVIOUS_TAB_KEY,tabMenu);
                startActivity(intent);

                break;

            case R.id.tabMypage:

                intent = new Intent(mActivity, MyPageActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(PREVIOUS_TAB_KEY,tabMenu);
                startActivity(intent);

                break;

        }
    }
    // leftRight = 0 일 경우 왼쪽탭으로 이동
    // leftRight = 1 일 경우 오른쪽탭으로 이동
    private void moveTab(int leftRight){
        switch (leftRight){
            case 0:
                Log.i(TAG, "moveTab: leftTab");
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;
            case 1:
                Log.i(TAG, "moveTab: rightTab");
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        previousTab = intent.getIntExtra(PREVIOUS_TAB_KEY,0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (tabMenu){
            case 0:
                moveTab(0);
                break;
            case 1:
                if(previousTab==0) moveTab(1);
                else moveTab(0);
                break;
            case 2:
                if (previousTab==0 || previousTab==1) moveTab(1);
                else moveTab(0);
                break;
            case 3:
                moveTab(1);
                break;
        }
    }

    // 액션바 커스텀
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
        View actionbar = inflater.inflate(R.layout.main_actionbar, null);

        actionBar.setCustomView(actionbar);

        // 그림자 없애기
        actionBar.setElevation(0);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        TextView mainActionBarTitle = findViewById(R.id.mainActionBarTitle);
        ImageView myPageDrawerButton= findViewById(R.id.myPageDrawerButton);

        switch (tabMenu){
            case 0:
                mainActionBarTitle.setText(R.string.home_label_home_tab);
                break;
            case 1:
                mainActionBarTitle.setText(R.string.info_label_info_tab);
                break;
            case 2:
                mainActionBarTitle.setText(R.string.qna_label_qna_tab);
                break;
        }
        myPageDrawerButton.setImageDrawable(null);

       // activateSaveButton();

        return true;
    }
}
