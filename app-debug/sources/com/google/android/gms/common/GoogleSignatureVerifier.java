package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.wrappers.Wrappers;
import javax.annotation.CheckReturnValue;

@ShowFirstParty
@CheckReturnValue
@KeepForSdk
public class GoogleSignatureVerifier {
    private static GoogleSignatureVerifier zzal;
    private final Context mContext;
    private volatile String zzam;

    private GoogleSignatureVerifier(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @KeepForSdk
    public static GoogleSignatureVerifier getInstance(Context context) {
        Preconditions.checkNotNull(context);
        synchronized (GoogleSignatureVerifier.class) {
            if (zzal == null) {
                zzc.zza(context);
                zzal = new GoogleSignatureVerifier(context);
            }
        }
        return zzal;
    }

    @ShowFirstParty
    @KeepForSdk
    public boolean isUidGoogleSigned(int i) {
        zzm zzm;
        String[] packagesForUid = Wrappers.packageManager(this.mContext).getPackagesForUid(i);
        if (packagesForUid != null) {
            if (packagesForUid.length != 0) {
                zzm = null;
                for (String zza : packagesForUid) {
                    zzm = zza(zza, i);
                    if (zzm.zzac) {
                        break;
                    }
                }
                zzm.zzf();
                return zzm.zzac;
            }
        }
        zzm = zzm.zzb("no pkgs");
        zzm.zzf();
        return zzm.zzac;
    }

    @ShowFirstParty
    @KeepForSdk
    public boolean isPackageGoogleSigned(String str) {
        str = zzc(str);
        str.zzf();
        return str.zzac;
    }

    public static boolean zza(PackageInfo packageInfo, boolean z) {
        if (!(packageInfo == null || packageInfo.signatures == null)) {
            if (z) {
                packageInfo = zza(packageInfo, zzh.zzx);
            } else {
                packageInfo = zza(packageInfo, zzh.zzx[0]);
            }
            if (packageInfo != null) {
                return true;
            }
        }
        return false;
    }

    @KeepForSdk
    public boolean isGooglePublicSignedPackage(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (zza(packageInfo, false)) {
            return true;
        }
        if (zza(packageInfo, true) != null) {
            if (GooglePlayServicesUtilLight.honorsDebugCertificates(this.mContext) != null) {
                return true;
            }
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return false;
    }

    private final zzm zza(String str, int i) {
        try {
            str = zza(Wrappers.packageManager(this.mContext).zza(str, 64, i));
            return str;
        } catch (NameNotFoundException e) {
            i = "no pkg ";
            str = String.valueOf(str);
            return zzm.zzb(str.length() != 0 ? i.concat(str) : new String(i));
        }
    }

    private final zzm zzc(String str) {
        if (str == null) {
            return zzm.zzb("null pkg");
        }
        if (str.equals(this.zzam)) {
            return zzm.zze();
        }
        try {
            zzm zza = zza(Wrappers.packageManager(this.mContext).getPackageInfo(str, 64));
            if (zza.zzac) {
                this.zzam = str;
            }
            return zza;
        } catch (NameNotFoundException e) {
            String str2 = "no pkg ";
            str = String.valueOf(str);
            return zzm.zzb(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    private final zzm zza(PackageInfo packageInfo) {
        boolean honorsDebugCertificates = GooglePlayServicesUtilLight.honorsDebugCertificates(this.mContext);
        if (packageInfo == null) {
            return zzm.zzb("null pkg");
        }
        if (packageInfo.signatures.length != 1) {
            return zzm.zzb("single cert required");
        }
        zze zzf = new zzf(packageInfo.signatures[0].toByteArray());
        String str = packageInfo.packageName;
        zzm zza = zzc.zza(str, zzf, honorsDebugCertificates);
        if (!zza.zzac || packageInfo.applicationInfo == null || (packageInfo.applicationInfo.flags & 2) == null || (honorsDebugCertificates && zzc.zza(str, zzf, false).zzac == null)) {
            return zza;
        }
        return zzm.zzb("debuggable release cert app rejected");
    }

    private static zze zza(PackageInfo packageInfo, zze... zzeArr) {
        if (packageInfo.signatures == null) {
            return null;
        }
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        int i = 0;
        zzf zzf = new zzf(packageInfo.signatures[0].toByteArray());
        while (i < zzeArr.length) {
            if (zzeArr[i].equals(zzf) != null) {
                return zzeArr[i];
            }
            i++;
        }
        return null;
    }
}
