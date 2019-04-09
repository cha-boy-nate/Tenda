package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.sqlite.CursorWrapper;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@KeepName
@KeepForSdk
@Class(creator = "DataHolderCreator", validate = true)
public final class DataHolder extends AbstractSafeParcelable implements Closeable {
    @KeepForSdk
    public static final Creator<DataHolder> CREATOR = new zac();
    private static final Builder zalx = new zab(new String[0], null);
    private boolean mClosed;
    @VersionField(id = 1000)
    private final int zale;
    @Field(getter = "getColumns", id = 1)
    private final String[] zalp;
    private Bundle zalq;
    @Field(getter = "getWindows", id = 2)
    private final CursorWindow[] zalr;
    @Field(getter = "getStatusCode", id = 3)
    private final int zals;
    @Field(getter = "getMetadata", id = 4)
    private final Bundle zalt;
    private int[] zalu;
    private int zalv;
    private boolean zalw;

    @KeepForSdk
    public static class Builder {
        private final String[] zalp;
        private final ArrayList<HashMap<String, Object>> zaly;
        private final String zalz;
        private final HashMap<Object, Integer> zama;
        private boolean zamb;
        private String zamc;

        private Builder(String[] strArr, String str) {
            this.zalp = (String[]) Preconditions.checkNotNull(strArr);
            this.zaly = new ArrayList();
            this.zalz = str;
            this.zama = new HashMap();
            this.zamb = null;
            this.zamc = null;
        }

        public Builder zaa(HashMap<String, Object> hashMap) {
            int i;
            Asserts.checkNotNull(hashMap);
            String str = this.zalz;
            if (str == null) {
                i = -1;
            } else {
                Object obj = hashMap.get(str);
                if (obj == null) {
                    i = -1;
                } else {
                    Integer num = (Integer) this.zama.get(obj);
                    if (num == null) {
                        this.zama.put(obj, Integer.valueOf(this.zaly.size()));
                        i = -1;
                    } else {
                        i = num.intValue();
                    }
                }
            }
            if (i == -1) {
                this.zaly.add(hashMap);
            } else {
                this.zaly.remove(i);
                this.zaly.add(i, hashMap);
            }
            this.zamb = null;
            return this;
        }

        @KeepForSdk
        public Builder withRow(ContentValues contentValues) {
            Asserts.checkNotNull(contentValues);
            HashMap hashMap = new HashMap(contentValues.size());
            for (Entry entry : contentValues.valueSet()) {
                hashMap.put((String) entry.getKey(), entry.getValue());
            }
            return zaa(hashMap);
        }

        @KeepForSdk
        public DataHolder build(int i) {
            return new DataHolder(this, i);
        }

        @KeepForSdk
        public DataHolder build(int i, Bundle bundle) {
            return new DataHolder(this, i, bundle);
        }
    }

    public static class zaa extends RuntimeException {
        public zaa(String str) {
            super(str);
        }
    }

    @Constructor
    DataHolder(@Param(id = 1000) int i, @Param(id = 1) String[] strArr, @Param(id = 2) CursorWindow[] cursorWindowArr, @Param(id = 3) int i2, @Param(id = 4) Bundle bundle) {
        this.mClosed = false;
        this.zalw = true;
        this.zale = i;
        this.zalp = strArr;
        this.zalr = cursorWindowArr;
        this.zals = i2;
        this.zalt = bundle;
    }

