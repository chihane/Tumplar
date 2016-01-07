package mlxy.tumplar.view;

import android.content.Intent;

public interface OAuthView {
    void showAuthorizingDialogIfNotShown();
    void dismissAuthorizingDialogIfShown();
    void showError(Throwable e);
    void jumpTo(String url);

    void onAuthorized(String verifier);

    void showTokenAccessingDialogIfNotShown();
    void dismissTokenAccessingDialogIfShown();

    void close();

    void startActivity(Intent intent);

    void stopLoading();

    void showTokenAccessedPrompt();
}
