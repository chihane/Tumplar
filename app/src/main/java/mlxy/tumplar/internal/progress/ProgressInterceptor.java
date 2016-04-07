package mlxy.tumplar.internal.progress;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.Subscriber;

class ProgressInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .body(new InterceptedResponseBody(request.httpUrl().toString(), response.body()))
                .build();
    }

    private class InterceptedResponseBody extends ResponseBody {
        private Subscriber<? super Long> subscriber;

        private final String url;
        private final ResponseBody responseBody;
        private final ResponseReadingProgressListener listener;

        private BufferedSource bufferedSource;

        public InterceptedResponseBody(String url, ResponseBody responseBody) {
            this.url = url;
            this.responseBody = responseBody;
            this.listener = ProgressDispatcher.create();
            ProgressDispatcher.pendingObservable(url, Observable.create(new Observable.OnSubscribe<Long>() {
                @Override
                public void call(Subscriber<? super Long> subscriber) {
                    InterceptedResponseBody.this.subscriber = subscriber;
                }
            }));
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

                    subscriber.onNext(totalBytesRead);

                    if (totalBytesRead == totalBytes) {
                        subscriber.onCompleted();
                    }
                    listener.onProgress(url, totalBytesRead, totalBytes);

                    return bytesRead;
                }
            };
        }
    }
}
