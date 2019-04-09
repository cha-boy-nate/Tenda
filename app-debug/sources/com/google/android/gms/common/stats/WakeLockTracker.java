package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.util.zza;
import java.util.Arrays;
import java.util.List;

@KeepForSdk
public class WakeLockTracker {
    @VisibleForTesting
    private static boolean zzfb = false;
    private static WakeLockTracker zzgb = new WakeLockTracker();
    private static Boolean zzgc;

    @KeepForSdk
    public static WakeLockTracker getInstance() {
        return zzgb;
    }

    @KeepForSdk
    public void registerAcquireEvent(Context context, Intent intent, String str, String str2, String str3, int i, String str4) {
        Intent intent2 = intent;
        Context context2 = context;
        registerEvent(context2, intent.getStringExtra(LoggingConstants.EXTRA_WAKE_LOCK_KEY), 7, str, str2, str3, i, Arrays.asList(new String[]{str4}));
    }

    @KeepForSdk
    public void registerReleaseEvent(Context context, Intent intent) {
        registerEvent(context, intent.getStringExtra(LoggingConstants.EXTRA_WAKE_LOCK_KEY), 8, null, null, null, 0, null);
    }

    @KeepForSdk
    public void registerEvent(Context context, String str, int i, String str2, String str3, String str4, int i2, List<String> list) {
        registerEvent(context, str, i, str2, str3, str4, i2, list, 0);
    }

    @KeepForSdk
    public void registerEvent(Context context, String str, int i, String str2, String str3, String str4, int i2, List<String> list, long j) {
        int i3 = i;
        List list2 = list;
        if (zzgc == null) {
            zzgc = Boolean.valueOf(false);
        }
        if (!zzgc.booleanValue()) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            String str5 = "WakeLockTracker";
            String str6 = "missing wakeLock key. ";
            String valueOf = String.valueOf(str);
            Log.e(str5, valueOf.length() != 0 ? str6.concat(valueOf) : new String(str6));
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (7 == i3 || 8 == i3 || 10 == i3 || 11 == i3) {
            List list3;
            String str7;
            if (list2 == null || list.size() != 1) {
                list3 = list2;
            } else {
                if ("com.google.android.gms".equals(list2.get(0))) {
                    list2 = null;
                }
                list3 = list2;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            int zzg = zza.zzg(context);
            str6 = context.getPackageName();
            if ("com.google.android.gms".equals(str6)) {
                str7 = null;
            } else {
                str7 = str6;
            }
            WakeLockEvent wakeLockEvent = r1;
            WakeLockEvent wakeLockEvent2 = new WakeLockEvent(currentTimeMillis, i, str2, i2, list3, str, elapsedRealtime, zzg, str3, str7, zza.zzh(context), j, str4);
            try {
                context.startService(new Intent().setComponent(LoggingConstants.zzfg).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", wakeLockEvent));
            } catch (Throwable e) {
                Log.wtf("WakeLockTracker", e);
            }
        }
    }
}
