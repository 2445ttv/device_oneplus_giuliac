/* SPDX-License-Identifier: Apache-2.0 */
package org.lineageos.settings.oneplus;

import android.content.Context;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;

final class HapticStyle {
    static final String KEY = "haptic_style";
    private static final String TAG = "OnePlusHapticStyle";
    // Device init forwards this system property to the vendor vibrator HAL.
    private static final String PROPERTY = "debug.haptic_profile";
    private static final String SETTINGS_KEY = "haptic_effects_profile";

    private HapticStyle() {}

    static boolean isSupported(Context context) {
        String[] values = context.getResources().getStringArray(R.array.haptic_effect_style_values);
        String[] entries = context.getResources().getStringArray(R.array.haptic_effect_style_entries);
        return values.length > 0 && values.length == entries.length;
    }

    private static boolean isValid(Context context, String value) {
        if (!isSupported(context) || value == null || value.isEmpty()) {
            return false;
        }
        for (String supported : context.getResources().getStringArray(
                R.array.haptic_effect_style_values)) {
            if (supported.equals(value)) {
                return true;
            }
        }
        return false;
    }

    static String getCurrent(Context context) {
        if (!isSupported(context)) {
            return null;
        }
        // The vibrator profile is device-wide and owned by the primary user.
        String value = Settings.Secure.getStringForUser(context.getContentResolver(),
                SETTINGS_KEY, UserHandle.USER_SYSTEM);
        if (isValid(context, value)) {
            return value;
        }
        return context.getResources().getStringArray(R.array.haptic_effect_style_values)[0];
    }

    static boolean setCurrent(Context context, String value) {
        if (!isValid(context, value)) {
            return false;
        }
        try {
            SystemProperties.set(PROPERTY, value);
            // Retain selections saved by the previous Settings implementation.
            return Settings.Secure.putStringForUser(context.getContentResolver(),
                    SETTINGS_KEY, value, UserHandle.USER_SYSTEM);
        } catch (RuntimeException e) {
            Log.e(TAG, "Cannot set haptic effect style", e);
            return false;
        }
    }

    static void restore(Context context) {
        if (isSupported(context)) {
            setCurrent(context, getCurrent(context));
        }
    }
}
