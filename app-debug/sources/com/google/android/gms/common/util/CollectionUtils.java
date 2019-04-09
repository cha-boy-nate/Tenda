package com.google.android.gms.common.util;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;
import com.google.android.gms.common.annotation.KeepForSdk;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@KeepForSdk
public final class CollectionUtils {
    private CollectionUtils() {
    }

    @com.google.android.gms.common.annotation.KeepForSdk
    public static <K, V> java.util.Map<K, V> mapOfKeyValueArrays(K[] r4, V[] r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:16:0x0055 in {6, 8, 11, 13, 15} preds:[]
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
        r0 = r4.length;
        r1 = r5.length;
        if (r0 != r1) goto L_0x0032;
    L_0x0005:
        r0 = r4.length;
        r1 = 0;
        switch(r0) {
            case 0: goto L_0x001a;
            case 1: goto L_0x0011;
            default: goto L_0x000a;
        };
    L_0x000a:
        r0 = r4.length;
        r0 = zzb(r0, r1);
        goto L_0x001f;
    L_0x0011:
        r4 = r4[r1];
        r5 = r5[r1];
        r4 = java.util.Collections.singletonMap(r4, r5);
        return r4;
    L_0x001a:
        r4 = java.util.Collections.emptyMap();
        return r4;
    L_0x001f:
        r2 = r4.length;
        if (r1 >= r2) goto L_0x002c;
    L_0x0022:
        r2 = r4[r1];
        r3 = r5[r1];
        r0.put(r2, r3);
        r1 = r1 + 1;
        goto L_0x001f;
        r4 = java.util.Collections.unmodifiableMap(r0);
        return r4;
    L_0x0032:
        r0 = new java.lang.IllegalArgumentException;
        r4 = r4.length;
        r5 = r5.length;
        r1 = 66;
        r2 = new java.lang.StringBuilder;
        r2.<init>(r1);
        r1 = "Key and values array lengths not equal: ";
        r2.append(r1);
        r2.append(r4);
        r4 = " != ";
        r2.append(r4);
        r2.append(r5);
        r4 = r2.toString();
        r0.<init>(r4);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.CollectionUtils.mapOfKeyValueArrays(java.lang.Object[], java.lang.Object[]):java.util.Map<K, V>");
    }

    @KeepForSdk
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return collection == null ? true : collection.isEmpty();
    }

    @KeepForSdk
    @Deprecated
    public static <T> List<T> listOf() {
        return Collections.emptyList();
    }

    @KeepForSdk
    @Deprecated
    public static <T> List<T> listOf(T t) {
        return Collections.singletonList(t);
    }

    @KeepForSdk
    @Deprecated
    public static <T> List<T> listOf(T... tArr) {
        switch (tArr.length) {
            case 0:
                return listOf();
            case 1:
                return listOf(tArr[0]);
            default:
                return Collections.unmodifiableList(Arrays.asList(tArr));
        }
    }

    private static <T> Set<T> zza(int i, boolean z) {
        float f = z ? 0.75f : 1.0f;
        if (i <= (z ? true : true)) {
            return new ArraySet(i);
        }
        return new HashSet(i, f);
    }

    @KeepForSdk
    @Deprecated
    public static <T> Set<T> setOf(T t, T t2, T t3) {
        Set zza = zza(3, false);
        zza.add(t);
        zza.add(t2);
        zza.add(t3);
        return Collections.unmodifiableSet(zza);
    }

    @KeepForSdk
    @Deprecated
    public static <T> Set<T> setOf(T... tArr) {
        Object obj;
        switch (tArr.length) {
            case 0:
                return Collections.emptySet();
            case 1:
                return Collections.singleton(tArr[0]);
            case 2:
                obj = tArr[0];
                tArr = tArr[1];
                Set zza = zza(2, false);
                zza.add(obj);
                zza.add(tArr);
                return Collections.unmodifiableSet(zza);
            case 3:
                return setOf(tArr[0], tArr[1], tArr[2]);
            case 4:
                obj = tArr[0];
                Object obj2 = tArr[1];
                Object obj3 = tArr[2];
                tArr = tArr[3];
                Set zza2 = zza(4, false);
                zza2.add(obj);
                zza2.add(obj2);
                zza2.add(obj3);
                zza2.add(tArr);
                return Collections.unmodifiableSet(zza2);
            default:
                obj = zza(tArr.length, false);
                Collections.addAll(obj, tArr);
                return Collections.unmodifiableSet(obj);
        }
    }

    @KeepForSdk
    public static <T> Set<T> mutableSetOfWithSize(int i) {
        if (i == 0) {
            return new ArraySet();
        }
        return zza(i, true);
    }

    private static <K, V> Map<K, V> zzb(int i, boolean z) {
        if (i <= true) {
            return new ArrayMap(i);
        }
        return new HashMap(i, 1.0f);
    }

    @KeepForSdk
    public static <K, V> Map<K, V> mapOf(K k, V v, K k2, V v2, K k3, V v3) {
        Map zzb = zzb(3, false);
        zzb.put(k, v);
        zzb.put(k2, v2);
        zzb.put(k3, v3);
        return Collections.unmodifiableMap(zzb);
    }

    @KeepForSdk
    public static <K, V> Map<K, V> mapOf(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map zzb = zzb(6, false);
        zzb.put(k, v);
        zzb.put(k2, v2);
        zzb.put(k3, v3);
        zzb.put(k4, v4);
        zzb.put(k5, v5);
        zzb.put(k6, v6);
        return Collections.unmodifiableMap(zzb);
    }
}
