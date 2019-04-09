package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.SimpleClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class SimpleClientAdapter<T extends IInterface> extends GmsClient<T> {
    private final SimpleClient<T> zapf;

    public SimpleClientAdapter(Context context, Looper looper, int i, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, ClientSettings clientSettings, SimpleClient<T> simpleClient) {
        super(context, looper, i, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zapf = simpleClient;
    }

    protected String getStartServiceAction() {
        return this.zapf.getStartServiceAction();
    }

    protected String getServiceDescriptor() {
        return this.zapf.getServiceDescriptor();
    }

    protected T createServiceInterface(IBinder iBinder) {
        return this.zapf.createServiceInterface(iBinder);
    }

    protected void onSetConnectState(int i, T t) {
        this.zapf.setState(i, t);
    }

    public SimpleClient<T> getClient() {
        return this.zapf;
    }

    public int getMinApkVersion() {
        return super.getMinApkVersion();
    }
}
