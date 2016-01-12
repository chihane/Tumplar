package mlxy.tumplar.model;

import com.google.api.client.http.HttpMethods;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import mlxy.tumplar.api.Api;
import mlxy.tumplar.api.TumblrService;
import mlxy.tumplar.data.Dashboard;
import mlxy.tumplar.global.Application;
import mlxy.tumplar.global.Constants;
import mlxy.tumplar.global.User;
import mlxy.tumplar.tumblr.Authorizer;
import mlxy.tumplar.tumblr.TumblrClient;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class DashboardModel {
    private final TumblrService service;

    public DashboardModel() {
        service = Application.retrofit.create(TumblrService.class);
    }

    public Observable<List<PhotoPost>> requestData() {
        return Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                subscriber.onNext(TumblrClient.userDashboard());
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<List<Post>, Observable<Post>>() {
            @Override
            public Observable<Post> call(List<Post> posts) {
                return Observable.from(posts);
            }
        }).ofType(PhotoPost.class).cast(PhotoPost.class).toList();
    }

    public Observable<Dashboard> dashboard() throws GeneralSecurityException {
//        HashMap<String, String> options = new HashMap<>();
//
//        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        options.put("oauth_timestamp", timestamp);
//
//        String nonce = String.valueOf(System.currentTimeMillis() + new Random().nextInt());
//        options.put("oauth_nonce", nonce);
//
//        String key = Constants.CONSUMER_KEY;
//        options.put("oauth_consumer_key", key);
//
//        String signMethod = "HMAC-SHA1";
//        options.put("oauth_signature_method", signMethod);
//
//        String version = "1.0";
//        options.put("oauth_version", version);
//
//        String signature = Authorizer.getAuthorizationHeader(User.access_token, User.access_token_secret, HttpMethods.GET, Api.BASE_URL + Api.URL_DASHBOARD);
//        options.put("oauth_signature", signature);

        String header = Authorizer.getAuthorizationHeader(User.access_token, User.access_token_secret, HttpMethods.GET, Api.BASE_URL + Api.URL_DASHBOARD);
        return service.dashboard(header);
    }
}
