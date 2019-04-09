package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.ListenerHolder.ListenerKey;
import com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.SimpleClientAdapter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.internal.base.zal;
import com.google.android.gms.signin.zad;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public class GoogleApiManager implements Callback {
    private static final Object lock = new Object();
    public static final Status zahw = new Status(4, "Sign-out occurred while this API call was in progress.");
    private static final Status zahx = new Status(4, "The user must be signed in to make this API call.");
    @GuardedBy("lock")
    private static GoogleApiManager zaib;
    private final Handler handler;
    private long zahy = 5000;
    private long zahz = 120000;
    private long zaia = 10000;
    private final Context zaic;
    private final GoogleApiAvailability zaid;
    private final GoogleApiAvailabilityCache zaie;
    private final AtomicInteger zaif = new AtomicInteger(1);
    private final AtomicInteger zaig = new AtomicInteger(0);
    private final Map<zai<?>, zaa<?>> zaih = new ConcurrentHashMap(5, 0.75f, 1);
    @GuardedBy("lock")
    private zaae zaii = null;
    @GuardedBy("lock")
    private final Set<zai<?>> zaij = new ArraySet();
    private final Set<zai<?>> zaik = new ArraySet();

    private static class zab {
        private final zai<?> zaja;
        private final Feature zajb;

        private zab(zai<?> zai, Feature feature) {
            this.zaja = zai;
            this.zajb = feature;
        }

        public final boolean equals(Object obj) {
            if (obj == null || !(obj instanceof zab)) {
                return false;
            }
            zab zab = (zab) obj;
            if (!Objects.equal(this.zaja, zab.zaja) || Objects.equal(this.zajb, zab.zajb) == null) {
                return false;
            }
            return true;
        }

        public final int hashCode() {
            return Objects.hashCode(this.zaja, this.zajb);
        }

        public final String toString() {
            return Objects.toStringHelper(this).add("key", this.zaja).add("feature", this.zajb).toString();
        }
    }

    private class zac implements zach, ConnectionProgressReportCallbacks {
        private final zai<?> zafp;
        final /* synthetic */ GoogleApiManager zail;
        private final Client zain;
        private IAccountAccessor zajc = null;
        private Set<Scope> zajd = null;
        private boolean zaje = null;

        public zac(GoogleApiManager googleApiManager, Client client, zai<?> zai) {
            this.zail = googleApiManager;
            this.zain = client;
            this.zafp = zai;
        }

        public final void onReportServiceBinding(@NonNull ConnectionResult connectionResult) {
            this.zail.handler.post(new zabo(this, connectionResult));
        }

        @WorkerThread
        public final void zag(ConnectionResult connectionResult) {
            ((zaa) this.zail.zaih.get(this.zafp)).zag(connectionResult);
        }

        @WorkerThread
        public final void zaa(IAccountAccessor iAccountAccessor, Set<Scope> set) {
            if (iAccountAccessor != null) {
                if (set != null) {
                    this.zajc = iAccountAccessor;
                    this.zajd = set;
                    zabr();
                    return;
                }
            }
            Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
            zag(new ConnectionResult(4));
        }

        @WorkerThread
        private final void zabr() {
            if (this.zaje) {
                IAccountAccessor iAccountAccessor = this.zajc;
                if (iAccountAccessor != null) {
                    this.zain.getRemoteService(iAccountAccessor, this.zajd);
                }
            }
        }
    }

    public class zaa<O extends ApiOptions> implements ConnectionCallbacks, OnConnectionFailedListener, zar {
        private final zai<O> zafp;
        final /* synthetic */ GoogleApiManager zail;
        private final Queue<zab> zaim = new LinkedList();
        private final Client zain;
        private final AnyClient zaio;
        private final zaab zaip;
        private final Set<zak> zaiq = new HashSet();
        private final Map<ListenerKey<?>, zabw> zair = new HashMap();
        private final int zais;
        private final zace zait;
        private boolean zaiu;
        private final List<zab> zaiv = new ArrayList();
        private ConnectionResult zaiw = null;

        @WorkerThread
        public zaa(GoogleApiManager googleApiManager, GoogleApi<O> googleApi) {
            this.zail = googleApiManager;
            this.zain = googleApi.zaa(googleApiManager.handler.getLooper(), this);
            AnyClient anyClient = this.zain;
            if (anyClient instanceof SimpleClientAdapter) {
                this.zaio = ((SimpleClientAdapter) anyClient).getClient();
            } else {
                this.zaio = anyClient;
            }
            this.zafp = googleApi.zak();
            this.zaip = new zaab();
            this.zais = googleApi.getInstanceId();
            if (this.zain.requiresSignIn()) {
                this.zait = googleApi.zaa(googleApiManager.zaic, googleApiManager.handler);
            } else {
                this.zait = null;
            }
        }

        public final void onConnected(@Nullable Bundle bundle) {
            if (Looper.myLooper() == this.zail.handler.getLooper()) {
                zabg();
            } else {
                this.zail.handler.post(new zabj(this));
            }
        }

        @WorkerThread
        private final void zabg() {
            zabl();
            zai(ConnectionResult.RESULT_SUCCESS);
            zabn();
            Iterator it = this.zair.values().iterator();
            while (it.hasNext()) {
                zabw zabw = (zabw) it.next();
                if (zaa(zabw.zajw.getRequiredFeatures()) != null) {
                    it.remove();
                } else {
                    try {
                        zabw.zajw.registerListener(this.zaio, new TaskCompletionSource());
                    } catch (DeadObjectException e) {
                        onConnectionSuspended(1);
                        this.zain.disconnect();
                    } catch (RemoteException e2) {
                        it.remove();
                    }
                }
            }
            zabi();
            zabo();
        }

        public final void onConnectionSuspended(int i) {
            if (Looper.myLooper() == this.zail.handler.getLooper()) {
                zabh();
            } else {
                this.zail.handler.post(new zabk(this));
            }
        }

        @WorkerThread
        private final void zabh() {
            zabl();
            this.zaiu = true;
            this.zaip.zaai();
            this.zail.handler.sendMessageDelayed(Message.obtain(this.zail.handler, 9, this.zafp), this.zail.zahy);
            this.zail.handler.sendMessageDelayed(Message.obtain(this.zail.handler, 11, this.zafp), this.zail.zahz);
            this.zail.zaie.flush();
        }

        @WorkerThread
        public final void zag(@NonNull ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(this.zail.handler);
            this.zain.disconnect();
            onConnectionFailed(connectionResult);
        }

        @WorkerThread
        private final boolean zah(@NonNull ConnectionResult connectionResult) {
            synchronized (GoogleApiManager.lock) {
                if (this.zail.zaii == null || !this.zail.zaij.contains(this.zafp)) {
                    return null;
                }
                this.zail.zaii.zab(connectionResult, this.zais);
                return true;
            }
        }

        public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean z) {
            if (Looper.myLooper() == this.zail.handler.getLooper()) {
                onConnectionFailed(connectionResult);
            } else {
                this.zail.handler.post(new zabl(this, connectionResult));
            }
        }

        @WorkerThread
        public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(this.zail.handler);
            zace zace = this.zait;
            if (zace != null) {
                zace.zabs();
            }
            zabl();
            this.zail.zaie.flush();
            zai(connectionResult);
            if (connectionResult.getErrorCode() == 4) {
                zac(GoogleApiManager.zahx);
            } else if (this.zaim.isEmpty()) {
                this.zaiw = connectionResult;
            } else {
                if (!(zah(connectionResult) || this.zail.zac(connectionResult, this.zais))) {
                    if (connectionResult.getErrorCode() == 18) {
                        this.zaiu = true;
                    }
                    if (this.zaiu != null) {
                        this.zail.handler.sendMessageDelayed(Message.obtain(this.zail.handler, 9, this.zafp), this.zail.zahy);
                        return;
                    }
                    String zan = this.zafp.zan();
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(zan).length() + 38);
                    stringBuilder.append("API: ");
                    stringBuilder.append(zan);
                    stringBuilder.append(" is not available on this device.");
                    zac(new Status(17, stringBuilder.toString()));
                }
            }
        }

        @WorkerThread
        private final void zabi() {
            ArrayList arrayList = new ArrayList(this.zaim);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                zab zab = (zab) obj;
                if (!this.zain.isConnected()) {
                    return;
                }
                if (zab(zab)) {
                    this.zaim.remove(zab);
                }
            }
        }

        @WorkerThread
        public final void zaa(zab zab) {
            Preconditions.checkHandlerThread(this.zail.handler);
            if (!this.zain.isConnected()) {
                this.zaim.add(zab);
                zab = this.zaiw;
                if (zab == null || zab.hasResolution() == null) {
                    connect();
                } else {
                    onConnectionFailed(this.zaiw);
                }
            } else if (zab(zab)) {
                zabo();
            } else {
                this.zaim.add(zab);
            }
        }

        @WorkerThread
        public final void zabj() {
            Preconditions.checkHandlerThread(this.zail.handler);
            zac(GoogleApiManager.zahw);
            this.zaip.zaah();
            for (ListenerKey zah : (ListenerKey[]) this.zair.keySet().toArray(new ListenerKey[this.zair.size()])) {
                zaa(new zah(zah, new TaskCompletionSource()));
            }
            zai(new ConnectionResult(4));
            if (this.zain.isConnected()) {
                this.zain.onUserSignOut(new zabm(this));
            }
        }

        public final Client zaab() {
            return this.zain;
        }

        public final Map<ListenerKey<?>, zabw> zabk() {
            return this.zair;
        }

        @WorkerThread
        public final void zabl() {
            Preconditions.checkHandlerThread(this.zail.handler);
            this.zaiw = null;
        }

        @WorkerThread
        public final ConnectionResult zabm() {
            Preconditions.checkHandlerThread(this.zail.handler);
            return this.zaiw;
        }

        @WorkerThread
        private final boolean zab(zab zab) {
            if (zab instanceof zac) {
                zac zac = (zac) zab;
                Feature zaa = zaa(zac.zab(this));
                if (zaa == null) {
                    zac(zab);
                    return true;
                }
                if (zac.zac(this) != null) {
                    zab = new zab(this.zafp, zaa);
                    int indexOf = this.zaiv.indexOf(zab);
                    if (indexOf >= 0) {
                        zab zab2 = (zab) this.zaiv.get(indexOf);
                        this.zail.handler.removeMessages(15, zab2);
                        this.zail.handler.sendMessageDelayed(Message.obtain(this.zail.handler, 15, zab2), this.zail.zahy);
                    } else {
                        this.zaiv.add(zab);
                        this.zail.handler.sendMessageDelayed(Message.obtain(this.zail.handler, 15, zab), this.zail.zahy);
                        this.zail.handler.sendMessageDelayed(Message.obtain(this.zail.handler, 16, zab), this.zail.zahz);
                        zab = new ConnectionResult(2, null);
                        if (!zah(zab)) {
                            this.zail.zac(zab, this.zais);
                        }
                    }
                } else {
                    zac.zaa(new UnsupportedApiCallException(zaa));
                }
                return null;
            }
            zac(zab);
            return true;
        }

        @WorkerThread
        private final void zac(zab zab) {
            zab.zaa(this.zaip, requiresSignIn());
            try {
                zab.zaa(this);
            } catch (DeadObjectException e) {
                onConnectionSuspended(1);
                this.zain.disconnect();
            }
        }

        @WorkerThread
        public final void zac(Status status) {
            Preconditions.checkHandlerThread(this.zail.handler);
            for (zab zaa : this.zaim) {
                zaa.zaa(status);
            }
            this.zaim.clear();
        }

        @WorkerThread
        public final void resume() {
            Preconditions.checkHandlerThread(this.zail.handler);
            if (this.zaiu) {
                connect();
            }
        }

        @WorkerThread
        private final void zabn() {
            if (this.zaiu) {
                this.zail.handler.removeMessages(11, this.zafp);
                this.zail.handler.removeMessages(9, this.zafp);
                this.zaiu = false;
            }
        }

        @WorkerThread
        public final void zaav() {
            Preconditions.checkHandlerThread(this.zail.handler);
            if (this.zaiu) {
                Status status;
                zabn();
                if (this.zail.zaid.isGooglePlayServicesAvailable(this.zail.zaic) == 18) {
                    status = new Status(8, "Connection timed out while waiting for Google Play services update to complete.");
                } else {
                    status = new Status(8, "API failed to connect while resuming due to an unknown error.");
                }
                zac(status);
                this.zain.disconnect();
            }
        }

        private final void zabo() {
            this.zail.handler.removeMessages(12, this.zafp);
            this.zail.handler.sendMessageDelayed(this.zail.handler.obtainMessage(12, this.zafp), this.zail.zaia);
        }

        @WorkerThread
        public final boolean zabp() {
            return zac(true);
        }

        @WorkerThread
        private final boolean zac(boolean z) {
            Preconditions.checkHandlerThread(this.zail.handler);
            if (!this.zain.isConnected() || this.zair.size() != 0) {
                return false;
            }
            if (this.zaip.zaag()) {
                if (z) {
                    zabo();
                }
                return false;
            }
            this.zain.disconnect();
            return true;
        }

        @WorkerThread
        public final void connect() {
            Preconditions.checkHandlerThread(this.zail.handler);
            if (!this.zain.isConnected()) {
                if (!this.zain.isConnecting()) {
                    int clientAvailability = this.zail.zaie.getClientAvailability(this.zail.zaic, this.zain);
                    if (clientAvailability != 0) {
                        onConnectionFailed(new ConnectionResult(clientAvailability, null));
                        return;
                    }
                    zach zac = new zac(this.zail, this.zain, this.zafp);
                    if (this.zain.requiresSignIn()) {
                        this.zait.zaa(zac);
                    }
                    this.zain.connect(zac);
                }
            }
        }

        @WorkerThread
        public final void zaa(zak zak) {
            Preconditions.checkHandlerThread(this.zail.handler);
            this.zaiq.add(zak);
        }

        @WorkerThread
        private final void zai(ConnectionResult connectionResult) {
            for (zak zak : this.zaiq) {
                String str = null;
                if (Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS)) {
                    str = this.zain.getEndpointPackageName();
                }
                zak.zaa(this.zafp, connectionResult, str);
            }
            this.zaiq.clear();
        }

        final boolean isConnected() {
            return this.zain.isConnected();
        }

        public final boolean requiresSignIn() {
            return this.zain.requiresSignIn();
        }

        public final int getInstanceId() {
            return this.zais;
        }

        final zad zabq() {
            zace zace = this.zait;
            return zace == null ? null : zace.zabq();
        }

        @Nullable
        @WorkerThread
        private final Feature zaa(@Nullable Feature[] featureArr) {
            if (featureArr != null) {
                if (featureArr.length != 0) {
                    Feature[] availableFeatures = this.zain.getAvailableFeatures();
                    int i = 0;
                    if (availableFeatures == null) {
                        availableFeatures = new Feature[0];
                    }
                    Map arrayMap = new ArrayMap(availableFeatures.length);
                    for (Feature feature : availableFeatures) {
                        arrayMap.put(feature.getName(), Long.valueOf(feature.getVersion()));
                    }
                    int length = featureArr.length;
                    while (i < length) {
                        Feature feature2 = featureArr[i];
                        if (arrayMap.containsKey(feature2.getName())) {
                            if (((Long) arrayMap.get(feature2.getName())).longValue() >= feature2.getVersion()) {
                                i++;
                            }
                        }
                        return feature2;
                    }
                    return null;
                }
            }
            return null;
        }

        @WorkerThread
        private final void zaa(zab zab) {
            if (this.zaiv.contains(zab) != null && this.zaiu == null) {
                if (this.zain.isConnected() == null) {
                    connect();
                    return;
                }
                zabi();
            }
        }

        @WorkerThread
        private final void zab(zab zab) {
            if (this.zaiv.remove(zab)) {
                this.zail.handler.removeMessages(15, zab);
                this.zail.handler.removeMessages(16, zab);
                Object zad = zab.zajb;
                ArrayList arrayList = new ArrayList(this.zaim.size());
                for (zab zab2 : this.zaim) {
                    if (zab2 instanceof zac) {
                        Object[] zab3 = ((zac) zab2).zab(this);
                        if (zab3 != null && ArrayUtils.contains(zab3, zad)) {
                            arrayList.add(zab2);
                        }
                    }
                }
                arrayList = arrayList;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    zab zab4 = (zab) obj;
                    this.zaim.remove(zab4);
                    zab4.zaa(new UnsupportedApiCallException(zad));
                }
            }
        }
    }

    public static GoogleApiManager zab(Context context) {
        synchronized (lock) {
            if (zaib == null) {
                HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                zaib = new GoogleApiManager(context.getApplicationContext(), handlerThread.getLooper(), GoogleApiAvailability.getInstance());
            }
            context = zaib;
        }
        return context;
    }

    public static GoogleApiManager zabc() {
        GoogleApiManager googleApiManager;
        synchronized (lock) {
            Preconditions.checkNotNull(zaib, "Must guarantee manager is non-null before using getInstance");
            googleApiManager = zaib;
        }
        return googleApiManager;
    }

    @KeepForSdk
    public static void reportSignOut() {
        synchronized (lock) {
            if (zaib != null) {
                GoogleApiManager googleApiManager = zaib;
                googleApiManager.zaig.incrementAndGet();
                googleApiManager.handler.sendMessageAtFrontOfQueue(googleApiManager.handler.obtainMessage(10));
            }
        }
    }

    @KeepForSdk
    private GoogleApiManager(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.zaic = context;
        this.handler = new zal(looper, this);
        this.zaid = googleApiAvailability;
        this.zaie = new GoogleApiAvailabilityCache(googleApiAvailability);
        context = this.handler;
        context.sendMessage(context.obtainMessage(6));
    }

    public final int zabd() {
        return this.zaif.getAndIncrement();
    }

    public final void zaa(GoogleApi<?> googleApi) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(7, googleApi));
    }

    @WorkerThread
    private final void zab(GoogleApi<?> googleApi) {
        zai zak = googleApi.zak();
        zaa zaa = (zaa) this.zaih.get(zak);
        if (zaa == null) {
            zaa = new zaa(this, googleApi);
            this.zaih.put(zak, zaa);
        }
        if (zaa.requiresSignIn() != null) {
            this.zaik.add(zak);
        }
        zaa.connect();
    }

    public final void zaa(@NonNull zaae zaae) {
        synchronized (lock) {
            if (this.zaii != zaae) {
                this.zaii = zaae;
                this.zaij.clear();
            }
            this.zaij.addAll(zaae.zaaj());
        }
    }

    final void zab(@NonNull zaae zaae) {
        synchronized (lock) {
            if (this.zaii == zaae) {
                this.zaii = null;
                this.zaij.clear();
            }
        }
    }

    public final Task<Map<zai<?>, String>> zaa(Iterable<? extends GoogleApi<?>> iterable) {
        zak zak = new zak(iterable);
        iterable = this.handler;
        iterable.sendMessage(iterable.obtainMessage(2, zak));
        return zak.getTask();
    }

    public final void zao() {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(3));
    }

    final void maybeSignOut() {
        this.zaig.incrementAndGet();
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(10));
    }

    public final Task<Boolean> zac(GoogleApi<?> googleApi) {
        zaaf zaaf = new zaaf(googleApi.zak());
        googleApi = this.handler;
        googleApi.sendMessage(googleApi.obtainMessage(14, zaaf));
        return zaaf.zaal().getTask();
    }

    public final <O extends ApiOptions> void zaa(GoogleApi<O> googleApi, int i, ApiMethodImpl<? extends Result, AnyClient> apiMethodImpl) {
        zab zae = new zae(i, apiMethodImpl);
        i = this.handler;
        i.sendMessage(i.obtainMessage(4, new zabv(zae, this.zaig.get(), googleApi)));
    }

    public final <O extends ApiOptions, ResultT> void zaa(GoogleApi<O> googleApi, int i, TaskApiCall<AnyClient, ResultT> taskApiCall, TaskCompletionSource<ResultT> taskCompletionSource, StatusExceptionMapper statusExceptionMapper) {
        zab zag = new zag(i, taskApiCall, taskCompletionSource, statusExceptionMapper);
        i = this.handler;
        i.sendMessage(i.obtainMessage(4, new zabv(zag, this.zaig.get(), googleApi)));
    }

    public final <O extends ApiOptions> Task<Void> zaa(@NonNull GoogleApi<O> googleApi, @NonNull RegisterListenerMethod<AnyClient, ?> registerListenerMethod, @NonNull UnregisterListenerMethod<AnyClient, ?> unregisterListenerMethod) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zab zaf = new zaf(new zabw(registerListenerMethod, unregisterListenerMethod), taskCompletionSource);
        registerListenerMethod = this.handler;
        registerListenerMethod.sendMessage(registerListenerMethod.obtainMessage(8, new zabv(zaf, this.zaig.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final <O extends ApiOptions> Task<Boolean> zaa(@NonNull GoogleApi<O> googleApi, @NonNull ListenerKey<?> listenerKey) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zab zah = new zah(listenerKey, taskCompletionSource);
        listenerKey = this.handler;
        listenerKey.sendMessage(listenerKey.obtainMessage(13, new zabv(zah, this.zaig.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    @WorkerThread
    public boolean handleMessage(Message message) {
        long j = 300000;
        zaa zaa;
        zaa zaa2;
        zai zak;
        zab zab;
        switch (message.what) {
            case 1:
                if (((Boolean) message.obj).booleanValue() != null) {
                    j = 10000;
                }
                this.zaia = j;
                this.handler.removeMessages(12);
                for (zai zai : this.zaih.keySet()) {
                    Handler handler = this.handler;
                    handler.sendMessageDelayed(handler.obtainMessage(12, zai), this.zaia);
                }
                break;
            case 2:
                zak zak2 = (zak) message.obj;
                for (zai zai2 : zak2.zap()) {
                    zaa = (zaa) this.zaih.get(zai2);
                    if (zaa == null) {
                        zak2.zaa(zai2, new ConnectionResult(13), null);
                        break;
                    } else if (zaa.isConnected()) {
                        zak2.zaa(zai2, ConnectionResult.RESULT_SUCCESS, zaa.zaab().getEndpointPackageName());
                    } else if (zaa.zabm() != null) {
                        zak2.zaa(zai2, zaa.zabm(), null);
                    } else {
                        zaa.zaa(zak2);
                        zaa.connect();
                    }
                }
                break;
            case 3:
                for (zaa zaa22 : this.zaih.values()) {
                    zaa22.zabl();
                    zaa22.connect();
                }
                break;
            case 4:
            case 8:
            case 13:
                zabv zabv = (zabv) message.obj;
                zaa22 = (zaa) this.zaih.get(zabv.zajs.zak());
                if (zaa22 == null) {
                    zab(zabv.zajs);
                    zaa22 = (zaa) this.zaih.get(zabv.zajs.zak());
                }
                if (zaa22.requiresSignIn() && this.zaig.get() != zabv.zajr) {
                    zabv.zajq.zaa(zahw);
                    zaa22.zabj();
                    break;
                }
                zaa22.zaa(zabv.zajq);
                break;
            case 5:
                StringBuilder stringBuilder;
                String errorString;
                StringBuilder stringBuilder2;
                int i = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                for (zaa zaa3 : this.zaih.values()) {
                    if (zaa3.getInstanceId() == i) {
                        if (zaa3 != null) {
                            stringBuilder = new StringBuilder(76);
                            stringBuilder.append("Could not find API instance ");
                            stringBuilder.append(i);
                            stringBuilder.append(" while trying to fail enqueued calls.");
                            Log.wtf("GoogleApiManager", stringBuilder.toString(), new Exception());
                            break;
                        }
                        errorString = this.zaid.getErrorString(connectionResult.getErrorCode());
                        message = connectionResult.getErrorMessage();
                        stringBuilder2 = new StringBuilder((String.valueOf(errorString).length() + 69) + String.valueOf(message).length());
                        stringBuilder2.append("Error resolution was canceled by the user, original error message: ");
                        stringBuilder2.append(errorString);
                        stringBuilder2.append(": ");
                        stringBuilder2.append(message);
                        zaa3.zac(new Status(17, stringBuilder2.toString()));
                        break;
                    }
                }
                zaa3 = null;
                if (zaa3 != null) {
                    stringBuilder = new StringBuilder(76);
                    stringBuilder.append("Could not find API instance ");
                    stringBuilder.append(i);
                    stringBuilder.append(" while trying to fail enqueued calls.");
                    Log.wtf("GoogleApiManager", stringBuilder.toString(), new Exception());
                } else {
                    errorString = this.zaid.getErrorString(connectionResult.getErrorCode());
                    message = connectionResult.getErrorMessage();
                    stringBuilder2 = new StringBuilder((String.valueOf(errorString).length() + 69) + String.valueOf(message).length());
                    stringBuilder2.append("Error resolution was canceled by the user, original error message: ");
                    stringBuilder2.append(errorString);
                    stringBuilder2.append(": ");
                    stringBuilder2.append(message);
                    zaa3.zac(new Status(17, stringBuilder2.toString()));
                }
            case 6:
                if (!(PlatformVersion.isAtLeastIceCreamSandwich() == null || (this.zaic.getApplicationContext() instanceof Application) == null)) {
                    BackgroundDetector.initialize((Application) this.zaic.getApplicationContext());
                    BackgroundDetector.getInstance().addListener(new zabi(this));
                    if (BackgroundDetector.getInstance().readCurrentStateIfPossible(true) == null) {
                        this.zaia = 300000;
                    }
                }
                break;
            case 7:
                zab((GoogleApi) message.obj);
                break;
            case 9:
                if (this.zaih.containsKey(message.obj)) {
                    ((zaa) this.zaih.get(message.obj)).resume();
                    break;
                }
                break;
            case 10:
                for (zai zak3 : this.zaik) {
                    ((zaa) this.zaih.remove(zak3)).zabj();
                }
                this.zaik.clear();
                break;
            case 11:
                if (this.zaih.containsKey(message.obj)) {
                    ((zaa) this.zaih.get(message.obj)).zaav();
                    break;
                }
                break;
            case 12:
                if (this.zaih.containsKey(message.obj)) {
                    ((zaa) this.zaih.get(message.obj)).zabp();
                    break;
                }
                break;
            case 14:
                zaaf zaaf = (zaaf) message.obj;
                zak3 = zaaf.zak();
                if (!this.zaih.containsKey(zak3)) {
                    zaaf.zaal().setResult(Boolean.valueOf(false));
                    break;
                }
                zaaf.zaal().setResult(Boolean.valueOf(((zaa) this.zaih.get(zak3)).zac(false)));
                break;
            case 15:
                zab = (zab) message.obj;
                if (this.zaih.containsKey(zab.zaja)) {
                    ((zaa) this.zaih.get(zab.zaja)).zaa(zab);
                    break;
                }
                break;
            case 16:
                zab = (zab) message.obj;
                if (this.zaih.containsKey(zab.zaja)) {
                    ((zaa) this.zaih.get(zab.zaja)).zab(zab);
                    break;
                }
                break;
            default:
                message = message.what;
                StringBuilder stringBuilder3 = new StringBuilder(31);
                stringBuilder3.append("Unknown message id: ");
                stringBuilder3.append(message);
                Log.w("GoogleApiManager", stringBuilder3.toString());
                return false;
        }
        return true;
    }

    final PendingIntent zaa(zai<?> zai, int i) {
        zaa zaa = (zaa) this.zaih.get(zai);
        if (zaa == null) {
            return null;
        }
        zai = zaa.zabq();
        if (zai == null) {
            return null;
        }
        return PendingIntent.getActivity(this.zaic, i, zai.getSignInIntent(), 134217728);
    }

    final boolean zac(ConnectionResult connectionResult, int i) {
        return this.zaid.zaa(this.zaic, connectionResult, i);
    }

    public final void zaa(ConnectionResult connectionResult, int i) {
        if (!zac(connectionResult, i)) {
            Handler handler = this.handler;
            handler.sendMessage(handler.obtainMessage(5, i, 0, connectionResult));
        }
    }
}
