package mlxy.tumplar.tumblr;

import android.support.annotation.NonNull;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.security.GeneralSecurityException;

import mlxy.tumplar.global.Constants;
import rx.Observable;
import rx.Subscriber;

public class Authorizer {
    public static final String URL_REQUEST_TOKEN = "https://www.tumblr.com/oauth/request_token";
    public static final String URL_AUTHORIZE = "https://www.tumblr.com/oauth/authorize";
    public static final String URL_ACCESS_TOKEN = "https://www.tumblr.com/oauth/access_token";

    @NonNull
    public static Observable<OAuthCredentialsResponse> requestTempToken(final String consumerKey, final String consumerSecret, final String callbackUrl) {
        return Observable.create(new Observable.OnSubscribe<OAuthCredentialsResponse>() {
            @Override
            public void call(Subscriber<? super OAuthCredentialsResponse> subscriber) {
                try {
                    OAuthHmacSigner signer = new OAuthHmacSigner();
                    signer.clientSharedSecret = consumerSecret;

                    OAuthGetTemporaryToken temporaryToken = new OAuthGetTemporaryToken(URL_REQUEST_TOKEN);
                    temporaryToken.signer = signer;
                    temporaryToken.consumerKey = consumerKey;
                    temporaryToken.transport = new NetHttpTransport();
                    temporaryToken.callback = callbackUrl;

                    OAuthCredentialsResponse response = temporaryToken.execute();
                    subscriber.onNext(response);
                    subscriber.onCompleted();

                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    @NonNull
    public static Observable<String> authorize(final String consumerSecret, final String tempToken, final String tempTokenSecret) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                OAuthHmacSigner signer = new OAuthHmacSigner();
                signer.clientSharedSecret = consumerSecret;
                signer.tokenSharedSecret = tempTokenSecret;

                OAuthAuthorizeTemporaryTokenUrl authorizeTemporaryTokenUrl = new OAuthAuthorizeTemporaryTokenUrl(URL_AUTHORIZE);
                authorizeTemporaryTokenUrl.temporaryToken = tempToken;

                String authorizeUrl = authorizeTemporaryTokenUrl.build();
                subscriber.onNext(authorizeUrl);
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<OAuthCredentialsResponse> accessToken(final String consumerKey, final String consumerSecret, final String tempToken, final String tempTokenSecret, final String verifier) {
        return Observable.create(new Observable.OnSubscribe<OAuthCredentialsResponse>() {
            @Override
            public void call(Subscriber<? super OAuthCredentialsResponse> subscriber) {
                try {
                    OAuthHmacSigner signer = new OAuthHmacSigner();
                    signer.clientSharedSecret = consumerSecret;
                    signer.tokenSharedSecret = tempTokenSecret;

                    OAuthGetAccessToken accessToken = new OAuthGetAccessToken(URL_ACCESS_TOKEN);
                    accessToken.transport = new NetHttpTransport();
                    accessToken.temporaryToken = tempToken;
                    accessToken.signer = signer;
                    accessToken.consumerKey = consumerKey;
                    accessToken.verifier = verifier;

                    OAuthCredentialsResponse tokenResponse = accessToken.execute();
                    subscriber.onNext(tokenResponse);
                    subscriber.onCompleted();

                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
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
