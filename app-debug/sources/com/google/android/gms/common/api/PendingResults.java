package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.OptionalPendingResultImpl;
import com.google.android.gms.common.api.internal.StatusPendingResult;
import com.google.android.gms.common.internal.Preconditions;

@KeepForSdk
public final class PendingResults {

    private static final class zaa<R extends Result> extends BasePendingResult<R> {
        private final R zach;

        public zaa(R r) {
            super(Looper.getMainLooper());
            this.zach = r;
        }

        protected final R createFailedResult(Status status) {
            if (status.getStatusCode() == this.zach.getStatus().getStatusCode()) {
                return this.zach;
            }
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private static final class zab<R extends Result> extends BasePendingResult<R> {
        private final R zaci;

        public zab(GoogleApiClient googleApiClient, R r) {
            super(googleApiClient);
            this.zaci = r;
        }

        protected final R createFailedResult(Status status) {
            return this.zaci;
        }
    }

    private static final class zac<R extends Result> extends BasePendingResult<R> {
        public zac(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        protected final R createFailedResult(Status status) {
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    @KeepForSdk
    public static PendingResult<Status> immediatePendingResult(Status status) {
        Preconditions.checkNotNull(status, "Result must not be null");
        PendingResult statusPendingResult = new StatusPendingResult(Looper.getMainLooper());
        statusPendingResult.setResult(status);
        return statusPendingResult;
    }

    @KeepForSdk
    public static PendingResult<Status> immediatePendingResult(Status status, GoogleApiClient googleApiClient) {
        Preconditions.checkNotNull(status, "Result must not be null");
        PendingResult statusPendingResult = new StatusPendingResult(googleApiClient);
        statusPendingResult.setResult(status);
        return statusPendingResult;
    }

    @KeepForSdk
    public static <R extends Result> PendingResult<R> immediateFailedResult(R r, GoogleApiClient googleApiClient) {
        Preconditions.checkNotNull(r, "Result must not be null");
        Preconditions.checkArgument(r.getStatus().isSuccess() ^ 1, "Status code must not be SUCCESS");
        PendingResult zab = new zab(googleApiClient, r);
        zab.setResult(r);
        return zab;
    }

    @KeepForSdk
    public static <R extends Result> OptionalPendingResult<R> immediatePendingResult(R r) {
        Preconditions.checkNotNull(r, "Result must not be null");
        PendingResult zac = new zac(null);
        zac.setResult(r);
        return new OptionalPendingResultImpl(zac);
    }

    @KeepForSdk
    public static <R extends Result> OptionalPendingResult<R> immediatePendingResult(R r, GoogleApiClient googleApiClient) {
        Preconditions.checkNotNull(r, "Result must not be null");
        PendingResult zac = new zac(googleApiClient);
        zac.setResult(r);
        return new OptionalPendingResultImpl(zac);
    }

    public static PendingResult<Status> canceledPendingResult() {
        PendingResult<Status> statusPendingResult = new StatusPendingResult(Looper.getMainLooper());
        statusPendingResult.cancel();
        return statusPendingResult;
    }

    public static <R extends Result> PendingResult<R> canceledPendingResult(R r) {
        Preconditions.checkNotNull(r, "Result must not be null");
        Preconditions.checkArgument(r.getStatus().getStatusCode() == 16, "Status code must be CommonStatusCodes.CANCELED");
        PendingResult<R> zaa = new zaa(r);
        zaa.cancel();
        return zaa;
    }

    @KeepForSdk
    private PendingResults() {
    }
}
