package com.google.android.gms.common.util;

import com.google.android.gms.common.annotation.KeepForSdk;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

@KeepForSdk
public class HttpUtils {
    private static final Pattern zzgy = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern zzgz = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern zzha = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    @KeepForSdk
    public static Map<String, String> parse(URI uri, String str) {
        Map<String, String> emptyMap = Collections.emptyMap();
        uri = uri.getRawQuery();
        if (uri != null && uri.length() > 0) {
            emptyMap = new HashMap();
            Scanner scanner = new Scanner(uri);
            scanner.useDelimiter("&");
            while (scanner.hasNext() != null) {
                uri = scanner.next().split("=");
                if (uri.length == 0 || uri.length > 2) {
                    throw new IllegalArgumentException("bad parameter");
                }
                String decode = decode(uri[0], str);
                Object obj = null;
                if (uri.length == 2) {
                    obj = decode(uri[1], str);
                }
                emptyMap.put(decode, obj);
            }
        }
        return emptyMap;
    }

    private static String decode(String str, String str2) {
        if (str2 == null) {
            try {
                str2 = "ISO-8859-1";
            } catch (String str3) {
                throw new IllegalArgumentException(str3);
            }
        }
        return URLDecoder.decode(str3, str2);
    }

    private HttpUtils() {
    }
}
