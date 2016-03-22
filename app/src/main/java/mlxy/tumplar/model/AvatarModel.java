package mlxy.tumplar.model;

import android.net.Uri;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import mlxy.tumplar.global.TumblrClient;
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

    private static Map<String, Uri> cache;
    private static Set<String> tasks;

    private AvatarModel() {
        cache = new ConcurrentHashMap<>();
        tasks = Collections.synchronizedSet(new LinkedHashSet<String>());
    }

    public static AvatarModel getInstance() {
        return Instance.instance;
    }

    public synchronized Observable<Uri> get(final String blogName) {
        return fromCache(blogName)
                .concatWith(fromNet(blogName))
                .first(new Func1<Uri, Boolean>() {
                    @Override
                    public Boolean call(Uri uri) {
                        return uri != null;
                    }
                })
                .retry()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Uri> fromNet(final String blogName) {
        // Runs only one net-task at the same time.
        if (tasks.contains(blogName)) {
            return Observable.empty();
        }

        return Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                String url = TumblrClient.blogAvatar(blogName);
                subscriber.onNext(Uri.parse(url));
                subscriber.onCompleted();
            }
        }).doOnNext(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                cache.put(blogName, uri);
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                putTask(blogName);
            }
        }).doOnTerminate(new Action0() {
            @Override
            public void call() {
                removeTask(blogName);
            }
        });
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

    private void putTask(String blogName) {
        tasks.add(blogName);
    }

    private void removeTask(String blogName) {
        tasks.remove(blogName);
    }
}
