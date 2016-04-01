package mlxy.tumplar.model;

import android.net.Uri;

import com.squareup.okhttp.HttpUrl;

import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import mlxy.tumplar.global.App;
import mlxy.tumplar.model.service.AvatarService;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AvatarModel {
    private static class Instance {
        private static final AvatarModel instance = new AvatarModel();
    }

    @Inject
    AvatarService service;

    private static ConcurrentHashMap<String, Observable<Uri>> observableCache;
    private static ConcurrentHashMap<String, Uri> cache;

    private AvatarModel() {
        App.graph.inject(this);
        observableCache = new ConcurrentHashMap<>();
        cache = new ConcurrentHashMap<>();
    }

    public static AvatarModel getInstance() {
        return Instance.instance;
    }

    public Observable<Uri> get(final String blogName) {
        if (cache.containsKey(blogName)) {
            return fromCache(blogName);
        }
        Observable<Uri> cachedObservable = observableCache.get(blogName);
        if (cachedObservable != null) {
            return cachedObservable;
        }

        // XXX try something like Observable.cache(blogName)?
        Observable<Uri> uriObservable = fromNet(blogName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        observableCache.remove(blogName);
                    }
                });

        Observable<Uri> doubleCheck = observableCache.putIfAbsent(blogName, uriObservable);
        if (doubleCheck != null) {
            return doubleCheck;
        }

        return uriObservable;
    }

    private Observable<Uri> fromCache(final String blogName) {
        return Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                subscriber.onNext(cache.get(blogName));
                subscriber.onCompleted();
            }
        });
    }

    private Observable<Uri> fromNet(final String blogName) {
        return service.avatar(blogName)
                .flatMap(new Func1<Response, Observable<Uri>>() {
                    @Override
                    public Observable<Uri> call(Response response) {
                        HttpUrl avatarUrl = response.raw().request().httpUrl();
                        return Observable.just(Uri.parse(avatarUrl.toString()));
                    }
                }).doOnNext(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        cache.putIfAbsent(blogName, uri);
                    }
                });
    }
}
