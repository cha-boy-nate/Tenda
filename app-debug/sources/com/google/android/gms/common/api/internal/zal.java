package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.concurrent.atomic.AtomicReference;

public abstract class zal extends LifecycleCallback implements OnCancelListener {
    protected volatile boolean mStarted;
    protected final GoogleApiAvailability zacc;
    protected final AtomicReference<zam> zade;
    private final Handler zadf;

    protected zal(LifecycleFragment lifecycleFragment) {
        this(lifecycleFragment, GoogleApiAvailability.getInstance());
    }

    protected abstract void zaa(ConnectionResult connectionResult, int i);

    protected abstract void zao();

    @VisibleForTesting
    private zal(LifecycleFragment lifecycleFragment, GoogleApiAvailability googleApiAvailability) {
        super(lifecycleFragment);
        this.zade = new AtomicReference(null);
        this.zadf = new com.google.android.gms.internal.base.zal(Looper.getMainLooper());
        this.zacc = googleApiAvailability;
    }

    public void onCancel(DialogInterface dialogInterface) {
        zaa(new ConnectionResult(13, null), zaa((zam) this.zade.get()));
        zaq();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            Object zam;
            AtomicReference atomicReference = this.zade;
            if (bundle.getBoolean("resolving_error", false)) {
                zam = new zam(new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution")), bundle.getInt("failed_client_id", -1));
            } else {
                zam = null;
            }
            atomicReference.set(zam);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        zam zam = (zam) this.zade.get();
        if (zam != null) {
            bundle.putBoolean("resolving_error", true);
            bundle.putInt("failed_client_id", zam.zar());
            bundle.putInt("failed_status", zam.getConnectionResult().getErrorCode());
            bundle.putParcelable("failed_resolution", zam.getConnectionResult().getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r4, int r5, android.content.Intent r6) {
        /*
        r3 = this;
        r0 = r3.zade;
        r0 = r0.get();
        r0 = (com.google.android.gms.common.api.internal.zam) r0;
        r1 = 1;
        r2 = 0;
        switch(r4) {
            case 1: goto L_0x002f;
            case 2: goto L_0x000f;
            default: goto L_0x000e;
        };
    L_0x000e:
        goto L_0x0057;
    L_0x000f:
        r4 = r3.zacc;
        r5 = r3.getActivity();
        r4 = r4.isGooglePlayServicesAvailable(r5);
        if (r4 != 0) goto L_0x001c;
    L_0x001b:
        goto L_0x001d;
    L_0x001c:
        r1 = 0;
    L_0x001d:
        if (r0 != 0) goto L_0x0020;
    L_0x001f:
        return;
    L_0x0020:
        r5 = r0.getConnectionResult();
        r5 = r5.getErrorCode();
        r6 = 18;
        if (r5 != r6) goto L_0x0058;
    L_0x002c:
        if (r4 != r6) goto L_0x0058;
    L_0x002e:
        return;
    L_0x002f:
        r4 = -1;
        if (r5 != r4) goto L_0x0033;
    L_0x0032:
        goto L_0x0058;
    L_0x0033:
        if (r5 != 0) goto L_0x0057;
        r4 = 13;
        if (r6 == 0) goto L_0x0040;
    L_0x003a:
        r5 = "<<ResolutionFailureErrorDetail>>";
        r4 = r6.getIntExtra(r5, r4);
    L_0x0040:
        r5 = new com.google.android.gms.common.api.internal.zam;
        r6 = new com.google.android.gms.common.ConnectionResult;
        r1 = 0;
        r6.<init>(r4, r1);
        r4 = zaa(r0);
        r5.<init>(r6, r4);
        r4 = r3.zade;
        r4.set(r5);
        r0 = r5;
        r1 = 0;
        goto L_0x0058;
    L_0x0057:
        r1 = 0;
    L_0x0058:
        if (r1 == 0) goto L_0x005e;
    L_0x005a:
        r3.zaq();
        return;
    L_0x005e:
        if (r0 == 0) goto L_0x006c;
        r4 = r0.getConnectionResult();
        r5 = r0.zar();
        r3.zaa(r4, r5);
    L_0x006c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zal.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
    }

    protected final void zaq() {
        this.zade.set(null);
        zao();
    }

    public final void zab(ConnectionResult connectionResult, int i) {
        zam zam = new zam(connectionResult, i);
        if (this.zade.compareAndSet(0, zam) != null) {
            this.zadf.post(new zan(this, zam));
        }
    }

    private static int zaa(@Nullable zam zam) {
        if (zam == null) {
            return -1;
        }
        return zam.zar();
    }
}
