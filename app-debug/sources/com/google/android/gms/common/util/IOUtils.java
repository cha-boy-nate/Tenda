package com.google.android.gms.common.util;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ShowFirstParty;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nullable;

@ShowFirstParty
@KeepForSdk
public final class IOUtils {
    private IOUtils() {
    }

    @com.google.android.gms.common.annotation.KeepForSdk
    public static long copyStream(java.io.InputStream r7, java.io.OutputStream r8, boolean r9, int r10) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:14:0x0025 in {6, 8, 9, 12, 13} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r0 = new byte[r10];
        r1 = 0;
    L_0x0004:
        r3 = 0;
        r4 = r7.read(r0, r3, r10);	 Catch:{ all -> 0x001b }
        r5 = -1;	 Catch:{ all -> 0x001b }
        if (r4 == r5) goto L_0x0012;	 Catch:{ all -> 0x001b }
    L_0x000c:
        r5 = (long) r4;	 Catch:{ all -> 0x001b }
        r1 = r1 + r5;	 Catch:{ all -> 0x001b }
        r8.write(r0, r3, r4);	 Catch:{ all -> 0x001b }
        goto L_0x0004;
    L_0x0012:
        if (r9 == 0) goto L_0x001a;
    L_0x0014:
        closeQuietly(r7);
        closeQuietly(r8);
    L_0x001a:
        return r1;
    L_0x001b:
        r10 = move-exception;
        if (r9 == 0) goto L_0x0024;
    L_0x001e:
        closeQuietly(r7);
        closeQuietly(r8);
    L_0x0024:
        throw r10;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.IOUtils.copyStream(java.io.InputStream, java.io.OutputStream, boolean, int):long");
    }

    @KeepForSdk
    public static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    @KeepForSdk
    public static void closeQuietly(@Nullable ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
            }
        }
    }

    @KeepForSdk
    public static boolean isGzipByteBuffer(byte[] bArr) {
        if (bArr.length > 1) {
            if ((((bArr[1] & 255) << 8) | (bArr[0] & 255)) == 35615) {
                return true;
            }
        }
        return false;
    }

    @KeepForSdk
    public static long copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        return zza(inputStream, outputStream, false);
    }

    private static long zza(InputStream inputStream, OutputStream outputStream, boolean z) throws IOException {
        return copyStream(inputStream, outputStream, z, 1024);
    }

    @KeepForSdk
    public static byte[] readInputStreamFully(InputStream inputStream) throws IOException {
        return readInputStreamFully(inputStream, true);
    }

    @KeepForSdk
    public static byte[] readInputStreamFully(InputStream inputStream, boolean z) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zza(inputStream, byteArrayOutputStream, z);
        return byteArrayOutputStream.toByteArray();
    }

    @KeepForSdk
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(byteArrayOutputStream);
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }
}
