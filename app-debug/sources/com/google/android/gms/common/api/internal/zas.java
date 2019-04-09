package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.base.zal;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zad;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

final class zas implements zabs {
    private final Context mContext;
    private final Looper zabj;
    private final zaaw zaed;
    private final zabe zaee;
    private final zabe zaef;
    private final Map<AnyClientKey<?>, zabe> zaeg;
    private final Set<SignInConnectionListener> zaeh = Collections.newSetFromMap(new WeakHashMap());
    private final Client zaei;
    private Bundle zaej;
    private ConnectionResult zaek = null;
    private ConnectionResult zael = null;
    private boolean zaem = false;
    private final Lock zaen;
    @GuardedBy("mLock")
    private int zaeo = 0;

    public static zas zaa(Context context, zaaw zaaw, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, AbstractClientBuilder<? extends zad, SignInOptions> abstractClientBuilder, ArrayList<zaq> arrayList) {
        Map<Api<?>, Boolean> map3 = map2;
        Map arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        Client client = null;
        for (Entry entry : map.entrySet()) {
            Client client2 = (Client) entry.getValue();
            if (client2.providesSignIn()) {
                client = client2;
            }
            if (client2.requiresSignIn()) {
                arrayMap.put((AnyClientKey) entry.getKey(), client2);
            } else {
                arrayMap2.put((AnyClientKey) entry.getKey(), client2);
            }
        }
        Preconditions.checkState(arrayMap.isEmpty() ^ 1, "CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        Map arrayMap3 = new ArrayMap();
        Map arrayMap4 = new ArrayMap();
        for (Api api : map2.keySet()) {
            AnyClientKey clientKey = api.getClientKey();
            if (arrayMap.containsKey(clientKey)) {
                arrayMap3.put(api, (Boolean) map3.get(api));
            } else if (arrayMap2.containsKey(clientKey)) {
                arrayMap4.put(api, (Boolean) map3.get(api));
            } else {
                throw new IllegalStateException("Each API in the isOptionalMap must have a corresponding client in the clients map.");
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = arrayList;
        int size = arrayList4.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList4.get(i);
            i++;
            zaq zaq = (zaq) obj;
            if (arrayMap3.containsKey(zaq.mApi)) {
                arrayList2.add(zaq);
            } else if (arrayMap4.containsKey(zaq.mApi)) {
                arrayList3.add(zaq);
            } else {
                throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the isOptionalMap");
            }
        }
        return new zas(context, zaaw, lock, looper, googleApiAvailabilityLight, arrayMap, arrayMap2, clientSettings, abstractClientBuilder, client, arrayList2, arrayList3, arrayMap3, arrayMap4);
    }

    private zas(Context context, zaaw zaaw, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, Map<AnyClientKey<?>, Client> map2, ClientSettings clientSettings, AbstractClientBuilder<? extends zad, SignInOptions> abstractClientBuilder, Client client, ArrayList<zaq> arrayList, ArrayList<zaq> arrayList2, Map<Api<?>, Boolean> map3, Map<Api<?>, Boolean> map4) {
        this.mContext = context;
        this.zaed = zaaw;
        this.zaen = lock;
        this.zabj = looper;
        this.zaei = client;
        Context context2 = context;
        Lock lock2 = lock;
        Looper looper2 = looper;
        GoogleApiAvailabilityLight googleApiAvailabilityLight2 = googleApiAvailabilityLight;
        zabe zabe = r3;
        zabe zabe2 = new zabe(context2, this.zaed, lock2, looper2, googleApiAvailabilityLight2, map2, null, map4, null, arrayList2, new zau());
        this.zaee = zabe;
        this.zaef = new zabe(context2, this.zaed, lock2, looper2, googleApiAvailabilityLight2, map, clientSettings, map3, abstractClientBuilder, arrayList, new zav());
        Map arrayMap = new ArrayMap();
        for (AnyClientKey put : map2.keySet()) {
            arrayMap.put(put, r0.zaee);
        }
        for (AnyClientKey put2 : map.keySet()) {
            arrayMap.put(put2, r0.zaef);
        }
        r0.zaeg = Collections.unmodifiableMap(arrayMap);
    }

    @GuardedBy("mLock")
    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        if (!zaa((ApiMethodImpl) t)) {
            return this.zaee.enqueue(t);
        }
        if (!zaz()) {
            return this.zaef.enqueue(t);
        }
        t.setFailedResult(new Status(4, null, zaaa()));
        return t;
    }

    @GuardedBy("mLock")
    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        if (!zaa((ApiMethodImpl) t)) {
            return this.zaee.execute(t);
        }
        if (!zaz()) {
            return this.zaef.execute(t);
        }
        t.setFailedResult(new Status(4, null, zaaa()));
        return t;
    }

