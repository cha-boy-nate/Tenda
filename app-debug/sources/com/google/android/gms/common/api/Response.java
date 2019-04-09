package com.google.android.gms.common.api;

import android.support.annotation.NonNull;

public class Response<T extends Result> {
    private T zzao;

    protected Response(@NonNull T t) {
        this.zzao = t;
    }

    @NonNull
    protected T getResult() {
        return this.zzao;
    }

    public void setResult(@NonNull T t) {
        this.zzao = t;
    }
}
