package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;

@Class(creator = "MarkerOptionsCreator")
@Reserved({1})
public final class MarkerOptions extends AbstractSafeParcelable {
    public static final Creator<MarkerOptions> CREATOR = new zzh();
    @Field(defaultValue = "1.0f", getter = "getAlpha", id = 14)
    private float alpha = 1.0f;
    @Field(getter = "getPosition", id = 2)
    private LatLng position;
    @Field(getter = "getZIndex", id = 15)
    private float zzcs;
    @Field(getter = "isVisible", id = 9)
    private boolean zzct = true;
    @Field(getter = "getAnchorU", id = 6)
    private float zzdb = 0.5f;
    @Field(getter = "getAnchorV", id = 7)
    private float zzdc = 1.0f;
    @Field(getter = "getTitle", id = 3)
    private String zzdn;
    @Field(getter = "getSnippet", id = 4)
    private String zzdo;
    @Field(getter = "getWrappedIconDescriptorImplBinder", id = 5, type = "android.os.IBinder")
    private BitmapDescriptor zzdp;
    @Field(getter = "isDraggable", id = 8)
    private boolean zzdq;
    @Field(getter = "isFlat", id = 10)
    private boolean zzdr = false;
    @Field(getter = "getRotation", id = 11)
    private float zzds = 0.0f;
    @Field(defaultValue = "0.5f", getter = "getInfoWindowAnchorU", id = 12)
    private float zzdt = 0.5f;
    @Field(getter = "getInfoWindowAnchorV", id = 13)
    private float zzdu = 0.0f;

    @Constructor
    MarkerOptions(@Param(id = 2) LatLng latLng, @Param(id = 3) String str, @Param(id = 4) String str2, @Param(id = 5) IBinder iBinder, @Param(id = 6) float f, @Param(id = 7) float f2, @Param(id = 8) boolean z, @Param(id = 9) boolean z2, @Param(id = 10) boolean z3, @Param(id = 11) float f3, @Param(id = 12) float f4, @Param(id = 13) float f5, @Param(id = 14) float f6, @Param(id = 15) float f7) {
        this.position = latLng;
        this.zzdn = str;
        this.zzdo = str2;
        if (iBinder == null) {
            r0.zzdp = null;
        } else {
            r0.zzdp = new BitmapDescriptor(Stub.asInterface(iBinder));
        }
        r0.zzdb = f;
        r0.zzdc = f2;
        r0.zzdq = z;
        r0.zzct = z2;
        r0.zzdr = z3;
        r0.zzds = f3;
        r0.zzdt = f4;
        r0.zzdu = f5;
        r0.alpha = f6;
        r0.zzcs = f7;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 2, getPosition(), i, false);
        SafeParcelWriter.writeString(parcel, 3, getTitle(), false);
        SafeParcelWriter.writeString(parcel, 4, getSnippet(), false);
        i = this.zzdp;
        if (i == 0) {
            i = 0;
        } else {
            i = i.zzb().asBinder();
        }
        SafeParcelWriter.writeIBinder(parcel, 5, i, false);
        SafeParcelWriter.writeFloat(parcel, 6, getAnchorU());
        SafeParcelWriter.writeFloat(parcel, 7, getAnchorV());
        SafeParcelWriter.writeBoolean(parcel, 8, isDraggable());
        SafeParcelWriter.writeBoolean(parcel, 9, isVisible());
        SafeParcelWriter.writeBoolean(parcel, 10, isFlat());
        SafeParcelWriter.writeFloat(parcel, 11, getRotation());
        SafeParcelWriter.writeFloat(parcel, 12, getInfoWindowAnchorU());
        SafeParcelWriter.writeFloat(parcel, 13, getInfoWindowAnchorV());
        SafeParcelWriter.writeFloat(parcel, 14, getAlpha());
        SafeParcelWriter.writeFloat(parcel, 15, getZIndex());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final MarkerOptions position(@NonNull LatLng latLng) {
        if (latLng != null) {
            this.position = latLng;
            return this;
        }
        throw new IllegalArgumentException("latlng cannot be null - a position is required.");
    }

    public final MarkerOptions zIndex(float f) {
        this.zzcs = f;
        return this;
    }

    public final MarkerOptions icon(@Nullable BitmapDescriptor bitmapDescriptor) {
        this.zzdp = bitmapDescriptor;
        return this;
    }

    public final MarkerOptions anchor(float f, float f2) {
        this.zzdb = f;
        this.zzdc = f2;
        return this;
    }

    public final MarkerOptions infoWindowAnchor(float f, float f2) {
        this.zzdt = f;
        this.zzdu = f2;
        return this;
    }

    public final MarkerOptions title(@Nullable String str) {
        this.zzdn = str;
        return this;
    }

    public final MarkerOptions snippet(@Nullable String str) {
        this.zzdo = str;
        return this;
    }

    public final MarkerOptions draggable(boolean z) {
        this.zzdq = z;
        return this;
    }

    public final MarkerOptions visible(boolean z) {
        this.zzct = z;
        return this;
    }

    public final MarkerOptions flat(boolean z) {
        this.zzdr = z;
        return this;
    }

    public final MarkerOptions rotation(float f) {
        this.zzds = f;
        return this;
    }

    public final MarkerOptions alpha(float f) {
        this.alpha = f;
        return this;
    }

    public final LatLng getPosition() {
        return this.position;
    }

    public final String getTitle() {
        return this.zzdn;
    }

    public final String getSnippet() {
        return this.zzdo;
    }

    public final BitmapDescriptor getIcon() {
        return this.zzdp;
    }

    public final float getAnchorU() {
        return this.zzdb;
    }

    public final float getAnchorV() {
        return this.zzdc;
    }

    public final boolean isDraggable() {
        return this.zzdq;
    }

    public final boolean isVisible() {
        return this.zzct;
    }

    public final boolean isFlat() {
        return this.zzdr;
    }

    public final float getRotation() {
        return this.zzds;
    }

    public final float getInfoWindowAnchorU() {
        return this.zzdt;
    }

    public final float getInfoWindowAnchorV() {
        return this.zzdu;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final float getZIndex() {
        return this.zzcs;
    }
}
