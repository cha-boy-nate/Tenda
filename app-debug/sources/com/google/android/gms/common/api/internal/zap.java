package com.google.android.gms.common.api.internal;

final class zap extends ThreadLocal<Boolean> {
    zap() {
    }

    protected final /* synthetic */ Object initialValue() {
        return Boolean.valueOf(false);
    }
}
