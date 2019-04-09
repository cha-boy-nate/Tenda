package com.google.android.gms.common.data;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

@ShowFirstParty
@KeepForSdk
@Class(creator = "BitmapTeleporterCreator")
public class BitmapTeleporter extends AbstractSafeParcelable implements ReflectedParcelable {
    @KeepForSdk
    public static final Creator<BitmapTeleporter> CREATOR = new zaa();
    @Field(id = 3)
    private final int mType;
    @VersionField(id = 1)
    private final int zale;
    @Field(id = 2)
    private ParcelFileDescriptor zalf;
    private Bitmap zalg;
    private boolean zalh;
    private File zali;

    @Constructor
    BitmapTeleporter(@Param(id = 1) int i, @Param(id = 2) ParcelFileDescriptor parcelFileDescriptor, @Param(id = 3) int i2) {
        this.zale = i;
        this.zalf = parcelFileDescriptor;
        this.mType = i2;
        this.zalg = 0;
        this.zalh = false;
    }

    @KeepForSdk
    public BitmapTeleporter(Bitmap bitmap) {
        this.zale = 1;
        this.zalf = null;
        this.mType = 0;
        this.zalg = bitmap;
        this.zalh = true;
    }

    @KeepForSdk
    public Bitmap get() {
        if (!this.zalh) {
            Closeable dataInputStream = new DataInputStream(new AutoCloseInputStream(this.zalf));
            try {
                byte[] bArr = new byte[dataInputStream.readInt()];
                int readInt = dataInputStream.readInt();
                int readInt2 = dataInputStream.readInt();
                Config valueOf = Config.valueOf(dataInputStream.readUTF());
                dataInputStream.read(bArr);
                zaa(dataInputStream);
                Buffer wrap = ByteBuffer.wrap(bArr);
                Bitmap createBitmap = Bitmap.createBitmap(readInt, readInt2, valueOf);
                createBitmap.copyPixelsFromBuffer(wrap);
                this.zalg = createBitmap;
                this.zalh = true;
            } catch (Throwable e) {
                throw new IllegalStateException("Could not read from parcel file descriptor", e);
            } catch (Throwable th) {
                zaa(dataInputStream);
            }
        }
        return this.zalg;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.zalf == null) {
            Bitmap bitmap = this.zalg;
            Buffer allocate = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer(allocate);
            byte[] array = allocate.array();
            Closeable dataOutputStream = new DataOutputStream(new BufferedOutputStream(zabz()));
            try {
                dataOutputStream.writeInt(array.length);
                dataOutputStream.writeInt(bitmap.getWidth());
                dataOutputStream.writeInt(bitmap.getHeight());
                dataOutputStream.writeUTF(bitmap.getConfig().toString());
                dataOutputStream.write(array);
                zaa(dataOutputStream);
            } catch (Parcel parcel2) {
                throw new IllegalStateException("Could not write into unlinked file", parcel2);
            } catch (Throwable th) {
                zaa(dataOutputStream);
            }
        }
        i |= 1;
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel2);
        SafeParcelWriter.writeInt(parcel2, 1, this.zale);
        SafeParcelWriter.writeParcelable(parcel2, 2, this.zalf, i, false);
        SafeParcelWriter.writeInt(parcel2, 3, this.mType);
        SafeParcelWriter.finishObjectHeader(parcel2, beginObjectHeader);
        this.zalf = null;
    }

    @KeepForSdk
    public void release() {
        if (!this.zalh) {
            try {
                this.zalf.close();
            } catch (Throwable e) {
                Log.w("BitmapTeleporter", "Could not close PFD", e);
            }
        }
    }

    @KeepForSdk
    public void setTempDir(File file) {
        if (file != null) {
            this.zali = file;
            return;
        }
        throw new NullPointerException("Cannot set null temp directory");
    }

    private final FileOutputStream zabz() {
        File file = this.zali;
        if (file != null) {
            try {
                file = File.createTempFile("teleporter", ".tmp", file);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    this.zalf = ParcelFileDescriptor.open(file, 268435456);
                    file.delete();
                    return fileOutputStream;
                } catch (FileNotFoundException e) {
                    throw new IllegalStateException("Temporary file is somehow already deleted");
                }
            } catch (Throwable e2) {
                throw new IllegalStateException("Could not create temporary file", e2);
            }
        }
        throw new IllegalStateException("setTempDir() must be called before writing this object to a parcel");
    }

    private static void zaa(Closeable closeable) {
        try {
            closeable.close();
        } catch (Closeable closeable2) {
            Log.w("BitmapTeleporter", "Could not close stream", closeable2);
        }
    }
}
