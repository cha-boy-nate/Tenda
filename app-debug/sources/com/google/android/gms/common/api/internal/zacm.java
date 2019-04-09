package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;
import javax.annotation.concurrent.GuardedBy;

public final class zacm<R extends Result> extends TransformedResult<R> implements ResultCallback<R> {
    private final Object zadn = new Object();
    private final WeakReference<GoogleApiClient> zadp;
    private ResultTransform<? super R, ? extends Result> zakn = null;
    private zacm<? extends Result> zako = null;
    private volatile ResultCallbacks<? super R> zakp = null;
    private PendingResult<R> zakq = null;
    private Status zakr = null;
    private final zaco zaks;
    private boolean zakt = false;

    public zacm(WeakReference<GoogleApiClient> weakReference) {
        Preconditions.checkNotNull(weakReference, "GoogleApiClient reference must not be null");
        this.zadp = weakReference;
        GoogleApiClient googleApiClient = (GoogleApiClient) this.zadp.get();
        this.zaks = new zaco(this, googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
    }

    @NonNull
    public final <S extends Result> TransformedResult<S> then(@NonNull ResultTransform<? super R, ? extends S> resultTransform) {
        synchronized (this.zadn) {
            boolean z = true;
            Preconditions.checkState(this.zakn == null, "Cannot call then() twice.");
            if (this.zakp != null) {
                z = false;
            }
            Preconditions.checkState(z, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zakn = resultTransform;
            resultTransform = new zacm(this.zadp);
            this.zako = resultTransform;
            zabu();
        }
        return resultTransform;
    }

    public final void andFinally(@NonNull ResultCallbacks<? super R> resultCallbacks) {
        synchronized (this.zadn) {
            boolean z = true;
            Preconditions.checkState(this.zakp == null, "Cannot call andFinally() twice.");
            if (this.zakn != null) {
                z = false;
            }
            Preconditions.checkState(z, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zakp = resultCallbacks;
            zabu();
        }
    }

    public final void onResult(R r) {
        synchronized (this.zadn) {
            if (!r.getStatus().isSuccess()) {
                zad(r.getStatus());
                zab(r);
            } else if (this.zakn != null) {
                zacc.zabb().submit(new zacn(this, r));
            } else if (zabw()) {
                this.zakp.onSuccess(r);
            }
        }
    }

    public final void zaa(PendingResult<?> pendingResult) {
        synchronized (this.zadn) {
            this.zakq = pendingResult;
            zabu();
        }
    }

    @GuardedBy("mSyncToken")
    private final void zabu() {
        if (this.zakn != null || this.zakp != null) {
            GoogleApiClient googleApiClient = (GoogleApiClient) this.zadp.get();
            if (!(this.zakt || this.zakn == null || googleApiClient == null)) {
                googleApiClient.zaa(this);
                this.zakt = true;
            }
            Status status = this.zakr;
            if (status != null) {
                zae(status);
                return;
            }
            PendingResult pendingResult = this.zakq;
            if (pendingResult != null) {
                pendingResult.setResultCallback(this);
            }
        }
    }

    private final void zad(Status status) {
        synchronized (this.zadn) {
            this.zakr = status;
            zae(this.zakr);
        }
    }

    private final void zae(Status status) {
        synchronized (this.zadn) {
            if (this.zakn != null) {
                status = this.zakn.onFailure(status);
                Preconditions.checkNotNull(status, "onFailure must not return null");
                this.zako.zad(status);
            } else if (zabw()) {
                this.zakp.onFailure(status);
            }
        }
    }

    final void zabv() {
        this.zakp = null;
    }

    @GuardedBy("mSyncToken")
    private final boolean zabw() {
        return (this.zakp == null || ((GoogleApiClient) this.zadp.get()) == null) ? false : true;
    }

    private static void zab(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (Throwable e) {
                result = String.valueOf(result);
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(result).length() + 18);
                stringBuilder.append("Unable to release ");
                stringBuilder.append(result);
                Log.w("TransformedResultImpl", stringBuilder.toString(), e);
            }
        }
    }
}
