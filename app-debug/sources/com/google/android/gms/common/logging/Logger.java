package com.google.android.gms.common.logging;

import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.GmsLogger;
import java.util.Locale;

@KeepForSdk
public class Logger {
    private final String mTag;
    private final String zzei;
    private final GmsLogger zzew;
    private final int zzex;

    @KeepForSdk
    public Logger(String str, String... strArr) {
        String str2;
        if (strArr.length == 0) {
            str2 = "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('[');
            for (String str3 : strArr) {
                if (stringBuilder.length() > 1) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(str3);
            }
            stringBuilder.append(93);
            stringBuilder.append(32);
            str2 = stringBuilder.toString();
        }
        this(str, str2);
    }

    private Logger(String str, String str2) {
        this.zzei = str2;
        this.mTag = str;
        this.zzew = new GmsLogger(str);
        str = 2;
        while (7 >= str && Log.isLoggable(this.mTag, str) == null) {
            str++;
        }
        this.zzex = str;
    }

    @KeepForSdk
    public boolean isLoggable(int i) {
        return this.zzex <= i;
    }

    @KeepForSdk
    /* renamed from: v */
    public void m17v(String str, @Nullable Object... objArr) {
        if (isLoggable(2)) {
            Log.v(this.mTag, format(str, objArr));
        }
    }

    @KeepForSdk
    /* renamed from: d */
    public void m13d(String str, @Nullable Object... objArr) {
        if (isLoggable(3)) {
            Log.d(this.mTag, format(str, objArr));
        }
    }

    @KeepForSdk
    /* renamed from: i */
    public void m16i(String str, @Nullable Object... objArr) {
        Log.i(this.mTag, format(str, objArr));
    }

    @KeepForSdk
    /* renamed from: w */
    public void m18w(String str, @Nullable Object... objArr) {
        Log.w(this.mTag, format(str, objArr));
    }

    @KeepForSdk
    /* renamed from: e */
    public void m15e(String str, @Nullable Object... objArr) {
        Log.e(this.mTag, format(str, objArr));
    }

    @KeepForSdk
    /* renamed from: e */
    public void m14e(String str, Throwable th, @Nullable Object... objArr) {
        Log.e(this.mTag, format(str, objArr), th);
    }

    @KeepForSdk
    public void wtf(String str, Throwable th, @Nullable Object... objArr) {
        Log.wtf(this.mTag, format(str, objArr), th);
    }

    @KeepForSdk
    public void wtf(Throwable th) {
        Log.wtf(this.mTag, th);
    }

    private final String format(String str, @Nullable Object... objArr) {
        if (objArr != null && objArr.length > 0) {
            str = String.format(Locale.US, str, objArr);
        }
        return this.zzei.concat(str);
    }
}
