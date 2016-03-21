package mlxy.tumplar.injection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mlxy.tumplar.model.service.DashboardService;
import retrofit.Retrofit;

@Module
public class ApiModule {
    @Provides
    @Singleton
    DashboardService provideDashboardService(Retrofit retrofit) {
        return retrofit.create(DashboardService.class);
    }
}
