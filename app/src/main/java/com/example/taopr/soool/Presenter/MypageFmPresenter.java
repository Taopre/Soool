package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Calendar.CalendarDB;
import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Object.CalendarItemMap;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.InfoOfSooolList;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardList;
import com.example.taopr.soool.Object.UserProfile;
import com.example.taopr.soool.Presenter.Interface.MypageFmInter;

import java.util.ArrayList;

public class MypageFmPresenter extends BasePresenter implements MypageFmInter {

    private APIService apiService;
    private Context context;
    private MypageFmPresenter.View view;
    private String TAG = "홈 마이페이지 프레젠터";

    ArrayList<QnaBoardItem> qnaBoardItems;
    ArrayList<InfoOfSoool> infoOfSoools;

    public MypageFmPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }
    @Override
    public void setView(View view) {
        this.view = view;
    }

    // 이 함수 int 변수값에 따라 구별하자 그럼 일단..
    // i = 0 -> 내 게시물 리스트 객체
    // i = 1 -> 북마크 리스트 객체
    // i = 3 -> 유저 프로필 정보 객체 ( 3으로 한 이유는 2는 캘린더 데이터 실패했을 때의 구분자 값이기 때문에 )


    // i = 1일때 왜 두개로 구분해뒀냐면 객체를 어떻게 정의해야될지 고민중이라서 qnaboard로 가게되면 위에꺼 지우면 될듯.
    // 현재 mypagebookmarkitem mypagemyboarditem 객체는 아무것도 없는 상태입니다.

    // getDataFail 함수로 데이터 유무 나눌려고 만들었습니다.
    // qnaBoardList이 Null값일때와 onFailure일때 두 부분에 적용을 해두었습니다. boolean값은 임의로 true로 해두었습니다.
    // 그 이유는
    // 1. 통신은 문제 없었지만 오류나 다른 이유로 리스트를 못받은 경우
    // 2. 통신 문제로 onFail 된 경우
    // 만약에 나눠서 메시지 처리를 다르게한다면을 고려해봤는데 이 부분은 불필요하다면 그냥 통일해서 진행해도 무방합니다.
    // 3

    // 현재 switch 문 2개로 받도록 조치했는데 ApiCallback generic으로 한번에 처리된다면 바꿔보는거 해볼까 고민해봅시다.

    // getDataFail 함수에서 두번째 인자 int 값에 따른 변화
    // 0 -> qnaBoardList가 통신 실패일 때
    // 1 -> infoOfSooolList가 통신 실패일 때
    // 3 -> getUserProfile 통신 실패 시 ( 3으로 한 이유는 2는 캘린더 데이터 실패했을 때의 구분자 값이기 때문에 )

    @Override
    public void loadMypageData(int accountNo, int i){
        switch (i) {
            case 0:
                addSubscription(
                        apiService.getMypageBoardItem(accountNo),
                        new APICallback<QnaBoardList>() {

                            @Override
                            public void onSuccess(QnaBoardList qnaBoardList) {
                                if (qnaBoardList != null) {
                                    qnaBoardItems = new ArrayList(qnaBoardList.getQnaBoardItems());
                                    Log.i(TAG, "onSuccess: 사이즈 수 :" +qnaBoardItems.size());
                                    view.getQnaMyboardResponse(qnaBoardItems);
                                }
                                else{
                                    Log.i(TAG, "onSuccess: list = null");
                                }
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: msg");
                                view.getDataFail(true, 0);
                            }

                            @Override
                            public void onFinish() {
                                //dismissProgressDialog();
                            }
                        });
                break;
            case 1:
                addSubscription(
                        apiService.getMypageBookmarkItem(),
                        new APICallback<InfoOfSooolList>() {

                            @Override
                            public void onSuccess(InfoOfSooolList infoOfSooolList) {
                                if (infoOfSooolList != null) {
                                    infoOfSoools = new ArrayList(infoOfSooolList.getInfoOfSoools());
                                    view.getInfoBookmarkResponse(infoOfSoools);
                                }
                                else{
                                    Log.i(TAG, "onSuccess: list = null");
                                }
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: mypage" + msg);
                                view.getDataFail(true, 1);
                            }

                            @Override
                            public void onFinish() {
                                Log.i(TAG, "onFinish: mypage");
                                //dismissProgressDialog();
                            }
                        });
                break;
            case 3:
                addSubscription(
                        apiService.getUserProfile(accountNo),
                        new APICallback<UserProfile>() {

                            @Override
                            public void onSuccess(UserProfile userProfile) {
                                if (userProfile != null) {
                                    Log.i(TAG, "onSuccess: 유저 프로필 정보 " + userProfile.getAccountImage());
                                    view.getUserProfileSuccess(userProfile);
                                }
                                else{
                                    Log.i(TAG, "onSuccess: list = null");
                                }
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: mypage" + msg);
                                view.getDataFail(true, 2);
                            }

                            @Override
                            public void onFinish() {

                                // TODO: 데이터 가져오는 동안 로딩화면을 보여주고 데이터를 가져왔을 때 로딩화면 종료하는 부분
                                //dismissProgressDialog();
                            }
                        });
                break;

        }
    }

    public void getCalendarItem(Activity mActivity, String accountNo){
        Log.i(TAG, "getCalendarItem: ");
        CalendarDB calendarDB = new CalendarDB(mActivity);
        ArrayList<CalendarItem> calendarItems = new ArrayList<CalendarItem>(calendarDB.eventSelect(accountNo));

        // HashMap<이벤트 작성 날짜(key) : calendar 객체(value)>  구조를 가진 CalendarItemMap 객체에 추가한다.
        // HashMap 으로 만든 이유는 캘린더에 날짜 선택 시 선택한 날짜에 작성한 내용을 보기위해서는 선택한 날짜 값으로
        // select 을 하기 위해서

        CalendarItemMap calendarItemMap = new CalendarItemMap();

        for(int a=0; a<calendarItems.size(); a++){
            calendarItemMap.put(calendarItems.get(a).getEventDate(),calendarItems.get(a));
        }

        view.getCalendarDataResponse(calendarItemMap);
    }
}