    @Nullable
    @GuardedBy("mLock")
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        if (!((zabe) this.zaeg.get(api.getClientKey())).equals(this.zaef)) {
            return this.zaee.getConnectionResult(api);
        }
        if (zaz()) {
            return new ConnectionResult(4, zaaa());
        }
        return this.zaef.getConnectionResult(api);
    }

    @GuardedBy("mLock")
    public final void connect() {
        this.zaeo = 2;
        this.zaem = false;
        this.zael = null;
        this.zaek = null;
        this.zaee.connect();
        this.zaef.connect();
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect() {
        throw new UnsupportedOperationException();
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @GuardedBy("mLock")
    public final void disconnect() {
        this.zael = null;
        this.zaek = null;
        this.zaeo = 0;
        this.zaee.disconnect();
        this.zaef.disconnect();
        zay();
    }

    public final boolean isConnected() {
        this.zaen.lock();
        try {
            boolean z = true;
            if (!this.zaee.isConnected() || (!this.zaef.isConnected() && !zaz() && this.zaeo != 1)) {
                z = false;
            }
            this.zaen.unlock();
            return z;
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    public final boolean isConnecting() {
        this.zaen.lock();
        try {
            boolean z = this.zaeo == 2;
            this.zaen.unlock();
            return z;
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        boolean z;
        this.zaen.lock();
        try {
            if ((isConnecting() || isConnected()) && !this.zaef.isConnected()) {
                this.zaeh.add(signInConnectionListener);
                z = true;
                if (this.zaeo == null) {
                    this.zaeo = 1;
                }
                this.zael = null;
                this.zaef.connect();
                return z;
            }
            this.zaen.unlock();
            return null;
        } finally {
            z = this.zaen;
            z.unlock();
        }
    }

    @GuardedBy("mLock")
    public final void zaw() {
        this.zaee.zaw();
        this.zaef.zaw();
    }

    public final void maybeSignOut() {
        this.zaen.lock();
        try {
            boolean isConnecting = isConnecting();
            this.zaef.disconnect();
            this.zael = new ConnectionResult(4);
            if (isConnecting) {
                new zal(this.zabj).post(new zat(this));
            } else {
                zay();
            }
            this.zaen.unlock();
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    @GuardedBy("mLock")
    private final void zax() {
        ConnectionResult connectionResult;
        if (zab(this.zaek)) {
            if (!zab(this.zael)) {
                if (!zaz()) {
                    connectionResult = this.zael;
                    if (connectionResult != null) {
                        if (this.zaeo == 1) {
                            zay();
                            return;
                        }
                        zaa(connectionResult);
                        this.zaee.disconnect();
                        return;
                    }
                }
            }
            switch (this.zaeo) {
                case 1:
                    break;
                case 2:
                    this.zaed.zab(this.zaej);
                    break;
                default:
                    Log.wtf("CompositeGAC", "Attempted to call success callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new AssertionError());
                    break;
            }
            zay();
            this.zaeo = 0;
        } else if (this.zaek == null || !zab(this.zael)) {
            connectionResult = this.zaek;
            if (!(connectionResult == null || this.zael == null)) {
                if (this.zaef.zahr < this.zaee.zahr) {
                    connectionResult = this.zael;
                }
                zaa(connectionResult);
            }
        } else {
            this.zaef.disconnect();
            zaa(this.zaek);
        }
    }

    @GuardedBy("mLock")
    private final void zaa(ConnectionResult connectionResult) {
        switch (this.zaeo) {
            case 1:
                break;
            case 2:
                this.zaed.zac(connectionResult);
                break;
            default:
                Log.wtf("CompositeGAC", "Attempted to call failure callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                break;
        }
        zay();
        this.zaeo = null;
    }

    @GuardedBy("mLock")
    private final void zay() {
        for (SignInConnectionListener onComplete : this.zaeh) {
            onComplete.onComplete();
        }
        this.zaeh.clear();
    }

    @GuardedBy("mLock")
    private final void zaa(int i, boolean z) {
        this.zaed.zab(i, z);
        this.zael = null;
        this.zaek = null;
    }

    @GuardedBy("mLock")
    private final boolean zaz() {
        ConnectionResult connectionResult = this.zael;
        return connectionResult != null && connectionResult.getErrorCode() == 4;
    }

    private final boolean zaa(ApiMethodImpl<? extends Result, ? extends AnyClient> apiMethodImpl) {
        apiMethodImpl = apiMethodImpl.getClientKey();
        Preconditions.checkArgument(this.zaeg.containsKey(apiMethodImpl), "GoogleApiClient is not configured to use the API required for this call.");
        return ((zabe) this.zaeg.get(apiMethodImpl)).equals(this.zaef);
    }

    @Nullable
    private final PendingIntent zaaa() {
        if (this.zaei == null) {
            return null;
        }
        return PendingIntent.getActivity(this.mContext, System.identityHashCode(this.zaed), this.zaei.getSignInIntent(), 134217728);
    }

    private final void zaa(Bundle bundle) {
        Bundle bundle2 = this.zaej;
        if (bundle2 == null) {
            this.zaej = bundle;
            return;
        }
        if (bundle != null) {
            bundle2.putAll(bundle);
        }
    }

    private static boolean zab(ConnectionResult connectionResult) {
        return (connectionResult == null || connectionResult.isSuccess() == null) ? null : true;
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).append("authClient").println(":");
        this.zaef.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
        printWriter.append(str).append("anonClient").println(":");
        this.zaee.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
    }
}
