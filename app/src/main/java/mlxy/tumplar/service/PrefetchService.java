package mlxy.tumplar.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mlxy.tumplar.entity.event.PrefetchProgressEvent;
import mlxy.tumplar.global.App;
import mlxy.tumplar.internal.progress.Progress;
import mlxy.tumplar.internal.progress.ProgressImagePipelineModule;
import mlxy.tumplar.internal.progress.ProgressListener;
import mlxy.tumplar.model.UserModel;
import rx.functions.Action0;
import rx.functions.Action1;

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
        final String url = "https://40.media.tumblr.com/08dba4ef02458447542912f25a0e7c56/tumblr_o4fd294APs1qix0dvo1_1280.jpg";
//        Observable.just(url)
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String url) {
//                        glide.load(Uri.parse(url)).downloadOnly(new ProgressTarget(url, createListener()));
//                    }
//                });
        ProgressImagePipelineModule.download(this, Uri.parse(url))
                            .subscribe(new Action1<Progress>() {
                                @Override
                                public void call(Progress progress) {
                                    eventBus.post(new PrefetchProgressEvent(url, progress.totalBytesRead, progress.totalBytes));
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                }
                            }, new Action0() {
                                @Override
                                public void call() {
                                }
                            });
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
