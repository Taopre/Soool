package com.example.taopr.soool.View;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.taopr.soool.Dialog.NoticeDialog;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.ProfileInfo;
import com.example.taopr.soool.Presenter.HomePresenter;
import com.example.taopr.soool.Presenter.Interface.HomeInter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.View.HomeFragment.InfoFragment;
import com.example.taopr.soool.View.HomeFragment.MainFragment;
import com.example.taopr.soool.View.HomeFragment.MypageFragment;
import com.example.taopr.soool.View.HomeFragment.QnaFragment;
import com.example.taopr.soool.View.MyPageFragment.BookmarkFragment;
import com.example.taopr.soool.View.MyPageFragment.CalendarFragment;
import com.example.taopr.soool.View.MyPageFragment.MyBoardFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity implements HomePresenter.View , MyBoardFragment.MyPageView{

    @BindView(R.id.tabMain)
    ViewGroup btn_tabMain;
    @BindView(R.id.tabInfo)
    ViewGroup btn_tabInfo;
    @BindView(R.id.tabQna)
    ViewGroup btn_tabQna;
    @BindView(R.id.tabMypage)
    ViewGroup btn_tabMypage;
    @BindView(R.id.HomeDrawerButton)
    ImageView HomeDrawerButton;
    @BindView(R.id.mainActionBarTitle)
    TextView mainActionBarTitle;
    @BindView(R.id.myPageDrawerEmail)
    TextView myPageDrawerEmail;
    @BindView(R.id.myPageDrawerNickname)
    TextView myPageDrawerNickname;
    @BindView(R.id.myPageDrawerLayout)
    DrawerLayout mypageDrawerLayout;

    private String TAG = "홈 액티비티 ";
    // 현재 탭, 이전 탭
    // 0 -> main , 1 -> info
    // 2 -> qna , 3 -> mypage
    // 이전탭 현재탭이 필요한 이유 애니메이션 때문
    private int currentTab=0,previousTab=1;

    private String PREVIOUS_TAB_KEY = "previousTab";
    private final int CHANGE_PROFILE = 4400;
    ImageView tabMainImage , tabInfoImage, tabQnaImage, tabMypageImage;
    TextView tabMainText, tabInfoText, tabQnaText, tabMypageText;

    QnaFragment qnaFragment = null;
    MainFragment mainFragment = null;
    InfoFragment infoFragment = null;
    MypageFragment mypageFragment = null;
    FragmentTransaction transaction = null;

    private int accountNo;
    private HomePresenter homePresenter;
    private NoticeDialog noticeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //액션바 숨기기
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.bind(this);

        homePresenter = new HomePresenter(this);

        homePresenter.setView(this);
        homePresenter.getAccountNo();


        // DrawerLayout 은 Home 액티비티의 마이페이지 프래그먼트에서 우측 상단의 메뉴 아이콘 클릭 시에만 생성
        // 하지만 Home 액티비티의 하위 프래그먼트에서 화면 우측 면에서 왼쪽으로 스와이프 할 경우 DrawerLayout 이 나타남
        // 그래서 하위 프래그먼트에서 스와이프로 DrawerLayout 생기는 것을 막음
        mypageDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        viewBinding();
        callFragment(0,1);
        tabSetting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void viewBinding() {
        tabMainImage = findViewById(R.id.tabMainImage);
        tabMainText = findViewById(R.id.tabMainText);
        tabInfoImage = findViewById(R.id.tabInfoImage);
        tabInfoText = findViewById(R.id.tabInfoText);
        tabQnaImage = findViewById(R.id.tabQnaImage);
        tabQnaText = findViewById(R.id.tabQnaText);
        tabMypageImage = findViewById(R.id.tabMypageImage);
        tabMypageText = findViewById(R.id.tabMypageText);
    }

    // 프래그먼트 변경시 액티비티에 프래그먼트를 부착하고 탈착하는 액션에 애니메이션을 부여
    private void callFragment(int currentTabNo,int previousTabNo){
        currentTab = currentTabNo;
        previousTab = previousTabNo;

        // 프래그먼트 사용을 위해
        // onCreate 에서 한번만 생성하려 했을때는 에러
        // 그 이유는 아직 모름
        transaction = getSupportFragmentManager().beginTransaction();

        switch (currentTab){
            case 0:
                if(mainFragment == null) mainFragment = new MainFragment();
               // transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                transaction.replace(R.id.homeFragmentContainer, mainFragment);
                break;
            case 1:
                // 정보 탭에서는 이전탭이 홈 탭일 경우, 오른쪽에 들어오는
                //              이전탭이 커뮤니티와 마이페이지 탭일 경우,
                if (infoFragment== null) infoFragment = new InfoFragment();

              /*  if (previousTabNo == 0) transaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                else transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);*/

                transaction.replace(R.id.homeFragmentContainer, infoFragment);

                break;
            case 2:
                if (qnaFragment == null ){
                    qnaFragment = new QnaFragment();
                    Log.i(TAG, "callFragment: null");
                }
                /*if (previousTabNo ==3 ) transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                else transaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);*/

                transaction.replace(R.id.homeFragmentContainer, qnaFragment);
                break;
            case 3:
                if (mypageFragment == null) mypageFragment = new MypageFragment();
               // transaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

                transaction.replace(R.id.homeFragmentContainer, mypageFragment);
                break;

        }
        transaction.commit();
    }

    @OnClick({R.id.tabMain,R.id.tabInfo,R.id.tabQna,R.id.tabMypage})
    public void tabClick(View view){
        switch (view.getId()){
            case R.id.tabMain:
                callFragment(0,currentTab);
                break;
            case R.id.tabInfo:
               // currentTab = 1;
                callFragment(1,currentTab);
                break;
            case R.id.tabQna:
               // currentTab = 2;
                callFragment(2,currentTab);
                break;
            case R.id.tabMypage:
              //  currentTab = 3;
                callFragment(3,currentTab);
                break;
        }
        tabSetting();
    }

    // DrawerLayout 제어
    @OnClick(R.id.HomeDrawerButton)
    public void drawerButtonClick(){
        DrawerLayout mypageDrawerLayout = findViewById(R.id.myPageDrawerLayout) ;

        if (!mypageDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mypageDrawerLayout.openDrawer(Gravity.RIGHT);


        }
    }

    // DrawerLayout 탭 클릭
    @OnClick({R.id.myPageDrawerMyAccount,R.id.myPageDrawerLogOut,R.id.myPageDrawerHandlingPrivacy,R.id.myPageDrawerPush,R.id.myPageDrawerTos
    ,R.id.myPageDrawerVersion,R.id.myPageDrawerReport,R.id.myPageDrawerElseTab})
    public void drawerTabOnClick(View view){
        switch (view.getId()) {
            case R.id.myPageDrawerMyAccount:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("accountNo", accountNo);
                startActivityForResult(intent, CHANGE_PROFILE);
                break;
            case R.id.myPageDrawerLogOut:
                //setLogout();

                noticeDialog = new NoticeDialog(HomeActivity.this,getString(R.string.notice_dialog_logout_title),
                        getString(R.string.notice_dialog_logout_content),false,getString(R.string.all_button_ok),
                        getString(R.string.all_button_cancel),positiveListener,negativeListener);
                noticeDialog.show();

                break;

        }

    }
    private View.OnClickListener positiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            noticeDialog.dismiss();
            setLogout();
        }
    };

    private View.OnClickListener negativeListener = new View.OnClickListener() {
        public void onClick(View v) {
            noticeDialog.dismiss();
        }
    };


    private void setLogout() {
        LoginSharedPreferences.LoginUserDelete(HomeActivity.this,"LoginAccount");
        Intent intent = new Intent(HomeActivity.this, StartingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // 마이페이지 프래그먼트에서 프로필 정보를 가져오는데 성공했을 때
    // Drawer 레이아웃 뷰에서 닉네임과 이메일의 변경 된 값을 보여준다
    // Home 액티비티에 붙어있는 마이페이지 프래그먼트의 변경된 프로필 정보를 변경한다

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CHANGE_PROFILE:
                    ProfileInfo profileInfo = data.getParcelableExtra("profileInfo");

                    // drawer 레이아웃에 바뀐 프로필 정보 보여주기
                    myPageDrawerNickname.setText(profileInfo.getAccountNick());
                    myPageDrawerEmail.setText(profileInfo.getAccountEmail());


                    // 마이페이지 프래그먼트에 변경된 프로필 내용으로 보여주기
                    mypageFragment.showProfileImgNick(profileInfo.getAccountImage(),profileInfo.getAccountNick());
                    break;
            }
        }
    }

    // 같은 페이지 이동 막기 위해서 버튼 비활성화
    // 추가로 나중에 같은 페이지로 이동하는 버튼의 아이콘 변경
    private void tabSetting() {
        switch (currentTab){
            case 0:
                btn_tabMain.setClickable(false);
                tabMainImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_home_on));
                tabMainText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                mainActionBarTitle.setText(R.string.main_tab_label_home);
                break;
            case 1:
                btn_tabInfo.setClickable(false);
                tabInfoImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_info_on));
                tabInfoText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                mainActionBarTitle.setText(R.string.main_tab_label_info);
                break;
            case 2:
                btn_tabQna.setClickable(false);
                tabQnaImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_qna_on));
                tabQnaText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                mainActionBarTitle.setText(R.string.main_tab_label_qna);
                break;
            case 3:
                btn_tabMypage.setClickable(false);
                tabMypageImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_mypage_on));
                tabMypageText.setTextColor(ContextCompat.getColor(this,R.color.greenDark));
                mainActionBarTitle.setText(R.string.main_tab_label_my_page);
                HomeDrawerButton.setVisibility(View.VISIBLE);
                break;
        }
        switch (previousTab){
            case 0:
                btn_tabMain.setClickable(true);
                tabMainImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_home_off));
                tabMainText.setTextColor(ContextCompat.getColor(this,R.color.grayMain));
                break;
            case 1:
                btn_tabInfo.setClickable(true);
                tabInfoImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_info_off));
                tabInfoText.setTextColor(ContextCompat.getColor(this,R.color.grayMain));
                break;
            case 2:
                btn_tabQna.setClickable(true);
                tabQnaImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_qna_off));
                tabQnaText.setTextColor(ContextCompat.getColor(this,R.color.grayMain));
                break;
            case 3:
                btn_tabMypage.setClickable(true);
                tabMypageImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.nav_mypage_off));
                tabMypageText.setTextColor(ContextCompat.getColor(this,R.color.grayMain));
                HomeDrawerButton.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void getAccountNoSuc(int accountNo) {
        this.accountNo = accountNo;
    }

    // 마이페이지의 자식 프래그먼트로 서버에 요청을 보내고, 응답을 받았다는 내용을 마이페이지에 전달한다
    @Override
    public void startMyBoardLoading() {
        // 내 게시물에서 서버에 요청을 보냄
        mypageFragment.waitChildFragmentRes();
    }

    @Override
    public void endMyBoardLoading(Boolean isMyBoardRes) {
        // 내 게시물에서 서버로부터 응답을 받음
        mypageFragment.getChildFragmentRes(0,isMyBoardRes);
    }


    // MyBoard 프래그먼트에서 마이페이그 프래그먼트 view 에서 '내 게시글' ,'내 포인트' 값의 갱신이 필요하다는 알림을 보내는 메서드
    // Home 액티비티에서는 MyBoard 프래그먼트에서 알림을 받으면 마이페이지 프래그먼트에 전달한다
    @Override
    public void updateProfileForMyBoard() {
        Log.i(TAG, "updateProfileForMyBoard: ");
        if (mypageFragment != null){
            mypageFragment.updateProfile();
        }
    }


}
