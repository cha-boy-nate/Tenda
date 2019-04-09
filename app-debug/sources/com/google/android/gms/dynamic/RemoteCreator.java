package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;

@KeepForSdk
public abstract class RemoteCreator<T> {
    private final String zzia;
    private T zzib;

    @KeepForSdk
    public static class RemoteCreatorException extends Exception {
        public RemoteCreatorException(String str) {
            super(str);
        }

        public RemoteCreatorException(String str, Throwable th) {
            super(str, th);
        }
    }

    @KeepForSdk
    protected RemoteCreator(String str) {
        this.zzia = str;
    }

    @KeepForSdk
    protected abstract T getRemoteCreator(IBinder iBinder);

    @KeepForSdk
    protected final T getRemoteCreatorInstance(Context context) throws RemoteCreatorException {
        if (this.zzib == null) {
            Preconditions.checkNotNull(context);
            context = GooglePlayServicesUtilLight.getRemoteContext(context);
            if (context != null) {
                try {
                    this.zzib = getRemoteCreator((IBinder) context.getClassLoader().loadClass(this.zzia).newInstance());
                } catch (Context context2) {
                    throw new RemoteCreatorException("Could not load creator class.", context2);
                } catch (Context context22) {
                    throw new RemoteCreatorException("Could not instantiate creator.", context22);
                } catch (Context context222) {
                    throw new RemoteCreatorException("Could not access creator.", context222);
                }
            }
            throw new RemoteCreatorException("Could not get remote context.");
        }
        return this.zzib;
    }
}
