package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable;
import com.google.android.gms.auth.api.signin.internal.HashAccumulator;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
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
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "GoogleSignInOptionsCreator")
public class GoogleSignInOptions extends AbstractSafeParcelable implements Optional, ReflectedParcelable {
    public static final Creator<GoogleSignInOptions> CREATOR = new zad();
    public static final GoogleSignInOptions DEFAULT_GAMES_SIGN_IN = new Builder().requestScopes(zau, new Scope[0]).build();
    public static final GoogleSignInOptions DEFAULT_SIGN_IN = new Builder().requestId().requestProfile().build();
    private static Comparator<Scope> zaaf = new zac();
    @VisibleForTesting
    public static final Scope zar = new Scope(Scopes.PROFILE);
    @VisibleForTesting
    public static final Scope zas = new Scope("email");
    @VisibleForTesting
    public static final Scope zat = new Scope(Scopes.OPEN_ID);
    @VisibleForTesting
    public static final Scope zau = new Scope(Scopes.GAMES_LITE);
    @VisibleForTesting
    public static final Scope zav = new Scope(Scopes.GAMES);
    @VersionField(id = 1)
    private final int versionCode;
    @Field(getter = "isForceCodeForRefreshToken", id = 6)
    private final boolean zaaa;
    @Field(getter = "getServerClientId", id = 7)
    private String zaab;
    @Field(getter = "getHostedDomain", id = 8)
    private String zaac;
    @Field(getter = "getExtensions", id = 9)
    private ArrayList<GoogleSignInOptionsExtensionParcelable> zaad;
    private Map<Integer, GoogleSignInOptionsExtensionParcelable> zaae;
    @Field(getter = "getScopes", id = 2)
    private final ArrayList<Scope> zaw;
    @Field(getter = "getAccount", id = 3)
    private Account zax;
    @Field(getter = "isIdTokenRequested", id = 4)
    private boolean zay;
    @Field(getter = "isServerAuthCodeRequested", id = 5)
    private final boolean zaz;

    public static final class Builder {
        private Set<Scope> mScopes = new HashSet();
        private boolean zaaa;
        private String zaab;
        private String zaac;
        private Map<Integer, GoogleSignInOptionsExtensionParcelable> zaag = new HashMap();
        private Account zax;
        private boolean zay;
        private boolean zaz;

        public Builder(@NonNull GoogleSignInOptions googleSignInOptions) {
            Preconditions.checkNotNull(googleSignInOptions);
            this.mScopes = new HashSet(googleSignInOptions.zaw);
            this.zaz = googleSignInOptions.zaz;
            this.zaaa = googleSignInOptions.zaaa;
            this.zay = googleSignInOptions.zay;
            this.zaab = googleSignInOptions.zaab;
            this.zax = googleSignInOptions.zax;
            this.zaac = googleSignInOptions.zaac;
            this.zaag = GoogleSignInOptions.zaa(googleSignInOptions.zaad);
        }

        public final Builder requestId() {
            this.mScopes.add(GoogleSignInOptions.zat);
            return this;
        }

        public final Builder requestEmail() {
            this.mScopes.add(GoogleSignInOptions.zas);
            return this;
        }

        public final Builder requestProfile() {
            this.mScopes.add(GoogleSignInOptions.zar);
            return this;
        }

        public final Builder requestScopes(Scope scope, Scope... scopeArr) {
            this.mScopes.add(scope);
            this.mScopes.addAll(Arrays.asList(scopeArr));
            return this;
        }

        public final Builder requestIdToken(String str) {
            this.zay = true;
            this.zaab = zac(str);
            return this;
        }

        public final Builder requestServerAuthCode(String str) {
            return requestServerAuthCode(str, false);
        }

        public final Builder requestServerAuthCode(String str, boolean z) {
            this.zaz = true;
            this.zaab = zac(str);
            this.zaaa = z;
            return this;
        }

        public final Builder setAccountName(String str) {
            this.zax = new Account(Preconditions.checkNotEmpty(str), AccountType.GOOGLE);
            return this;
        }

        public final Builder setHostedDomain(String str) {
            this.zaac = Preconditions.checkNotEmpty(str);
            return this;
        }

