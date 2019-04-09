package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import javax.annotation.concurrent.GuardedBy;

final class zzk<TResult> implements zzq<TResult> {
    private final Object mLock = new Object();
    private final Executor zzd;
    @GuardedBy("mLock")
    private OnFailureListener zzn;

    public zzk(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        this.zzd = executor;
        this.zzn = onFailureListener;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onComplete(@android.support.annotation.NonNull com.google.android.gms.tasks.Task<TResult> r3) {
        /*
        r2 = this;
        r0 = r3.isSuccessful();
        if (r0 != 0) goto L_0x0024;
    L_0x0006:
        r0 = r3.isCanceled();
        if (r0 != 0) goto L_0x0024;
    L_0x000c:
        r0 = r2.mLock;
        monitor-enter(r0);
        r1 = r2.zzn;	 Catch:{ all -> 0x0021 }
        if (r1 != 0) goto L_0x0015;
    L_0x0013:
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
        return;
    L_0x0015:
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
        r0 = r2.zzd;
        r1 = new com.google.android.gms.tasks.zzl;
        r1.<init>(r2, r3);
        r0.execute(r1);
        goto L_0x0024;
    L_0x0021:
        r3 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
        throw r3;
    L_0x0024:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tasks.zzk.onComplete(com.google.android.gms.tasks.Task):void");
    }

    public final void cancel() {
        synchronized (this.mLock) {
            this.zzn = null;
        }
    }
}
