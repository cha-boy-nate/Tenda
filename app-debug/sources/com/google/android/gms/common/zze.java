package com.google.android.gms.common;

import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzi;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import java.util.Arrays;

abstract class zze extends zzj {
    private int zzt;

    protected zze(byte[] bArr) {
        Preconditions.checkArgument(bArr.length == 25);
        this.zzt = Arrays.hashCode(bArr);
    }

    abstract byte[] getBytes();

    public int hashCode() {
        return this.zzt;
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof zzi) {
                try {
                    zzi zzi = (zzi) obj;
                    if (zzi.zzc() != hashCode()) {
                        return false;
                    }
                    obj = zzi.zzb();
                    if (obj == null) {
                        return false;
                    }
                    return Arrays.equals(getBytes(), (byte[]) ObjectWrapper.unwrap(obj));
                } catch (Object obj2) {
                    Log.e("GoogleCertificates", "Failed to get Google certificates from remote", obj2);
                    return false;
                }
            }
        }
        return false;
    }

    public final IObjectWrapper zzb() {
        return ObjectWrapper.wrap(getBytes());
    }

    public final int zzc() {
        return hashCode();
    }

    protected static byte[] zza(String str) {
        try {
            return str.getBytes("ISO-8859-1");
        } catch (String str2) {
            throw new AssertionError(str2);
        }
    }
}
