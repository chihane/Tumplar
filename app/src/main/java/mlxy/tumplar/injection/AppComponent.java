package mlxy.tumplar.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;
import mlxy.tumplar.presenter.DashboardPresenter;

@Singleton
@Component(modules = { AppModule.class, ApiModule.class })
public interface AppComponent {
    Context context();

    void inject(DashboardPresenter dashboardPresenter);
}
