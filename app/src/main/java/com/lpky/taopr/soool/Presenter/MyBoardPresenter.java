package com.lpky.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lpky.taopr.soool.Networking.APICallback;
import com.lpky.taopr.soool.Networking.APIClient;
import com.lpky.taopr.soool.Networking.APIService;
import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.Object.QnaBoardList;
import com.lpky.taopr.soool.Presenter.Interface.MyBoardInter;
import com.lpky.taopr.soool.View.QnaBoardDetailActivity;

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

    // loadingKind 가 1 일때는 새로고침
    // 새로고침을 구별한 이유는 새로고침을 경우 게시글 리스트를 포맷한 후에 새로 받아야하기 때문에

    @Override
    public void loadData(int accountNo, int loadingKind,int lastPostNo) {

        //데이터 로딩
        addSubscription(
                apiService.getMypageBoardItem(accountNo,lastPostNo),
                new APICallback<QnaBoardList>() {

                    @Override
                    public void onSuccess(QnaBoardList qnaBoardList) {

                        ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList(qnaBoardList.getQnaBoardItems());
                        view.getDataSuccess(qnaBoardItems,loadingKind);

                    }

                    @Override
                    public void onFailure(String msg) {
                        view.getDataFail(msg);
                    }

                    @Override
                    public void onFinish() {
                       // view.hideLoading();
                    }
                });
    }
}
