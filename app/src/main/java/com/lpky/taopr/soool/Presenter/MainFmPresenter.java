package com.lpky.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lpky.taopr.soool.Networking.APICallback;
import com.lpky.taopr.soool.Networking.APIClient;
import com.lpky.taopr.soool.Networking.APIService;
import com.lpky.taopr.soool.Object.InfoItem;
import com.lpky.taopr.soool.Object.InfoList;
import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.Object.QnaBoardList;
import com.lpky.taopr.soool.Presenter.Interface.MainFmInter;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.lpky.taopr.soool.View.InfoDetailActivity;
import com.lpky.taopr.soool.View.QnaBoardDetailActivity;

import java.util.ArrayList;


public class MainFmPresenter extends BasePresenter implements MainFmInter{

    private Context context;
    private final int MAIN_MOVE_TO_QNA = 1100;
    private final int MAIN_MOVE_TO_INFO = 1200;
    private MainFmPresenter.View view;

    public int accountNo;
    private boolean isQnaBoardRes,isInfoRes; // 로딩화면을 위해 response 를 받았는지 안받은지에 대한 상태변수
    public boolean qnaBoardResSuccess, infoResSuccess; // qna,info 글 리스트를 서버로부터 성공적으로 받아 왔는지에 대한 상태변수
    private String TAG = "홈 메인 액티비티";

    public MainFmPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);
        getAccountNo();
    }

    @Override
    public void setView(MainFmPresenter.View view) {
        this.view = view;
    }

    private void getAccountNo(){
        this.accountNo = LoginSharedPreferences.getAccountNo(context,"LoginAccount");
    }

    @Override
    public void getIntentMoveQna(Activity activity, int qnaListPosition ,QnaBoardItem qnaBoardItem) {
        Intent intent = new Intent(activity, QnaBoardDetailActivity.class);
        intent.putExtra("qnaBoardItem",qnaBoardItem);
        intent.putExtra("fromActivity", 0);
        intent.putExtra("actionKind", 1);
        intent.putExtra("qnaListPosition",qnaListPosition);
        view.moveToPage(intent,MAIN_MOVE_TO_QNA);
    }

    @Override
    public void getIntentMoveInfo(Activity activity, int infoListPosition, InfoItem infoItem) {
        Intent intent = new Intent(activity, InfoDetailActivity.class);
        intent.putExtra("infoItem",infoItem);
        intent.putExtra("fromActivity", 0);
        intent.putExtra("actionKind", 1);
        intent.putExtra("infoPosition",infoListPosition);
        view.moveToPage(intent,MAIN_MOVE_TO_INFO);
    }

    // qna 게시글 서버에 요청하고 받아오는 부분 ( info 도 일치 )
    // 서버에 request 를 보낼 때는 로딩화면을 생성하고
    // Response 를 아직 받지 못했다는 상태를 표시하기 위해 isQnaBoardRes 의 값을 false 로 설정
    // Response 를 받았을 경우 isQnaBoardRes 값을 true 로 설정한 후에 checkLoading 함수로 이동해
    // info,qna 게시글 리스트에 대해서 모두 가져왔을 경우 로딩화면을 숨긴다.
    // 서버로부터 글 리스트를 성공적으로 받았다면 qnaBoardResSuccess 의 값을 true 실패 했다면 false
    // 그리고 이 상태값으로 Main 프래그먼트를 Home 액티비티에 부착할 경우 서버에 글 리스트를 요청할 지 말지 구분
    @Override
    public void loadQnaBoardList() {
        isQnaBoardRes = false;
        view.showLoading();

        addSubscription(
                apiService.mainGetQnaItem(accountNo),
                new APICallback<QnaBoardList>() {
                    @Override
                    public void onSuccess(QnaBoardList qnaBoardList) {
                        ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList(qnaBoardList.getQnaBoardItems());
                        view.getQnaBoardSuccess(qnaBoardItems);
                        qnaBoardResSuccess = true;

                    }

                    @Override
                    public void onFailure(String msg) {
                        qnaBoardResSuccess = false;
                        view.getDataFail(1); // dataType 값이 0 -> info / 1 -> qna
                    }

                    @Override
                    public void onFinish() {
                        isQnaBoardRes = true;

                        checkLoading();
                    }
                });
    }

    // info 게시글 서버에 요청하고 받아오는 부분
    @Override
    public void loadInfoList() {
        isInfoRes = false;
        view.showLoading();

        // 데이터 로딩
        addSubscription(
                apiService.mainGetInfoItem(accountNo),
                new APICallback<InfoList>() {
                    @Override
                    public void onSuccess(InfoList infoList) {

                        ArrayList<InfoItem> infoItems = new ArrayList(infoList.getInfoItems());
                        view.getInfoSuccess(infoItems);
                        infoResSuccess = true;

                    }

                    @Override
                    public void onFailure(String msg) {
                        qnaBoardResSuccess = false;
                        view.getDataFail(0); // dataType 값이 0 -> info / 1 -> qna
                    }

                    @Override
                    public void onFinish() {
                        isInfoRes = true;
                        checkLoading();

                    }
                }
        );

    }

    // qna 게시글과 info 게시글을 전부 가져왔을 경우에 로딩화면을 제거
    // isQnaBoardRes 와 isInfoRes 변수는 qna 와 info 게시글을 가져왔는지에 대한 상태값을 가지고 있는 변수
    // 값이 true 일 경우에는 Response 를 받은 것이고 , false 는 받지 않은 상태
    // 두 변수의 값이 true 일 경우에 로딩화면 숨김
    private void checkLoading(){
        if (isQnaBoardRes && isInfoRes){
            view.hideLoading();
        }
    }
}
