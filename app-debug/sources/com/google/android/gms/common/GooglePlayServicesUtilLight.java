package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller.SessionInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.UserManager;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.HideFirstParty;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.util.ClientLibraryUtils;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.util.zzb;
import java.util.concurrent.atomic.AtomicBoolean;

@ShowFirstParty
@KeepForSdk
public class GooglePlayServicesUtilLight {
    @KeepForSdk
    static final int GMS_AVAILABILITY_NOTIFICATION_ID = 10436;
    @KeepForSdk
    static final int GMS_GENERAL_ERROR_NOTIFICATION_ID = 39789;
    @KeepForSdk
    public static final String GOOGLE_PLAY_GAMES_PACKAGE = "com.google.android.play.games";
    @KeepForSdk
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @KeepForSdk
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = 12451000;
    @KeepForSdk
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    @KeepForSdk
    @VisibleForTesting
    static final AtomicBoolean sCanceledAvailabilityNotification = new AtomicBoolean();
    @VisibleForTesting
    private static boolean zzag = false;
    @VisibleForTesting
    private static boolean zzah = false;
    private static boolean zzai = false;
    @VisibleForTesting
    private static boolean zzaj = false;
    private static final AtomicBoolean zzak = new AtomicBoolean();

    @ShowFirstParty
    @KeepForSdk
    public static void enableUsingApkIndependentContext() {
        zzak.set(true);
    }

    @KeepForSdk
    GooglePlayServicesUtilLight() {
    }

    @KeepForSdk
    @Deprecated
    @VisibleForTesting
    public static String getErrorString(int i) {
        return ConnectionResult.zza(i);
    }

