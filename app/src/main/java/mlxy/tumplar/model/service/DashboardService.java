package mlxy.tumplar.model.service;

import mlxy.tumplar.entity.DashboardPhotoResponse;
import mlxy.tumplar.global.Apis;
import retrofit.http.GET;
import rx.Observable;

public interface DashboardService {
    @GET(Apis.DASHBOARD)
    Observable<DashboardPhotoResponse> dashboard();
}
