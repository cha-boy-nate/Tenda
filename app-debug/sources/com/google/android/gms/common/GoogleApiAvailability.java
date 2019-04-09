package com.google.android.gms.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.GuardedBy;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.gms.base.C0235R;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zabr;
import com.google.android.gms.common.api.internal.zabu;
import com.google.android.gms.common.internal.ConnectionErrorMessages;
import com.google.android.gms.common.internal.DialogRedirect;
import com.google.android.gms.common.internal.HideFirstParty;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.internal.base.zal;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.Arrays;

public class GoogleApiAvailability extends GoogleApiAvailabilityLight {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final Object mLock = new Object();
    private static final GoogleApiAvailability zaao = new GoogleApiAvailability();
    @GuardedBy("mLock")
    private String zaap;

    @SuppressLint({"HandlerLeak"})
    private class zaa extends zal {
        private final Context zaaq;
        private final /* synthetic */ GoogleApiAvailability zaar;

        public zaa(GoogleApiAvailability googleApiAvailability, Context context) {
            this.zaar = googleApiAvailability;
            super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
            this.zaaq = context.getApplicationContext();
        }

        public final void handleMessage(Message message) {
            if (message.what != 1) {
                message = message.what;
                StringBuilder stringBuilder = new StringBuilder(50);
                stringBuilder.append("Don't know how to handle this message: ");
                stringBuilder.append(message);
                Log.w("GoogleApiAvailability", stringBuilder.toString());
            } else {
                int isGooglePlayServicesAvailable = this.zaar.isGooglePlayServicesAvailable(this.zaaq);
                if (this.zaar.isUserResolvableError(isGooglePlayServicesAvailable)) {
                    this.zaar.showErrorNotification(this.zaaq, isGooglePlayServicesAvailable);
                }
            }
        }
    }

    public static GoogleApiAvailability getInstance() {
        return zaao;
    }

    GoogleApiAvailability() {
    }

    @MainThread
    public Task<Void> makeGooglePlayServicesAvailable(Activity activity) {
        int i = GOOGLE_PLAY_SERVICES_VERSION_CODE;
        Preconditions.checkMainThread("makeGooglePlayServicesAvailable must be called from the main thread");
        i = isGooglePlayServicesAvailable(activity, i);
        if (i == 0) {
            return Tasks.forResult(null);
        }
        activity = zabu.zac(activity);
        activity.zab(new ConnectionResult(i, null), 0);
        return activity.getTask();
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2) {
        return getErrorDialog(activity, i, i2, null);
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        return zaa((Context) activity, i, DialogRedirect.getInstance(activity, getErrorResolutionIntent(activity, i, "d"), i2), onCancelListener);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2) {
        return showErrorDialogFragment(activity, i, i2, null);
    }

    public final boolean zaa(Activity activity, @NonNull LifecycleFragment lifecycleFragment, int i, int i2, OnCancelListener onCancelListener) {
        Dialog zaa = zaa((Context) activity, i, DialogRedirect.getInstance(lifecycleFragment, getErrorResolutionIntent(activity, i, "d"), 2), onCancelListener);
        if (zaa == null) {
            return null;
        }
        zaa(activity, zaa, GooglePlayServicesUtil.GMS_ERROR_DIALOG, onCancelListener);
        return true;
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        Dialog errorDialog = getErrorDialog(activity, i, i2, onCancelListener);
        if (errorDialog == 0) {
            return null;
        }
        zaa(activity, errorDialog, GooglePlayServicesUtil.GMS_ERROR_DIALOG, onCancelListener);
        return true;
    }

    public void showErrorNotification(Context context, int i) {
        zaa(context, i, null, getErrorResolutionPendingIntent(context, i, 0, "n"));
    }

    public void showErrorNotification(Context context, ConnectionResult connectionResult) {
        zaa(context, connectionResult.getErrorCode(), null, getErrorResolutionPendingIntent(context, connectionResult));
    }

    public final boolean zaa(Context context, ConnectionResult connectionResult, int i) {
        PendingIntent errorResolutionPendingIntent = getErrorResolutionPendingIntent(context, connectionResult);
        if (errorResolutionPendingIntent == null) {
            return null;
        }
        zaa(context, connectionResult.getErrorCode(), null, GoogleApiActivity.zaa(context, errorResolutionPendingIntent, i));
        return true;
    }

    public static Dialog zaa(Activity activity, OnCancelListener onCancelListener) {
        View progressBar = new ProgressBar(activity, null, 16842874);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        Builder builder = new Builder(activity);
        builder.setView(progressBar);
        builder.setMessage(ConnectionErrorMessages.getErrorMessage(activity, 18));
        builder.setPositiveButton("", null);
        Dialog create = builder.create();
        zaa(activity, create, "GooglePlayServicesUpdatingDialog", onCancelListener);
        return create;
    }

