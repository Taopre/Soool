package com.example.taopr.soool.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.InfoOfSooolList;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardList;

import java.util.ArrayList;

public class MypagePresenter extends BasePresenter implements MypageInter {

    private APIService apiService;
    private Context context;
    private MypagePresenter.View view;
    private String TAG = "MypagePresenter";

    ArrayList<InfoOfSoool> infoOfSoools;
    ArrayList<QnaBoardItem> qnaBoardItems;

    // 아래 apiservice는 진태 정리대로 해볼려고 일단 받아뒀는데 mypage에 맞게 정리해야할듯??

    public MypagePresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    // 달력 데이터 업로드하게되면 작성하면 될곳.

    @Override
    public void uploadSchedule() {

    }

    // 이 함수 int 변수값에 따라 구별하자 그럼 일단..
    // i = 0 -> 북마크 리스트 객체
    // i = 1 -> 내 게시물 리스트 객체
    // 혹시 몰라서 2일때 대비로 해둔다. (켈린더 정보)

    // i = 1일때 왜 두개로 구분해뒀냐면 객체를 어떻게 정의해야될지 고민중이라서 qnaboard로 가게되면 위에꺼 지우면 될듯.
    // 현제 mypagebookmarkitem mypagemyboarditem 객체는 아무것도 없는 상태입니다.

    // getDataFail 함수로 데이터 유무 나눌려고 만들었습니다.
    // qnaBoardList이 Null값일때와 onFailure일때 두 부분에 적용을 해두었습니다. boolean값은 임의로 true로 해두었습니다.
    // 그 이유는
    // 1. 통신은 문제 없었지만 오류나 다른 이유로 리스트를 못받은 경우
    // 2. 통신 문제로 onFail 된 경우
    // 만약에 나눠서 메시지 처리를 다르게한다면을 고려해봤는데 이 부분은 불필요하다면 그냥 통일해서 진행해도 무방합니다.

    // 현재 switch 문 2개로 받도록 조치했는데 ApiCallback generic으로 한번에 처리된다면 바꿔보는거 해볼까 고민해봅시다.

    public void loadMypageData(int i){
        switch (i) {
            case 0:
                addSubscription(
                        apiService.getMypageBoardItem(),
                        new APICallback<QnaBoardList>() {

                            @Override
                            public void onSuccess(QnaBoardList qnaBoardList) {
                                if (qnaBoardList != null) {
                                    qnaBoardItems = new ArrayList(qnaBoardList.getQnaBoardItems());
                                    view.getQnaMyboardResponse(qnaBoardItems);
                                }
                                else{
                                    Log.i(TAG, "onSuccess: list = null");
                                    view.getDataFail(true);
                                }
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: mypage" + msg);
                                view.getDataFail(true);
                            }

                            @Override
                            public void onFinish() {
                                Log.i(TAG, "onFinish: mypage");
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
                                    view.getDataFail(true);
                                }
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: mypage" + msg);
                                view.getDataFail(true);
                            }

                            @Override
                            public void onFinish() {
                                Log.i(TAG, "onFinish: mypage");
                                //dismissProgressDialog();
                            }
                        });
                break;
            case 2:
                // 달력 대비해서 만들어뒀음
                // 나중에 결정되면 처리.
                break;
        }
    }
}
