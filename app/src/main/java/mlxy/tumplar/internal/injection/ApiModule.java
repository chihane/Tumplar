package mlxy.tumplar.internal.injection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mlxy.tumplar.model.service.UserService;
import retrofit.Retrofit;

@Module
public class ApiModule {
    @Provides
    @Singleton
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }
}
