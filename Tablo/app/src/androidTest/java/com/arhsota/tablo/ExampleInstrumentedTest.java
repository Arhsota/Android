/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 10 january 2019
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 25.01.2022, 19:14
 *  
 ******************************************************************************/

package com.arhsota.tablo;

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

        assertEquals("com.arhsota.stopwatch", appContext.getPackageName());
    }
}
