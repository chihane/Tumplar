package mlxy.tumplar.tumblr;

import android.text.TextUtils;

import com.tumblr.jumblr.types.Post;

import java.util.List;
import java.util.Map;

import mlxy.tumplar.global.Constants;

public class Tumblr {
    private static com.tumblr.jumblr.JumblrClient client;

    public static void initialize() {
        client = new com.tumblr.jumblr.JumblrClient(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    }

    public static void setToken(String token, String tokenSecret) {
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(tokenSecret)) {
            return;
        }
        client.setToken(token, tokenSecret);
    }

    public static String blogAvatar(String blogName) {
        return client.blogAvatar(blogName);
    }

    public static List<Post> userDashboard() {
        return client.userDashboard();
    }

    public static List<Post> userDashboard(Map<String, Object> params) {
        return client.userDashboard(params);
    }
}
