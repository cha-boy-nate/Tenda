package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.Preconditions;

final class zam {
    private final int zadg;
    private final ConnectionResult zadh;

    zam(ConnectionResult connectionResult, int i) {
        Preconditions.checkNotNull(connectionResult);
        this.zadh = connectionResult;
        this.zadg = i;
    }

    final int zar() {
        return this.zadg;
    }

    final ConnectionResult getConnectionResult() {
        return this.zadh;
    }
}
