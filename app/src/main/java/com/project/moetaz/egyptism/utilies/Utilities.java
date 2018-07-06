package com.project.moetaz.egyptism.utilies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.project.moetaz.egyptism.R;
import com.project.moetaz.egyptism.interfaces.Action;



public class Utilities {

    public static void message(Context context, Object s) {
        Toast.makeText(context, s+" ", Toast.LENGTH_LONG).show();
    }


    public static void showSnack(View view, String snackMessage, final String undoMessage, final Action action){
        Snackbar snackbar = Snackbar
                .make(view, snackMessage, Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar1 = Snackbar.make(view, undoMessage, Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        action.undo();
                    }
                });

        snackbar.show();
    }


    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
