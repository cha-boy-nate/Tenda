package com.google.android.gms.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;
import android.os.WorkSource;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.wrappers.Wrappers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@KeepForSdk
public class WorkSourceUtil {
    private static final int zzhh = Process.myUid();
    private static final Method zzhi = zzw();
    private static final Method zzhj = zzx();
    private static final Method zzhk = zzy();
    private static final Method zzhl = zzz();
    private static final Method zzhm = zzaa();
    private static final Method zzhn = zzab();
    private static final Method zzho = zzac();

    private WorkSourceUtil() {
    }

    private static WorkSource zza(int i, String str) {
        WorkSource workSource = new WorkSource();
        zza(workSource, i, str);
        return workSource;
    }

    @Nullable
    @KeepForSdk
    public static WorkSource fromPackage(Context context, @Nullable String str) {
        if (!(context == null || context.getPackageManager() == null)) {
            if (str != null) {
                String str2;
                try {
                    context = Wrappers.packageManager(context).getApplicationInfo(str, 0);
                    if (context != null) {
                        return zza(context.uid, str);
                    }
                    context = "WorkSourceUtil";
                    str2 = "Could not get applicationInfo from package: ";
                    str = String.valueOf(str);
                    Log.e(context, str.length() != 0 ? str2.concat(str) : new String(str2));
                    return null;
                } catch (NameNotFoundException e) {
                    context = "WorkSourceUtil";
                    str2 = "Could not find package: ";
                    str = String.valueOf(str);
                    Log.e(context, str.length() != 0 ? str2.concat(str) : new String(str2));
                    return null;
                }
            }
        }
        return null;
    }

    private static void zza(WorkSource workSource, int i, @Nullable String str) {
        if (zzhj != null) {
            if (str == null) {
                str = "";
            }
            try {
                zzhj.invoke(workSource, new Object[]{Integer.valueOf(i), str});
                return;
            } catch (WorkSource workSource2) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", workSource2);
                return;
            }
        }
        str = zzhi;
        if (str != null) {
            try {
                str.invoke(workSource2, new Object[]{Integer.valueOf(i)});
            } catch (WorkSource workSource22) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", workSource22);
            }
        }
    }

    @KeepForSdk
    public static WorkSource fromPackageAndModuleExperimentalPi(Context context, String str, String str2) {
        if (context != null) {
            if (!(context.getPackageManager() == null || str2 == null)) {
                if (str != null) {
                    context = zzd(context, str);
                    if (context < null) {
                        return null;
                    }
                    WorkSource workSource = new WorkSource();
                    Method method = zzhn;
                    if (method != null) {
                        if (zzho != null) {
                            try {
                                Object invoke = method.invoke(workSource, new Object[0]);
                                if (context != zzhh) {
                                    zzho.invoke(invoke, new Object[]{Integer.valueOf(context), str});
                                }
                                zzho.invoke(invoke, new Object[]{Integer.valueOf(zzhh), str2});
                            } catch (Context context2) {
                                Log.w("WorkSourceUtil", "Unable to assign chained blame through WorkSource", context2);
                            }
                            return workSource;
                        }
                    }
                    zza(workSource, context2, str);
                    return workSource;
                }
            }
        }
        Log.w("WorkSourceUtil", "Unexpected null arguments");
        return null;
    }

    private static int zza(WorkSource workSource) {
        Method method = zzhk;
        if (method == null) {
            return 0;
        }
        try {
            return ((Integer) method.invoke(workSource, new Object[0])).intValue();
        } catch (WorkSource workSource2) {
            Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", workSource2);
        }
    }

    @Nullable
    private static String zza(WorkSource workSource, int i) {
        Method method = zzhm;
        if (method == null) {
            return null;
        }
        try {
            return (String) method.invoke(workSource, new Object[]{Integer.valueOf(i)});
        } catch (WorkSource workSource2) {
            Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", workSource2);
        }
    }

    @KeepForSdk
    public static List<String> getNames(@Nullable WorkSource workSource) {
        int zza = workSource == null ? 0 : zza(workSource);
        if (zza == 0) {
            return Collections.emptyList();
        }
        List<String> arrayList = new ArrayList();
        for (int i = 0; i < zza; i++) {
            String zza2 = zza(workSource, i);
            if (!Strings.isEmptyOrWhitespace(zza2)) {
                arrayList.add(zza2);
            }
        }
        return arrayList;
    }

    @KeepForSdk
    public static boolean hasWorkSourcePermission(Context context) {
        if (context == null || context.getPackageManager() == null || Wrappers.packageManager(context).checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) != null) {
            return false;
        }
        return true;
    }

    private static int zzd(Context context, String str) {
        String str2;
        try {
            context = Wrappers.packageManager(context).getApplicationInfo(str, 0);
            if (context != null) {
                return context.uid;
            }
            context = "WorkSourceUtil";
            str2 = "Could not get applicationInfo from package: ";
            str = String.valueOf(str);
            Log.e(context, str.length() != 0 ? str2.concat(str) : new String(str2));
            return -1;
        } catch (NameNotFoundException e) {
            context = "WorkSourceUtil";
            str2 = "Could not find package: ";
            str = String.valueOf(str);
            Log.e(context, str.length() != 0 ? str2.concat(str) : new String(str2));
            return -1;
        }
    }

    private static Method zzw() {
        try {
            return WorkSource.class.getMethod("add", new Class[]{Integer.TYPE});
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzx() {
        if (PlatformVersion.isAtLeastJellyBeanMR2()) {
            try {
                return WorkSource.class.getMethod("add", new Class[]{Integer.TYPE, String.class});
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static Method zzy() {
        try {
            return WorkSource.class.getMethod("size", new Class[0]);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzz() {
        try {
            return WorkSource.class.getMethod("get", new Class[]{Integer.TYPE});
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzaa() {
        if (PlatformVersion.isAtLeastJellyBeanMR2()) {
            try {
                return WorkSource.class.getMethod("getName", new Class[]{Integer.TYPE});
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static final Method zzab() {
        if (PlatformVersion.isAtLeastP()) {
            try {
                return WorkSource.class.getMethod("createWorkChain", new Class[0]);
            } catch (Throwable e) {
                Log.w("WorkSourceUtil", "Missing WorkChain API createWorkChain", e);
            }
        }
        return null;
    }

    @SuppressLint({"PrivateApi"})
    private static final Method zzac() {
        if (PlatformVersion.isAtLeastP()) {
            try {
                return Class.forName("android.os.WorkSource$WorkChain").getMethod("addNode", new Class[]{Integer.TYPE, String.class});
            } catch (Throwable e) {
                Log.w("WorkSourceUtil", "Missing WorkChain class", e);
            }
        }
        return null;
    }
}
