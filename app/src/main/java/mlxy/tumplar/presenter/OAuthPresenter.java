package mlxy.tumplar.presenter;

import android.content.Intent;
import android.net.Uri;

import mlxy.tumplar.R;
import mlxy.tumplar.global.App;
import mlxy.tumplar.global.Constants;
import mlxy.tumplar.global.User;
import mlxy.tumplar.internal.Authorizer;
import mlxy.tumplar.view.OAuthView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OAuthPresenter {
    private static String callbackUrl = App.component.context().getString(R.string.oauth_callback_scheme) + "://" + App.component.context().getString(R.string.oauth_callback_host);

    enum State { IDLE, AUTHORIZING, AUTHORIZED, ACCESSING_TOKEN, TOKEN_ACCESSED, ERROR }

    private OAuthView view;
    private Throwable error;

    private State state = State.IDLE;

    private String tempToken;
    private String tempTokenSecret;

    public OAuthPresenter() {
        refresh();
    }

    public void refresh() {
        Authorizer.requestTempToken(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET, callbackUrl)
                .flatMap(oAuthCredentialsResponse -> {
                    tempToken = oAuthCredentialsResponse.token;
                    tempTokenSecret = oAuthCredentialsResponse.tokenSecret;
                    return Authorizer.authorize(Constants.CONSUMER_SECRET, tempToken, tempTokenSecret);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    state = State.AUTHORIZING;
                    publish();
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        state = State.AUTHORIZED;
                        publish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        error = e;
                        state = State.ERROR;
                        publish();
                    }

                    @Override
                    public void onNext(String callbackUrl) {
                        if (view != null) {
                            view.jumpTo(callbackUrl);
                        }
                    }
                });
    }

    public boolean filterRedirection(String url) {
        if (url != null) {
            Uri uri = Uri.parse(url);

            String scheme = uri.getScheme();

            // Eject requests in other schemes.
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                view.startActivity(intent);
                view.stopLoading();
                return true;
            }

            // OAuth authorization callback. See https://www.tumblr.com/docs/en/api/v2#auth
            if (url.contains(App.component.context().getString(R.string.oauth_callback_scheme)) && url.contains(App.component.context().getString(R.string.oauth_callback_host))) {
                // https://www.tumblr.com/oauth/oauth_callback://callback?oauth_token=***&oauth_verifier=***#_=_
                String callbackUrl = url.replace("https://www.tumblr.com/oauth/", "");
                handleCallback(callbackUrl);
                return true;
            }
        }

        return false;
    }

    private void handleCallback(String callbackUrl) {
        // Permitted:   oauth_callback://callback?oauth_token=***&oauth_verifier=***#_=_
        // Rejected:    oauth_callback://callback#_=_
        Uri callbackUri = Uri.parse(callbackUrl);
        boolean authorizationPermitted = callbackUri.getQueryParameterNames().contains("oauth_verifier");
        if (authorizationPermitted) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(callbackUrl));
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);

            view.startActivity(intent);
            view.stopLoading();
        } else {
            view.close();
        }
    }

    public void accessToken(String verifier) {
        Authorizer.accessToken(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET, tempToken, tempTokenSecret, verifier)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(oAuthCredentialsResponse -> Observable.create(subscriber -> {
                    String token = oAuthCredentialsResponse.token;
                    String tokenSecret = oAuthCredentialsResponse.tokenSecret;

                    User.saveToken(token, tokenSecret);
                    User.tryLogin();

                    subscriber.onCompleted();
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    state = State.ACCESSING_TOKEN;
                    publish();
                })
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        state = State.TOKEN_ACCESSED;
                        publish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        error = e;
                        state = State.ERROR;
                        publish();
                    }

                    @Override
                    public void onNext(Object nothing) {
                    }
                });
    }

    public void onTakeView(OAuthView view) {
        this.view = view;
        publish();
    }

    public void publish() {
        if (view != null) {
            switch (state) {
                case AUTHORIZING:
                    view.showAuthorizingDialogIfNotShown();
                    break;

                case AUTHORIZED:
                    view.dismissAuthorizingDialogIfShown();
                    break;

                case ACCESSING_TOKEN:
                    view.showTokenAccessingDialogIfNotShown();
                    break;

                case TOKEN_ACCESSED:
                    view.dismissTokenAccessingDialogIfShown();
                    view.showTokenAccessedPrompt();
                    view.close();
                    break;

                case ERROR:
                    view.dismissAuthorizingDialogIfShown();
                    view.dismissTokenAccessingDialogIfShown();
                    view.showError(error);
                    break;

                case IDLE:
                default:
                    view.dismissAuthorizingDialogIfShown();
                    view.dismissTokenAccessingDialogIfShown();
                    break;
            }
        }
    }
}
