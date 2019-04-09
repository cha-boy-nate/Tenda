package com.google.android.gms.tasks;

import android.support.annotation.NonNull;

public class TaskCompletionSource<TResult> {
    private final zzu<TResult> zza = new zzu();

    public TaskCompletionSource(@NonNull CancellationToken cancellationToken) {
        cancellationToken.onCanceledRequested(new zzs(this));
    }

    public void setResult(TResult tResult) {
        this.zza.setResult(tResult);
    }

    public boolean trySetResult(TResult tResult) {
        return this.zza.trySetResult(tResult);
    }

    public void setException(@NonNull Exception exception) {
        this.zza.setException(exception);
    }

    public boolean trySetException(@NonNull Exception exception) {
        return this.zza.trySetException(exception);
    }

    @NonNull
    public Task<TResult> getTask() {
        return this.zza;
    }
}
