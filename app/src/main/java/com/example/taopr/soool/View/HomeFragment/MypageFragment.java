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

    String TAG = "홈 마이페이지 프래그먼트";
    private Context context;

    private final int MYBOARD_INT = 0;
    private final int BOOKMARK_INT = 1;
    private final int CALENDAR_INT = 2;
    public final String CALENDAR_LIST_EXTRA = "calendarItems";
    private final String ACCOUNT_NO_EXTRA = "accountNo";
    private int fragmentNo=0;  // 마이 페이지 첫 화면은 myBoard 프래그먼트 화면이기 때문

    public MyBoardFragment myBoardFragment = null;
    public CalendarFragment calendarFragment = null;
    public BookmarkFragment bookmarkFragment = null;

    private MypageFmPresenter mypagePresenter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private ArrayList<CalendarItem> calendarItems = new ArrayList<>();
    private CalendarItemMap calendarItemMap = new CalendarItemMap();
    private UserProfile userProfile=null;
    private int accountNo=0;

    private getUserProfileListener getUserProfileListener; //


    public MypageFragment() {

    }

    // 액티비티에서 프래그먼트로 값 넘기는 거 확인
    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_mypage, container, false);

        unbinder = ButterKnife.bind(this,view);

        context = view.getContext();
        mypagePresenter = new MypageFmPresenter(context);
        mypagePresenter.setView(this);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.rgb(255,255,255)); // 원형 백그라운드 색상 (디폴트 검정색)
        mypageProfileImage.setBackground(drawable);

        // 받아온 프로필 정보가 없을 시 서버에서 받는다
        if(userProfile == null){
            getUserProfile();
        }
        else{
            callFragment(fragmentNo);
            tabSetting(fragmentNo);
            getUserProfileSuccess(userProfile);
        }

        return view;

    }

    private void tabSetting(int tabNo){
        switch (tabNo){
            case 0:
                tabMyBoardBar.setVisibility(View.VISIBLE);
                tabBookMarkBar.setVisibility(View.INVISIBLE);
                tabCalendarBar.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tabMyBoardBar.setVisibility(View.INVISIBLE);
                tabBookMarkBar.setVisibility(View.VISIBLE);
                tabCalendarBar.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tabMyBoardBar.setVisibility(View.INVISIBLE);
                tabBookMarkBar.setVisibility(View.INVISIBLE);
                tabCalendarBar.setVisibility(View.VISIBLE);
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

        mypagePresenter.loadMypageData(accountNo,3);

        // oncreate에서 현재 탭에 맞는 프래그먼트를 띄어주기 위해서 fragmentNo값으로 구별한다.
        // 0 = myboard , 1 = bookmark , 2 = calendar
        // 0,1일 경우에는 서버에서 리스트 목록을 가져오고 가져온 후에 프래그먼트를 부착한다
        if(fragmentNo == 0 || fragmentNo == 1){
            mypagePresenter.loadMypageData(accountNo,fragmentNo);
        }

        else{
            mypagePresenter.getCalendarItem(getActivity(),String.valueOf(accountNo));
        }
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
        switch (view.getId()) {
            case R.id.tabMyBoard:
                if(myBoardFragment==null){
                    myBoardFragment = new MyBoardFragment();
                    // 프래그먼트 탭 이동시 탭에 맞는 bar 만 보여주기
                    mypagePresenter.loadMypageData(accountNo,MYBOARD_INT);

                }
                else{
                    callFragment(MYBOARD_INT);
                    tabSetting(MYBOARD_INT);
                }

                break;

            case R.id.tabBookmark:
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                if(bookmarkFragment ==null){
                    // TODO: 나중에 지워줘야 하는 부분
                    callFragment(BOOKMARK_INT);
                    tabSetting(BOOKMARK_INT);
                }
                else {
                    callFragment(BOOKMARK_INT);
                    tabSetting(BOOKMARK_INT);
                }
                break;

            case R.id.tabCalendar:
                if(calendarFragment==null) {
                    mypagePresenter.getCalendarItem(getActivity(), String.valueOf(accountNo));
                }
                else{
                    callFragment(CALENDAR_INT);
                    tabSetting(CALENDAR_INT);
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (fragmentNo) {
            case 0:

                // 프래그먼트가 null이 아닐경우에만 callFragment 함수를 들어오게 했지만
                // 혹시나 버그가 있을 수 있기 때문에 예외처리를 함
                // 불필요한 코드일 수도 있음
                if (myBoardFragment == null) {
                    Log.i(TAG, "callFragment: myBoardFragment null");
                    myBoardFragment = new MyBoardFragment();
                    mypagePresenter.loadMypageData(accountNo, MYBOARD_INT);
                } else {

                    // fragment에 값을 전달하기 위해 bundle 객체를 생성하고
                    // bundle에 qnaBoardList 를 담아서 전달

                    Log.i(TAG, "callFragment: 사이즈 " + qnaBoardItems.size());
                    myBoardFragment = new MyBoardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("qnaBoardItems", (ArrayList<? extends Parcelable>) qnaBoardItems); // list 넘기기
                    myBoardFragment.setArguments(bundle);

                    transaction.replace(R.id.fragment_container, myBoardFragment);
                    transaction.commit();
                }


                break;

            case 1:
                bookmarkFragment = new BookmarkFragment();
                transaction.replace(R.id.fragment_container, bookmarkFragment);
                transaction.commit();
                break;

            case 2:
                calendarFragment = new CalendarFragment();

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

    // 프래그먼트를 액티비티에 부착할 때 액티비티와 프래그먼트 사이의 인터페이스가 될줄 getUserProfileListener 를 생성하고
    // detach() 일때 getUserProfileListener = null 값을 넣어준다

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
          if (context instanceof getUserProfileListener) {
              getUserProfileListener = (getUserProfileListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
        getUserProfileListener = null;
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
    public void getCalendarDataResponse(CalendarItemMap calendarItemMap){
        this.calendarItemMap = calendarItemMap;
        callFragment(CALENDAR_INT);
    }

    @Override
    public void getDataFail(boolean response, int i) {
        if (i == MYBOARD_INT) {
            Toast.makeText(context, "내 게시물들을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            myBoardFragment = null;
        } else if (i == BOOKMARK_INT) {
            Toast.makeText(context, "북마크들을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            bookmarkFragment = null;
        }
        else if (i == CALENDAR_INT) {
            Toast.makeText(context, " 달력을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            calendarFragment = null;
        }
    }

    // 유저의 프로필 정보를 response 받아 뷰로 보여주기
    @Override
    public void getUserProfileSuccess(UserProfile userProfile){
        this.userProfile = userProfile;

        mypageProfileMyQnaCount.setText(String.valueOf(userProfile.getAccountBc()));
        mypageProfileMyPointCount.setText(String.valueOf(userProfile.getAccountPoint()));


        showProfileImgNick(userProfile.getAccountImage(),userProfile.getAccountNick());


        // Home 액티비티의 Drawer 레이아웃에서 회원정보 중에 닉네임과 이메일주소로 필요로 한다
        // 그래서 서버로 부터 회원정보를 받아온 후에 프래그먼트에서 액티비티로 데이터를 전달해줘야 하기 때문에
        // getUserProfileListener 를 인터페이스로 둬서 이를 통해 전달한다

        getUserProfileListener.getUserProfile(userProfile.getAccountNick(),userProfile.getAccountEmail());
    }

    public interface getUserProfileListener{
        void getUserProfile(String accountNick,String accountEmail);
    }

}
