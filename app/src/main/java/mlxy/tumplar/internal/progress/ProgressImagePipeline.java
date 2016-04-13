package mlxy.tumplar.internal.progress;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.facebook.common.executors.HandlerExecutorServiceImpl;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.squareup.okhttp.OkHttpClient;

import rx.Observable;
import rx.Subscriber;

public class ProgressImagePipeline {
    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new ProgressInterceptor());
        return OkHttpImagePipelineConfigFactory.newBuilder(context, client).build();
    }

    public static Observable<Progress> download(final Context context, final Uri uri) {
        return Observable.create(new Observable.OnSubscribe<Progress>() {
            @Override
            public void call(final Subscriber<? super Progress> subscriber) {
                ProgressDispatcher.subscribe(uri.toString(), subscriber);

                Fresco.getImagePipeline().prefetchToDiskCache(ImageRequest.fromUri(uri), context)
                        .subscribe(new SimpleDataSubscriber(subscriber),
                                new HandlerExecutorServiceImpl(new Handler()));
            }
        }).finallyDo(() -> ProgressDispatcher.unsubscribe(uri.toString()));
    }

    static class SimpleDataSubscriber extends BaseDataSubscriber<Void> {
        private Subscriber<? super Progress> subscriber;

        public SimpleDataSubscriber(Subscriber<? super Progress> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        protected void onNewResultImpl(DataSource<Void> dataSource) {
            subscriber.onCompleted();
        }

        @Override
        protected void onFailureImpl(DataSource<Void> dataSource) {
            subscriber.onCompleted();
        }

        @Override
        public void onCancellation(DataSource<Void> dataSource) {
            subscriber.onCompleted();
        }
    }

}
