package com.google.android.gms.tasks;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import javax.annotation.concurrent.GuardedBy;

final class zzu<TResult> extends Task<TResult> {
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private TResult zzaa;
    @GuardedBy("mLock")
    private Exception zzab;
    private final zzr<TResult> zzx = new zzr();
    @GuardedBy("mLock")
    private boolean zzy;
    private volatile boolean zzz;

    private static class zza extends LifecycleCallback {
        private final List<WeakReference<zzq<?>>> zzac = new ArrayList();

        public static zza zza(Activity activity) {
            activity = LifecycleCallback.getFragment(activity);
            zza zza = (zza) activity.getCallbackOrNull("TaskOnStopCallback", zza.class);
            if (zza == null) {
                zza = new zza(activity);
            }
            return zza;
        }

        @android.support.annotation.MainThread
        public void onStop() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:16:0x002b in {8, 9, 12, 15} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
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
            r0 = r3.zzac;
            monitor-enter(r0);
            r1 = r3.zzac;	 Catch:{ all -> 0x0028 }
            r1 = r1.iterator();	 Catch:{ all -> 0x0028 }
        L_0x0009:
            r2 = r1.hasNext();	 Catch:{ all -> 0x0028 }
            if (r2 == 0) goto L_0x0021;	 Catch:{ all -> 0x0028 }
        L_0x000f:
            r2 = r1.next();	 Catch:{ all -> 0x0028 }
            r2 = (java.lang.ref.WeakReference) r2;	 Catch:{ all -> 0x0028 }
            r2 = r2.get();	 Catch:{ all -> 0x0028 }
            r2 = (com.google.android.gms.tasks.zzq) r2;	 Catch:{ all -> 0x0028 }
            if (r2 == 0) goto L_0x0020;	 Catch:{ all -> 0x0028 }
        L_0x001d:
            r2.cancel();	 Catch:{ all -> 0x0028 }
        L_0x0020:
            goto L_0x0009;	 Catch:{ all -> 0x0028 }
        L_0x0021:
            r1 = r3.zzac;	 Catch:{ all -> 0x0028 }
            r1.clear();	 Catch:{ all -> 0x0028 }
            monitor-exit(r0);	 Catch:{ all -> 0x0028 }
            return;	 Catch:{ all -> 0x0028 }
        L_0x0028:
            r1 = move-exception;	 Catch:{ all -> 0x0028 }
            monitor-exit(r0);	 Catch:{ all -> 0x0028 }
            throw r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tasks.zzu.zza.onStop():void");
        }

        private zza(LifecycleFragment lifecycleFragment) {
            super(lifecycleFragment);
            this.mLifecycleFragment.addCallback("TaskOnStopCallback", this);
        }