    private static android.database.CursorWindow[] zaa(com.google.android.gms.common.sqlite.CursorWrapper r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:24:0x0070 in {6, 7, 13, 14, 18, 20, 23} preds:[]
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
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = r6.getCount();	 Catch:{ all -> 0x006b }
        r2 = r6.getWindow();	 Catch:{ all -> 0x006b }
        r3 = 0;	 Catch:{ all -> 0x006b }
        r4 = 0;	 Catch:{ all -> 0x006b }
        if (r2 == 0) goto L_0x0026;	 Catch:{ all -> 0x006b }
    L_0x0012:
        r5 = r2.getStartPosition();	 Catch:{ all -> 0x006b }
        if (r5 != 0) goto L_0x0026;	 Catch:{ all -> 0x006b }
    L_0x0018:
        r2.acquireReference();	 Catch:{ all -> 0x006b }
        r6.setWindow(r3);	 Catch:{ all -> 0x006b }
        r0.add(r2);	 Catch:{ all -> 0x006b }
        r2 = r2.getNumRows();	 Catch:{ all -> 0x006b }
        goto L_0x0027;	 Catch:{ all -> 0x006b }
    L_0x0026:
        r2 = 0;	 Catch:{ all -> 0x006b }
    L_0x0027:
        if (r2 >= r1) goto L_0x005a;	 Catch:{ all -> 0x006b }
    L_0x0029:
        r5 = r6.moveToPosition(r2);	 Catch:{ all -> 0x006b }
        if (r5 == 0) goto L_0x005a;	 Catch:{ all -> 0x006b }
    L_0x002f:
        r5 = r6.getWindow();	 Catch:{ all -> 0x006b }
        if (r5 == 0) goto L_0x003c;	 Catch:{ all -> 0x006b }
    L_0x0035:
        r5.acquireReference();	 Catch:{ all -> 0x006b }
        r6.setWindow(r3);	 Catch:{ all -> 0x006b }
        goto L_0x0047;	 Catch:{ all -> 0x006b }
    L_0x003c:
        r5 = new android.database.CursorWindow;	 Catch:{ all -> 0x006b }
        r5.<init>(r4);	 Catch:{ all -> 0x006b }
        r5.setStartPosition(r2);	 Catch:{ all -> 0x006b }
        r6.fillWindow(r2, r5);	 Catch:{ all -> 0x006b }
    L_0x0047:
        r2 = r5.getNumRows();	 Catch:{ all -> 0x006b }
        if (r2 == 0) goto L_0x005a;	 Catch:{ all -> 0x006b }
    L_0x004d:
        r0.add(r5);	 Catch:{ all -> 0x006b }
        r2 = r5.getStartPosition();	 Catch:{ all -> 0x006b }
        r5 = r5.getNumRows();	 Catch:{ all -> 0x006b }
        r2 = r2 + r5;
        goto L_0x0027;
    L_0x005a:
        r6.close();
        r6 = r0.size();
        r6 = new android.database.CursorWindow[r6];
        r6 = r0.toArray(r6);
        r6 = (android.database.CursorWindow[]) r6;
        return r6;
    L_0x006b:
        r0 = move-exception;
        r6.close();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.data.DataHolder.zaa(com.google.android.gms.common.sqlite.CursorWrapper):android.database.CursorWindow[]");
    }

