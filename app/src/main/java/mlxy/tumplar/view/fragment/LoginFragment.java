package mlxy.tumplar.view.fragment;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import mlxy.tumplar.R;
import mlxy.tumplar.presenter.OAuthPresenter;
import mlxy.tumplar.view.OAuthView;
import mlxy.utils.T;

public class LoginFragment extends BaseFragment implements OAuthView {
    private OAuthPresenter oAuthPresenter;
    private ProgressDialog authorizingDialog;
    private View rootView;
    private WebView webView;
    private View loadingProgressBar;
    private ProgressDialog accessingTokenDialog;

    public LoginFragment() {
        oAuthPresenter = new OAuthPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_webview, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        initWebView(webView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oAuthPresenter.onTakeView(this);
    }

    @Override
    public void showAuthorizingDialogIfNotShown() {
        if (authorizingDialog == null) {
            authorizingDialog = new ProgressDialog(getActivity());
            authorizingDialog.setMessage(getString(R.string.msg_authorizing));
        }

        if (!authorizingDialog.isShowing()) {
            authorizingDialog.show();
        }
    }

    @Override
    public void dismissAuthorizingDialogIfShown() {
        if (authorizingDialog != null && authorizingDialog.isShowing()) {
            authorizingDialog.dismiss();
        }
    }

    @Override
    public void showError(Throwable e) {
        String errorMessage;
        if (e == null || TextUtils.isEmpty(e.getLocalizedMessage())) {
            errorMessage = getString(R.string.error_unknown);
        } else {
            errorMessage = e.getLocalizedMessage();
        }
        Snackbar snackbar = Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oAuthPresenter.refresh();
            }
        });
        snackbar.show();
    }

    @Override
    public void jumpTo(String url) {
        if (url != null) {
            webView.loadUrl(url);
        }
    }

    private void initWebView(WebView webView) {
        // Re-login every time
        removeAllCookies();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return oAuthPresenter.filterRedirection(url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoadingProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoadingProgress();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showError(new NetworkErrorException(description));
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
    }

    private void removeAllCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager.hasCookies()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeAllCookies(null);
            } else {
                cookieManager.removeAllCookie();
            }
        }
    }

    private void showLoadingProgress() {
        if (loadingProgressBar == null) {
            loadingProgressBar = rootView.findViewById(R.id.progressBar);
        }
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingProgress() {
        if (loadingProgressBar != null) {
            loadingProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void stopLoading() {
        webView.stopLoading();
    }

    @Override
    public void onAuthorized(String verifier) {
        if (oAuthPresenter != null) {
            oAuthPresenter.accessToken(verifier);
        }
    }

    @Override
    public void showTokenAccessingDialogIfNotShown() {
        if (accessingTokenDialog == null) {
            accessingTokenDialog = new ProgressDialog(getActivity());
            accessingTokenDialog.setMessage(getString(R.string.msg_accessing_token));
        }

        if (!accessingTokenDialog.isShowing()) {
            accessingTokenDialog.show();
        }
    }

    @Override
    public void dismissTokenAccessingDialogIfShown() {
        if (accessingTokenDialog != null && accessingTokenDialog.isShowing()) {
            accessingTokenDialog.dismiss();
        }
    }

    @Override
    public void showTokenAccessedPrompt() {
        T.showShort(getActivity(), getString(R.string.msg_token_accessed));
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
