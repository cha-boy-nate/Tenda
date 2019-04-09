package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClientEventManager;
import com.google.android.gms.common.internal.GmsClientEventManager.GmsClientEventState;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.common.util.ClientLibraryUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zad;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

public final class zaaw extends GoogleApiClient implements zabt {
    private final Context mContext;
    private final Looper zabj;
    private final int zaca;
    private final GoogleApiAvailability zacc;
    private final AbstractClientBuilder<? extends zad, SignInOptions> zacd;
    private boolean zacg;
    private final Lock zaen;
    private final ClientSettings zaes;
    private final Map<Api<?>, Boolean> zaev;
    @VisibleForTesting
    final Queue<ApiMethodImpl<?, ?>> zafb = new LinkedList();
    private final GmsClientEventManager zagr;
    private zabs zags = null;
    private volatile boolean zagt;
    private long zagu;
    private long zagv;
    private final zabb zagw;
    @VisibleForTesting
    private zabq zagx;
    final Map<AnyClientKey<?>, Client> zagy;
    Set<Scope> zagz;
    private final ListenerHolders zaha;
    private final ArrayList<zaq> zahb;
    private Integer zahc;
    Set<zacm> zahd;
    final zacp zahe;
    private final GmsClientEventState zahf;

    public zaaw(Context context, Lock lock, Looper looper, ClientSettings clientSettings, GoogleApiAvailability googleApiAvailability, AbstractClientBuilder<? extends zad, SignInOptions> abstractClientBuilder, Map<Api<?>, Boolean> map, List<ConnectionCallbacks> list, List<OnConnectionFailedListener> list2, Map<AnyClientKey<?>, Client> map2, int i, int i2, ArrayList<zaq> arrayList, boolean z) {
        Looper looper2 = looper;
        r0.zagu = ClientLibraryUtils.isPackageSide() ? 10000 : 120000;
        r0.zagv = 5000;
        r0.zagz = new HashSet();
        r0.zaha = new ListenerHolders();
        r0.zahc = null;
        r0.zahd = null;
        r0.zahf = new zaax(this);
        r0.mContext = context;
        r0.zaen = lock;
        r0.zacg = false;
        r0.zagr = new GmsClientEventManager(looper, r0.zahf);
        r0.zabj = looper2;
        r0.zagw = new zabb(this, looper);
        r0.zacc = googleApiAvailability;
        r0.zaca = i;
        if (r0.zaca >= 0) {
            r0.zahc = Integer.valueOf(i2);
        }
        r0.zaev = map;
        r0.zagy = map2;
        r0.zahb = arrayList;
        r0.zahe = new zacp(r0.zagy);
        for (ConnectionCallbacks registerConnectionCallbacks : list) {
            r0.zagr.registerConnectionCallbacks(registerConnectionCallbacks);
        }
        for (OnConnectionFailedListener registerConnectionFailedListener : list2) {
            r0.zagr.registerConnectionFailedListener(registerConnectionFailedListener);
        }
        r0.zaes = clientSettings;
        r0.zacd = abstractClientBuilder;
    }

