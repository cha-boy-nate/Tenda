package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.AccountType;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "GoogleSignInAccountCreator")
public class GoogleSignInAccount extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<GoogleSignInAccount> CREATOR = new zab();
    @VisibleForTesting
    private static Clock zae = DefaultClock.getInstance();
    @Field(getter = "getId", id = 2)
    private String mId;
    @VersionField(id = 1)
    private final int versionCode;
    @Field(getter = "getIdToken", id = 3)
    private String zaf;
    @Field(getter = "getEmail", id = 4)
    private String zag;
    @Field(getter = "getDisplayName", id = 5)
    private String zah;
    @Field(getter = "getPhotoUrl", id = 6)
    private Uri zai;
    @Field(getter = "getServerAuthCode", id = 7)
    private String zaj;
    @Field(getter = "getExpirationTimeSecs", id = 8)
    private long zak;
    @Field(getter = "getObfuscatedIdentifier", id = 9)
    private String zal;
    @Field(id = 10)
    private List<Scope> zam;
    @Field(getter = "getGivenName", id = 11)
    private String zan;
    @Field(getter = "getFamilyName", id = 12)
    private String zao;
    private Set<Scope> zap = new HashSet();

    @Nullable
    public static GoogleSignInAccount zaa(@Nullable String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Uri uri;
        JSONObject jSONObject = new JSONObject(str);
        str = jSONObject.optString("photoUrl", null);
        if (TextUtils.isEmpty(str)) {
            uri = null;
        } else {
            uri = Uri.parse(str);
        }
        long parseLong = Long.parseLong(jSONObject.getString("expirationTime"));
        Set hashSet = new HashSet();
        str = jSONObject.getJSONArray("grantedScopes");
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hashSet.add(new Scope(str.getString(i)));
        }
        str = zaa(jSONObject.optString("id"), jSONObject.optString("tokenId", null), jSONObject.optString("email", null), jSONObject.optString("displayName", null), jSONObject.optString("givenName", null), jSONObject.optString("familyName", null), uri, Long.valueOf(parseLong), jSONObject.getString("obfuscatedIdentifier"), hashSet);
        str.zaj = jSONObject.optString("serverAuthCode", null);
        return str;
    }

    private final org.json.JSONObject zad() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:34:0x00c6 in {4, 7, 10, 13, 16, 19, 22, 25, 28, 30, 33} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r6 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = r6.getId();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x0014;	 Catch:{ JSONException -> 0x00bf }
    L_0x000b:
        r1 = "id";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getId();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x0014:
        r1 = r6.getIdToken();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x0023;	 Catch:{ JSONException -> 0x00bf }
    L_0x001a:
        r1 = "tokenId";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getIdToken();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x0023:
        r1 = r6.getEmail();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x0032;	 Catch:{ JSONException -> 0x00bf }
    L_0x0029:
        r1 = "email";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getEmail();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x0032:
        r1 = r6.getDisplayName();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x0041;	 Catch:{ JSONException -> 0x00bf }
    L_0x0038:
        r1 = "displayName";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getDisplayName();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x0041:
        r1 = r6.getGivenName();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x0050;	 Catch:{ JSONException -> 0x00bf }
    L_0x0047:
        r1 = "givenName";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getGivenName();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x0050:
        r1 = r6.getFamilyName();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x005f;	 Catch:{ JSONException -> 0x00bf }
    L_0x0056:
        r1 = "familyName";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getFamilyName();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x005f:
        r1 = r6.getPhotoUrl();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x0072;	 Catch:{ JSONException -> 0x00bf }
    L_0x0065:
        r1 = "photoUrl";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getPhotoUrl();	 Catch:{ JSONException -> 0x00bf }
        r2 = r2.toString();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x0072:
        r1 = r6.getServerAuthCode();	 Catch:{ JSONException -> 0x00bf }
        if (r1 == 0) goto L_0x0081;	 Catch:{ JSONException -> 0x00bf }
    L_0x0078:
        r1 = "serverAuthCode";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.getServerAuthCode();	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
    L_0x0081:
        r1 = "expirationTime";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.zak;	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
        r1 = "obfuscatedIdentifier";	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.zal;	 Catch:{ JSONException -> 0x00bf }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00bf }
        r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x00bf }
        r1.<init>();	 Catch:{ JSONException -> 0x00bf }
        r2 = r6.zam;	 Catch:{ JSONException -> 0x00bf }
        r3 = r6.zam;	 Catch:{ JSONException -> 0x00bf }
        r3 = r3.size();	 Catch:{ JSONException -> 0x00bf }
        r3 = new com.google.android.gms.common.api.Scope[r3];	 Catch:{ JSONException -> 0x00bf }
        r2 = r2.toArray(r3);	 Catch:{ JSONException -> 0x00bf }
        r2 = (com.google.android.gms.common.api.Scope[]) r2;	 Catch:{ JSONException -> 0x00bf }
        r3 = com.google.android.gms.auth.api.signin.zaa.zaq;	 Catch:{ JSONException -> 0x00bf }
        java.util.Arrays.sort(r2, r3);	 Catch:{ JSONException -> 0x00bf }
        r3 = r2.length;	 Catch:{ JSONException -> 0x00bf }
        r4 = 0;	 Catch:{ JSONException -> 0x00bf }
    L_0x00ab:
        if (r4 >= r3) goto L_0x00b9;	 Catch:{ JSONException -> 0x00bf }
    L_0x00ad:
        r5 = r2[r4];	 Catch:{ JSONException -> 0x00bf }
        r5 = r5.getScopeUri();	 Catch:{ JSONException -> 0x00bf }
        r1.put(r5);	 Catch:{ JSONException -> 0x00bf }
        r4 = r4 + 1;	 Catch:{ JSONException -> 0x00bf }
        goto L_0x00ab;	 Catch:{ JSONException -> 0x00bf }
    L_0x00b9:
        r2 = "grantedScopes";	 Catch:{ JSONException -> 0x00bf }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x00bf }
        return r0;
    L_0x00bf:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r0);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInAccount.zad():org.json.JSONObject");
    }

    private static GoogleSignInAccount zaa(@Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5, @Nullable String str6, @Nullable Uri uri, @Nullable Long l, @NonNull String str7, @NonNull Set<Scope> set) {
        Long valueOf;
        if (l == null) {
            valueOf = Long.valueOf(zae.currentTimeMillis() / 1000);
        } else {
            valueOf = l;
        }
        return new GoogleSignInAccount(3, str, str2, str3, str4, uri, null, valueOf.longValue(), Preconditions.checkNotEmpty(str7), new ArrayList((Collection) Preconditions.checkNotNull(set)), str5, str6);
    }

    @KeepForSdk
    public static GoogleSignInAccount createDefault() {
        Account account = new Account("<<default account>>", AccountType.GOOGLE);
        return zaa(null, null, account.name, null, null, null, null, Long.valueOf(0), account.name, new HashSet());
    }

    @Constructor
    GoogleSignInAccount(@Param(id = 1) int i, @Param(id = 2) String str, @Param(id = 3) String str2, @Param(id = 4) String str3, @Param(id = 5) String str4, @Param(id = 6) Uri uri, @Param(id = 7) String str5, @Param(id = 8) long j, @Param(id = 9) String str6, @Param(id = 10) List<Scope> list, @Param(id = 11) String str7, @Param(id = 12) String str8) {
        this.versionCode = i;
        this.mId = str;
        this.zaf = str2;
        this.zag = str3;
        this.zah = str4;
        this.zai = uri;
        this.zaj = str5;
        this.zak = j;
        this.zal = str6;
        this.zam = list;
        this.zan = str7;
        this.zao = str8;
    }

    @Nullable
    public String getId() {
        return this.mId;
    }

    @Nullable
    public String getIdToken() {
        return this.zaf;
    }

    @Nullable
    public String getEmail() {
        return this.zag;
    }

    @Nullable
    public Account getAccount() {
        String str = this.zag;
        return str == null ? null : new Account(str, AccountType.GOOGLE);
    }

    @Nullable
    public String getDisplayName() {
        return this.zah;
    }

    @Nullable
    public String getGivenName() {
        return this.zan;
    }

    @Nullable
    public String getFamilyName() {
        return this.zao;
    }

    @Nullable
    public Uri getPhotoUrl() {
        return this.zai;
    }

    @KeepForSdk
    public GoogleSignInAccount requestExtraScopes(Scope... scopeArr) {
        if (scopeArr != null) {
            Collections.addAll(this.zap, scopeArr);
        }
        return this;
    }

    @Nullable
    public String getServerAuthCode() {
        return this.zaj;
    }

    @KeepForSdk
    public boolean isExpired() {
        return zae.currentTimeMillis() / 1000 >= this.zak - 300;
    }

    @NonNull
    public final String zab() {
        return this.zal;
    }

    @NonNull
    public Set<Scope> getGrantedScopes() {
        return new HashSet(this.zam);
    }

    @KeepForSdk
    @NonNull
    public Set<Scope> getRequestedScopes() {
        Set<Scope> hashSet = new HashSet(this.zam);
        hashSet.addAll(this.zap);
        return hashSet;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, getId(), false);
        SafeParcelWriter.writeString(parcel, 3, getIdToken(), false);
        SafeParcelWriter.writeString(parcel, 4, getEmail(), false);
        SafeParcelWriter.writeString(parcel, 5, getDisplayName(), false);
        SafeParcelWriter.writeParcelable(parcel, 6, getPhotoUrl(), i, false);
        SafeParcelWriter.writeString(parcel, 7, getServerAuthCode(), false);
        SafeParcelWriter.writeLong(parcel, 8, this.zak);
        SafeParcelWriter.writeString(parcel, 9, this.zal, false);
        SafeParcelWriter.writeTypedList(parcel, 10, this.zam, false);
        SafeParcelWriter.writeString(parcel, 11, getGivenName(), false);
        SafeParcelWriter.writeString(parcel, 12, getFamilyName(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public int hashCode() {
        return ((this.zal.hashCode() + 527) * 31) + getRequestedScopes().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GoogleSignInAccount)) {
            return false;
        }
        GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) obj;
        if (!googleSignInAccount.zal.equals(this.zal) || googleSignInAccount.getRequestedScopes().equals(getRequestedScopes()) == null) {
            return false;
        }
        return true;
    }

    public final String zac() {
        JSONObject zad = zad();
        zad.remove("serverAuthCode");
        return zad.toString();
    }
}
