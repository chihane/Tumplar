package mlxy.tumplar.interceptor;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.security.GeneralSecurityException;

import mlxy.tumplar.global.Constants;
import mlxy.tumplar.global.User;

public class AuthHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.httpUrl().toString();
        String header = null;
        try {
            header = getAuthorizationHeader(User.access_token, User.access_token_secret, HttpMethods.GET, url);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        Request newRequest = null;
        if (header != null) {
            newRequest = request.newBuilder().addHeader("Authorization",
                    header).build();
        }
        return chain.proceed(newRequest);
    }

    public static String getAuthorizationHeader(String token, String tokenSecret, String httpMethod, String url) throws GeneralSecurityException {
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = Constants.CONSUMER_SECRET;
        signer.tokenSharedSecret = tokenSecret;

        OAuthParameters params = new OAuthParameters();
        params.consumerKey = Constants.CONSUMER_KEY;
        params.signer = signer;
        params.token = token;
        params.computeNonce();
        params.computeTimestamp();

        params.computeSignature(httpMethod, new GenericUrl(url));

        return params.getAuthorizationHeader();
    }
}
