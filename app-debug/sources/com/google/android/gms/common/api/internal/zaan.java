package com.google.android.gms.common.api.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class zaan extends zaau {
    final /* synthetic */ zaak zagi;
    private final Map<Client, zaam> zagk;

    public zaan(zaak zaak, Map<Client, zaam> map) {
        this.zagi = zaak;
        super(zaak);
        this.zagk = map;
    }

    @WorkerThread
    public final void zaan() {
        GoogleApiAvailabilityCache googleApiAvailabilityCache = new GoogleApiAvailabilityCache(this.zagi.zaex);
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        for (Client client : this.zagk.keySet()) {
            if (!client.requiresGooglePlayServices() || ((zaam) this.zagk.get(client)).zaeb) {
                arrayList2.add(client);
            } else {
                arrayList.add(client);
            }
        }
        int i = -1;
        int i2 = 0;
        if (!arrayList.isEmpty()) {
            ArrayList arrayList3 = (ArrayList) arrayList;
            int size = arrayList3.size();
            while (i2 < size) {
                Object obj = arrayList3.get(i2);
                i2++;
                i = googleApiAvailabilityCache.getClientAvailability(this.zagi.mContext, (Client) obj);
                if (i != 0) {
                    break;
                }
            }
        }
        ArrayList arrayList4 = (ArrayList) arrayList2;
        int size2 = arrayList4.size();
        while (i2 < size2) {
            obj = arrayList4.get(i2);
            i2++;
            i = googleApiAvailabilityCache.getClientAvailability(this.zagi.mContext, (Client) obj);
            if (i == 0) {
                break;
            }
        }
        if (i != 0) {
            this.zagi.zafs.zaa(new zaao(this, this.zagi, new ConnectionResult(i, null)));
            return;
        }
        if (this.zagi.zagc) {
            this.zagi.zaga.connect();
        }
        for (Client client2 : this.zagk.keySet()) {
            ConnectionProgressReportCallbacks connectionProgressReportCallbacks = (ConnectionProgressReportCallbacks) this.zagk.get(client2);
            if (!client2.requiresGooglePlayServices() || googleApiAvailabilityCache.getClientAvailability(this.zagi.mContext, client2) == 0) {
                client2.connect(connectionProgressReportCallbacks);
            } else {
                this.zagi.zafs.zaa(new zaap(this, this.zagi, connectionProgressReportCallbacks));
            }
        }
    }
}
