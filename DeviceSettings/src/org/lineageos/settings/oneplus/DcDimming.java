/* SPDX-License-Identifier: Apache-2.0 */
package org.lineageos.settings.oneplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

final class DcDimming {
    static final String KEY = "dc_dimming";
    private static final String TAG = "OnePlusDcDimming";
    private static final String NODE = "/sys/kernel/oplus_display/pwm_onepulse";

    private DcDimming() {}

    private static SharedPreferences getPreferences(Context context) {
        return context.createDeviceProtectedStorageContext()
                .getSharedPreferences("device_settings", Context.MODE_PRIVATE);
    }

    static boolean getSavedEnabled(Context context) {
        return getPreferences(context).getBoolean(KEY, false);
    }

    static void saveEnabled(Context context, boolean enabled) {
        getPreferences(context).edit().putBoolean(KEY, enabled).apply();
    }

    // A missing or unsupported node must not be presented as a working toggle.
    static Boolean getEnabled() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NODE))) {
            String value = reader.readLine();
            if (value != null) {
                switch (value.trim()) {
                    case "0": return false;
                    case "1": return true;
                }
            }
        } catch (IOException e) {
            Log.w(TAG, "Cannot read DC dimming state", e);
        }
        return null;
    }

    static boolean setEnabled(boolean enabled) {
        if (Boolean.valueOf(enabled).equals(getEnabled())) {
            return true;
        }
        try (FileWriter writer = new FileWriter(NODE)) {
            writer.write(enabled ? "1" : "0");
        } catch (IOException e) {
            Log.e(TAG, "Cannot set DC dimming state", e);
            return false;
        }
        return Boolean.valueOf(enabled).equals(getEnabled());
    }
}
