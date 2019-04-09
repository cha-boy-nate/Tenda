package com.google.android.gms.common.util;

import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public class MurmurHash3 {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.android.gms.common.annotation.KeepForSdk
    public static int murmurhash3_x86_32(byte[] r5, int r6, int r7, int r8) {
        /*
        r0 = r7 & -4;
        r0 = r0 + r6;
    L_0x0005:
        r1 = 461845907; // 0x1b873593 float:2.2368498E-22 double:2.281821963E-315;
        r2 = -862048943; // 0xffffffffcc9e2d51 float:-8.2930312E7 double:NaN;
        if (r6 >= r0) goto L_0x0042;
    L_0x000d:
        r3 = r5[r6];
        r3 = r3 & 255;
        r4 = r6 + 1;
        r4 = r5[r4];
        r4 = r4 & 255;
        r4 = r4 << 8;
        r3 = r3 | r4;
        r4 = r6 + 2;
        r4 = r5[r4];
        r4 = r4 & 255;
        r4 = r4 << 16;
        r3 = r3 | r4;
        r4 = r6 + 3;
        r4 = r5[r4];
        r4 = r4 << 24;
        r3 = r3 | r4;
        r3 = r3 * r2;
        r2 = r3 << 15;
        r3 = r3 >>> 17;
        r2 = r2 | r3;
        r2 = r2 * r1;
        r8 = r8 ^ r2;
        r1 = r8 << 13;
        r8 = r8 >>> 19;
        r8 = r8 | r1;
        r8 = r8 * 5;
        r1 = -430675100; // 0xffffffffe6546b64 float:-2.5078068E23 double:NaN;
        r8 = r8 + r1;
        r6 = r6 + 4;
        goto L_0x0005;
    L_0x0042:
        r6 = 0;
        r3 = r7 & 3;
        switch(r3) {
            case 1: goto L_0x005a;
            case 2: goto L_0x0051;
            case 3: goto L_0x0049;
            default: goto L_0x0048;
        };
    L_0x0048:
        goto L_0x0069;
    L_0x0049:
        r6 = r0 + 2;
        r6 = r5[r6];
        r6 = r6 & 255;
        r6 = r6 << 16;
    L_0x0051:
        r3 = r0 + 1;
        r3 = r5[r3];
        r3 = r3 & 255;
        r3 = r3 << 8;
        r6 = r6 | r3;
    L_0x005a:
        r5 = r5[r0];
        r5 = r5 & 255;
        r5 = r5 | r6;
        r5 = r5 * r2;
        r6 = r5 << 15;
        r5 = r5 >>> 17;
        r5 = r5 | r6;
        r5 = r5 * r1;
        r8 = r8 ^ r5;
    L_0x0069:
        r5 = r8 ^ r7;
        r6 = r5 >>> 16;
        r5 = r5 ^ r6;
        r6 = -2048144789; // 0xffffffff85ebca6b float:-2.217365E-35 double:NaN;
        r5 = r5 * r6;
        r6 = r5 >>> 13;
        r5 = r5 ^ r6;
        r6 = -1028477387; // 0xffffffffc2b2ae35 float:-89.34025 double:NaN;
        r5 = r5 * r6;
        r6 = r5 >>> 16;
        r5 = r5 ^ r6;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.MurmurHash3.murmurhash3_x86_32(byte[], int, int, int):int");
    }

    private MurmurHash3() {
    }
}
