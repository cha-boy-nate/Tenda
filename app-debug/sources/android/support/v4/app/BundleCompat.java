package android.support.v4.app;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {

    static class BundleCompatBaseImpl {
        private static final String TAG = "BundleCompatBaseImpl";
        private static Method sGetIBinderMethod;
        private static boolean sGetIBinderMethodFetched;
        private static Method sPutIBinderMethod;
        private static boolean sPutIBinderMethodFetched;

        private BundleCompatBaseImpl() {
        }

        public static IBinder getBinder(Bundle bundle, String key) {
            Exception e;
            if (!sGetIBinderMethodFetched) {
                try {
                    sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[]{String.class});
                    sGetIBinderMethod.setAccessible(true);
                } catch (NoSuchMethodException e2) {
                    Log.i(TAG, "Failed to retrieve getIBinder method", e2);
                }
                sGetIBinderMethodFetched = true;
            }
            Method method = sGetIBinderMethod;
            if (method == null) {
                return null;
            }
            try {
                return (IBinder) method.invoke(bundle, new Object[]{key});
            } catch (InvocationTargetException e3) {
                e = e3;
                Log.i(TAG, "Failed to invoke getIBinder via reflection", e);
                sGetIBinderMethod = null;
                return null;
            } catch (IllegalAccessException e4) {
                e = e4;
                Log.i(TAG, "Failed to invoke getIBinder via reflection", e);
                sGetIBinderMethod = null;
                return null;
            } catch (IllegalArgumentException e5) {
                e = e5;
                Log.i(TAG, "Failed to invoke getIBinder via reflection", e);
                sGetIBinderMethod = null;
                return null;
            }
        }

        public static void putBinder(Bundle bundle, String key, IBinder binder) {
            Exception e;
            if (!sPutIBinderMethodFetched) {
                try {
                    sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[]{String.class, IBinder.class});
                    sPutIBinderMethod.setAccessible(true);
                } catch (NoSuchMethodException e2) {
                    Log.i(TAG, "Failed to retrieve putIBinder method", e2);
                }
                sPutIBinderMethodFetched = true;
            }
            Method method = sPutIBinderMethod;
            if (method != null) {
                try {
                    method.invoke(bundle, new Object[]{key, binder});
                } catch (InvocationTargetException e3) {
                    e = e3;
                    Log.i(TAG, "Failed to invoke putIBinder via reflection", e);
                    sPutIBinderMethod = null;
                } catch (IllegalAccessException e4) {
                    e = e4;
                    Log.i(TAG, "Failed to invoke putIBinder via reflection", e);
                    sPutIBinderMethod = null;
                } catch (IllegalArgumentException e5) {
                    e = e5;
                    Log.i(TAG, "Failed to invoke putIBinder via reflection", e);
                    sPutIBinderMethod = null;
                }
            }
        }
    }

    private BundleCompat() {
    }

    @Nullable
    public static IBinder getBinder(@NonNull Bundle bundle, @Nullable String key) {
        if (VERSION.SDK_INT >= 18) {
            return bundle.getBinder(key);
        }
        return BundleCompatBaseImpl.getBinder(bundle, key);
    }

    public static void putBinder(@NonNull Bundle bundle, @Nullable String key, @Nullable IBinder binder) {
        if (VERSION.SDK_INT >= 18) {
            bundle.putBinder(key, binder);
        } else {
            BundleCompatBaseImpl.putBinder(bundle, key, binder);
        }
    }
}
