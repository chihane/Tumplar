package mlxy.tumplar.global;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;

import mlxy.tumplar.tumblr.TumblrClient;

public class Application extends android.app.Application {
    private static final String LOG_TAG = "chihane";

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Logger.init(LOG_TAG);

        TumblrClient.initialize();
        User.tryLogin();

        Fresco.initialize(this);
    }
}
