package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.OnCompleteListener;
import java.util.Map;

final class zaz implements OnCompleteListener<Map<zai<?>, String>> {
    private final /* synthetic */ zax zafh;

    private zaz(zax zax) {
        this.zafh = zax;
    }

    public final void onComplete(@android.support.annotation.NonNull com.google.android.gms.tasks.Task<java.util.Map<com.google.android.gms.common.api.internal.zai<?>, java.lang.String>> r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:42:0x016e in {5, 12, 13, 23, 24, 25, 26, 27, 28, 31, 34, 35, 38, 41} preds:[]
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
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r0.zafc;	 Catch:{ all -> 0x0163 }
        if (r0 != 0) goto L_0x001b;
    L_0x0011:
        r6 = r5.zafh;
        r6 = r6.zaen;
        r6.unlock();
        return;
    L_0x001b:
        r0 = r6.isSuccessful();	 Catch:{ all -> 0x0163 }
        if (r0 == 0) goto L_0x0061;	 Catch:{ all -> 0x0163 }
    L_0x0021:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = new android.support.v4.util.ArrayMap;	 Catch:{ all -> 0x0163 }
        r1 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r1 = r1.zaet;	 Catch:{ all -> 0x0163 }
        r1 = r1.size();	 Catch:{ all -> 0x0163 }
        r0.<init>(r1);	 Catch:{ all -> 0x0163 }
        r6.zafd = r0;	 Catch:{ all -> 0x0163 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6 = r6.zaet;	 Catch:{ all -> 0x0163 }
        r6 = r6.values();	 Catch:{ all -> 0x0163 }
        r6 = r6.iterator();	 Catch:{ all -> 0x0163 }
    L_0x0043:
        r0 = r6.hasNext();	 Catch:{ all -> 0x0163 }
        if (r0 == 0) goto L_0x005f;	 Catch:{ all -> 0x0163 }
    L_0x0049:
        r0 = r6.next();	 Catch:{ all -> 0x0163 }
        r0 = (com.google.android.gms.common.api.internal.zaw) r0;	 Catch:{ all -> 0x0163 }
        r1 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r1 = r1.zafd;	 Catch:{ all -> 0x0163 }
        r0 = r0.zak();	 Catch:{ all -> 0x0163 }
        r2 = com.google.android.gms.common.ConnectionResult.RESULT_SUCCESS;	 Catch:{ all -> 0x0163 }
        r1.put(r0, r2);	 Catch:{ all -> 0x0163 }
        goto L_0x0043;	 Catch:{ all -> 0x0163 }
    L_0x005f:
        goto L_0x0106;	 Catch:{ all -> 0x0163 }
    L_0x0061:
        r0 = r6.getException();	 Catch:{ all -> 0x0163 }
        r0 = r0 instanceof com.google.android.gms.common.api.AvailabilityException;	 Catch:{ all -> 0x0163 }
        if (r0 == 0) goto L_0x00e6;	 Catch:{ all -> 0x0163 }
    L_0x0069:
        r6 = r6.getException();	 Catch:{ all -> 0x0163 }
        r6 = (com.google.android.gms.common.api.AvailabilityException) r6;	 Catch:{ all -> 0x0163 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r0.zafa;	 Catch:{ all -> 0x0163 }
        if (r0 == 0) goto L_0x00d1;	 Catch:{ all -> 0x0163 }
    L_0x0077:
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r1 = new android.support.v4.util.ArrayMap;	 Catch:{ all -> 0x0163 }
        r2 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r2 = r2.zaet;	 Catch:{ all -> 0x0163 }
        r2 = r2.size();	 Catch:{ all -> 0x0163 }
        r1.<init>(r2);	 Catch:{ all -> 0x0163 }
        r0.zafd = r1;	 Catch:{ all -> 0x0163 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r0.zaet;	 Catch:{ all -> 0x0163 }
        r0 = r0.values();	 Catch:{ all -> 0x0163 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0163 }
    L_0x0099:
        r1 = r0.hasNext();	 Catch:{ all -> 0x0163 }
        if (r1 == 0) goto L_0x00d0;	 Catch:{ all -> 0x0163 }
    L_0x009f:
        r1 = r0.next();	 Catch:{ all -> 0x0163 }
        r1 = (com.google.android.gms.common.api.internal.zaw) r1;	 Catch:{ all -> 0x0163 }
        r2 = r1.zak();	 Catch:{ all -> 0x0163 }
        r3 = r6.getConnectionResult(r1);	 Catch:{ all -> 0x0163 }
        r4 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r1 = r4.zaa(r1, r3);	 Catch:{ all -> 0x0163 }
        if (r1 == 0) goto L_0x00c6;	 Catch:{ all -> 0x0163 }
    L_0x00b5:
        r1 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r1 = r1.zafd;	 Catch:{ all -> 0x0163 }
        r3 = new com.google.android.gms.common.ConnectionResult;	 Catch:{ all -> 0x0163 }
        r4 = 16;	 Catch:{ all -> 0x0163 }
        r3.<init>(r4);	 Catch:{ all -> 0x0163 }
        r1.put(r2, r3);	 Catch:{ all -> 0x0163 }
        goto L_0x0099;	 Catch:{ all -> 0x0163 }
    L_0x00c6:
        r1 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r1 = r1.zafd;	 Catch:{ all -> 0x0163 }
        r1.put(r2, r3);	 Catch:{ all -> 0x0163 }
        goto L_0x0099;	 Catch:{ all -> 0x0163 }
    L_0x00d0:
        goto L_0x00da;	 Catch:{ all -> 0x0163 }
    L_0x00d1:
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6 = r6.zaj();	 Catch:{ all -> 0x0163 }
        r0.zafd = r6;	 Catch:{ all -> 0x0163 }
    L_0x00da:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r0.zaaf();	 Catch:{ all -> 0x0163 }
        r6.zafg = r0;	 Catch:{ all -> 0x0163 }
        goto L_0x0106;	 Catch:{ all -> 0x0163 }
    L_0x00e6:
        r0 = "ConnectionlessGAC";	 Catch:{ all -> 0x0163 }
        r1 = "Unexpected availability exception";	 Catch:{ all -> 0x0163 }
        r6 = r6.getException();	 Catch:{ all -> 0x0163 }
        android.util.Log.e(r0, r1, r6);	 Catch:{ all -> 0x0163 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = java.util.Collections.emptyMap();	 Catch:{ all -> 0x0163 }
        r6.zafd = r0;	 Catch:{ all -> 0x0163 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = new com.google.android.gms.common.ConnectionResult;	 Catch:{ all -> 0x0163 }
        r1 = 8;	 Catch:{ all -> 0x0163 }
        r0.<init>(r1);	 Catch:{ all -> 0x0163 }
        r6.zafg = r0;	 Catch:{ all -> 0x0163 }
    L_0x0106:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6 = r6.zafe;	 Catch:{ all -> 0x0163 }
        if (r6 == 0) goto L_0x0128;	 Catch:{ all -> 0x0163 }
    L_0x010e:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6 = r6.zafd;	 Catch:{ all -> 0x0163 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r0.zafe;	 Catch:{ all -> 0x0163 }
        r6.putAll(r0);	 Catch:{ all -> 0x0163 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r0.zaaf();	 Catch:{ all -> 0x0163 }
        r6.zafg = r0;	 Catch:{ all -> 0x0163 }
    L_0x0128:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6 = r6.zafg;	 Catch:{ all -> 0x0163 }
        if (r6 != 0) goto L_0x013b;	 Catch:{ all -> 0x0163 }
    L_0x0130:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6.zaad();	 Catch:{ all -> 0x0163 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6.zaae();	 Catch:{ all -> 0x0163 }
        goto L_0x0150;	 Catch:{ all -> 0x0163 }
    L_0x013b:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = 0;	 Catch:{ all -> 0x0163 }
        r6.zafc = false;	 Catch:{ all -> 0x0163 }
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6 = r6.zaew;	 Catch:{ all -> 0x0163 }
        r0 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r0 = r0.zafg;	 Catch:{ all -> 0x0163 }
        r6.zac(r0);	 Catch:{ all -> 0x0163 }
    L_0x0150:
        r6 = r5.zafh;	 Catch:{ all -> 0x0163 }
        r6 = r6.zaey;	 Catch:{ all -> 0x0163 }
        r6.signalAll();	 Catch:{ all -> 0x0163 }
        r6 = r5.zafh;
        r6 = r6.zaen;
        r6.unlock();
        return;
    L_0x0163:
        r6 = move-exception;
        r0 = r5.zafh;
        r0 = r0.zaen;
        r0.unlock();
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zaz.onComplete(com.google.android.gms.tasks.Task):void");
    }
}