    private final void zae(int r14) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:45:0x011d in {2, 7, 13, 16, 17, 20, 21, 26, 28, 31, 33, 35, 40, 42, 44} preds:[]
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
        r13 = this;
        r0 = r13.zahc;
        if (r0 != 0) goto L_0x000b;
    L_0x0004:
        r0 = java.lang.Integer.valueOf(r14);
        r13.zahc = r0;
        goto L_0x0011;
    L_0x000b:
        r0 = r0.intValue();
        if (r0 != r14) goto L_0x00dd;
    L_0x0011:
        r0 = r13.zags;
        if (r0 == 0) goto L_0x0016;
    L_0x0015:
        return;
        r0 = r13.zagy;
        r0 = r0.values();
        r0 = r0.iterator();
        r1 = 0;
        r2 = 0;
    L_0x0024:
        r3 = r0.hasNext();
        if (r3 == 0) goto L_0x0040;
    L_0x002a:
        r3 = r0.next();
        r3 = (com.google.android.gms.common.api.Api.Client) r3;
        r4 = r3.requiresSignIn();
        r5 = 1;
        if (r4 == 0) goto L_0x0038;
    L_0x0037:
        r1 = 1;
    L_0x0038:
        r3 = r3.providesSignIn();
        if (r3 == 0) goto L_0x003f;
    L_0x003e:
        r2 = 1;
    L_0x003f:
        goto L_0x0024;
    L_0x0040:
        r0 = r13.zahc;
        r0 = r0.intValue();
        switch(r0) {
            case 1: goto L_0x0088;
            case 2: goto L_0x004b;
            case 3: goto L_0x004a;
            default: goto L_0x0049;
        };
    L_0x0049:
        goto L_0x009d;
    L_0x004a:
        goto L_0x009d;
    L_0x004b:
        if (r1 == 0) goto L_0x009d;
    L_0x004d:
        r0 = r13.zacg;
        if (r0 == 0) goto L_0x006e;
    L_0x0051:
        r12 = new com.google.android.gms.common.api.internal.zax;
        r1 = r13.mContext;
        r2 = r13.zaen;
        r3 = r13.zabj;
        r4 = r13.zacc;
        r5 = r13.zagy;
        r6 = r13.zaes;
        r7 = r13.zaev;
        r8 = r13.zacd;
        r9 = r13.zahb;
        r11 = 1;
        r0 = r12;
        r10 = r13;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        r13.zags = r12;
        return;
    L_0x006e:
        r0 = r13.mContext;
        r2 = r13.zaen;
        r3 = r13.zabj;
        r4 = r13.zacc;
        r5 = r13.zagy;
        r6 = r13.zaes;
        r7 = r13.zaev;
        r8 = r13.zacd;
        r9 = r13.zahb;
        r1 = r13;
        r0 = com.google.android.gms.common.api.internal.zas.zaa(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9);
        r13.zags = r0;
        return;
    L_0x0088:
        if (r1 == 0) goto L_0x0095;
    L_0x008a:
        if (r2 != 0) goto L_0x008d;
    L_0x008c:
        goto L_0x009d;
    L_0x008d:
        r0 = new java.lang.IllegalStateException;
        r1 = "Cannot use SIGN_IN_MODE_REQUIRED with GOOGLE_SIGN_IN_API. Use connect(SIGN_IN_MODE_OPTIONAL) instead.";
        r0.<init>(r1);
        throw r0;
    L_0x0095:
        r0 = new java.lang.IllegalStateException;
        r1 = "SIGN_IN_MODE_REQUIRED cannot be used on a GoogleApiClient that does not contain any authenticated APIs. Use connect() instead.";
        r0.<init>(r1);
        throw r0;
    L_0x009d:
        r0 = r13.zacg;
        if (r0 == 0) goto L_0x00c0;
    L_0x00a1:
        if (r2 != 0) goto L_0x00c0;
    L_0x00a3:
        r12 = new com.google.android.gms.common.api.internal.zax;
        r1 = r13.mContext;
        r2 = r13.zaen;
        r3 = r13.zabj;
        r4 = r13.zacc;
        r5 = r13.zagy;
        r6 = r13.zaes;
        r7 = r13.zaev;
        r8 = r13.zacd;
        r9 = r13.zahb;
        r11 = 0;
        r0 = r12;
        r10 = r13;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        r13.zags = r12;
        return;
    L_0x00c0:
        r12 = new com.google.android.gms.common.api.internal.zabe;
        r1 = r13.mContext;
        r3 = r13.zaen;
        r4 = r13.zabj;
        r5 = r13.zacc;
        r6 = r13.zagy;
        r7 = r13.zaes;
        r8 = r13.zaev;
        r9 = r13.zacd;
        r10 = r13.zahb;
        r0 = r12;
        r2 = r13;
        r11 = r13;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        r13.zags = r12;
        return;
    L_0x00dd:
        r0 = new java.lang.IllegalStateException;
        r1 = zaf(r14);
        r2 = r13.zahc;
        r2 = r2.intValue();
        r2 = zaf(r2);
        r3 = java.lang.String.valueOf(r1);
        r3 = r3.length();
        r3 = r3 + 51;
        r4 = java.lang.String.valueOf(r2);
        r4 = r4.length();
        r3 = r3 + r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>(r3);
        r3 = "Cannot use sign-in mode: ";
        r4.append(r3);
        r4.append(r1);
        r1 = ". Mode was already set to ";
        r4.append(r1);
        r4.append(r2);
        r1 = r4.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zaaw.zae(int):void");
    }

    public final void disconnect() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:20:0x0056 in {4, 8, 12, 16, 19} preds:[]
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
        r0 = r3.zahe;	 Catch:{ all -> 0x004f }
        r0.release();	 Catch:{ all -> 0x004f }
        r0 = r3.zags;	 Catch:{ all -> 0x004f }
        if (r0 == 0) goto L_0x0013;	 Catch:{ all -> 0x004f }
    L_0x000e:
        r0 = r3.zags;	 Catch:{ all -> 0x004f }
        r0.disconnect();	 Catch:{ all -> 0x004f }
    L_0x0013:
        r0 = r3.zaha;	 Catch:{ all -> 0x004f }
        r0.release();	 Catch:{ all -> 0x004f }
        r0 = r3.zafb;	 Catch:{ all -> 0x004f }
        r0 = r0.iterator();	 Catch:{ all -> 0x004f }
    L_0x001e:
        r1 = r0.hasNext();	 Catch:{ all -> 0x004f }
        if (r1 == 0) goto L_0x0032;	 Catch:{ all -> 0x004f }
    L_0x0024:
        r1 = r0.next();	 Catch:{ all -> 0x004f }
        r1 = (com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl) r1;	 Catch:{ all -> 0x004f }
        r2 = 0;	 Catch:{ all -> 0x004f }
        r1.zaa(r2);	 Catch:{ all -> 0x004f }
        r1.cancel();	 Catch:{ all -> 0x004f }
        goto L_0x001e;	 Catch:{ all -> 0x004f }
    L_0x0032:
        r0 = r3.zafb;	 Catch:{ all -> 0x004f }
        r0.clear();	 Catch:{ all -> 0x004f }
        r0 = r3.zags;	 Catch:{ all -> 0x004f }
        if (r0 != 0) goto L_0x0041;
    L_0x003b:
        r0 = r3.zaen;
        r0.unlock();
        return;
    L_0x0041:
        r3.zaaw();	 Catch:{ all -> 0x004f }
        r0 = r3.zagr;	 Catch:{ all -> 0x004f }
        r0.disableCallbacks();	 Catch:{ all -> 0x004f }
        r0 = r3.zaen;
        r0.unlock();
        return;
    L_0x004f:
        r0 = move-exception;
        r1 = r3.zaen;
        r1.unlock();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zaaw.disconnect():void");
    }

    public final <A extends com.google.android.gms.common.api.Api.AnyClient, T extends com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl<? extends com.google.android.gms.common.api.Result, A>> T execute(@android.support.annotation.NonNull T r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:31:0x009d in {2, 3, 6, 7, 18, 20, 24, 27, 30} preds:[]
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
        r0 = r5.getClientKey();
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        r0 = 1;
        goto L_0x000a;
    L_0x0009:
        r0 = 0;
    L_0x000a:
        r1 = "This task can not be executed (it's probably a Batch or malformed)";
        com.google.android.gms.common.internal.Preconditions.checkArgument(r0, r1);
        r0 = r4.zagy;
        r1 = r5.getClientKey();
        r0 = r0.containsKey(r1);
        r1 = r5.getApi();
        if (r1 == 0) goto L_0x0028;
    L_0x001f:
        r1 = r5.getApi();
        r1 = r1.getName();
        goto L_0x002a;
    L_0x0028:
        r1 = "the API";
    L_0x002a:
        r2 = java.lang.String.valueOf(r1);
        r2 = r2.length();
        r2 = r2 + 65;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r2);
        r2 = "GoogleApiClient is not configured to use ";
        r3.append(r2);
        r3.append(r1);
        r1 = " required for this call.";
        r3.append(r1);
        r1 = r3.toString();
        com.google.android.gms.common.internal.Preconditions.checkArgument(r0, r1);
        r0 = r4.zaen;
        r0.lock();
        r0 = r4.zags;	 Catch:{ all -> 0x0096 }
        if (r0 == 0) goto L_0x008e;	 Catch:{ all -> 0x0096 }
        r0 = r4.zagt;	 Catch:{ all -> 0x0096 }
        if (r0 == 0) goto L_0x0082;	 Catch:{ all -> 0x0096 }
    L_0x005b:
        r0 = r4.zafb;	 Catch:{ all -> 0x0096 }
        r0.add(r5);	 Catch:{ all -> 0x0096 }
    L_0x0060:
        r0 = r4.zafb;	 Catch:{ all -> 0x0096 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0096 }
        if (r0 != 0) goto L_0x007b;	 Catch:{ all -> 0x0096 }
    L_0x0068:
        r0 = r4.zafb;	 Catch:{ all -> 0x0096 }
        r0 = r0.remove();	 Catch:{ all -> 0x0096 }
        r0 = (com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl) r0;	 Catch:{ all -> 0x0096 }
        r1 = r4.zahe;	 Catch:{ all -> 0x0096 }
        r1.zab(r0);	 Catch:{ all -> 0x0096 }
        r1 = com.google.android.gms.common.api.Status.RESULT_INTERNAL_ERROR;	 Catch:{ all -> 0x0096 }
        r0.setFailedResult(r1);	 Catch:{ all -> 0x0096 }
        goto L_0x0060;
        r0 = r4.zaen;
        r0.unlock();
        return r5;
    L_0x0082:
        r0 = r4.zags;	 Catch:{ all -> 0x0096 }
        r5 = r0.execute(r5);	 Catch:{ all -> 0x0096 }
        r0 = r4.zaen;
        r0.unlock();
        return r5;
    L_0x008e:
        r5 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0096 }
        r0 = "GoogleApiClient is not connected yet.";	 Catch:{ all -> 0x0096 }
        r5.<init>(r0);	 Catch:{ all -> 0x0096 }
        throw r5;	 Catch:{ all -> 0x0096 }
    L_0x0096:
        r5 = move-exception;
        r0 = r4.zaen;
        r0.unlock();
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zaaw.execute(com.google.android.gms.common.api.internal.BaseImplementation$ApiMethodImpl):T");
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        Preconditions.checkArgument(t.getClientKey() != null, "This task can not be enqueued (it's probably a Batch or malformed)");
        boolean containsKey = this.zagy.containsKey(t.getClientKey());
        String name = t.getApi() != null ? t.getApi().getName() : "the API";
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(name).length() + 65);
        stringBuilder.append("GoogleApiClient is not configured to use ");
        stringBuilder.append(name);
        stringBuilder.append(" required for this call.");
        Preconditions.checkArgument(containsKey, stringBuilder.toString());
        this.zaen.lock();
        try {
            if (this.zags == null) {
                this.zafb.add(t);
                return t;
            }
            t = this.zags.enqueue(t);
            this.zaen.unlock();
            return t;
        } finally {
            this.zaen.unlock();
        }
    }

    public final <L> ListenerHolder<L> registerListener(@NonNull L l) {
        this.zaen.lock();
        try {
            l = this.zaha.zaa(l, this.zabj, "NO_TYPE");
            return l;
        } finally {
            this.zaen.unlock();
        }
    }

    @NonNull
    public final <C extends Client> C getClient(@NonNull AnyClientKey<C> anyClientKey) {
        Client client = (Client) this.zagy.get(anyClientKey);
        Preconditions.checkNotNull(client, "Appropriate Api was not requested.");
        return client;
    }

    public final boolean hasApi(@NonNull Api<?> api) {
        return this.zagy.containsKey(api.getClientKey());
    }

    public final boolean hasConnectedApi(@NonNull Api<?> api) {
        if (!isConnected()) {
            return false;
        }
        Client client = (Client) this.zagy.get(api.getClientKey());
        if (client == null || client.isConnected() == null) {
            return false;
        }
        return true;
    }

    @NonNull
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        this.zaen.lock();
        try {
            if (!isConnected()) {
                if (!this.zagt) {
                    throw new IllegalStateException("Cannot invoke getConnectionResult unless GoogleApiClient is connected");
                }
            }
            if (this.zagy.containsKey(api.getClientKey())) {
                ConnectionResult connectionResult = this.zags.getConnectionResult(api);
                if (connectionResult != null) {
                    this.zaen.unlock();
                    return connectionResult;
                } else if (this.zagt) {
                    api = ConnectionResult.RESULT_SUCCESS;
                    return api;
                } else {
                    Log.w("GoogleApiClientImpl", zaay());
                    Log.wtf("GoogleApiClientImpl", String.valueOf(api.getName()).concat(" requested in getConnectionResult is not connected but is not present in the failed  connections map"), new Exception());
                    api = new ConnectionResult(8, null);
                    this.zaen.unlock();
                    return api;
                }
            }
            throw new IllegalArgumentException(String.valueOf(api.getName()).concat(" was never registered with GoogleApiClient"));
        } finally {
            this.zaen.unlock();
        }
    }

    public final void connect() {
        this.zaen.lock();
        try {
            boolean z = false;
            if (this.zaca >= 0) {
                if (this.zahc != null) {
                    z = true;
                }
                Preconditions.checkState(z, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zahc == null) {
                this.zahc = Integer.valueOf(zaa(this.zagy.values(), false));
            } else if (this.zahc.intValue() == 2) {
                throw new IllegalStateException("Cannot call connect() when SignInMode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            connect(this.zahc.intValue());
        } finally {
            this.zaen.unlock();
        }
    }

    public final void connect(int i) {
        this.zaen.lock();
        boolean z = true;
        if (!(i == 3 || i == 1)) {
            if (i != 2) {
                z = false;
            }
        }
        try {
            StringBuilder stringBuilder = new StringBuilder(33);
            stringBuilder.append("Illegal sign-in mode: ");
            stringBuilder.append(i);
            Preconditions.checkArgument(z, stringBuilder.toString());
            zae(i);
            zaau();
        } finally {
            this.zaen.unlock();
        }
    }

    public final ConnectionResult blockingConnect() {
        boolean z = true;
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        this.zaen.lock();
        try {
            if (this.zaca >= 0) {
                if (this.zahc == null) {
                    z = false;
                }
                Preconditions.checkState(z, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zahc == null) {
                this.zahc = Integer.valueOf(zaa(this.zagy.values(), false));
            } else if (this.zahc.intValue() == 2) {
                throw new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            zae(this.zahc.intValue());
            this.zagr.enableCallbacks();
            ConnectionResult blockingConnect = this.zags.blockingConnect();
            return blockingConnect;
        } finally {
            this.zaen.unlock();
        }
    }

    public final ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        Preconditions.checkNotNull(timeUnit, "TimeUnit must not be null");
        this.zaen.lock();
        try {
            if (this.zahc == null) {
                this.zahc = Integer.valueOf(zaa(this.zagy.values(), false));
            } else if (this.zahc.intValue() == 2) {
                throw new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            zae(this.zahc.intValue());
            this.zagr.enableCallbacks();
            j = this.zags.blockingConnect(j, timeUnit);
            return j;
        } finally {
            this.zaen.unlock();
        }
    }

    public final void reconnect() {
        disconnect();
        connect();
    }

    public final PendingResult<Status> clearDefaultAccountAndReconnect() {
        Preconditions.checkState(isConnected(), "GoogleApiClient is not connected yet.");
        Preconditions.checkState(this.zahc.intValue() != 2, "Cannot use clearDefaultAccountAndReconnect with GOOGLE_SIGN_IN_API");
        PendingResult statusPendingResult = new StatusPendingResult((GoogleApiClient) this);
        if (this.zagy.containsKey(Common.CLIENT_KEY)) {
            zaa(this, statusPendingResult, false);
        } else {
            AtomicReference atomicReference = new AtomicReference();
            GoogleApiClient build = new Builder(this.mContext).addApi(Common.API).addConnectionCallbacks(new zaay(this, atomicReference, statusPendingResult)).addOnConnectionFailedListener(new zaaz(this, statusPendingResult)).setHandler(this.zagw).build();
            atomicReference.set(build);
            build.connect();
        }
        return statusPendingResult;
    }

    private final void zaa(GoogleApiClient googleApiClient, StatusPendingResult statusPendingResult, boolean z) {
        Common.zaph.zaa(googleApiClient).setResultCallback(new zaba(this, statusPendingResult, z, googleApiClient));
    }

    public final void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {
        LifecycleActivity lifecycleActivity = new LifecycleActivity((Activity) fragmentActivity);
        if (this.zaca >= null) {
            zaj.zaa(lifecycleActivity).zaa(this.zaca);
            return;
        }
        throw new IllegalStateException("Called stopAutoManage but automatic lifecycle management is not enabled.");
    }

    public final boolean isConnected() {
        zabs zabs = this.zags;
        return zabs != null && zabs.isConnected();
    }

    public final boolean isConnecting() {
        zabs zabs = this.zags;
        return zabs != null && zabs.isConnecting();
    }

    @GuardedBy("mLock")
    private final void zaau() {
        this.zagr.enableCallbacks();
        this.zags.connect();
    }

    private final void resume() {
        this.zaen.lock();
        try {
            if (this.zagt) {
                zaau();
            }
            this.zaen.unlock();
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    private final void zaav() {
        this.zaen.lock();
        try {
            if (zaaw()) {
                zaau();
            }
            this.zaen.unlock();
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    @GuardedBy("mLock")
    final boolean zaaw() {
        if (!this.zagt) {
            return false;
        }
        this.zagt = false;
        this.zagw.removeMessages(2);
        this.zagw.removeMessages(1);
        zabq zabq = this.zagx;
        if (zabq != null) {
            zabq.unregister();
            this.zagx = null;
        }
        return true;
    }

    public final void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
        this.zagr.registerConnectionCallbacks(connectionCallbacks);
    }

    public final boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
        return this.zagr.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public final void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
        this.zagr.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public final void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        this.zagr.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public final boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        return this.zagr.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public final void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
        this.zagr.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    @GuardedBy("mLock")
    public final void zab(Bundle bundle) {
        while (!this.zafb.isEmpty()) {
            execute((ApiMethodImpl) this.zafb.remove());
        }
        this.zagr.onConnectionSuccess(bundle);
    }

    @GuardedBy("mLock")
    public final void zac(ConnectionResult connectionResult) {
        if (!this.zacc.isPlayServicesPossiblyUpdating(this.mContext, connectionResult.getErrorCode())) {
            zaaw();
        }
        if (!this.zagt) {
            this.zagr.onConnectionFailure(connectionResult);
            this.zagr.disableCallbacks();
        }
    }

    @GuardedBy("mLock")
    public final void zab(int i, boolean z) {
        if (!(i != 1 || z || this.zagt)) {
            this.zagt = true;
            if (!(this.zagx || ClientLibraryUtils.isPackageSide())) {
                this.zagx = this.zacc.zaa(this.mContext.getApplicationContext(), new zabc(this));
            }
            z = this.zagw;
            z.sendMessageDelayed(z.obtainMessage(1), this.zagu);
            z = this.zagw;
            z.sendMessageDelayed(z.obtainMessage(2), this.zagv);
        }
        this.zahe.zabx();
        this.zagr.onUnintentionalDisconnection(i);
        this.zagr.disableCallbacks();
        if (i == 2) {
            zaau();
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zabj;
    }

    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        zabs zabs = this.zags;
        return (zabs == null || zabs.maybeSignIn(signInConnectionListener) == null) ? null : true;
    }

    public final void maybeSignOut() {
        zabs zabs = this.zags;
        if (zabs != null) {
            zabs.maybeSignOut();
        }
    }

    public final void zaa(zacm zacm) {
        this.zaen.lock();
        try {
            if (this.zahd == null) {
                this.zahd = new HashSet();
            }
            this.zahd.add(zacm);
        } finally {
            this.zaen.unlock();
        }
    }

    public final void zab(zacm zacm) {
        this.zaen.lock();
        try {
            if (this.zahd == null) {
                Log.wtf("GoogleApiClientImpl", "Attempted to remove pending transform when no transforms are registered.", new Exception());
            } else if (this.zahd.remove(zacm) == null) {
                Log.wtf("GoogleApiClientImpl", "Failed to remove pending transform - this may lead to memory leaks!", new Exception());
            } else if (zaax() == null) {
                this.zags.zaw();
            }
            this.zaen.unlock();
        } catch (Throwable th) {
            this.zaen.unlock();
        }
    }

    final boolean zaax() {
        this.zaen.lock();
        try {
            if (this.zahd == null) {
                return false;
            }
            boolean isEmpty = this.zahd.isEmpty() ^ 1;
            this.zaen.unlock();
            return isEmpty;
        } finally {
            this.zaen.unlock();
        }
    }

    final String zaay() {
        Writer stringWriter = new StringWriter();
        dump("", null, new PrintWriter(stringWriter), null);
        return stringWriter.toString();
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).append("mContext=").println(this.mContext);
        printWriter.append(str).append("mResuming=").print(this.zagt);
        printWriter.append(" mWorkQueue.size()=").print(this.zafb.size());
        printWriter.append(" mUnconsumedApiCalls.size()=").println(this.zahe.zaky.size());
        zabs zabs = this.zags;
        if (zabs != null) {
            zabs.dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    public static int zaa(Iterable<Client> iterable, boolean z) {
        Object obj = null;
        Object obj2 = null;
        for (Client client : iterable) {
            if (client.requiresSignIn()) {
                obj = 1;
            }
            if (client.providesSignIn()) {
                obj2 = 1;
            }
        }
        if (obj == null) {
            return 3;
        }
        if (obj2 == null || !z) {
            return 1;
        }
        return 2;
    }

    private static String zaf(int i) {
        switch (i) {
            case 1:
                return "SIGN_IN_MODE_REQUIRED";
            case 2:
                return "SIGN_IN_MODE_OPTIONAL";
            case 3:
                return "SIGN_IN_MODE_NONE";
            default:
                return "UNKNOWN";
        }
    }
}
