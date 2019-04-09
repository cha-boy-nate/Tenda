package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class zaa extends ActivityLifecycleObserver {
    private final WeakReference<zaa> zack;

    @VisibleForTesting(otherwise = 2)
    static class zaa extends LifecycleCallback {
        private List<Runnable> zacl = new ArrayList();

        private static zaa zaa(Activity activity) {
            zaa zaa;
            synchronized (activity) {
                LifecycleFragment fragment = LifecycleCallback.getFragment(activity);
                zaa = (zaa) fragment.getCallbackOrNull("LifecycleObserverOnStop", zaa.class);
                if (zaa == null) {
                    zaa = new zaa(fragment);
                }
            }
            return zaa;
        }

        @android.support.annotation.MainThread
        public void onStop() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:13:0x0023 in {7, 8, 12} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
            /*
            r2 = this;
            monitor-enter(r2);
            r0 = r2.zacl;	 Catch:{ all -> 0x0020 }
            r1 = new java.util.ArrayList;	 Catch:{ all -> 0x0020 }
            r1.<init>();	 Catch:{ all -> 0x0020 }
            r2.zacl = r1;	 Catch:{ all -> 0x0020 }
            monitor-exit(r2);	 Catch:{ all -> 0x0020 }
            r0 = r0.iterator();
        L_0x000f:
            r1 = r0.hasNext();
            if (r1 == 0) goto L_0x001f;
        L_0x0015:
            r1 = r0.next();
            r1 = (java.lang.Runnable) r1;
            r1.run();
            goto L_0x000f;
        L_0x001f:
            return;
        L_0x0020:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0020 }
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zaa.zaa.onStop():void");
        }

        private zaa(LifecycleFragment lifecycleFragment) {
            super(lifecycleFragment);
            this.mLifecycleFragment.addCallback("LifecycleObserverOnStop", this);
        }

        private final synchronized void zaa(Runnable runnable) {
            this.zacl.add(runnable);
        }
    }

    public zaa(Activity activity) {
        this(zaa.zaa(activity));
    }

    @VisibleForTesting(otherwise = 2)
    private zaa(zaa zaa) {
        this.zack = new WeakReference(zaa);
    }

    public final ActivityLifecycleObserver onStopCallOnce(Runnable runnable) {
        zaa zaa = (zaa) this.zack.get();
        if (zaa != null) {
            zaa.zaa(runnable);
            return this;
        }
        throw new IllegalStateException("The target activity has already been GC'd");
    }
}
