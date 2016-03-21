package mlxy.tumplar.injection;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mlxy.tumplar.global.Apis;
import mlxy.tumplar.interceptor.AuthHeaderInterceptor;
import mlxy.tumplar.interceptor.LoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return this.context;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Apis.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        List<Interceptor> interceptors = client.interceptors();
        interceptors.add(new LoggingInterceptor());
        interceptors.add(new AuthHeaderInterceptor());
        return client;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }
}
