package com.example.taopr.soool.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.taopr.soool.Adapter.CommentAdapter;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.CommentListObject;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.InfoDetailPresenter;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;
import com.example.taopr.soool.View.InfoDetailActivity;
import com.example.taopr.soool.View.QnaBoardDetailActivity;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentModel
{

    private QnaDetailPresenter qnaDetailPresenter;
    private InfoDetailPresenter infoDetailPresenter;
    private Context context;
    private APIService service;
    String TAG = "commentInsertModel";


    CommentItem commentItem;
    RecommentItem recommentItem;


    public CommentModel(Context context,QnaDetailPresenter qnaDetailPresenter)
    {
        this.context = context;
        this.qnaDetailPresenter =  qnaDetailPresenter;
        Log.d(TAG, "commentRequest: 모델 생성자 성공?????");
    }
    public CommentModel(Context context, InfoDetailPresenter infoDetailPresenter)
    {
        this.context = context;
        this.infoDetailPresenter = infoDetailPresenter;
    }

    public void commentRequest(int postNo,int accountNo,String commentContent)
    {
        Log.d(TAG, "commentRequest: 인서트 리퀘스트 성공?????");
        Observable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map
                        (
                                new Function<String, Boolean>()
                                {
                                    @Override
                                    public Boolean apply(String s) throws Exception
                                    {
                                        try
                                        {

                                            service = APIClient.getClient1().create(APIService.class);
                                            final Call<ResponseBody> reqeust = service.commentRequest(postNo,accountNo,commentContent);

                                            reqeust.enqueue(new Callback<ResponseBody>()
                                            {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                                                {
                                                    try
                                                    {
                                                        String message = response.body().string();

                                                        JSONArray jsonArray = new JSONArray(message);
                                                        for (int i =0;i <jsonArray.length() ;i++)
                                                        {
                                                            JSONObject returnData = jsonArray.getJSONObject(i);

                                                            String result = returnData.getString("result");

                                                            Log.d(TAG, "result!!!: " + result);

                                                            int commentCount = returnData.getInt("commentCount");
                                                            if (result.equals("true"))
                                                            {

                                                                Gson gsonObject = new Gson();
                                                                String addComment = String.valueOf(returnData.getJSONObject("commentitem"));
                                                                Log.d(TAG, "onResponseCCCCCC: " + addComment);

                                                                commentItem = gsonObject.fromJson(addComment, CommentItem.class);

                                                                Log.d(TAG, "onResponseBBBBBBB: " + commentItem.getAccountNo());
                                                                //commentItemArrayList.add(commentItem);
                                                                //Log.d(TAG, "onResponse: DDDDDDD" + commentItemArrayList.get(commentItemArrayList.size()-1).getAccountNo());
                                                                if (qnaDetailPresenter == null)
                                                                {
                                                                    infoDetailPresenter.commentInsertResponse(0,commentCount,commentItem);
                                                                }
                                                                else
                                                                {
                                                                    qnaDetailPresenter.commentInsertResponse(0,commentCount,commentItem);
                                                                }

                                                                //qnaDetailPresenter.loadData(postNo);
                                                                //현재 댓글 입력이 성공(result값으로 true)되면
                                                                //해당 게시물에 있는 전체 댓글을 load하는 방식 - 굉장히 비효율적
                                                                //댓글 insert할떄에 response값으로 방금 입력한 댓글을 전송하는 방법으로 수정해야함함
                                                            }
                                                            else if (result.equals("false"))
                                                            {

                                                                if (qnaDetailPresenter == null)
                                                                {
                                                                    infoDetailPresenter.commentInsertResponse(1,commentCount,commentItem);
                                                                }
                                                                else
                                                                {
                                                                    qnaDetailPresenter.commentInsertResponse(1,commentCount,commentItem);
                                                                }
                                                            }

                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        Log.e(TAG, "apply: ", e);
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t)
                                                {

                                                }


                                            });
                                        }
                                        catch (Exception e)
                                        {
                                            Log.d(TAG, "onFailure: 실패2");
                                            Log.e(TAG, "apply: ", e);
                                        }
                                        return  true;
                                    }
                                }

                        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void recommentRequest(int postNo,int commentNo,int accountNo,String commentContent)
    {
        Observable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<String,Boolean>()
                {

                    @Override
                    public Boolean apply(String s) throws Exception
                    {
                        try
                        {
                            service = APIClient.getClient1().create(APIService.class);
                            Call<ResponseBody> reqeust = service.recommentRequest(postNo,commentNo,accountNo,commentContent);

                            reqeust.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                                {
                                    try
                                    {
                                        String message = response.body().string();
                                        JSONArray jsonArray = new JSONArray(message);

                                        for (int i= 0;i < jsonArray.length(); i++)
                                        {
                                            JSONObject returnData = jsonArray.getJSONObject(i);

                                            String result = returnData.getString("result");
                                            int commentNo = returnData.getInt("commentNo");
                                            Log.d(TAG, "result!!!: " + result);
                                            Log.d(TAG, "Coommentent!!!: " + String.valueOf(commentNo));


                                            if (result.equals("true"))
                                            {

                                                Gson gsonObject = new Gson();
                                                String addComment = String.valueOf(returnData.getJSONObject("recommentitem"));
                                                recommentItem = gsonObject.fromJson(addComment,RecommentItem.class);
                                                Log.d(TAG, "onResponseRRRRCCCCCC: " + addComment);


                                                if (qnaDetailPresenter == null)
                                                {
                                                    infoDetailPresenter.recommentInsertResponse(0,recommentItem,commentNo);
                                                }
                                                else
                                                {
                                                    qnaDetailPresenter.recommentInsertResponse(0,recommentItem,commentNo);
                                                }
                                            }
                                            else if (result.equals("false"))
                                            {

                                                if (qnaDetailPresenter == null)
                                                {
                                                    infoDetailPresenter.recommentInsertResponse(1,recommentItem,commentNo);
                                                }
                                                else
                                                {
                                                    qnaDetailPresenter.recommentInsertResponse(1,recommentItem,commentNo);
                                                }
                                            }
                                        }

                                    }
                                    catch (Exception e)
                                    {

                                    }

                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t)
                                {

                                }
                            });
                        }
                        catch (Exception e)
                        {

                        }

                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void commentLikeRequest(int postNo,int commentNo,int accountNo,int like_check,int commentORrecomment,int recommentNo)
    {
        Observable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map
                        (
                                new Function<String, Boolean>()
                                {
                                    @Override
                                    public Boolean apply(String s) throws Exception
                                    {
                                        try
                                        {

                                            service = APIClient.getClient1().create(APIService.class);
                                            final Call<ResponseBody> reqeust = service.commentLikeRequest(postNo,commentNo,accountNo,like_check,commentORrecomment,recommentNo);

                                            reqeust.enqueue(new Callback<ResponseBody>()
                                            {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                                                {
                                                    try
                                                    {
                                                        String message = response.body().string();
                                                        //Gson gsonobject = new Gson();
                                                        Log.d(TAG,message);

                                                        JSONArray jsonArray = new JSONArray(message);
                                                        for (int i =0;i <jsonArray.length() ;i++)
                                                        {
                                                            JSONObject returnData = jsonArray.getJSONObject(i);


                                                            String result = returnData.getString("result");

                                                            if (result.equals("true"))
                                                            {
                                                                int a = 0;
                                                                if (qnaDetailPresenter == null)
                                                                {
                                                                    infoDetailPresenter.likeResponse(a);
                                                                }
                                                                else
                                                                {
                                                                    qnaDetailPresenter.likeResponse(a);
                                                                }
                                                            }
                                                            else if (result.equals("false"))
                                                            {
                                                                if (qnaDetailPresenter == null)
                                                                {
                                                                    infoDetailPresenter.likeResponse(1);
                                                                }
                                                                else
                                                                {
                                                                    qnaDetailPresenter.likeResponse(1);
                                                                }
                                                            }

                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        Log.e(TAG, "apply: ", e);
                                                        Log.d(TAG,"eeorkeor");
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t)
                                                {

                                                }


                                            });
                                        }
                                        catch (Exception e)
                                        {
                                            Log.d(TAG, "onFailure: 실패2");
                                            Log.e(TAG, "apply: ", e);
                                        }
                                        return  true;
                                    }
                                }

                        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void commentDeleteRequest(int postNo,int commentNo,int recommentNo) {
        Observable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map
                        (
                                new Function<String, Boolean>() {
                                    @Override
                                    public Boolean apply(String s) throws Exception {
                                        try {

                                            service = APIClient.getClient1().create(APIService.class);
                                            final Call<ResponseBody> reqeust = service.commentDeleteRequest(postNo, commentNo, recommentNo);

                                            reqeust.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try {
                                                        String message = response.body().string();
                                                        //Gson gsonobject = new Gson();
                                                        Log.d(TAG, message);

                                                        JSONArray jsonArray = new JSONArray(message);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject returnData = jsonArray.getJSONObject(i);
                                                            String result = returnData.getString("result");
                                                            int commentCount = returnData.getInt("commentCount");

                                                            if (result.equals("true")) {
                                                                Log.d(TAG, "commentDeleteGoResponse:commentCount " + String.valueOf(commentCount));

                                                                if (qnaDetailPresenter == null)
                                                                {
                                                                    infoDetailPresenter.commentDeleteResponss(0, commentCount,commentNo);
                                                                    //infoDetailPresenter.loadData(postNo);
                                                                }
                                                                else
                                                                    {
                                                                    qnaDetailPresenter.commentDeleteResponss(0, commentCount,commentNo);
                                                                    //qnaDetailPresenter.loadData(postNo);
                                                                }
                                                            } else if (result.equals("false")) {

                                                                if (qnaDetailPresenter == null) {
                                                                    infoDetailPresenter.commentDeleteResponss(1, commentCount,commentNo);
                                                                } else {
                                                                    qnaDetailPresenter.commentDeleteResponss(1, commentCount,commentNo);
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        Log.e(TAG, "apply: ", e);
                                                        Log.d(TAG, "eeorkeor");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }


                                            });
                                        } catch (Exception e) {
                                            Log.d(TAG, "onFailure: 실패2");
                                            Log.e(TAG, "apply: ", e);
                                        }
                                        return true;
                                    }
                                }

                        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
