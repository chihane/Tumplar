package mlxy.tumplar.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mlxy.tumplar.entity.Photo;
import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.entity.event.PrefetchProgressEvent;
import mlxy.tumplar.global.App;
import mlxy.tumplar.internal.progress.Progress;
import mlxy.tumplar.internal.progress.ProgressImagePipeline;
import mlxy.tumplar.model.DashboardModel;
import rx.Observable;
import rx.schedulers.Schedulers;

public class PrefetchService extends Service {
    @Inject
    DashboardModel model;
    @Inject
    EventBus eventBus;

    public PrefetchService() {
        App.graph.inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void prefetch() {
        model.dashboardPhoto()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .flatMap(Observable::from)
                .doOnNext(this::savePhotoPost)
                .flatMap(this::download)
                .subscribe(
                        progress -> eventBus.post(new PrefetchProgressEvent(progress)),
                        throwable -> Logger.e(throwable, "prefetch error")
                );
    }

    private void savePhotoPost(PhotoPost photoPost) {
        model.savePhotoPost(photoPost);
    }

    private Observable<Progress> download(PhotoPost photoPost) {
        Photo photo0 = photoPost.photos.get(0);
        String url = photo0.original_size.url;
        return ProgressImagePipeline.download(this, Uri.parse(url));
    }
}
