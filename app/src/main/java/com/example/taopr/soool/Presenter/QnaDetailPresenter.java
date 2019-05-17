package com.example.taopr.soool.Presenter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.ResponseVote;
import com.example.taopr.soool.Presenter.Interface.QnaDetailInter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

public class QnaDetailPresenter extends BasePresenter implements QnaDetailInter {

    private APIService apiService;
    private Context context;
    private QnaDetailPresenter.View view;
    private String TAG = "QnaDetailPresenter";

    JSONArray jsonArray;
    JSONObject returnData;

    String result, count;
    int qnaVoteStatus;
    ArrayList<String> voteList = new ArrayList<>();
    ArrayList<Integer> voteResult = new ArrayList<>();
    QnaVoteItem receiveQnaVoteItem = new QnaVoteItem();


    public QnaDetailPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void downloadVoteData(int accountNo, int postNo) {
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
                            //Retrofit 사용 시 apiservice와 apiclient를 사용하자.
                            //Retrofit 객체 불러와서 선언하는 부분.
                            apiService = APIClient.getClient1().create(APIService.class);
                            Call<ResponseBody> callServer = apiService.receiveVoteItem(accountNo, postNo);

                            callServer.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();
                                        
                                        jsonArray = new JSONArray(msg);
                                        returnData = jsonArray.getJSONObject(0);
                                        result = returnData.getString("result");

                                        if (result.equals("true")) {
                                            Log.d(TAG, "onResponse: true상태이네!!!!!!!!");
                                            qnaVoteStatus = returnData.getInt("qnaVoteStatus");
                                            count = returnData.getString("count");

                                            try {
                                                for (int j=0; j<Integer.parseInt(count); j++) {
                                                    voteList.add(returnData.getString("voteContent"+(j+1)));
                                                }

                                                for (int j=0; j<Integer.parseInt(count); j++) {
                                                    voteResult.add(returnData.getInt("voteResult"+(j+1)));
                                                }
                                            }catch (NullPointerException e) {
                                                Log.d(TAG, "exception null : "+e);
                                            }

                                            receiveQnaVoteItem.setQnaVoteStatus(qnaVoteStatus);
                                            if (qnaVoteStatus == 0) {
                                                receiveQnaVoteItem.setVoteText(voteList);

                                                for (int i=0; i<receiveQnaVoteItem.getVoteText().size(); i++)
                                                    Log.d(TAG, "vote text value : "+receiveQnaVoteItem.getVoteText().get(i));

                                                if (voteResult.size() > 0) {
                                                    receiveQnaVoteItem.setVoteResult(voteResult);
                                                    for (int i=0; i<voteResult.size(); i++)
                                                        Log.d(TAG, "vote result value: " + voteResult.get(i));
                                                }
                                            } else if (qnaVoteStatus == 1) {
                                                receiveQnaVoteItem.setVoteImage(voteList);

                                                for (int i=0; i<receiveQnaVoteItem.getVoteImage().size(); i++)
                                                    Log.d(TAG, "vote text value : "+receiveQnaVoteItem.getVoteImage().get(i));

                                                if (voteResult.size() > 0) {
                                                    receiveQnaVoteItem.setVoteResult(voteResult);
                                                    for (int i = 0; i < voteResult.size(); i++)
                                                        Log.d(TAG, "vote result value: " + voteResult.get(i));
                                                }
                                            }

                                            view.getDataSuccess(receiveQnaVoteItem);

                                        } else if (result.equals("false")) {
                                            Log.d(TAG, "onResponse: 불러오기 실패.");
                                        }

                                    }catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 콜백 실패
                                    view.getDataFail("불러오기 실패");
                                    Log.d(TAG, "onFailure: 콜백 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            // 통신 실패

                            Log.d(TAG, "onFailure: 통신 실패");
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
                        Log.d(TAG, "onSubscribe : "+d);
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext: "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError : ",e);
                    }
                    @Override
                    public void onComplete()
                    {
                    }
                });
    }

    @Override
    public void updateVoteResult(int accountNo, int postNo, int selectNum) {
        Log.d(TAG, "updateVoteResult: "+accountNo+"  "+postNo+"  "+selectNum);

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
                            apiService = APIClient.getClient1().create(APIService.class);
                            Call<ResponseBody> callServer = apiService.updateVoteResult(accountNo, postNo, selectNum);

                            callServer.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();

                                        jsonArray = new JSONArray(msg);
                                        returnData = jsonArray.getJSONObject(0);
                                        result = returnData.getString("result");

                                        if (result.equals("true")) {
                                            Log.d(TAG, "onResponse: 업데이트 성공.");
                                        } else if (result.equals("false")) {
                                            Log.d(TAG, "onResponse: 업데이트 실패.");
                                        }

                                    }catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 콜백 실패

                                    Log.d(TAG, "onFailure: 콜백 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            // 통신 실패

                            Log.d(TAG, "onFailure: 통신 실패");
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
                        Log.d(TAG, "onSubscribe : "+d);
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext: "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e);
                    }
                    @Override
                    public void onComplete()
                    {
                    }
                });
    }
}
