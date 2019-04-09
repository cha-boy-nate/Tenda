package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResult.StatusListener;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.ICancelToken;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.base.zal;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@KeepName
@KeepForSdk
public abstract class BasePendingResult<R extends Result> extends PendingResult<R> {
    static final ThreadLocal<Boolean> zadm = new zap();
    @KeepName
    private zaa mResultGuardian;
    private Status mStatus;
    private R zaci;
    private final Object zadn;
    private final CallbackHandler<R> zado;
    private final WeakReference<GoogleApiClient> zadp;
    private final CountDownLatch zadq;
    private final ArrayList<StatusListener> zadr;
    private ResultCallback<? super R> zads;
    private final AtomicReference<zacs> zadt;
    private volatile boolean zadu;
    private boolean zadv;
    private boolean zadw;
    private ICancelToken zadx;
    private volatile zacm<R> zady;
    private boolean zadz;

    private final class zaa {
        private final /* synthetic */ BasePendingResult zaea;

        private zaa(BasePendingResult basePendingResult) {
            this.zaea = basePendingResult;
        }

        protected final void finalize() throws Throwable {
            BasePendingResult.zab(this.zaea.zaci);
            super.finalize();
        }
    }

    @VisibleForTesting
    public static class CallbackHandler<R extends Result> extends zal {
        public CallbackHandler() {
            this(Looper.getMainLooper());
        }

        public CallbackHandler(Looper looper) {
            super(looper);
        }

