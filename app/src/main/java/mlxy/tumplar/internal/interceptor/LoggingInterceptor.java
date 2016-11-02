package mlxy.tumplar.internal.interceptor;

import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {
    public static final boolean ENABLED = true;
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        String url = response.request().httpUrl().toString();

        if (ENABLED) {
            Logger.d(url);
        }

        final String responseString = new String(response.body().bytes());
        if (ENABLED) {
            Logger.json(responseString);
        }

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), responseString))
                .build();
    }
}
