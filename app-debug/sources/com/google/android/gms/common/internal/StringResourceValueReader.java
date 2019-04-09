package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.common.C0236R;
import com.google.android.gms.common.annotation.KeepForSdk;
import javax.annotation.Nullable;

@KeepForSdk
public class StringResourceValueReader {
    private final Resources zzeu;
    private final String zzev = this.zzeu.getResourcePackageName(C0236R.string.common_google_play_services_unknown_issue);

    public StringResourceValueReader(Context context) {
        Preconditions.checkNotNull(context);
        this.zzeu = context.getResources();
    }

    @KeepForSdk
    @Nullable
    public String getString(String str) {
        str = this.zzeu.getIdentifier(str, "string", this.zzev);
        return str == null ? null : this.zzeu.getString(str);
    }
}
