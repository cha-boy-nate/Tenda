package com.google.android.gms.common;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzm;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import javax.annotation.CheckReturnValue;

@CheckReturnValue
final class zzc {
    private static volatile zzm zzn;
    private static final Object zzo = new Object();
    private static Context zzp;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static synchronized void zza(android.content.Context r2) {
        /*
        r0 = com.google.android.gms.common.zzc.class;
        monitor-enter(r0);
        r1 = zzp;	 Catch:{ all -> 0x001a }
        if (r1 != 0) goto L_0x0011;
    L_0x0007:
        if (r2 == 0) goto L_0x0018;
    L_0x0009:
        r2 = r2.getApplicationContext();	 Catch:{ all -> 0x001a }
        zzp = r2;	 Catch:{ all -> 0x001a }
        monitor-exit(r0);
        return;
    L_0x0011:
        r2 = "GoogleCertificates";
        r1 = "GoogleCertificates has been initialized already";
        android.util.Log.w(r2, r1);	 Catch:{ all -> 0x001a }
    L_0x0018:
        monitor-exit(r0);
        return;
    L_0x001a:
        r2 = move-exception;
        monitor-exit(r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.zzc.zza(android.content.Context):void");
    }

    static zzm zza(String str, zze zze, boolean z) {
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            str = zzb(str, zze, z);
            return str;
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    private static zzm zzb(String str, zze zze, boolean z) {
        try {
            if (zzn == null) {
                Preconditions.checkNotNull(zzp);
                synchronized (zzo) {
                    if (zzn == null) {
                        zzn = zzn.zzc(DynamiteModule.load(zzp, DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING, "com.google.android.gms.googlecertificates").instantiate("com.google.android.gms.common.GoogleCertificatesImpl"));
                    }
                }
            }
            Preconditions.checkNotNull(zzp);
            try {
                if (zzn.zza(new zzk(str, zze, z), ObjectWrapper.wrap(zzp.getPackageManager()))) {
                    return zzm.zze();
                }
                return zzm.zza(new zzd(z, str, zze));
            } catch (String str2) {
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", str2);
                return zzm.zza("module call", str2);
            }
        } catch (String str22) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", str22);
            zze = "module init: ";
            z = String.valueOf(str22.getMessage());
            return zzm.zza(z.length() != 0 ? zze.concat(z) : new String(zze), str22);
        }
    }

    static final /* synthetic */ String zza(boolean z, String str, zze zze) throws Exception {
        boolean z2 = true;
        if (z || !zzb(str, zze, true).zzac) {
            z2 = false;
        }
        return zzm.zza(str, zze, z, z2);
    }
}
