package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class TreeDocumentFile extends DocumentFile {
    private Context mContext;
    private Uri mUri;

    public android.support.v4.provider.DocumentFile[] listFiles() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x0083 in {6, 7, 14, 18, 19, 21} preds:[]
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
        r11 = this;
        r0 = r11.mContext;
        r0 = r0.getContentResolver();
        r1 = r11.mUri;
        r2 = android.provider.DocumentsContract.getDocumentId(r1);
        r7 = android.provider.DocumentsContract.buildChildDocumentsUriUsingTree(r1, r2);
        r1 = new java.util.ArrayList;
        r1.<init>();
        r8 = r1;
        r9 = 0;
        r1 = 1;
        r3 = new java.lang.String[r1];	 Catch:{ Exception -> 0x0044 }
        r1 = "document_id";	 Catch:{ Exception -> 0x0044 }
        r10 = 0;	 Catch:{ Exception -> 0x0044 }
        r3[r10] = r1;	 Catch:{ Exception -> 0x0044 }
        r4 = 0;	 Catch:{ Exception -> 0x0044 }
        r5 = 0;	 Catch:{ Exception -> 0x0044 }
        r6 = 0;	 Catch:{ Exception -> 0x0044 }
        r1 = r0;	 Catch:{ Exception -> 0x0044 }
        r2 = r7;	 Catch:{ Exception -> 0x0044 }
        r1 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0044 }
        r9 = r1;	 Catch:{ Exception -> 0x0044 }
    L_0x0029:
        r1 = r9.moveToNext();	 Catch:{ Exception -> 0x0044 }
        if (r1 == 0) goto L_0x003d;	 Catch:{ Exception -> 0x0044 }
    L_0x002f:
        r1 = r9.getString(r10);	 Catch:{ Exception -> 0x0044 }
        r2 = r11.mUri;	 Catch:{ Exception -> 0x0044 }
        r2 = android.provider.DocumentsContract.buildDocumentUriUsingTree(r2, r1);	 Catch:{ Exception -> 0x0044 }
        r8.add(r2);	 Catch:{ Exception -> 0x0044 }
        goto L_0x0029;
    L_0x003e:
        closeQuietly(r9);
        goto L_0x005d;
    L_0x0042:
        r1 = move-exception;
        goto L_0x007f;
    L_0x0044:
        r1 = move-exception;
        r2 = "DocumentFile";	 Catch:{ all -> 0x0042 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0042 }
        r3.<init>();	 Catch:{ all -> 0x0042 }
        r4 = "Failed query: ";	 Catch:{ all -> 0x0042 }
        r3.append(r4);	 Catch:{ all -> 0x0042 }
        r3.append(r1);	 Catch:{ all -> 0x0042 }
        r3 = r3.toString();	 Catch:{ all -> 0x0042 }
        android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0042 }
        goto L_0x003e;
    L_0x005d:
        r1 = r8.size();
        r1 = new android.net.Uri[r1];
        r1 = r8.toArray(r1);
        r1 = (android.net.Uri[]) r1;
        r2 = r1.length;
        r2 = new android.support.v4.provider.DocumentFile[r2];
        r3 = 0;
    L_0x006d:
        r4 = r1.length;
        if (r3 >= r4) goto L_0x007e;
    L_0x0070:
        r4 = new android.support.v4.provider.TreeDocumentFile;
        r5 = r11.mContext;
        r6 = r1[r3];
        r4.<init>(r11, r5, r6);
        r2[r3] = r4;
        r3 = r3 + 1;
        goto L_0x006d;
    L_0x007e:
        return r2;
    L_0x007f:
        closeQuietly(r9);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.provider.TreeDocumentFile.listFiles():android.support.v4.provider.DocumentFile[]");
    }

    TreeDocumentFile(@Nullable DocumentFile parent, Context context, Uri uri) {
        super(parent);
        this.mContext = context;
        this.mUri = uri;
    }

    @Nullable
    public DocumentFile createFile(String mimeType, String displayName) {
        Uri result = createFile(this.mContext, this.mUri, mimeType, displayName);
        return result != null ? new TreeDocumentFile(this, this.mContext, result) : null;
    }

    @Nullable
    private static Uri createFile(Context context, Uri self, String mimeType, String displayName) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), self, mimeType, displayName);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public DocumentFile createDirectory(String displayName) {
        Uri result = createFile(this.mContext, this.mUri, "vnd.android.document/directory", displayName);
        return result != null ? new TreeDocumentFile(this, this.mContext, result) : null;
    }

    public Uri getUri() {
        return this.mUri;
    }

    @Nullable
    public String getName() {
        return DocumentsContractApi19.getName(this.mContext, this.mUri);
    }

    @Nullable
    public String getType() {
        return DocumentsContractApi19.getType(this.mContext, this.mUri);
    }

    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
    }

    public boolean isFile() {
        return DocumentsContractApi19.isFile(this.mContext, this.mUri);
    }

    public boolean isVirtual() {
        return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
    }

    public long lastModified() {
        return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
    }

    public long length() {
        return DocumentsContractApi19.length(this.mContext, this.mUri);
    }

    public boolean canRead() {
        return DocumentsContractApi19.canRead(this.mContext, this.mUri);
    }

    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
    }

    public boolean delete() {
        try {
            return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exists() {
        return DocumentsContractApi19.exists(this.mContext, this.mUri);
    }

    private static void closeQuietly(@Nullable AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public boolean renameTo(String displayName) {
        try {
            Uri result = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, displayName);
            if (result == null) {
                return false;
            }
            this.mUri = result;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
