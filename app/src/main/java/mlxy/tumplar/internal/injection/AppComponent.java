package mlxy.tumplar.internal.injection;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Component;
import mlxy.tumplar.global.App;

@Singleton
@Component(modules = { AppModule.class, ApiModule.class })
public interface AppComponent extends Graph {
    final class Initializer {
        public static AppComponent init(App app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .apiModule(new ApiModule())
                    .build();
        }
    }

    Context context();
    OkHttpClient okHttpClient();
}
