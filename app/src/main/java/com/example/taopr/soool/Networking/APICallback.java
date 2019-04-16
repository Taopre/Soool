package com.example.taopr.soool.Networking;



import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;


/**
 * Created by WuXiaolong on 2016/9/22.
 * github:https://github.com/WuXiaolong/
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
public abstract class APICallback<M> extends DisposableObserver<M> {

    public abstract void onSuccess(M loginItem);

    public abstract void onFailure(String msg);

    public abstract void onFinish();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
           // LogUtil.d("code=" + code);
            if (code == 504) {
                msg = "504";
            }
            if (code == 502 || code == 404) {
                msg = "502 // 404";
            }
            onFailure(msg);
        } else {
            onFailure(e.getMessage());
        }
        onFinish();
    }

    @Override
    public void onNext(M loginItem) {
        onSuccess(loginItem);

    }
    @Override
    public void onComplete() {
        onFinish();
    }
}
