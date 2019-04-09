package com.google.android.gms.dynamic;

import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;

@KeepForSdk
public final class ObjectWrapper<T> extends Stub {
    private final T zzhz;

    private ObjectWrapper(T t) {
        this.zzhz = t;
    }

    @com.google.android.gms.common.annotation.KeepForSdk
    public static <T> T unwrap(com.google.android.gms.dynamic.IObjectWrapper r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:28:0x0071 in {3, 8, 9, 17, 20, 23, 25, 27} preds:[]
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
        r0 = r7 instanceof com.google.android.gms.dynamic.ObjectWrapper;
        if (r0 == 0) goto L_0x0009;
    L_0x0004:
        r7 = (com.google.android.gms.dynamic.ObjectWrapper) r7;
        r7 = r7.zzhz;
        return r7;
    L_0x0009:
        r7 = r7.asBinder();
        r0 = r7.getClass();
        r0 = r0.getDeclaredFields();
        r1 = 0;
        r2 = r0.length;
        r3 = 0;
        r4 = r1;
        r1 = 0;
    L_0x001b:
        if (r3 >= r2) goto L_0x002c;
    L_0x001d:
        r5 = r0[r3];
        r6 = r5.isSynthetic();
        if (r6 != 0) goto L_0x0029;
        r1 = r1 + 1;
        r4 = r5;
    L_0x0029:
        r3 = r3 + 1;
        goto L_0x001b;
    L_0x002c:
        r2 = 1;
        if (r1 != r2) goto L_0x0057;
    L_0x002f:
        r0 = r4.isAccessible();
        if (r0 != 0) goto L_0x004f;
    L_0x0035:
        r4.setAccessible(r2);
        r7 = r4.get(r7);	 Catch:{ NullPointerException -> 0x0046, IllegalAccessException -> 0x003d }
        return r7;
    L_0x003d:
        r7 = move-exception;
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Could not access the field in remoteBinder.";
        r0.<init>(r1, r7);
        throw r0;
    L_0x0046:
        r7 = move-exception;
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Binder object is null.";
        r0.<init>(r1, r7);
        throw r0;
    L_0x004f:
        r7 = new java.lang.IllegalArgumentException;
        r0 = "IObjectWrapper declared field not private!";
        r7.<init>(r0);
        throw r7;
    L_0x0057:
        r7 = new java.lang.IllegalArgumentException;
        r0 = r0.length;
        r1 = 64;
        r2 = new java.lang.StringBuilder;
        r2.<init>(r1);
        r1 = "Unexpected number of IObjectWrapper declared fields: ";
        r2.append(r1);
        r2.append(r0);
        r0 = r2.toString();
        r7.<init>(r0);
        throw r7;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamic.ObjectWrapper.unwrap(com.google.android.gms.dynamic.IObjectWrapper):T");
    }

    @KeepForSdk
    public static <T> IObjectWrapper wrap(T t) {
        return new ObjectWrapper(t);
    }
}
