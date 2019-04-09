package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public final class zaab {
    private final Map<BasePendingResult<?>, Boolean> zafj = Collections.synchronizedMap(new WeakHashMap());
    private final Map<TaskCompletionSource<?>, Boolean> zafk = Collections.synchronizedMap(new WeakHashMap());

    private final void zaa(boolean r5, com.google.android.gms.common.api.Status r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:37:0x007a in {16, 17, 18, 25, 26, 27, 28, 32, 36} preds:[]
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
        r4 = this;
        r0 = r4.zafj;
        monitor-enter(r0);
        r1 = new java.util.HashMap;	 Catch:{ all -> 0x0077 }
        r2 = r4.zafj;	 Catch:{ all -> 0x0077 }
        r1.<init>(r2);	 Catch:{ all -> 0x0077 }
        monitor-exit(r0);	 Catch:{ all -> 0x0077 }
        r2 = r4.zafk;
        monitor-enter(r2);
        r0 = new java.util.HashMap;	 Catch:{ all -> 0x0074 }
        r3 = r4.zafk;	 Catch:{ all -> 0x0074 }
        r0.<init>(r3);	 Catch:{ all -> 0x0074 }
        monitor-exit(r2);	 Catch:{ all -> 0x0074 }
        r1 = r1.entrySet();
        r1 = r1.iterator();
    L_0x001e:
        r2 = r1.hasNext();
        if (r2 == 0) goto L_0x0042;
    L_0x0024:
        r2 = r1.next();
        r2 = (java.util.Map.Entry) r2;
        if (r5 != 0) goto L_0x0038;
    L_0x002c:
        r3 = r2.getValue();
        r3 = (java.lang.Boolean) r3;
        r3 = r3.booleanValue();
        if (r3 == 0) goto L_0x0041;
    L_0x0038:
        r2 = r2.getKey();
        r2 = (com.google.android.gms.common.api.internal.BasePendingResult) r2;
        r2.zab(r6);
    L_0x0041:
        goto L_0x001e;
    L_0x0042:
        r0 = r0.entrySet();
        r0 = r0.iterator();
    L_0x004a:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x0073;
    L_0x0050:
        r1 = r0.next();
        r1 = (java.util.Map.Entry) r1;
        if (r5 != 0) goto L_0x0064;
    L_0x0058:
        r2 = r1.getValue();
        r2 = (java.lang.Boolean) r2;
        r2 = r2.booleanValue();
        if (r2 == 0) goto L_0x0072;
    L_0x0064:
        r1 = r1.getKey();
        r1 = (com.google.android.gms.tasks.TaskCompletionSource) r1;
        r2 = new com.google.android.gms.common.api.ApiException;
        r2.<init>(r6);
        r1.trySetException(r2);
    L_0x0072:
        goto L_0x004a;
    L_0x0073:
        return;
    L_0x0074:
        r5 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0074 }
        throw r5;
    L_0x0077:
        r5 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0077 }
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zaab.zaa(boolean, com.google.android.gms.common.api.Status):void");
    }

    final void zaa(BasePendingResult<? extends Result> basePendingResult, boolean z) {
        this.zafj.put(basePendingResult, Boolean.valueOf(z));
        basePendingResult.addStatusListener(new zaac(this, basePendingResult));
    }

    final <TResult> void zaa(TaskCompletionSource<TResult> taskCompletionSource, boolean z) {
        this.zafk.put(taskCompletionSource, Boolean.valueOf(z));
        taskCompletionSource.getTask().addOnCompleteListener(new zaad(this, taskCompletionSource));
    }

    final boolean zaag() {
        if (this.zafj.isEmpty()) {
            if (this.zafk.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public final void zaah() {
        zaa(false, GoogleApiManager.zahw);
    }

    public final void zaai() {
        zaa(true, zacp.zakw);
    }
}
