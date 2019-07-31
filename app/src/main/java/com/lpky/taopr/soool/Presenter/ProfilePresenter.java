package com.lpky.taopr.soool.Presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.lpky.taopr.soool.Networking.APICallback;
import com.lpky.taopr.soool.Networking.APIClient;
import com.lpky.taopr.soool.Networking.APIService;
import com.lpky.taopr.soool.Object.ProfileInfo;
import com.lpky.taopr.soool.Object.SooolResponseBody;
import com.lpky.taopr.soool.Presenter.Interface.ProfileInter;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfilePresenter extends BasePresenter implements ProfileInter{
    private View view;
    private String TAG = "마이 프로필 프레젠터" ;
    private Context context;
    private int accountNo;

    @Override
    public void setView(View view,Context context) {
        this.view = view;
        this.apiService = APIClient.getClient1().create(APIService.class);
        this.context = context;
    }


    // 프로필 정보 가져오기
    @Override
    public void getProfileInfo(int accountNo) {
        this.accountNo = accountNo;

        view.showLoading();

        addSubscription(
                apiService.getProfileInfo(accountNo ),
                new APICallback<ProfileInfo>() {
                    @Override
                    public void onSuccess(ProfileInfo profileInfo) {
                        view.getProfileInfoSuccess(profileInfo);
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.getDataFail(0);
                    }

                    @Override
                    public void onFinish() {
                        view.hideLoading();
                    }
                });

    }

    // 프로필 이미지 변경
    @Override
    public void changeProfileImage(Uri uri) {

        view.showLoading();

        File file = new File(getPathFromUri(uri));

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part accountImageBody = MultipartBody.Part.createFormData("accountImage", file.getName(), requestBody);

        addSubscription(
                apiService.changeProfileImage(accountNo,accountImageBody),
                new APICallback<SooolResponseBody>() {
                    @Override
                    public void onSuccess(SooolResponseBody sooolResponseBody) {
                        Log.i(TAG, "onSuccess: 이미지 "+ sooolResponseBody.getResult());
                        view.changeProfileImgSuccess(sooolResponseBody.getResult());
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure: msg");
                        view.getDataFail(1);

                    }

                    @Override
                    public void onFinish() {
                        view.hideLoading();
                    }
                });
    }

    // 변경한 프로필 정보 서버에 전달
    @Override
    public void changeProfileInfo(ProfileInfo profileInfo) {

        view.showLoading();
        addSubscription(
                apiService.changeProfileInfo(accountNo,profileInfo.getAccountEmail(),profileInfo.getAccountNick()
                , profileInfo.getAccountImage(),profileInfo.getAccountPw(),profileInfo.getCapacity() ,
                        profileInfo.getFavorite(), profileInfo.getReason()),
                new APICallback<ProfileInfo>() {
                    @Override
                    public void onSuccess(ProfileInfo changedProfileInfo) {
                        Log.i(TAG, "onSuccess: " + changedProfileInfo.getAccountEmail());
                        view.changeProfileInfoSuccess(changedProfileInfo);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure: msg");
                        view.getDataFail(2);

                    }

                    @Override
                    public void onFinish() {
                        view.hideLoading();
                    }
                });
    }

    // 프로필 이미지 삭제
    @Override
    public void deleteProfileImg() {
        view.showLoading();
        addSubscription(
                apiService.deleteProfileImage(accountNo,"true"),
                new APICallback<SooolResponseBody>() {
                    @Override
                    public void onSuccess(SooolResponseBody sooolResponseBody) {
                        Log.i(TAG, "onSuccess: " + sooolResponseBody.getResult());
                        view.deleteProfileImgSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure: msg");
                        view.getDataFail(2);

                    }

                    @Override
                    public void onFinish() {
                        view.hideLoading();
                    }
                });
    }

    // 회원 탈퇴
    // 회원 번호를 받아와 서버에 전달
    // 회원 탈퇴에 대한 result 값으로 true / false 를 받아와 처리

    @Override
    public void deleteAccount(int accountNo) {
        view.showLoading();
        addSubscription(
                apiService.deleteAccount(accountNo),
                new APICallback<SooolResponseBody>() {
                    @Override
                    public void onSuccess(SooolResponseBody sooolResponseBody) {
                        Log.i(TAG, "onSuccess: " + sooolResponseBody.getResult());
                        view.deleteAccountSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.getDataFail(4);

                    }

                    @Override
                    public void onFinish() {
                        view.hideLoading();
                    }
                });
    }


    // 이미지 uri 를 file path 로 변경
    private String getPathFromUri(Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        cursor.moveToNext();

        String path = cursor.getString(cursor.getColumnIndex("_data"));

        cursor.close();

        return path;
    }
}
