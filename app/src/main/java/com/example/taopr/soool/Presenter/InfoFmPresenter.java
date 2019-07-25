package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Object.InfoList;
import com.example.taopr.soool.Presenter.Interface.InfoFmInter;
import com.example.taopr.soool.View.InfoDetailActivity;


import java.util.ArrayList;

public class InfoFmPresenter extends BasePresenter implements InfoFmInter {


    public Context context;
    public InfoFmPresenter.View view;
    public Activity activity;
    //private ArrayList<InfoItem> infoItems = new ArrayList<>();
    private static String TAG = "정보_presenter";
    private final int infoToDetail = 1212;


    public InfoFmPresenter(Context context){

        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class); // getClient1인 이유는 뭐지?
    }

    // 서버에서 데이터를 가져온 후 객체로 만들어서 view에 전달
    // 서버에 처음 데이터를 요청하는 경우와 새로고침하는 경우를 구분
    public void loadData(int loadingKind, int accountNo, int lastPostNo){

        // loadingKind refreshLayout 쓸 때.
        if (loadingKind == 0) {
            view.showLoading(); // 로딩화면 보여주기
        }

        // 데이터 로딩
        addSubscription(
                apiService.getInfoItem(accountNo, lastPostNo),
                new APICallback<InfoList>() {
                    @Override
                    public void onSuccess(InfoList infoList) {

                        if (infoList !=null) {

                            ArrayList<InfoItem> infoItems = new ArrayList(infoList.getInfoItems());
                            view.getDataSuccess(infoItems, loadingKind);

                        } else {
                            Log.i(TAG, "onSuccess: infoList = null");
                        }

                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure : info " + msg);
                        view.getDataFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        Log.i(TAG, "onFinish: info");
                        view.hideLoading();

                    }
                }
        );
    }

    // 아이템 선택 시 선택한 아이템 상세 보기로 이동
    @Override
    public void sendItem(InfoItem infoItem, Activity activity, int infoPosition) {
        this.activity = activity;
        //Toast.makeText(activity, infoItem.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, InfoDetailActivity.class);
        intent.putExtra("infoItem",infoItem);
        intent.putExtra("fromActivity", 0);
        intent.putExtra("actionKind", 1);
        intent.putExtra("infoPosition", infoPosition);

        view.showInfoDetail(intent, infoToDetail);

    }

    @Override
    public void setView(InfoFmPresenter.View view) {
        this.view = view;
    }

}
