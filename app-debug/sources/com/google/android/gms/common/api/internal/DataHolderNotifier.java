package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.internal.ListenerHolder.Notifier;
import com.google.android.gms.common.data.DataHolder;

@KeepForSdk
public abstract class DataHolderNotifier<L> implements Notifier<L> {
    private final DataHolder mDataHolder;

    @KeepForSdk
    protected DataHolderNotifier(DataHolder dataHolder) {
        this.mDataHolder = dataHolder;
    }

    @KeepForSdk
    protected abstract void notifyListener(L l, DataHolder dataHolder);

    @KeepForSdk
    public final void notifyListener(L l) {
        notifyListener(l, this.mDataHolder);
    }

    @KeepForSdk
    public void onNotifyListenerFailed() {
        DataHolder dataHolder = this.mDataHolder;
        if (dataHolder != null) {
            dataHolder.close();
        }
    }
}
