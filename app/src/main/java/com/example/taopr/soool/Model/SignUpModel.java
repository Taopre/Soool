package com.example.taopr.soool.Model;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Presenter.SignUpPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.Util.DeCryptor;
import com.example.taopr.soool.Util.EnCryptor;
import com.example.taopr.soool.View.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
                                        Log.i(TAG, "onResponse: IOException");
                                    } catch (JSONException e) {
                                        Toast.makeText(context, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
                                        Log.i(TAG, "onResponse: JSONException");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    signUpPresenter.signUpReqResponse(false,"");
                                    Log.i(TAG, "onFailure: ");
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

        Log.i(TAG, "암호 signUpReq: 0");

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
                                                Log.i(TAG, "onResponse: 암호 " + accountNo);
                                                LoginSharedPreferences.LoginUserSave(context, "LoginAccount", userClass);
                                                Log.i(TAG, "onCreate: 비번" + LoginSharedPreferences.getAccountNo(context,"LoginAccount"));
                                                //회원가입 성공을 View에게 전송.
                                                signUpPresenter.signUpReqResponse(true,String.valueOf(accountNo));
                                            } else if (result.equals("false")) {
                                                Log.d(TAG, "onResponse false : false");
                                                //회원가입 실패를 View에게 전송.
                                                signUpPresenter.signUpReqResponse(false,"");
                                            }
                                        }
                                    }

                                    catch (IOException e) {
                                        signUpPresenter.signUpReqResponse(false,"");
                                        Log.i(TAG, "Sign Up onResponse: IOException");
                                    }
                                    catch (JSONException e) {
                                        Toast.makeText(context, context.getString(R.string.toast_notice_page_error), Toast.LENGTH_SHORT).show();
                                        Log.i(TAG, "Sign Up onResponse: JSONException");
                                    }
                                }


                                // 우선은 회원가입에 실패하는 경우 모두 다같이 처리 중
                                // TODO: 후에 각각 상황에 맞는 처리를 해줘야 할듯
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    signUpPresenter.signUpReqResponse(false,"");
                                    Log.i(TAG, "Sign Up onFailure: ");
                                    Toast.makeText(context, context.getString(R.string.toast_notice_page_error), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        catch(Exception e) {
                            signUpPresenter.signUpReqResponse(false,"");
                            Log.i(TAG, "Sign Up apply: Exception");
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
