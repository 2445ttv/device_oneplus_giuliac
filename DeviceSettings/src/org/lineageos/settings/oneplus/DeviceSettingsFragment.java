/* SPDX-License-Identifier: Apache-2.0 */
package org.lineageos.settings.oneplus;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.android.settingslib.widget.SettingsBasePreferenceFragment;

public class DeviceSettingsFragment extends SettingsBasePreferenceFragment {
    private SwitchPreferenceCompat mDcDimming;
    private ListPreference mHapticStyle;

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

        if (HapticStyle.isSupported(requireContext())) {
            mHapticStyle = findPreference(HapticStyle.KEY);
            mHapticStyle.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            mHapticStyle.setOnPreferenceChangeListener((preference, newValue) -> {
                if (!HapticStyle.setCurrent(requireContext(), (String) newValue)) {
                    Toast.makeText(requireContext(), R.string.haptic_effect_style_error,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            });
        } else {
            getPreferenceScreen().removePreference(findPreference("haptics_category"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Boolean enabled = DcDimming.getEnabled();
        mDcDimming.setEnabled(enabled != null);
        mDcDimming.setChecked(Boolean.TRUE.equals(enabled));
        mDcDimming.setSummary(enabled == null
                ? R.string.dc_dimming_unavailable : R.string.dc_dimming_summary);
        if (mHapticStyle != null) {
            mHapticStyle.setValue(HapticStyle.getCurrent(requireContext()));
        }
    }
}
