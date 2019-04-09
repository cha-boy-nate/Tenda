package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    @GuardedBy("sCache")
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    interface PathStrategy {
        File getFileForUri(Uri uri);

        Uri getUriForFile(File file);
    }

    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        public android.net.Uri getUriForFile(java.io.File r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:24:0x00d8 in {9, 10, 11, 15, 16, 18, 20, 23} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
            /*
            r7 = this;
            r0 = r8.getCanonicalPath();	 Catch:{ IOException -> 0x00c0 }
            r1 = 0;
            r2 = r7.mRoots;
            r2 = r2.entrySet();
            r2 = r2.iterator();
        L_0x0010:
            r3 = r2.hasNext();
            if (r3 == 0) goto L_0x0044;
        L_0x0016:
            r3 = r2.next();
            r3 = (java.util.Map.Entry) r3;
            r4 = r3.getValue();
            r4 = (java.io.File) r4;
            r4 = r4.getPath();
            r5 = r0.startsWith(r4);
            if (r5 == 0) goto L_0x0043;
        L_0x002c:
            if (r1 == 0) goto L_0x0042;
        L_0x002e:
            r5 = r4.length();
            r6 = r1.getValue();
            r6 = (java.io.File) r6;
            r6 = r6.getPath();
            r6 = r6.length();
            if (r5 <= r6) goto L_0x0043;
        L_0x0042:
            r1 = r3;
        L_0x0043:
            goto L_0x0010;
        L_0x0044:
            if (r1 == 0) goto L_0x00a9;
        L_0x0046:
            r2 = r1.getValue();
            r2 = (java.io.File) r2;
            r2 = r2.getPath();
            r3 = "/";
            r3 = r2.endsWith(r3);
            if (r3 == 0) goto L_0x0061;
        L_0x0058:
            r3 = r2.length();
            r0 = r0.substring(r3);
            goto L_0x006b;
        L_0x0061:
            r3 = r2.length();
            r3 = r3 + 1;
            r0 = r0.substring(r3);
        L_0x006b:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = r1.getKey();
            r4 = (java.lang.String) r4;
            r4 = android.net.Uri.encode(r4);
            r3.append(r4);
            r4 = 47;
            r3.append(r4);
            r4 = "/";
            r4 = android.net.Uri.encode(r0, r4);
            r3.append(r4);
            r0 = r3.toString();
            r3 = new android.net.Uri$Builder;
            r3.<init>();
            r4 = "content";
            r3 = r3.scheme(r4);
            r4 = r7.mAuthority;
            r3 = r3.authority(r4);
            r3 = r3.encodedPath(r0);
            r3 = r3.build();
            return r3;
        L_0x00a9:
            r2 = new java.lang.IllegalArgumentException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Failed to find configured root that contains ";
            r3.append(r4);
            r3.append(r0);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
        L_0x00c0:
            r0 = move-exception;
            r1 = new java.lang.IllegalArgumentException;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Failed to resolve canonical path for ";
            r2.append(r3);
            r2.append(r8);
            r2 = r2.toString();
            r1.<init>(r2);
            throw r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.FileProvider.SimplePathStrategy.getUriForFile(java.io.File):android.net.Uri");
        }

        SimplePathStrategy(String authority) {
            this.mAuthority = authority;
        }

        void addRoot(String name, File root) {
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(name, root.getCanonicalFile());
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to resolve canonical path for ");
                stringBuilder.append(root);
                throw new IllegalArgumentException(stringBuilder.toString(), e);
            }
        }

        public File getFileForUri(Uri uri) {
            String path = uri.getEncodedPath();
            int splitIndex = path.indexOf(47, 1);
            String tag = Uri.decode(path.substring(1, splitIndex));
            path = Uri.decode(path.substring(splitIndex + 1));
            File root = (File) this.mRoots.get(tag);
            if (root != null) {
                File file = new File(root, path);
                try {
                    file = file.getCanonicalFile();
                    if (file.getPath().startsWith(root.getPath())) {
                        return file;
                    }
                    throw new SecurityException("Resolved path jumped beyond configured root");
                } catch (IOException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(file);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to find configured root for ");
            stringBuilder2.append(uri);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }

    private static android.support.v4.content.FileProvider.PathStrategy parsePathStrategy(android.content.Context r12, java.lang.String r13) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:43:0x00bf in {8, 11, 14, 17, 22, 23, 28, 29, 36, 38, 39, 40, 42} preds:[]
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
        r0 = new android.support.v4.content.FileProvider$SimplePathStrategy;
        r0.<init>(r13);
        r1 = r12.getPackageManager();
        r2 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r1 = r1.resolveContentProvider(r13, r2);
        r2 = r12.getPackageManager();
        r3 = "android.support.FILE_PROVIDER_PATHS";
        r2 = r1.loadXmlMetaData(r2, r3);
        if (r2 == 0) goto L_0x00b7;
    L_0x001c:
        r3 = r2.next();
        r4 = r3;
        r5 = 1;
        if (r3 == r5) goto L_0x00b6;
    L_0x0024:
        r3 = 2;
        if (r4 != r3) goto L_0x001c;
    L_0x0027:
        r3 = r2.getName();
        r6 = "name";
        r7 = 0;
        r6 = r2.getAttributeValue(r7, r6);
        r8 = "path";
        r8 = r2.getAttributeValue(r7, r8);
        r9 = 0;
        r10 = "root-path";
        r10 = r10.equals(r3);
        r11 = 0;
        if (r10 == 0) goto L_0x0045;
    L_0x0042:
        r9 = DEVICE_ROOT;
        goto L_0x00a7;
    L_0x0045:
        r10 = "files-path";
        r10 = r10.equals(r3);
        if (r10 == 0) goto L_0x0052;
    L_0x004d:
        r9 = r12.getFilesDir();
        goto L_0x00a7;
    L_0x0052:
        r10 = "cache-path";
        r10 = r10.equals(r3);
        if (r10 == 0) goto L_0x005f;
    L_0x005a:
        r9 = r12.getCacheDir();
        goto L_0x00a7;
    L_0x005f:
        r10 = "external-path";
        r10 = r10.equals(r3);
        if (r10 == 0) goto L_0x006c;
    L_0x0067:
        r9 = android.os.Environment.getExternalStorageDirectory();
        goto L_0x00a7;
    L_0x006c:
        r10 = "external-files-path";
        r10 = r10.equals(r3);
        if (r10 == 0) goto L_0x007e;
    L_0x0074:
        r7 = android.support.v4.content.ContextCompat.getExternalFilesDirs(r12, r7);
        r10 = r7.length;
        if (r10 <= 0) goto L_0x007d;
    L_0x007b:
        r9 = r7[r11];
    L_0x007d:
        goto L_0x00a7;
    L_0x007e:
        r7 = "external-cache-path";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x0090;
    L_0x0086:
        r7 = android.support.v4.content.ContextCompat.getExternalCacheDirs(r12);
        r10 = r7.length;
        if (r10 <= 0) goto L_0x008f;
    L_0x008d:
        r9 = r7[r11];
    L_0x008f:
        goto L_0x00a7;
    L_0x0090:
        r7 = android.os.Build.VERSION.SDK_INT;
        r10 = 21;
        if (r7 < r10) goto L_0x008f;
    L_0x0096:
        r7 = "external-media-path";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x00a7;
    L_0x009e:
        r7 = r12.getExternalMediaDirs();
        r10 = r7.length;
        if (r10 <= 0) goto L_0x00a7;
    L_0x00a5:
        r9 = r7[r11];
    L_0x00a7:
        if (r9 == 0) goto L_0x00b4;
    L_0x00a9:
        r5 = new java.lang.String[r5];
        r5[r11] = r8;
        r5 = buildPath(r9, r5);
        r0.addRoot(r6, r5);
    L_0x00b4:
        goto L_0x001c;
    L_0x00b6:
        return r0;
    L_0x00b7:
        r3 = new java.lang.IllegalArgumentException;
        r4 = "Missing android.support.FILE_PROVIDER_PATHS meta-data";
        r3.<init>(r4);
        throw r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.FileProvider.parsePathStrategy(android.content.Context, java.lang.String):android.support.v4.content.FileProvider$PathStrategy");
    }

    public boolean onCreate() {
        return true;
    }

    public void attachInfo(@NonNull Context context, @NonNull ProviderInfo info) {
        super.attachInfo(context, info);
        if (info.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if (info.grantUriPermissions) {
            this.mStrategy = getPathStrategy(context, info.authority);
        } else {
            throw new SecurityException("Provider must grant uri permissions");
        }
    }

    public static Uri getUriForFile(@NonNull Context context, @NonNull String authority, @NonNull File file) {
        return getPathStrategy(context, authority).getUriForFile(file);
    }

    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        File file = this.mStrategy.getFileForUri(uri);
        if (projection == null) {
            projection = COLUMNS;
        }
        String[] cols = new String[projection.length];
        Object[] values = new Object[projection.length];
        int i = 0;
        for (String col : projection) {
            int i2;
            if ("_display_name".equals(col)) {
                cols[i] = "_display_name";
                i2 = i + 1;
                values[i] = file.getName();
                i = i2;
            } else if ("_size".equals(col)) {
                cols[i] = "_size";
                i2 = i + 1;
                values[i] = Long.valueOf(file.length());
                i = i2;
            }
        }
        cols = copyOf(cols, i);
        values = copyOf(values, i);
        MatrixCursor cursor = new MatrixCursor(cols, 1);
        cursor.addRow(values);
        return cursor;
    }

    public String getType(@NonNull Uri uri) {
        File file = this.mStrategy.getFileForUri(uri);
        int lastDot = file.getName().lastIndexOf(46);
        if (lastDot >= 0) {
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName().substring(lastDot + 1));
            if (mime != null) {
                return mime;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public int update(@NonNull Uri uri, ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("No external updates");
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return this.mStrategy.getFileForUri(uri).delete();
    }

    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(mode));
    }

    private static PathStrategy getPathStrategy(Context context, String authority) {
        PathStrategy strat;
        synchronized (sCache) {
            strat = (PathStrategy) sCache.get(authority);
            if (strat == null) {
                try {
                    strat = parsePathStrategy(context, authority);
                    sCache.put(authority, strat);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);
                } catch (XmlPullParserException e2) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e2);
                }
            }
        }
        return strat;
    }

    private static int modeToMode(String mode) {
        if ("r".equals(mode)) {
            return 268435456;
        }
        if (!"w".equals(mode)) {
            if (!"wt".equals(mode)) {
                if ("wa".equals(mode)) {
                    return 704643072;
                }
                if ("rw".equals(mode)) {
                    return 939524096;
                }
                if ("rwt".equals(mode)) {
                    return 1006632960;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid mode: ");
                stringBuilder.append(mode);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        return 738197504;
    }

    private static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (segment != null) {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }

    private static String[] copyOf(String[] original, int newLength) {
        String[] result = new String[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        Object[] result = new Object[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }
}
