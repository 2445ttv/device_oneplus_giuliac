/* SPDX-License-Identifier: Apache-2.0 */
package org.lineageos.settings.oneplus;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.SwitchPreferenceCompat;

import com.android.settingslib.widget.SettingsBasePreferenceFragment;

public class DeviceSettingsFragment extends SettingsBasePreferenceFragment {
    private SwitchPreferenceCompat mDcDimming;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.device_settings, rootKey);
        mDcDimming = findPreference(DcDimming.KEY);
        mDcDimming.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabled = (Boolean) newValue;
            if (!DcDimming.setEnabled(enabled)) {
                Toast.makeText(requireContext(), R.string.dc_dimming_error,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            DcDimming.saveEnabled(requireContext(), enabled);
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Boolean enabled = DcDimming.getEnabled();
        mDcDimming.setEnabled(enabled != null);
        mDcDimming.setChecked(Boolean.TRUE.equals(enabled));
        mDcDimming.setSummary(enabled == null
                ? R.string.dc_dimming_unavailable : R.string.dc_dimming_summary);
    }
}
