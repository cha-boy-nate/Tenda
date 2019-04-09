package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.OnCompleteListener;
import java.util.Map;

final class zaaa implements OnCompleteListener<Map<zai<?>, String>> {
    private final /* synthetic */ zax zafh;
    private SignInConnectionListener zafi;

    zaaa(zax zax, SignInConnectionListener signInConnectionListener) {
        this.zafh = zax;
        this.zafi = signInConnectionListener;
    }

    public final void onComplete(@android.support.annotation.NonNull com.google.android.gms.tasks.Task<java.util.Map<com.google.android.gms.common.api.internal.zai<?>, java.lang.String>> r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:40:0x0140 in {6, 13, 14, 24, 25, 26, 27, 28, 33, 36, 39} preds:[]
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
        r0 = r5.zafh;
        r0 = r0.zaen;
        r0.lock();
        r0 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r0 = r0.zafc;	 Catch:{ all -> 0x0135 }
        if (r0 != 0) goto L_0x0020;	 Catch:{ all -> 0x0135 }
    L_0x0011:
        r6 = r5.zafi;	 Catch:{ all -> 0x0135 }
        r6.onComplete();	 Catch:{ all -> 0x0135 }
        r6 = r5.zafh;
        r6 = r6.zaen;
        r6.unlock();
        return;
    L_0x0020:
        r0 = r6.isSuccessful();	 Catch:{ all -> 0x0135 }
        if (r0 == 0) goto L_0x0066;	 Catch:{ all -> 0x0135 }
    L_0x0026:
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r0 = new android.support.v4.util.ArrayMap;	 Catch:{ all -> 0x0135 }
        r1 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r1 = r1.zaeu;	 Catch:{ all -> 0x0135 }
        r1 = r1.size();	 Catch:{ all -> 0x0135 }
        r0.<init>(r1);	 Catch:{ all -> 0x0135 }
        r6.zafe = r0;	 Catch:{ all -> 0x0135 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6 = r6.zaeu;	 Catch:{ all -> 0x0135 }
        r6 = r6.values();	 Catch:{ all -> 0x0135 }
        r6 = r6.iterator();	 Catch:{ all -> 0x0135 }
    L_0x0048:
        r0 = r6.hasNext();	 Catch:{ all -> 0x0135 }
        if (r0 == 0) goto L_0x0064;	 Catch:{ all -> 0x0135 }
    L_0x004e:
        r0 = r6.next();	 Catch:{ all -> 0x0135 }
        r0 = (com.google.android.gms.common.api.internal.zaw) r0;	 Catch:{ all -> 0x0135 }
        r1 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r1 = r1.zafe;	 Catch:{ all -> 0x0135 }
        r0 = r0.zak();	 Catch:{ all -> 0x0135 }
        r2 = com.google.android.gms.common.ConnectionResult.RESULT_SUCCESS;	 Catch:{ all -> 0x0135 }
        r1.put(r0, r2);	 Catch:{ all -> 0x0135 }
        goto L_0x0048;	 Catch:{ all -> 0x0135 }
    L_0x0064:
        goto L_0x00f4;	 Catch:{ all -> 0x0135 }
    L_0x0066:
        r0 = r6.getException();	 Catch:{ all -> 0x0135 }
        r0 = r0 instanceof com.google.android.gms.common.api.AvailabilityException;	 Catch:{ all -> 0x0135 }
        if (r0 == 0) goto L_0x00e0;	 Catch:{ all -> 0x0135 }
    L_0x006e:
        r6 = r6.getException();	 Catch:{ all -> 0x0135 }
        r6 = (com.google.android.gms.common.api.AvailabilityException) r6;	 Catch:{ all -> 0x0135 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r0 = r0.zafa;	 Catch:{ all -> 0x0135 }
        if (r0 == 0) goto L_0x00d6;	 Catch:{ all -> 0x0135 }
    L_0x007c:
        r0 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r1 = new android.support.v4.util.ArrayMap;	 Catch:{ all -> 0x0135 }
        r2 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r2 = r2.zaeu;	 Catch:{ all -> 0x0135 }
        r2 = r2.size();	 Catch:{ all -> 0x0135 }
        r1.<init>(r2);	 Catch:{ all -> 0x0135 }
        r0.zafe = r1;	 Catch:{ all -> 0x0135 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r0 = r0.zaeu;	 Catch:{ all -> 0x0135 }
        r0 = r0.values();	 Catch:{ all -> 0x0135 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0135 }
    L_0x009e:
        r1 = r0.hasNext();	 Catch:{ all -> 0x0135 }
        if (r1 == 0) goto L_0x00d5;	 Catch:{ all -> 0x0135 }
    L_0x00a4:
        r1 = r0.next();	 Catch:{ all -> 0x0135 }
        r1 = (com.google.android.gms.common.api.internal.zaw) r1;	 Catch:{ all -> 0x0135 }
        r2 = r1.zak();	 Catch:{ all -> 0x0135 }
        r3 = r6.getConnectionResult(r1);	 Catch:{ all -> 0x0135 }
        r4 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r1 = r4.zaa(r1, r3);	 Catch:{ all -> 0x0135 }
        if (r1 == 0) goto L_0x00cb;	 Catch:{ all -> 0x0135 }
    L_0x00ba:
        r1 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r1 = r1.zafe;	 Catch:{ all -> 0x0135 }
        r3 = new com.google.android.gms.common.ConnectionResult;	 Catch:{ all -> 0x0135 }
        r4 = 16;	 Catch:{ all -> 0x0135 }
        r3.<init>(r4);	 Catch:{ all -> 0x0135 }
        r1.put(r2, r3);	 Catch:{ all -> 0x0135 }
        goto L_0x009e;	 Catch:{ all -> 0x0135 }
    L_0x00cb:
        r1 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r1 = r1.zafe;	 Catch:{ all -> 0x0135 }
        r1.put(r2, r3);	 Catch:{ all -> 0x0135 }
        goto L_0x009e;	 Catch:{ all -> 0x0135 }
    L_0x00d5:
        goto L_0x00f4;	 Catch:{ all -> 0x0135 }
    L_0x00d6:
        r0 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6 = r6.zaj();	 Catch:{ all -> 0x0135 }
        r0.zafe = r6;	 Catch:{ all -> 0x0135 }
        goto L_0x00f4;	 Catch:{ all -> 0x0135 }
    L_0x00e0:
        r0 = "ConnectionlessGAC";	 Catch:{ all -> 0x0135 }
        r1 = "Unexpected availability exception";	 Catch:{ all -> 0x0135 }
        r6 = r6.getException();	 Catch:{ all -> 0x0135 }
        android.util.Log.e(r0, r1, r6);	 Catch:{ all -> 0x0135 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r0 = java.util.Collections.emptyMap();	 Catch:{ all -> 0x0135 }
        r6.zafe = r0;	 Catch:{ all -> 0x0135 }
    L_0x00f4:
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6 = r6.isConnected();	 Catch:{ all -> 0x0135 }
        if (r6 == 0) goto L_0x0126;	 Catch:{ all -> 0x0135 }
    L_0x00fc:
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6 = r6.zafd;	 Catch:{ all -> 0x0135 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r0 = r0.zafe;	 Catch:{ all -> 0x0135 }
        r6.putAll(r0);	 Catch:{ all -> 0x0135 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6 = r6.zaaf();	 Catch:{ all -> 0x0135 }
        if (r6 != 0) goto L_0x0126;	 Catch:{ all -> 0x0135 }
    L_0x0113:
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6.zaad();	 Catch:{ all -> 0x0135 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6.zaae();	 Catch:{ all -> 0x0135 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0135 }
        r6 = r6.zaey;	 Catch:{ all -> 0x0135 }
        r6.signalAll();	 Catch:{ all -> 0x0135 }
    L_0x0126:
        r6 = r5.zafi;	 Catch:{ all -> 0x0135 }
        r6.onComplete();	 Catch:{ all -> 0x0135 }
        r6 = r5.zafh;
        r6 = r6.zaen;
        r6.unlock();
        return;
    L_0x0135:
        r6 = move-exception;
        r0 = r5.zafh;
        r0 = r0.zaen;
        r0.unlock();
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zaaa.onComplete(com.google.android.gms.tasks.Task):void");
    }

    final void cancel() {
        this.zafi.onComplete();
    }
}
