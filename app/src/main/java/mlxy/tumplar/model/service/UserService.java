package mlxy.tumplar.model.service;

import mlxy.tumplar.entity.response.DashboardResponse;
import mlxy.tumplar.entity.response.UserInfoResponse;
import mlxy.tumplar.global.Apis;
import retrofit.http.GET;
import rx.Observable;

public interface UserService {
    @GET(Apis.USER_INFO)
    Observable<UserInfoResponse> me();

    @GET(Apis.DASHBOARD)
    Observable<DashboardResponse> dashboard();
}
