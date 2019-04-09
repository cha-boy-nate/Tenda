package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.internal.base.zak;

public abstract class zaa {
    final zab zamu;
    private int zamv = 0;
    protected int zamw = 0;
    private boolean zamx = false;
    private boolean zamy = true;
    private boolean zamz = false;
    private boolean zana = true;

    public zaa(Uri uri, int i) {
        this.zamu = new zab(uri);
        this.zamw = i;
    }

    protected abstract void zaa(Drawable drawable, boolean z, boolean z2, boolean z3);

    final void zaa(Context context, Bitmap bitmap, boolean z) {
        Asserts.checkNotNull(bitmap);
        zaa(new BitmapDrawable(context.getResources(), bitmap), z, null, true);
    }

    final void zaa(Context context, zak zak) {
        if (this.zana != null) {
            zaa(null, false, true, false);
        }
    }

    final void zaa(Context context, zak zak, boolean z) {
        zak = this.zamw;
        if (zak != null) {
            context = context.getResources().getDrawable(zak);
        } else {
            context = null;
        }
        zaa(context, z, false, false);
    }

    protected final boolean zaa(boolean z, boolean z2) {
        return (!this.zamy || z2 || z) ? false : true;
    }
}
