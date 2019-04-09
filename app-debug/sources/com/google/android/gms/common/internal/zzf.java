package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.HashSet;
import java.util.Set;

final class zzf implements ServiceConnection {
    private ComponentName mComponentName;
    private int mState = 2;
    private IBinder zzcy;
    private final Set<ServiceConnection> zzdz = new HashSet();
    private boolean zzea;
    private final zza zzeb;
    private final /* synthetic */ zze zzec;

    public zzf(zze zze, zza zza) {
        this.zzec = zze;
        this.zzeb = zza;
    }

    public final void onServiceConnected(android.content.ComponentName r5, android.os.IBinder r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:13:0x0034 in {6, 9, 12} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r4.zzec;
        r0 = r0.zzdu;
        monitor-enter(r0);
        r1 = r4.zzec;	 Catch:{ all -> 0x0031 }
        r1 = r1.mHandler;	 Catch:{ all -> 0x0031 }
        r2 = r4.zzeb;	 Catch:{ all -> 0x0031 }
        r3 = 1;	 Catch:{ all -> 0x0031 }
        r1.removeMessages(r3, r2);	 Catch:{ all -> 0x0031 }
        r4.zzcy = r6;	 Catch:{ all -> 0x0031 }
        r4.mComponentName = r5;	 Catch:{ all -> 0x0031 }
        r1 = r4.zzdz;	 Catch:{ all -> 0x0031 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0031 }
    L_0x001d:
        r2 = r1.hasNext();	 Catch:{ all -> 0x0031 }
        if (r2 == 0) goto L_0x002d;	 Catch:{ all -> 0x0031 }
    L_0x0023:
        r2 = r1.next();	 Catch:{ all -> 0x0031 }
        r2 = (android.content.ServiceConnection) r2;	 Catch:{ all -> 0x0031 }
        r2.onServiceConnected(r5, r6);	 Catch:{ all -> 0x0031 }
        goto L_0x001d;	 Catch:{ all -> 0x0031 }
    L_0x002d:
        r4.mState = r3;	 Catch:{ all -> 0x0031 }
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        return;	 Catch:{ all -> 0x0031 }
    L_0x0031:
        r5 = move-exception;	 Catch:{ all -> 0x0031 }
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzf.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
    }

    public final void onServiceDisconnected(android.content.ComponentName r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:13:0x0036 in {6, 9, 12} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r4.zzec;
        r0 = r0.zzdu;
        monitor-enter(r0);
        r1 = r4.zzec;	 Catch:{ all -> 0x0033 }
        r1 = r1.mHandler;	 Catch:{ all -> 0x0033 }
        r2 = 1;	 Catch:{ all -> 0x0033 }
        r3 = r4.zzeb;	 Catch:{ all -> 0x0033 }
        r1.removeMessages(r2, r3);	 Catch:{ all -> 0x0033 }
        r1 = 0;	 Catch:{ all -> 0x0033 }
        r4.zzcy = r1;	 Catch:{ all -> 0x0033 }
        r4.mComponentName = r5;	 Catch:{ all -> 0x0033 }
        r1 = r4.zzdz;	 Catch:{ all -> 0x0033 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0033 }
    L_0x001e:
        r2 = r1.hasNext();	 Catch:{ all -> 0x0033 }
        if (r2 == 0) goto L_0x002e;	 Catch:{ all -> 0x0033 }
    L_0x0024:
        r2 = r1.next();	 Catch:{ all -> 0x0033 }
        r2 = (android.content.ServiceConnection) r2;	 Catch:{ all -> 0x0033 }
        r2.onServiceDisconnected(r5);	 Catch:{ all -> 0x0033 }
        goto L_0x001e;	 Catch:{ all -> 0x0033 }
    L_0x002e:
        r5 = 2;	 Catch:{ all -> 0x0033 }
        r4.mState = r5;	 Catch:{ all -> 0x0033 }
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        return;	 Catch:{ all -> 0x0033 }
    L_0x0033:
        r5 = move-exception;	 Catch:{ all -> 0x0033 }
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzf.onServiceDisconnected(android.content.ComponentName):void");
    }

    public final void zze(String str) {
        this.mState = 3;
        this.zzea = this.zzec.zzdw.zza(this.zzec.zzdv, str, this.zzeb.zzb(this.zzec.zzdv), this, this.zzeb.zzq());
        if (this.zzea != null) {
            this.zzec.mHandler.sendMessageDelayed(this.zzec.mHandler.obtainMessage(1, this.zzeb), this.zzec.zzdy);
            return;
        }
        this.mState = 2;
        try {
            this.zzec.zzdw.unbindService(this.zzec.zzdv, this);
        } catch (IllegalArgumentException e) {
        }
    }

    public final void zzf(String str) {
        this.zzec.mHandler.removeMessages(1, this.zzeb);
        this.zzec.zzdw.unbindService(this.zzec.zzdv, this);
        this.zzea = null;
        this.mState = 2;
    }

    public final void zza(ServiceConnection serviceConnection, String str) {
        this.zzec.zzdw;
        this.zzec.zzdv;
        this.zzeb.zzb(this.zzec.zzdv);
        this.zzdz.add(serviceConnection);
    }

    public final void zzb(ServiceConnection serviceConnection, String str) {
        this.zzec.zzdw;
        this.zzec.zzdv;
        this.zzdz.remove(serviceConnection);
    }

    public final boolean isBound() {
        return this.zzea;
    }

    public final int getState() {
        return this.mState;
    }

    public final boolean zza(ServiceConnection serviceConnection) {
        return this.zzdz.contains(serviceConnection);
    }

    public final boolean zzr() {
        return this.zzdz.isEmpty();
    }

    public final IBinder getBinder() {
        return this.zzcy;
    }

    public final ComponentName getComponentName() {
        return this.mComponentName;
    }
}
