package com.vsklamm.cppquiz.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.vsklamm.cppquiz.R;

import java.util.Objects;

public class GoToDialog extends AppCompatDialogFragment {

    private GoToDialog.DialogListener listener;

    private EditText editTextNumber;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_goto, null); // TODO: auto opening keyboard

        dialogBuilder.setView(view)
                .setTitle(R.string.goto_dialog_title)
                .setMessage(R.string.goto_dialog_text)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int number = Integer.parseInt(editTextNumber.getText().toString());
                            listener.applyGoTo(number);
                        } catch (NumberFormatException e) {
                            dialog.dismiss();
                        }
                    }
                });
        editTextNumber = view.findViewById(R.id.ed_dialog_goto_id);
        editTextNumber.requestFocus();

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
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
