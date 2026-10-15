/* SPDX-License-Identifier: Apache-2.0 */
package org.lineageos.settings.oneplus;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public class RestoreService extends Service {
    private static final int MAX_ATTEMPTS = 5;
    private static final long RETRY_DELAY_MS = 500;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private int mAttempts;

    private final BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // The panel may still be powering on when SCREEN_ON arrives.
            scheduleRestore();
        }
    };

    private final Runnable mRestore = this::restore;

    private void restore() {
        if (DcDimming.getEnabled() == null
                || DcDimming.setEnabled(DcDimming.getSavedEnabled(this))) {
            stopSelf();
        } else if (++mAttempts < MAX_ATTEMPTS) {
            mHandler.postDelayed(mRestore, RETRY_DELAY_MS);
        }
        // If the display is off, wait for SCREEN_ON before trying again.
    }

    private void scheduleRestore() {
        mHandler.removeCallbacksAndMessages(null);
        mAttempts = 0;
        mHandler.postDelayed(mRestore, RETRY_DELAY_MS);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(mScreenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON),
                Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleRestore();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(mScreenReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
