package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
    private final File mBackupName;
    private final File mBaseName;

    @android.support.annotation.NonNull
    public byte[] readFully() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:17:0x0031 in {6, 12, 13, 16} preds:[]
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
        r7 = this;
        r0 = r7.openRead();
        r1 = 0;
        r2 = r0.available();	 Catch:{ all -> 0x002c }
        r3 = new byte[r2];	 Catch:{ all -> 0x002c }
    L_0x000b:
        r4 = r3.length;	 Catch:{ all -> 0x002c }
        r4 = r4 - r1;	 Catch:{ all -> 0x002c }
        r4 = r0.read(r3, r1, r4);	 Catch:{ all -> 0x002c }
        if (r4 > 0) goto L_0x0018;
        r0.close();
        return r3;
    L_0x0018:
        r1 = r1 + r4;
        r5 = r0.available();	 Catch:{ all -> 0x002c }
        r2 = r5;	 Catch:{ all -> 0x002c }
        r5 = r3.length;	 Catch:{ all -> 0x002c }
        r5 = r5 - r1;	 Catch:{ all -> 0x002c }
        if (r2 <= r5) goto L_0x002b;	 Catch:{ all -> 0x002c }
    L_0x0022:
        r5 = r1 + r2;	 Catch:{ all -> 0x002c }
        r5 = new byte[r5];	 Catch:{ all -> 0x002c }
        r6 = 0;	 Catch:{ all -> 0x002c }
        java.lang.System.arraycopy(r3, r6, r5, r6, r1);	 Catch:{ all -> 0x002c }
        r3 = r5;
    L_0x002b:
        goto L_0x000b;
    L_0x002c:
        r1 = move-exception;
        r0.close();
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.AtomicFile.readFully():byte[]");
    }

    public AtomicFile(@NonNull File baseName) {
        this.mBaseName = baseName;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(baseName.getPath());
        stringBuilder.append(".bak");
        this.mBackupName = new File(stringBuilder.toString());
    }

    @NonNull
    public File getBaseFile() {
        return this.mBaseName;
    }

    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }

    @NonNull
    public FileOutputStream startWrite() throws IOException {
        if (this.mBaseName.exists()) {
            if (this.mBackupName.exists()) {
                this.mBaseName.delete();
            } else if (!this.mBaseName.renameTo(this.mBackupName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't rename file ");
                stringBuilder.append(this.mBaseName);
                stringBuilder.append(" to backup file ");
                stringBuilder.append(this.mBackupName);
                Log.w("AtomicFile", stringBuilder.toString());
            }
        }
        try {
            return new FileOutputStream(this.mBaseName);
        } catch (FileNotFoundException e) {
            if (this.mBaseName.getParentFile().mkdirs()) {
                try {
                    return new FileOutputStream(this.mBaseName);
                } catch (FileNotFoundException e2) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Couldn't create ");
                    stringBuilder2.append(this.mBaseName);
                    throw new IOException(stringBuilder2.toString());
                }
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Couldn't create directory ");
            stringBuilder3.append(this.mBaseName);
            throw new IOException(stringBuilder3.toString());
        }
    }

    public void finishWrite(@Nullable FileOutputStream str) {
        if (str != null) {
            sync(str);
            try {
                str.close();
                this.mBackupName.delete();
            } catch (IOException e) {
                Log.w("AtomicFile", "finishWrite: Got exception:", e);
            }
        }
    }

    public void failWrite(@Nullable FileOutputStream str) {
        if (str != null) {
            sync(str);
            try {
                str.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
            } catch (IOException e) {
                Log.w("AtomicFile", "failWrite: Got exception:", e);
            }
        }
    }

    @NonNull
    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }

    private static boolean sync(@NonNull FileOutputStream stream) {
        try {
            stream.getFD().sync();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
