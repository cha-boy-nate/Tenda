package com.google.android.gms.common.api.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.util.PlatformVersion;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public final class BackgroundDetector implements ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private static final BackgroundDetector zzas = new BackgroundDetector();
    private final AtomicBoolean zzat = new AtomicBoolean();
    private final AtomicBoolean zzau = new AtomicBoolean();
    @GuardedBy("sInstance")
    private final ArrayList<BackgroundStateChangeListener> zzav = new ArrayList();
    @GuardedBy("sInstance")
    private boolean zzaw = false;

    @KeepForSdk
    public interface BackgroundStateChangeListener {
        @KeepForSdk
        void onBackgroundStateChanged(boolean z);
    }

    @KeepForSdk
    private BackgroundDetector() {
    }

    private final void onBackgroundStateChanged(boolean r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:11:0x001f in {5, 7, 10} preds:[]
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
        r5 = this;
        r0 = zzas;
        monitor-enter(r0);
        r1 = r5.zzav;	 Catch:{ all -> 0x001c }
        r1 = (java.util.ArrayList) r1;	 Catch:{ all -> 0x001c }
        r2 = r1.size();	 Catch:{ all -> 0x001c }
        r3 = 0;	 Catch:{ all -> 0x001c }
    L_0x000c:
        if (r3 >= r2) goto L_0x001a;	 Catch:{ all -> 0x001c }
    L_0x000e:
        r4 = r1.get(r3);	 Catch:{ all -> 0x001c }
        r3 = r3 + 1;	 Catch:{ all -> 0x001c }
        r4 = (com.google.android.gms.common.api.internal.BackgroundDetector.BackgroundStateChangeListener) r4;	 Catch:{ all -> 0x001c }
        r4.onBackgroundStateChanged(r6);	 Catch:{ all -> 0x001c }
        goto L_0x000c;	 Catch:{ all -> 0x001c }
    L_0x001a:
        monitor-exit(r0);	 Catch:{ all -> 0x001c }
        return;	 Catch:{ all -> 0x001c }
    L_0x001c:
        r6 = move-exception;	 Catch:{ all -> 0x001c }
        monitor-exit(r0);	 Catch:{ all -> 0x001c }
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BackgroundDetector.onBackgroundStateChanged(boolean):void");
    }

    @KeepForSdk
    public static BackgroundDetector getInstance() {
        return zzas;
    }

    @KeepForSdk
    public static void initialize(Application application) {
        synchronized (zzas) {
            if (!zzas.zzaw) {
                application.registerActivityLifecycleCallbacks(zzas);
                application.registerComponentCallbacks(zzas);
                zzas.zzaw = true;
            }
        }
    }

    @TargetApi(16)
    @KeepForSdk
    public final boolean readCurrentStateIfPossible(boolean z) {
        if (!this.zzau.get()) {
            if (!PlatformVersion.isAtLeastJellyBean()) {
                return z;
            }
            z = new RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(z);
            if (!this.zzau.getAndSet(true) && z.importance > true) {
                this.zzat.set(true);
            }
        }
        return isInBackground();
    }

    @KeepForSdk
    public final boolean isInBackground() {
        return this.zzat.get();
    }

    @KeepForSdk
    public final void addListener(BackgroundStateChangeListener backgroundStateChangeListener) {
        synchronized (zzas) {
            this.zzav.add(backgroundStateChangeListener);
        }
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        activity = this.zzat.compareAndSet(true, false);
        this.zzau.set(true);
        if (activity != null) {
            onBackgroundStateChanged(false);
        }
    }

    public final void onActivityResumed(Activity activity) {
        activity = this.zzat.compareAndSet(true, false);
        this.zzau.set(true);
        if (activity != null) {
            onBackgroundStateChanged(false);
        }
    }

    public final void onTrimMemory(int i) {
        if (i == 20 && this.zzat.compareAndSet(false, true) != 0) {
            this.zzau.set(true);
            onBackgroundStateChanged(true);
        }
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityPaused(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public final void onActivityDestroyed(Activity activity) {
    }

    public final void onConfigurationChanged(Configuration configuration) {
    }

    public final void onLowMemory() {
    }
}
