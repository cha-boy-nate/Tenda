package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public class StatsUtils {
    @KeepForSdk
    public static String getEventKey(Context context, Intent intent) {
        return String.valueOf(((long) System.identityHashCode(intent)) | (((long) System.identityHashCode(context)) << 32));
    }

    @KeepForSdk
    public static String getEventKey(WakeLock wakeLock, String str) {
        wakeLock = String.valueOf(String.valueOf((((long) Process.myPid()) << 32) | ((long) System.identityHashCode(wakeLock))));
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        str = String.valueOf(str);
        return str.length() != 0 ? wakeLock.concat(str) : new String(wakeLock);
    }
}
