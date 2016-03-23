package mlxy.tumplar.internal.injection;

import android.content.Context;
import android.support.v7.widget.AppCompatDrawableManager;

import javax.inject.Singleton;

import dagger.Component;
import mlxy.tumplar.global.App;
import mlxy.tumplar.model.AvatarModel;
import mlxy.tumplar.presenter.DashboardPresenter;
import mlxy.tumplar.presenter.DrawerHeaderPresenter;

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
}
