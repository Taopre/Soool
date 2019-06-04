package com.example.taopr.soool.Presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.ProfileInfo;
import com.example.taopr.soool.Object.SooolResponseBody;
import com.example.taopr.soool.Presenter.Interface.ProfileInter;

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

    @Override
    public void getProfileInfo(int accountNo) {
        this.accountNo = accountNo;
        addSubscription(
                apiService.getProfileInfo(accountNo ),
                new APICallback<ProfileInfo>() {
                    @Override
                    public void onSuccess(ProfileInfo profileInfo) {
                        Log.i(TAG, "onSuccess: 프로필 "+ profileInfo.getAccountEmail());
                        view.getProfileInfoSuccess(profileInfo);
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

    @Override
    public void changeProfileImage(Uri uri) {

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
                        //dismissProgressDialog();
                    }
                });
    }

    // 변경한 프로필 정보 서버에 전달
    @Override
    public void changeProfileInfo(ProfileInfo profileInfo) {
        Log.i(TAG, "changeProfileInfo: " + profileInfo.getAccountEmail());
        Log.i(TAG, "changeProfileInfo: " + profileInfo.getCapacity());
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
                        //dismissProgressDialog();
                    }
                });
    }

    // 프로필 이미지 삭제
    @Override
    public void deleteProfileImg() {
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
                        //dismissProgressDialog();
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
