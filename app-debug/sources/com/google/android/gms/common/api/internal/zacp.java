package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class zacp {
    public static final Status zakw = new Status(8, "The connection to Google Play services was lost");
    private static final BasePendingResult<?>[] zakx = new BasePendingResult[0];
    private final Map<AnyClientKey<?>, Client> zagy;
    @VisibleForTesting
    final Set<BasePendingResult<?>> zaky = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zacs zakz = new zacq(this);

    public zacp(Map<AnyClientKey<?>, Client> map) {
        this.zagy = map;
    }

    final void zab(BasePendingResult<? extends Result> basePendingResult) {
        this.zaky.add(basePendingResult);
        basePendingResult.zaa(this.zakz);
    }

    public final void release() {
        for (PendingResult pendingResult : (BasePendingResult[]) this.zaky.toArray(zakx)) {
            zacs zacs = null;
            pendingResult.zaa(zacs);
            if (pendingResult.zam() != null) {
                pendingResult.setResultCallback(zacs);
                IBinder serviceBrokerBinder = ((Client) this.zagy.get(((ApiMethodImpl) pendingResult).getClientKey())).getServiceBrokerBinder();
                if (pendingResult.isReady()) {
                    pendingResult.zaa(new zacr(pendingResult, zacs, serviceBrokerBinder, zacs));
                } else if (serviceBrokerBinder == null || !serviceBrokerBinder.isBinderAlive()) {
                    pendingResult.zaa(zacs);
                    pendingResult.cancel();
                    zacs.remove(pendingResult.zam().intValue());
                } else {
                    zacs zacr = new zacr(pendingResult, zacs, serviceBrokerBinder, zacs);
                    pendingResult.zaa(zacr);
                    try {
                        serviceBrokerBinder.linkToDeath(zacr, 0);
                    } catch (RemoteException e) {
                        pendingResult.cancel();
                        zacs.remove(pendingResult.zam().intValue());
                    }
                }
                this.zaky.remove(pendingResult);
            } else if (pendingResult.zat()) {
                this.zaky.remove(pendingResult);
            }
        }
    }

    public final void zabx() {
        for (BasePendingResult zab : (BasePendingResult[]) this.zaky.toArray(zakx)) {
            zab.zab(zakw);
        }
    }
}
