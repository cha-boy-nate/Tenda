package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.ConnectionErrorMessages;
import java.util.LinkedList;

@KeepForSdk
public abstract class DeferredLifecycleHelper<T extends LifecycleDelegate> {
    private T zare;
    private Bundle zarf;
    private LinkedList<zaa> zarg;
    private final OnDelegateCreatedListener<T> zarh = new zaa(this);

    private interface zaa {
        int getState();

        void zaa(LifecycleDelegate lifecycleDelegate);
    }

    @KeepForSdk
    protected abstract void createDelegate(OnDelegateCreatedListener<T> onDelegateCreatedListener);

    @KeepForSdk
    public T getDelegate() {
        return this.zare;
    }

    private final void zal(int i) {
        while (!this.zarg.isEmpty() && ((zaa) this.zarg.getLast()).getState() >= i) {
            this.zarg.removeLast();
        }
    }

    private final void zaa(Bundle bundle, zaa zaa) {
        LifecycleDelegate lifecycleDelegate = this.zare;
        if (lifecycleDelegate != null) {
            zaa.zaa(lifecycleDelegate);
            return;
        }
        if (this.zarg == null) {
            this.zarg = new LinkedList();
        }
        this.zarg.add(zaa);
        if (bundle != null) {
            zaa = this.zarf;
            if (zaa == null) {
                this.zarf = (Bundle) bundle.clone();
            } else {
                zaa.putAll(bundle);
            }
        }
        createDelegate(this.zarh);
    }

    @KeepForSdk
    public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
        zaa(bundle2, new zab(this, activity, bundle, bundle2));
    }

    @KeepForSdk
    public void onCreate(Bundle bundle) {
        zaa(bundle, new zac(this, bundle));
    }

    @KeepForSdk
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View frameLayout = new FrameLayout(layoutInflater.getContext());
        zaa(bundle, new zad(this, frameLayout, layoutInflater, viewGroup, bundle));
        if (this.zare == null) {
            handleGooglePlayUnavailable(frameLayout);
        }
        return frameLayout;
    }

    @KeepForSdk
    protected void handleGooglePlayUnavailable(FrameLayout frameLayout) {
        showGooglePlayUnavailableMessage(frameLayout);
    }

    @KeepForSdk
    public static void showGooglePlayUnavailableMessage(FrameLayout frameLayout) {
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        Context context = frameLayout.getContext();
        int isGooglePlayServicesAvailable = instance.isGooglePlayServicesAvailable(context);
        CharSequence errorMessage = ConnectionErrorMessages.getErrorMessage(context, isGooglePlayServicesAvailable);
        CharSequence errorDialogButtonMessage = ConnectionErrorMessages.getErrorDialogButtonMessage(context, isGooglePlayServicesAvailable);
        View linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        View textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(errorMessage);
        linearLayout.addView(textView);
        frameLayout = instance.getErrorResolutionIntent(context, isGooglePlayServicesAvailable, null);
        if (frameLayout != null) {
            View button = new Button(context);
            button.setId(16908313);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(errorDialogButtonMessage);
            linearLayout.addView(button);
            button.setOnClickListener(new zae(context, frameLayout));
        }
    }

    @KeepForSdk
    public void onStart() {
        zaa(null, new zaf(this));
    }

    @KeepForSdk
    public void onResume() {
        zaa(null, new zag(this));
    }

    @KeepForSdk
    public void onPause() {
        LifecycleDelegate lifecycleDelegate = this.zare;
        if (lifecycleDelegate != null) {
            lifecycleDelegate.onPause();
        } else {
            zal(5);
        }
    }

    @KeepForSdk
    public void onStop() {
        LifecycleDelegate lifecycleDelegate = this.zare;
        if (lifecycleDelegate != null) {
            lifecycleDelegate.onStop();
        } else {
            zal(4);
        }
    }

    @KeepForSdk
    public void onDestroyView() {
        LifecycleDelegate lifecycleDelegate = this.zare;
        if (lifecycleDelegate != null) {
            lifecycleDelegate.onDestroyView();
        } else {
            zal(2);
        }
    }

    @KeepForSdk
    public void onDestroy() {
        LifecycleDelegate lifecycleDelegate = this.zare;
        if (lifecycleDelegate != null) {
            lifecycleDelegate.onDestroy();
        } else {
            zal(1);
        }
    }

    @KeepForSdk
    public void onSaveInstanceState(Bundle bundle) {
        LifecycleDelegate lifecycleDelegate = this.zare;
        if (lifecycleDelegate != null) {
            lifecycleDelegate.onSaveInstanceState(bundle);
            return;
        }
        Bundle bundle2 = this.zarf;
        if (bundle2 != null) {
            bundle.putAll(bundle2);
        }
    }

    @KeepForSdk
    public void onLowMemory() {
        LifecycleDelegate lifecycleDelegate = this.zare;
        if (lifecycleDelegate != null) {
            lifecycleDelegate.onLowMemory();
        }
    }
}
