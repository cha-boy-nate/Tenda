package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.internal.maps.zzaf;
import com.google.android.gms.internal.maps.zzag;

@Class(creator = "TileOverlayOptionsCreator")
@Reserved({1})
public final class TileOverlayOptions extends AbstractSafeParcelable {
    public static final Creator<TileOverlayOptions> CREATOR = new zzu();
    @Field(getter = "getZIndex", id = 4)
    private float zzcs;
    @Field(getter = "isVisible", id = 3)
    private boolean zzct = true;
    @Field(getter = "getTransparency", id = 6)
    private float zzda = 0.0f;
    @Field(getter = "getTileProviderDelegate", id = 2, type = "android.os.IBinder")
    private zzaf zzei;
    private TileProvider zzej;
    @Field(defaultValue = "true", getter = "getFadeIn", id = 5)
    private boolean zzek = true;

    @Constructor
    TileOverlayOptions(@Param(id = 2) IBinder iBinder, @Param(id = 3) boolean z, @Param(id = 4) float f, @Param(id = 5) boolean z2, @Param(id = 6) float f2) {
        this.zzei = zzag.zzk(iBinder);
        if (this.zzei == null) {
            iBinder = null;
        } else {
            iBinder = new zzs(this);
        }
        this.zzej = iBinder;
        this.zzct = z;
        this.zzcs = f;
        this.zzek = z2;
        this.zzda = f2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeIBinder(parcel, 2, this.zzei.asBinder(), false);
        SafeParcelWriter.writeBoolean(parcel, 3, isVisible());
        SafeParcelWriter.writeFloat(parcel, 4, getZIndex());
        SafeParcelWriter.writeBoolean(parcel, 5, getFadeIn());
        SafeParcelWriter.writeFloat(parcel, 6, getTransparency());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    public final TileOverlayOptions tileProvider(TileProvider tileProvider) {
        this.zzej = tileProvider;
        if (this.zzej == null) {
            tileProvider = null;
        } else {
            tileProvider = new zzt(this, tileProvider);
        }
        this.zzei = tileProvider;
        return this;
    }

    public final TileOverlayOptions zIndex(float f) {
        this.zzcs = f;
        return this;
    }

    public final TileOverlayOptions visible(boolean z) {
        this.zzct = z;
        return this;
    }

    public final TileOverlayOptions fadeIn(boolean z) {
        this.zzek = z;
        return this;
    }

    public final TileOverlayOptions transparency(float f) {
        boolean z = f >= 0.0f && f <= 1.0f;
        Preconditions.checkArgument(z, "Transparency must be in the range [0..1]");
        this.zzda = f;
        return this;
    }

    public final TileProvider getTileProvider() {
        return this.zzej;
    }

    public final float getZIndex() {
        return this.zzcs;
    }

    public final boolean isVisible() {
        return this.zzct;
    }

    public final boolean getFadeIn() {
        return this.zzek;
    }

    public final float getTransparency() {
        return this.zzda;
    }
}
