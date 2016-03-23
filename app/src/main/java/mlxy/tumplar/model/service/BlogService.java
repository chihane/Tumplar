package mlxy.tumplar.model.service;

import com.squareup.okhttp.ResponseBody;

import mlxy.tumplar.entity.BlogAvatarResponse;
import mlxy.tumplar.global.Apis;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface BlogService {
    @GET(Apis.BLOG_AVATAR)
    Observable<String> avatar(@Path("blog") String blog);

    @GET(Apis.BLOG_AVATAR)
    Observable<String> avatar(@Path("blog") String blog, @Path("size") int size);
}
