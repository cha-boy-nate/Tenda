package android.support.v4.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

@RequiresApi(19)
class DocumentsContractApi19 {
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";

    public static boolean isVirtual(Context context, Uri self) {
        boolean z = false;
        if (!DocumentsContract.isDocumentUri(context, self)) {
            return false;
        }
        if ((getFlags(context, self) & 512) != 0) {
            z = true;
        }
        return z;
    }

    @Nullable
    public static String getName(Context context, Uri self) {
        return queryForString(context, self, "_display_name", null);
    }

    @Nullable
    private static String getRawType(Context context, Uri self) {
        return queryForString(context, self, "mime_type", null);
    }

    @Nullable
    public static String getType(Context context, Uri self) {
        String rawType = getRawType(context, self);
        if ("vnd.android.document/directory".equals(rawType)) {
            return null;
        }
        return rawType;
    }

    public static long getFlags(Context context, Uri self) {
        return queryForLong(context, self, "flags", 0);
    }

    public static boolean isDirectory(Context context, Uri self) {
        return "vnd.android.document/directory".equals(getRawType(context, self));
    }

    public static boolean isFile(Context context, Uri self) {
        String type = getRawType(context, self);
        if (!"vnd.android.document/directory".equals(type)) {
            if (!TextUtils.isEmpty(type)) {
                return true;
            }
        }
        return false;
    }

    public static long lastModified(Context context, Uri self) {
        return queryForLong(context, self, "last_modified", 0);
    }

    public static long length(Context context, Uri self) {
        return queryForLong(context, self, "_size", 0);
    }

    public static boolean canRead(Context context, Uri self) {
        return context.checkCallingOrSelfUriPermission(self, 1) == 0 && !TextUtils.isEmpty(getRawType(context, self));
    }

    public static boolean canWrite(Context context, Uri self) {
        if (context.checkCallingOrSelfUriPermission(self, 2) != 0) {
            return false;
        }
        String type = getRawType(context, self);
        int flags = queryForInt(context, self, "flags", 0);
        if (TextUtils.isEmpty(type)) {
            return false;
        }
        if ((flags & 4) != 0) {
            return true;
        }
        if ("vnd.android.document/directory".equals(type) && (flags & 8) != 0) {
            return true;
        }
        if (TextUtils.isEmpty(type) || (flags & 2) == 0) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean exists(android.content.Context r10, android.net.Uri r11) {
        /*
        r6 = r10.getContentResolver();
        r7 = 0;
        r8 = 1;
        r9 = 0;
        r2 = new java.lang.String[r8];	 Catch:{ Exception -> 0x0025 }
        r0 = "document_id";
        r2[r9] = r0;	 Catch:{ Exception -> 0x0025 }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r0 = r6;
        r1 = r11;
        r0 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x0025 }
        r7 = r0;
        r0 = r7.getCount();	 Catch:{ Exception -> 0x0025 }
        if (r0 <= 0) goto L_0x001e;
    L_0x001d:
        goto L_0x001f;
    L_0x001e:
        r8 = 0;
    L_0x001f:
        closeQuietly(r7);
        return r8;
    L_0x0023:
        r0 = move-exception;
        goto L_0x0041;
    L_0x0025:
        r0 = move-exception;
        r1 = "DocumentFile";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0023 }
        r2.<init>();	 Catch:{ all -> 0x0023 }
        r3 = "Failed query: ";
        r2.append(r3);	 Catch:{ all -> 0x0023 }
        r2.append(r0);	 Catch:{ all -> 0x0023 }
        r2 = r2.toString();	 Catch:{ all -> 0x0023 }
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x0023 }
        closeQuietly(r7);
        return r9;
    L_0x0041:
        closeQuietly(r7);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.provider.DocumentsContractApi19.exists(android.content.Context, android.net.Uri):boolean");
    }

    @Nullable
    private static String queryForString(Context context, Uri self, String column, @Nullable String defaultValue) {
        Cursor c = null;
        String string;
        try {
            c = context.getContentResolver().query(self, new String[]{column}, null, null, null);
            if (!c.moveToFirst() || c.isNull(0)) {
                closeQuietly(c);
                return defaultValue;
            }
            string = c.getString(0);
            return string;
        } catch (Exception e) {
            string = e;
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed query: ");
            stringBuilder.append(string);
            Log.w(str, stringBuilder.toString());
            return defaultValue;
        } finally {
            closeQuietly(c);
        }
    }

    private static int queryForInt(Context context, Uri self, String column, int defaultValue) {
        return (int) queryForLong(context, self, column, (long) defaultValue);
    }

    private static long queryForLong(Context context, Uri self, String column, long defaultValue) {
        long j;
        Cursor c = null;
        try {
            c = context.getContentResolver().query(self, new String[]{column}, null, null, null);
            if (!c.moveToFirst() || c.isNull(0)) {
                closeQuietly(c);
                return defaultValue;
            }
            j = c.getLong(0);
            return j;
        } catch (Exception e) {
            j = e;
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed query: ");
            stringBuilder.append(j);
            Log.w(str, stringBuilder.toString());
            return defaultValue;
        } finally {
            closeQuietly(c);
        }
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

    private DocumentsContractApi19() {
    }
}