    @Nullable
    public final zabq zaa(Context context, zabr zabr) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        BroadcastReceiver zabq = new zabq(zabr);
        context.registerReceiver(zabq, intentFilter);
        zabq.zac(context);
        if (isUninstalledAppPossiblyUpdating(context, "com.google.android.gms") != null) {
            return zabq;
        }
        zabr.zas();
        zabq.unregister();
        return null;
    }

    public Task<Void> checkApiAvailability(GoogleApi<?> googleApi, GoogleApi<?>... googleApiArr) {
        Preconditions.checkNotNull(googleApi, "Requested API must not be null.");
        for (Object checkNotNull : googleApiArr) {
            Preconditions.checkNotNull(checkNotNull, "Requested API must not be null.");
        }
        Iterable arrayList = new ArrayList(googleApiArr.length + 1);
        arrayList.add(googleApi);
        arrayList.addAll(Arrays.asList(googleApiArr));
        return GoogleApiManager.zabc().zaa(arrayList).continueWith(new zaa(this));
    }

    @VisibleForTesting(otherwise = 2)
    private final String zag() {
        String str;
        synchronized (mLock) {
            str = this.zaap;
        }
        return str;
    }

    @TargetApi(26)
    public void setDefaultNotificationChannelId(@NonNull Context context, @NonNull String str) {
        if (PlatformVersion.isAtLeastO()) {
            Preconditions.checkNotNull(((NotificationManager) context.getSystemService("notification")).getNotificationChannel(str));
        }
        synchronized (mLock) {
            this.zaap = str;
        }
    }

    @HideFirstParty
    public int isGooglePlayServicesAvailable(Context context) {
        return super.isGooglePlayServicesAvailable(context);
    }

    @ShowFirstParty
    @KeepForSdk
    public int isGooglePlayServicesAvailable(Context context, int i) {
        return super.isGooglePlayServicesAvailable(context, i);
    }

    public final boolean isUserResolvableError(int i) {
        return super.isUserResolvableError(i);
    }

    @Nullable
    @ShowFirstParty
    @KeepForSdk
    public Intent getErrorResolutionIntent(Context context, int i, @Nullable String str) {
        return super.getErrorResolutionIntent(context, i, str);
    }

    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return super.getErrorResolutionPendingIntent(context, i, i2);
    }

    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            return connectionResult.getResolution();
        }
        return getErrorResolutionPendingIntent(context, connectionResult.getErrorCode(), 0);
    }

    @ShowFirstParty
    @KeepForSdk
    public int getClientVersion(Context context) {
        return super.getClientVersion(context);
    }

    public final String getErrorString(int i) {
        return super.getErrorString(i);
    }

    static Dialog zaa(Context context, int i, DialogRedirect dialogRedirect, OnCancelListener onCancelListener) {
        Builder builder = null;
        if (i == 0) {
            return null;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843529, typedValue, true);
        if ("Theme.Dialog.Alert".equals(context.getResources().getResourceEntryName(typedValue.resourceId))) {
            builder = new Builder(context, 5);
        }
        if (builder == null) {
            builder = new Builder(context);
        }
        builder.setMessage(ConnectionErrorMessages.getErrorMessage(context, i));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        onCancelListener = ConnectionErrorMessages.getErrorDialogButtonMessage(context, i);
        if (onCancelListener != null) {
            builder.setPositiveButton(onCancelListener, dialogRedirect);
        }
        context = ConnectionErrorMessages.getErrorTitle(context, i);
        if (context != null) {
            builder.setTitle(context);
        }
        return builder.create();
    }

    static void zaa(Activity activity, Dialog dialog, String str, OnCancelListener onCancelListener) {
        if (activity instanceof FragmentActivity) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
            return;
        }
        ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
    }

    @TargetApi(20)
    private final void zaa(Context context, int i, String str, PendingIntent pendingIntent) {
        if (i == 18) {
            zaa(context);
        } else if (pendingIntent == null) {
            if (i == 6) {
                Log.w("GoogleApiAvailability", "Missing resolution for ConnectionResult.RESOLUTION_REQUIRED. Call GoogleApiAvailability#showErrorNotification(Context, ConnectionResult) instead.");
            }
        } else {
            str = ConnectionErrorMessages.getErrorNotificationTitle(context, i);
            CharSequence errorNotificationMessage = ConnectionErrorMessages.getErrorNotificationMessage(context, i);
            Resources resources = context.getResources();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            str = new NotificationCompat.Builder(context).setLocalOnly(true).setAutoCancel(true).setContentTitle(str).setStyle(new BigTextStyle().bigText(errorNotificationMessage));
            if (DeviceProperties.isWearable(context)) {
                Preconditions.checkState(PlatformVersion.isAtLeastKitKatWatch());
                str.setSmallIcon(context.getApplicationInfo().icon).setPriority(2);
                if (DeviceProperties.isWearableWithoutPlayStore(context)) {
                    str.addAction(C0235R.drawable.common_full_open_on_phone, resources.getString(C0235R.string.common_open_on_phone), pendingIntent);
                } else {
                    str.setContentIntent(pendingIntent);
                }
            } else {
                str.setSmallIcon(17301642).setTicker(resources.getString(C0235R.string.common_google_play_services_notification_ticker)).setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent).setContentText(errorNotificationMessage);
            }
            if (PlatformVersion.isAtLeastO() != null) {
                Preconditions.checkState(PlatformVersion.isAtLeastO());
                pendingIntent = zag();
                if (pendingIntent == null) {
                    pendingIntent = "com.google.android.gms.availability";
                    NotificationChannel notificationChannel = notificationManager.getNotificationChannel(pendingIntent);
                    context = ConnectionErrorMessages.getDefaultNotificationChannelName(context);
                    if (notificationChannel == null) {
                        notificationManager.createNotificationChannel(new NotificationChannel(pendingIntent, context, 4));
                    } else if (!context.equals(notificationChannel.getName())) {
                        notificationChannel.setName(context);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }
                str.setChannelId(pendingIntent);
            }
            context = str.build();
            switch (i) {
                case 1:
                case 2:
                case 3:
                    i = 10436;
                    GooglePlayServicesUtilLight.sCanceledAvailabilityNotification.set(null);
                    break;
                default:
                    i = 39789;
                    break;
            }
            notificationManager.notify(i, context);
        }
    }

    final void zaa(Context context) {
        new zaa(this, context).sendEmptyMessageDelayed(1, 120000);
    }
}
