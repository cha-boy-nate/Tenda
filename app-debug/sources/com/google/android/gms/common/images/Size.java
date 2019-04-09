package com.google.android.gms.common.images;

public final class Size {
    private final int zand;
    private final int zane;

    public Size(int i, int i2) {
        this.zand = i;
        this.zane = i2;
    }

    public final int getWidth() {
        return this.zand;
    }

    public final int getHeight() {
        return this.zane;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Size)) {
            return false;
        }
        Size size = (Size) obj;
        if (this.zand == size.zand && this.zane == size.zane) {
            return true;
        }
        return false;
    }

    public final String toString() {
        int i = this.zand;
        int i2 = this.zane;
        StringBuilder stringBuilder = new StringBuilder(23);
        stringBuilder.append(i);
        stringBuilder.append("x");
        stringBuilder.append(i2);
        return stringBuilder.toString();
    }

    private static NumberFormatException zah(String str) {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 16);
        stringBuilder.append("Invalid Size: \"");
        stringBuilder.append(str);
        stringBuilder.append("\"");
        throw new NumberFormatException(stringBuilder.toString());
    }

    public static Size parseSize(String str) throws NumberFormatException {
        if (str != null) {
            int indexOf = str.indexOf(42);
            if (indexOf < 0) {
                indexOf = str.indexOf(120);
            }
            if (indexOf >= 0) {
                try {
                    return new Size(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(indexOf + 1)));
                } catch (NumberFormatException e) {
                    throw zah(str);
                }
            }
            throw zah(str);
        }
        throw new IllegalArgumentException("string must not be null");
    }

    public final int hashCode() {
        int i = this.zane;
        int i2 = this.zand;
        return i ^ ((i2 >>> 16) | (i2 << 16));
    }
}