        public final void zaa(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Pair pair = (Pair) message.obj;
                    ResultCallback resultCallback = (ResultCallback) pair.first;
                    Result result = (Result) pair.second;
                    try {
                        resultCallback.onResult(result);
                        return;
                    } catch (RuntimeException e) {
                        BasePendingResult.zab(result);
                        throw e;
                    }
                case 2:
                    ((BasePendingResult) message.obj).zab(Status.RESULT_TIMEOUT);
                    return;
                default:
                    message = message.what;
                    StringBuilder stringBuilder = new StringBuilder(45);
                    stringBuilder.append("Don't know how to handle message: ");
                    stringBuilder.append(message);
                    Log.wtf("BasePendingResult", stringBuilder.toString(), new Exception());
                    return;
            }
        }
    }

    @Deprecated
    BasePendingResult() {
        this.zadn = new Object();
        this.zadq = new CountDownLatch(1);
        this.zadr = new ArrayList();
        this.zadt = new AtomicReference();
        this.zadz = false;
        this.zado = new CallbackHandler(Looper.getMainLooper());
        this.zadp = new WeakReference(null);
    }

    @KeepForSdk
    @NonNull
    protected abstract R createFailedResult(Status status);

    @KeepForSdk
    protected BasePendingResult(GoogleApiClient googleApiClient) {
        this.zadn = new Object();
        this.zadq = new CountDownLatch(1);
        this.zadr = new ArrayList();
        this.zadt = new AtomicReference();
        this.zadz = false;
        this.zado = new CallbackHandler(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
        this.zadp = new WeakReference(googleApiClient);
    }

    @KeepForSdk
    @Deprecated
    protected BasePendingResult(Looper looper) {
        this.zadn = new Object();
        this.zadq = new CountDownLatch(1);
        this.zadr = new ArrayList();
        this.zadt = new AtomicReference();
        this.zadz = false;
        this.zado = new CallbackHandler(looper);
        this.zadp = new WeakReference(null);
    }

    @KeepForSdk
    @VisibleForTesting
    protected BasePendingResult(@NonNull CallbackHandler<R> callbackHandler) {
        this.zadn = new Object();
        this.zadq = new CountDownLatch(1);
        this.zadr = new ArrayList();
        this.zadt = new AtomicReference();
        this.zadz = false;
        this.zado = (CallbackHandler) Preconditions.checkNotNull(callbackHandler, "CallbackHandler must not be null");
        this.zadp = new WeakReference(null);
    }

    @KeepForSdk
    public final boolean isReady() {
        return this.zadq.getCount() == 0;
    }

    public final R await() {
        Preconditions.checkNotMainThread("await must not be called on the UI thread");
        boolean z = true;
        Preconditions.checkState(this.zadu ^ true, "Result has already been consumed");
        if (this.zady != null) {
            z = false;
        }
        Preconditions.checkState(z, "Cannot await if then() has been called.");
        try {
            this.zadq.await();
        } catch (InterruptedException e) {
            zab(Status.RESULT_INTERRUPTED);
        }
        Preconditions.checkState(isReady(), "Result is not ready.");
        return get();
    }

    public final R await(long j, TimeUnit timeUnit) {
        if (j > 0) {
            Preconditions.checkNotMainThread("await must not be called on the UI thread when time is greater than zero.");
        }
        boolean z = true;
        Preconditions.checkState(this.zadu ^ true, "Result has already been consumed.");
        if (this.zady != null) {
            z = false;
        }
        Preconditions.checkState(z, "Cannot await if then() has been called.");
        try {
            if (this.zadq.await(j, timeUnit) == null) {
                zab(Status.RESULT_TIMEOUT);
            }
        } catch (InterruptedException e) {
            zab(Status.RESULT_INTERRUPTED);
        }
        Preconditions.checkState(isReady(), "Result is not ready.");
        return get();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.android.gms.common.annotation.KeepForSdk
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r6) {
        /*
        r5 = this;
        r0 = r5.zadn;
        monitor-enter(r0);
        if (r6 != 0) goto L_0x000a;
    L_0x0005:
        r6 = 0;
        r5.zads = r6;	 Catch:{ all -> 0x003f }
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        return;
    L_0x000a:
        r1 = r5.zadu;	 Catch:{ all -> 0x003f }
        r2 = 1;
        r3 = 0;
        if (r1 != 0) goto L_0x0012;
    L_0x0010:
        r1 = 1;
        goto L_0x0013;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        r4 = "Result has already been consumed.";
        com.google.android.gms.common.internal.Preconditions.checkState(r1, r4);	 Catch:{ all -> 0x003f }
        r1 = r5.zady;	 Catch:{ all -> 0x003f }
        if (r1 != 0) goto L_0x001d;
    L_0x001c:
        goto L_0x001e;
    L_0x001d:
        r2 = 0;
    L_0x001e:
        r1 = "Cannot set callbacks if then() has been called.";
        com.google.android.gms.common.internal.Preconditions.checkState(r2, r1);	 Catch:{ all -> 0x003f }
        r1 = r5.isCanceled();	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x002b;
    L_0x0029:
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        return;
    L_0x002b:
        r1 = r5.isReady();	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x003b;
    L_0x0031:
        r1 = r5.zado;	 Catch:{ all -> 0x003f }
        r2 = r5.get();	 Catch:{ all -> 0x003f }
        r1.zaa(r6, r2);	 Catch:{ all -> 0x003f }
        goto L_0x003d;
    L_0x003b:
        r5.zads = r6;	 Catch:{ all -> 0x003f }
    L_0x003d:
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        return;
    L_0x003f:
        r6 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.setResultCallback(com.google.android.gms.common.api.ResultCallback):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.android.gms.common.annotation.KeepForSdk
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r6, long r7, java.util.concurrent.TimeUnit r9) {
        /*
        r5 = this;
        r0 = r5.zadn;
        monitor-enter(r0);
        if (r6 != 0) goto L_0x000a;
    L_0x0005:
        r6 = 0;
        r5.zads = r6;	 Catch:{ all -> 0x004d }
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        return;
    L_0x000a:
        r1 = r5.zadu;	 Catch:{ all -> 0x004d }
        r2 = 1;
        r3 = 0;
        if (r1 != 0) goto L_0x0012;
    L_0x0010:
        r1 = 1;
        goto L_0x0013;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        r4 = "Result has already been consumed.";
        com.google.android.gms.common.internal.Preconditions.checkState(r1, r4);	 Catch:{ all -> 0x004d }
        r1 = r5.zady;	 Catch:{ all -> 0x004d }
        if (r1 != 0) goto L_0x001d;
    L_0x001c:
        goto L_0x001e;
    L_0x001d:
        r2 = 0;
    L_0x001e:
        r1 = "Cannot set callbacks if then() has been called.";
        com.google.android.gms.common.internal.Preconditions.checkState(r2, r1);	 Catch:{ all -> 0x004d }
        r1 = r5.isCanceled();	 Catch:{ all -> 0x004d }
        if (r1 == 0) goto L_0x002b;
    L_0x0029:
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        return;
    L_0x002b:
        r1 = r5.isReady();	 Catch:{ all -> 0x004d }
        if (r1 == 0) goto L_0x003b;
    L_0x0031:
        r7 = r5.zado;	 Catch:{ all -> 0x004d }
        r8 = r5.get();	 Catch:{ all -> 0x004d }
        r7.zaa(r6, r8);	 Catch:{ all -> 0x004d }
        goto L_0x004b;
    L_0x003b:
        r5.zads = r6;	 Catch:{ all -> 0x004d }
        r6 = r5.zado;	 Catch:{ all -> 0x004d }
        r7 = r9.toMillis(r7);	 Catch:{ all -> 0x004d }
        r9 = 2;
        r9 = r6.obtainMessage(r9, r5);	 Catch:{ all -> 0x004d }
        r6.sendMessageDelayed(r9, r7);	 Catch:{ all -> 0x004d }
    L_0x004b:
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        return;
    L_0x004d:
        r6 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.setResultCallback(com.google.android.gms.common.api.ResultCallback, long, java.util.concurrent.TimeUnit):void");
    }

    public final void addStatusListener(StatusListener statusListener) {
        Preconditions.checkArgument(statusListener != null, "Callback cannot be null.");
        synchronized (this.zadn) {
            if (isReady()) {
                statusListener.onComplete(this.mStatus);
            } else {
                this.zadr.add(statusListener);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.android.gms.common.annotation.KeepForSdk
    public void cancel() {
        /*
        r2 = this;
        r0 = r2.zadn;
        monitor-enter(r0);
        r1 = r2.zadv;	 Catch:{ all -> 0x002c }
        if (r1 != 0) goto L_0x002a;
    L_0x0007:
        r1 = r2.zadu;	 Catch:{ all -> 0x002c }
        if (r1 == 0) goto L_0x000c;
    L_0x000b:
        goto L_0x002a;
    L_0x000c:
        r1 = r2.zadx;	 Catch:{ all -> 0x002c }
        if (r1 == 0) goto L_0x0017;
    L_0x0010:
        r1 = r2.zadx;	 Catch:{ RemoteException -> 0x0016 }
        r1.cancel();	 Catch:{ RemoteException -> 0x0016 }
        goto L_0x0017;
    L_0x0016:
        r1 = move-exception;
    L_0x0017:
        r1 = r2.zaci;	 Catch:{ all -> 0x002c }
        zab(r1);	 Catch:{ all -> 0x002c }
        r1 = 1;
        r2.zadv = r1;	 Catch:{ all -> 0x002c }
        r1 = com.google.android.gms.common.api.Status.RESULT_CANCELED;	 Catch:{ all -> 0x002c }
        r1 = r2.createFailedResult(r1);	 Catch:{ all -> 0x002c }
        r2.zaa(r1);	 Catch:{ all -> 0x002c }
        monitor-exit(r0);	 Catch:{ all -> 0x002c }
        return;
    L_0x002a:
        monitor-exit(r0);	 Catch:{ all -> 0x002c }
        return;
    L_0x002c:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002c }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.cancel():void");
    }

    public final boolean zat() {
        boolean isCanceled;
        synchronized (this.zadn) {
            if (((GoogleApiClient) this.zadp.get()) == null || !this.zadz) {
                cancel();
            }
            isCanceled = isCanceled();
        }
        return isCanceled;
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zadn) {
            z = this.zadv;
        }
        return z;
    }

    public <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> resultTransform) {
        Preconditions.checkState(this.zadu ^ true, "Result has already been consumed.");
        synchronized (this.zadn) {
            boolean z = false;
            Preconditions.checkState(this.zady == null, "Cannot call then() twice.");
            Preconditions.checkState(this.zads == null, "Cannot call then() if callbacks are set.");
            if (!this.zadv) {
                z = true;
            }
            Preconditions.checkState(z, "Cannot call then() if result was canceled.");
            this.zadz = true;
            this.zady = new zacm(this.zadp);
            resultTransform = this.zady.then(resultTransform);
            if (isReady()) {
                this.zado.zaa(this.zady, get());
            } else {
                this.zads = this.zady;
            }
        }
        return resultTransform;
    }

    @KeepForSdk
    public final void setResult(R r) {
        synchronized (this.zadn) {
            if (this.zadw || this.zadv) {
                zab((Result) r);
                return;
            }
            boolean isReady = isReady();
            boolean z = true;
            Preconditions.checkState(!isReady(), "Results have already been set");
            if (this.zadu) {
                z = false;
            }
            Preconditions.checkState(z, "Result has already been consumed");
            zaa((Result) r);
        }
    }

    public final void zab(Status status) {
        synchronized (this.zadn) {
            if (!isReady()) {
                setResult(createFailedResult(status));
                this.zadw = true;
            }
        }
    }

    public final void zaa(zacs zacs) {
        this.zadt.set(zacs);
    }

    public final Integer zam() {
        return null;
    }

    @KeepForSdk
    protected final void setCancelToken(ICancelToken iCancelToken) {
        synchronized (this.zadn) {
            this.zadx = iCancelToken;
        }
    }

    public final void zau() {
        boolean z;
        if (!this.zadz) {
            if (!((Boolean) zadm.get()).booleanValue()) {
                z = false;
                this.zadz = z;
            }
        }
        z = true;
        this.zadz = z;
    }

    private final R get() {
        R r;
        synchronized (this.zadn) {
            Preconditions.checkState(!this.zadu, "Result has already been consumed.");
            Preconditions.checkState(isReady(), "Result is not ready.");
            r = this.zaci;
            this.zaci = null;
            this.zads = null;
            this.zadu = true;
        }
        zacs zacs = (zacs) this.zadt.getAndSet(null);
        if (zacs != null) {
            zacs.zac(this);
        }
        return r;
    }

    private final void zaa(R r) {
        this.zaci = r;
        this.zadx = null;
        this.zadq.countDown();
        this.mStatus = this.zaci.getStatus();
        if (this.zadv) {
            this.zads = null;
        } else if (this.zads != null) {
            this.zado.removeMessages(2);
            this.zado.zaa(this.zads, get());
        } else if (this.zaci instanceof Releasable) {
            this.mResultGuardian = new zaa();
        }
        ArrayList arrayList = (ArrayList) this.zadr;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((StatusListener) obj).onComplete(this.mStatus);
        }
        this.zadr.clear();
    }

    public static void zab(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (Throwable e) {
                result = String.valueOf(result);
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(result).length() + 18);
                stringBuilder.append("Unable to release ");
                stringBuilder.append(result);
                Log.w("BasePendingResult", stringBuilder.toString(), e);
            }
        }
    }
}
