package mlxy.tumplar.view.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import de.psdev.licensesdialog.LicensesDialog;
import mlxy.tumplar.BuildConfig;
import mlxy.tumplar.R;
import mlxy.tumplar.view.dialog.ChangelogDialogFragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_config);
    }
}
