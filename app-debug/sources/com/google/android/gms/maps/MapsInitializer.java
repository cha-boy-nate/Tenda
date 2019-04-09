package com.google.android.gms.maps;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.maps.internal.zzbz;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import javax.annotation.concurrent.GuardedBy;

public final class MapsInitializer {
    @GuardedBy("MapsInitializer.class")
    private static boolean zzbm = false;

    public static synchronized int initialize(Context context) {
        synchronized (MapsInitializer.class) {
            Preconditions.checkNotNull(context, "Context is null");
            if (zzbm) {
                return 0;
            }
            try {
                context = zzbz.zza(context);
                try {
                    CameraUpdateFactory.zza(context.zze());
                    BitmapDescriptorFactory.zza(context.zzf());
                    zzbm = true;
                    return 0;
                } catch (Context context2) {
                    throw new RuntimeRemoteException(context2);
                }
            } catch (Context context22) {
                return context22.errorCode;
            }
        }
    }

    private MapsInitializer() {
    }
}
