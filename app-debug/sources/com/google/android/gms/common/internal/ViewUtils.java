package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public class ViewUtils {
    private ViewUtils() {
    }

    @KeepForSdk
    public static String getXmlAttributeString(String str, String str2, Context context, AttributeSet attributeSet, boolean z, boolean z2, String str3) {
        str = attributeSet == null ? null : attributeSet.getAttributeValue(str, str2);
        if (!(str == null || str.startsWith("@string/") == null || !z)) {
            z = str.substring(8);
            String packageName = context.getPackageName();
            TypedValue typedValue = new TypedValue();
            try {
                context = context.getResources();
                attributeSet = new StringBuilder((String.valueOf(packageName).length() + 8) + String.valueOf(z).length());
                attributeSet.append(packageName);
                attributeSet.append(":string/");
                attributeSet.append(z);
                context.getValue(attributeSet.toString(), typedValue, true);
            } catch (NotFoundException e) {
                attributeSet = new StringBuilder((String.valueOf(str2).length() + 30) + String.valueOf(str).length());
                attributeSet.append("Could not find resource for ");
                attributeSet.append(str2);
                attributeSet.append(": ");
                attributeSet.append(str);
                Log.w(str3, attributeSet.toString());
            }
            if (typedValue.string != null) {
                str = typedValue.string.toString();
            } else {
                context = String.valueOf(typedValue);
                z = new StringBuilder((String.valueOf(str2).length() + 28) + String.valueOf(context).length());
                z.append("Resource ");
                z.append(str2);
                z.append(" was not a string: ");
                z.append(context);
                Log.w(str3, z.toString());
            }
        }
        if (z2 && str == null) {
            attributeSet = new StringBuilder(String.valueOf(str2).length() + 33);
            attributeSet.append("Required XML attribute \"");
            attributeSet.append(str2);
            attributeSet.append("\" missing");
            Log.w(str3, attributeSet.toString());
        }
        return str;
    }
}
