/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on Septenber 2018
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 16.10.20 16:40
 *
 ******************************************************************************/

package com.arhsota.android.imt;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardHide{
    public static void hide(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
