package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.reflect.Method;

public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static final Object lock = new Object();
    private static final GoogleApiAvailabilityLight zziu = GoogleApiAvailabilityLight.getInstance();
    private static Method zziv = null;

    public interface ProviderInstallListener {
        void onProviderInstallFailed(int i, Intent intent);

        void onProviderInstalled();
    }

    public static void installIfNeeded(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        Preconditions.checkNotNull(context, "Context must not be null");
        zziu.verifyGooglePlayServicesIsAvailable(context, 11925000);
        try {
            context = GooglePlayServicesUtilLight.getRemoteContext(context);
            if (context == null) {
                if (Log.isLoggable("ProviderInstaller", 6) != null) {
                    Log.e("ProviderInstaller", "Failed to get remote context");
                }
                throw new GooglePlayServicesNotAvailableException(8);
            }
            synchronized (lock) {
                try {
                    if (zziv == null) {
                        zziv = context.getClassLoader().loadClass("com.google.android.gms.common.security.ProviderInstallerImpl").getMethod("insertProvider", new Class[]{Context.class});
                    }
                    zziv.invoke(null, new Object[]{context});
                } catch (Context context2) {
                    Throwable cause = context2.getCause();
                    if (Log.isLoggable("ProviderInstaller", 6)) {
                        context2 = cause == null ? context2.getMessage() : cause.getMessage();
                        String str = "ProviderInstaller";
                        String str2 = "Failed to install provider: ";
                        context2 = String.valueOf(context2);
                        Log.e(str, context2.length() != 0 ? str2.concat(context2) : new String(str2));
                    }
                    throw new GooglePlayServicesNotAvailableException(8);
                }
            }
        } catch (NotFoundException e) {
            if (Log.isLoggable("ProviderInstaller", 6) != null) {
                Log.e("ProviderInstaller", "Failed to get remote context - resource not found");
            }
            throw new GooglePlayServicesNotAvailableException(8);
        }
    }

    public static void installIfNeededAsync(Context context, ProviderInstallListener providerInstallListener) {
        Preconditions.checkNotNull(context, "Context must not be null");
        Preconditions.checkNotNull(providerInstallListener, "Listener must not be null");
        Preconditions.checkMainThread("Must be called on the UI thread");
        new zza(context, providerInstallListener).execute(new Void[null]);
    }
}
