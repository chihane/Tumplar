package mlxy.tumplar.api;

import mlxy.tumplar.data.Dashboard;
import retrofit.http.GET;
import retrofit.http.Header;
import rx.Observable;

public interface TumblrService {
    @GET(Api.URL_DASHBOARD)
    Observable<Dashboard> dashboard(@Header("Authorization") String authorization);
}
