package mlxy.tumplar.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import mlxy.tumplar.R;
import mlxy.utils.L;

public class LoginFragmentII extends SupportWebViewFragment {
    public static final String ARG_KEY_URL = "url";

    private String url;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString(ARG_KEY_URL);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = getWebView();
        if (webView != null) {
            initWebView(webView);
            webView.loadUrl(url);
        }
    }

    private void initWebView(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.v(url);
                if (url != null) {
                    Uri uri = Uri.parse(url);
                    String scheme = uri.getScheme();
                    if (!"http".equals(scheme) && !"https".equals(scheme)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                        view.stopLoading();
                        return true;
                    }

                    if (url.contains(getString(R.string.oauth_callback_scheme)) && url.contains(getString(R.string.oauth_callback_host))) {
                        String newUrl = url.replace("https://www.tumblr.com/oauth/", "");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(newUrl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
                        startActivity(intent);
                        view.stopLoading();
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
    }
}
