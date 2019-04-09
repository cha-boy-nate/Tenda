package com.google.android.gms.common.data;

import com.google.android.gms.common.annotation.KeepForSdk;

public final class DataBufferUtils {
    @KeepForSdk
    public static final String KEY_NEXT_PAGE_TOKEN = "next_page_token";
    @KeepForSdk
    public static final String KEY_PREV_PAGE_TOKEN = "prev_page_token";

    private DataBufferUtils() {
    }

    public static <T, E extends com.google.android.gms.common.data.Freezable<T>> java.util.ArrayList<T> freezeAndClose(com.google.android.gms.common.data.DataBuffer<E> r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x002b in {6, 8, 11} preds:[]
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
        r0 = new java.util.ArrayList;
        r1 = r3.getCount();
        r0.<init>(r1);
        r1 = r3.iterator();	 Catch:{ all -> 0x0026 }
    L_0x000d:
        r2 = r1.hasNext();	 Catch:{ all -> 0x0026 }
        if (r2 == 0) goto L_0x0021;	 Catch:{ all -> 0x0026 }
    L_0x0013:
        r2 = r1.next();	 Catch:{ all -> 0x0026 }
        r2 = (com.google.android.gms.common.data.Freezable) r2;	 Catch:{ all -> 0x0026 }
        r2 = r2.freeze();	 Catch:{ all -> 0x0026 }
        r0.add(r2);	 Catch:{ all -> 0x0026 }
        goto L_0x000d;
    L_0x0021:
        r3.close();
        return r0;
    L_0x0026:
        r0 = move-exception;
        r3.close();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.data.DataBufferUtils.freezeAndClose(com.google.android.gms.common.data.DataBuffer):java.util.ArrayList<T>");
    }

    public static boolean hasNextPage(DataBuffer<?> dataBuffer) {
        dataBuffer = dataBuffer.getMetadata();
        return (dataBuffer == null || dataBuffer.getString(KEY_NEXT_PAGE_TOKEN) == null) ? null : true;
    }

    public static boolean hasPrevPage(DataBuffer<?> dataBuffer) {
        dataBuffer = dataBuffer.getMetadata();
        return (dataBuffer == null || dataBuffer.getString(KEY_PREV_PAGE_TOKEN) == null) ? null : true;
    }

    public static boolean hasData(DataBuffer<?> dataBuffer) {
        return (dataBuffer == null || dataBuffer.getCount() <= null) ? null : true;
    }
}
