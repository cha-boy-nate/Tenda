package com.google.android.gms.common.util;

import com.google.android.gms.common.annotation.KeepForSdk;
import java.util.HashMap;

@KeepForSdk
public class MapUtils {
    @KeepForSdk
    public static void writeStringMapToJson(StringBuilder stringBuilder, HashMap<String, String> hashMap) {
        stringBuilder.append("{");
        Object obj = 1;
        for (String str : hashMap.keySet()) {
            if (obj == null) {
                stringBuilder.append(",");
            } else {
                obj = null;
            }
            String str2 = (String) hashMap.get(str);
            stringBuilder.append("\"");
            stringBuilder.append(str);
            stringBuilder.append("\":");
            if (str2 == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append("\"");
                stringBuilder.append(str2);
                stringBuilder.append("\"");
            }
        }
        stringBuilder.append("}");
    }
}
