package com.google.android.gms.common.internal;

import android.support.annotation.Nullable;
import com.google.android.gms.common.annotation.KeepForSdk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@KeepForSdk
public final class Objects {

    @KeepForSdk
    public static final class ToStringHelper {
        private final List<String> zzer;
        private final Object zzes;

        private ToStringHelper(Object obj) {
            this.zzes = Preconditions.checkNotNull(obj);
            this.zzer = new ArrayList();
        }

        @KeepForSdk
        public final ToStringHelper add(String str, @Nullable Object obj) {
            List list = this.zzer;
            str = (String) Preconditions.checkNotNull(str);
            obj = String.valueOf(obj);
            StringBuilder stringBuilder = new StringBuilder((String.valueOf(str).length() + 1) + String.valueOf(obj).length());
            stringBuilder.append(str);
            stringBuilder.append("=");
            stringBuilder.append(obj);
            list.add(stringBuilder.toString());
            return this;
        }

        @KeepForSdk
        public final String toString() {
            StringBuilder stringBuilder = new StringBuilder(100);
            stringBuilder.append(this.zzes.getClass().getSimpleName());
            stringBuilder.append('{');
            int size = this.zzer.size();
            for (int i = 0; i < size; i++) {
                stringBuilder.append((String) this.zzer.get(i));
                if (i < size - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    @KeepForSdk
    public static boolean equal(@Nullable Object obj, @Nullable Object obj2) {
        if (obj != obj2) {
            if (obj == null || obj.equals(obj2) == null) {
                return null;
            }
        }
        return true;
    }

    @KeepForSdk
    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    @KeepForSdk
    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(obj);
    }

    private Objects() {
        throw new AssertionError("Uninstantiable");
    }
}
