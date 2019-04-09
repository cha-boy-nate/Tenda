package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import javax.annotation.concurrent.GuardedBy;

final class zzm<TResult> implements zzq<TResult> {
    private final Object mLock = new Object();
    private final Executor zzd;
    @GuardedBy("mLock")
    private OnSuccessListener<? super TResult> zzp;

    public zzm(@NonNull Executor executor, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        this.zzd = executor;
        this.zzp = onSuccessListener;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onComplete(@android.support.annotation.NonNull com.google.android.gms.tasks.Task<TResult> r3) {
        /*
        r2 = this;
        r0 = r3.isSuccessful();
        if (r0 == 0) goto L_0x001e;
    L_0x0006:
        r0 = r2.mLock;
        monitor-enter(r0);
        r1 = r2.zzp;	 Catch:{ all -> 0x001b }
        if (r1 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return;
    L_0x000f:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        r0 = r2.zzd;
        r1 = new com.google.android.gms.tasks.zzn;
        r1.<init>(r2, r3);
        r0.execute(r1);
        goto L_0x001e;
    L_0x001b:
        r3 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        throw r3;
    L_0x001e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tasks.zzm.onComplete(com.google.android.gms.tasks.Task):void");
    }

    public final void cancel() {
        synchronized (this.mLock) {
            this.zzp = null;
        }
    }
}
