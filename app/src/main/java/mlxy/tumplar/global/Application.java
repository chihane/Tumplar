package mlxy.tumplar.global;

import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import mlxy.tumplar.api.Api;
import mlxy.tumplar.tumblr.TumblrClient;
import mlxy.utils.Prefs;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class Application extends android.app.Application {
    private static final String LOG_TAG = "chihane";

    public static Context context;
    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Logger.init(LOG_TAG);

        TumblrClient.initialize();
        User.tryLogin();

        Fresco.initialize(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        retrofit.client().interceptors().add(new LoggingInterceptor());
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Logger.d(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Logger.d(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));


            final String responseString = new String(response.body().bytes());

//            Log.d("asdf", "Response: " + responseString);
            Logger.json(responseString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }
}
