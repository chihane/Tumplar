package mlxy.tumplar.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mlxy.tumplar.entity.event.PrefetchProgressEvent;
import mlxy.tumplar.global.App;
import mlxy.tumplar.internal.progress.Progress;
import mlxy.tumplar.internal.progress.ProgressImagePipelineModule;
import mlxy.tumplar.model.UserModel;
import rx.functions.Action0;
import rx.functions.Action1;

public class PrefetchService extends Service {
    @Inject
    UserModel model;
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
        final String url = "https://40.media.tumblr.com/08dba4ef02458447542912f25a0e7c56/tumblr_o4fd294APs1qix0dvo1_1280.jpg";
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
                                    eventBus.post(new PrefetchProgressEvent(url, -1, -1));
                                }
                            });
    }
}
