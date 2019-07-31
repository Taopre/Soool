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
import com.lpky.taopr.soool.Presenter.Interface.BookmarkInter;
import com.lpky.taopr.soool.View.InfoDetailActivity;

import java.util.ArrayList;

public class BookmarkPresenter extends BasePresenter implements BookmarkInter {
    private Context context;
    private BookmarkInter.View view;
    public Activity activity;
    private String TAG = "북마크_presenter";
    private final int bookmarkToDetail = 1212;

    public BookmarkPresenter(Context context) {
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void sendItem(InfoItem infoItem, Activity activity, int infoPosition) {

        Intent intent = new Intent(activity, InfoDetailActivity.class);
        intent.putExtra("infoItem",infoItem);
        intent.putExtra("fromActivity", 0);
        intent.putExtra("actionKind", 1);
        intent.putExtra("infoPosition", infoPosition);

        view.showInfoDetail(intent, bookmarkToDetail);
    }

    @Override
    public void loadData(int accountNo, int loadingKind) {

        if (loadingKind == 0) {
            //TODO: loading status를 MainActivity -> mypageFragment로 전달해야.
            //view.showLoading();
        }

        // 데이터 로딩
        addSubscription(
                apiService.getBookmarkedItem(accountNo),
                new APICallback<InfoList>() {
                    @Override
                    public void onSuccess(InfoList infoList) {

                        if (infoList != null) {


                            ArrayList<InfoItem> infoItems = new ArrayList(infoList.getInfoItems());
                            view.getDataSuccess(infoItems, loadingKind);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.getDataFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        Log.i(TAG, "onFinish: info");
                    }
                }
        );
    }

}
