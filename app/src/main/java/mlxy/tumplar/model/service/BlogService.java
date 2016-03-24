package mlxy.tumplar.model.service;

import mlxy.tumplar.global.Apis;
import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface BlogService {
    @GET(Apis.BLOG_AVATAR)
    Observable<Response<String>> avatar(@Path("blog") String blog);

    @GET(Apis.BLOG_AVATAR)
    Observable<Response<String>> avatar(@Path("blog") String blog, @Path("size") int size);
}
