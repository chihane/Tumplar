package mlxy.tumplar.tumblr;

import com.tumblr.jumblr.types.User;

import mlxy.tumplar.global.Constants;

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
}
