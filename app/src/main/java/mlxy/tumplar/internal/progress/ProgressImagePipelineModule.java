package mlxy.tumplar.internal.progress;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.HashMap;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

public class ProgressImagePipelineModule {
    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new ProgressInterceptor());
        return OkHttpImagePipelineConfigFactory.newBuilder(context, client).build();
    }

    public static Observable<Progress> download(final Context context, final Uri uri) {
        return Observable.create(new Observable.OnSubscribe<Progress>() {
            @Override
            public void call(Subscriber<? super Progress> subscriber) {
                ProgressInterceptor.subscribe(uri.toString(), subscriber);
                ImageRequest request = ImageRequest.fromUri(uri);
                Fresco.getImagePipeline().prefetchToDiskCache(request, context);
            }
        }).finallyDo(new Action0() {
            @Override
            public void call() {
                ProgressInterceptor.unsubscribe(uri.toString());
            }
        });
    }

    static class ProgressInterceptor implements Interceptor {
        private static final HashMap<String, Subscriber<? super Progress>> SUBSCRIBERS = new HashMap<>();

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .body(new InterceptedResponseBody(request.httpUrl().toString(), response.body()))
                    .build();
        }

        public static void subscribe(String url, Subscriber<? super Progress> subscriber) {
            SUBSCRIBERS.put(url, subscriber);
        }

        public static void unsubscribe(String url) {
            SUBSCRIBERS.remove(url);
        }

        private class InterceptedResponseBody extends ResponseBody {
            private final String url;
            private final ResponseBody responseBody;

            private BufferedSource bufferedSource;

            public InterceptedResponseBody(String url, ResponseBody responseBody) {
                this.url = url;
                this.responseBody = responseBody;
            }

            @Override
            public MediaType contentType() {
                return responseBody.contentType();
            }

            @Override
            public long contentLength() throws IOException {
                return responseBody.contentLength();
            }

            @Override
            public BufferedSource source() throws IOException {
                if (bufferedSource == null) {
                    bufferedSource = Okio.buffer(intercept(responseBody.source()));
                }
                return bufferedSource;
            }

            private Source intercept(final Source source) {
                return new ForwardingSource(source) {

                    private long totalBytesRead;

                    @Override
                    public long read(Buffer sink, long byteCount) throws IOException {
                        long bytesRead = super.read(sink, byteCount);
                        long totalBytes = responseBody.contentLength();

                        if (bytesRead == -1) {
                            totalBytesRead = totalBytes;
                        } else {
                            totalBytesRead += bytesRead;
                        }

                        Subscriber<? super Progress> subscriber = SUBSCRIBERS.get(url);
                        if (subscriber != null) {
                            if (totalBytesRead == totalBytes) {
                                subscriber.onCompleted();
                            } else {
                                subscriber.onNext(Progress.obtain(totalBytesRead, totalBytes));
                            }
                        }

                        return bytesRead;
                    }
                };
            }
        }
    }
}
