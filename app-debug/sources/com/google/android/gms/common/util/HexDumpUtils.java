package com.google.android.gms.common.util;

import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public final class HexDumpUtils {
    @KeepForSdk
    public static String dump(byte[] bArr, int i, int i2, boolean z) {
        if (bArr != null && bArr.length != 0 && i >= 0 && i2 > 0) {
            if (i + i2 <= bArr.length) {
                int i3 = 57;
                if (z) {
                    i3 = 75;
                }
                StringBuilder stringBuilder = new StringBuilder(i3 * (((i2 + 16) - 1) / 16));
                int i4 = i;
                i = i2;
                int i5 = 0;
                int i6 = 0;
                while (i > 0) {
                    if (i5 == 0) {
                        if (i2 < 65536) {
                            stringBuilder.append(String.format("%04X:", new Object[]{Integer.valueOf(i4)}));
                        } else {
                            stringBuilder.append(String.format("%08X:", new Object[]{Integer.valueOf(i4)}));
                        }
                        i6 = i4;
                    } else if (i5 == 8) {
                        stringBuilder.append(" -");
                    }
                    stringBuilder.append(String.format(" %02X", new Object[]{Integer.valueOf(bArr[i4] & 255)}));
                    i--;
                    i5++;
                    if (z && (i5 == 16 || i == 0)) {
                        int i7 = 16 - i5;
                        if (i7 > 0) {
                            for (int i8 = 0; i8 < i7; i8++) {
                                stringBuilder.append("   ");
                            }
                        }
                        if (i7 >= 8) {
                            stringBuilder.append("  ");
                        }
                        stringBuilder.append("  ");
                        for (int i9 = 0; i9 < i5; i9++) {
                            char c = (char) bArr[i6 + i9];
                            if (c < ' ' || c > '~') {
                                c = '.';
                            }
                            stringBuilder.append(c);
                        }
                    }
                    if (i5 == 16 || i == 0) {
                        stringBuilder.append('\n');
                        i5 = 0;
                    }
                    i4++;
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }
}
