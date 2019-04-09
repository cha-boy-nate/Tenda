package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zad;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

public final class zabe implements zabs, zar {
    private final Context mContext;
    private final AbstractClientBuilder<? extends zad, SignInOptions> zacd;
    final zaaw zaed;
    private final Lock zaen;
    private final ClientSettings zaes;
    private final Map<Api<?>, Boolean> zaev;
    private final GoogleApiAvailabilityLight zaex;
    final Map<AnyClientKey<?>, Client> zagy;
    private final Condition zahm;
    private final zabg zahn;
    final Map<AnyClientKey<?>, ConnectionResult> zaho = new HashMap();
    private volatile zabd zahp;
    private ConnectionResult zahq = null;
    int zahr;
    final zabt zahs;

    public zabe(Context context, zaaw zaaw, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, AbstractClientBuilder<? extends zad, SignInOptions> abstractClientBuilder, ArrayList<zaq> arrayList, zabt zabt) {
        this.mContext = context;
        this.zaen = lock;
        this.zaex = googleApiAvailabilityLight;
        this.zagy = map;
        this.zaes = clientSettings;
        this.zaev = map2;
        this.zacd = abstractClientBuilder;
        this.zaed = zaaw;
        this.zahs = zabt;
        arrayList = arrayList;
        zaaw size = arrayList.size();
        zaaw = null;
        while (zaaw < size) {
            googleApiAvailabilityLight = arrayList.get(zaaw);
            zaaw++;
            ((zaq) googleApiAvailabilityLight).zaa(this);
        }
        this.zahn = new zabg(this, looper);
        this.zahm = lock.newCondition();
        this.zahp = new zaav(this);
    }

    @GuardedBy("mLock")
    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        t.zau();
        return this.zahp.enqueue(t);
    }

    @GuardedBy("mLock")
    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        t.zau();
        return this.zahp.execute(t);
    }

    @GuardedBy("mLock")
    public final void connect() {
        this.zahp.connect();
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zahm.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        if (isConnected()) {
            return ConnectionResult.RESULT_SUCCESS;
        }
        ConnectionResult connectionResult = this.zahq;
        if (connectionResult != null) {
            return connectionResult;
        }
        return new ConnectionResult(13, null);
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect(long j, TimeUnit timeUnit) {
        connect();
        j = timeUnit.toNanos(j);
        while (isConnecting() != null) {
            if (j <= 0) {
                try {
                    disconnect();
                    return new ConnectionResult(14, null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new ConnectionResult(15, null);
                }
            }
            j = this.zahm.awaitNanos(j);
        }
        if (isConnected() != null) {
            return ConnectionResult.RESULT_SUCCESS;
        }
        j = this.zahq;
        if (j != null) {
            return j;
        }
        return new ConnectionResult(13, null);
    }

    @GuardedBy("mLock")
    public final void disconnect() {
        if (this.zahp.disconnect()) {
            this.zaho.clear();
        }
    }

    @Nullable
    @GuardedBy("mLock")
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        api = api.getClientKey();
        if (this.zagy.containsKey(api)) {
            if (((Client) this.zagy.get(api)).isConnected()) {
                return ConnectionResult.RESULT_SUCCESS;
            }
            if (this.zaho.containsKey(api)) {
                return (ConnectionResult) this.zaho.get(api);
            }
        }
        return null;
    }

    final void zaaz() {
        this.zaen.lock();
        try {
            this.zahp = new zaak(this, this.zaes, this.zaev, this.zaex, this.zacd, this.zaen, this.mContext);
            this.zahp.begin();
            this.zahm.signalAll();
        } finally {
            this.zaen.unlock();
        }
    }

    final void zaba() {
        this.zaen.lock();
        try {
            this.zaed.zaaw();
            this.zahp = new zaah(this);
            this.zahp.begin();
            this.zahm.signalAll();
        } finally {
            this.zaen.unlock();
        }
    }

    final void zaf(ConnectionResult connectionResult) {
        this.zaen.lock();
        try {
            this.zahq = connectionResult;
            this.zahp = new zaav(this);
            this.zahp.begin();
            this.zahm.signalAll();
        } finally {
            this.zaen.unlock();
        }
    }

    public final boolean isConnected() {
        return this.zahp instanceof zaah;
    }

    public final boolean isConnecting() {
        return this.zahp instanceof zaak;
    }

    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        return null;
    }

    public final void maybeSignOut() {
    }

    @GuardedBy("mLock")
    public final void zaw() {
        if (isConnected()) {
            ((zaah) this.zahp).zaam();
        }
    }

    public final void zaa(@NonNull ConnectionResult connectionResult, @NonNull Api<?> api, boolean z) {
        this.zaen.lock();
        try {
            this.zahp.zaa(connectionResult, api, z);
        } finally {
            this.zaen.unlock();
        }
    }

    public final void onConnected(@Nullable Bundle bundle) {
        this.zaen.lock();
        try {
            this.zahp.onConnected(bundle);
        } finally {
            this.zaen.unlock();
        }
    }

    public final void onConnectionSuspended(int i) {
        this.zaen.lock();
        try {
            this.zahp.onConnectionSuspended(i);
        } finally {
            this.zaen.unlock();
        }
    }

    final void zaa(zabf zabf) {
        this.zahn.sendMessage(this.zahn.obtainMessage(1, zabf));
    }

    final void zab(RuntimeException runtimeException) {
        this.zahn.sendMessage(this.zahn.obtainMessage(2, runtimeException));
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String concat = String.valueOf(str).concat("  ");
        printWriter.append(str).append("mState=").println(this.zahp);
        for (Api api : this.zaev.keySet()) {
            printWriter.append(str).append(api.getName()).println(":");
            ((Client) this.zagy.get(api.getClientKey())).dump(concat, fileDescriptor, printWriter, strArr);
        }
    }
}
