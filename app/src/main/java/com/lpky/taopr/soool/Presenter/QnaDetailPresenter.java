package com.lpky.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.lpky.taopr.soool.Networking.APIClient;
import com.lpky.taopr.soool.Networking.APIService;
import com.lpky.taopr.soool.Object.BoardRecommend;
import com.lpky.taopr.soool.Object.QnaVoteItem;
import com.lpky.taopr.soool.Presenter.Interface.QnaDetailInter;
import com.google.gson.Gson;

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
    Gson gson = new Gson();
    BoardRecommend boardRecommend;

    String result, count;
    int qnaVoteStatus, memberIsVoted, totalVoteCount;
    ArrayList<String> voteList = new ArrayList<>();
    ArrayList<Integer> voteResult = new ArrayList<>();
    QnaVoteItem receiveQnaVoteItem = new QnaVoteItem();


    private Activity activity;

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
                                        Log.d(TAG, "onResponse: 투표 객체값들~" + msg);
                                        
                                        jsonArray = new JSONArray(msg);
                                        returnData = jsonArray.getJSONObject(0);
                                        receiveQnaVoteItem.setResult(returnData.getString("result"));

                                        if (receiveQnaVoteItem.getResult().equals("true")) {
                                            Log.d(TAG, "onResponse: true상태이네!!!!!!!!");
                                            receiveQnaVoteItem.setQnaVoteStatus(returnData.getInt("qnaVoteStatus"));
                                            receiveQnaVoteItem.setCount(returnData.getInt("count"));
                                            receiveQnaVoteItem.setMemberIsVoted(returnData.getInt("memberIsVoted"));
                                            receiveQnaVoteItem.setTotalVoteCount(returnData.getInt("totalVoteCount"));


                                            try {
                                                for (int j=0; j<receiveQnaVoteItem.getCount(); j++) {
                                                    voteList.add(returnData.getString("voteContent"+(j+1)));
                                                }
                                            }catch (NullPointerException e) {
                                                Log.d(TAG, "exception null : "+e);
                                            }

                                            // 투표항목 투표상태에 따른 값 저장 구문
                                            if (receiveQnaVoteItem.getQnaVoteStatus() == 0)
                                                receiveQnaVoteItem.setVoteText(voteList);
                                            else
                                                receiveQnaVoteItem.setVoteImage(voteList);

                                            try {
                                                for (int j=0; j<receiveQnaVoteItem.getCount(); j++) {
                                                    voteResult.add(returnData.getInt("voteResult"+(j+1)));
                                                }
                                            }catch (NullPointerException e) {
                                            }

                                            // 투표 참여상태 따른 투표결과 값 저장 구문
                                            if (receiveQnaVoteItem.getMemberIsVoted() == 0) {

                                            } else {
                                                receiveQnaVoteItem.setVoteResult(voteResult);
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
                                        receiveQnaVoteItem = new QnaVoteItem();
                                        receiveQnaVoteItem.setResult(returnData.getString("result"));

                                        if (receiveQnaVoteItem.getResult().equals("true")) {
                                            receiveQnaVoteItem.setQnaVoteStatus(returnData.getInt("qnaVoteStatus"));
                                            receiveQnaVoteItem.setCount(returnData.getInt("count"));
                                            receiveQnaVoteItem.setTotalVoteCount(returnData.getInt("totalVoteCount"));
                                            receiveQnaVoteItem.setMemberIsVoted(selectNum-1);
                                            voteResult.clear();
                                            voteList.clear();
                                            for (int i = 0; i < receiveQnaVoteItem.getCount(); i++) {
                                                voteList.add(returnData.getString("voteContent" + (i + 1)));
                                            }
                                            receiveQnaVoteItem.setVoteText(voteList);
                                            for (int i = 0; i < receiveQnaVoteItem.getCount(); i++) {
                                                voteResult.add(returnData.getInt("voteResult" + (i + 1)));
                                            }
                                            Log.d(TAG, "onResponse: 하이 프레젠터"+receiveQnaVoteItem.getCount()+receiveQnaVoteItem.getVoteText().size());
                                            receiveQnaVoteItem.setVoteResult(voteResult);

                                            view.updateVoteResultComplete(true, receiveQnaVoteItem);
                                        } else {

                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 콜백 실패
                                    view.updateVoteResultComplete(true, receiveQnaVoteItem);
                                    Log.d(TAG, "onFailure: 콜백 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            // 통신 실패
                            view.updateVoteResultComplete(true, receiveQnaVoteItem);
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

    @Override
    public void recommendOnOffReq(int accountNo, int postNo, int likeType, int btnOnOff) {
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
                            Call<ResponseBody> callServer = apiService.recommendOnOffReq(postNo, accountNo, likeType, btnOnOff);

                            callServer.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();
                                        Log.d(TAG, "onResponse: 서버 응답 "+msg);

                                        boardRecommend = gson.fromJson(msg, BoardRecommend.class);
                                        view.recommendComplete(true, boardRecommend);
                                    }catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 콜백 실패
                                    view.recommendComplete(false, boardRecommend);
                                    Log.d(TAG, "onFailure: 콜백 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            // 통신 실패
                            view.recommendComplete(false, boardRecommend);
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
