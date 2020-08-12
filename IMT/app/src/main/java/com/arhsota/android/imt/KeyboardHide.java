/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 12.08.20 19:21
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 05.08.20 0:04
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
