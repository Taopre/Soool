package com.example.taopr.soool.View;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.MypagePresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.View.MyPageFragment.BookmarkFragment;
import com.example.taopr.soool.View.MyPageFragment.CalendarFragment;
import com.example.taopr.soool.View.MyPageFragment.MyBoardFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyPageActivity extends BaseActivity implements MypagePresenter.View{


    private final int myBoard = 0;
    private final int bookmark = 1;
    private final int calendar = 2;

    private static int fragmentNo=0;  // 마이 페이지 첫 화면은 myBoard 프래그먼트 화면이기 때문

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

    public static MyBoardFragment myBoardFragment;
    static String TAG = "프래그먼트 main";

    private static MypagePresenter mypagePresenter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();

    private static String accountNick, accountImage;
    private static int accountNo, accountPoint, accountBc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage,3);

        ButterKnife.bind(this);

        mypagePresenter = new MypagePresenter(this);
        mypagePresenter.setView(this);

        // oncreate에서 현재 탭에 맞는 프래그먼트를 띄어주기 위해서 fragmentNo값으로 구별한다.
        // 0 = myboard , 1 = bookmark , 2 = calendar
        // 0,1일 경우에는 서버에서 리스트 목록을 가져오고 가져온 후에 프래그먼트를 부착한다

        if(fragmentNo == 0 || fragmentNo == 1){
            mypagePresenter.loadMypageData(fragmentNo);
        }

        else{ } // TODO: 캘린더 데이터를 가져온 후 캘린더프래그먼트 부착하는 메서드 필요
                // 캘린더 데이터는 myboard와 bookmark랑 달리 데이터를 가져온 후에 calendar 프래그먼트로
                // replace 할 이유가 없을 듯
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
        accountBc = loginSessionItem.getAccountBc();
        accountImage = loginSessionItem.getAccountImage();
        accountNick = loginSessionItem.getAccountNick();
        accountNo = loginSessionItem.getAccountNo();
        accountPoint = loginSessionItem.getAccountPoint();

        setUserProfile();
    }


    void setUserProfile(){
        mypageProfileNickname.setText(accountNick);
        mypageProfileMyQnaCount.setText(String.valueOf(accountBc));
        mypageProfileMyPointCount.setText(String.valueOf(accountPoint));


        // 이미지 원형 처리하는 부분
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.rgb(255,255,255)); // 원형 백그라운드 색상 (디폴트 검정색)
        mypageProfileImage.setBackground(drawable);
        mypageProfileImage.setClipToOutline(true);

        //mypageProfileImage.setImageDrawable(MyPageActivity.this.getDrawable(R.drawable.ic_add_a_photo_gray_100dp));

        //TODO: 이미지 경로 하드코딩된 상태 , 추후에 주석 또는 제거
       accountImage ="http://54.180.90.184/qnaImage/677px-Process_deadlock.svg.png";

        Glide.with(MyPageActivity.this)
                .load(accountImage)
                .centerCrop()
                .into(mypageProfileImage);

    }


    // 탭 클릭 시 클릭한 페이지의 프래그먼트가 null인지 아닌지를 구별하여
    // 내 게시물,북마크에서는 서버에서 데이터를 받아올지를 선택한다
    // 프래그먼트가 null인 경우에는 서버로부터 리스트 데이터를 받아오지 못했기 때문에
    // 서버에 데이터를 요청 후 받아온다

    @OnClick({R.id.tabMyBoard,R.id.tabBookmark,R.id.tabCalendar})
    void tabChange(View view){
        switch (view.getId()) {
            case R.id.tabMyBoard:
                if(myBoardFragment==null){
                    myBoardFragment = new MyBoardFragment();

                    mypagePresenter.loadMypageData(myBoard);
                }
                else{
                    callFragment(myBoard);
                }

                break;

            case R.id.tabBookmark:
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(bookmark);
                break;

            case R.id.tabCalendar:
                callFragment(calendar);
                break;

        }
    }

    // callFragment에서는 탭에서 선택한 프래그먼트를 액티비티에 부착하는 메서드이다.
    // 현재 부착된 프래그먼트를 구별하기 위해 fragmentNo에 값을 넣어준다
    // callFragment는 탭에서 선택한 프래그먼트가 생성된 상태에서만 올 수 있게 처리
    // 만약에 없을 경우를 대비해서 없을 경우 프래그먼트를 생성하고
    // myboard와 myinfo에서는 qna와 info 리스트가 null이라면 서버에서 리스트 데이터를 받아온다
    // TODO: 그래서 서버에서 데이터 전달이 실패하게 된다면 현재 프래그먼트를 null값으로 변경한다
    // TODO: 그러기 위해서는 실패했을 때 어떤 프래그먼트에서 실패했는지에 관한 구분자 값을 전달 받아야함


    private void callFragment(int fragment_no){

        fragmentNo = fragment_no;

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentNo) {
            case 0:

                // 프래그먼트가 null이 아닐경우에만 callFragment 함수를 들어오게 했지만
                // 혹시나 버그가 있을 수 있기 때문에 예외처리를 함
                // 불필요한 코드일 수도 있음
                if(myBoardFragment == null) {
                    myBoardFragment = new MyBoardFragment();

                    mypagePresenter.loadMypageData(myBoard);
                }

                else{
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
        callFragment(myBoard);

    }


    // 내 게시물 , 북마크 리스트를 가져오는 작업을 실패 했을 경우
    // 다시 시도해달라는 메세지 같은 화면을 유저에게 보여준다

    // TODO:fragment를 null 값으로 변경해야 하기 때문에, presenter에서 fragmentNo값도 같이 전달해줘야한다.

    // i=0 -> qnaBoardList가 null일때
    // i=1 -> infoOfSooolList가 null일때
    // i=2 -> qnaBoardList의 통신 fail일때
    // i=3 -> infoOfSooolList의 통신 fail일떄

    @Override
    public void getDataFail(boolean response, int i) {
        if (i == 0) {
            Toast.makeText(this, "내 게시물들을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        } else if (i == 1) {
            Toast.makeText(this, "북마크들을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        } else if (i == 2) {
            Toast.makeText(this, "내 게시물들을 가져오는것을 실패하였습니다.", Toast.LENGTH_SHORT).show();
        } else if (i == 3) {
            Toast.makeText(this, "북마크들을 가져오는것을 실패하였습니다..", Toast.LENGTH_SHORT).show();
        }
    }

}
