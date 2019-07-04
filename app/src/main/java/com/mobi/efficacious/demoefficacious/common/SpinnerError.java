package com.mobi.efficacious.demoefficacious.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerError {
    private Context _context;

    public SpinnerError(Context context){
        this._context = context;
    }
    public void setSpinnerError(Spinner spinner, String error) {
        try {
            View selectedView = spinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                spinner.requestFocus();
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError(error); // any name of the error will do
                selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
                selectedTextView.setText(error); // actual error message
                spinner.performClick(); // to open the spinner list if error is found.

            }
        }catch (Exception ex)
        {

        }

    }

}
