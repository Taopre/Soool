package com.example.taopr.soool.Model;

import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.LoginSessionItem;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Presenter.SignUpPresenter;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpModel {

    String TAG = "SignUpModel";
    String result;
    boolean duplicity = false;
    boolean signupStatus;

    private static String emailOrNick;
    private static APIClient apiClient;
    Call<ResponseBody> request = null;

    LoginSessionItem item;
    SignUpPresenter signUpPresenter;

    Context context;

    public SignUpModel(SignUpPresenter signUpPresenter, Context context) {
        this.signUpPresenter = signUpPresenter;
        this.context = context;
    }

    // presenter를 통해 전달 받은 구분자 값과 이메일 혹은 닉네임 값을 서버에 전달.
    // 1. retrofit 객체를 생성
    // 2. restAPI 명세에 맞는 인터페이스 생성
    // 서버는 중복여부에 관한 result값을 전달 받고

    //내가 수정해본 소스
    public void checkDuplicity(int separator, String emailOrNick) {
        //결과 값이 비동기처리가 되지않아서 false가 먼저 가버림.
        //rxjava2 retrofit 연동부분을 좀 더 파봐야할것 같다.

        Log.i(TAG, "checkDuplicity: 구분자 값 : " + separator + " , 중복체크 값 : " + emailOrNick);

        APIService service = APIClient.getClient().create(APIService.class);

        if(separator == 0){
            Log.i(TAG, "checkDuplicity: 00");
            request = service.checkEmailDup(emailOrNick);
        }

        else{
            Log.i(TAG, "checkDuplicity: 111");
            request = service.checkNickDup(emailOrNick);
        }

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

                            request.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    try {
//                                      Log.i(TAG, "onResponse: 결과 값 :" + response.message() + response.body().string());
                                        String msg = response.body().string();
                                        Log.d(TAG, "onResponse 1st : "+msg);
                                        JSONArray jsonArray = new JSONArray(msg);
                                        for (int i=0; i<jsonArray.length(); i++) {
                                            JSONObject returnData = jsonArray.getJSONObject(i);

                                            String result = returnData.getString("result");
                                            //result는 이메일 존재여부를 받는 변수.
                                            if(result.equals("true")) {
                                                Log.d(TAG, "onResponse true : true");
                                                duplicity = true;
                                                signUpPresenter.clickDuplicityResponse(separator, emailOrNick, duplicity);
                                            } else if (result.equals("false")) {
                                                Log.d(TAG, "onResponse false : false");
                                                duplicity = false;
                                                signUpPresenter.clickDuplicityResponse(separator, emailOrNick, duplicity);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    Log.d(TAG, "onFailure: 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            Log.d(TAG, "onFailure: 실패2");
                            Log.e(TAG, "apply: ", e);
                        }

                        return true;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onComplete()
                    {
                    }
                });
    }

    //void로 클래스화 처리하자 그냥 귀찮다. response 비동기처리 때문에. (주석처리해놈 원래 소스 부분)
//    public boolean checkDuplicity(int separator, String emailOrNick) {
//        //결과 값이 비동기처리가 되지않아서 false가 먼저 가버림.
//        //rxjava2 retrofit 연동부분을 좀 더 파봐야할것 같다.
//
//        Log.i(TAG, "checkDuplicity: 구분자 값 : " + separator + " , 중복체크 값 : " + emailOrNick);
//
//        APIService service = APIClient.getClient().create(APIService.class);
//
//        if(separator == 0){
//            Log.i(TAG, "checkDuplicity: 00");
//            request = service.checkEmailDup(emailOrNick);
//        }
//
//        else{
//            Log.i(TAG, "checkDuplicity: 111");
//            request = service.checkNickDup(emailOrNick);
//        }
//
//        Observable.just(emailOrNick)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .map(new Function<String, Boolean>()
//                {
//                    @Override
//                    public Boolean apply(String s) throws Exception
//                    {
//                        try
//                        {
//                            Log.d(TAG, "시작 바로 전");
//
//                            request.enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                                    try {
////                                      Log.i(TAG, "onResponse: 결과 값 :" + response.message() + response.body().string());
//                                        String msg = response.body().string();
//                                        Log.d(TAG, "onResponse 1st : "+msg);
//                                        JSONArray jsonArray = new JSONArray(msg);
//                                        for (int i=0; i<jsonArray.length(); i++) {
//                                            JSONObject returnData = jsonArray.getJSONObject(i);
//
//                                            String result = returnData.getString("result");
//                                            //result는 이메일 존재여부를 받는 변수.
//                                            if(result.equals("true")) {
//                                                Log.d(TAG, "onResponse true : true");
//                                                duplicity = true;
//
//                                            } else if (result.equals("false")) {
//                                                Log.d(TAG, "onResponse false : false");
//                                                duplicity = false;
//
//                                            }
//                                        }
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                    Log.d(TAG, "onFailure: 실패");
//                                    Log.e(TAG, "onFailure: ", t);
//                                }
//                            });
//                        }
//                        catch(Exception e) {
//                            Log.d(TAG, "onFailure: 실패2");
//                            Log.e(TAG, "apply: ", e);
//                        }
//
//                        return true;
//                    }
//                }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Boolean>()
//                {
//                    @Override
//                    public void onSubscribe(Disposable d)
//                    {
//                    }
//                    @Override
//                    public void onNext(Boolean s)
//                    {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                    @Override
//                    public void onComplete()
//                    {
//                    }
//                });
//        Log.d(TAG, "checkDuplicity: "+duplicity);
//        return duplicity;
//    }

    //테스트를 위해 boolean 함수 주석처리할게.
//    public boolean signUpReq(String accountEmail, String accountPW, String accountNick){
////        //회원가입 버튼 눌렀을때 처리할 부분.
////        APIService service = APIClient.getClient().create(APIService.class);
////
////        Call<ResponseBody> callServer = service.signUpRes(accountEmail, accountPW, accountNick);
////        callServer.enqueue(new Callback<ResponseBody>() {
////            @Override
////            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
////                try {
////                    String msg = response.body().string();
////                    Log.d(TAG, "onResponse 1st : "+msg);
////                    JSONArray jsonArray = new JSONArray(msg);
////                    for (int i=0; i<jsonArray.length(); i++) {
////                        JSONObject returnData = jsonArray.getJSONObject(i);
////
////                        String result = returnData.getString("result");
////                        int accountNo = returnData.getInt("accountNo");
////                        int accountPoint = returnData.getInt("accountPoint");
////                        //result는 이메일 존재여부를 받는 변수.
////                        if(result.equals("true")) {
////                            Log.d(TAG, "onResponse true : true");
////
////                            item = new LoginSessionItem(accountNo, accountNick, "/profileimage/defualtimage.png", accountPoint, 0, 0, true);
////                            // Gson 인스턴스 생성
////                            Gson gson = new GsonBuilder().create();
////                            // JSON 으로 변환
////                            String userClass = gson.toJson(item, LoginSessionItem.class);
////                            //shared에 객체 저장
////                            LoginSharedPreferences.LoginUserSave(context, "LoginAccount", userClass);
////
////                            signupStatus = true;
////                        } else if (result.equals("false")) {
////                            Log.d(TAG, "onResponse false : false");
////                            signupStatus = false;
////                        }
////                    }
////                }
////                catch (IOException e) {
////                    e.printStackTrace();
////                }
////                catch (JSONException e) {
////                    e.printStackTrace();
////                }
////
////            }
////
////            @Override
////            public void onFailure(Call<ResponseBody> call, Throwable t) {
////
////                Log.d(TAG, "onFailure: 실패");
////                Log.e(TAG, "onFailure: ", t);
////            }
////        });
////
////        return signupStatus;
////    }
    public void signUpReq(String accountEmail, String accountPW, String accountNick){
        //회원가입 버튼 눌렀을때 처리할 부분.

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

                            APIService service = APIClient.getClient().create(APIService.class);

                            Log.d(TAG, "apply: 회원가입때 보낼 데이터"+accountEmail+"//"+accountPW+"//"+accountNick);
                            Call<ResponseBody> request = service.signUpRes(accountEmail, accountPW, accountNick);

                            request.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();
                                        Log.d(TAG, "onResponse 1st : "+msg);
                                        JSONArray jsonArray = new JSONArray(msg);
                                        for (int i=0; i<jsonArray.length(); i++) {
                                            JSONObject returnData = jsonArray.getJSONObject(i);

                                            String result = returnData.getString("result");
                                            int accountNo = returnData.getInt("accountNo");
                                            int accountPoint = returnData.getInt("accountPoint");

                                            if(result.equals("true")) {
                                                Log.d(TAG, "onResponse true : true");

                                                item = new LoginSessionItem(accountNo, accountNick, "/profileimage/defualtimage.png", accountPoint, 0, 0, true);
                                                // Gson 인스턴스 생성
                                                Gson gson = new GsonBuilder().create();
                                                // JSON 으로 변환
                                                String userClass = gson.toJson(item, LoginSessionItem.class);
                                                //shared에 객체 저장
                                                LoginSharedPreferences.LoginUserSave(context, "LoginAccount", userClass);

                                                signUpPresenter.signUpReqResponse(true);
                                            } else if (result.equals("false")) {
                                                Log.d(TAG, "onResponse false : false");
                                                signUpPresenter.signUpReqResponse(false);
                                            }
                                        }
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    Log.d(TAG, "onFailure: 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            Log.d(TAG, "onFailure: 실패2");
                            Log.e(TAG, "apply: ", e);
                        }

                        return true;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onComplete()
                    {
                    }
                });

    }

}