    @KeepForSdk
    @Deprecated
    @HideFirstParty
    public static int isGooglePlayServicesAvailable(Context context) {
        return isGooglePlayServicesAvailable(context, GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    @KeepForSdk
    @Deprecated
    public static int isGooglePlayServicesAvailable(Context context, int i) {
        try {
            context.getResources().getString(C0236R.string.common_google_play_services_unknown_issue);
        } catch (Throwable th) {
            Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        if (!("com.google.android.gms".equals(context.getPackageName()) || zzak.get())) {
            int zzd = zzp.zzd(context);
            if (zzd != 0) {
                int i2 = GOOGLE_PLAY_SERVICES_VERSION_CODE;
                if (zzd != i2) {
                    StringBuilder stringBuilder = new StringBuilder(320);
                    stringBuilder.append("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected ");
                    stringBuilder.append(i2);
                    stringBuilder.append(" but found ");
                    stringBuilder.append(zzd);
                    stringBuilder.append(".  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
                    throw new IllegalStateException(stringBuilder.toString());
                }
            } else {
                throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
            }
        }
        boolean z = (DeviceProperties.isWearableWithoutPlayStore(context) || DeviceProperties.zzf(context)) ? false : true;
        return zza(context, z, i);
    }

    @VisibleForTesting
    private static int zza(Context context, boolean z, int i) {
        Preconditions.checkArgument(i >= 0);
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        if (z) {
            try {
                packageInfo = packageManager.getPackageInfo("com.android.vending", 8256);
            } catch (NameNotFoundException e) {
                Log.w("GooglePlayServicesUtil", "Google Play Store is missing.");
                return 9;
            }
        }
        try {
            PackageInfo packageInfo2 = packageManager.getPackageInfo("com.google.android.gms", 64);
            GoogleSignatureVerifier.getInstance(context);
            if (GoogleSignatureVerifier.zza(packageInfo2, true) == null) {
                Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                return 9;
            } else if (z && (GoogleSignatureVerifier.zza(r3, true) == null || r3.signatures[0].equals(packageInfo2.signatures[0]) == null)) {
                Log.w("GooglePlayServicesUtil", "Google Play Store signature invalid.");
                return 9;
            } else if (zzb.zzc(packageInfo2.versionCode) < zzb.zzc(i)) {
                z = packageInfo2.versionCode;
                StringBuilder stringBuilder = new StringBuilder(77);
                stringBuilder.append("Google Play services out of date.  Requires ");
                stringBuilder.append(i);
                stringBuilder.append(" but found ");
                stringBuilder.append(z);
                Log.w("GooglePlayServicesUtil", stringBuilder.toString());
                return 2;
            } else {
                context = packageInfo2.applicationInfo;
                if (context == null) {
                    try {
                        context = packageManager.getApplicationInfo("com.google.android.gms", 0);
                    } catch (Context context2) {
                        Log.wtf("GooglePlayServicesUtil", "Google Play services missing when getting application info.", context2);
                        return 1;
                    }
                }
                if (context2.enabled == null) {
                    return 3;
                }
                return 0;
            }
        } catch (NameNotFoundException e2) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 1;
        }
    }

    @KeepForSdk
    @Deprecated
    public static void ensurePlayServicesAvailable(Context context, int i) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        i = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context, i);
        if (i != 0) {
            context = GoogleApiAvailabilityLight.getInstance().getErrorResolutionIntent(context, i, "e");
            StringBuilder stringBuilder = new StringBuilder(57);
            stringBuilder.append("GooglePlayServices not available due to error ");
            stringBuilder.append(i);
            Log.e("GooglePlayServicesUtil", stringBuilder.toString());
            if (context == null) {
                throw new GooglePlayServicesNotAvailableException(i);
            }
            throw new GooglePlayServicesRepairableException(i, "Google Play Services not available", context);
        }
    }

    @KeepForSdk
    @Deprecated
    public static boolean isGooglePlayServicesUid(Context context, int i) {
        return UidVerifier.isGooglePlayServicesUid(context, i);
    }

    @TargetApi(19)
    @KeepForSdk
    @Deprecated
    public static boolean uidHasPackageName(Context context, int i, String str) {
        return UidVerifier.uidHasPackageName(context, i, str);
    }

    @ShowFirstParty
    @KeepForSdk
    @Deprecated
    public static Intent getGooglePlayServicesAvailabilityRecoveryIntent(int i) {
        return GoogleApiAvailabilityLight.getInstance().getErrorResolutionIntent(null, i, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.android.gms.common.internal.ShowFirstParty
    @com.google.android.gms.common.annotation.KeepForSdk
    public static boolean honorsDebugCertificates(android.content.Context r5) {
        /*
        r0 = zzaj;
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x003e;
        r0 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r5);	 Catch:{ NameNotFoundException -> 0x0030 }
        r3 = "com.google.android.gms";
        r4 = 64;
        r0 = r0.getPackageInfo(r3, r4);	 Catch:{ NameNotFoundException -> 0x0030 }
        com.google.android.gms.common.GoogleSignatureVerifier.getInstance(r5);	 Catch:{ NameNotFoundException -> 0x0030 }
        if (r0 == 0) goto L_0x0029;
    L_0x001a:
        r5 = com.google.android.gms.common.GoogleSignatureVerifier.zza(r0, r1);	 Catch:{ NameNotFoundException -> 0x0030 }
        if (r5 != 0) goto L_0x0029;
    L_0x0020:
        r5 = com.google.android.gms.common.GoogleSignatureVerifier.zza(r0, r2);	 Catch:{ NameNotFoundException -> 0x0030 }
        if (r5 == 0) goto L_0x0029;
    L_0x0026:
        zzai = r2;	 Catch:{ NameNotFoundException -> 0x0030 }
        goto L_0x002b;
    L_0x0029:
        zzai = r1;	 Catch:{ NameNotFoundException -> 0x0030 }
    L_0x002b:
        zzaj = r2;
        goto L_0x003e;
    L_0x002e:
        r5 = move-exception;
        goto L_0x003b;
    L_0x0030:
        r5 = move-exception;
        r0 = "GooglePlayServicesUtil";
        r3 = "Cannot find Google Play services package name.";
        android.util.Log.w(r0, r3, r5);	 Catch:{ all -> 0x002e }
        zzaj = r2;
        goto L_0x003e;
    L_0x003b:
        zzaj = r2;
        throw r5;
    L_0x003e:
        r5 = zzai;
        if (r5 != 0) goto L_0x004a;
    L_0x0042:
        r5 = com.google.android.gms.common.util.DeviceProperties.isUserBuild();
        if (r5 != 0) goto L_0x0049;
    L_0x0048:
        goto L_0x004a;
    L_0x0049:
        return r1;
    L_0x004a:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.honorsDebugCertificates(android.content.Context):boolean");
    }

    @KeepForSdk
    @Deprecated
    public static PendingIntent getErrorPendingIntent(int i, Context context, int i2) {
        return GoogleApiAvailabilityLight.getInstance().getErrorResolutionPendingIntent(context, i, i2);
    }

    @KeepForSdk
    @Deprecated
    public static void cancelAvailabilityErrorNotifications(Context context) {
        if (!sCanceledAvailabilityNotification.getAndSet(true)) {
            try {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                if (notificationManager != null) {
                    notificationManager.cancel(GMS_AVAILABILITY_NOTIFICATION_ID);
                }
            } catch (SecurityException e) {
            }
        }
    }

    @KeepForSdk
    @Deprecated
    public static boolean isUserRecoverableError(int i) {
        if (i != 9) {
            switch (i) {
                case 1:
                case 2:
                case 3:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @KeepForSdk
    public static Resources getRemoteResource(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    @KeepForSdk
    public static Context getRemoteContext(Context context) {
        try {
            return context.createPackageContext("com.google.android.gms", 3);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    @ShowFirstParty
    @KeepForSdk
    @Deprecated
    public static int getClientVersion(Context context) {
        Preconditions.checkState(true);
        return ClientLibraryUtils.getClientVersion(context, context.getPackageName());
    }

    @ShowFirstParty
    @KeepForSdk
    @Deprecated
    public static int getApkVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 0;
        }
    }

    @ShowFirstParty
    @KeepForSdk
    @Deprecated
    @VisibleForTesting
    public static boolean isSidewinderDevice(Context context) {
        return DeviceProperties.isSidewinder(context);
    }

    @ShowFirstParty
    @KeepForSdk
    @Deprecated
    public static boolean isPlayServicesPossiblyUpdating(Context context, int i) {
        if (i == 18) {
            return true;
        }
        if (i == 1) {
            return isUninstalledAppPossiblyUpdating(context, "com.google.android.gms");
        }
        return null;
    }

    @ShowFirstParty
    @KeepForSdk
    @Deprecated
    public static boolean isPlayStorePossiblyUpdating(Context context, int i) {
        if (i == 9) {
            return isUninstalledAppPossiblyUpdating(context, "com.android.vending");
        }
        return null;
    }

    @TargetApi(21)
    static boolean isUninstalledAppPossiblyUpdating(Context context, String str) {
        boolean equals = str.equals("com.google.android.gms");
        if (PlatformVersion.isAtLeastLollipop()) {
            try {
                for (SessionInfo appPackageName : context.getPackageManager().getPackageInstaller().getAllSessions()) {
                    if (str.equals(appPackageName.getAppPackageName())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        try {
            str = context.getPackageManager().getApplicationInfo(str, 8192);
            if (equals) {
                return str.enabled;
            }
            return str.enabled != null && isRestrictedUserProfile(context) == null;
        } catch (NameNotFoundException e2) {
            return false;
        }
    }

    @TargetApi(18)
    @KeepForSdk
    public static boolean isRestrictedUserProfile(Context context) {
        if (PlatformVersion.isAtLeastJellyBeanMR2()) {
            context = ((UserManager) context.getSystemService("user")).getApplicationRestrictions(context.getPackageName());
            if (!(context == null || "true".equals(context.getString("restricted_profile")) == null)) {
                return true;
            }
        }
        return null;
    }
}
