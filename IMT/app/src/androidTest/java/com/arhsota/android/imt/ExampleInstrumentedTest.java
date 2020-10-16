/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on Septenber 2018
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 16.10.20 16:40
 *
 ******************************************************************************/

package com.arhsota.android.imt;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.arhsota.android.imt", appContext.getPackageName());
    }
}
