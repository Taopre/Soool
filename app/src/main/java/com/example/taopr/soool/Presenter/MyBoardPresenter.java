package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardList;
import com.example.taopr.soool.Presenter.Interface.MyBoardInter;
import com.example.taopr.soool.View.QnaBoardDetailActivity;

import java.util.ArrayList;

public class MyBoardPresenter extends BasePresenter implements MyBoardInter {
    private Context context;
    private MyBoardInter.View view;

    private String TAG = "큐앤에이_presenter";
    private final int MY_BOARD_MOVE_TO_WRITE = 4110;
    private final int MY_BOARD_MOVE_TO_DETAIL= 4120;


    public MyBoardPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    // 아이템 선택 시 선택한 아이템 상세 보기로 이동
    // 선택한 아이템 객체와 아이템의 position 값을 보낸다
    // 보내는 이유는 position 값을 QnaBoard , QnaDetail 액티비티로 이동해서 수정이나 삭제를 수행하고
    // Qna 액티비티로 이동했을 때 리스트의 아이템을 업데이트 할 때 position 값이 있어야 하기 때문에

    @Override
    public void getItem(QnaBoardItem qnaBoardItem, Activity activity, int qnaListPosition) {
        Intent intent = new Intent(activity, QnaBoardDetailActivity.class);
        intent.putExtra("qnaBoardItem",qnaBoardItem);
        intent.putExtra("fromActivity", 0);
        intent.putExtra("actionKind", 1);
        intent.putExtra("qnaListPosition",qnaListPosition);
        view.moveToPage(intent,MY_BOARD_MOVE_TO_DETAIL);
    }

    // actionKind 가 1일 경우에는 새로고침이므로 새로고침 아이콘을 통해 로딩중을 표시하기 때문에
    // 프로그래스바는 실행하지 않도록 한다

    @Override
    public void loadData(int accountNo, int actionKind) {
        if (actionKind==0) {
            view.showLoading(); //로딩 화면
        }
        //데이터 로딩
        addSubscription(
                apiService.getMypageBoardItem(accountNo),
                new APICallback<QnaBoardList>() {

                    @Override
                    public void onSuccess(QnaBoardList qnaBoardList) {

                        ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList(qnaBoardList.getQnaBoardItems());
                        view.getDataSuccess(qnaBoardItems);

                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure: qna" + msg);
                        view.getDataFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        Log.i(TAG, "onFinish: qna");
                        view.hideLoading();
                    }
                });
    }
}
