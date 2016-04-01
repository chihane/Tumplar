package mlxy.tumplar.global;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import mlxy.tumplar.global.settings.PreviewQuality;

public class Settings {
    private static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.component.context());

    public static final String PREF_KEY_PREVIEW_QUALITY = "pref_key_preview_quality";
    public static PreviewQuality getPreviewQuality() {
        int identifier = Integer.valueOf(prefs.getString(PREF_KEY_PREVIEW_QUALITY, PreviewQuality.MEDIUM.getIdentifier()+""));
        return PreviewQuality.byIdentifier(identifier);
    }
}