        public final <T> void zzb(zzq<T> zzq) {
            synchronized (this.zzac) {
                this.zzac.add(new WeakReference(zzq));
            }
        }
    }

    zzu() {
    }

    public final boolean isComplete() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzy;
        }
        return z;
    }

    public final boolean isCanceled() {
        return this.zzz;
    }

    public final boolean isSuccessful() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzy && !this.zzz && this.zzab == null;
        }
        return z;
    }

    public final TResult getResult() {
        TResult tResult;
        synchronized (this.mLock) {
            zzb();
            zzd();
            if (this.zzab == null) {
                tResult = this.zzaa;
            } else {
                throw new RuntimeExecutionException(this.zzab);
            }
        }
        return tResult;
    }

    public final <X extends Throwable> TResult getResult(@NonNull Class<X> cls) throws Throwable {
        synchronized (this.mLock) {
            zzb();
            zzd();
            if (cls.isInstance(this.zzab)) {
                throw ((Throwable) cls.cast(this.zzab));
            } else if (this.zzab == null) {
                cls = this.zzaa;
            } else {
                throw new RuntimeExecutionException(this.zzab);
            }
        }
        return cls;
    }

    @Nullable
    public final Exception getException() {
        Exception exception;
        synchronized (this.mLock) {
            exception = this.zzab;
        }
        return exception;
    }

    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        return addOnSuccessListener(TaskExecutors.MAIN_THREAD, (OnSuccessListener) onSuccessListener);
    }

    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        this.zzx.zza(new zzm(executor, onSuccessListener));
        zze();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        zzq zzm = new zzm(TaskExecutors.MAIN_THREAD, onSuccessListener);
        this.zzx.zza(zzm);
        zza.zza(activity).zzb(zzm);
        zze();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return addOnFailureListener(TaskExecutors.MAIN_THREAD, onFailureListener);
    }

    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        this.zzx.zza(new zzk(executor, onFailureListener));
        zze();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        zzq zzk = new zzk(TaskExecutors.MAIN_THREAD, onFailureListener);
        this.zzx.zza(zzk);
        zza.zza(activity).zzb(zzk);
        zze();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull OnCompleteListener<TResult> onCompleteListener) {
        return addOnCompleteListener(TaskExecutors.MAIN_THREAD, (OnCompleteListener) onCompleteListener);
    }

    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull Executor executor, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        this.zzx.zza(new zzi(executor, onCompleteListener));
        zze();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        zzq zzi = new zzi(TaskExecutors.MAIN_THREAD, onCompleteListener);
        this.zzx.zza(zzi);
        zza.zza(activity).zzb(zzi);
        zze();
        return this;
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Continuation<TResult, TContinuationResult> continuation) {
        return continueWith(TaskExecutors.MAIN_THREAD, continuation);
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation) {
        Task zzu = new zzu();
        this.zzx.zza(new zzc(executor, continuation, zzu));
        zze();
        return zzu;
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        return continueWithTask(TaskExecutors.MAIN_THREAD, continuation);
    }

    @NonNull
    public final Task<TResult> addOnCanceledListener(@NonNull OnCanceledListener onCanceledListener) {
        return addOnCanceledListener(TaskExecutors.MAIN_THREAD, onCanceledListener);
    }

    @NonNull
    public final Task<TResult> addOnCanceledListener(@NonNull Executor executor, @NonNull OnCanceledListener onCanceledListener) {
        this.zzx.zza(new zzg(executor, onCanceledListener));
        zze();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnCanceledListener(@NonNull Activity activity, @NonNull OnCanceledListener onCanceledListener) {
        zzq zzg = new zzg(TaskExecutors.MAIN_THREAD, onCanceledListener);
        this.zzx.zza(zzg);
        zza.zza(activity).zzb(zzg);
        zze();
        return this;
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        Task zzu = new zzu();
        this.zzx.zza(new zze(executor, continuation, zzu));
        zze();
        return zzu;
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> onSuccessTask(Executor executor, SuccessContinuation<TResult, TContinuationResult> successContinuation) {
        Task zzu = new zzu();
        this.zzx.zza(new zzo(executor, successContinuation, zzu));
        zze();
        return zzu;
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> onSuccessTask(@NonNull SuccessContinuation<TResult, TContinuationResult> successContinuation) {
        return onSuccessTask(TaskExecutors.MAIN_THREAD, successContinuation);
    }

    public final void setResult(TResult tResult) {
        synchronized (this.mLock) {
            zzc();
            this.zzy = true;
            this.zzaa = tResult;
        }
        this.zzx.zza((Task) this);
    }

    public final boolean trySetResult(TResult tResult) {
        synchronized (this.mLock) {
            if (this.zzy) {
                return null;
            }
            this.zzy = true;
            this.zzaa = tResult;
            this.zzx.zza((Task) this);
            return true;
        }
    }

    public final void setException(@NonNull Exception exception) {
        Preconditions.checkNotNull(exception, "Exception must not be null");
        synchronized (this.mLock) {
            zzc();
            this.zzy = true;
            this.zzab = exception;
        }
        this.zzx.zza((Task) this);
    }

    public final boolean trySetException(@NonNull Exception exception) {
        Preconditions.checkNotNull(exception, "Exception must not be null");
        synchronized (this.mLock) {
            if (this.zzy) {
                return null;
            }
            this.zzy = true;
            this.zzab = exception;
            this.zzx.zza((Task) this);
            return true;
        }
    }

    public final boolean zza() {
        synchronized (this.mLock) {
            if (this.zzy) {
                return false;
            }
            this.zzy = true;
            this.zzz = true;
            this.zzx.zza((Task) this);
            return true;
        }
    }

    @GuardedBy("mLock")
    private final void zzb() {
        Preconditions.checkState(this.zzy, "Task is not yet complete");
    }

    @GuardedBy("mLock")
    private final void zzc() {
        Preconditions.checkState(this.zzy ^ 1, "Task is already complete");
    }

    @GuardedBy("mLock")
    private final void zzd() {
        if (this.zzz) {
            throw new CancellationException("Task is already canceled.");
        }
    }

    private final void zze() {
        synchronized (this.mLock) {
            if (this.zzy) {
                this.zzx.zza((Task) this);
                return;
            }
        }
    }
}
