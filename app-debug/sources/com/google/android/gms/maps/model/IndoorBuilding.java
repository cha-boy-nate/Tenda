package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.maps.zzn;
import com.google.android.gms.internal.maps.zzq;
import com.google.android.gms.internal.maps.zzr;

public final class IndoorBuilding {
    @NonNull
    private final zzn zzdd;
    @NonNull
    private final zza zzde;

    @VisibleForTesting
    static class zza {
        public static final zza zzdf = new zza();

        private zza() {
        }

        @NonNull
        public static IndoorLevel zza(@NonNull zzq zzq) {
            return new IndoorLevel(zzq);
        }

        @NonNull
        public static zzq zza(IBinder iBinder) {
            return zzr.zzf(iBinder);
        }
    }

    public IndoorBuilding(@NonNull zzn zzn) {
        this(zzn, zza.zzdf);
    }

    public final java.util.List<com.google.android.gms.maps.model.IndoorLevel> getLevels() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:10:0x0033 in {5, 6, 9} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = r3.zzdd;	 Catch:{ RemoteException -> 0x002c }
        r0 = r0.getLevels();	 Catch:{ RemoteException -> 0x002c }
        r1 = new java.util.ArrayList;	 Catch:{ RemoteException -> 0x002c }
        r2 = r0.size();	 Catch:{ RemoteException -> 0x002c }
        r1.<init>(r2);	 Catch:{ RemoteException -> 0x002c }
        r0 = r0.iterator();	 Catch:{ RemoteException -> 0x002c }
    L_0x0013:
        r2 = r0.hasNext();	 Catch:{ RemoteException -> 0x002c }
        if (r2 == 0) goto L_0x002b;	 Catch:{ RemoteException -> 0x002c }
    L_0x0019:
        r2 = r0.next();	 Catch:{ RemoteException -> 0x002c }
        r2 = (android.os.IBinder) r2;	 Catch:{ RemoteException -> 0x002c }
        r2 = com.google.android.gms.maps.model.IndoorBuilding.zza.zza(r2);	 Catch:{ RemoteException -> 0x002c }
        r2 = com.google.android.gms.maps.model.IndoorBuilding.zza.zza(r2);	 Catch:{ RemoteException -> 0x002c }
        r1.add(r2);	 Catch:{ RemoteException -> 0x002c }
        goto L_0x0013;
    L_0x002b:
        return r1;
    L_0x002c:
        r0 = move-exception;
        r1 = new com.google.android.gms.maps.model.RuntimeRemoteException;
        r1.<init>(r0);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.model.IndoorBuilding.getLevels():java.util.List<com.google.android.gms.maps.model.IndoorLevel>");
    }

    @VisibleForTesting
    private IndoorBuilding(@NonNull zzn zzn, @NonNull zza zza) {
        this.zzdd = (zzn) Preconditions.checkNotNull(zzn, "delegate");
        this.zzde = (zza) Preconditions.checkNotNull(zza, "shim");
    }

    public final int getDefaultLevelIndex() {
        try {
            return this.zzdd.getDefaultLevelIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getActiveLevelIndex() {
        try {
            return this.zzdd.getActiveLevelIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isUnderground() {
        try {
            return this.zzdd.isUnderground();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof IndoorBuilding)) {
            return null;
        }
        try {
            return this.zzdd.zzb(((IndoorBuilding) obj).zzdd);
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    public final int hashCode() {
        try {
            return this.zzdd.zzj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
