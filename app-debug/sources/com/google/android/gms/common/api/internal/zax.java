package com.google.android.gms.common.api.internal;

import android.content.Context;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.ClientSettings.OptionalApiSettings;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zad;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

public final class zax implements zabs {
    private final Looper zabj;
    private final GoogleApiManager zabm;
    private final Lock zaen;
    private final ClientSettings zaes;
    private final Map<AnyClientKey<?>, zaw<?>> zaet = new HashMap();
    private final Map<AnyClientKey<?>, zaw<?>> zaeu = new HashMap();
    private final Map<Api<?>, Boolean> zaev;
    private final zaaw zaew;
    private final GoogleApiAvailabilityLight zaex;
    private final Condition zaey;
    private final boolean zaez;
    private final boolean zafa;
    private final Queue<ApiMethodImpl<?, ?>> zafb = new LinkedList();
    @GuardedBy("mLock")
    private boolean zafc;
    @GuardedBy("mLock")
    private Map<zai<?>, ConnectionResult> zafd;
    @GuardedBy("mLock")
    private Map<zai<?>, ConnectionResult> zafe;
    @GuardedBy("mLock")
    private zaaa zaff;
    @GuardedBy("mLock")
    private ConnectionResult zafg;

    public zax(Context context, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, AbstractClientBuilder<? extends zad, SignInOptions> abstractClientBuilder, ArrayList<zaq> arrayList, zaaw zaaw, boolean z) {
        this.zaen = lock;
        this.zabj = looper;
        this.zaey = lock.newCondition();
        this.zaex = googleApiAvailabilityLight;
        this.zaew = zaaw;
        this.zaev = map2;
        this.zaes = clientSettings;
        this.zaez = z;
        Map hashMap = new HashMap();
        for (Api api : map2.keySet()) {
            hashMap.put(api.getClientKey(), api);
        }
        Map hashMap2 = new HashMap();
        ArrayList arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            zaq zaq = (zaq) obj;
            hashMap2.put(zaq.mApi, zaq);
        }
        boolean z2 = true;
        Object obj2 = null;
        Object obj3 = 1;
        Object obj4 = null;
        for (Entry entry : map.entrySet()) {
            Object obj5;
            Object obj6;
            Object obj7;
            Api api2 = (Api) hashMap.get(entry.getKey());
            Client client = (Client) entry.getValue();
            if (!client.requiresGooglePlayServices()) {
                obj5 = obj2;
                obj6 = obj4;
                obj7 = null;
            } else if (((Boolean) r0.zaev.get(api2)).booleanValue()) {
                obj7 = obj3;
                obj6 = obj4;
                obj5 = 1;
            } else {
                obj7 = obj3;
                obj6 = 1;
                obj5 = 1;
            }
            zaw zaw = r1;
            zaw zaw2 = new zaw(context, api2, looper, client, (zaq) hashMap2.get(api2), clientSettings, abstractClientBuilder);
            r0.zaet.put((AnyClientKey) entry.getKey(), zaw);
            if (client.requiresSignIn()) {
                r0.zaeu.put((AnyClientKey) entry.getKey(), zaw);
            }
            obj4 = obj6;
            obj3 = obj7;
            obj2 = obj5;
        }
        if (obj2 == null || obj3 != null || obj4 != null) {
            z2 = false;
        }
        r0.zafa = z2;
        r0.zabm = GoogleApiManager.zabc();
    }

    private final boolean zaac() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:25:0x004d in {6, 14, 15, 17, 19, 21, 24} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = r3.zaen;
        r0.lock();
        r0 = r3.zafc;	 Catch:{ all -> 0x0046 }
        r1 = 0;	 Catch:{ all -> 0x0046 }
        if (r0 == 0) goto L_0x0040;	 Catch:{ all -> 0x0046 }
    L_0x000a:
        r0 = r3.zaez;	 Catch:{ all -> 0x0046 }
        if (r0 != 0) goto L_0x000f;	 Catch:{ all -> 0x0046 }
    L_0x000e:
        goto L_0x0040;	 Catch:{ all -> 0x0046 }
    L_0x000f:
        r0 = r3.zaeu;	 Catch:{ all -> 0x0046 }
        r0 = r0.keySet();	 Catch:{ all -> 0x0046 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0046 }
    L_0x0019:
        r2 = r0.hasNext();	 Catch:{ all -> 0x0046 }
        if (r2 == 0) goto L_0x0039;	 Catch:{ all -> 0x0046 }
    L_0x001f:
        r2 = r0.next();	 Catch:{ all -> 0x0046 }
        r2 = (com.google.android.gms.common.api.Api.AnyClientKey) r2;	 Catch:{ all -> 0x0046 }
        r2 = r3.zaa(r2);	 Catch:{ all -> 0x0046 }
        if (r2 == 0) goto L_0x0033;	 Catch:{ all -> 0x0046 }
    L_0x002b:
        r2 = r2.isSuccess();	 Catch:{ all -> 0x0046 }
        if (r2 != 0) goto L_0x0032;
    L_0x0031:
        goto L_0x0033;
    L_0x0032:
        goto L_0x0019;
    L_0x0033:
        r0 = r3.zaen;
        r0.unlock();
        return r1;
    L_0x0039:
        r0 = r3.zaen;
        r0.unlock();
        r0 = 1;
        return r0;
    L_0x0040:
        r0 = r3.zaen;
        r0.unlock();
        return r1;
    L_0x0046:
        r0 = move-exception;
        r1 = r3.zaen;
        r1.unlock();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zax.zaac():boolean");
    }

    public final void disconnect() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x0043 in {4, 8, 11, 14} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = r2.zaen;
        r0.lock();
        r0 = 0;
        r2.zafc = r0;	 Catch:{ all -> 0x003c }
        r0 = 0;	 Catch:{ all -> 0x003c }
        r2.zafd = r0;	 Catch:{ all -> 0x003c }
        r2.zafe = r0;	 Catch:{ all -> 0x003c }
        r1 = r2.zaff;	 Catch:{ all -> 0x003c }
        if (r1 == 0) goto L_0x0018;	 Catch:{ all -> 0x003c }
    L_0x0011:
        r1 = r2.zaff;	 Catch:{ all -> 0x003c }
        r1.cancel();	 Catch:{ all -> 0x003c }
        r2.zaff = r0;	 Catch:{ all -> 0x003c }
    L_0x0018:
        r2.zafg = r0;	 Catch:{ all -> 0x003c }
    L_0x001a:
        r1 = r2.zafb;	 Catch:{ all -> 0x003c }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x003c }
        if (r1 != 0) goto L_0x0031;	 Catch:{ all -> 0x003c }
    L_0x0022:
        r1 = r2.zafb;	 Catch:{ all -> 0x003c }
        r1 = r1.remove();	 Catch:{ all -> 0x003c }
        r1 = (com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl) r1;	 Catch:{ all -> 0x003c }
        r1.zaa(r0);	 Catch:{ all -> 0x003c }
        r1.cancel();	 Catch:{ all -> 0x003c }
        goto L_0x001a;	 Catch:{ all -> 0x003c }
    L_0x0031:
        r0 = r2.zaey;	 Catch:{ all -> 0x003c }
        r0.signalAll();	 Catch:{ all -> 0x003c }
        r0 = r2.zaen;
        r0.unlock();
        return;
    L_0x003c:
        r0 = move-exception;
        r1 = r2.zaen;
        r1.unlock();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zax.disconnect():void");
    }

    public final void maybeSignOut() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:20:0x0065 in {4, 7, 11, 14, 16, 19} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r4.zaen;
        r0.lock();
        r0 = r4.zabm;	 Catch:{ all -> 0x005e }
        r0.maybeSignOut();	 Catch:{ all -> 0x005e }
        r0 = r4.zaff;	 Catch:{ all -> 0x005e }
        if (r0 == 0) goto L_0x0016;	 Catch:{ all -> 0x005e }
    L_0x000e:
        r0 = r4.zaff;	 Catch:{ all -> 0x005e }
        r0.cancel();	 Catch:{ all -> 0x005e }
        r0 = 0;	 Catch:{ all -> 0x005e }
        r4.zaff = r0;	 Catch:{ all -> 0x005e }
    L_0x0016:
        r0 = r4.zafe;	 Catch:{ all -> 0x005e }
        if (r0 != 0) goto L_0x0027;	 Catch:{ all -> 0x005e }
    L_0x001a:
        r0 = new android.support.v4.util.ArrayMap;	 Catch:{ all -> 0x005e }
        r1 = r4.zaeu;	 Catch:{ all -> 0x005e }
        r1 = r1.size();	 Catch:{ all -> 0x005e }
        r0.<init>(r1);	 Catch:{ all -> 0x005e }
        r4.zafe = r0;	 Catch:{ all -> 0x005e }
    L_0x0027:
        r0 = new com.google.android.gms.common.ConnectionResult;	 Catch:{ all -> 0x005e }
        r1 = 4;	 Catch:{ all -> 0x005e }
        r0.<init>(r1);	 Catch:{ all -> 0x005e }
        r1 = r4.zaeu;	 Catch:{ all -> 0x005e }
        r1 = r1.values();	 Catch:{ all -> 0x005e }
        r1 = r1.iterator();	 Catch:{ all -> 0x005e }
    L_0x0037:
        r2 = r1.hasNext();	 Catch:{ all -> 0x005e }
        if (r2 == 0) goto L_0x004d;	 Catch:{ all -> 0x005e }
    L_0x003d:
        r2 = r1.next();	 Catch:{ all -> 0x005e }
        r2 = (com.google.android.gms.common.api.internal.zaw) r2;	 Catch:{ all -> 0x005e }
        r3 = r4.zafe;	 Catch:{ all -> 0x005e }
        r2 = r2.zak();	 Catch:{ all -> 0x005e }
        r3.put(r2, r0);	 Catch:{ all -> 0x005e }
        goto L_0x0037;	 Catch:{ all -> 0x005e }
    L_0x004d:
        r0 = r4.zafd;	 Catch:{ all -> 0x005e }
        if (r0 == 0) goto L_0x0058;	 Catch:{ all -> 0x005e }
    L_0x0051:
        r0 = r4.zafd;	 Catch:{ all -> 0x005e }
        r1 = r4.zafe;	 Catch:{ all -> 0x005e }
        r0.putAll(r1);	 Catch:{ all -> 0x005e }
    L_0x0058:
        r0 = r4.zaen;
        r0.unlock();
        return;
    L_0x005e:
        r0 = move-exception;
        r1 = r4.zaen;
        r1.unlock();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zax.maybeSignOut():void");
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        if (this.zaez && zab((ApiMethodImpl) t)) {
            return t;
        }
        if (isConnected()) {
            this.zaew.zahe.zab(t);
            return ((zaw) this.zaet.get(t.getClientKey())).doRead((ApiMethodImpl) t);
        }
        this.zafb.add(t);
        return t;
    }

    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        AnyClientKey clientKey = t.getClientKey();
        if (this.zaez && zab((ApiMethodImpl) t)) {
            return t;
        }
        this.zaew.zahe.zab(t);
        return ((zaw) this.zaet.get(clientKey)).doWrite((ApiMethodImpl) t);
    }

    private final <T extends ApiMethodImpl<? extends Result, ? extends AnyClient>> boolean zab(@NonNull T t) {
        AnyClientKey clientKey = t.getClientKey();
        ConnectionResult zaa = zaa(clientKey);
        if (zaa == null || zaa.getErrorCode() != 4) {
            return null;
        }
        t.setFailedResult(new Status(4, null, this.zabm.zaa(((zaw) this.zaet.get(clientKey)).zak(), System.identityHashCode(this.zaew))));
        return true;
    }

    public final void connect() {
        this.zaen.lock();
        try {
            if (!this.zafc) {
                this.zafc = true;
                this.zafd = null;
                this.zafe = null;
                this.zaff = null;
                this.zafg = null;
                this.zabm.zao();
                this.zabm.zaa(this.zaet.values()).addOnCompleteListener(new HandlerExecutor(this.zabj), new zaz());
                this.zaen.unlock();
            }
        } finally {
            this.zaen.unlock();
        }
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zaey.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        if (isConnected()) {
            return ConnectionResult.RESULT_SUCCESS;
        }
        ConnectionResult connectionResult = this.zafg;
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
            j = this.zaey.awaitNanos(j);
        }
        if (isConnected() != null) {
            return ConnectionResult.RESULT_SUCCESS;
        }
        j = this.zafg;
        if (j != null) {
            return j;
        }
        return new ConnectionResult(13, null);
    }

    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return zaa(api.getClientKey());
    }

    @Nullable
    private final ConnectionResult zaa(@NonNull AnyClientKey<?> anyClientKey) {
        this.zaen.lock();
        try {
            zaw zaw = (zaw) this.zaet.get(anyClientKey);
            if (this.zafd == null || zaw == null) {
                this.zaen.unlock();
                return null;
            }
            ConnectionResult connectionResult = (ConnectionResult) this.zafd.get(zaw.zak());
            return connectionResult;
        } finally {
            this.zaen.unlock();
        }
    }

    public final boolean isConnected() {
        this.zaen.lock();
        try {
            boolean z = this.zafd != null && this.zafg == null;
            this.zaen.unlock();
            return z;
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    public final boolean isConnecting() {
        this.zaen.lock();
        try {
            boolean z = this.zafd == null && this.zafc;
            this.zaen.unlock();
            return z;
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        this.zaen.lock();
        try {
            if (!this.zafc || zaac()) {
                this.zaen.unlock();
                return null;
            }
            this.zabm.zao();
            this.zaff = new zaaa(this, signInConnectionListener);
            this.zabm.zaa(this.zaeu.values()).addOnCompleteListener(new HandlerExecutor(this.zabj), this.zaff);
            return true;
        } finally {
            this.zaen.unlock();
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public final void zaw() {
    }

    @GuardedBy("mLock")
    private final void zaad() {
        ClientSettings clientSettings = this.zaes;
        if (clientSettings == null) {
            this.zaew.zagz = Collections.emptySet();
            return;
        }
        Set hashSet = new HashSet(clientSettings.getRequiredScopes());
        Map optionalApiSettings = this.zaes.getOptionalApiSettings();
        for (Api api : optionalApiSettings.keySet()) {
            ConnectionResult connectionResult = getConnectionResult(api);
            if (connectionResult != null && connectionResult.isSuccess()) {
                hashSet.addAll(((OptionalApiSettings) optionalApiSettings.get(api)).mScopes);
            }
        }
        this.zaew.zagz = hashSet;
    }

    @GuardedBy("mLock")
    private final void zaae() {
        while (!this.zafb.isEmpty()) {
            execute((ApiMethodImpl) this.zafb.remove());
        }
        this.zaew.zab(null);
    }

    private final boolean zaa(zaw<?> zaw, ConnectionResult connectionResult) {
        return (connectionResult.isSuccess() || connectionResult.hasResolution() || !((Boolean) this.zaev.get(zaw.getApi())).booleanValue() || zaw.zaab().requiresGooglePlayServices() == null || this.zaex.isUserResolvableError(connectionResult.getErrorCode()) == null) ? null : true;
    }

    @Nullable
    @GuardedBy("mLock")
    private final ConnectionResult zaaf() {
        ConnectionResult connectionResult = null;
        ConnectionResult connectionResult2 = null;
        int i = 0;
        int i2 = 0;
        for (zaw zaw : this.zaet.values()) {
            Api api = zaw.getApi();
            ConnectionResult connectionResult3 = (ConnectionResult) this.zafd.get(zaw.zak());
            if (!connectionResult3.isSuccess() && (!((Boolean) this.zaev.get(api)).booleanValue() || connectionResult3.hasResolution() || this.zaex.isUserResolvableError(connectionResult3.getErrorCode()))) {
                int priority;
                if (connectionResult3.getErrorCode() == 4 && this.zaez) {
                    priority = api.zah().getPriority();
                    if (connectionResult2 == null || r4 > priority) {
                        connectionResult2 = connectionResult3;
                        i2 = priority;
                    }
                } else {
                    priority = api.zah().getPriority();
                    if (connectionResult == null || r3 > priority) {
                        connectionResult = connectionResult3;
                        i = priority;
                    }
                }
            }
        }
        if (connectionResult == null || connectionResult2 == null || r3 <= r4) {
            return connectionResult;
        }
        return connectionResult2;
    }
}
