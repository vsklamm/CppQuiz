package com.vsklamm.cppquiz.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.view.ContextThemeWrapper;

import com.vsklamm.cppquiz.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;
import static com.vsklamm.cppquiz.utils.ActivityUtils.APP_THEME_IS_DARK;

public class GoToDialog extends AppCompatDialogFragment {

    private GoToDialog.DialogListener listener;

    private EditText editTextNumber;

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

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_goto, null); // TODO: auto opening keyboard

        dialogBuilder.setView(view)
                .setTitle(R.string.goto_dialog_title)
                .setMessage(R.string.goto_dialog_text)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setPositiveButton(R.string.go, (dialog, which) -> {
                    try {
                        int number = Integer.parseInt(editTextNumber.getText().toString());
                        listener.applyGoTo(number);
                    } catch (NumberFormatException e) {
                        dialog.dismiss();
                    }
                });
        editTextNumber = view.findViewById(R.id.ed_dialog_goto_id);
        editTextNumber.requestFocus();

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            listener = (GoToDialog.DialogListener) context;
        } catch (ClassCastException e) {
            // ignore
        }
    }

    public interface DialogListener {
        void applyGoTo(final int number);
    }

}
