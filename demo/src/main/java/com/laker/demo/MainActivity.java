package com.laker.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.laker.base.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                App.getsLogger().e("currentThread","Observable thread is :  ->"+Thread.currentThread().getName());
//                App.getsLogger().e("Observable","subscribe ObservableEmitter ->"+123);
//                e.onNext(123);
//                App.getsLogger().e("Observable","subscribe ObservableEmitter ->"+456);
//                e.onNext(456);
//                App.getsLogger().e("Observable","subscribe ObservableEmitter ->"+789);
//                e.onNext(789);
//            }
//        });
//
//        Consumer<Integer> integerConsumer = new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                App.getsLogger().e("currentThread","Observer thread is :  ->"+Thread.currentThread().getName());
//                App.getsLogger().e("Consumer","accept integer = "+integer);
//
//
//            }
//        };
//
////        integerObservable.subscribe(integerConsumer);
//        integerObservable.subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(integerConsumer);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.e("1111111111111111");
                Logger.e("333","222222222222222");
                Logger.e("3333333333" );
            }
        });


    }
}
