package com.onimaskesi.onimusicplayer.util;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public final class ViewUtil {

    public static void createSnackbar(String message, View view){

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("OK", new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();

    }

}
