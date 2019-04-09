package com.google.android.gms.maps.model;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.Preconditions;

public final class CustomCap extends Cap {
    public final BitmapDescriptor bitmapDescriptor;
    public final float refWidth;

    public CustomCap(@NonNull BitmapDescriptor bitmapDescriptor, float f) {
        BitmapDescriptor bitmapDescriptor2 = (BitmapDescriptor) Preconditions.checkNotNull(bitmapDescriptor, "bitmapDescriptor must not be null");
        if (f > 0.0f) {
            super(bitmapDescriptor2, f);
            this.bitmapDescriptor = bitmapDescriptor;
            this.refWidth = f;
            return;
        }
        throw new IllegalArgumentException("refWidth must be positive");
    }

    public CustomCap(@NonNull BitmapDescriptor bitmapDescriptor) {
        this(bitmapDescriptor, 10.0f);
    }

    public final String toString() {
        String valueOf = String.valueOf(this.bitmapDescriptor);
        float f = this.refWidth;
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 55);
        stringBuilder.append("[CustomCap: bitmapDescriptor=");
        stringBuilder.append(valueOf);
        stringBuilder.append(" refWidth=");
        stringBuilder.append(f);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
