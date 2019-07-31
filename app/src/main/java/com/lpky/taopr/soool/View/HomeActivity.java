package com.lpky.taopr.soool.View;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lpky.taopr.soool.Dialog.NoticeDialog;
import com.lpky.taopr.soool.Object.ProfileInfo;
import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.Presenter.HomePresenter;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.lpky.taopr.soool.View.HomeFragment.InfoFragment;
import com.lpky.taopr.soool.View.HomeFragment.MainFragment;
import com.lpky.taopr.soool.View.HomeFragment.MypageFragment;
import com.lpky.taopr.soool.View.HomeFragment.QnaFragment;
import com.lpky.taopr.soool.View.MyPageFragment.BookmarkFragment;
import com.lpky.taopr.soool.View.MyPageFragment.MyBoardFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity implements HomePresenter.View,
        MyBoardFragment.MyPageView, BookmarkFragment.BookmarkView,QnaFragment.QnaFmView
        ,MainFragment.MainFmView {

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
    @OnClick({R.id.myPageDrawerMyAccount,R.id.myPageDrawerLogOut,R.id.myPageDrawerNotice,R.id.myPageDrawerHandlingPrivacy,
            R.id.myPageDrawerPush,R.id.myPageDrawerTos,R.id.myPageDrawerVersion,R.id.myPageDrawerReport,R.id.myPageDrawerElseTab})
    public void drawerTabOnClick(View view){
        Intent intent;
        switch (view.getId()) {
            // 내 계정
            case R.id.myPageDrawerMyAccount:
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("accountNo", accountNo);
                startActivityForResult(intent, CHANGE_PROFILE);
                break;
            // 공지사항
            case R.id.myPageDrawerNotice:
                intent = new Intent(HomeActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;

            // 이용약관
            case R.id.myPageDrawerTos:
                intent = new Intent(this, TosActivity.class);
                startActivity(intent);
                break;

            case R.id.myPageDrawerHandlingPrivacy:
                intent = new Intent(this,HandlingPrivacyActivity.class);
                startActivity(intent);
                break;

            //버전정보
            case R.id.myPageDrawerVersion:
                noticeDialog = new NoticeDialog(this, getString(R.string.soool_version),getString(R.string.all_button_ok),positiveListener);
                noticeDialog.show();
                break;

                //이용약관
            //
            //로그 아웃
            case R.id.myPageDrawerLogOut:
                noticeDialog = new NoticeDialog(HomeActivity.this,getString(R.string.notice_dialog_logout_title),
                        getString(R.string.notice_dialog_logout_content),false,getString(R.string.all_button_ok),
                        getString(R.string.all_button_cancel),logOutPositiveListener,negativeListener);
                noticeDialog.show();
                break;
            //문의 및 신고
            case R.id.myPageDrawerReport:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] sooolEmail = {getString(R.string.declaration_soool_email)};
                email.putExtra(Intent.EXTRA_EMAIL, sooolEmail);
                email.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.declaration_email_title));
                startActivity(email);
                break;
        }

    }
    // 로그 아웃 다이얼로그 '확인' 버튼 리스너
    private View.OnClickListener logOutPositiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            noticeDialog.dismiss();
            setLogout();
        }
    };

    private View.OnClickListener positiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            noticeDialog.dismiss();
        }
    };

    private View.OnClickListener negativeListener = new View.OnClickListener() {
        public void onClick(View v) {
            noticeDialog.dismiss();
        }
    };


    private void setLogout() {
        LoginSharedPreferences.LoginUserLogOut(HomeActivity.this,"LoginAccount");
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

    // MyBoard 프래그먼트에서 마이페이지 프래그먼트 view 에서 '내 게시글' ,'내 포인트' 값의 갱신이 필요하다는 알림을 보내는 메서드
    // Home 액티비티에서는 MyBoard 프래그먼트에서 알림을 받으면 마이페이지 프래그먼트에 전달한다
    @Override
    public void updateProfileForMyBoard() {
        if (mypageFragment != null){
            Log.i(TAG, "updateProfileForMyBoard: ");
            mypageFragment.updateProfile();
        }
    }


    @Override
    public void waitingForResponse() {

        // to inform myPageFragment that its childFragment, bookmarkFragment
        // has sent a request to server and waiting for the response
        mypageFragment.waitChildFragmentRes();

    }

    @Override
    public void receivedResponse(Boolean serverResponded) {

        // to inform myPageFragment that its childFragment, bookmarkFragment
        // has received the response from server
        mypageFragment.getChildFragmentRes(1,serverResponded);

    }

    @Override
    public void updateAccountInfo() {
        // if there has been any change in bookmarkFragment concerning userProfile
        if (mypageFragment != null) {
            mypageFragment.updateProfile();
        }
    }

    // 커뮤니티 글이나 정보 글이 업데이트 되었을 경우
    // 커뮤니티 글이 업데이트( 추가,수정,삭제 ) 될 경우
    // Main, Qna, MyPage 의 MyBoard 페이지의 리스트를 업데이트해주기 위해서
    // 각각 프래그먼트에서 업데이트 정보를 Home 액티비티로 전달을 하고 Home 액티비티에서는
    // 모든 페이지에서 업데이트한 아이템을 수정해준다

    // qna 페이지에서 업데이트가 있으면 Main 페이지와 MyPage 페이지의 리스트에서 업데이트된 아이템을 수정

    @Override
    public void qnaUpdateItem(QnaBoardItem qnaBoardItem, int actionKind,boolean updatedByUser) {
        Log.i(TAG, "qnaUpdateItem: ");
        if (mypageFragment != null && updatedByUser){
            mypageFragment.updateMyBoard(qnaBoardItem,actionKind);
        }
        // main 페이지에서는 조회수 순으로 보여주기 때문에 새로 작성한 글에 대해서는
        // main 에 추가하지 않도록 예외처리
        if (mainFragment != null && actionKind != 0){
            mainFragment.updateQnaBoardItem(qnaBoardItem,actionKind);
        }
    }

    // main 페이지에서 qna 글 업데이트
    //
    @Override
    public void mainUpdateQnaItem(QnaBoardItem qnaBoardItem, int actionKind,boolean updatedByUser) {
        Log.i(TAG, "mainUpdateQnaItem: ");
        if (mypageFragment != null && updatedByUser){
            mypageFragment.updateMyBoard(qnaBoardItem,actionKind);
        }
        if (qnaFragment != null){
            qnaFragment.updateQnaBoardItem(qnaBoardItem,actionKind);
        }
    }

    // main 페이지에서 info 글 업데이트
    @Override
    public void mainUpdateInfoItem() {

    }

    @Override
    public void myBoardUpdateItem(QnaBoardItem qnaBoardItem, int actionKind) {
        // main 페이지에서는 조회수 순으로 보여주기 때문에 새로 작성한 글에 대해서는
        // main 에 추가하지 않도록 예외처리
        Log.i(TAG, "myBoardUpdateItem: ");
        if (mainFragment != null && actionKind != 0){
            mainFragment.updateQnaBoardItem(qnaBoardItem,actionKind);
        }

        if (qnaFragment != null){
            qnaFragment.updateQnaBoardItem(qnaBoardItem,actionKind);
        }
    }
}
