package com.test.packages;

import android.support.annotation.Nullable;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by arhso on 11.03.2018.
 * Lesson 18
 *
 */

public class RootHelper {

    public static boolean uninstall(String packageName) {
        String output = executeCommand("pm uninstall " +
                packageName);
        if (output != null &&
                output.toLowerCase().contains("success")) {
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private static String executeCommand(String command) {
        List<String> stdout = Shell.SU.run(command);
        if (stdout == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : stdout) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

}
