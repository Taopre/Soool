package com.lpky.taopr.soool.Presenter;

import android.content.Context;
import android.util.Log;

import com.lpky.taopr.soool.Networking.APICallback;
import com.lpky.taopr.soool.Networking.APIClient;
import com.lpky.taopr.soool.Networking.APIService;
import com.lpky.taopr.soool.Object.UserProfile;
import com.lpky.taopr.soool.Presenter.Interface.MypageFmInter;

public class MypageFmPresenter extends BasePresenter implements MypageFmInter {

    private APIService apiService;
    private Context context;
    private MypageFmPresenter.View view;
    private String TAG = "홈 마이페이지 프레젠터";

    public MypageFmPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    // 유저 프로필 정보 가져오는 부분

    @Override
    public void loadMypageData(int accountNo){

        addSubscription(
                apiService.getUserProfile(accountNo),
                new APICallback<UserProfile>() {

                    @Override
                    public void onSuccess(UserProfile userProfile) {
                        if (userProfile != null) {
                            view.getUserProfileSuccess(userProfile);
                        }
                        else{
                            Log.i(TAG, "onSuccess: list = null");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure: " );
                        view.getDataFail(true, 2);
                    }

                    @Override
                    public void onFinish() {

                        // TODO: 데이터 가져오는 동안 로딩화면을 보여주고 데이터를 가져왔을 때 로딩화면 종료하는 부분
                        //dismissProgressDialog();
                    }
                });

    }


}
