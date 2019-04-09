package com.google.android.gms.common.api;

import com.google.android.gms.common.api.internal.BasePendingResult;
import java.util.ArrayList;
import java.util.List;

public final class Batch extends BasePendingResult<BatchResult> {
    private final Object mLock;
    private int zaaz;
    private boolean zaba;
    private boolean zabb;
    private final PendingResult<?>[] zabc;

    public static final class Builder {
        private List<PendingResult<?>> zabe = new ArrayList();
        private GoogleApiClient zabf;

        public Builder(GoogleApiClient googleApiClient) {
            this.zabf = googleApiClient;
        }

        public final <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken(this.zabe.size());
            this.zabe.add(pendingResult);
            return batchResultToken;
        }

        public final Batch build() {
            return new Batch(this.zabe, this.zabf);
        }
    }

    private Batch(List<PendingResult<?>> list, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.mLock = new Object();
        this.zaaz = list.size();
        this.zabc = new PendingResult[this.zaaz];
        if (list.isEmpty() != null) {
            setResult(new BatchResult(Status.RESULT_SUCCESS, this.zabc));
            return;
        }
        for (googleApiClient = null; googleApiClient < list.size(); googleApiClient++) {
            PendingResult pendingResult = (PendingResult) list.get(googleApiClient);
            this.zabc[googleApiClient] = pendingResult;
            pendingResult.addStatusListener(new zaa(this));
        }
    }

    public final void cancel() {
        super.cancel();
        for (PendingResult cancel : this.zabc) {
            cancel.cancel();
        }
    }

    public final BatchResult createFailedResult(Status status) {
        return new BatchResult(status, this.zabc);
    }
}
