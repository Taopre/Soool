package com.example.taopr.soool.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Object.CalendarItemMap;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.UserProfile;
import com.example.taopr.soool.Presenter.MypagePresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.View.MyPageFragment.BookmarkFragment;
import com.example.taopr.soool.View.MyPageFragment.CalendarFragment;
import com.example.taopr.soool.View.MyPageFragment.MyBoardFragment;
import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prolificinteractive.materialcalendarview.CalendarMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyPageActivity extends BaseActivity implements MypagePresenter.View{


    @BindView(R.id.tabMyBoard)
    TextView tabMyBoard;
    @BindView(R.id.tabBookmark)
    TextView tabBookmark;
    @BindView(R.id.tabCalendar)
    TextView tabCalendar;
    @BindView(R.id.mypageProfileImage)
    ImageView mypageProfileImage;
    @BindView(R.id.mypageProfileMyPointCount)
    TextView mypageProfileMyPointCount;
    @BindView(R.id.mypageProfileMyQnaCount)
    TextView mypageProfileMyQnaCount;
    @BindView(R.id.mypageProfileNickname)
    TextView mypageProfileNickname;
    @BindView(R.id.mainActionBarTitle)
    TextView mainActionBarTitle;

    private final int MYBOARD_INT = 0;
    private final int BOOKMARK_INT = 1;
    private final int CALENDAR_INT = 2;
    public static final String CALENDAR_LIST_EXTRA = "calendarItems";
    private static final String ACCOUNT_NO_EXTRA = "accountNo";
    private static int fragmentNo=0;  // 마이 페이지 첫 화면은 myBoard 프래그먼트 화면이기 때문

    public static MyBoardFragment myBoardFragment = null;
    public static CalendarFragment calendarFragment = null;
    public static BookmarkFragment bookmarkFragment = null;

    static String TAG = "마이페이지 액티비티";

    private static MypagePresenter mypagePresenter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private ArrayList<CalendarItem> calendarItems = new ArrayList<>();
    private CalendarItemMap calendarItemMap = new CalendarItemMap();

    private static int accountNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage,3);

        //액션바 숨기기
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.bind(this);

        mainActionBarTitle.setText("마이 페이지");
        mypagePresenter = new MypagePresenter(this);
        mypagePresenter.setView(this);


        // 쉐어드에 저장된 유저의 회원번호를 가져온 후 회원번호를 서버에 전송해서
        // 유저의 프로필 정보를 가져온다
        getUserProfile();

    }

    // 쉐어드에 저장된 유저 프로필을 가져온 후에
    // 프로필 이미지, 닉네임, 작성글 수, 포인트 수를 보여주기
    // 회원번호를 가져오는 이유는 회원번호가 외래키이기 때문에 회원번호를 파라미터로 전송하기 때문

    void getUserProfile() {

        String data = LoginSharedPreferences.LoginUserLoad(this, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);

        accountNo = loginSessionItem.getAccountNo();

        mypagePresenter.loadMypageData(accountNo,3);

        // oncreate에서 현재 탭에 맞는 프래그먼트를 띄어주기 위해서 fragmentNo값으로 구별한다.
        // 0 = myboard , 1 = bookmark , 2 = calendar
        // 0,1일 경우에는 서버에서 리스트 목록을 가져오고 가져온 후에 프래그먼트를 부착한다
        if(fragmentNo == 0 || fragmentNo == 1){
            mypagePresenter.loadMypageData(accountNo,fragmentNo);
        }

        else{
            mypagePresenter.getCalendarItem(MyPageActivity.this,String.valueOf(accountNo));
        }



    }

    // 탭 클릭 시 클릭한 페이지의 프래그먼트가 null인지 아닌지를 구별하여
    // 내 게시물,북마크에서는 서버에서 데이터를 받아올지를 선택한다
    // 프래그먼트가 null인 경우에는 서버로부터 리스트 데이터를 받아오지 못했기 때문에
    // 서버에 데이터를 요청 후 받아온다

    @OnClick({R.id.tabMyBoard,R.id.tabBookmark,R.id.tabCalendar,R.id.myPageDrawerButton})
    void OnClickButton(View view){
        switch (view.getId()) {
            case R.id.tabMyBoard:
                if(myBoardFragment==null){
                    myBoardFragment = new MyBoardFragment();

                    mypagePresenter.loadMypageData(accountNo,MYBOARD_INT);
                }
                else{
                    callFragment(MYBOARD_INT);
                }

                break;

            case R.id.tabBookmark:
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(BOOKMARK_INT);
                break;

            case R.id.tabCalendar:
                mypagePresenter.getCalendarItem(MyPageActivity.this,String.valueOf(accountNo));
               //callFragment(CALENDAR_INT;

                break;

            case R.id.myPageDrawerButton:
                DrawerLayout mypageDrawerLayout = (DrawerLayout) findViewById(R.id.myPageDrawerLayout) ;
                if (!mypageDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mypageDrawerLayout.openDrawer(Gravity.RIGHT) ;
                }
                break;
        }
    }

    // callFragment 에서는 탭에서 선택한 프래그먼트를 액티비티에 부착하는 메서드이다.
    // 현재 부착된 프래그먼트를 구별하기 위해 fragmentNo에 값을 넣어준다
    // callFragment 는 탭에서 선택한 프래그먼트가 생성된 상태에서만 올 수 있게 처리
    // 만약에 없을 경우를 대비해서 없을 경우 프래그먼트를 생성하고
    // myBoard 와 myInfo 에서는 qna 와 info 리스트가 null 이라면 서버에서 리스트 데이터를 받아온다
    // 그래서 서버에서 데이터 전달이 실패하게 된다면 현재 프래그먼트를 null 값으로 변경한다
    // 그러기 위해서는 실패했을 때 어떤 프래그먼트에서 실패했는지에 관한 구분자 값을 전달 받음


    private void callFragment(int fragment_no){

        fragmentNo = fragment_no;

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentNo) {
            case 0:

                // 프래그먼트가 null이 아닐경우에만 callFragment 함수를 들어오게 했지만
                // 혹시나 버그가 있을 수 있기 때문에 예외처리를 함
                // 불필요한 코드일 수도 있음
                if (myBoardFragment == null) {
                    myBoardFragment = new MyBoardFragment();
                    mypagePresenter.loadMypageData(accountNo, MYBOARD_INT);
                } else {

                    // fragment에 값을 전달하기 위해 bundle 객체를 생성하고
                    // bundle에 qnaBoardList 를 담아서 전달

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("qnaBoardItems", (ArrayList<? extends Parcelable>) qnaBoardItems); // list 넘기기
                    myBoardFragment.setArguments(bundle);

                    transaction.replace(R.id.fragment_container, myBoardFragment);
                    transaction.commit();
                }


                break;

            case 1:
                BookmarkFragment bookmarkFragment = new BookmarkFragment();
                transaction.replace(R.id.fragment_container, bookmarkFragment);
                transaction.commit();
                break;

            case 2:
                CalendarFragment calendarFragment = new CalendarFragment();

                if(calendarItems!=null) {
                    Bundle bundle = new Bundle();
                    // bundle.putParcelableArrayList("calendarItems", (ArrayList<? extends Parcelable>) calendarItems); // list 넘기기
                    bundle.putParcelable(CALENDAR_LIST_EXTRA,calendarItemMap);
                    bundle.putString(ACCOUNT_NO_EXTRA
                            ,String.valueOf(accountNo));
                    calendarFragment.setArguments(bundle);

                }
                transaction.replace(R.id.fragment_container, calendarFragment );
                transaction.commit();
                break;
        }
    }


    // 북마크 게시물 데이터를 받아온 후

    @Override
    public void getInfoBookmarkResponse(ArrayList<InfoOfSoool> infoOfSoools) {

    }

    // 서버로부터 MyBoard 프래그먼트에서 보여줄 리스트 데이터 성공했을 경우의 함수
    // TODO: 서버로부터 데이터 전달을 기다리는 동안 다른 탭으로 이동했을 때, 데이터 전달이 성공되면 페이지가 변경될 수 있다.
    // TODO: 이 부분에 대해서 어떻게 처리할 지 생각

    @Override
    public void getQnaMyboardResponse(ArrayList<QnaBoardItem> qnaBoardItems) {

        this.qnaBoardItems = qnaBoardItems;
        callFragment(MYBOARD_INT);
    }

    @Override
    public void getUserProfileResponse(UserProfile userProfile){
        mypageProfileNickname.setText(userProfile.getAccountNick());
        mypageProfileMyQnaCount.setText(String.valueOf(userProfile.getAccountBc()));
        mypageProfileMyPointCount.setText(String.valueOf(userProfile.getAccountPoint()));

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.rgb(255,255,255)); // 원형 백그라운드 색상 (디폴트 검정색)
        mypageProfileImage.setBackground(drawable);
        mypageProfileImage.setClipToOutline(true);

        String accountImageAddress= "http://54.180.90.184" + userProfile.getAccountImage();

        Glide.with(MyPageActivity.this)
                .load(accountImageAddress)
                .centerCrop()
                .into(mypageProfileImage);
    }


    @Override
    public void getCalendarDataResponse(CalendarItemMap calendarItemMap){
        this.calendarItemMap = calendarItemMap;
        callFragment(CALENDAR_INT);
    }

    // 내 게시물 , 북마크 리스트를 가져오는 작업을 실패 했을 경우
    // 다시 시도해달라는 메세지 같은 화면을 유저에게 보여준다
    // 해당 fragment 를 null 값으로 변경하여 실패한 후에 서버에 요청을 보낼 수 있게 한다
    // ( fragment 값이 null 일 때만 요청하게끔 하여 서버에 중복 요청을 보내는 것을 방지했기 때문 )
    // i=0 -> qnaBoardList가 통신 실패일 떄
    // i=1 -> infoOfSooolList가 통신 실패일 때
    // i=2 -> calendarItems가 통신 실패일 때

    @Override
    public void getDataFail(boolean response, int i) {
        if (i == MYBOARD_INT) {
            Toast.makeText(this, "내 게시물들을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            myBoardFragment = null;
        } else if (i == BOOKMARK_INT) {
            Toast.makeText(this, "북마크들을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            bookmarkFragment = null;
        }
        else if (i == CALENDAR_INT) {
            Toast.makeText(this, " 달력을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            calendarFragment = null;
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


        mainActionBarTitle.setText(R.string.my_page_label_qna_my_page);

        return true;
    }
}
