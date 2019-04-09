package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import org.json.JSONException;

public class Storage {
    private static final Lock zaaj = new ReentrantLock();
    @GuardedBy("sLk")
    private static Storage zaak;
    private final Lock zaal = new ReentrantLock();
    @GuardedBy("mLk")
    private final SharedPreferences zaam;

    @KeepForSdk
    public static Storage getInstance(Context context) {
        Preconditions.checkNotNull(context);
        zaaj.lock();
        try {
            if (zaak == null) {
                zaak = new Storage(context.getApplicationContext());
            }
            context = zaak;
            return context;
        } finally {
            zaaj.unlock();
        }
    }

    @VisibleForTesting
    private Storage(Context context) {
        this.zaam = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    @KeepForSdk
    public void saveDefaultGoogleSignInAccount(GoogleSignInAccount googleSignInAccount, GoogleSignInOptions googleSignInOptions) {
        Preconditions.checkNotNull(googleSignInAccount);
        Preconditions.checkNotNull(googleSignInOptions);
        zaa("defaultGoogleSignInAccount", googleSignInAccount.zab());
        Preconditions.checkNotNull(googleSignInAccount);
        Preconditions.checkNotNull(googleSignInOptions);
        String zab = googleSignInAccount.zab();
        zaa(zab("googleSignInAccount", zab), googleSignInAccount.zac());
        zaa(zab("googleSignInOptions", zab), googleSignInOptions.zae());
    }

    private final void zaa(String str, String str2) {
        this.zaal.lock();
        try {
            this.zaam.edit().putString(str, str2).apply();
        } finally {
            this.zaal.unlock();
        }
    }

    @KeepForSdk
    @Nullable
    public GoogleSignInAccount getSavedDefaultGoogleSignInAccount() {
        return zad(zaf("defaultGoogleSignInAccount"));
    }

    @Nullable
    @VisibleForTesting
    private final GoogleSignInAccount zad(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str = zaf(zab("googleSignInAccount", str));
        if (str == null) {
            return null;
        }
        try {
            return GoogleSignInAccount.zaa(str);
        } catch (JSONException e) {
            return null;
        }
    }

    @KeepForSdk
    @Nullable
    public GoogleSignInOptions getSavedDefaultGoogleSignInOptions() {
        return zae(zaf("defaultGoogleSignInAccount"));
    }

    @Nullable
    @VisibleForTesting
    private final GoogleSignInOptions zae(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str = zaf(zab("googleSignInOptions", str));
        if (str == null) {
            return null;
        }
        try {
            return GoogleSignInOptions.zab(str);
        } catch (JSONException e) {
            return null;
        }
    }

    @KeepForSdk
    @Nullable
    public String getSavedRefreshToken() {
        return zaf("refreshToken");
    }

    @Nullable
    private final String zaf(String str) {
        this.zaal.lock();
        try {
            str = this.zaam.getString(str, null);
            return str;
        } finally {
            this.zaal.unlock();
        }
    }

    public final void zaf() {
        String zaf = zaf("defaultGoogleSignInAccount");
        zag("defaultGoogleSignInAccount");
        if (!TextUtils.isEmpty(zaf)) {
            zag(zab("googleSignInAccount", zaf));
            zag(zab("googleSignInOptions", zaf));
        }
    }

    private final void zag(String str) {
        this.zaal.lock();
        try {
            this.zaam.edit().remove(str).apply();
        } finally {
            this.zaal.unlock();
        }
    }

    @KeepForSdk
    public void clear() {
        this.zaal.lock();
        try {
            this.zaam.edit().clear().apply();
        } finally {
            this.zaal.unlock();
        }
    }

    private static String zab(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder((String.valueOf(str).length() + 1) + String.valueOf(str2).length());
        stringBuilder.append(str);
        stringBuilder.append(":");
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }
}
