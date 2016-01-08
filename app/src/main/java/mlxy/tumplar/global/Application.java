package mlxy.tumplar.global;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import mlxy.tumplar.tumblr.TumblrClient;
import mlxy.utils.Prefs;

public class Application extends android.app.Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        TumblrClient.initialize();
        User.tryLogin();

        Fresco.initialize(this);
    }
}
