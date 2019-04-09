package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.common.wrappers.Wrappers;
import javax.annotation.concurrent.GuardedBy;

public final class zzp {
    private static Object sLock = new Object();
    @GuardedBy("sLock")
    private static boolean zzeo;
    private static String zzep;
    private static int zzeq;

    public static String zzc(Context context) {
        zze(context);
        return zzep;
    }

    public static int zzd(Context context) {
        zze(context);
        return zzeq;
    }

    private static void zze(Context context) {
        synchronized (sLock) {
            if (zzeo) {
                return;
            }
            zzeo = true;
            try {
                context = Wrappers.packageManager(context).getApplicationInfo(context.getPackageName(), 128).metaData;
                if (context == null) {
                    return;
                } else {
                    zzep = context.getString("com.google.app.id");
                    zzeq = context.getInt("com.google.android.gms.version");
                }
            } catch (Context context2) {
                Log.wtf("MetadataValueReader", "This should never happen.", context2);
            }
        }
    }
}
