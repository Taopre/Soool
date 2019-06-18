package com.example.taopr.soool.View.HomeFragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Object.CalendarItemMap;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.UserProfile;
import com.example.taopr.soool.Presenter.MypageFmPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.View.MyPageFragment.BookmarkFragment;
import com.example.taopr.soool.View.MyPageFragment.CalendarFragment;
import com.example.taopr.soool.View.MyPageFragment.MyBoardFragment;
import com.example.taopr.soool.Whatisthis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MypageFragment extends BaseFragment implements MypageFmPresenter.View{

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
    @BindView(R.id.tabCalendarBar)
    View tabCalendarBar;
    @BindView(R.id.tabBookMarkBar)
    View tabBookMarkBar;
    @BindView(R.id.tabMyBoardBar)
    View tabMyBoardBar;
    @BindView(R.id.mypageProgress)
    ProgressBar mypageProgress;

    String TAG = "홈 마이페이지 프래그먼트";
    private Context context;

    private final int MYBOARD_INT = 0;
    private final int BOOKMARK_INT = 1;
    private final int CALENDAR_INT = 2;
    private final String ACCOUNT_NO_EXTRA = "accountNo";
    private int fragmentNo=0;  // 마이 페이지 첫 화면은 myBoard 프래그먼트 화면이기 때문

    private int currentTab=0,previousTab=1;

    private MyBoardFragment myBoardFragment = null;
    private CalendarFragment calendarFragment = null;
    private BookmarkFragment bookmarkFragment = null;

    private MypageFmPresenter mypagePresenter;

    private UserProfile userProfile=null;
    private int accountNo=0;

    private Boolean isProfileRes = false;  // 프로필 정보를 서버로 응답을 받았는지 (성공,실패 포함)
    private Boolean isFragmentRes = false; // 부착한 프래그먼트에서 서버로부터 응답을 받았는지 ( 성공.실패 포함 )


    public MypageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // 유저의 프로필 정보를 가져오기 위해서 쉐어드에 저장된 accountNo를 가져온다
    // accountNo를 가져온 후 서버를 통해 프로필 정보를 가져온 후 뷰로 보여준다

    // 프래그먼트 부분
    // 우선 첫 페이지는 내 게시물 프래그먼트 탭을 부착한다.
    // 값으로 0으로 생성한 fragmentNo 를 callFragment 에 전달해 프래그먼트를 부착한다

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_home_mypage, container, false);

        unbinder = ButterKnife.bind(this,view);

        context = view.getContext();
        mypagePresenter = new MypageFmPresenter(context);
        mypagePresenter.setView(this);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.rgb(255,255,255)); // 원형 백그라운드 색상 (디폴트 검정색)
        mypageProfileImage.setBackground(drawable);
        mypageProfileImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.profile_default));

        // 받아온 프로필 정보가 없을 시 서버에서 받는다
        // 받아온 프로필 정보가 있다면 뷰로 보여주기
        if(userProfile == null){
            getUserProfile();
        }
        else showProfileInfo();

        callFragment();

        return view;

    }

    // 마이페이지 탭
    // 현재 탭의 탭바를 보여주고 , 이전 탭의 탭바를 숨겨준다
    // 그리고 현재 탭 타이틀의 글자색을 green_dark 색상으로 변경해주고, 이전 탭의 색상은 gray_mid 색상으로 변경
    private void tabSetting(){
        Log.i(TAG, "tabSetting: 이전탭 : "+ previousTab +"현재탭 " +currentTab);
        switch (previousTab){
            case 0:
                tabMyBoardBar.setVisibility(View.INVISIBLE);
                tabMyBoard.setTextColor(ContextCompat.getColor(getContext(),R.color.grayMid));
                break;
            case 1:
                tabBookMarkBar.setVisibility(View.INVISIBLE);
                tabBookmark.setTextColor(ContextCompat.getColor(getContext(),R.color.grayMid));
                break;
            case 2:
                tabCalendarBar.setVisibility(View.INVISIBLE);
                tabCalendar.setTextColor(ContextCompat.getColor(getContext(),R.color.grayMid));
                break;
        }

        switch (currentTab){
            case 0:
                tabMyBoardBar.setVisibility(View.VISIBLE);
                tabMyBoard.setTextColor(ContextCompat.getColor(getContext(),R.color.greenDark));
                break;
            case 1:
                tabBookMarkBar.setVisibility(View.VISIBLE);
                tabBookmark.setTextColor(ContextCompat.getColor(getContext(),R.color.greenDark));
                break;
            case 2:
                tabCalendarBar.setVisibility(View.VISIBLE);
                tabCalendar.setTextColor(ContextCompat.getColor(getContext(),R.color.greenDark));
                break;
        }


    }

    // 쉐어드에 저장된 유저 프로필을 가져온 후에
    // 프로필 이미지, 닉네임, 작성글 수, 포인트 수를 보여주기
    // 회원번호를 가져오는 이유는 회원번호가 외래키이기 때문에 회원번호를 파라미터로 전송하기 때문

    void getUserProfile() {

        String data = LoginSharedPreferences.LoginUserLoad(context, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);

        accountNo = loginSessionItem.getAccountNo();

        showLoading();
        mypagePresenter.loadMypageData(accountNo);
    }

    public void showProfileInfo(String accountImage,String accountNick){

        // TODO : 바뀐 프로필 정보 보여주기

    }

    // 회원정보 페이지(ProfileAct) 에서 회원정보를 수정했을 때
    // 마이페이지에서도 수정한 데이터를 보여줘야 하기 때문에
    // 메서드로 따로 땜
    public void showProfileImgNick(String accountImage,String accountNick){

        mypageProfileNickname.setText(accountNick);

        // 유저가 프로필 이미지를 저장한 경우에는 저장한 이미지를
        // 그렇지 않은 경우에는 디폴트 이미지를 보여준다.

        if(!accountImage.equals("soool_default")) {

            String accountImageAddress = Whatisthis.serverIp + accountImage;

            Glide.with(getActivity())
                    .load(accountImageAddress)
                    .centerCrop()
                    .into(mypageProfileImage);
        }
        else{
            mypageProfileImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.profile_default));
        }
        mypageProfileImage.setClipToOutline(true);
    }


    // 탭 클릭 시 클릭한 페이지의 프래그먼트가 null인지 아닌지를 구별하여
    // 내 게시물,북마크에서는 서버에서 데이터를 받아올지를 선택한다
    // 프래그먼트가 null인 경우에는 서버로부터 리스트 데이터를 받아오지 못했기 때문에
    // 서버에 데이터를 요청 후 받아온다

    @OnClick({R.id.tabMyBoard,R.id.tabBookmark,R.id.tabCalendar})
    void OnClickButton(View view){
        previousTab = currentTab;

        switch (view.getId()) {
            case R.id.tabMyBoard:
                currentTab = MYBOARD_INT;
                callFragment();
                break;
            case R.id.tabBookmark:
                currentTab = BOOKMARK_INT;
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                if(bookmarkFragment ==null){
                    // TODO: 나중에 지워줘야 하는 부분
                    callFragment();

                }
                else {
                    callFragment();
                }
                break;

            case R.id.tabCalendar:
                currentTab = CALENDAR_INT;
                callFragment();
                break;

        }
    }

    // callFragment 에서는 탭에서 선택한 프래그먼트를 액티비티에 부착하는 메서드이다.
    // 현재 부착된 프래그먼트를 구별하기 위해 fragmentNo에 값을 넣어준다

    // 서버에 데이터 요청을 받아온 페이지에 대해서는 다시 요청을 보내지 않기 위해서
    // 프래그먼트가 null 값이 아니라면 프래그먼트를 새로 생성하지 않고 이전에 생성했던 프래그먼트를 재사용하도록 한다
    // 하지만 이렇게 했을 때 생성을 했지만 서버 요청을 받는데 실패 했을 경우에 대처를 못하기 때문에
    // 서버로부터 요청이 실패를 받는 방법은 마이프래그먼트에 부착한 프래그먼트에서 요청을 받아왔을 경우
    // Home 액티비티에서 Response 값을 전달 받는다. 그 다음 Home 액티비티에서 전달받은 Response 값을
    // MyPage 프래그먼트로 전달하는 방식이다. 그리고 받은 Response 값이 false 일 경우 Response 를 전달한 프래그먼트는
    // null 값으로 변경하여 프래그먼트를 부착할 때 다시 생성해서 서버에 다시 요청을 받아오도록 한다

    private void callFragment(){


        // FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        // 프래그먼트 안의 프래그먼트를 관리할 때는 getChildFragmentManager 를 통해 관리한다
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();


        //switch (fragmentNo) {
        switch (currentTab) {
            case 0:
                if (myBoardFragment == null ){
                    myBoardFragment = new MyBoardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ACCOUNT_NO_EXTRA
                            ,accountNo);
                    myBoardFragment.setArguments(bundle);
                }
                transaction.replace(R.id.fragment_container, myBoardFragment);

                break;

            case 1:
                bookmarkFragment = new BookmarkFragment();
                transaction.replace(R.id.fragment_container, bookmarkFragment);

                break;

            case 2:

               if (calendarFragment == null) {
                   calendarFragment = new CalendarFragment();
                   Bundle bundle = new Bundle();
                   bundle.putString(ACCOUNT_NO_EXTRA
                           ,String.valueOf(accountNo));
                   calendarFragment.setArguments(bundle);
               }

                transaction.replace(R.id.fragment_container, calendarFragment );

                break;
        }
        tabSetting();
        transaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
        if (myBoardFragment != null) {
            myBoardFragment.onDetach();
        }
    }

    @Override
    public void getDataFail(boolean response, int i) {
        // 로딩화면 처리
        isProfileRes = true;
        if(isFragmentRes){
            hideLoading();
        }
    }


    // 유저의 프로필 정보를 response 받아 뷰로 보여주기
    @Override
    public void getUserProfileSuccess(UserProfile userProfile){
        this.userProfile = userProfile;

        showProfileInfo();

        // 로딩화면 처리
        isProfileRes = true;
        if(isFragmentRes){
            hideLoading();
        }

        // 프로필 액티비티에서 회원 정보를 수정했을 경우 수정된 내용을 Drawer Layout 에서도 갱신해줘야 한다.
        // 하지만 DrawerLayout 은 홈액티비티에 상속되어 있는 뷰이기 때문에 마이페이지 프래그먼트에서 홈액티비티의 뷰에
        // 접근하기 위해서 getActivity() 를 통해 홈액티비티를 가져와 접근하면 된다

        TextView myPageDrawerEmail = getActivity().findViewById(R.id.myPageDrawerEmail);
        TextView myPageDrawerNickname = getActivity().findViewById(R.id.myPageDrawerNickname);

        myPageDrawerEmail.setText(userProfile.getAccountEmail());
        myPageDrawerNickname.setText(userProfile.getAccountNick());
    }


    // 유저의 프로필 정보를 뷰로 보여준다
    public void showProfileInfo(){
        mypageProfileMyQnaCount.setText(String.valueOf(userProfile.getAccountBc()));
        mypageProfileMyPointCount.setText(String.valueOf(userProfile.getAccountPoint()));
        showProfileImgNick(userProfile.getAccountImage(),userProfile.getAccountNick());
    }

    // 로딩 화면
    // 내 게시물 프래그먼트에서 내 게시물 데이터를 서버에 요청한 응답이 도착했을 때
    // 유저의 프로필 정보를 서버에 요청한 응답도 도착이 했을 경우 페이지의 로딩화면을 숨긴다
    // 그리고 isFragment 의 값을 true 로 변경하여 프로필에 대한 응답이 오지 않을 경우
    // 프로필에 대한 응답이 왔을 경우 isFragment 값이 true 일때 로딩화면을 숨기게 한다

    // 내 게시물 요청에 대한 응답을 제대로 가져오지 못했을 경우
    // myBoardFragment 를 null 값으로 변경하여 myBoardFragment 를 다시 부착할 때
    // 서버에 내 게시물 요청을 다시끔 하게 한다

    public void getFragmentRes(int fragmentNo,boolean isResponse){
        isFragmentRes =true;
        if(isProfileRes) hideLoading();

        if(!isResponse) {
            switch (fragmentNo) {
                case 0:
                    myBoardFragment =null;
                    break;
                case 1:
                    bookmarkFragment = null;
                    break;
                case 2:
                    calendarFragment = null;
                    break;
            }
        }
    }


    private void showLoading(){
        mypageProgress.setVisibility(View.VISIBLE);
    }
    private void hideLoading(){
        mypageProgress.setVisibility(View.GONE);
    }

}
