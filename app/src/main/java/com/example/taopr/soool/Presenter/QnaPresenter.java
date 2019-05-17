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
import com.example.taopr.soool.Presenter.Interface.QnaInter;
import com.example.taopr.soool.View.QnaBoardDetailActivity;

import java.util.ArrayList;


public class QnaPresenter extends BasePresenter implements QnaInter {

    // 생성장에서 context 전달 받기
    // 그 후 성공, 실패 시 info 액티비티의 successdata() 함수와 fail 함수 가져오기
    private Context context;
    private QnaPresenter.View view;
    private Activity activity;
  //  private List<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private static String TAG = "큐앤에이_presenter";
    private final int QNA_MOVE_TO_DETAIL= 2100;
    private final int QNA_MOVE_TO_WRITE = 2200;

 //<
    private APIService apiService;
    //>

    public QnaPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }


    // 서버에서 데이터를 가져온 후 리스트로 만들어서 전송

    public void loadData(){

        // view.showLoading(); 로딩 화면

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
    //>

    /// 아이템 선택 시 선택한 아이템 상세 보기로 이동

    public void getItem(QnaBoardItem qnaBoardItem, Activity activity) {
        this.activity = activity;
        Toast.makeText(activity, qnaBoardItem.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, QnaBoardDetailActivity.class);
        intent.putExtra("QnaBoardItem",qnaBoardItem);
        intent.putExtra("fromActivity", 0);
        intent.putExtra("actionKind", 1);

        activity.startActivityForResult(intent, QNA_MOVE_TO_DETAIL);
    }


    @Override
    public void setView(QnaPresenter.View view) {
        this.view = view;
    }


}
