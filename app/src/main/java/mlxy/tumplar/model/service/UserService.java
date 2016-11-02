package mlxy.tumplar.model.service;

import mlxy.tumplar.entity.response.LikesResponse;
import mlxy.tumplar.entity.response.UserInfoResponse;
import mlxy.tumplar.global.Apis;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface UserService {
    @GET(Apis.USER_INFO)
    Observable<UserInfoResponse> me();

    @GET(Apis.LIKES)
    Observable<LikesResponse> likes(@Query("offset") int offset);
}
