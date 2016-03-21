package mlxy.tumplar.model.service;

import mlxy.tumplar.entity.response.DashboardPhotoResponse;
import mlxy.tumplar.global.Apis;
import retrofit.http.GET;
import rx.Observable;

public interface UserService {
    @GET(Apis.USER_INFO)
    Observable<UserInfoResponse> me();

    @GET(Apis.DASHBOARD)
    Observable<DashboardPhotoResponse> dashboard();
}
