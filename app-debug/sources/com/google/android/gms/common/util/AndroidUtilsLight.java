package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.wrappers.Wrappers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@KeepForSdk
public class AndroidUtilsLight {
    private static volatile int zzgd = -1;

    @KeepForSdk
    public static byte[] getPackageCertificateHashBytes(Context context, String str) throws NameNotFoundException {
        String str2 = "SHA1";
        context = Wrappers.packageManager(context).getPackageInfo(str, 64);
        if (context.signatures != null && context.signatures.length == 1) {
            str = zzi(str2);
            if (str != null) {
                return str.digest(context.signatures[0].toByteArray());
            }
        }
        return null;
    }

    public static MessageDigest zzi(String str) {
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest instance = MessageDigest.getInstance(str);
                if (instance != null) {
                    return instance;
                }
                i++;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return null;
    }

    @TargetApi(24)
    @KeepForSdk
    public static Context getDeviceProtectedStorageContext(Context context) {
        if (!PlatformVersion.isAtLeastN() || context.isDeviceProtectedStorage()) {
            return context;
        }
        return context.createDeviceProtectedStorageContext();
    }
}
