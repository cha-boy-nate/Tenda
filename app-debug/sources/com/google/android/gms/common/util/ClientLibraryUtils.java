package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.Nullable;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.wrappers.Wrappers;

@KeepForSdk
public class ClientLibraryUtils {
    private ClientLibraryUtils() {
    }

    @KeepForSdk
    public static int getClientVersion(Context context, String str) {
        context = zzb(context, str);
        if (context != null) {
            if (context.applicationInfo != null) {
                context = context.applicationInfo.metaData;
                if (context == null) {
                    return -1;
                }
                return context.getInt("com.google.android.gms.version", -1);
            }
        }
        return -1;
    }

    @Nullable
    private static PackageInfo zzb(Context context, String str) {
        try {
            return Wrappers.packageManager(context).getPackageInfo(str, 128);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static boolean zzc(Context context, String str) {
        "com.google.android.gms".equals(str);
        try {
            if ((Wrappers.packageManager(context).getApplicationInfo(str, 0).flags & 2097152) != null) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    @KeepForSdk
    public static boolean isPackageSide() {
        return false;
    }
}
