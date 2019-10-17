package com.vsklamm.cppquiz.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.view.ContextThemeWrapper;

import com.vsklamm.cppquiz.R;

import static android.content.Context.MODE_PRIVATE;
import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;
import static com.vsklamm.cppquiz.utils.ActivityUtils.APP_THEME_IS_DARK;

public class ConfirmResetDialog extends AppCompatDialogFragment {

    private DialogListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /* WELCOME THIS IS KLUDGE */
        SharedPreferences appPreferences = getActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        boolean isDark = appPreferences.getBoolean(APP_THEME_IS_DARK, false);
        int theme = isDark ? R.style.DarkAlertDialog : R.style.AlertDialog;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), theme)
        );

        dialogBuilder
                .setTitle(R.string.reset_dialog_title)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setPositiveButton(R.string.yes, (dialog, which) -> listener.onConfirmedReset());
        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            // ignore
        }
    }

    public interface DialogListener {
        void onConfirmedReset();
    }
}
