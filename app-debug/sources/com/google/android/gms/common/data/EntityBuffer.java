package com.google.android.gms.common.data;

import com.google.android.gms.common.annotation.KeepForSdk;
import java.util.ArrayList;

@KeepForSdk
public abstract class EntityBuffer<T> extends AbstractDataBuffer<T> {
    private boolean zamd = null;
    private ArrayList<Integer> zame;

    @KeepForSdk
    protected EntityBuffer(DataHolder dataHolder) {
        super(dataHolder);
    }

    private final void zacb() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x008d in {12, 13, 15, 16, 18, 21} preds:[]
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
        monitor-enter(r7);
        r0 = r7.zamd;	 Catch:{ all -> 0x008a }
        if (r0 != 0) goto L_0x0088;	 Catch:{ all -> 0x008a }
    L_0x0005:
        r0 = r7.mDataHolder;	 Catch:{ all -> 0x008a }
        r0 = r0.getCount();	 Catch:{ all -> 0x008a }
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x008a }
        r1.<init>();	 Catch:{ all -> 0x008a }
        r7.zame = r1;	 Catch:{ all -> 0x008a }
        r1 = 1;	 Catch:{ all -> 0x008a }
        if (r0 <= 0) goto L_0x0086;	 Catch:{ all -> 0x008a }
    L_0x0015:
        r2 = r7.zame;	 Catch:{ all -> 0x008a }
        r3 = 0;	 Catch:{ all -> 0x008a }
        r4 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x008a }
        r2.add(r4);	 Catch:{ all -> 0x008a }
        r2 = r7.getPrimaryDataMarkerColumn();	 Catch:{ all -> 0x008a }
        r4 = r7.mDataHolder;	 Catch:{ all -> 0x008a }
        r4 = r4.getWindowIndex(r3);	 Catch:{ all -> 0x008a }
        r5 = r7.mDataHolder;	 Catch:{ all -> 0x008a }
        r3 = r5.getString(r2, r3, r4);	 Catch:{ all -> 0x008a }
        r4 = r3;	 Catch:{ all -> 0x008a }
        r3 = 1;	 Catch:{ all -> 0x008a }
    L_0x0031:
        if (r3 >= r0) goto L_0x0086;	 Catch:{ all -> 0x008a }
    L_0x0033:
        r5 = r7.mDataHolder;	 Catch:{ all -> 0x008a }
        r5 = r5.getWindowIndex(r3);	 Catch:{ all -> 0x008a }
        r6 = r7.mDataHolder;	 Catch:{ all -> 0x008a }
        r6 = r6.getString(r2, r3, r5);	 Catch:{ all -> 0x008a }
        if (r6 == 0) goto L_0x0055;	 Catch:{ all -> 0x008a }
    L_0x0041:
        r5 = r6.equals(r4);	 Catch:{ all -> 0x008a }
        if (r5 != 0) goto L_0x0052;	 Catch:{ all -> 0x008a }
        r4 = r7.zame;	 Catch:{ all -> 0x008a }
        r5 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x008a }
        r4.add(r5);	 Catch:{ all -> 0x008a }
        r4 = r6;	 Catch:{ all -> 0x008a }
    L_0x0052:
        r3 = r3 + 1;	 Catch:{ all -> 0x008a }
        goto L_0x0031;	 Catch:{ all -> 0x008a }
    L_0x0055:
        r0 = new java.lang.NullPointerException;	 Catch:{ all -> 0x008a }
        r1 = java.lang.String.valueOf(r2);	 Catch:{ all -> 0x008a }
        r1 = r1.length();	 Catch:{ all -> 0x008a }
        r1 = r1 + 78;	 Catch:{ all -> 0x008a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008a }
        r4.<init>(r1);	 Catch:{ all -> 0x008a }
        r1 = "Missing value for markerColumn: ";	 Catch:{ all -> 0x008a }
        r4.append(r1);	 Catch:{ all -> 0x008a }
        r4.append(r2);	 Catch:{ all -> 0x008a }
        r1 = ", at row: ";	 Catch:{ all -> 0x008a }
        r4.append(r1);	 Catch:{ all -> 0x008a }
        r4.append(r3);	 Catch:{ all -> 0x008a }
        r1 = ", for window: ";	 Catch:{ all -> 0x008a }
        r4.append(r1);	 Catch:{ all -> 0x008a }
        r4.append(r5);	 Catch:{ all -> 0x008a }
        r1 = r4.toString();	 Catch:{ all -> 0x008a }
        r0.<init>(r1);	 Catch:{ all -> 0x008a }
        throw r0;	 Catch:{ all -> 0x008a }
    L_0x0086:
        r7.zamd = r1;	 Catch:{ all -> 0x008a }
    L_0x0088:
        monitor-exit(r7);	 Catch:{ all -> 0x008a }
        return;	 Catch:{ all -> 0x008a }
    L_0x008a:
        r0 = move-exception;	 Catch:{ all -> 0x008a }
        monitor-exit(r7);	 Catch:{ all -> 0x008a }
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.data.EntityBuffer.zacb():void");
    }

    @KeepForSdk
    protected abstract T getEntry(int i, int i2);

    @KeepForSdk
    protected abstract String getPrimaryDataMarkerColumn();

    @KeepForSdk
    public final T get(int i) {
        zacb();
        int zah = zah(i);
        int i2 = 0;
        if (i >= 0) {
            if (i != this.zame.size()) {
                int count;
                if (i == this.zame.size() - 1) {
                    count = this.mDataHolder.getCount() - ((Integer) this.zame.get(i)).intValue();
                } else {
                    count = ((Integer) this.zame.get(i + 1)).intValue() - ((Integer) this.zame.get(i)).intValue();
                }
                if (count == 1) {
                    i = zah(i);
                    int windowIndex = this.mDataHolder.getWindowIndex(i);
                    String childDataMarkerColumn = getChildDataMarkerColumn();
                    if (childDataMarkerColumn != null && this.mDataHolder.getString(childDataMarkerColumn, i, windowIndex) == 0) {
                        return getEntry(zah, i2);
                    }
                }
                i2 = count;
                return getEntry(zah, i2);
            }
        }
        return getEntry(zah, i2);
    }

    @KeepForSdk
    public int getCount() {
        zacb();
        return this.zame.size();
    }

    private final int zah(int i) {
        if (i >= 0 && i < this.zame.size()) {
            return ((Integer) this.zame.get(i)).intValue();
        }
        StringBuilder stringBuilder = new StringBuilder(53);
        stringBuilder.append("Position ");
        stringBuilder.append(i);
        stringBuilder.append(" is out of bounds for this buffer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @KeepForSdk
    protected String getChildDataMarkerColumn() {
        return null;
    }
}
