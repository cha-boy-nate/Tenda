package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.annotation.concurrent.GuardedBy;

final class zzr<TResult> {
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private Queue<zzq<TResult>> zzt;
    @GuardedBy("mLock")
    private boolean zzu;

    zzr() {
    }

    public final void zza(@android.support.annotation.NonNull com.google.android.gms.tasks.Task<TResult> r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:30:0x002f in {7, 17, 19, 23, 26, 29} preds:[]
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
        r2 = this;
        r0 = r2.mLock;
        monitor-enter(r0);
        r1 = r2.zzt;	 Catch:{ all -> 0x002c }
        if (r1 == 0) goto L_0x002a;	 Catch:{ all -> 0x002c }
    L_0x0007:
        r1 = r2.zzu;	 Catch:{ all -> 0x002c }
        if (r1 == 0) goto L_0x000c;	 Catch:{ all -> 0x002c }
    L_0x000b:
        goto L_0x002a;	 Catch:{ all -> 0x002c }
    L_0x000c:
        r1 = 1;	 Catch:{ all -> 0x002c }
        r2.zzu = r1;	 Catch:{ all -> 0x002c }
        monitor-exit(r0);	 Catch:{ all -> 0x002c }
    L_0x0010:
        r1 = r2.mLock;
        monitor-enter(r1);
        r0 = r2.zzt;	 Catch:{ all -> 0x0027 }
        r0 = r0.poll();	 Catch:{ all -> 0x0027 }
        r0 = (com.google.android.gms.tasks.zzq) r0;	 Catch:{ all -> 0x0027 }
        if (r0 != 0) goto L_0x0022;	 Catch:{ all -> 0x0027 }
    L_0x001d:
        r3 = 0;	 Catch:{ all -> 0x0027 }
        r2.zzu = r3;	 Catch:{ all -> 0x0027 }
        monitor-exit(r1);	 Catch:{ all -> 0x0027 }
        return;	 Catch:{ all -> 0x0027 }
    L_0x0022:
        monitor-exit(r1);	 Catch:{ all -> 0x0027 }
        r0.onComplete(r3);
        goto L_0x0010;
    L_0x0027:
        r3 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0027 }
        throw r3;
    L_0x002a:
        monitor-exit(r0);	 Catch:{ all -> 0x002c }
        return;	 Catch:{ all -> 0x002c }
    L_0x002c:
        r3 = move-exception;	 Catch:{ all -> 0x002c }
        monitor-exit(r0);	 Catch:{ all -> 0x002c }
        throw r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tasks.zzr.zza(com.google.android.gms.tasks.Task):void");
    }

    public final void zza(@NonNull zzq<TResult> zzq) {
        synchronized (this.mLock) {
            if (this.zzt == null) {
                this.zzt = new ArrayDeque();
            }
            this.zzt.add(zzq);
        }
    }
}
