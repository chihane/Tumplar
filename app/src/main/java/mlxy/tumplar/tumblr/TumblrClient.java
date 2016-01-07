package mlxy.tumplar.tumblr;

import com.tumblr.jumblr.types.*;
import com.tumblr.jumblr.types.User;

import java.util.List;
import java.util.Map;

import mlxy.tumplar.global.*;

public class TumblrClient {
    private static com.tumblr.jumblr.JumblrClient client;

    public static void initialize() {
        client = new com.tumblr.jumblr.JumblrClient(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    }

    public static void setToken(String token, String tokenSecret) {
        client.setToken(token, tokenSecret);
    }

    public static User userInfo() {
        return client.user();
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
