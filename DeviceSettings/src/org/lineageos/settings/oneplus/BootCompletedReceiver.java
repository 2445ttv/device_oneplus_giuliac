/* SPDX-License-Identifier: Apache-2.0 */
package org.lineageos.settings.oneplus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (UserHandle.myUserId() == UserHandle.USER_SYSTEM
                && (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action)
                        || Intent.ACTION_BOOT_COMPLETED.equals(action))) {
            context.startService(new Intent(context, RestoreService.class));
        }
    }
}
