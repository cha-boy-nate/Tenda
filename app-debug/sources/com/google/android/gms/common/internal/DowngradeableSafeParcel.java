package com.google.android.gms.common.internal;

import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

@KeepForSdk
public abstract class DowngradeableSafeParcel extends AbstractSafeParcelable implements ReflectedParcelable {
    private static final Object zzdb = new Object();
    private static ClassLoader zzdc = null;
    private static Integer zzdd = null;
    private boolean zzde = false;

    @KeepForSdk
    protected abstract boolean prepareForClientVersion(int i);

    private static ClassLoader zzp() {
        synchronized (zzdb) {
        }
        return null;
    }

    @KeepForSdk
    protected static Integer getUnparcelClientVersion() {
        synchronized (zzdb) {
        }
        return null;
    }

    @KeepForSdk
    protected boolean shouldDowngrade() {
        return this.zzde;
    }

    @KeepForSdk
    public void setShouldDowngrade(boolean z) {
        this.zzde = z;
    }

    @KeepForSdk
    protected static boolean canUnparcelSafely(String str) {
        zzp();
        return true;
    }
}
