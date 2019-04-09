package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager.zaa;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zad<T> extends zac {
    protected final TaskCompletionSource<T> zacm;

    public zad(int i, TaskCompletionSource<T> taskCompletionSource) {
        super(i);
        this.zacm = taskCompletionSource;
    }

    protected abstract void zad(zaa<?> zaa) throws RemoteException;

    public void zaa(@NonNull Status status) {
        this.zacm.trySetException(new ApiException(status));
    }

    public void zaa(@NonNull RuntimeException runtimeException) {
        this.zacm.trySetException(runtimeException);
    }

    public void zaa(@NonNull zaab zaab, boolean z) {
    }

    public final void zaa(zaa<?> zaa) throws DeadObjectException {
        try {
            zad(zaa);
        } catch (zaa<?> zaa2) {
            zaa(zab.zaa((RemoteException) zaa2));
            throw zaa2;
        } catch (zaa<?> zaa22) {
            zaa(zab.zaa((RemoteException) zaa22));
        } catch (RuntimeException e) {
            zaa(e);
        }
    }
}
