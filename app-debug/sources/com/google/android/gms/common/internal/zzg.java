package com.google.android.gms.common.internal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public final class zzg {
    private static final Uri zzed;
    private static final Uri zzee;

    public static Intent zzg(String str) {
        str = Uri.fromParts("package", str, null);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(str);
        return intent;
    }

    public static Intent zza(String str, @Nullable String str2) {
        Intent intent = new Intent("android.intent.action.VIEW");
        str = Uri.parse("market://details").buildUpon().appendQueryParameter("id", str);
        if (!TextUtils.isEmpty(str2)) {
            str.appendQueryParameter("pcampaignid", str2);
        }
        intent.setData(str.build());
        intent.setPackage("com.android.vending");
        intent.addFlags(524288);
        return intent;
    }

    public static Intent zzs() {
        Intent intent = new Intent("com.google.android.clockwork.home.UPDATE_ANDROID_WEAR_ACTION");
        intent.setPackage("com.google.android.wearable.app");
        return intent;
    }

    static {
        Uri parse = Uri.parse("https://plus.google.com/");
        zzed = parse;
        zzee = parse.buildUpon().appendPath("circles").appendPath("find").build();
    }
}
