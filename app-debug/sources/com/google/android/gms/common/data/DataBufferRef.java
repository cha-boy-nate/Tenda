package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;

@KeepForSdk
public class DataBufferRef {
    @KeepForSdk
    protected final DataHolder mDataHolder;
    @KeepForSdk
    protected int mDataRow;
    private int zalm;

    @KeepForSdk
    public DataBufferRef(DataHolder dataHolder, int i) {
        this.mDataHolder = (DataHolder) Preconditions.checkNotNull(dataHolder);
        zag(i);
    }

    @KeepForSdk
    protected int getDataRow() {
        return this.mDataRow;
    }

    protected final void zag(int i) {
        boolean z = i >= 0 && i < this.mDataHolder.getCount();
        Preconditions.checkState(z);
        this.mDataRow = i;
        this.zalm = this.mDataHolder.getWindowIndex(this.mDataRow);
    }

    @KeepForSdk
    public boolean isDataValid() {
        return !this.mDataHolder.isClosed();
    }

    @KeepForSdk
    public boolean hasColumn(String str) {
        return this.mDataHolder.hasColumn(str);
    }

    @KeepForSdk
    protected long getLong(String str) {
        return this.mDataHolder.getLong(str, this.mDataRow, this.zalm);
    }

    @KeepForSdk
    protected int getInteger(String str) {
        return this.mDataHolder.getInteger(str, this.mDataRow, this.zalm);
    }

    @KeepForSdk
    protected boolean getBoolean(String str) {
        return this.mDataHolder.getBoolean(str, this.mDataRow, this.zalm);
    }

    @KeepForSdk
    protected String getString(String str) {
        return this.mDataHolder.getString(str, this.mDataRow, this.zalm);
    }

    @KeepForSdk
    protected float getFloat(String str) {
        return this.mDataHolder.zaa(str, this.mDataRow, this.zalm);
    }

    @KeepForSdk
    protected double getDouble(String str) {
        return this.mDataHolder.zab(str, this.mDataRow, this.zalm);
    }

    @KeepForSdk
    protected byte[] getByteArray(String str) {
        return this.mDataHolder.getByteArray(str, this.mDataRow, this.zalm);
    }

    @KeepForSdk
    protected Uri parseUri(String str) {
        str = this.mDataHolder.getString(str, this.mDataRow, this.zalm);
        return str == null ? null : Uri.parse(str);
    }

    @KeepForSdk
    protected void copyToBuffer(String str, CharArrayBuffer charArrayBuffer) {
        this.mDataHolder.zaa(str, this.mDataRow, this.zalm, charArrayBuffer);
    }

    @KeepForSdk
    protected boolean hasNull(String str) {
        return this.mDataHolder.hasNull(str, this.mDataRow, this.zalm);
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.mDataRow), Integer.valueOf(this.zalm), this.mDataHolder);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DataBufferRef)) {
            return false;
        }
        DataBufferRef dataBufferRef = (DataBufferRef) obj;
        if (Objects.equal(Integer.valueOf(dataBufferRef.mDataRow), Integer.valueOf(this.mDataRow)) && Objects.equal(Integer.valueOf(dataBufferRef.zalm), Integer.valueOf(this.zalm)) && dataBufferRef.mDataHolder == this.mDataHolder) {
            return true;
        }
        return false;
    }
}
