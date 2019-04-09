package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule.LoadingException;
import com.google.android.gms.dynamite.DynamiteModule.VersionPolicy;
import com.google.android.gms.dynamite.DynamiteModule.VersionPolicy.zza;
import com.google.android.gms.dynamite.DynamiteModule.VersionPolicy.zzb;

final class zzg implements VersionPolicy {
    zzg() {
    }

    public final zzb zza(Context context, String str, zza zza) throws LoadingException {
        zzb zzb = new zzb();
        zzb.zziq = zza.getLocalVersion(context, str);
        if (zzb.zziq != 0) {
            zzb.zzir = zza.zza(context, str, false);
        } else {
            zzb.zzir = zza.zza(context, str, true);
        }
        if (zzb.zziq == null && zzb.zzir == null) {
            zzb.zzis = 0;
        } else if (zzb.zzir >= zzb.zziq) {
            zzb.zzis = 1;
        } else {
            zzb.zzis = -1;
        }
        return zzb;
    }
}
