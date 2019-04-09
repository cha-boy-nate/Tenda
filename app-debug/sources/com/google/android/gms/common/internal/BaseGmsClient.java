package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.BinderThread;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public abstract class BaseGmsClient<T extends IInterface> {
    @KeepForSdk
    public static final int CONNECT_STATE_CONNECTED = 4;
    @KeepForSdk
    public static final int CONNECT_STATE_DISCONNECTED = 1;
    @KeepForSdk
    public static final int CONNECT_STATE_DISCONNECTING = 5;
    @KeepForSdk
    public static final String DEFAULT_ACCOUNT = "<<default account>>";
    @KeepForSdk
    public static final String[] GOOGLE_PLUS_REQUIRED_FEATURES = new String[]{"service_esmobile", "service_googleme"};
    @KeepForSdk
    public static final String KEY_PENDING_INTENT = "pendingIntent";
    private static final Feature[] zzbs = new Feature[0];
    private final Context mContext;
    final Handler mHandler;
    private final Object mLock;
    private int zzbt;
    private long zzbu;
    private long zzbv;
    private int zzbw;
    private long zzbx;
    @VisibleForTesting
    private zzh zzby;
    private final Looper zzbz;
    private final GmsClientSupervisor zzca;
    private final GoogleApiAvailabilityLight zzcb;
    private final Object zzcc;
    @GuardedBy("mServiceBrokerLock")
    private IGmsServiceBroker zzcd;
    @VisibleForTesting
    protected ConnectionProgressReportCallbacks zzce;
    @GuardedBy("mLock")
    private T zzcf;
    private final ArrayList<zzc<?>> zzcg;
    @GuardedBy("mLock")
    private zze zzch;
    @GuardedBy("mLock")
    private int zzci;
    private final BaseConnectionCallbacks zzcj;
    private final BaseOnConnectionFailedListener zzck;
    private final int zzcl;
    private final String zzcm;
    private ConnectionResult zzcn;
    private boolean zzco;
    private volatile zzb zzcp;
    @VisibleForTesting
    protected AtomicInteger zzcq;

    @KeepForSdk
    public interface BaseConnectionCallbacks {
        @KeepForSdk
        void onConnected(@Nullable Bundle bundle);

        @KeepForSdk
        void onConnectionSuspended(int i);
    }

    @KeepForSdk
    public interface BaseOnConnectionFailedListener {
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }

    @KeepForSdk
    public interface ConnectionProgressReportCallbacks {
        @KeepForSdk
        void onReportServiceBinding(@NonNull ConnectionResult connectionResult);
    }

    @KeepForSdk
    public interface SignOutCallbacks {
        @KeepForSdk
        void onSignOutComplete();
    }

    protected abstract class zzc<TListener> {
        private final /* synthetic */ BaseGmsClient zzcs;
        private TListener zzct;
        private boolean zzcu = null;

        public zzc(BaseGmsClient baseGmsClient, TListener tListener) {
            this.zzcs = baseGmsClient;
            this.zzct = tListener;
        }

        protected abstract void zza(TListener tListener);

        protected abstract void zzn();

        public final void zzo() {
            synchronized (this) {
                Object obj = this.zzct;
                if (this.zzcu) {
                    String valueOf = String.valueOf(this);
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 47);
                    stringBuilder.append("Callback proxy ");
                    stringBuilder.append(valueOf);
                    stringBuilder.append(" being reused. This is not safe.");
                    Log.w("GmsClient", stringBuilder.toString());
                }
            }
            if (obj != null) {
                try {
                    zza(obj);
                } catch (RuntimeException e) {
                    zzn();
                    throw e;
                }
            }
            zzn();
            synchronized (this) {
                this.zzcu = true;
            }
            unregister();
        }

        public final void unregister() {
            removeListener();
            synchronized (this.zzcs.zzcg) {
                this.zzcs.zzcg.remove(this);
            }
        }

        public final void removeListener() {
            synchronized (this) {
                this.zzct = null;
            }
        }
    }

    @VisibleForTesting
    public final class zze implements ServiceConnection {
        private final /* synthetic */ BaseGmsClient zzcs;
        private final int zzcw;

        public zze(BaseGmsClient baseGmsClient, int i) {
            this.zzcs = baseGmsClient;
            this.zzcw = i;
        }

        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder == null) {
                this.zzcs.zzb(16);
                return;
            }
            synchronized (this.zzcs.zzcc) {
                IGmsServiceBroker iGmsServiceBroker;
                BaseGmsClient baseGmsClient = this.zzcs;
                if (iBinder == null) {
                    iGmsServiceBroker = null;
                } else {
                    IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    if (queryLocalInterface == null || !(queryLocalInterface instanceof IGmsServiceBroker)) {
                        iGmsServiceBroker = new zza(iBinder);
                    } else {
                        iGmsServiceBroker = (IGmsServiceBroker) queryLocalInterface;
                    }
                }
                baseGmsClient.zzcd = iGmsServiceBroker;
            }
            this.zzcs.zza(0, null, this.zzcw);
        }

        public final void onServiceDisconnected(ComponentName componentName) {
            synchronized (this.zzcs.zzcc) {
                this.zzcs.zzcd = null;
            }
            this.zzcs.mHandler.sendMessage(this.zzcs.mHandler.obtainMessage(6, this.zzcw, 1));
        }
    }

    protected class LegacyClientCallbackAdapter implements ConnectionProgressReportCallbacks {
        private final /* synthetic */ BaseGmsClient zzcs;

        @KeepForSdk
        public LegacyClientCallbackAdapter(BaseGmsClient baseGmsClient) {
            this.zzcs = baseGmsClient;
        }

        public void onReportServiceBinding(@NonNull ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                connectionResult = this.zzcs;
                connectionResult.getRemoteService(null, connectionResult.getScopes());
                return;
            }
            if (this.zzcs.zzck != null) {
                this.zzcs.zzck.onConnectionFailed(connectionResult);
            }
        }
    }

    private abstract class zza extends zzc<Boolean> {
        private final int statusCode;
        private final Bundle zzcr;
        private final /* synthetic */ BaseGmsClient zzcs;

        @BinderThread
        protected zza(BaseGmsClient baseGmsClient, int i, Bundle bundle) {
            this.zzcs = baseGmsClient;
            super(baseGmsClient, Boolean.valueOf(true));
            this.statusCode = i;
            this.zzcr = bundle;
        }

        protected abstract void zza(ConnectionResult connectionResult);

        protected abstract boolean zzm();

        protected final void zzn() {
        }

        protected final /* synthetic */ void zza(Object obj) {
            PendingIntent pendingIntent = null;
            if (((Boolean) obj) == null) {
                this.zzcs.zza(1, null);
                return;
            }
            obj = this.statusCode;
            if (obj != null) {
                if (obj != 10) {
                    this.zzcs.zza(1, null);
                    obj = this.zzcr;
                    if (obj != null) {
                        pendingIntent = (PendingIntent) obj.getParcelable(BaseGmsClient.KEY_PENDING_INTENT);
                    }
                    zza(new ConnectionResult(this.statusCode, pendingIntent));
                } else {
                    this.zzcs.zza(1, null);
                    throw new IllegalStateException(String.format("A fatal developer error has occurred. Class name: %s. Start service action: %s. Service Descriptor: %s. ", new Object[]{getClass().getSimpleName(), this.zzcs.getStartServiceAction(), this.zzcs.getServiceDescriptor()}));
                }
            } else if (zzm() == null) {
                this.zzcs.zza(1, null);
                zza(new ConnectionResult(8, null));
            }
        }
    }

    final class zzb extends com.google.android.gms.internal.common.zze {
        private final /* synthetic */ BaseGmsClient zzcs;

        public zzb(BaseGmsClient baseGmsClient, Looper looper) {
            this.zzcs = baseGmsClient;
            super(looper);
        }

        public final void handleMessage(Message message) {
            if (this.zzcs.zzcq.get() != message.arg1) {
                if (zzb(message)) {
                    zza(message);
                }
            } else if ((message.what == 1 || message.what == 7 || message.what == 4 || message.what == 5) && !this.zzcs.isConnecting()) {
                zza(message);
            } else {
                PendingIntent pendingIntent = null;
                if (message.what == 4) {
                    this.zzcs.zzcn = new ConnectionResult(message.arg2);
                    if (this.zzcs.zzl() == null || this.zzcs.zzco != null) {
                        if (this.zzcs.zzcn != null) {
                            message = this.zzcs.zzcn;
                        } else {
                            message = new ConnectionResult(8);
                        }
                        this.zzcs.zzce.onReportServiceBinding(message);
                        this.zzcs.onConnectionFailed(message);
                        return;
                    }
                    this.zzcs.zza(3, null);
                } else if (message.what == 5) {
                    if (this.zzcs.zzcn != null) {
                        message = this.zzcs.zzcn;
                    } else {
                        message = new ConnectionResult(8);
                    }
                    this.zzcs.zzce.onReportServiceBinding(message);
                    this.zzcs.onConnectionFailed(message);
                } else if (message.what == 3) {
                    if (message.obj instanceof PendingIntent) {
                        pendingIntent = (PendingIntent) message.obj;
                    }
                    ConnectionResult connectionResult = new ConnectionResult(message.arg2, pendingIntent);
                    this.zzcs.zzce.onReportServiceBinding(connectionResult);
                    this.zzcs.onConnectionFailed(connectionResult);
                } else if (message.what == 6) {
                    this.zzcs.zza(5, null);
                    if (this.zzcs.zzcj != null) {
                        this.zzcs.zzcj.onConnectionSuspended(message.arg2);
                    }
                    this.zzcs.onConnectionSuspended(message.arg2);
                    this.zzcs.zza(5, 1, null);
                } else if (message.what == 2 && !this.zzcs.isConnected()) {
                    zza(message);
                } else if (zzb(message)) {
                    ((zzc) message.obj).zzo();
                } else {
                    message = message.what;
                    StringBuilder stringBuilder = new StringBuilder(45);
                    stringBuilder.append("Don't know how to handle message: ");
                    stringBuilder.append(message);
                    Log.wtf("GmsClient", stringBuilder.toString(), new Exception());
                }
            }
        }

        private static void zza(Message message) {
            zzc zzc = (zzc) message.obj;
            zzc.zzn();
            zzc.unregister();
        }

        private static boolean zzb(Message message) {
            if (!(message.what == 2 || message.what == 1)) {
                if (message.what != 7) {
                    return null;
                }
            }
            return true;
        }
    }

    @VisibleForTesting
    public static final class zzd extends com.google.android.gms.common.internal.IGmsCallbacks.zza {
        private BaseGmsClient zzcv;
        private final int zzcw;

        public zzd(@NonNull BaseGmsClient baseGmsClient, int i) {
            this.zzcv = baseGmsClient;
            this.zzcw = i;
        }

        @BinderThread
        public final void zza(int i, @Nullable Bundle bundle) {
            Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
        }

        @BinderThread
        public final void onPostInitComplete(int i, @NonNull IBinder iBinder, @Nullable Bundle bundle) {
            Preconditions.checkNotNull(this.zzcv, "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzcv.onPostInitHandler(i, iBinder, bundle, this.zzcw);
            this.zzcv = 0;
        }

        @BinderThread
        public final void zza(int i, @NonNull IBinder iBinder, @NonNull zzb zzb) {
            Preconditions.checkNotNull(this.zzcv, "onPostInitCompleteWithConnectionInfo can be called only once per call togetRemoteService");
            Preconditions.checkNotNull(zzb);
            this.zzcv.zza(zzb);
            onPostInitComplete(i, iBinder, zzb.zzcz);
        }
    }

    protected final class zzf extends zza {
        private final /* synthetic */ BaseGmsClient zzcs;
        private final IBinder zzcx;

        @BinderThread
        public zzf(BaseGmsClient baseGmsClient, int i, IBinder iBinder, Bundle bundle) {
            this.zzcs = baseGmsClient;
            super(baseGmsClient, i, bundle);
            this.zzcx = iBinder;
        }

        protected final void zza(ConnectionResult connectionResult) {
            if (this.zzcs.zzck != null) {
                this.zzcs.zzck.onConnectionFailed(connectionResult);
            }
            this.zzcs.onConnectionFailed(connectionResult);
        }

        protected final boolean zzm() {
            try {
                String interfaceDescriptor = this.zzcx.getInterfaceDescriptor();
                if (this.zzcs.getServiceDescriptor().equals(interfaceDescriptor)) {
                    IInterface createServiceInterface = this.zzcs.createServiceInterface(this.zzcx);
                    if (createServiceInterface == null || (!this.zzcs.zza(2, 4, createServiceInterface) && !this.zzcs.zza(3, 4, createServiceInterface))) {
                        return false;
                    }
                    this.zzcs.zzcn = null;
                    Bundle connectionHint = this.zzcs.getConnectionHint();
                    if (this.zzcs.zzcj != null) {
                        this.zzcs.zzcj.onConnected(connectionHint);
                    }
                    return true;
                }
                String serviceDescriptor = this.zzcs.getServiceDescriptor();
                StringBuilder stringBuilder = new StringBuilder((String.valueOf(serviceDescriptor).length() + 34) + String.valueOf(interfaceDescriptor).length());
                stringBuilder.append("service descriptor mismatch: ");
                stringBuilder.append(serviceDescriptor);
                stringBuilder.append(" vs. ");
                stringBuilder.append(interfaceDescriptor);
                Log.e("GmsClient", stringBuilder.toString());
                return false;
            } catch (RemoteException e) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }

    protected final class zzg extends zza {
        private final /* synthetic */ BaseGmsClient zzcs;

        @BinderThread
        public zzg(BaseGmsClient baseGmsClient, @Nullable int i, Bundle bundle) {
            this.zzcs = baseGmsClient;
            super(baseGmsClient, i, null);
        }

        protected final void zza(ConnectionResult connectionResult) {
            this.zzcs.zzce.onReportServiceBinding(connectionResult);
            this.zzcs.onConnectionFailed(connectionResult);
        }

        protected final boolean zzm() {
            this.zzcs.zzce.onReportServiceBinding(ConnectionResult.RESULT_SUCCESS);
            return true;
        }
    }

    @KeepForSdk
    protected BaseGmsClient(Context context, Looper looper, int i, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener, String str) {
        this(context, looper, GmsClientSupervisor.getInstance(context), GoogleApiAvailabilityLight.getInstance(), i, (BaseConnectionCallbacks) Preconditions.checkNotNull(baseConnectionCallbacks), (BaseOnConnectionFailedListener) Preconditions.checkNotNull(baseOnConnectionFailedListener), str);
    }

    @Nullable
    @KeepForSdk
    protected abstract T createServiceInterface(IBinder iBinder);

    @com.google.android.gms.common.annotation.KeepForSdk
    public void disconnect() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:24:0x0037 in {5, 15, 19, 23} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r4.zzcq;
        r0.incrementAndGet();
        r0 = r4.zzcg;
        monitor-enter(r0);
        r1 = r4.zzcg;	 Catch:{ all -> 0x0034 }
        r1 = r1.size();	 Catch:{ all -> 0x0034 }
        r2 = 0;	 Catch:{ all -> 0x0034 }
    L_0x000f:
        if (r2 >= r1) goto L_0x001f;	 Catch:{ all -> 0x0034 }
    L_0x0011:
        r3 = r4.zzcg;	 Catch:{ all -> 0x0034 }
        r3 = r3.get(r2);	 Catch:{ all -> 0x0034 }
        r3 = (com.google.android.gms.common.internal.BaseGmsClient.zzc) r3;	 Catch:{ all -> 0x0034 }
        r3.removeListener();	 Catch:{ all -> 0x0034 }
        r2 = r2 + 1;	 Catch:{ all -> 0x0034 }
        goto L_0x000f;	 Catch:{ all -> 0x0034 }
    L_0x001f:
        r1 = r4.zzcg;	 Catch:{ all -> 0x0034 }
        r1.clear();	 Catch:{ all -> 0x0034 }
        monitor-exit(r0);	 Catch:{ all -> 0x0034 }
        r1 = r4.zzcc;
        monitor-enter(r1);
        r0 = 0;
        r4.zzcd = r0;	 Catch:{ all -> 0x0031 }
        monitor-exit(r1);	 Catch:{ all -> 0x0031 }
        r1 = 1;
        r4.zza(r1, r0);
        return;
    L_0x0031:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0031 }
        throw r0;
    L_0x0034:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0034 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.BaseGmsClient.disconnect():void");
    }

    @KeepForSdk
    @NonNull
    protected abstract String getServiceDescriptor();

    @KeepForSdk
    @NonNull
    protected abstract String getStartServiceAction();

    @KeepForSdk
    @VisibleForTesting
    protected BaseGmsClient(Context context, Looper looper, GmsClientSupervisor gmsClientSupervisor, GoogleApiAvailabilityLight googleApiAvailabilityLight, int i, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener, String str) {
        this.mLock = new Object();
        this.zzcc = new Object();
        this.zzcg = new ArrayList();
        this.zzci = 1;
        this.zzcn = null;
        this.zzco = false;
        this.zzcp = null;
        this.zzcq = new AtomicInteger(0);
        this.mContext = (Context) Preconditions.checkNotNull(context, "Context must not be null");
        this.zzbz = (Looper) Preconditions.checkNotNull(looper, "Looper must not be null");
        this.zzca = (GmsClientSupervisor) Preconditions.checkNotNull(gmsClientSupervisor, "Supervisor must not be null");
        this.zzcb = (GoogleApiAvailabilityLight) Preconditions.checkNotNull(googleApiAvailabilityLight, "API availability must not be null");
        this.mHandler = new zzb(this, looper);
        this.zzcl = i;
        this.zzcj = baseConnectionCallbacks;
        this.zzck = baseOnConnectionFailedListener;
        this.zzcm = str;
    }

    @KeepForSdk
    @VisibleForTesting
    protected BaseGmsClient(Context context, Handler handler, GmsClientSupervisor gmsClientSupervisor, GoogleApiAvailabilityLight googleApiAvailabilityLight, int i, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener) {
        this.mLock = new Object();
        this.zzcc = new Object();
        this.zzcg = new ArrayList();
        this.zzci = 1;
        this.zzcn = null;
        this.zzco = false;
        this.zzcp = null;
        this.zzcq = new AtomicInteger(0);
        this.mContext = (Context) Preconditions.checkNotNull(context, "Context must not be null");
        this.mHandler = (Handler) Preconditions.checkNotNull(handler, "Handler must not be null");
        this.zzbz = handler.getLooper();
        this.zzca = (GmsClientSupervisor) Preconditions.checkNotNull(gmsClientSupervisor, "Supervisor must not be null");
        this.zzcb = (GoogleApiAvailabilityLight) Preconditions.checkNotNull(googleApiAvailabilityLight, "API availability must not be null");
        this.zzcl = i;
        this.zzcj = baseConnectionCallbacks;
        this.zzck = baseOnConnectionFailedListener;
        this.zzcm = null;
    }

    @KeepForSdk
    protected String getStartServicePackage() {
        return "com.google.android.gms";
    }

    @Nullable
    private final String zzj() {
        String str = this.zzcm;
        return str == null ? this.mContext.getClass().getName() : str;
    }

    @Nullable
    @KeepForSdk
    protected String getLocalStartServiceAction() {
        return null;
    }

    private final void zza(zzb zzb) {
        this.zzcp = zzb;
    }

    @Nullable
    @KeepForSdk
    public final Feature[] getAvailableFeatures() {
        zzb zzb = this.zzcp;
        if (zzb == null) {
            return null;
        }
        return zzb.zzda;
    }

    @KeepForSdk
    @CallSuper
    protected void onConnectedLocked(@NonNull T t) {
        this.zzbv = System.currentTimeMillis();
    }

    @KeepForSdk
    @CallSuper
    protected void onConnectionSuspended(int i) {
        this.zzbt = i;
        this.zzbu = System.currentTimeMillis();
    }

    @KeepForSdk
    @CallSuper
    protected void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzbw = connectionResult.getErrorCode();
        this.zzbx = System.currentTimeMillis();
    }

    private final void zza(int i, T t) {
        Preconditions.checkArgument((i == 4 ? 1 : null) == (t != null ? 1 : null));
        synchronized (this.mLock) {
            this.zzci = i;
            this.zzcf = t;
            onSetConnectState(i, t);
            switch (i) {
                case 1:
                    if (this.zzch != 0) {
                        this.zzca.zza(getStartServiceAction(), getStartServicePackage(), 129, this.zzch, zzj());
                        this.zzch = null;
                        break;
                    }
                    break;
                case 2:
                case 3:
                    if (!(this.zzch == 0 || this.zzby == 0)) {
                        t = this.zzby.zzt();
                        String packageName = this.zzby.getPackageName();
                        StringBuilder stringBuilder = new StringBuilder((String.valueOf(t).length() + 70) + String.valueOf(packageName).length());
                        stringBuilder.append("Calling connect() while still connected, missing disconnect() for ");
                        stringBuilder.append(t);
                        stringBuilder.append(" on ");
                        stringBuilder.append(packageName);
                        Log.e("GmsClient", stringBuilder.toString());
                        this.zzca.zza(this.zzby.zzt(), this.zzby.getPackageName(), this.zzby.zzq(), this.zzch, zzj());
                        this.zzcq.incrementAndGet();
                    }
                    this.zzch = new zze(this, this.zzcq.get());
                    if (this.zzci != 3 || getLocalStartServiceAction() == 0) {
                        i = new zzh(getStartServicePackage(), getStartServiceAction(), false, 129);
                    } else {
                        i = new zzh(getContext().getPackageName(), getLocalStartServiceAction(), true, 129);
                    }
                    this.zzby = i;
                    if (this.zzca.zza(new zza(this.zzby.zzt(), this.zzby.getPackageName(), this.zzby.zzq()), this.zzch, zzj()) == 0) {
                        t = this.zzby.zzt();
                        String packageName2 = this.zzby.getPackageName();
                        StringBuilder stringBuilder2 = new StringBuilder((String.valueOf(t).length() + 34) + String.valueOf(packageName2).length());
                        stringBuilder2.append("unable to connect to service: ");
                        stringBuilder2.append(t);
                        stringBuilder2.append(" on ");
                        stringBuilder2.append(packageName2);
                        Log.e("GmsClient", stringBuilder2.toString());
                        zza(16, null, this.zzcq.get());
                    }
                    break;
                case 4:
                    onConnectedLocked(t);
                    break;
                default:
                    break;
            }
        }
    }

    @KeepForSdk
    void onSetConnectState(int i, T t) {
    }

    private final boolean zza(int i, int i2, T t) {
        synchronized (this.mLock) {
            if (this.zzci != i) {
                return false;
            }
            zza(i2, (IInterface) t);
            return true;
        }
    }

    @KeepForSdk
    public void checkAvailabilityAndConnect() {
        int isGooglePlayServicesAvailable = this.zzcb.isGooglePlayServicesAvailable(this.mContext, getMinApkVersion());
        if (isGooglePlayServicesAvailable != 0) {
            zza(1, null);
            triggerNotAvailable(new LegacyClientCallbackAdapter(this), isGooglePlayServicesAvailable, null);
            return;
        }
        connect(new LegacyClientCallbackAdapter(this));
    }

    @KeepForSdk
    public void connect(@NonNull ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        this.zzce = (ConnectionProgressReportCallbacks) Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        zza(2, null);
    }

    @KeepForSdk
    public boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzci == 4;
        }
        return z;
    }

    @KeepForSdk
    public boolean isConnecting() {
        boolean z;
        synchronized (this.mLock) {
            if (this.zzci != 2) {
                if (this.zzci != 3) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    private final boolean zzk() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzci == 3;
        }
        return z;
    }

    @KeepForSdk
    public void triggerConnectionSuspended(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(6, this.zzcq.get(), i));
    }

    private final void zzb(int i) {
        if (zzk() != 0) {
            i = 5;
            this.zzco = true;
        } else {
            i = 4;
        }
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(i, this.zzcq.get(), 16));
    }

    @KeepForSdk
    @VisibleForTesting
    protected void triggerNotAvailable(@NonNull ConnectionProgressReportCallbacks connectionProgressReportCallbacks, int i, @Nullable PendingIntent pendingIntent) {
        this.zzce = (ConnectionProgressReportCallbacks) Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        connectionProgressReportCallbacks = this.mHandler;
        connectionProgressReportCallbacks.sendMessage(connectionProgressReportCallbacks.obtainMessage(3, this.zzcq.get(), i, pendingIntent));
    }

    @KeepForSdk
    public final Context getContext() {
        return this.mContext;
    }

    @KeepForSdk
    public final Looper getLooper() {
        return this.zzbz;
    }

    @KeepForSdk
    public Account getAccount() {
        return null;
    }

    @KeepForSdk
    public Feature[] getApiFeatures() {
        return zzbs;
    }

    @KeepForSdk
    protected Bundle getGetServiceRequestExtraArgs() {
        return new Bundle();
    }

    @KeepForSdk
    protected void onPostInitHandler(int i, IBinder iBinder, Bundle bundle, int i2) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(1, i2, -1, new zzf(this, i, iBinder, bundle)));
    }

    protected final void zza(int i, @Nullable Bundle bundle, int i2) {
        bundle = this.mHandler;
        bundle.sendMessage(bundle.obtainMessage(7, i2, -1, new zzg(this, i, null)));
    }

    @KeepForSdk
    protected final void checkConnected() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    @KeepForSdk
    public Bundle getConnectionHint() {
        return null;
    }

    @KeepForSdk
    public final T getService() throws DeadObjectException {
        T t;
        synchronized (this.mLock) {
            if (this.zzci != 5) {
                checkConnected();
                Preconditions.checkState(this.zzcf != null, "Client is connected but service is null");
                t = this.zzcf;
            } else {
                throw new DeadObjectException();
            }
        }
        return t;
    }

    @WorkerThread
    @KeepForSdk
    public void getRemoteService(IAccountAccessor iAccountAccessor, Set<Scope> set) {
        Bundle getServiceRequestExtraArgs = getGetServiceRequestExtraArgs();
        GetServiceRequest getServiceRequest = new GetServiceRequest(this.zzcl);
        getServiceRequest.zzdh = this.mContext.getPackageName();
        getServiceRequest.zzdk = getServiceRequestExtraArgs;
        if (set != null) {
            getServiceRequest.zzdj = (Scope[]) set.toArray(new Scope[set.size()]);
        }
        if (requiresSignIn() != null) {
            getServiceRequest.zzdl = getAccount() != null ? getAccount() : new Account("<<default account>>", AccountType.GOOGLE);
            if (iAccountAccessor != null) {
                getServiceRequest.zzdi = iAccountAccessor.asBinder();
            }
        } else if (requiresAccount() != null) {
            getServiceRequest.zzdl = getAccount();
        }
        getServiceRequest.zzdm = zzbs;
        getServiceRequest.zzdn = getApiFeatures();
        try {
            synchronized (this.zzcc) {
                if (this.zzcd != null) {
                    this.zzcd.getService(new zzd(this, this.zzcq.get()), getServiceRequest);
                } else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        } catch (IAccountAccessor iAccountAccessor2) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", iAccountAccessor2);
            triggerConnectionSuspended(1);
        } catch (IAccountAccessor iAccountAccessor22) {
            throw iAccountAccessor22;
        } catch (RemoteException e) {
            iAccountAccessor22 = e;
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", iAccountAccessor22);
            onPostInitHandler(8, null, null, this.zzcq.get());
        } catch (RuntimeException e2) {
            iAccountAccessor22 = e2;
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", iAccountAccessor22);
            onPostInitHandler(8, null, null, this.zzcq.get());
        }
    }

    @KeepForSdk
    public boolean requiresSignIn() {
        return false;
    }

    @KeepForSdk
    public void onUserSignOut(@NonNull SignOutCallbacks signOutCallbacks) {
        signOutCallbacks.onSignOutComplete();
    }

    @KeepForSdk
    public boolean requiresAccount() {
        return false;
    }

    @KeepForSdk
    public boolean requiresGooglePlayServices() {
        return true;
    }

    @KeepForSdk
    public boolean providesSignIn() {
        return false;
    }

    @KeepForSdk
    public Intent getSignInIntent() {
        throw new UnsupportedOperationException("Not a sign in API");
    }

    @KeepForSdk
    protected Set<Scope> getScopes() {
        return Collections.EMPTY_SET;
    }

    @KeepForSdk
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        synchronized (this.mLock) {
            strArr = this.zzci;
            IInterface iInterface = this.zzcf;
        }
        synchronized (this.zzcc) {
            fileDescriptor = this.zzcd;
        }
        printWriter.append(str).append("mConnectState=");
        switch (strArr) {
            case 1:
                printWriter.print("DISCONNECTED");
                break;
            case 2:
                printWriter.print("REMOTE_CONNECTING");
                break;
            case 3:
                printWriter.print("LOCAL_CONNECTING");
                break;
            case 4:
                printWriter.print("CONNECTED");
                break;
            case 5:
                printWriter.print("DISCONNECTING");
                break;
            default:
                printWriter.print("UNKNOWN");
                break;
        }
        printWriter.append(" mService=");
        if (iInterface == null) {
            printWriter.append("null");
        } else {
            printWriter.append(getServiceDescriptor()).append("@").append(Integer.toHexString(System.identityHashCode(iInterface.asBinder())));
        }
        printWriter.append(" mServiceBroker=");
        if (fileDescriptor == null) {
            printWriter.println("null");
        } else {
            printWriter.append("IGmsServiceBroker@").println(Integer.toHexString(System.identityHashCode(fileDescriptor.asBinder())));
        }
        fileDescriptor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        if (this.zzbv > 0) {
            strArr = printWriter.append(str).append("lastConnectedTime=");
            long j = this.zzbv;
            String format = fileDescriptor.format(new Date(j));
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(format).length() + 21);
            stringBuilder.append(j);
            stringBuilder.append(" ");
            stringBuilder.append(format);
            strArr.println(stringBuilder.toString());
        }
        if (this.zzbu > 0) {
            printWriter.append(str).append("lastSuspendedCause=");
            strArr = this.zzbt;
            switch (strArr) {
                case 1:
                    printWriter.append("CAUSE_SERVICE_DISCONNECTED");
                    break;
                case 2:
                    printWriter.append("CAUSE_NETWORK_LOST");
                    break;
                default:
                    printWriter.append(String.valueOf(strArr));
                    break;
            }
            strArr = printWriter.append(" lastSuspendedTime=");
            j = this.zzbu;
            format = fileDescriptor.format(new Date(j));
            stringBuilder = new StringBuilder(String.valueOf(format).length() + 21);
            stringBuilder.append(j);
            stringBuilder.append(" ");
            stringBuilder.append(format);
            strArr.println(stringBuilder.toString());
        }
        if (this.zzbx > 0) {
            printWriter.append(str).append("lastFailedStatus=").append(CommonStatusCodes.getStatusCodeString(this.zzbw));
            str = printWriter.append(" lastFailedTime=");
            printWriter = this.zzbx;
            fileDescriptor = fileDescriptor.format(new Date(printWriter));
            StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(fileDescriptor).length() + 21);
            stringBuilder2.append(printWriter);
            stringBuilder2.append(" ");
            stringBuilder2.append(fileDescriptor);
            str.println(stringBuilder2.toString());
        }
    }

    @Nullable
    @KeepForSdk
    public IBinder getServiceBrokerBinder() {
        synchronized (this.zzcc) {
            if (this.zzcd == null) {
                return null;
            }
            IBinder asBinder = this.zzcd.asBinder();
            return asBinder;
        }
    }

    private final boolean zzl() {
        if (this.zzco || TextUtils.isEmpty(getServiceDescriptor()) || TextUtils.isEmpty(getLocalStartServiceAction())) {
            return false;
        }
        try {
            Class.forName(getServiceDescriptor());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @KeepForSdk
    public String getEndpointPackageName() {
        if (isConnected()) {
            zzh zzh = this.zzby;
            if (zzh != null) {
                return zzh.getPackageName();
            }
        }
        throw new RuntimeException("Failed to connect when checking package");
    }

    @KeepForSdk
    public int getMinApkVersion() {
        return GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }
}
