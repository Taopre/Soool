package com.example.taopr.soool.Model;

import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaVotePresenter;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class QnaVoteModel {

    String TAG = "QnaVoteModel", accountNick, result;

    private QnaVotePresenter qnaVotePresenter;

    Context context;

    public QnaVoteModel(QnaVotePresenter qnaVotePresenter, Context context) {
        this.qnaVotePresenter = qnaVotePresenter;
        this.context = context;
    }

    // 뷰로부터 객체를 받아와 서버에 저장하기 위해 만든 메서드.

    public void enrollmentVoteReqFromView(QnaVoteItem item) {
        Log.d(TAG, "enrollmentVoteReqFromView: "+item.getTitle()+item.getContent()+item.getFirst_image()+item.getSecond_image());

        // 쉐어드에서 로그인 유저 닉네임을 얻어내는 부분.

        String data = LoginSharedPreferences.LoginUserLoad(context, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNick = loginSessionItem.getAccountNick();

        // 이미지 값을 파일로 저장하여 서버로 전달.
        // 이 부분은 4/6일 상황에서 디버깅을 아직 해보지 못한 부분이다.

        File firstImage = new File(item.getFirst_image());
        File secondImage = new File(item.getSecond_image());

        Observable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<String, Boolean>()
                {
                    @Override
                    public Boolean apply(String s) throws Exception
                    {
                        try
                        {
                            Log.d(TAG, "시작 바로 전");

                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("accountNick", accountNick)
                                    .addFormDataPart("qnaVoteTitle", item.getTitle())
                                    .addFormDataPart("qnaVoteContent", item.getContent())
                                    .addFormDataPart("qnaVote1stImg", firstImage.getName(), RequestBody.create(MultipartBody.FORM, firstImage))
                                    .addFormDataPart("qnaVote2ndImg",secondImage.getName(), RequestBody.create(MultipartBody.FORM, secondImage))
                                    .build();

                            Request request = new Request.Builder()
                                    .url("http://54.180.90.184/qnapost/postWrite.php") // php경로 바꿔줘야함.
                                    .post(requestBody)
                                    .build();

                            OkHttpClient client = new OkHttpClient();

                            client.newCall(request).enqueue(new okhttp3.Callback() {
                                @Override
                                public void onFailure(okhttp3.Call call, IOException e) {
                                    Log.d(TAG, "onFailure: ",e);
                                }

                                @Override
                                public void onResponse(okhttp3.Call call, okhttp3.Response response){
                                    try {
                                        String msg = response.body().string();
                                        Log.d(TAG, "onResponse okhttp: " + msg);
                                        JSONArray jsonArray = new JSONArray(msg);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject returnData = jsonArray.getJSONObject(i);

                                            result = returnData.getString("result");

                                            if(result.equals("true")){
                                                Log.d(TAG, "onResponse: 인서트 성공");
                                                qnaVotePresenter.enrollmentVoteResp(true);
                                            }else {
                                                Log.d(TAG, "onResponse: 인서트 실패");
                                                qnaVotePresenter.enrollmentVoteResp(false);
                                            }
                                        }
                                    }catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        catch(Exception e) {
                            Log.d(TAG, "onFailure : 실패2");
                            Log.e(TAG, "apply : ", e);
                        }

                        return true;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.d(TAG, "onSubscribe 이미지 있을때 이미지뺴고 보내는 부분: 구독!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext 이미지 있을때 이미지뺴고 보내는 부분: 다음!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError 이미지 있을때 이미지뺴고 보내는 부분: 에러발생!!!!!!!!!!!!!!!!!!",e);
                    }
                    @Override
                    public void onComplete()
                    {
                        Log.d(TAG, "onComplete 이미지 있을때 이미지뺴고 보내는 부분: 완료!!!!!!!!!!!!!!!!!!!!!!");
                    }
                });
    }
}
