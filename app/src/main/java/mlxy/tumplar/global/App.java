package mlxy.tumplar.global;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;

import mlxy.tumplar.injection.ApiModule;
import mlxy.tumplar.injection.AppComponent;
import mlxy.tumplar.injection.AppModule;
import mlxy.tumplar.injection.DaggerAppComponent;
import mlxy.tumplar.tumblr.TumblrClient;

public class App extends android.app.Application {
    private static final String LOG_TAG = "chihane";
    public static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init(LOG_TAG);
        Fresco.initialize(this);

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();

        TumblrClient.initialize();
        User.tryLogin();
    }
}
