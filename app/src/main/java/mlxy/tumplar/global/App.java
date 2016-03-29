package mlxy.tumplar.global;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;

import mlxy.tumplar.internal.injection.AppComponent;
import mlxy.tumplar.internal.injection.Graph;

public class App extends android.app.Application {
    private static final String LOG_TAG = "chihane";
    public static Graph graph;
    public static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init(LOG_TAG);
        Fresco.initialize(this);

        component = AppComponent.Initializer.init(this);
        graph = component;

        User.tryLogin();
    }
}
