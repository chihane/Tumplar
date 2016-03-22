package mlxy.tumplar.model.service;

import mlxy.tumplar.entity.response.DashboardResponse;
import mlxy.tumplar.entity.response.UserInfoResponse;
import mlxy.tumplar.global.Apis;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface UserService {
    @GET(Apis.USER_INFO)
    Observable<UserInfoResponse> me();

    @GET(Apis.DASHBOARD)
    Observable<DashboardResponse> dashboard();

    @GET(Apis.DASHBOARD)
    Observable<DashboardResponse> dashboard(@Query("offset") int offset);

    @GET(Apis.DASHBOARD)
    Observable<DashboardResponse> dashboard(@Query("type") String type);

    @GET(Apis.DASHBOARD)
    Observable<DashboardResponse> dashboard(@Query("type") String type, @Query("offset") int offset);
}
