package mlxy.tumplar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import mlxy.tumplar.entity.Photo;
import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.entity.event.PrefetchProgressEvent;
import mlxy.tumplar.global.App;
import mlxy.tumplar.internal.progress.ProgressListener;
import mlxy.tumplar.internal.progress.ProgressTarget;
import mlxy.tumplar.model.UserModel;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PrefetchService extends Service {
    @Inject
    UserModel model;
    @Inject
    EventBus eventBus;

    private final RequestManager glide;

    public PrefetchService() {
        App.graph.inject(this);
        glide = Glide.with(App.component.context());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void prefetch() {
        model.dashboardPhoto()
                .flatMap(new Func1<List<PhotoPost>, Observable<PhotoPost>>() {
                    @Override
                    public Observable<PhotoPost> call(List<PhotoPost> photoPosts) {
//                        return Observable.from(photoPosts);
                        return Observable.just(photoPosts.get(0));
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<PhotoPost, Photo>() {
                    @Override
                    public Photo call(PhotoPost photoPost) {
                        return photoPost.photos.get(0);
                    }
                })
                .flatMap(new Func1<Photo, Observable<?>>() {
                    @Override
                    public Observable<?> call(Photo photo) {
                        String url = photo.original_size.url;
                        glide.load(url).downloadOnly(new ProgressTarget(url, createListener()));
                        return null;
                    }
                })
                .subscribe();
    }

    @NonNull
    private ProgressListener createListener() {
        return new ProgressListener() {
            @Override
            public void onProgress(String url, long bytesRead, long totalBytes) {
                eventBus.post(new PrefetchProgressEvent(url, bytesRead, totalBytes));
            }
        };
    }
}
