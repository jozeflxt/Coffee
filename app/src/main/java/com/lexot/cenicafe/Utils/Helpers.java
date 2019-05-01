package com.lexot.cenicafe.Utils;

import android.content.Context;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.lexot.cenicafe.R;

public class Helpers {

    private static AwesomeProgressDialog loadingView;

    public static void showLoading(Context context, String title, String description) {
        loadingView = new AwesomeProgressDialog(context);
        loadingView
                .setTitle(title)
                .setMessage(description)
                .setProgressBarColor(R.color.dialogProgressBackgroundColor)
                .setCancelable(false)
                .show();
    }

    public static void hideLoading() {
        loadingView.hide();
    }
}
