package mlxy.tumplar.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import mlxy.tumplar.R;

public class ChangelogDialogFragment extends DialogFragment {
    public static final String TAG = "changelog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // XXX Any better solution for dialog creating?
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_changelog, null, false);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.changelog)
                .setView(view)
                .setNegativeButton(R.string.close, null)
                .create();
    }
}
