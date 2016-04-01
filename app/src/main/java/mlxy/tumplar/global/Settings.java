package mlxy.tumplar.global;

import mlxy.tumplar.global.settings.PreviewQuality;
import mlxy.utils.Prefs;

public class Settings {
    public static final String PREF_KEY_PREVIEW_QUALITY = "pref_key_preview_quality";
    public static PreviewQuality getPreviewQuality() {
        int identifier = Prefs.get(App.component.context(), PREF_KEY_PREVIEW_QUALITY, PreviewQuality.MEDIUM.getIdentifier());
        return PreviewQuality.byIdentifier(identifier);
    }
}
