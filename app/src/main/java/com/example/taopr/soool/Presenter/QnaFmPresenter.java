package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardList;
import com.example.taopr.soool.Presenter.Interface.QnaFmInter;
import com.example.taopr.soool.View.QnaBoardDetailActivity;

import java.util.ArrayList;

public class QnaFmPresenter extends BasePresenter implements QnaFmInter {

    // 서버에서 데이터를 가져온 후 리스트로 만들어서 전송
    private Context context;
    private QnaFmPresenter.View view;
    private Activity activity;
    //  private List<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private String TAG = "큐앤에이_presenter";
    private APIService apiService;
    private final int QNA_MOVE_TO_DETAIL= 3100;
    private final int QNA_MOVE_TO_WRITE = 3200;


    public QnaFmPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }

    public void loadData(){
        // view.showLoading(); 로딩 화면
        Log.i(TAG, "loadData: ");
        //<
        //데이터 로딩
        addSubscription(
                apiService.getQnaItem(),
                new APICallback<QnaBoardList>() {

                    @Override
                    public void onSuccess(QnaBoardList qnaBoardList) {

                        if (qnaBoardList != null) {

                            ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList(qnaBoardList.getQnaBoardItems());
                            view.getDataSuccess(qnaBoardItems);
                            Log.d(TAG, "onSuccess: "+qnaBoardItems.get(0).getPostNo());
                            //  view.getDataSuccess(qnaBoardList.getQnaBoardItems());
                        }
                        else{
                            Log.i(TAG, "onSuccess: list = null");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure: qna" + msg);
                        view.getDataFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        Log.i(TAG, "onFinish: qna");
                        //dismissProgressDialog();
                    }
                });

    }

    // 아이템 선택 시 선택한 아이템 상세 보기로 이동
    // 선택한 아이템 객체와 아이템의 position 값을 보낸다
    // 보내는 이유는 position 값을 QnaBoard , QnaDetail 액티비티로 이동해서 수정이나 삭제를 수행하고
    // Qna 액티비티로 이동했을 때 리스트의 아이템을 업데이트 할 때 position 값이 있어야 하기 때문에

    @Override
    public void getItem(QnaBoardItem qnaBoardItem, Activity activity,int qnaListPosition) {
        this.activity = activity;
        Toast.makeText(activity, qnaBoardItem.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, QnaBoardDetailActivity.class);
        intent.putExtra("qnaBoardItem",qnaBoardItem);
        intent.putExtra("fromActivity", 0);
        intent.putExtra("actionKind", 1);
        intent.putExtra("qnaListPosition",qnaListPosition);

        //activity.startActivityForResult(intent, QNA_MOVE_TO_DETAIL);
        view.moveToPage(intent,QNA_MOVE_TO_DETAIL);

    }

    @Override
    public void setView(QnaFmPresenter.View view) {
        this.view = view;
    }

}
