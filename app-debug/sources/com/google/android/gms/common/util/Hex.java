package com.google.android.gms.common.util;

import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.ShowFirstParty;

@ShowFirstParty
@KeepForSdk
public class Hex {
    private static final char[] zzgw = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] zzgx = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @com.google.android.gms.common.annotation.KeepForSdk
    public static byte[] stringToBytes(java.lang.String r6) throws java.lang.IllegalArgumentException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:8:0x002b in {4, 5, 7} preds:[]
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
        r0 = r6.length();
        r1 = r0 % 2;
        if (r1 != 0) goto L_0x0023;
    L_0x0008:
        r1 = r0 / 2;
        r1 = new byte[r1];
        r2 = 0;
    L_0x000d:
        if (r2 >= r0) goto L_0x0022;
    L_0x000f:
        r3 = r2 / 2;
        r4 = r2 + 2;
        r2 = r6.substring(r2, r4);
        r5 = 16;
        r2 = java.lang.Integer.parseInt(r2, r5);
        r2 = (byte) r2;
        r1[r3] = r2;
        r2 = r4;
        goto L_0x000d;
    L_0x0022:
        return r1;
    L_0x0023:
        r6 = new java.lang.IllegalArgumentException;
        r0 = "Hex string has odd number of characters";
        r6.<init>(r0);
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.Hex.stringToBytes(java.lang.String):byte[]");
    }

    @KeepForSdk
    public static String bytesToStringUppercase(byte[] bArr) {
        return bytesToStringUppercase(bArr, false);
    }

    @KeepForSdk
    public static String bytesToStringUppercase(byte[] bArr, boolean z) {
        int length = bArr.length;
        StringBuilder stringBuilder = new StringBuilder(length << 1);
        int i = 0;
        while (i < length && (!z || i != length - 1 || (bArr[i] & 255) != 0)) {
            stringBuilder.append(zzgw[(bArr[i] & 240) >>> 4]);
            stringBuilder.append(zzgw[bArr[i] & 15]);
            i++;
        }
        return stringBuilder.toString();
    }

    public static String zza(byte[] bArr) {
        char[] cArr = new char[(bArr.length << 1)];
        int i = 0;
        for (byte b : bArr) {
            int i2 = b & 255;
            int i3 = i + 1;
            char[] cArr2 = zzgx;
            cArr[i] = cArr2[i2 >>> 4];
            i = i3 + 1;
            cArr[i3] = cArr2[i2 & 15];
        }
        return new String(cArr);
    }
}
