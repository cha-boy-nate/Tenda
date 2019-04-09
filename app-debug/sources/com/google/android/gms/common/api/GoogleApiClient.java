package com.google.android.gms.common.api;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.ApiOptions.NotRequiredOptions;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.SignInConnectionListener;
import com.google.android.gms.common.api.internal.zacm;
import com.google.android.gms.common.internal.AccountType;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.ClientSettings.OptionalApiSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zaa;
import com.google.android.gms.signin.zad;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public abstract class GoogleApiClient {
    @KeepForSdk
    public static final String DEFAULT_ACCOUNT = "<<default account>>";
    public static final int SIGN_IN_MODE_OPTIONAL = 2;
    public static final int SIGN_IN_MODE_REQUIRED = 1;
    @GuardedBy("sAllClients")
    private static final Set<GoogleApiClient> zabq = Collections.newSetFromMap(new WeakHashMap());

    @KeepForSdk
    public static final class Builder {
        private final Context mContext;
        private Looper zabj;
        private final Set<Scope> zabr;
        private final Set<Scope> zabs;
        private int zabt;
        private View zabu;
        private String zabv;
        private String zabw;
        private final Map<Api<?>, OptionalApiSettings> zabx;
        private final Map<Api<?>, ApiOptions> zaby;
        private LifecycleActivity zabz;
        private int zaca;
        private OnConnectionFailedListener zacb;
        private GoogleApiAvailability zacc;
        private AbstractClientBuilder<? extends zad, SignInOptions> zacd;
        private final ArrayList<ConnectionCallbacks> zace;
        private final ArrayList<OnConnectionFailedListener> zacf;
        private boolean zacg;
        private Account zax;

        @KeepForSdk
        public Builder(@NonNull Context context) {
            this.zabr = new HashSet();
            this.zabs = new HashSet();
            this.zabx = new ArrayMap();
            this.zaby = new ArrayMap();
            this.zaca = -1;
            this.zacc = GoogleApiAvailability.getInstance();
            this.zacd = zaa.zapg;
            this.zace = new ArrayList();
            this.zacf = new ArrayList();
            this.zacg = false;
            this.mContext = context;
            this.zabj = context.getMainLooper();
            this.zabv = context.getPackageName();
            this.zabw = context.getClass().getName();
        }

        public final com.google.android.gms.common.api.GoogleApiClient build() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:42:0x017b in {5, 6, 10, 11, 12, 16, 18, 19, 24, 25, 26, 28, 36, 37, 41} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
            /*
            r22 = this;
            r1 = r22;
            r0 = r1.zaby;
            r0 = r0.isEmpty();
            r2 = 1;
            r0 = r0 ^ r2;
            r3 = "must call addApi() to add at least one API";
            com.google.android.gms.common.internal.Preconditions.checkArgument(r0, r3);
            r0 = r22.buildClientSettings();
            r3 = 0;
            r11 = r0.getOptionalApiSettings();
            r12 = new android.support.v4.util.ArrayMap;
            r12.<init>();
            r14 = new android.support.v4.util.ArrayMap;
            r14.<init>();
            r15 = new java.util.ArrayList;
            r15.<init>();
            r4 = r1.zaby;
            r4 = r4.keySet();
            r13 = r4.iterator();
            r16 = 0;
            r17 = 0;
        L_0x0038:
            r4 = r13.hasNext();
            if (r4 == 0) goto L_0x00d3;
        L_0x003e:
            r4 = r13.next();
            r10 = r4;
            r10 = (com.google.android.gms.common.api.Api) r10;
            r4 = r1.zaby;
            r18 = r4.get(r10);
            r4 = r11.get(r10);
            if (r4 == 0) goto L_0x0053;
        L_0x0051:
            r4 = 1;
            goto L_0x0054;
        L_0x0053:
            r4 = 0;
        L_0x0054:
            r5 = java.lang.Boolean.valueOf(r4);
            r12.put(r10, r5);
            r9 = new com.google.android.gms.common.api.internal.zaq;
            r9.<init>(r10, r4);
            r15.add(r9);
            r19 = r10.zai();
            r5 = r1.mContext;
            r6 = r1.zabj;
            r4 = r19;
            r7 = r0;
            r8 = r18;
            r20 = r9;
            r21 = r10;
            r10 = r20;
            r4 = r4.buildClient(r5, r6, r7, r8, r9, r10);
            r5 = r21.getClientKey();
            r14.put(r5, r4);
            r5 = r19.getPriority();
            if (r5 != r2) goto L_0x0091;
        L_0x008a:
            if (r18 == 0) goto L_0x008e;
        L_0x008c:
            r5 = 1;
            goto L_0x008f;
        L_0x008e:
            r5 = 0;
        L_0x008f:
            r17 = r5;
        L_0x0091:
            r4 = r4.providesSignIn();
            if (r4 == 0) goto L_0x00d1;
        L_0x0097:
            if (r3 != 0) goto L_0x009c;
        L_0x0099:
            r3 = r21;
            goto L_0x00d1;
        L_0x009c:
            r0 = new java.lang.IllegalStateException;
            r2 = r21.getName();
            r3 = r3.getName();
            r4 = java.lang.String.valueOf(r2);
            r4 = r4.length();
            r4 = r4 + 21;
            r5 = java.lang.String.valueOf(r3);
            r5 = r5.length();
            r4 = r4 + r5;
            r5 = new java.lang.StringBuilder;
            r5.<init>(r4);
            r5.append(r2);
            r2 = " cannot be used with ";
            r5.append(r2);
            r5.append(r3);
            r2 = r5.toString();
            r0.<init>(r2);
            throw r0;
        L_0x00d1:
            goto L_0x0038;
        L_0x00d3:
            if (r3 == 0) goto L_0x012b;
        L_0x00d5:
            if (r17 != 0) goto L_0x0101;
        L_0x00d7:
            r4 = r1.zax;
            if (r4 != 0) goto L_0x00dd;
        L_0x00db:
            r4 = 1;
            goto L_0x00de;
        L_0x00dd:
            r4 = 0;
        L_0x00de:
            r5 = "Must not set an account in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead";
            r6 = new java.lang.Object[r2];
            r7 = r3.getName();
            r6[r16] = r7;
            com.google.android.gms.common.internal.Preconditions.checkState(r4, r5, r6);
            r4 = r1.zabr;
            r5 = r1.zabs;
            r4 = r4.equals(r5);
            r5 = "Must not set scopes in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead.";
            r6 = new java.lang.Object[r2];
            r3 = r3.getName();
            r6[r16] = r3;
            com.google.android.gms.common.internal.Preconditions.checkState(r4, r5, r6);
            goto L_0x012b;
        L_0x0101:
            r0 = new java.lang.IllegalStateException;
            r2 = r3.getName();
            r3 = java.lang.String.valueOf(r2);
            r3 = r3.length();
            r3 = r3 + 82;
            r4 = new java.lang.StringBuilder;
            r4.<init>(r3);
            r3 = "With using ";
            r4.append(r3);
            r4.append(r2);
            r2 = ", GamesOptions can only be specified within GoogleSignInOptions.Builder";
            r4.append(r2);
            r2 = r4.toString();
            r0.<init>(r2);
            throw r0;
            r3 = r14.values();
            r16 = com.google.android.gms.common.api.internal.zaaw.zaa(r3, r2);
            r2 = new com.google.android.gms.common.api.internal.zaaw;
            r5 = r1.mContext;
            r6 = new java.util.concurrent.locks.ReentrantLock;
            r6.<init>();
            r7 = r1.zabj;
            r9 = r1.zacc;
            r10 = r1.zacd;
            r3 = r1.zace;
            r13 = r1.zacf;
            r11 = r1.zaca;
            r18 = 0;
            r4 = r2;
            r8 = r0;
            r0 = r11;
            r11 = r12;
            r12 = r3;
            r3 = r15;
            r15 = r0;
            r17 = r3;
            r4.<init>(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
            r3 = com.google.android.gms.common.api.GoogleApiClient.zabq;
            monitor-enter(r3);
            r0 = com.google.android.gms.common.api.GoogleApiClient.zabq;	 Catch:{ all -> 0x0178 }
            r0.add(r2);	 Catch:{ all -> 0x0178 }
            monitor-exit(r3);	 Catch:{ all -> 0x0178 }
            r0 = r1.zaca;
            if (r0 < 0) goto L_0x0177;
            r0 = r1.zabz;
            r0 = com.google.android.gms.common.api.internal.zaj.zaa(r0);
            r3 = r1.zaca;
            r4 = r1.zacb;
            r0.zaa(r3, r2, r4);
        L_0x0177:
            return r2;
        L_0x0178:
            r0 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0178 }
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.GoogleApiClient.Builder.build():com.google.android.gms.common.api.GoogleApiClient");
        }

        @KeepForSdk
        public Builder(@NonNull Context context, @NonNull ConnectionCallbacks connectionCallbacks, @NonNull OnConnectionFailedListener onConnectionFailedListener) {
            this(context);
            Preconditions.checkNotNull(connectionCallbacks, "Must provide a connected listener");
            this.zace.add(connectionCallbacks);
            Preconditions.checkNotNull(onConnectionFailedListener, "Must provide a connection failed listener");
            this.zacf.add(onConnectionFailedListener);
        }

        public final Builder setHandler(@NonNull Handler handler) {
            Preconditions.checkNotNull(handler, "Handler must not be null");
            this.zabj = handler.getLooper();
            return this;
        }

        public final Builder addConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
            Preconditions.checkNotNull(connectionCallbacks, "Listener must not be null");
            this.zace.add(connectionCallbacks);
            return this;
        }

        public final Builder addOnConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
            Preconditions.checkNotNull(onConnectionFailedListener, "Listener must not be null");
            this.zacf.add(onConnectionFailedListener);
            return this;
        }

        public final Builder setViewForPopups(@NonNull View view) {
            Preconditions.checkNotNull(view, "View must not be null");
            this.zabu = view;
            return this;
        }

        public final Builder addScope(@NonNull Scope scope) {
            Preconditions.checkNotNull(scope, "Scope must not be null");
            this.zabr.add(scope);
            return this;
        }

        @KeepForSdk
        public final Builder addScopeNames(String[] strArr) {
            for (String scope : strArr) {
                this.zabr.add(new Scope(scope));
            }
            return this;
        }

        public final Builder addApi(@NonNull Api<? extends NotRequiredOptions> api) {
            Preconditions.checkNotNull(api, "Api must not be null");
            this.zaby.put(api, null);
            api = api.zah().getImpliedScopes(null);
            this.zabs.addAll(api);
            this.zabr.addAll(api);
            return this;
        }

        public final Builder addApiIfAvailable(@NonNull Api<? extends NotRequiredOptions> api, Scope... scopeArr) {
            Preconditions.checkNotNull(api, "Api must not be null");
            this.zaby.put(api, null);
            zaa(api, null, scopeArr);
            return this;
        }

        public final <O extends HasOptions> Builder addApi(@NonNull Api<O> api, @NonNull O o) {
            Preconditions.checkNotNull(api, "Api must not be null");
            Preconditions.checkNotNull(o, "Null options are not permitted for this Api");
            this.zaby.put(api, o);
            api = api.zah().getImpliedScopes(o);
            this.zabs.addAll(api);
            this.zabr.addAll(api);
            return this;
        }

        public final <O extends HasOptions> Builder addApiIfAvailable(@NonNull Api<O> api, @NonNull O o, Scope... scopeArr) {
            Preconditions.checkNotNull(api, "Api must not be null");
            Preconditions.checkNotNull(o, "Null options are not permitted for this Api");
            this.zaby.put(api, o);
            zaa(api, o, scopeArr);
            return this;
        }

        public final Builder setAccountName(String str) {
            this.zax = str == null ? null : new Account(str, AccountType.GOOGLE);
            return this;
        }

        public final Builder useDefaultAccount() {
            return setAccountName("<<default account>>");
        }

        public final Builder setGravityForPopups(int i) {
            this.zabt = i;
            return this;
        }

        public final Builder enableAutoManage(@NonNull FragmentActivity fragmentActivity, int i, @Nullable OnConnectionFailedListener onConnectionFailedListener) {
            LifecycleActivity lifecycleActivity = new LifecycleActivity((Activity) fragmentActivity);
            Preconditions.checkArgument(i >= 0 ? true : null, "clientId must be non-negative");
            this.zaca = i;
            this.zacb = onConnectionFailedListener;
            this.zabz = lifecycleActivity;
            return this;
        }

        public final Builder enableAutoManage(@NonNull FragmentActivity fragmentActivity, @Nullable OnConnectionFailedListener onConnectionFailedListener) {
            return enableAutoManage(fragmentActivity, 0, onConnectionFailedListener);
        }

        @KeepForSdk
        @VisibleForTesting
        public final ClientSettings buildClientSettings() {
            SignInOptions signInOptions;
            SignInOptions signInOptions2 = SignInOptions.DEFAULT;
            if (this.zaby.containsKey(zaa.API)) {
                signInOptions = (SignInOptions) this.zaby.get(zaa.API);
            } else {
                signInOptions = signInOptions2;
            }
            return new ClientSettings(this.zax, this.zabr, this.zabx, this.zabt, this.zabu, this.zabv, this.zabw, signInOptions);
        }

        private final <O extends ApiOptions> void zaa(Api<O> api, O o, Scope... scopeArr) {
            Set hashSet = new HashSet(api.zah().getImpliedScopes(o));
            for (Object add : scopeArr) {
                hashSet.add(add);
            }
            this.zabx.put(api, new OptionalApiSettings(hashSet));
        }
    }

    public interface ConnectionCallbacks {
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;

        void onConnected(@Nullable Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }

    public static void dumpAll(java.lang.String r7, java.io.FileDescriptor r8, java.io.PrintWriter r9, java.lang.String[] r10) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:13:0x0039 in {7, 9, 12} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r0 = zabq;
        monitor-enter(r0);
        r1 = 0;
        r2 = java.lang.String.valueOf(r7);	 Catch:{ all -> 0x0036 }
        r3 = "  ";	 Catch:{ all -> 0x0036 }
        r2 = r2.concat(r3);	 Catch:{ all -> 0x0036 }
        r3 = zabq;	 Catch:{ all -> 0x0036 }
        r3 = r3.iterator();	 Catch:{ all -> 0x0036 }
    L_0x0014:
        r4 = r3.hasNext();	 Catch:{ all -> 0x0036 }
        if (r4 == 0) goto L_0x0034;	 Catch:{ all -> 0x0036 }
    L_0x001a:
        r4 = r3.next();	 Catch:{ all -> 0x0036 }
        r4 = (com.google.android.gms.common.api.GoogleApiClient) r4;	 Catch:{ all -> 0x0036 }
        r5 = r9.append(r7);	 Catch:{ all -> 0x0036 }
        r6 = "GoogleApiClient#";	 Catch:{ all -> 0x0036 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0036 }
        r6 = r1 + 1;	 Catch:{ all -> 0x0036 }
        r5.println(r1);	 Catch:{ all -> 0x0036 }
        r4.dump(r2, r8, r9, r10);	 Catch:{ all -> 0x0036 }
        r1 = r6;	 Catch:{ all -> 0x0036 }
        goto L_0x0014;	 Catch:{ all -> 0x0036 }
    L_0x0034:
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        return;	 Catch:{ all -> 0x0036 }
    L_0x0036:
        r7 = move-exception;	 Catch:{ all -> 0x0036 }
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        throw r7;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.GoogleApiClient.dumpAll(java.lang.String, java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    public abstract ConnectionResult blockingConnect();

    public abstract ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit);

    public abstract PendingResult<Status> clearDefaultAccountAndReconnect();

    public abstract void connect();

    public abstract void disconnect();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    @NonNull
    public abstract ConnectionResult getConnectionResult(@NonNull Api<?> api);

    public abstract boolean hasConnectedApi(@NonNull Api<?> api);

    public abstract boolean isConnected();

    public abstract boolean isConnecting();

    public abstract boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    public abstract void reconnect();

    public abstract void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    public abstract void stopAutoManage(@NonNull FragmentActivity fragmentActivity);

    public abstract void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    @KeepForSdk
    public static Set<GoogleApiClient> getAllClients() {
        Set<GoogleApiClient> set;
        synchronized (zabq) {
            set = zabq;
        }
        return set;
    }

    @KeepForSdk
    public <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public <L> ListenerHolder<L> registerListener(@NonNull L l) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    @NonNull
    public <C extends Client> C getClient(@NonNull AnyClientKey<C> anyClientKey) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public boolean hasApi(@NonNull Api<?> api) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public Context getContext() {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public void maybeSignOut() {
        throw new UnsupportedOperationException();
    }

    public void connect(int i) {
        throw new UnsupportedOperationException();
    }

    public void zaa(zacm zacm) {
        throw new UnsupportedOperationException();
    }

    public void zab(zacm zacm) {
        throw new UnsupportedOperationException();
    }
}
