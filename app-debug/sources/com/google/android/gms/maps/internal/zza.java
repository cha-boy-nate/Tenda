package com.google.android.gms.maps.internal;

public final class zza {
    public static Boolean zza(byte b) {
        switch (b) {
            case (byte) 0:
                return Boolean.FALSE;
            case (byte) 1:
                return Boolean.TRUE;
            default:
                return (byte) 0;
        }
    }

    public static byte zza(Boolean bool) {
        if (bool != null) {
            return bool.booleanValue() != null ? (byte) 1 : null;
        } else {
            return (byte) -1;
        }
    }
}
