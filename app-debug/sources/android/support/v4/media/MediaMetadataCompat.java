package android.support.v4.media;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public final class MediaMetadataCompat implements Parcelable {
    public static final Creator<MediaMetadataCompat> CREATOR = new C01071();
    static final ArrayMap<String, Integer> METADATA_KEYS_TYPE = new ArrayMap();
    public static final String METADATA_KEY_ADVERTISEMENT = "android.media.metadata.ADVERTISEMENT";
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DOWNLOAD_STATUS = "android.media.metadata.DOWNLOAD_STATUS";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
    static final int METADATA_TYPE_BITMAP = 2;
    static final int METADATA_TYPE_LONG = 0;
    static final int METADATA_TYPE_RATING = 3;
    static final int METADATA_TYPE_TEXT = 1;
    private static final String[] PREFERRED_BITMAP_ORDER = new String[]{METADATA_KEY_DISPLAY_ICON, METADATA_KEY_ART, METADATA_KEY_ALBUM_ART};
    private static final String[] PREFERRED_DESCRIPTION_ORDER = new String[]{METADATA_KEY_TITLE, METADATA_KEY_ARTIST, METADATA_KEY_ALBUM, METADATA_KEY_ALBUM_ARTIST, METADATA_KEY_WRITER, METADATA_KEY_AUTHOR, METADATA_KEY_COMPOSER};
    private static final String[] PREFERRED_URI_ORDER = new String[]{METADATA_KEY_DISPLAY_ICON_URI, METADATA_KEY_ART_URI, METADATA_KEY_ALBUM_ART_URI};
    private static final String TAG = "MediaMetadata";
    final Bundle mBundle;
    private MediaDescriptionCompat mDescription;
    private Object mMetadataObj;

    /* renamed from: android.support.v4.media.MediaMetadataCompat$1 */
    static class C01071 implements Creator<MediaMetadataCompat> {
        C01071() {
        }

        public MediaMetadataCompat createFromParcel(Parcel in) {
            return new MediaMetadataCompat(in);
        }

        public MediaMetadataCompat[] newArray(int size) {
            return new MediaMetadataCompat[size];
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BitmapKey {
    }

    public static final class Builder {
        private final Bundle mBundle;

        public Builder() {
            this.mBundle = new Bundle();
        }

        public Builder(MediaMetadataCompat source) {
            this.mBundle = new Bundle(source.mBundle);
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public Builder(MediaMetadataCompat source, int maxBitmapSize) {
            this(source);
            for (String key : this.mBundle.keySet()) {
                Bitmap value = this.mBundle.get(key);
                if (value instanceof Bitmap) {
                    Bitmap bmp = value;
                    if (bmp.getHeight() > maxBitmapSize || bmp.getWidth() > maxBitmapSize) {
                        putBitmap(key, scaleBitmap(bmp, maxBitmapSize));
                    }
                }
            }
        }

        public Builder putText(String key, CharSequence value) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (((Integer) MediaMetadataCompat.METADATA_KEYS_TYPE.get(key)).intValue() != 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The ");
                    stringBuilder.append(key);
                    stringBuilder.append(" key cannot be used to put a CharSequence");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mBundle.putCharSequence(key, value);
            return this;
        }

        public Builder putString(String key, String value) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (((Integer) MediaMetadataCompat.METADATA_KEYS_TYPE.get(key)).intValue() != 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The ");
                    stringBuilder.append(key);
                    stringBuilder.append(" key cannot be used to put a String");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mBundle.putCharSequence(key, value);
            return this;
        }

        public Builder putLong(String key, long value) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (((Integer) MediaMetadataCompat.METADATA_KEYS_TYPE.get(key)).intValue() != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The ");
                    stringBuilder.append(key);
                    stringBuilder.append(" key cannot be used to put a long");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mBundle.putLong(key, value);
            return this;
        }

        public Builder putRating(String key, RatingCompat value) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (((Integer) MediaMetadataCompat.METADATA_KEYS_TYPE.get(key)).intValue() != 3) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The ");
                    stringBuilder.append(key);
                    stringBuilder.append(" key cannot be used to put a Rating");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            if (VERSION.SDK_INT >= 19) {
                this.mBundle.putParcelable(key, (Parcelable) value.getRating());
            } else {
                this.mBundle.putParcelable(key, value);
            }
            return this;
        }

        public Builder putBitmap(String key, Bitmap value) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (((Integer) MediaMetadataCompat.METADATA_KEYS_TYPE.get(key)).intValue() != 2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The ");
                    stringBuilder.append(key);
                    stringBuilder.append(" key cannot be used to put a Bitmap");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mBundle.putParcelable(key, value);
            return this;
        }

        public MediaMetadataCompat build() {
            return new MediaMetadataCompat(this.mBundle);
        }

        private Bitmap scaleBitmap(Bitmap bmp, int maxSize) {
            float maxSizeF = (float) maxSize;
            float scale = Math.min(maxSizeF / ((float) bmp.getWidth()), maxSizeF / ((float) bmp.getHeight()));
            return Bitmap.createScaledBitmap(bmp, (int) (((float) bmp.getWidth()) * scale), (int) (((float) bmp.getHeight()) * scale), true);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LongKey {
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RatingKey {
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextKey {
    }

    static {
        METADATA_KEYS_TYPE.put(METADATA_KEY_TITLE, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ARTIST, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DURATION, Integer.valueOf(0));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_AUTHOR, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_WRITER, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMPOSER, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMPILATION, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DATE, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_YEAR, Integer.valueOf(0));
        METADATA_KEYS_TYPE.put(METADATA_KEY_GENRE, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_TRACK_NUMBER, Integer.valueOf(0));
        METADATA_KEYS_TYPE.put(METADATA_KEY_NUM_TRACKS, Integer.valueOf(0));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISC_NUMBER, Integer.valueOf(0));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM_ARTIST, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ART, Integer.valueOf(2));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ART_URI, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM_ART, Integer.valueOf(2));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM_ART_URI, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_USER_RATING, Integer.valueOf(3));
        METADATA_KEYS_TYPE.put(METADATA_KEY_RATING, Integer.valueOf(3));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_TITLE, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_SUBTITLE, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_DESCRIPTION, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_ICON, Integer.valueOf(2));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_ICON_URI, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_MEDIA_ID, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_BT_FOLDER_TYPE, Integer.valueOf(0));
        METADATA_KEYS_TYPE.put(METADATA_KEY_MEDIA_URI, Integer.valueOf(1));
        METADATA_KEYS_TYPE.put(METADATA_KEY_ADVERTISEMENT, Integer.valueOf(0));
        METADATA_KEYS_TYPE.put(METADATA_KEY_DOWNLOAD_STATUS, Integer.valueOf(0));
    }

    MediaMetadataCompat(Bundle bundle) {
        this.mBundle = new Bundle(bundle);
    }

    MediaMetadataCompat(Parcel in) {
        this.mBundle = in.readBundle();
    }

    public boolean containsKey(String key) {
        return this.mBundle.containsKey(key);
    }

    public CharSequence getText(String key) {
        return this.mBundle.getCharSequence(key);
    }

    public String getString(String key) {
        CharSequence text = this.mBundle.getCharSequence(key);
        if (text != null) {
            return text.toString();
        }
        return null;
    }

    public long getLong(String key) {
        return this.mBundle.getLong(key, 0);
    }

    public RatingCompat getRating(String key) {
        RatingCompat rating = null;
        try {
            if (VERSION.SDK_INT >= 19) {
                rating = RatingCompat.fromRating(this.mBundle.getParcelable(key));
            } else {
                rating = (RatingCompat) this.mBundle.getParcelable(key);
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed to retrieve a key as Rating.", e);
        }
        return rating;
    }

    public Bitmap getBitmap(String key) {
        try {
            return (Bitmap) this.mBundle.getParcelable(key);
        } catch (Exception e) {
            Log.w(TAG, "Failed to retrieve a key as Bitmap.", e);
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.support.v4.media.MediaDescriptionCompat getDescription() {
        /*
        r13 = this;
        r0 = r13.mDescription;
        if (r0 == 0) goto L_0x0005;
    L_0x0004:
        return r0;
    L_0x0005:
        r0 = "android.media.metadata.MEDIA_ID";
        r0 = r13.getString(r0);
        r1 = 3;
        r1 = new java.lang.CharSequence[r1];
        r2 = 0;
        r3 = 0;
        r4 = "android.media.metadata.DISPLAY_TITLE";
        r4 = r13.getText(r4);
        r5 = android.text.TextUtils.isEmpty(r4);
        r6 = 2;
        r7 = 0;
        r8 = 1;
        if (r5 != 0) goto L_0x0032;
    L_0x001f:
        r1[r7] = r4;
        r5 = "android.media.metadata.DISPLAY_SUBTITLE";
        r5 = r13.getText(r5);
        r1[r8] = r5;
        r5 = "android.media.metadata.DISPLAY_DESCRIPTION";
        r5 = r13.getText(r5);
        r1[r6] = r5;
        goto L_0x0051;
    L_0x0032:
        r5 = 0;
        r9 = 0;
    L_0x0034:
        r10 = r1.length;
        if (r5 >= r10) goto L_0x0051;
    L_0x0037:
        r10 = PREFERRED_DESCRIPTION_ORDER;
        r11 = r10.length;
        if (r9 >= r11) goto L_0x0051;
    L_0x003c:
        r11 = r9 + 1;
        r9 = r10[r9];
        r9 = r13.getText(r9);
        r10 = android.text.TextUtils.isEmpty(r9);
        if (r10 != 0) goto L_0x004f;
    L_0x004a:
        r10 = r5 + 1;
        r1[r5] = r9;
        r5 = r10;
    L_0x004f:
        r9 = r11;
        goto L_0x0034;
    L_0x0051:
        r5 = 0;
    L_0x0052:
        r9 = PREFERRED_BITMAP_ORDER;
        r10 = r9.length;
        if (r5 >= r10) goto L_0x0064;
    L_0x0057:
        r9 = r9[r5];
        r9 = r13.getBitmap(r9);
        if (r9 == 0) goto L_0x0061;
    L_0x005f:
        r2 = r9;
        goto L_0x0064;
    L_0x0061:
        r5 = r5 + 1;
        goto L_0x0052;
    L_0x0064:
        r5 = 0;
    L_0x0065:
        r9 = PREFERRED_URI_ORDER;
        r10 = r9.length;
        if (r5 >= r10) goto L_0x007e;
    L_0x006a:
        r9 = r9[r5];
        r9 = r13.getString(r9);
        r10 = android.text.TextUtils.isEmpty(r9);
        if (r10 != 0) goto L_0x007b;
    L_0x0076:
        r3 = android.net.Uri.parse(r9);
        goto L_0x007e;
    L_0x007b:
        r5 = r5 + 1;
        goto L_0x0065;
    L_0x007e:
        r5 = 0;
        r9 = "android.media.metadata.MEDIA_URI";
        r9 = r13.getString(r9);
        r10 = android.text.TextUtils.isEmpty(r9);
        if (r10 != 0) goto L_0x008f;
    L_0x008b:
        r5 = android.net.Uri.parse(r9);
    L_0x008f:
        r10 = new android.support.v4.media.MediaDescriptionCompat$Builder;
        r10.<init>();
        r10.setMediaId(r0);
        r7 = r1[r7];
        r10.setTitle(r7);
        r7 = r1[r8];
        r10.setSubtitle(r7);
        r6 = r1[r6];
        r10.setDescription(r6);
        r10.setIconBitmap(r2);
        r10.setIconUri(r3);
        r10.setMediaUri(r5);
        r6 = new android.os.Bundle;
        r6.<init>();
        r7 = r13.mBundle;
        r8 = "android.media.metadata.BT_FOLDER_TYPE";
        r7 = r7.containsKey(r8);
        if (r7 == 0) goto L_0x00c9;
    L_0x00be:
        r7 = "android.media.extra.BT_FOLDER_TYPE";
        r8 = "android.media.metadata.BT_FOLDER_TYPE";
        r11 = r13.getLong(r8);
        r6.putLong(r7, r11);
    L_0x00c9:
        r7 = r13.mBundle;
        r8 = "android.media.metadata.DOWNLOAD_STATUS";
        r7 = r7.containsKey(r8);
        if (r7 == 0) goto L_0x00de;
    L_0x00d3:
        r7 = "android.media.extra.DOWNLOAD_STATUS";
        r8 = "android.media.metadata.DOWNLOAD_STATUS";
        r11 = r13.getLong(r8);
        r6.putLong(r7, r11);
    L_0x00de:
        r7 = r6.isEmpty();
        if (r7 != 0) goto L_0x00e7;
    L_0x00e4:
        r10.setExtras(r6);
    L_0x00e7:
        r7 = r10.build();
        r13.mDescription = r7;
        r7 = r13.mDescription;
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.media.MediaMetadataCompat.getDescription():android.support.v4.media.MediaDescriptionCompat");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mBundle);
    }

    public int size() {
        return this.mBundle.size();
    }

    public Set<String> keySet() {
        return this.mBundle.keySet();
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public static MediaMetadataCompat fromMediaMetadata(Object metadataObj) {
        if (metadataObj == null || VERSION.SDK_INT < 21) {
            return null;
        }
        Parcel p = Parcel.obtain();
        MediaMetadataCompatApi21.writeToParcel(metadataObj, p, 0);
        p.setDataPosition(0);
        MediaMetadataCompat metadata = (MediaMetadataCompat) CREATOR.createFromParcel(p);
        p.recycle();
        metadata.mMetadataObj = metadataObj;
        return metadata;
    }

    public Object getMediaMetadata() {
        if (this.mMetadataObj == null && VERSION.SDK_INT >= 21) {
            Parcel p = Parcel.obtain();
            writeToParcel(p, 0);
            p.setDataPosition(0);
            this.mMetadataObj = MediaMetadataCompatApi21.createFromParcel(p);
            p.recycle();
        }
        return this.mMetadataObj;
    }
}
