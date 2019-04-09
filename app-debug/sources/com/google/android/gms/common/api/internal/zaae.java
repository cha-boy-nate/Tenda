package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.support.v4.util.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.Preconditions;

public class zaae extends zal {
    private GoogleApiManager zabm;
    private final ArraySet<zai<?>> zafo = new ArraySet();

    public static void zaa(Activity activity, GoogleApiManager googleApiManager, zai<?> zai) {
        activity = LifecycleCallback.getFragment(activity);
        zaae zaae = (zaae) activity.getCallbackOrNull("ConnectionlessLifecycleHelper", zaae.class);
        if (zaae == null) {
            zaae = new zaae(activity);
        }
        zaae.zabm = googleApiManager;
        Preconditions.checkNotNull(zai, "ApiKey cannot be null");
        zaae.zafo.add(zai);
        googleApiManager.zaa(zaae);
    }

    private zaae(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment);
        this.mLifecycleFragment.addCallback("ConnectionlessLifecycleHelper", this);
    }

    public void onStart() {
        super.onStart();
        zaak();
    }

    public void onResume() {
        super.onResume();
        zaak();
    }

    public void onStop() {
        super.onStop();
        this.zabm.zab(this);
    }

    protected final void zaa(ConnectionResult connectionResult, int i) {
        this.zabm.zaa(connectionResult, i);
    }

    protected final void zao() {
        this.zabm.zao();
    }

    final ArraySet<zai<?>> zaaj() {
        return this.zafo;
    }

    private final void zaak() {
        if (!this.zafo.isEmpty()) {
            this.zabm.zaa(this);
        }
    }
}
