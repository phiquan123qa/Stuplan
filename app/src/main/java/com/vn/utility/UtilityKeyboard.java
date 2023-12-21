package com.vn.utility;

import android.app.Activity;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vn.appdesign.R;


public class UtilityKeyboard {
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public static void clearData(EditText editText) {
        editText.getText().clear();
    }

    public static void togglePasswordVisibility(EditText editTextPassword) {
        int inputType = editTextPassword.getInputType();

        if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
            // Password is currently visible, so hide it
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
        } else {
            // Password is currently hidden, so show it
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);

        }

        // Move the cursor to the end of the text
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

}
