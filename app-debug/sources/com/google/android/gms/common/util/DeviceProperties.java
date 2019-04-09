package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public final class DeviceProperties {
    private static Boolean zzgl;
    private static Boolean zzgm;
    private static Boolean zzgn;
    private static Boolean zzgo;
    private static Boolean zzgp;
    private static Boolean zzgq;
    private static Boolean zzgr;
    private static Boolean zzgs;

    private DeviceProperties() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.android.gms.common.annotation.KeepForSdk
    public static boolean isTablet(android.content.res.Resources r4) {
        /*
        r0 = 0;
        if (r4 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = zzgl;
        if (r1 != 0) goto L_0x0046;
        r1 = r4.getConfiguration();
        r1 = r1.screenLayout;
        r1 = r1 & 15;
        r2 = 3;
        r3 = 1;
        if (r1 <= r2) goto L_0x0017;
    L_0x0015:
        r1 = 1;
        goto L_0x0018;
    L_0x0017:
        r1 = 0;
    L_0x0018:
        if (r1 != 0) goto L_0x003f;
    L_0x001a:
        r1 = zzgm;
        if (r1 != 0) goto L_0x0037;
    L_0x001e:
        r4 = r4.getConfiguration();
        r1 = r4.screenLayout;
        r1 = r1 & 15;
        if (r1 > r2) goto L_0x0030;
    L_0x0028:
        r4 = r4.smallestScreenWidthDp;
        r1 = 600; // 0x258 float:8.41E-43 double:2.964E-321;
        if (r4 < r1) goto L_0x0030;
    L_0x002e:
        r4 = 1;
        goto L_0x0031;
    L_0x0030:
        r4 = 0;
    L_0x0031:
        r4 = java.lang.Boolean.valueOf(r4);
        zzgm = r4;
    L_0x0037:
        r4 = zzgm;
        r4 = r4.booleanValue();
        if (r4 == 0) goto L_0x0040;
    L_0x003f:
        r0 = 1;
    L_0x0040:
        r4 = java.lang.Boolean.valueOf(r0);
        zzgl = r4;
    L_0x0046:
        r4 = zzgl;
        r4 = r4.booleanValue();
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.DeviceProperties.isTablet(android.content.res.Resources):boolean");
    }

    @TargetApi(20)
    @KeepForSdk
    public static boolean isWearable(Context context) {
        if (zzgn == null) {
            context = (!PlatformVersion.isAtLeastKitKatWatch() || context.getPackageManager().hasSystemFeature("android.hardware.type.watch") == null) ? null : true;
            zzgn = Boolean.valueOf(context);
        }
        return zzgn.booleanValue();
    }

    @TargetApi(26)
    @KeepForSdk
    public static boolean isWearableWithoutPlayStore(Context context) {
        return (!isWearable(context) || (PlatformVersion.isAtLeastN() && (isSidewinder(context) == null || PlatformVersion.isAtLeastO() != null))) ? null : true;
    }

    @TargetApi(21)
    @KeepForSdk
    public static boolean isSidewinder(Context context) {
        if (zzgo == null) {
            context = (!PlatformVersion.isAtLeastLollipop() || context.getPackageManager().hasSystemFeature("cn.google") == null) ? null : true;
            zzgo = Boolean.valueOf(context);
        }
        return zzgo.booleanValue();
    }

    @KeepForSdk
    public static boolean isLatchsky(Context context) {
        if (zzgp == null) {
            context = context.getPackageManager();
            context = (!context.hasSystemFeature("com.google.android.feature.services_updater") || context.hasSystemFeature("cn.google.services") == null) ? null : true;
            zzgp = Boolean.valueOf(context);
        }
        return zzgp.booleanValue();
    }

    public static boolean zzf(Context context) {
        if (zzgq == null) {
            if (!context.getPackageManager().hasSystemFeature("android.hardware.type.iot")) {
                if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded") == null) {
                    context = null;
                    zzgq = Boolean.valueOf(context);
                }
            }
            context = true;
            zzgq = Boolean.valueOf(context);
        }
        return zzgq.booleanValue();
    }

    @KeepForSdk
    public static boolean isAuto(Context context) {
        if (zzgr == null) {
            context = (!PlatformVersion.isAtLeastO() || context.getPackageManager().hasSystemFeature("android.hardware.type.automotive") == null) ? null : true;
            zzgr = Boolean.valueOf(context);
        }
        return zzgr.booleanValue();
    }

    @KeepForSdk
    public static boolean isTv(Context context) {
        if (zzgs == null) {
            context = context.getPackageManager();
            if (!(context.hasSystemFeature("com.google.android.tv") || context.hasSystemFeature("android.hardware.type.television"))) {
                if (context.hasSystemFeature("android.software.leanback") == null) {
                    context = null;
                    zzgs = Boolean.valueOf(context);
                }
            }
            context = true;
            zzgs = Boolean.valueOf(context);
        }
        return zzgs.booleanValue();
    }

    @KeepForSdk
    public static boolean isUserBuild() {
        return "user".equals(Build.TYPE);
    }
}
