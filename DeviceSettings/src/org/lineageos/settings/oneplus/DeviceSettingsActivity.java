/* SPDX-License-Identifier: Apache-2.0 */
package org.lineageos.settings.oneplus;

import android.os.Bundle;

import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;

public class DeviceSettingsActivity extends CollapsingToolbarBaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.android.settingslib.collapsingtoolbar.R.id.content_frame,
                            new DeviceSettingsFragment())
                    .commit();
        }
    }
}
