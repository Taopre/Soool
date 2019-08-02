package com.lpky.taopr.soool.Model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lpky.taopr.soool.Object.LoginSessionItem;
import com.lpky.taopr.soool.Networking.APIClient;
import com.lpky.taopr.soool.Networking.APIService;
import com.lpky.taopr.soool.Presenter.SignUpPresenter;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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


    public void checkDuplicity(int separator, String emailOrNick) {


        APIService service = APIClient.getClient().create(APIService.class);
        //separator로 무슨 중복체크인지 구별하기위해.
        if(separator == 0){
            request = service.checkEmailDup(0,emailOrNick);
        }

        else{
            request = service.checkNickDup(1,emailOrNick);
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
                            request.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
//                                      Log.i(TAG, "onResponse: 결과 값 :" + response.message() + response.body().string());
                                        String msg = response.body().string();
                                       // JSONArray jsonArray = new JSONArray(msg);
                                        JSONObject returnData = new JSONObject(msg);
                                        /*for (int i=0; i<jsonArray.length(); i++) {
                                            JSONObject returnData = jsonArray.getJSONObject(i);*/

                                            String result = returnData.getString("result");

                                            if(result.equals("true")) {
                                                duplicity = true;
                                                signUpPresenter.clickDuplicityResponse(separator, emailOrNick, duplicity);
                                            } else if (result.equals("false")) {
                                                duplicity = false;
                                                signUpPresenter.clickDuplicityResponse(separator, emailOrNick, duplicity);
                                            }
                                        //}
                                    } catch (IOException e) {
                                        signUpPresenter.signUpReqResponse(false,"");
                                    } catch (JSONException e) {
                                        Toast.makeText(context, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    signUpPresenter.signUpReqResponse(false,"");
                                }
                            });
                        }
                        catch(Exception e) {
                            signUpPresenter.signUpReqResponse(false,"");
                            Log.i(TAG, "apply: Exception");
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


                            APIService service = APIClient.getClient().create(APIService.class);


                            Call<ResponseBody> request = service.signUpRes(accountEmail, accountPW, accountNick);


                            request.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();
                                        JSONArray jsonArray = new JSONArray(msg);
                                        for (int i=0; i<jsonArray.length(); i++) {
                                            JSONObject returnData = jsonArray.getJSONObject(i);

                                            String result = returnData.getString("result");
                                            int accountNo = returnData.getInt("accountNo");

                                            if(result.equals("true")) {

                                                item = new LoginSessionItem(accountNo, accountNick, "/profileimage/defualtimage.png", 50, 0, 0, true);
                                                // Gson 인스턴스 생성
                                                Gson gson = new GsonBuilder().create();
                                                // JSON 으로 변환
                                                String userClass = gson.toJson(item, LoginSessionItem.class);
                                                //shared에 객체 저장
                                                LoginSharedPreferences.LoginUserSave(context, "LoginAccount", userClass);
                                                //회원가입 성공을 View에게 전송.
                                                signUpPresenter.signUpReqResponse(true,String.valueOf(accountNo));
                                            } else if (result.equals("false")) {
                                                //회원가입 실패를 View에게 전송.
                                                signUpPresenter.signUpReqResponse(false,"");
                                            }
                                        }
                                    }

                                    catch (IOException e) {
                                        signUpPresenter.signUpReqResponse(false,"");
                                    }
                                    catch (JSONException e) {
                                        Toast.makeText(context, context.getString(R.string.toast_notice_page_error), Toast.LENGTH_SHORT).show();
                                    }
                                }


                                // 우선은 회원가입에 실패하는 경우 모두 다같이 처리 중
                                // TODO: 후에 각각 상황에 맞는 처리를 해줘야 할듯
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    signUpPresenter.signUpReqResponse(false,"");
                                    Toast.makeText(context, context.getString(R.string.toast_notice_page_error), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        catch(Exception e) {
                            signUpPresenter.signUpReqResponse(false,"");
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
