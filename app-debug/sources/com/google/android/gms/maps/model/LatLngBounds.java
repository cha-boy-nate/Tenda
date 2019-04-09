package com.google.android.gms.maps.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.maps.GoogleMapOptions;

@Class(creator = "LatLngBoundsCreator")
@Reserved({1})
public final class LatLngBounds extends AbstractSafeParcelable implements ReflectedParcelable {
    @KeepForSdk
    public static final Creator<LatLngBounds> CREATOR = new zze();
    @Field(id = 3)
    public final LatLng northeast;
    @Field(id = 2)
    public final LatLng southwest;

    public static final class Builder {
        private double zzdh = Double.POSITIVE_INFINITY;
        private double zzdi = Double.NEGATIVE_INFINITY;
        private double zzdj = Double.NaN;
        private double zzdk = Double.NaN;

        public final Builder include(LatLng latLng) {
            this.zzdh = Math.min(this.zzdh, latLng.latitude);
            this.zzdi = Math.max(this.zzdi, latLng.latitude);
            double d = latLng.longitude;
            if (Double.isNaN(this.zzdj) != null) {
                this.zzdj = d;
            } else {
                double d2 = this.zzdj;
                double d3 = this.zzdk;
                latLng = null;
                if (d2 <= d3) {
                    if (d2 <= d && d <= d3) {
                        latLng = true;
                    }
                } else if (d2 <= d || d <= d3) {
                    latLng = true;
                }
                if (latLng == null) {
                    if (LatLngBounds.zza(this.zzdj, d) < LatLngBounds.zzb(this.zzdk, d)) {
                        this.zzdj = d;
                    }
                }
                return this;
            }
            this.zzdk = d;
            return this;
        }

        public final LatLngBounds build() {
            Preconditions.checkState(Double.isNaN(this.zzdj) ^ 1, "no included points");
            return new LatLngBounds(new LatLng(this.zzdh, this.zzdj), new LatLng(this.zzdi, this.zzdk));
        }
    }

    @Constructor
    public LatLngBounds(@Param(id = 2) LatLng latLng, @Param(id = 3) LatLng latLng2) {
        Preconditions.checkNotNull(latLng, "null southwest");
        Preconditions.checkNotNull(latLng2, "null northeast");
        Preconditions.checkArgument(latLng2.latitude >= latLng.latitude, "southern latitude exceeds northern latitude (%s > %s)", Double.valueOf(latLng.latitude), Double.valueOf(latLng2.latitude));
        this.southwest = latLng;
        this.northeast = latLng2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 2, this.southwest, i, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.northeast, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final boolean contains(LatLng latLng) {
        double d = latLng.latitude;
        Object obj = (this.southwest.latitude > d || d > this.northeast.latitude) ? null : 1;
        return (obj == null || zza(latLng.longitude) == null) ? false : true;
    }

    public final LatLngBounds including(LatLng latLng) {
        double min = Math.min(this.southwest.latitude, latLng.latitude);
        double max = Math.max(this.northeast.latitude, latLng.latitude);
        double d = this.northeast.longitude;
        double d2 = this.southwest.longitude;
        double d3 = latLng.longitude;
        if (zza(d3) == null) {
            if (zza(d2, d3) < zzb(d, d3)) {
                d2 = d3;
            } else {
                d = d3;
            }
        }
        return new LatLngBounds(new LatLng(min, d2), new LatLng(max, d));
    }

    public final LatLng getCenter() {
        double d = (this.southwest.latitude + this.northeast.latitude) / 2.0d;
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        if (d3 <= d2) {
            d2 = (d2 + d3) / 2.0d;
        } else {
            d2 = ((d2 + 360.0d) + d3) / 2.0d;
        }
        return new LatLng(d, d2);
    }

    private static double zza(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    private static double zzb(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    private final boolean zza(double d) {
        if (this.southwest.longitude <= this.northeast.longitude) {
            return this.southwest.longitude <= d && d <= this.northeast.longitude;
        } else {
            if (this.southwest.longitude > d) {
                if (d > this.northeast.longitude) {
                    return false;
                }
            }
            return true;
        }
    }

    public final int hashCode() {
        return Objects.hashCode(this.southwest, this.northeast);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LatLngBounds)) {
            return false;
        }
        LatLngBounds latLngBounds = (LatLngBounds) obj;
        if (!this.southwest.equals(latLngBounds.southwest) || this.northeast.equals(latLngBounds.northeast) == null) {
            return false;
        }
        return true;
    }

    public final String toString() {
        return Objects.toStringHelper(this).add("southwest", this.southwest).add("northeast", this.northeast).toString();
    }

    public static LatLngBounds createFromAttributes(Context context, AttributeSet attributeSet) {
        return GoogleMapOptions.zza(context, attributeSet);
    }
}
