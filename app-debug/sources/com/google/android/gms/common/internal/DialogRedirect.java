package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.android.gms.common.api.internal.LifecycleFragment;

public abstract class DialogRedirect implements OnClickListener {
    protected abstract void redirect();

    public static DialogRedirect getInstance(Activity activity, Intent intent, int i) {
        return new zac(intent, activity, i);
    }

    public static DialogRedirect getInstance(@NonNull Fragment fragment, Intent intent, int i) {
        return new zad(intent, fragment, i);
    }

    public static DialogRedirect getInstance(@NonNull LifecycleFragment lifecycleFragment, Intent intent, int i) {
        return new zae(intent, lifecycleFragment, i);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            redirect();
        } catch (int i2) {
            Log.e("DialogRedirect", "Failed to start resolution intent", i2);
        } finally {
            dialogInterface.dismiss();
        }
    }
}
