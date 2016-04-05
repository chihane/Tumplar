package mlxy.tumplar.internal.progress;

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

class ProgressInterceptor implements com.squareup.okhttp.Interceptor {
    private final ResponseReadingProgressListener listener;

    public ProgressInterceptor(ResponseReadingProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return new Response.Builder()
                .body(new InterceptedResponseBody(request.httpUrl().toString(), response.body(), listener))
                .build();
    }

    private class InterceptedResponseBody extends ResponseBody {
        private final String url;
        private final ResponseBody responseBody;
        private final ResponseReadingProgressListener listener;

        public InterceptedResponseBody(String url, ResponseBody responseBody, ResponseReadingProgressListener listener) {
            this.url = url;
            this.responseBody = responseBody;
            this.listener = listener;
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
            return Okio.buffer(intercept(responseBody.source()));
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

                    if (listener != null) {
                        listener.onProgress(url, totalBytesRead, totalBytes);
                    }

                    return bytesRead;
                }
            };
        }
    }
}
