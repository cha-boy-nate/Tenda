package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.base.zal;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class GmsClientEventManager implements Callback {
    private final Handler mHandler;
    private final Object mLock = new Object();
    private final GmsClientEventState zaok;
    private final ArrayList<ConnectionCallbacks> zaol = new ArrayList();
    @VisibleForTesting
    private final ArrayList<ConnectionCallbacks> zaom = new ArrayList();
    private final ArrayList<OnConnectionFailedListener> zaon = new ArrayList();
    private volatile boolean zaoo = false;
    private final AtomicInteger zaop = new AtomicInteger(0);
    private boolean zaoq = false;

    @VisibleForTesting
    public interface GmsClientEventState {
        Bundle getConnectionHint();

        boolean isConnected();
    }

    public GmsClientEventManager(Looper looper, GmsClientEventState gmsClientEventState) {
        this.zaok = gmsClientEventState;
        this.mHandler = new zal(looper, this);
    }

    @com.google.android.gms.common.util.VisibleForTesting
    public final void onConnectionFailure(com.google.android.gms.common.ConnectionResult r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:25:0x005b in {2, 3, 13, 16, 17, 19, 21, 24} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r7 = this;
        r0 = android.os.Looper.myLooper();
        r1 = r7.mHandler;
        r1 = r1.getLooper();
        r2 = 0;
        r3 = 1;
        if (r0 != r1) goto L_0x0010;
    L_0x000e:
        r0 = 1;
        goto L_0x0011;
    L_0x0010:
        r0 = 0;
    L_0x0011:
        r1 = "onConnectionFailure must only be called on the Handler thread";
        com.google.android.gms.common.internal.Preconditions.checkState(r0, r1);
        r0 = r7.mHandler;
        r0.removeMessages(r3);
        r0 = r7.mLock;
        monitor-enter(r0);
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x0058 }
        r3 = r7.zaon;	 Catch:{ all -> 0x0058 }
        r1.<init>(r3);	 Catch:{ all -> 0x0058 }
        r3 = r7.zaop;	 Catch:{ all -> 0x0058 }
        r3 = r3.get();	 Catch:{ all -> 0x0058 }
        r1 = (java.util.ArrayList) r1;	 Catch:{ all -> 0x0058 }
        r4 = r1.size();	 Catch:{ all -> 0x0058 }
    L_0x0031:
        if (r2 >= r4) goto L_0x0056;	 Catch:{ all -> 0x0058 }
    L_0x0033:
        r5 = r1.get(r2);	 Catch:{ all -> 0x0058 }
        r2 = r2 + 1;	 Catch:{ all -> 0x0058 }
        r5 = (com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener) r5;	 Catch:{ all -> 0x0058 }
        r6 = r7.zaoo;	 Catch:{ all -> 0x0058 }
        if (r6 == 0) goto L_0x0054;	 Catch:{ all -> 0x0058 }
    L_0x003f:
        r6 = r7.zaop;	 Catch:{ all -> 0x0058 }
        r6 = r6.get();	 Catch:{ all -> 0x0058 }
        if (r6 == r3) goto L_0x0048;	 Catch:{ all -> 0x0058 }
    L_0x0047:
        goto L_0x0054;	 Catch:{ all -> 0x0058 }
    L_0x0048:
        r6 = r7.zaon;	 Catch:{ all -> 0x0058 }
        r6 = r6.contains(r5);	 Catch:{ all -> 0x0058 }
        if (r6 == 0) goto L_0x0053;	 Catch:{ all -> 0x0058 }
    L_0x0050:
        r5.onConnectionFailed(r8);	 Catch:{ all -> 0x0058 }
    L_0x0053:
        goto L_0x0031;	 Catch:{ all -> 0x0058 }
    L_0x0054:
        monitor-exit(r0);	 Catch:{ all -> 0x0058 }
        return;	 Catch:{ all -> 0x0058 }
    L_0x0056:
        monitor-exit(r0);	 Catch:{ all -> 0x0058 }
        return;	 Catch:{ all -> 0x0058 }
    L_0x0058:
        r8 = move-exception;	 Catch:{ all -> 0x0058 }
        monitor-exit(r0);	 Catch:{ all -> 0x0058 }
        throw r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.GmsClientEventManager.onConnectionFailure(com.google.android.gms.common.ConnectionResult):void");
    }

    @com.google.android.gms.common.util.VisibleForTesting
    public final void onConnectionSuccess(android.os.Bundle r9) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:33:0x0081 in {2, 3, 9, 10, 13, 14, 25, 26, 29, 32} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r8 = this;
        r0 = android.os.Looper.myLooper();
        r1 = r8.mHandler;
        r1 = r1.getLooper();
        r2 = 0;
        r3 = 1;
        if (r0 != r1) goto L_0x0010;
    L_0x000e:
        r0 = 1;
        goto L_0x0011;
    L_0x0010:
        r0 = 0;
    L_0x0011:
        r1 = "onConnectionSuccess must only be called on the Handler thread";
        com.google.android.gms.common.internal.Preconditions.checkState(r0, r1);
        r0 = r8.mLock;
        monitor-enter(r0);
        r1 = r8.zaoq;	 Catch:{ all -> 0x007e }
        if (r1 != 0) goto L_0x001f;	 Catch:{ all -> 0x007e }
    L_0x001d:
        r1 = 1;	 Catch:{ all -> 0x007e }
        goto L_0x0020;	 Catch:{ all -> 0x007e }
    L_0x001f:
        r1 = 0;	 Catch:{ all -> 0x007e }
    L_0x0020:
        com.google.android.gms.common.internal.Preconditions.checkState(r1);	 Catch:{ all -> 0x007e }
        r1 = r8.mHandler;	 Catch:{ all -> 0x007e }
        r1.removeMessages(r3);	 Catch:{ all -> 0x007e }
        r8.zaoq = r3;	 Catch:{ all -> 0x007e }
        r1 = r8.zaom;	 Catch:{ all -> 0x007e }
        r1 = r1.size();	 Catch:{ all -> 0x007e }
        if (r1 != 0) goto L_0x0033;	 Catch:{ all -> 0x007e }
    L_0x0032:
        goto L_0x0034;	 Catch:{ all -> 0x007e }
    L_0x0033:
        r3 = 0;	 Catch:{ all -> 0x007e }
    L_0x0034:
        com.google.android.gms.common.internal.Preconditions.checkState(r3);	 Catch:{ all -> 0x007e }
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x007e }
        r3 = r8.zaol;	 Catch:{ all -> 0x007e }
        r1.<init>(r3);	 Catch:{ all -> 0x007e }
        r3 = r8.zaop;	 Catch:{ all -> 0x007e }
        r3 = r3.get();	 Catch:{ all -> 0x007e }
        r1 = (java.util.ArrayList) r1;	 Catch:{ all -> 0x007e }
        r4 = r1.size();	 Catch:{ all -> 0x007e }
        r5 = 0;	 Catch:{ all -> 0x007e }
    L_0x004b:
        if (r5 >= r4) goto L_0x0075;	 Catch:{ all -> 0x007e }
    L_0x004d:
        r6 = r1.get(r5);	 Catch:{ all -> 0x007e }
        r5 = r5 + 1;	 Catch:{ all -> 0x007e }
        r6 = (com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks) r6;	 Catch:{ all -> 0x007e }
        r7 = r8.zaoo;	 Catch:{ all -> 0x007e }
        if (r7 == 0) goto L_0x0075;	 Catch:{ all -> 0x007e }
    L_0x0059:
        r7 = r8.zaok;	 Catch:{ all -> 0x007e }
        r7 = r7.isConnected();	 Catch:{ all -> 0x007e }
        if (r7 == 0) goto L_0x0075;	 Catch:{ all -> 0x007e }
    L_0x0061:
        r7 = r8.zaop;	 Catch:{ all -> 0x007e }
        r7 = r7.get();	 Catch:{ all -> 0x007e }
        if (r7 != r3) goto L_0x0075;	 Catch:{ all -> 0x007e }
    L_0x0069:
        r7 = r8.zaom;	 Catch:{ all -> 0x007e }
        r7 = r7.contains(r6);	 Catch:{ all -> 0x007e }
        if (r7 != 0) goto L_0x0074;	 Catch:{ all -> 0x007e }
    L_0x0071:
        r6.onConnected(r9);	 Catch:{ all -> 0x007e }
    L_0x0074:
        goto L_0x004b;	 Catch:{ all -> 0x007e }
    L_0x0075:
        r9 = r8.zaom;	 Catch:{ all -> 0x007e }
        r9.clear();	 Catch:{ all -> 0x007e }
        r8.zaoq = r2;	 Catch:{ all -> 0x007e }
        monitor-exit(r0);	 Catch:{ all -> 0x007e }
        return;	 Catch:{ all -> 0x007e }
    L_0x007e:
        r9 = move-exception;	 Catch:{ all -> 0x007e }
        monitor-exit(r0);	 Catch:{ all -> 0x007e }
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.GmsClientEventManager.onConnectionSuccess(android.os.Bundle):void");
    }

    @com.google.android.gms.common.util.VisibleForTesting
    public final void onUnintentionalDisconnection(int r9) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x0062 in {2, 3, 15, 16, 19, 22} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r8 = this;
        r0 = android.os.Looper.myLooper();
        r1 = r8.mHandler;
        r1 = r1.getLooper();
        r2 = 0;
        r3 = 1;
        if (r0 != r1) goto L_0x0010;
    L_0x000e:
        r0 = 1;
        goto L_0x0011;
    L_0x0010:
        r0 = 0;
    L_0x0011:
        r1 = "onUnintentionalDisconnection must only be called on the Handler thread";
        com.google.android.gms.common.internal.Preconditions.checkState(r0, r1);
        r0 = r8.mHandler;
        r0.removeMessages(r3);
        r0 = r8.mLock;
        monitor-enter(r0);
        r8.zaoq = r3;	 Catch:{ all -> 0x005f }
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x005f }
        r3 = r8.zaol;	 Catch:{ all -> 0x005f }
        r1.<init>(r3);	 Catch:{ all -> 0x005f }
        r3 = r8.zaop;	 Catch:{ all -> 0x005f }
        r3 = r3.get();	 Catch:{ all -> 0x005f }
        r1 = (java.util.ArrayList) r1;	 Catch:{ all -> 0x005f }
        r4 = r1.size();	 Catch:{ all -> 0x005f }
        r5 = 0;	 Catch:{ all -> 0x005f }
    L_0x0034:
        if (r5 >= r4) goto L_0x0056;	 Catch:{ all -> 0x005f }
    L_0x0036:
        r6 = r1.get(r5);	 Catch:{ all -> 0x005f }
        r5 = r5 + 1;	 Catch:{ all -> 0x005f }
        r6 = (com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks) r6;	 Catch:{ all -> 0x005f }
        r7 = r8.zaoo;	 Catch:{ all -> 0x005f }
        if (r7 == 0) goto L_0x0056;	 Catch:{ all -> 0x005f }
    L_0x0042:
        r7 = r8.zaop;	 Catch:{ all -> 0x005f }
        r7 = r7.get();	 Catch:{ all -> 0x005f }
        if (r7 != r3) goto L_0x0056;	 Catch:{ all -> 0x005f }
    L_0x004a:
        r7 = r8.zaol;	 Catch:{ all -> 0x005f }
        r7 = r7.contains(r6);	 Catch:{ all -> 0x005f }
        if (r7 == 0) goto L_0x0055;	 Catch:{ all -> 0x005f }
    L_0x0052:
        r6.onConnectionSuspended(r9);	 Catch:{ all -> 0x005f }
    L_0x0055:
        goto L_0x0034;	 Catch:{ all -> 0x005f }
    L_0x0056:
        r9 = r8.zaom;	 Catch:{ all -> 0x005f }
        r9.clear();	 Catch:{ all -> 0x005f }
        r8.zaoq = r2;	 Catch:{ all -> 0x005f }
        monitor-exit(r0);	 Catch:{ all -> 0x005f }
        return;	 Catch:{ all -> 0x005f }
    L_0x005f:
        r9 = move-exception;	 Catch:{ all -> 0x005f }
        monitor-exit(r0);	 Catch:{ all -> 0x005f }
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.GmsClientEventManager.onUnintentionalDisconnection(int):void");
    }

    public final void disableCallbacks() {
        this.zaoo = false;
        this.zaop.incrementAndGet();
    }

    public final void enableCallbacks() {
        this.zaoo = true;
    }

    @VisibleForTesting
    protected final void onConnectionSuccess() {
        synchronized (this.mLock) {
            onConnectionSuccess(this.zaok.getConnectionHint());
        }
    }

    public final void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        Preconditions.checkNotNull(connectionCallbacks);
        synchronized (this.mLock) {
            if (this.zaol.contains(connectionCallbacks)) {
                String valueOf = String.valueOf(connectionCallbacks);
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 62);
                stringBuilder.append("registerConnectionCallbacks(): listener ");
                stringBuilder.append(valueOf);
                stringBuilder.append(" is already registered");
                Log.w("GmsClientEvents", stringBuilder.toString());
            } else {
                this.zaol.add(connectionCallbacks);
            }
        }
        if (this.zaok.isConnected()) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(1, connectionCallbacks));
        }
    }

    public final boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        Preconditions.checkNotNull(connectionCallbacks);
        synchronized (this.mLock) {
            connectionCallbacks = this.zaol.contains(connectionCallbacks);
        }
        return connectionCallbacks;
    }

    public final void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        Preconditions.checkNotNull(connectionCallbacks);
        synchronized (this.mLock) {
            if (!this.zaol.remove(connectionCallbacks)) {
                connectionCallbacks = String.valueOf(connectionCallbacks);
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(connectionCallbacks).length() + 52);
                stringBuilder.append("unregisterConnectionCallbacks(): listener ");
                stringBuilder.append(connectionCallbacks);
                stringBuilder.append(" not found");
                Log.w("GmsClientEvents", stringBuilder.toString());
            } else if (this.zaoq) {
                this.zaom.add(connectionCallbacks);
            }
        }
    }

    public final void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        Preconditions.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (this.zaon.contains(onConnectionFailedListener)) {
                onConnectionFailedListener = String.valueOf(onConnectionFailedListener);
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(onConnectionFailedListener).length() + 67);
                stringBuilder.append("registerConnectionFailedListener(): listener ");
                stringBuilder.append(onConnectionFailedListener);
                stringBuilder.append(" is already registered");
                Log.w("GmsClientEvents", stringBuilder.toString());
            } else {
                this.zaon.add(onConnectionFailedListener);
            }
        }
    }

    public final boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        Preconditions.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            onConnectionFailedListener = this.zaon.contains(onConnectionFailedListener);
        }
        return onConnectionFailedListener;
    }

    public final void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        Preconditions.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (!this.zaon.remove(onConnectionFailedListener)) {
                onConnectionFailedListener = String.valueOf(onConnectionFailedListener);
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(onConnectionFailedListener).length() + 57);
                stringBuilder.append("unregisterConnectionFailedListener(): listener ");
                stringBuilder.append(onConnectionFailedListener);
                stringBuilder.append(" not found");
                Log.w("GmsClientEvents", stringBuilder.toString());
            }
        }
    }

    public final boolean handleMessage(Message message) {
        if (message.what == 1) {
            ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) message.obj;
            synchronized (this.mLock) {
                if (this.zaoo && this.zaok.isConnected() && this.zaol.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(this.zaok.getConnectionHint());
                }
            }
            return true;
        }
        message = message.what;
        StringBuilder stringBuilder = new StringBuilder(45);
        stringBuilder.append("Don't know how to handle message: ");
        stringBuilder.append(message);
        Log.wtf("GmsClientEvents", stringBuilder.toString(), new Exception());
        return null;
    }

    public final boolean areCallbacksEnabled() {
        return this.zaoo;
    }
}
