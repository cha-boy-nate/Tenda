package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.GoogleApiManager.zaa;

public final class zae<A extends ApiMethodImpl<? extends Result, AnyClient>> extends zab {
    private final A zacn;

    public zae(int i, A a) {
        super(i);
        this.zacn = a;
    }

    public final void zaa(zaa<?> zaa) throws DeadObjectException {
        try {
            this.zacn.run(zaa.zaab());
        } catch (RuntimeException e) {
            zaa(e);
        }
    }

    public final void zaa(@NonNull Status status) {
        this.zacn.setFailedResult(status);
    }

    public final void zaa(@NonNull RuntimeException runtimeException) {
        String simpleName = runtimeException.getClass().getSimpleName();
        runtimeException = runtimeException.getLocalizedMessage();
        StringBuilder stringBuilder = new StringBuilder((String.valueOf(simpleName).length() + 2) + String.valueOf(runtimeException).length());
        stringBuilder.append(simpleName);
        stringBuilder.append(": ");
        stringBuilder.append(runtimeException);
        this.zacn.setFailedResult(new Status(10, stringBuilder.toString()));
    }

    public final void zaa(@NonNull zaab zaab, boolean z) {
        zaab.zaa(this.zacn, z);
    }
}
