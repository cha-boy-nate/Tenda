package com.google.android.gms.maps.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class zzbz {
    private static final String TAG = zzbz.class.getSimpleName();
    @Nullable
    @SuppressLint({"StaticFieldLeak"})
    private static Context zzck = null;
    private static zze zzcl;

    public static zze zza(Context context) throws GooglePlayServicesNotAvailableException {
        Preconditions.checkNotNull(context);
        zze zze = zzcl;
        if (zze != null) {
            return zze;
        }
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context, 13400000);
        if (isGooglePlayServicesAvailable == 0) {
            Log.i(TAG, "Making Creator dynamically");
            IBinder iBinder = (IBinder) zza(zzb(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl");
            if (iBinder == null) {
                zze = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
                if (queryLocalInterface instanceof zze) {
                    zze = (zze) queryLocalInterface;
                } else {
                    zze = new zzf(iBinder);
                }
            }
            zzcl = zze;
            try {
                zzcl.zza(ObjectWrapper.wrap(zzb(context).getResources()), GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
                return zzcl;
            } catch (Context context2) {
                throw new RuntimeRemoteException(context2);
            }
        }
        throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
    }

    @Nullable
    private static Context zzb(Context context) {
        Context context2 = zzck;
        if (context2 != null) {
            return context2;
        }
        context = zzc(context);
        zzck = context;
        return context;
    }

    @Nullable
    private static Context zzc(Context context) {
        try {
            context = DynamiteModule.load(context, DynamiteModule.PREFER_REMOTE, "com.google.android.gms.maps_dynamite").getModuleContext();
            return context;
        } catch (Throwable e) {
            Log.e(TAG, "Failed to load maps module, use legacy", e);
            return GooglePlayServicesUtil.getRemoteContext(context);
        }
    }

    private static <T> T zza(ClassLoader classLoader, String str) {
        try {
            return zza(((ClassLoader) Preconditions.checkNotNull(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            String str2 = "Unable to find dynamic class ";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    private static <T> T zza(Class<?> cls) {
        String str;
        try {
            cls = cls.newInstance();
            return cls;
        } catch (InstantiationException e) {
            str = "Unable to instantiate the dynamic class ";
            cls = String.valueOf(cls.getName());
            throw new IllegalStateException(cls.length() != 0 ? str.concat(cls) : new String(str));
        } catch (IllegalAccessException e2) {
            str = "Unable to call the default constructor of ";
            cls = String.valueOf(cls.getName());
            throw new IllegalStateException(cls.length() != 0 ? str.concat(cls) : new String(str));
        }
    }
}
