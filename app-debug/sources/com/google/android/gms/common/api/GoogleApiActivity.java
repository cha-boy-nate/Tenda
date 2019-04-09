package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.util.VisibleForTesting;

@KeepName
public class GoogleApiActivity extends Activity implements OnCancelListener {
    @VisibleForTesting
    private int zabp = 0;

    public static PendingIntent zaa(Context context, PendingIntent pendingIntent, int i) {
        return PendingIntent.getActivity(context, 0, zaa(context, pendingIntent, i, true), 134217728);
    }

    public static Intent zaa(Context context, PendingIntent pendingIntent, int i, boolean z) {
        Intent intent = new Intent(context, GoogleApiActivity.class);
        intent.putExtra("pending_intent", pendingIntent);
        intent.putExtra("failing_client_id", i);
        intent.putExtra("notify_manager", z);
        return intent;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zabp = bundle.getInt("resolution");
        }
        if (this.zabp != 1) {
            bundle = getIntent().getExtras();
            if (bundle == null) {
                Log.e("GoogleApiActivity", "Activity started without extras");
                finish();
                return;
            }
            PendingIntent pendingIntent = (PendingIntent) bundle.get("pending_intent");
            Integer num = (Integer) bundle.get("error_code");
            if (pendingIntent == null && num == null) {
                Log.e("GoogleApiActivity", "Activity started without resolution");
                finish();
            } else if (pendingIntent != null) {
                try {
                    startIntentSenderForResult(pendingIntent.getIntentSender(), 1, null, 0, 0, 0);
                    this.zabp = 1;
                } catch (Bundle bundle2) {
                    Log.e("GoogleApiActivity", "Failed to launch pendingIntent", bundle2);
                    finish();
                }
            } else {
                GoogleApiAvailability.getInstance().showErrorDialogFragment(this, num.intValue(), 2, this);
                this.zabp = 1;
            }
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            i = getIntent().getBooleanExtra("notify_manager", true);
            this.zabp = 0;
            setResult(i2, intent);
            if (i != 0) {
                i = GoogleApiManager.zab((Context) this);
                switch (i2) {
                    case -1:
                        i.zao();
                        break;
                    case 0:
                        i.zaa(new ConnectionResult(13, null), getIntent().getIntExtra("failing_client_id", -1));
                        break;
                    default:
                        break;
                }
            }
        } else if (i == 2) {
            this.zabp = 0;
            setResult(i2, intent);
            finish();
        }
        finish();
    }

    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("resolution", this.zabp);
        super.onSaveInstanceState(bundle);
    }

    public void onCancel(DialogInterface dialogInterface) {
        this.zabp = 0;
        setResult(0);
        finish();
    }
}
