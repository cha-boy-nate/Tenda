package com.google.android.gms.dynamite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CrashUtils;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.dynamic.ObjectWrapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public final class DynamiteModule {
    @KeepForSdk
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzd();
    @KeepForSdk
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zze();
    @KeepForSdk
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzf();
    @KeepForSdk
    public static final VersionPolicy PREFER_REMOTE = new zzb();
    @GuardedBy("DynamiteModule.class")
    private static Boolean zzid;
    @GuardedBy("DynamiteModule.class")
    private static zzi zzie;
    @GuardedBy("DynamiteModule.class")
    private static zzk zzif;
    @GuardedBy("DynamiteModule.class")
    private static String zzig;
    @GuardedBy("DynamiteModule.class")
    private static int zzih = -1;
    private static final ThreadLocal<zza> zzii = new ThreadLocal();
    private static final zza zzij = new zza();
    private static final VersionPolicy zzik = new zzc();
    private static final VersionPolicy zzil = new zzg();
    private final Context zzim;

    @DynamiteApi
    public static class DynamiteLoaderClassLoader {
        @GuardedBy("DynamiteLoaderClassLoader.class")
        public static ClassLoader sClassLoader;
    }

    @KeepForSdk
    public static class LoadingException extends Exception {
        private LoadingException(String str) {
            super(str);
        }

        private LoadingException(String str, Throwable th) {
            super(str, th);
        }
    }

    public interface VersionPolicy {

        public interface zza {
            int getLocalVersion(Context context, String str);

            int zza(Context context, String str, boolean z) throws LoadingException;
        }

        public static class zzb {
            public int zziq = 0;
            public int zzir = 0;
            public int zzis = 0;
        }

        zzb zza(Context context, String str, zza zza) throws LoadingException;
    }

    private static class zza {
        public Cursor zzin;

        private zza() {
        }
    }

    private static class zzb implements zza {
        private final int zzio;
        private final int zzip = 0;

        public zzb(int i, int i2) {
            this.zzio = i;
        }

        public final int zza(Context context, String str, boolean z) {
            return null;
        }

        public final int getLocalVersion(Context context, String str) {
            return this.zzio;
        }
    }

    @KeepForSdk
    public static DynamiteModule load(Context context, VersionPolicy versionPolicy, String str) throws LoadingException {
        zza zza = (zza) zzii.get();
        zza zza2 = new zza();
        zzii.set(zza2);
        zzb zza3;
        try {
            zza3 = versionPolicy.zza(context, str, zzij);
            int i = zza3.zziq;
            int i2 = zza3.zzir;
            StringBuilder stringBuilder = new StringBuilder((String.valueOf(str).length() + 68) + String.valueOf(str).length());
            stringBuilder.append("Considering local module ");
            stringBuilder.append(str);
            stringBuilder.append(":");
            stringBuilder.append(i);
            stringBuilder.append(" and remote module ");
            stringBuilder.append(str);
            stringBuilder.append(":");
            stringBuilder.append(i2);
            Log.i("DynamiteModule", stringBuilder.toString());
            if (zza3.zzis == 0 || ((zza3.zzis == -1 && zza3.zziq == 0) || (zza3.zzis == 1 && zza3.zzir == 0))) {
                versionPolicy = zza3.zziq;
                str = zza3.zzir;
                StringBuilder stringBuilder2 = new StringBuilder(91);
                stringBuilder2.append("No acceptable module found. Local version is ");
                stringBuilder2.append(versionPolicy);
                stringBuilder2.append(" and remote version is ");
                stringBuilder2.append(str);
                stringBuilder2.append(".");
                throw new LoadingException(stringBuilder2.toString());
            } else if (zza3.zzis == -1) {
                context = zze(context, str);
                return context;
            } else if (zza3.zzis == 1) {
                context = zza(context, str, zza3.zzir);
                return context;
            } else {
                versionPolicy = zza3.zzis;
                StringBuilder stringBuilder3 = new StringBuilder(47);
                stringBuilder3.append("VersionPolicy returned invalid code:");
                stringBuilder3.append(versionPolicy);
                throw new LoadingException(stringBuilder3.toString());
            }
        } catch (Throwable e) {
            String str2 = "DynamiteModule";
            String str3 = "Failed to load remote module: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            if (zza3.zziq == 0 || versionPolicy.zza(context, str, new zzb(zza3.zziq, 0)).zzis != -1) {
                throw new LoadingException("Remote load failed. No local fallback found.", e);
            }
            context = zze(context, str);
            return context;
        } finally {
            if (zza2.zzin != null) {
                zza2.zzin.close();
            }
            zzii.set(zza);
        }
    }

    @KeepForSdk
    public static int getLocalVersion(Context context, String str) {
        StringBuilder stringBuilder;
        String valueOf;
        try {
            context = context.getApplicationContext().getClassLoader();
            stringBuilder = new StringBuilder(String.valueOf(str).length() + 61);
            stringBuilder.append("com.google.android.gms.dynamite.descriptors.");
            stringBuilder.append(str);
            stringBuilder.append(".ModuleDescriptor");
            context = context.loadClass(stringBuilder.toString());
            Field declaredField = context.getDeclaredField("MODULE_ID");
            context = context.getDeclaredField("MODULE_VERSION");
            if (declaredField.get(null).equals(str)) {
                return context.getInt(null);
            }
            valueOf = String.valueOf(declaredField.get(null));
            StringBuilder stringBuilder2 = new StringBuilder((String.valueOf(valueOf).length() + 51) + String.valueOf(str).length());
            stringBuilder2.append("Module descriptor id '");
            stringBuilder2.append(valueOf);
            stringBuilder2.append("' didn't match expected id '");
            stringBuilder2.append(str);
            stringBuilder2.append("'");
            Log.e("DynamiteModule", stringBuilder2.toString());
            return 0;
        } catch (ClassNotFoundException e) {
            stringBuilder = new StringBuilder(String.valueOf(str).length() + 45);
            stringBuilder.append("Local module descriptor class for ");
            stringBuilder.append(str);
            stringBuilder.append(" not found.");
            Log.w("DynamiteModule", stringBuilder.toString());
            return 0;
        } catch (Context context2) {
            str = "DynamiteModule";
            valueOf = "Failed to load module descriptor class: ";
            context2 = String.valueOf(context2.getMessage());
            Log.e(str, context2.length() != 0 ? valueOf.concat(context2) : new String(valueOf));
            return 0;
        }
    }

    public static int zza(Context context, String str, boolean z) {
        Boolean bool;
        Boolean bool2;
        try {
            Object e;
            synchronized (DynamiteModule.class) {
                bool = zzid;
                if (bool == null) {
                    try {
                        Class loadClass = context.getApplicationContext().getClassLoader().loadClass(DynamiteLoaderClassLoader.class.getName());
                        Field declaredField = loadClass.getDeclaredField("sClassLoader");
                        synchronized (loadClass) {
                            ClassLoader classLoader = (ClassLoader) declaredField.get(null);
                            if (classLoader != null) {
                                if (classLoader == ClassLoader.getSystemClassLoader()) {
                                    bool2 = Boolean.FALSE;
                                } else {
                                    try {
                                        zza(classLoader);
                                    } catch (LoadingException e2) {
                                    }
                                    bool2 = Boolean.TRUE;
                                }
                            } else if ("com.google.android.gms".equals(context.getApplicationContext().getPackageName())) {
                                declaredField.set(null, ClassLoader.getSystemClassLoader());
                                bool2 = Boolean.FALSE;
                            } else {
                                try {
                                    int zzc = zzc(context, str, z);
                                    if (zzig != null) {
                                        if (!zzig.isEmpty()) {
                                            ClassLoader zzh = new zzh(zzig, ClassLoader.getSystemClassLoader());
                                            zza(zzh);
                                            declaredField.set(null, zzh);
                                            zzid = Boolean.TRUE;
                                            return zzc;
                                        }
                                    }
                                    return zzc;
                                } catch (LoadingException e3) {
                                    declaredField.set(null, ClassLoader.getSystemClassLoader());
                                    bool2 = Boolean.FALSE;
                                    bool = bool2;
                                    zzid = bool;
                                    if (bool.booleanValue()) {
                                        return zzb(context, str, z);
                                    }
                                    context = zzc(context, str, z);
                                    return context;
                                }
                            }
                        }
                    } catch (ClassNotFoundException e4) {
                        e = e4;
                    } catch (IllegalAccessException e5) {
                        e = e5;
                    } catch (NoSuchFieldException e6) {
                        e = e6;
                    }
                }
            }
            String valueOf = String.valueOf(e);
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 30);
            stringBuilder.append("Failed to load module via V2: ");
            stringBuilder.append(valueOf);
            Log.w("DynamiteModule", stringBuilder.toString());
            bool = Boolean.FALSE;
            zzid = bool;
            if (bool.booleanValue()) {
                return zzb(context, str, z);
            }
            context = zzc(context, str, z);
            return context;
        } catch (String str2) {
            z = "DynamiteModule";
            String str3 = "Failed to retrieve remote module version: ";
            str2 = String.valueOf(str2.getMessage());
            Log.w(z, str2.length() != 0 ? str3.concat(str2) : new String(str3));
            return null;
        } catch (String str22) {
            CrashUtils.addDynamiteErrorToDropBox(context, str22);
        }
    }

    private static int zzb(Context context, String str, boolean z) {
        zzi zzj = zzj(context);
        if (zzj == null) {
            return 0;
        }
        try {
            if (zzj.zzaj() >= 2) {
                return zzj.zzb(ObjectWrapper.wrap(context), str, z);
            }
            Log.w("DynamiteModule", "IDynamite loader version < 2, falling back to getModuleVersion2");
            return zzj.zza(ObjectWrapper.wrap(context), str, z);
        } catch (Context context2) {
            str = "DynamiteModule";
            z = "Failed to retrieve remote module version: ";
            context2 = String.valueOf(context2.getMessage());
            Log.w(str, context2.length() != 0 ? z.concat(context2) : new String(z));
            return 0;
        }
    }

    private static int zzc(Context context, String str, boolean z) throws LoadingException {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            context = z ? "api_force_staging" : "api";
            StringBuilder stringBuilder = new StringBuilder((String.valueOf(context).length() + 42) + String.valueOf(str).length());
            stringBuilder.append("content://com.google.android.gms.chimera/");
            stringBuilder.append(context);
            stringBuilder.append("/");
            stringBuilder.append(str);
            context = contentResolver.query(Uri.parse(stringBuilder.toString()), null, null, null, null);
            if (context != null) {
                try {
                    if (context.moveToFirst() != null) {
                        str = context.getInt(null);
                        if (str > null) {
                            synchronized (DynamiteModule.class) {
                                zzig = context.getString(2);
                                int columnIndex = context.getColumnIndex("loaderVersion");
                                if (columnIndex >= 0) {
                                    zzih = context.getInt(columnIndex);
                                }
                            }
                            zza zza = (zza) zzii.get();
                            if (zza != null && zza.zzin == null) {
                                zza.zzin = context;
                                context = null;
                            }
                        }
                        if (context != null) {
                            context.close();
                        }
                        return str;
                    }
                } catch (String str2) {
                    String str3 = str2;
                    str2 = context;
                    context = str3;
                } catch (String str22) {
                    cursor = context;
                    context = str22;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw context;
                }
            }
            Log.w("DynamiteModule", "Failed to retrieve remote module version.");
            throw new LoadingException("Failed to connect to dynamite module ContentResolver.");
        } catch (Exception e) {
            context = e;
            str22 = null;
            try {
                if (context instanceof LoadingException) {
                    throw context;
                }
                throw new LoadingException("V2 version check failed", context);
            } catch (Throwable th) {
                context = th;
                cursor = str22;
                if (cursor != null) {
                    cursor.close();
                }
                throw context;
            }
        } catch (Throwable th2) {
            context = th2;
            if (cursor != null) {
                cursor.close();
            }
            throw context;
        }
    }

    @KeepForSdk
    public static int getRemoteVersion(Context context, String str) {
        return zza(context, str, false);
    }

    private static DynamiteModule zze(Context context, String str) {
        String str2 = "DynamiteModule";
        String str3 = "Selected local version of ";
        str = String.valueOf(str);
        Log.i(str2, str.length() != 0 ? str3.concat(str) : new String(str3));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static DynamiteModule zza(Context context, String str, int i) throws LoadingException {
        try {
            Boolean bool;
            synchronized (DynamiteModule.class) {
                bool = zzid;
            }
            if (bool == null) {
                throw new LoadingException("Failed to determine which loading route to use.");
            } else if (bool.booleanValue()) {
                return zzc(context, str, i);
            } else {
                return zzb(context, str, i);
            }
        } catch (String str2) {
            CrashUtils.addDynamiteErrorToDropBox(context, str2);
        }
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws LoadingException {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 51);
        stringBuilder.append("Selected remote version of ");
        stringBuilder.append(str);
        stringBuilder.append(", version >= ");
        stringBuilder.append(i);
        Log.i("DynamiteModule", stringBuilder.toString());
        zzi zzj = zzj(context);
        if (zzj != null) {
            try {
                if (zzj.zzaj() >= 2) {
                    context = zzj.zzb(ObjectWrapper.wrap(context), str, i);
                } else {
                    Log.w("DynamiteModule", "Dynamite loader version < 2, falling back to createModuleContext");
                    context = zzj.zza(ObjectWrapper.wrap(context), str, i);
                }
                if (ObjectWrapper.unwrap(context) != null) {
                    return new DynamiteModule((Context) ObjectWrapper.unwrap(context));
                }
                throw new LoadingException("Failed to load remote module.");
            } catch (Context context2) {
                throw new LoadingException("Failed to load remote module.", context2);
            }
        }
        throw new LoadingException("Failed to create IDynamiteLoader.");
    }

    private static zzi zzj(Context context) {
        synchronized (DynamiteModule.class) {
            if (zzie != null) {
                context = zzie;
                return context;
            } else if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            } else {
                try {
                    IBinder iBinder = (IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                    if (iBinder == null) {
                        context = null;
                    } else {
                        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                        if (queryLocalInterface instanceof zzi) {
                            context = (zzi) queryLocalInterface;
                        } else {
                            context = new zzj(iBinder);
                        }
                    }
                    if (context != null) {
                        zzie = context;
                        return context;
                    }
                } catch (Context context2) {
                    String str = "DynamiteModule";
                    String str2 = "Failed to load IDynamiteLoader from GmsCore: ";
                    context2 = String.valueOf(context2.getMessage());
                    Log.e(str, context2.length() != 0 ? str2.concat(context2) : new String(str2));
                }
            }
        }
        return null;
    }

    @KeepForSdk
    public final Context getModuleContext() {
        return this.zzim;
    }

    private static DynamiteModule zzc(Context context, String str, int i) throws LoadingException {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 51);
        stringBuilder.append("Selected remote version of ");
        stringBuilder.append(str);
        stringBuilder.append(", version >= ");
        stringBuilder.append(i);
        Log.i("DynamiteModule", stringBuilder.toString());
        synchronized (DynamiteModule.class) {
            zzk zzk = zzif;
        }
        if (zzk != null) {
            zza zza = (zza) zzii.get();
            if (zza == null || zza.zzin == null) {
                throw new LoadingException("No result cursor");
            }
            context = zza(context.getApplicationContext(), str, i, zza.zzin, zzk);
            if (context != null) {
                return new DynamiteModule(context);
            }
            throw new LoadingException("Failed to get module context");
        }
        throw new LoadingException("DynamiteLoaderV2 was not cached.");
    }

    private static Boolean zzai() {
        Boolean valueOf;
        synchronized (DynamiteModule.class) {
            valueOf = Boolean.valueOf(zzih >= 2);
        }
        return valueOf;
    }

    private static Context zza(Context context, String str, int i, Cursor cursor, zzk zzk) {
        try {
            ObjectWrapper.wrap(null);
            if (zzai().booleanValue()) {
                Log.v("DynamiteModule", "Dynamite loader version >= 2, using loadModule2NoCrashUtils");
                context = zzk.zzb(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(cursor));
            } else {
                Log.w("DynamiteModule", "Dynamite loader version < 2, falling back to loadModule2");
                context = zzk.zza(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(cursor));
            }
            return (Context) ObjectWrapper.unwrap(context);
        } catch (Context context2) {
            str = "DynamiteModule";
            i = "Failed to load DynamiteLoader: ";
            context2 = String.valueOf(context2.toString());
            Log.e(str, context2.length() != null ? i.concat(context2) : new String(i));
            return null;
        }
    }

    @GuardedBy("DynamiteModule.class")
    private static void zza(ClassLoader classLoader) throws LoadingException {
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                classLoader = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                if (queryLocalInterface instanceof zzk) {
                    classLoader = (zzk) queryLocalInterface;
                } else {
                    classLoader = new zzl(iBinder);
                }
            }
            zzif = classLoader;
        } catch (ClassNotFoundException e) {
            classLoader = e;
            throw new LoadingException("Failed to instantiate dynamite loader", classLoader);
        } catch (IllegalAccessException e2) {
            classLoader = e2;
            throw new LoadingException("Failed to instantiate dynamite loader", classLoader);
        } catch (InstantiationException e3) {
            classLoader = e3;
            throw new LoadingException("Failed to instantiate dynamite loader", classLoader);
        } catch (InvocationTargetException e4) {
            classLoader = e4;
            throw new LoadingException("Failed to instantiate dynamite loader", classLoader);
        } catch (NoSuchMethodException e5) {
            classLoader = e5;
            throw new LoadingException("Failed to instantiate dynamite loader", classLoader);
        }
    }

    @KeepForSdk
    public final IBinder instantiate(String str) throws LoadingException {
        Throwable e;
        String str2;
        try {
            return (IBinder) this.zzim.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException e2) {
            e = e2;
            str2 = "Failed to instantiate module class: ";
            str = String.valueOf(str);
            throw new LoadingException(str.length() != 0 ? str2.concat(str) : new String(str2), e);
        } catch (InstantiationException e3) {
            e = e3;
            str2 = "Failed to instantiate module class: ";
            str = String.valueOf(str);
            if (str.length() != 0) {
            }
            throw new LoadingException(str.length() != 0 ? str2.concat(str) : new String(str2), e);
        } catch (IllegalAccessException e4) {
            e = e4;
            str2 = "Failed to instantiate module class: ";
            str = String.valueOf(str);
            if (str.length() != 0) {
            }
            throw new LoadingException(str.length() != 0 ? str2.concat(str) : new String(str2), e);
        }
    }

    private DynamiteModule(Context context) {
        this.zzim = (Context) Preconditions.checkNotNull(context);
    }
}
