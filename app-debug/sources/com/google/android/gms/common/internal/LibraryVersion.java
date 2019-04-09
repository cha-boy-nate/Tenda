package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@KeepForSdk
public class LibraryVersion {
    private static final GmsLogger zzel = new GmsLogger("LibraryVersion", "");
    private static LibraryVersion zzem = new LibraryVersion();
    private ConcurrentHashMap<String, String> zzen = new ConcurrentHashMap();

    @KeepForSdk
    public static LibraryVersion getInstance() {
        return zzem;
    }

    @VisibleForTesting
    protected LibraryVersion() {
    }

    @KeepForSdk
    public String getVersion(@NonNull String str) {
        Preconditions.checkNotEmpty(str, "Please provide a valid libraryName");
        if (this.zzen.containsKey(str)) {
            return (String) this.zzen.get(str);
        }
        Properties properties = new Properties();
        String str2 = null;
        String str3;
        String valueOf;
        try {
            InputStream resourceAsStream = LibraryVersion.class.getResourceAsStream(String.format("/%s.properties", new Object[]{str}));
            if (resourceAsStream != null) {
                properties.load(resourceAsStream);
                str2 = properties.getProperty("version", null);
                StringBuilder stringBuilder = new StringBuilder((String.valueOf(str).length() + 12) + String.valueOf(str2).length());
                stringBuilder.append(str);
                stringBuilder.append(" version is ");
                stringBuilder.append(str2);
                zzel.m9v("LibraryVersion", stringBuilder.toString());
            } else {
                GmsLogger gmsLogger = zzel;
                String str4 = "LibraryVersion";
                str3 = "Failed to get app version for libraryName: ";
                valueOf = String.valueOf(str);
                gmsLogger.m5e(str4, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            }
        } catch (Throwable e) {
            GmsLogger gmsLogger2 = zzel;
            str3 = "LibraryVersion";
            valueOf = "Failed to get app version for libraryName: ";
            String valueOf2 = String.valueOf(str);
            gmsLogger2.m6e(str3, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf), e);
        }
        if (str2 == null) {
            str2 = "UNKNOWN";
            zzel.m3d("LibraryVersion", ".properties file is dropped during release process. Failure to read app version isexpected druing Google internal testing where locally-built libraries are used");
        }
        this.zzen.put(str, str2);
        return str2;
    }
}