    @com.google.android.gms.common.annotation.KeepForSdk
    public final void close() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:13:0x001d in {7, 9, 12} preds:[]
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
        r2 = this;
        monitor-enter(r2);
        r0 = r2.mClosed;	 Catch:{ all -> 0x001a }
        if (r0 != 0) goto L_0x0018;	 Catch:{ all -> 0x001a }
    L_0x0005:
        r0 = 1;	 Catch:{ all -> 0x001a }
        r2.mClosed = r0;	 Catch:{ all -> 0x001a }
        r0 = 0;	 Catch:{ all -> 0x001a }
    L_0x0009:
        r1 = r2.zalr;	 Catch:{ all -> 0x001a }
        r1 = r1.length;	 Catch:{ all -> 0x001a }
        if (r0 >= r1) goto L_0x0018;	 Catch:{ all -> 0x001a }
    L_0x000e:
        r1 = r2.zalr;	 Catch:{ all -> 0x001a }
        r1 = r1[r0];	 Catch:{ all -> 0x001a }
        r1.close();	 Catch:{ all -> 0x001a }
        r0 = r0 + 1;	 Catch:{ all -> 0x001a }
        goto L_0x0009;	 Catch:{ all -> 0x001a }
    L_0x0018:
        monitor-exit(r2);	 Catch:{ all -> 0x001a }
        return;	 Catch:{ all -> 0x001a }
    L_0x001a:
        r0 = move-exception;	 Catch:{ all -> 0x001a }
        monitor-exit(r2);	 Catch:{ all -> 0x001a }
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.data.DataHolder.close():void");
    }

    @KeepForSdk
    public DataHolder(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.zalw = true;
        this.zale = 1;
        this.zalp = (String[]) Preconditions.checkNotNull(strArr);
        this.zalr = (CursorWindow[]) Preconditions.checkNotNull(cursorWindowArr);
        this.zals = i;
        this.zalt = bundle;
        zaca();
    }

    private DataHolder(CursorWrapper cursorWrapper, int i, Bundle bundle) {
        this(cursorWrapper.getColumnNames(), zaa(cursorWrapper), i, bundle);
    }

    @KeepForSdk
    public DataHolder(Cursor cursor, int i, Bundle bundle) {
        this(new CursorWrapper(cursor), i, bundle);
    }

    private DataHolder(Builder builder, int i, Bundle bundle) {
        this(builder.zalp, zaa(builder, -1), i, null);
    }

    private DataHolder(Builder builder, int i, Bundle bundle, int i2) {
        this(builder.zalp, zaa(builder, -1), i, bundle);
    }

    public final void zaca() {
        this.zalq = new Bundle();
        int i = 0;
        int i2 = 0;
        while (true) {
            String[] strArr = this.zalp;
            if (i2 >= strArr.length) {
                break;
            }
            this.zalq.putInt(strArr[i2], i2);
            i2++;
        }
        this.zalu = new int[this.zalr.length];
        i2 = 0;
        while (true) {
            CursorWindow[] cursorWindowArr = this.zalr;
            if (i < cursorWindowArr.length) {
                this.zalu[i] = i2;
                i2 += this.zalr[i].getNumRows() - (i2 - cursorWindowArr[i].getStartPosition());
                i++;
            } else {
                this.zalv = i2;
                return;
            }
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringArray(parcel, 1, this.zalp, false);
        SafeParcelWriter.writeTypedArray(parcel, 2, this.zalr, i, false);
        SafeParcelWriter.writeInt(parcel, 3, getStatusCode());
        SafeParcelWriter.writeBundle(parcel, 4, getMetadata(), false);
        SafeParcelWriter.writeInt(parcel, 1000, this.zale);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
        if ((i & 1) != null) {
            close();
        }
    }

    @KeepForSdk
    public final int getStatusCode() {
        return this.zals;
    }

    @KeepForSdk
    public final Bundle getMetadata() {
        return this.zalt;
    }

    private static CursorWindow[] zaa(Builder builder, int i) {
        int i2 = 0;
        if (builder.zalp.length == 0) {
            return new CursorWindow[0];
        }
        int size;
        CursorWindow cursorWindow;
        ArrayList arrayList;
        CursorWindow cursorWindow2;
        int i3;
        Object obj;
        Map map;
        boolean z;
        int i4;
        String str;
        Object obj2;
        if (i >= 0) {
            if (i < builder.zaly.size()) {
                i = builder.zaly.subList(0, i);
                size = i.size();
                cursorWindow = new CursorWindow(false);
                arrayList = new ArrayList();
                arrayList.add(cursorWindow);
                cursorWindow.setNumColumns(builder.zalp.length);
                cursorWindow2 = cursorWindow;
                i3 = 0;
                obj = null;
                while (i3 < size) {
                    try {
                        if (!cursorWindow2.allocRow()) {
                            StringBuilder stringBuilder = new StringBuilder(72);
                            stringBuilder.append("Allocating additional cursor window for large data set (row ");
                            stringBuilder.append(i3);
                            stringBuilder.append(")");
                            Log.d("DataHolder", stringBuilder.toString());
                            cursorWindow2 = new CursorWindow(false);
                            cursorWindow2.setStartPosition(i3);
                            cursorWindow2.setNumColumns(builder.zalp.length);
                            arrayList.add(cursorWindow2);
                            if (!cursorWindow2.allocRow()) {
                                Log.e("DataHolder", "Unable to allocate row to hold data.");
                                arrayList.remove(cursorWindow2);
                                return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
                            }
                        }
                        map = (Map) i.get(i3);
                        z = true;
                        for (i4 = 0; i4 < builder.zalp.length && z; i4++) {
                            str = builder.zalp[i4];
                            obj2 = map.get(str);
                            if (obj2 == null) {
                                z = cursorWindow2.putNull(i3, i4);
                            } else if (obj2 instanceof String) {
                                z = cursorWindow2.putString((String) obj2, i3, i4);
                            } else if (obj2 instanceof Long) {
                                z = cursorWindow2.putLong(((Long) obj2).longValue(), i3, i4);
                            } else if (obj2 instanceof Integer) {
                                z = cursorWindow2.putLong((long) ((Integer) obj2).intValue(), i3, i4);
                            } else if (obj2 instanceof Boolean) {
                                z = cursorWindow2.putLong(((Boolean) obj2).booleanValue() ? 1 : 0, i3, i4);
                            } else if (obj2 instanceof byte[]) {
                                z = cursorWindow2.putBlob((byte[]) obj2, i3, i4);
                            } else if (obj2 instanceof Double) {
                                z = cursorWindow2.putDouble(((Double) obj2).doubleValue(), i3, i4);
                            } else if (obj2 instanceof Float) {
                                i = String.valueOf(obj2);
                                StringBuilder stringBuilder2 = new StringBuilder((String.valueOf(str).length() + 32) + String.valueOf(i).length());
                                stringBuilder2.append("Unsupported object for column ");
                                stringBuilder2.append(str);
                                stringBuilder2.append(": ");
                                stringBuilder2.append(i);
                                throw new IllegalArgumentException(stringBuilder2.toString());
                            } else {
                                z = cursorWindow2.putDouble((double) ((Float) obj2).floatValue(), i3, i4);
                            }
                        }
                        if (!z) {
                            obj = null;
                        } else if (obj != null) {
                            StringBuilder stringBuilder3 = new StringBuilder(74);
                            stringBuilder3.append("Couldn't populate window data for row ");
                            stringBuilder3.append(i3);
                            stringBuilder3.append(" - allocating new window.");
                            Log.d("DataHolder", stringBuilder3.toString());
                            cursorWindow2.freeLastRow();
                            cursorWindow2 = new CursorWindow(false);
                            cursorWindow2.setStartPosition(i3);
                            cursorWindow2.setNumColumns(builder.zalp.length);
                            arrayList.add(cursorWindow2);
                            i3--;
                            obj = 1;
                        } else {
                            throw new zaa("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
                        }
                        i3++;
                    } catch (Builder builder2) {
                        i = arrayList.size();
                        while (i2 < i) {
                            ((CursorWindow) arrayList.get(i2)).close();
                            i2++;
                        }
                        throw builder2;
                    }
                }
                return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
            }
        }
        i = builder2.zaly;
        size = i.size();
        cursorWindow = new CursorWindow(false);
        arrayList = new ArrayList();
        arrayList.add(cursorWindow);
        cursorWindow.setNumColumns(builder2.zalp.length);
        cursorWindow2 = cursorWindow;
        i3 = 0;
        obj = null;
        while (i3 < size) {
            if (cursorWindow2.allocRow()) {
                StringBuilder stringBuilder4 = new StringBuilder(72);
                stringBuilder4.append("Allocating additional cursor window for large data set (row ");
                stringBuilder4.append(i3);
                stringBuilder4.append(")");
                Log.d("DataHolder", stringBuilder4.toString());
                cursorWindow2 = new CursorWindow(false);
                cursorWindow2.setStartPosition(i3);
                cursorWindow2.setNumColumns(builder2.zalp.length);
                arrayList.add(cursorWindow2);
                if (cursorWindow2.allocRow()) {
                    Log.e("DataHolder", "Unable to allocate row to hold data.");
                    arrayList.remove(cursorWindow2);
                    return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
                }
            }
            map = (Map) i.get(i3);
            z = true;
            for (i4 = 0; i4 < builder2.zalp.length; i4++) {
                str = builder2.zalp[i4];
                obj2 = map.get(str);
                if (obj2 == null) {
                    z = cursorWindow2.putNull(i3, i4);
                } else if (obj2 instanceof String) {
                    z = cursorWindow2.putString((String) obj2, i3, i4);
                } else if (obj2 instanceof Long) {
                    z = cursorWindow2.putLong(((Long) obj2).longValue(), i3, i4);
                } else if (obj2 instanceof Integer) {
                    z = cursorWindow2.putLong((long) ((Integer) obj2).intValue(), i3, i4);
                } else if (obj2 instanceof Boolean) {
                    if (((Boolean) obj2).booleanValue()) {
                    }
                    z = cursorWindow2.putLong(((Boolean) obj2).booleanValue() ? 1 : 0, i3, i4);
                } else if (obj2 instanceof byte[]) {
                    z = cursorWindow2.putBlob((byte[]) obj2, i3, i4);
                } else if (obj2 instanceof Double) {
                    z = cursorWindow2.putDouble(((Double) obj2).doubleValue(), i3, i4);
                } else if (obj2 instanceof Float) {
                    i = String.valueOf(obj2);
                    StringBuilder stringBuilder22 = new StringBuilder((String.valueOf(str).length() + 32) + String.valueOf(i).length());
                    stringBuilder22.append("Unsupported object for column ");
                    stringBuilder22.append(str);
                    stringBuilder22.append(": ");
                    stringBuilder22.append(i);
                    throw new IllegalArgumentException(stringBuilder22.toString());
                } else {
                    z = cursorWindow2.putDouble((double) ((Float) obj2).floatValue(), i3, i4);
                }
            }
            if (!z) {
                obj = null;
            } else if (obj != null) {
                throw new zaa("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
            } else {
                StringBuilder stringBuilder32 = new StringBuilder(74);
                stringBuilder32.append("Couldn't populate window data for row ");
                stringBuilder32.append(i3);
                stringBuilder32.append(" - allocating new window.");
                Log.d("DataHolder", stringBuilder32.toString());
                cursorWindow2.freeLastRow();
                cursorWindow2 = new CursorWindow(false);
                cursorWindow2.setStartPosition(i3);
                cursorWindow2.setNumColumns(builder2.zalp.length);
                arrayList.add(cursorWindow2);
                i3--;
                obj = 1;
            }
            i3++;
        }
        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
    }

    private final void zaa(String str, int i) {
        Bundle bundle = this.zalq;
        if (bundle != null) {
            if (bundle.containsKey(str)) {
                if (isClosed() != null) {
                    throw new IllegalArgumentException("Buffer is closed.");
                } else if (i < 0 || i >= this.zalv) {
                    throw new CursorIndexOutOfBoundsException(i, this.zalv);
                } else {
                    return;
                }
            }
        }
        String str2 = "No such column: ";
        str = String.valueOf(str);
        throw new IllegalArgumentException(str.length() != 0 ? str2.concat(str) : new String(str2));
    }

    @KeepForSdk
    public final boolean hasColumn(String str) {
        return this.zalq.containsKey(str);
    }

    @KeepForSdk
    public final long getLong(String str, int i, int i2) {
        zaa(str, i);
        return this.zalr[i2].getLong(i, this.zalq.getInt(str));
    }

    @KeepForSdk
    public final int getInteger(String str, int i, int i2) {
        zaa(str, i);
        return this.zalr[i2].getInt(i, this.zalq.getInt(str));
    }

    @KeepForSdk
    public final String getString(String str, int i, int i2) {
        zaa(str, i);
        return this.zalr[i2].getString(i, this.zalq.getInt(str));
    }

    @KeepForSdk
    public final boolean getBoolean(String str, int i, int i2) {
        zaa(str, i);
        return Long.valueOf(this.zalr[i2].getLong(i, this.zalq.getInt(str))).longValue() == 1 ? true : null;
    }

    public final float zaa(String str, int i, int i2) {
        zaa(str, i);
        return this.zalr[i2].getFloat(i, this.zalq.getInt(str));
    }

    public final double zab(String str, int i, int i2) {
        zaa(str, i);
        return this.zalr[i2].getDouble(i, this.zalq.getInt(str));
    }

    @KeepForSdk
    public final byte[] getByteArray(String str, int i, int i2) {
        zaa(str, i);
        return this.zalr[i2].getBlob(i, this.zalq.getInt(str));
    }

    public final void zaa(String str, int i, int i2, CharArrayBuffer charArrayBuffer) {
        zaa(str, i);
        this.zalr[i2].copyStringToBuffer(i, this.zalq.getInt(str), charArrayBuffer);
    }

    @KeepForSdk
    public final boolean hasNull(String str, int i, int i2) {
        zaa(str, i);
        return this.zalr[i2].isNull(i, this.zalq.getInt(str));
    }

    @KeepForSdk
    public final int getCount() {
        return this.zalv;
    }

    @KeepForSdk
    public final int getWindowIndex(int i) {
        int i2 = 0;
        boolean z = i >= 0 && i < this.zalv;
        Preconditions.checkState(z);
        while (true) {
            int[] iArr = this.zalu;
            if (i2 >= iArr.length) {
                break;
            } else if (i < iArr[i2]) {
                break;
            } else {
                i2++;
            }
        }
        i2--;
        if (i2 == this.zalu.length) {
            return i2 - 1;
        }
        return i2;
    }

    @KeepForSdk
    public final boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.zalw && this.zalr.length > 0 && !isClosed()) {
                close();
                String obj = toString();
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(obj).length() + 178);
                stringBuilder.append("Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (internal object: ");
                stringBuilder.append(obj);
                stringBuilder.append(")");
                Log.e("DataBuffer", stringBuilder.toString());
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    @KeepForSdk
    public static Builder builder(String[] strArr) {
        return new Builder(strArr);
    }

    @KeepForSdk
    public static DataHolder empty(int i) {
        return new DataHolder(zalx, i, null);
    }
}
