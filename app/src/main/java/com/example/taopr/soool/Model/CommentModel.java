package com.example.taopr.soool.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;
import com.example.taopr.soool.View.QnaBoardDetailActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentModel
{

    private QnaDetailPresenter qnaDetailPresenter;
    private Context context;
    private APIService service;
    String TAG = "commentInsertModel";

    public CommentModel(Context context,QnaDetailPresenter qnaDetailPresenter)
    {
        this.context = context;
        this.qnaDetailPresenter =  qnaDetailPresenter;
        Log.d(TAG, "commentRequest: 모델 생성자 성공?????");
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

                                                                qnaDetailPresenter.commentInsertResponse(0,commentCount);
                                                                qnaDetailPresenter.loadData(postNo);
                                                                //현재 댓글 입력이 성공(result값으로 true)되면
                                                                //해당 게시물에 있는 전체 댓글을 load하는 방식 - 굉장히 비효율적
                                                                //댓글 insert할떄에 response값으로 방금 입력한 댓글을 전송하는 방법으로 수정해야함함                                                                qnaDetailPresenter.loadData(postNo);
                                                            }
                                                            else if (result.equals("false"))
                                                            {
                                                                qnaDetailPresenter.commentInsertResponse(1,commentCount);
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

                                            Log.d(TAG, "result!!!: " + result);
                                            Log.d(TAG, "Coommentent!!!: " + String.valueOf(commentNo));


                                            if (result.equals("true"))
                                            {
                                                qnaDetailPresenter.recommentInsertResponse(0);
                                                qnaDetailPresenter.loadData(postNo);
                                            }
                                            else if (result.equals("false"))
                                            {
                                                qnaDetailPresenter.recommentInsertResponse(1);
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
                                                                qnaDetailPresenter.likeResponse(a);
                                                                Log.d(TAG,"on");
                                                            }
                                                            else if (result.equals("false"))
                                                            {
                                                                qnaDetailPresenter.likeResponse(1);
                                                                Log.d(TAG,"off");
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
    public void commentDeleteRequest(int postNo,int commentNo,int recommentNo)
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
                                            final Call<ResponseBody> reqeust = service.commentDeleteRequest(postNo,commentNo,recommentNo);

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
                                                            int commentCount = returnData.getInt("commentCount");

                                                            if (result.equals("true"))
                                                            {
                                                                Log.d(TAG, "commentDeleteGoResponse:commentCount " + String.valueOf(commentCount));
                                                                qnaDetailPresenter.commentDeleteResponss(0,commentCount);
                                                                qnaDetailPresenter.loadData(postNo);
                                                                Log.d(TAG,"on");
                                                            }
                                                            else if (result.equals("false"))
                                                            {
                                                                qnaDetailPresenter.commentDeleteResponss(1,commentCount);
                                                                Log.d(TAG,"off");
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

//    public void commentLikeCancel_Request(final int postNo,final int commentNo,final int accountNo)
//    {
//        Observable.just("")
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .map
//                        (
//                                new Function<String, Boolean>()
//                                {
//                                    @Override
//                                    public Boolean apply(String s) throws Exception
//                                    {
//                                        try
//                                        {
//
//                                            apiService = APIClient.getRetrofit().create(APIService.class);
//                                            final Call<ResponseBody> reqeust = apiService.commentLikeCancelRequest(postNo,commentNo,accountNo);
//
//                                            reqeust.enqueue(new Callback<ResponseBody>()
//                                            {
//                                                @Override
//                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
//                                                {
//                                                    try
//                                                    {
//                                                        String message = response.body().string();
//                                                        //Gson gsonobject = new Gson();
//                                                        Log.d(TAG,message);
//
//                                                        JSONArray jsonArray = new JSONArray(message);
//                                                        for (int i =0;i <jsonArray.length() ;i++)
//                                                        {
//                                                            JSONObject returnData = jsonArray.getJSONObject(i);
//
//
//                                                            String result = returnData.getString("result");
//
//                                                            if (result.equals("true"))
//                                                            {
//                                                                int a = 0;
//                                                                commentLikepresenter.likeCancelResponse(a);
//                                                                Log.d(TAG,"on");
//                                                            }
//                                                            else if (result.equals("false"))
//                                                            {
//                                                                commentLikepresenter.likeCancelResponse(1);
//                                                                Log.d(TAG,"off");
//                                                            }
//
//                                                        }
//                                                    }
//                                                    catch (Exception e)
//                                                    {
//                                                        Log.e(TAG, "apply: ", e);
//                                                        Log.d(TAG,"eeorkeor");
//                                                    }
//                                                }
//                                                @Override
//                                                public void onFailure(Call<ResponseBody> call, Throwable t)
//                                                {
//
//                                                }
//
//
//                                            });
//                                        }
//                                        catch (Exception e)
//                                        {
//                                            Log.d(TAG, "onFailure: 실패2");
//                                            Log.e(TAG, "apply: ", e);
//                                        }
//                                        return  true;
//                                    }
//                                }
//
//                        )
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Boolean>()
//                {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

}
