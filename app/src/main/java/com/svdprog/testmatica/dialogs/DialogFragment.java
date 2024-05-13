package com.svdprog.testmatica.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.svdprog.testmatica.R;

// This dialog box is designed to display the loading status of an item

public class DialogFragment {
    private Activity activity;
    private AlertDialog dialog;
    public DialogFragment(Activity myActivity) {
        activity = myActivity;
    }
    @SuppressLint("InflateParams")
    public void startLoadingdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
    public void finishdialog() {
        dialog.dismiss();
    }
}