        public final Builder addExtension(GoogleSignInOptionsExtension googleSignInOptionsExtension) {
            if (this.zaag.containsKey(Integer.valueOf(googleSignInOptionsExtension.getExtensionType()))) {
                throw new IllegalStateException("Only one extension per type may be added");
            }
            if (googleSignInOptionsExtension.getImpliedScopes() != null) {
                this.mScopes.addAll(googleSignInOptionsExtension.getImpliedScopes());
            }
            this.zaag.put(Integer.valueOf(googleSignInOptionsExtension.getExtensionType()), new GoogleSignInOptionsExtensionParcelable(googleSignInOptionsExtension));
            return this;
        }

        public final GoogleSignInOptions build() {
            if (this.mScopes.contains(GoogleSignInOptions.zav) && this.mScopes.contains(GoogleSignInOptions.zau)) {
                this.mScopes.remove(GoogleSignInOptions.zau);
            }
            if (this.zay && (this.zax == null || !this.mScopes.isEmpty())) {
                requestId();
            }
            return new GoogleSignInOptions(new ArrayList(this.mScopes), this.zax, this.zay, this.zaz, this.zaaa, this.zaab, this.zaac, this.zaag);
        }

        private final String zac(String str) {
            boolean z;
            Preconditions.checkNotEmpty(str);
            String str2 = this.zaab;
            if (str2 != null) {
                if (!str2.equals(str)) {
                    z = false;
                    Preconditions.checkArgument(z, "two different server client ids provided");
                    return str;
                }
            }
            z = true;
            Preconditions.checkArgument(z, "two different server client ids provided");
            return str;
        }
    }

