package com.example.taopr.soool.Presenter;

import android.util.Log;

import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.ChangeProfile;
import com.example.taopr.soool.Object.InfoOfSooolList;
import com.example.taopr.soool.Presenter.Interface.ProfileInter;

import java.util.ArrayList;

public class ProfilePresenter extends BasePresenter implements ProfileInter{
    private View view;
    private String TAG = "마이 프로필 프레젠터" ;

    @Override
    public void setView(View view) {
        this.view = view;
        this.apiService = APIClient.getClient1().create(APIService.class);
    }

    @Override
    public void getProfileInfo(int accountNo) {
        addSubscription(
                apiService.getChangeProfile(accountNo ),
                new APICallback<ChangeProfile>() {

                    @Override
                    public void onSuccess(ChangeProfile changeProfile) {
                        Log.i(TAG, "onSuccess: 프로필 "+ changeProfile.getAccountEmail());
                        view.getChangeProfileSuccess(changeProfile);
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.getDataFail(0);
                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }
}
