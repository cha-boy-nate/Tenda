package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.IAccountAccessor.Stub;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;

@Class(creator = "AuthAccountRequestCreator")
public class AuthAccountRequest extends AbstractSafeParcelable {
    public static final Creator<AuthAccountRequest> CREATOR = new zaa();
    @VersionField(id = 1)
    private final int zale;
    @Field(id = 2)
    @Deprecated
    private final IBinder zanw;
    @Field(id = 3)
    private final Scope[] zanx;
    @Field(id = 4)
    private Integer zany;
    @Field(id = 5)
    private Integer zanz;
    @Field(id = 6, type = "android.accounts.Account")
    private Account zax;

    @Constructor
    AuthAccountRequest(@Param(id = 1) int i, @Param(id = 2) IBinder iBinder, @Param(id = 3) Scope[] scopeArr, @Param(id = 4) Integer num, @Param(id = 5) Integer num2, @Param(id = 6) Account account) {
        this.zale = i;
        this.zanw = iBinder;
        this.zanx = scopeArr;
        this.zany = num;
        this.zanz = num2;
        this.zax = account;
    }

    @Deprecated
    public AuthAccountRequest(IAccountAccessor iAccountAccessor, Set<Scope> set) {
        this(3, iAccountAccessor.asBinder(), (Scope[]) set.toArray(new Scope[set.size()]), null, null, null);
    }

    public AuthAccountRequest(Account account, Set<Scope> set) {
        this(3, null, (Scope[]) set.toArray(new Scope[set.size()]), null, null, (Account) Preconditions.checkNotNull(account));
    }

    public Account getAccount() {
        Account account = this.zax;
        if (account != null) {
            return account;
        }
        IBinder iBinder = this.zanw;
        if (iBinder != null) {
            return AccountAccessor.getAccountBinderSafe(Stub.asInterface(iBinder));
        }
        return null;
    }

    public Set<Scope> getScopes() {
        return new HashSet(Arrays.asList(this.zanx));
    }

    public AuthAccountRequest setOauthPolicy(@Nullable Integer num) {
        this.zany = num;
        return this;
    }

    @Nullable
    public Integer getOauthPolicy() {
        return this.zany;
    }

    public AuthAccountRequest setPolicyAction(@Nullable Integer num) {
        this.zanz = num;
        return this;
    }

    @Nullable
    public Integer getPolicyAction() {
        return this.zanz;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zale);
        SafeParcelWriter.writeIBinder(parcel, 2, this.zanw, false);
        SafeParcelWriter.writeTypedArray(parcel, 3, this.zanx, i, false);
        SafeParcelWriter.writeIntegerObject(parcel, 4, this.zany, false);
        SafeParcelWriter.writeIntegerObject(parcel, 5, this.zanz, false);
        SafeParcelWriter.writeParcelable(parcel, 6, this.zax, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
