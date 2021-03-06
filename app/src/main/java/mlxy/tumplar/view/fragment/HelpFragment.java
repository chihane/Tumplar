package mlxy.tumplar.view.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import de.psdev.licensesdialog.LicensesDialog;
import mlxy.tumplar.BuildConfig;
import mlxy.tumplar.R;
import mlxy.tumplar.view.dialog.ChangelogDialogFragment;
import mlxy.utils.T;

public class HelpFragment extends PreferenceFragment {
    private Preference prefChangelog;
    private Preference prefLicense;
    private Preference prefVersion;

    private ChangelogDialogFragment changelogDialog;
    private LicensesDialog licensesDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_help);

        prefChangelog = findPreference(getString(R.string.pref_key_changelog));
        prefLicense = findPreference(getString(R.string.pref_key_license));
        prefVersion = findPreference(getString(R.string.pref_key_version));

        Preference prefVersion = findPreference(getString(R.string.pref_key_version));
        prefVersion.setSummary(BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == prefChangelog) {
            showChangeLog();
            return true;
        }
        if (preference == prefLicense) {
            showLicense();
            return true;
        }
        if (preference == prefVersion) {
            versionClicked();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void showChangeLog() {
        if (changelogDialog == null) {
            changelogDialog = new ChangelogDialogFragment();
        }

        if (!changelogDialog.isAdded()) {
            changelogDialog.show(getFragmentManager(), ChangelogDialogFragment.TAG);
        }
    }

    private void showLicense() {
        if (licensesDialog == null) {
            licensesDialog = new LicensesDialog.Builder(getActivity())
                    .setNotices(R.raw.licenses)
                    .setTitle(R.string.license)
                    .setThemeResourceId(R.style.AppTheme)
                    .build();
        }
        licensesDialog.show();
    }

    private int versionClicked;
    private void versionClicked() {
        if (versionClicked == 8) {
            T.showShort(getActivity(), "Easter egg coming soon (｀･ω･´)");
        }
        versionClicked++;
    }
}
