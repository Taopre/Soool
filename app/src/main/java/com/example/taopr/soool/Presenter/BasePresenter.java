package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.Networking.APIService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


// compositeDisposable로 관리하는 메서드를 BasePresenter로 모아둠


public class BasePresenter {
    protected APIService apiService;
    private CompositeDisposable mCompositeDisposable;


    /// 액티비티가 destory 될 때
    public void onUnSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    // 액티비티가 stop일 때
    public void clearSubscribe(){
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public void addSubscription(Observable observable, DisposableObserver observer) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }

        mCompositeDisposable.add(observer);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }
}
