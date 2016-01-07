package mlxy.tumplar.tumblr;

public class User {
    private static String access_token;
    private static String access_token_secret;

    public static void login(String token, String tokenSecret) {
        access_token = token;
        access_token_secret = tokenSecret;

        Tumblr.setToken(token, tokenSecret);
    }
}
