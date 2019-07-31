package com.lpky.taopr.soool.Networking;



import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;


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