    @Nullable
    public static GoogleSignInOptions zab(@Nullable String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Account account;
        JSONObject jSONObject = new JSONObject(str);
        str = new HashSet();
        JSONArray jSONArray = jSONObject.getJSONArray("scopes");
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            str.add(new Scope(jSONArray.getString(i)));
        }
        Object optString = jSONObject.optString("accountName", null);
        if (TextUtils.isEmpty(optString)) {
            account = null;
        } else {
            account = new Account(optString, AccountType.GOOGLE);
        }
        return new GoogleSignInOptions(3, new ArrayList(str), account, jSONObject.getBoolean("idTokenRequested"), jSONObject.getBoolean("serverAuthRequested"), jSONObject.getBoolean("forceCodeForRefreshToken"), jSONObject.optString("serverClientId", null), jSONObject.optString("hostedDomain", null), new HashMap());
    }

    private final org.json.JSONObject zad() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:18:0x0079 in {4, 7, 10, 13, 14, 17} preds:[]
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
        r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0072 }
        r1.<init>();	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zaw;	 Catch:{ JSONException -> 0x0072 }
        r3 = zaaf;	 Catch:{ JSONException -> 0x0072 }
        java.util.Collections.sort(r2, r3);	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zaw;	 Catch:{ JSONException -> 0x0072 }
        r2 = (java.util.ArrayList) r2;	 Catch:{ JSONException -> 0x0072 }
        r3 = r2.size();	 Catch:{ JSONException -> 0x0072 }
        r4 = 0;	 Catch:{ JSONException -> 0x0072 }
    L_0x001a:
        if (r4 >= r3) goto L_0x002c;	 Catch:{ JSONException -> 0x0072 }
    L_0x001c:
        r5 = r2.get(r4);	 Catch:{ JSONException -> 0x0072 }
        r4 = r4 + 1;	 Catch:{ JSONException -> 0x0072 }
        r5 = (com.google.android.gms.common.api.Scope) r5;	 Catch:{ JSONException -> 0x0072 }
        r5 = r5.getScopeUri();	 Catch:{ JSONException -> 0x0072 }
        r1.put(r5);	 Catch:{ JSONException -> 0x0072 }
        goto L_0x001a;	 Catch:{ JSONException -> 0x0072 }
    L_0x002c:
        r2 = "scopes";	 Catch:{ JSONException -> 0x0072 }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x0072 }
        r1 = r6.zax;	 Catch:{ JSONException -> 0x0072 }
        if (r1 == 0) goto L_0x003e;	 Catch:{ JSONException -> 0x0072 }
    L_0x0035:
        r1 = "accountName";	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zax;	 Catch:{ JSONException -> 0x0072 }
        r2 = r2.name;	 Catch:{ JSONException -> 0x0072 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0072 }
    L_0x003e:
        r1 = "idTokenRequested";	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zay;	 Catch:{ JSONException -> 0x0072 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0072 }
        r1 = "forceCodeForRefreshToken";	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zaaa;	 Catch:{ JSONException -> 0x0072 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0072 }
        r1 = "serverAuthRequested";	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zaz;	 Catch:{ JSONException -> 0x0072 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0072 }
        r1 = r6.zaab;	 Catch:{ JSONException -> 0x0072 }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ JSONException -> 0x0072 }
        if (r1 != 0) goto L_0x0062;	 Catch:{ JSONException -> 0x0072 }
    L_0x005b:
        r1 = "serverClientId";	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zaab;	 Catch:{ JSONException -> 0x0072 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0072 }
    L_0x0062:
        r1 = r6.zaac;	 Catch:{ JSONException -> 0x0072 }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ JSONException -> 0x0072 }
        if (r1 != 0) goto L_0x0071;	 Catch:{ JSONException -> 0x0072 }
    L_0x006a:
        r1 = "hostedDomain";	 Catch:{ JSONException -> 0x0072 }
        r2 = r6.zaac;	 Catch:{ JSONException -> 0x0072 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0072 }
    L_0x0071:
        return r0;
    L_0x0072:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r0);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInOptions.zad():org.json.JSONObject");
    }

    @Constructor
    GoogleSignInOptions(@Param(id = 1) int i, @Param(id = 2) ArrayList<Scope> arrayList, @Param(id = 3) Account account, @Param(id = 4) boolean z, @Param(id = 5) boolean z2, @Param(id = 6) boolean z3, @Param(id = 7) String str, @Param(id = 8) String str2, @Param(id = 9) ArrayList<GoogleSignInOptionsExtensionParcelable> arrayList2) {
        this(i, (ArrayList) arrayList, account, z, z2, z3, str, str2, zaa((List) arrayList2));
    }

    private GoogleSignInOptions(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map<Integer, GoogleSignInOptionsExtensionParcelable> map) {
        this.versionCode = i;
        this.zaw = arrayList;
        this.zax = account;
        this.zay = z;
        this.zaz = z2;
        this.zaaa = z3;
        this.zaab = str;
        this.zaac = str2;
        this.zaad = new ArrayList(map.values());
        this.zaae = map;
    }

    @KeepForSdk
    public ArrayList<Scope> getScopes() {
        return new ArrayList(this.zaw);
    }

    public Scope[] getScopeArray() {
        ArrayList arrayList = this.zaw;
        return (Scope[]) arrayList.toArray(new Scope[arrayList.size()]);
    }

    @KeepForSdk
    public Account getAccount() {
        return this.zax;
    }

    @KeepForSdk
    public boolean isIdTokenRequested() {
        return this.zay;
    }

    @KeepForSdk
    public boolean isServerAuthCodeRequested() {
        return this.zaz;
    }

    @KeepForSdk
    public boolean isForceCodeForRefreshToken() {
        return this.zaaa;
    }

    @KeepForSdk
    public String getServerClientId() {
        return this.zaab;
    }

    @KeepForSdk
    public ArrayList<GoogleSignInOptionsExtensionParcelable> getExtensions() {
        return this.zaad;
    }

    private static Map<Integer, GoogleSignInOptionsExtensionParcelable> zaa(@Nullable List<GoogleSignInOptionsExtensionParcelable> list) {
        Map<Integer, GoogleSignInOptionsExtensionParcelable> hashMap = new HashMap();
        if (list == null) {
            return hashMap;
        }
        for (GoogleSignInOptionsExtensionParcelable googleSignInOptionsExtensionParcelable : list) {
            hashMap.put(Integer.valueOf(googleSignInOptionsExtensionParcelable.getType()), googleSignInOptionsExtensionParcelable);
        }
        return hashMap;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeTypedList(parcel, 2, getScopes(), false);
        SafeParcelWriter.writeParcelable(parcel, 3, getAccount(), i, false);
        SafeParcelWriter.writeBoolean(parcel, 4, isIdTokenRequested());
        SafeParcelWriter.writeBoolean(parcel, 5, isServerAuthCodeRequested());
        SafeParcelWriter.writeBoolean(parcel, 6, isForceCodeForRefreshToken());
        SafeParcelWriter.writeString(parcel, 7, getServerClientId(), false);
        SafeParcelWriter.writeString(parcel, 8, this.zaac, false);
        SafeParcelWriter.writeTypedList(parcel, 9, getExtensions(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
        r3 = this;
        r0 = 0;
        if (r4 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r4 = (com.google.android.gms.auth.api.signin.GoogleSignInOptions) r4;	 Catch:{ ClassCastException -> 0x008a }
        r1 = r3.zaad;	 Catch:{ ClassCastException -> 0x008a }
        r1 = r1.size();	 Catch:{ ClassCastException -> 0x008a }
        if (r1 > 0) goto L_0x0089;
    L_0x000e:
        r1 = r4.zaad;	 Catch:{ ClassCastException -> 0x008a }
        r1 = r1.size();	 Catch:{ ClassCastException -> 0x008a }
        if (r1 <= 0) goto L_0x0018;
    L_0x0016:
        goto L_0x0089;
    L_0x0018:
        r1 = r3.zaw;	 Catch:{ ClassCastException -> 0x008a }
        r1 = r1.size();	 Catch:{ ClassCastException -> 0x008a }
        r2 = r4.getScopes();	 Catch:{ ClassCastException -> 0x008a }
        r2 = r2.size();	 Catch:{ ClassCastException -> 0x008a }
        if (r1 != r2) goto L_0x0088;
    L_0x0028:
        r1 = r3.zaw;	 Catch:{ ClassCastException -> 0x008a }
        r2 = r4.getScopes();	 Catch:{ ClassCastException -> 0x008a }
        r1 = r1.containsAll(r2);	 Catch:{ ClassCastException -> 0x008a }
        if (r1 != 0) goto L_0x0035;
    L_0x0034:
        goto L_0x0088;
    L_0x0035:
        r1 = r3.zax;	 Catch:{ ClassCastException -> 0x008a }
        if (r1 != 0) goto L_0x0040;
    L_0x0039:
        r1 = r4.getAccount();	 Catch:{ ClassCastException -> 0x008a }
        if (r1 != 0) goto L_0x0086;
    L_0x003f:
        goto L_0x004d;
    L_0x0040:
        r1 = r3.zax;	 Catch:{ ClassCastException -> 0x008a }
        r2 = r4.getAccount();	 Catch:{ ClassCastException -> 0x008a }
        r1 = r1.equals(r2);	 Catch:{ ClassCastException -> 0x008a }
        if (r1 == 0) goto L_0x0086;
    L_0x004c:
        goto L_0x003f;
    L_0x004d:
        r1 = r3.zaab;	 Catch:{ ClassCastException -> 0x008a }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ ClassCastException -> 0x008a }
        if (r1 == 0) goto L_0x0060;
    L_0x0055:
        r1 = r4.getServerClientId();	 Catch:{ ClassCastException -> 0x008a }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ ClassCastException -> 0x008a }
        if (r1 == 0) goto L_0x0086;
    L_0x005f:
        goto L_0x006c;
    L_0x0060:
        r1 = r3.zaab;	 Catch:{ ClassCastException -> 0x008a }
        r2 = r4.getServerClientId();	 Catch:{ ClassCastException -> 0x008a }
        r1 = r1.equals(r2);	 Catch:{ ClassCastException -> 0x008a }
        if (r1 == 0) goto L_0x0086;
    L_0x006c:
        r1 = r3.zaaa;	 Catch:{ ClassCastException -> 0x008a }
        r2 = r4.isForceCodeForRefreshToken();	 Catch:{ ClassCastException -> 0x008a }
        if (r1 != r2) goto L_0x0086;
    L_0x0074:
        r1 = r3.zay;	 Catch:{ ClassCastException -> 0x008a }
        r2 = r4.isIdTokenRequested();	 Catch:{ ClassCastException -> 0x008a }
        if (r1 != r2) goto L_0x0086;
    L_0x007c:
        r1 = r3.zaz;	 Catch:{ ClassCastException -> 0x008a }
        r4 = r4.isServerAuthCodeRequested();	 Catch:{ ClassCastException -> 0x008a }
        if (r1 != r4) goto L_0x0086;
    L_0x0084:
        r4 = 1;
        return r4;
        return r0;
    L_0x0088:
        return r0;
    L_0x0089:
        return r0;
    L_0x008a:
        r4 = move-exception;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInOptions.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        List arrayList = new ArrayList();
        ArrayList arrayList2 = this.zaw;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            arrayList.add(((Scope) obj).getScopeUri());
        }
        Collections.sort(arrayList);
        return new HashAccumulator().addObject(arrayList).addObject(this.zax).addObject(this.zaab).zaa(this.zaaa).zaa(this.zay).zaa(this.zaz).hash();
    }

    public final String zae() {
        return zad().toString();
    }
}
