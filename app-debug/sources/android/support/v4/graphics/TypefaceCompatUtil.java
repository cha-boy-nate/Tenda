package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

@RestrictTo({Scope.LIBRARY_GROUP})
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    @android.support.annotation.Nullable
    @android.support.annotation.RequiresApi(19)
    public static java.nio.ByteBuffer copyToDirectBuffer(android.content.Context r3, android.content.res.Resources r4, int r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x001d in {2, 6, 8, 11, 14} preds:[]
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
        r0 = getTempFile(r3);
        r1 = 0;
        if (r0 != 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r2 = copyToFile(r0, r4, r5);	 Catch:{ all -> 0x0018 }
        if (r2 != 0) goto L_0x0013;
    L_0x000f:
        r0.delete();
        return r1;
    L_0x0013:
        r1 = mmap(r0);	 Catch:{ all -> 0x0018 }
        goto L_0x000f;
    L_0x0018:
        r1 = move-exception;
        r0.delete();
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.copyToDirectBuffer(android.content.Context, android.content.res.Resources, int):java.nio.ByteBuffer");
    }

    public static boolean copyToFile(java.io.File r7, java.io.InputStream r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:18:0x0050 in {6, 8, 15, 17} preds:[]
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
        r0 = 0;
        r1 = android.os.StrictMode.allowThreadDiskWrites();
        r2 = 0;
        r3 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0026 }
        r3.<init>(r7, r2);	 Catch:{ IOException -> 0x0026 }
        r0 = r3;	 Catch:{ IOException -> 0x0026 }
        r3 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ IOException -> 0x0026 }
        r3 = new byte[r3];	 Catch:{ IOException -> 0x0026 }
    L_0x0010:
        r4 = r8.read(r3);	 Catch:{ IOException -> 0x0026 }
        r5 = r4;	 Catch:{ IOException -> 0x0026 }
        r6 = -1;	 Catch:{ IOException -> 0x0026 }
        if (r4 == r6) goto L_0x001c;	 Catch:{ IOException -> 0x0026 }
    L_0x0018:
        r0.write(r3, r2, r5);	 Catch:{ IOException -> 0x0026 }
        goto L_0x0010;
    L_0x001c:
        r2 = 1;
        closeQuietly(r0);
        android.os.StrictMode.setThreadPolicy(r1);
        return r2;
    L_0x0024:
        r2 = move-exception;
        goto L_0x0049;
    L_0x0026:
        r3 = move-exception;
        r4 = "TypefaceCompatUtil";	 Catch:{ all -> 0x0024 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0024 }
        r5.<init>();	 Catch:{ all -> 0x0024 }
        r6 = "Error copying resource contents to temp file: ";	 Catch:{ all -> 0x0024 }
        r5.append(r6);	 Catch:{ all -> 0x0024 }
        r6 = r3.getMessage();	 Catch:{ all -> 0x0024 }
        r5.append(r6);	 Catch:{ all -> 0x0024 }
        r5 = r5.toString();	 Catch:{ all -> 0x0024 }
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0024 }
        closeQuietly(r0);
        android.os.StrictMode.setThreadPolicy(r1);
        return r2;
    L_0x0049:
        closeQuietly(r0);
        android.os.StrictMode.setThreadPolicy(r1);
        throw r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.copyToFile(java.io.File, java.io.InputStream):boolean");
    }

    private TypefaceCompatUtil() {
    }

    @Nullable
    public static File getTempFile(Context context) {
        String prefix = new StringBuilder();
        prefix.append(CACHE_FILE_PREFIX);
        prefix.append(Process.myPid());
        prefix.append("-");
        prefix.append(Process.myTid());
        prefix.append("-");
        prefix = prefix.toString();
        int i = 0;
        while (i < 100) {
            File cacheDir = context.getCacheDir();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(i);
            File file = new File(cacheDir, stringBuilder.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException e) {
            }
        }
        return null;
    }

    @Nullable
    @RequiresApi(19)
    private static ByteBuffer mmap(File file) {
        Throwable th;
        Throwable th2;
        try {
            FileInputStream fis = new FileInputStream(file);
            try {
                FileChannel channel = fis.getChannel();
                ByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                fis.close();
                return map;
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
            if (th22 != null) {
                try {
                    fis.close();
                } catch (Throwable th4) {
                    th22.addSuppressed(th4);
                }
            } else {
                fis.close();
            }
            throw th;
            throw th;
        } catch (IOException e) {
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.Nullable
    @android.support.annotation.RequiresApi(19)
    public static java.nio.ByteBuffer mmap(android.content.Context r11, android.os.CancellationSignal r12, android.net.Uri r13) {
        /*
        r0 = r11.getContentResolver();
        r1 = 0;
        r2 = "r";
        r2 = r0.openFileDescriptor(r13, r2, r12);	 Catch:{ IOException -> 0x007f }
        if (r2 != 0) goto L_0x0014;
        if (r2 == 0) goto L_0x0013;
    L_0x0010:
        r2.close();	 Catch:{ IOException -> 0x007f }
    L_0x0013:
        return r1;
    L_0x0014:
        r3 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0060, all -> 0x005d }
        r4 = r2.getFileDescriptor();	 Catch:{ Throwable -> 0x0060, all -> 0x005d }
        r3.<init>(r4);	 Catch:{ Throwable -> 0x0060, all -> 0x005d }
        r4 = r3.getChannel();	 Catch:{ Throwable -> 0x0040, all -> 0x003d }
        r8 = r4.size();	 Catch:{ Throwable -> 0x0040, all -> 0x003d }
        r5 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x0040, all -> 0x003d }
        r6 = 0;
        r5 = r4.map(r5, r6, r8);	 Catch:{ Throwable -> 0x0040, all -> 0x003d }
        r3.close();	 Catch:{ Throwable -> 0x0060, all -> 0x005d }
        if (r2 == 0) goto L_0x003a;
        r2.close();	 Catch:{ IOException -> 0x007f }
        return r5;
    L_0x003d:
        r4 = move-exception;
        r5 = r1;
        goto L_0x0047;
    L_0x0040:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0043 }
    L_0x0043:
        r5 = move-exception;
        r10 = r5;
        r5 = r4;
        r4 = r10;
        if (r5 == 0) goto L_0x0056;
        r3.close();	 Catch:{ Throwable -> 0x0050, all -> 0x005d }
        goto L_0x005a;
    L_0x0050:
        r6 = move-exception;
        r5.addSuppressed(r6);	 Catch:{ Throwable -> 0x0060, all -> 0x005d }
        goto L_0x005a;
        r3.close();	 Catch:{ Throwable -> 0x0060, all -> 0x005d }
        throw r4;	 Catch:{ Throwable -> 0x0060, all -> 0x005d }
    L_0x005d:
        r3 = move-exception;
        r4 = r1;
        goto L_0x0067;
    L_0x0060:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0063 }
    L_0x0063:
        r4 = move-exception;
        r10 = r4;
        r4 = r3;
        r3 = r10;
        if (r2 == 0) goto L_0x007c;
        if (r4 == 0) goto L_0x0078;
        r2.close();	 Catch:{ Throwable -> 0x0072 }
        goto L_0x007c;
    L_0x0072:
        r5 = move-exception;
        r4.addSuppressed(r5);	 Catch:{ IOException -> 0x007f }
        goto L_0x007c;
        r2.close();	 Catch:{ IOException -> 0x007f }
        throw r3;	 Catch:{ IOException -> 0x007f }
    L_0x007f:
        r2 = move-exception;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    public static boolean copyToFile(File file, Resources res, int id) {
        InputStream is = null;
        try {
            is = res.openRawResource(id);
            boolean copyToFile = copyToFile(file, is);
            return copyToFile;
        } finally {
            closeQuietly(is);
        }
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
