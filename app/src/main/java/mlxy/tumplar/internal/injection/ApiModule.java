package mlxy.tumplar.internal.injection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mlxy.tumplar.model.service.AvatarService;
import mlxy.tumplar.model.service.DashboardService;
import mlxy.tumplar.model.service.UserService;
import retrofit.Retrofit;

@Module
public class ApiModule {
    @Provides
    @Singleton
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    DashboardService provideDashboardService(Retrofit retrofit) {
        return retrofit.create(DashboardService.class);
    }

    @Provides
    @Singleton
    AvatarService provideBlogService(Retrofit retrofit) {
        return retrofit.create(AvatarService.class);
    }
}
