package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.compat.C0014R;
import android.support.v4.provider.FontRequest;
import android.util.Base64;
import android.util.TypedValue;
import android.util.Xml;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
public class FontResourcesParserCompat {
    private static final int DEFAULT_TIMEOUT_MILLIS = 500;
    public static final int FETCH_STRATEGY_ASYNC = 1;
    public static final int FETCH_STRATEGY_BLOCKING = 0;
    public static final int INFINITE_TIMEOUT_VALUE = -1;
    private static final int ITALIC = 1;
    private static final int NORMAL_WEIGHT = 400;

    public interface FamilyResourceEntry {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FetchStrategy {
    }

    public static final class FontFileResourceEntry {
        @NonNull
        private final String mFileName;
        private boolean mItalic;
        private int mResourceId;
        private int mTtcIndex;
        private String mVariationSettings;
        private int mWeight;

        public FontFileResourceEntry(@NonNull String fileName, int weight, boolean italic, @Nullable String variationSettings, int ttcIndex, int resourceId) {
            this.mFileName = fileName;
            this.mWeight = weight;
            this.mItalic = italic;
            this.mVariationSettings = variationSettings;
            this.mTtcIndex = ttcIndex;
            this.mResourceId = resourceId;
        }

        @NonNull
        public String getFileName() {
            return this.mFileName;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        @Nullable
        public String getVariationSettings() {
            return this.mVariationSettings;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public int getResourceId() {
            return this.mResourceId;
        }
    }

    public static final class FontFamilyFilesResourceEntry implements FamilyResourceEntry {
        @NonNull
        private final FontFileResourceEntry[] mEntries;

        public FontFamilyFilesResourceEntry(@NonNull FontFileResourceEntry[] entries) {
            this.mEntries = entries;
        }

        @NonNull
        public FontFileResourceEntry[] getEntries() {
            return this.mEntries;
        }
    }

    public static final class ProviderResourceEntry implements FamilyResourceEntry {
        @NonNull
        private final FontRequest mRequest;
        private final int mStrategy;
        private final int mTimeoutMs;

        public ProviderResourceEntry(@NonNull FontRequest request, int strategy, int timeoutMs) {
            this.mRequest = request;
            this.mStrategy = strategy;
            this.mTimeoutMs = timeoutMs;
        }

        @NonNull
        public FontRequest getRequest() {
            return this.mRequest;
        }

        public int getFetchStrategy() {
            return this.mStrategy;
        }

        public int getTimeout() {
            return this.mTimeoutMs;
        }
    }

    @android.support.annotation.Nullable
    public static android.support.v4.content.res.FontResourcesParserCompat.FamilyResourceEntry parse(org.xmlpull.v1.XmlPullParser r3, android.content.res.Resources r4) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:10:0x001b in {4, 7, 9} preds:[]
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
    L_0x0000:
        r0 = r3.next();
        r1 = r0;
        r2 = 2;
        if (r0 == r2) goto L_0x000c;
    L_0x0008:
        r0 = 1;
        if (r1 == r0) goto L_0x000c;
    L_0x000b:
        goto L_0x0000;
    L_0x000c:
        if (r1 != r2) goto L_0x0013;
    L_0x000e:
        r0 = readFamilies(r3, r4);
        return r0;
    L_0x0013:
        r0 = new org.xmlpull.v1.XmlPullParserException;
        r2 = "No start tag found";
        r0.<init>(r2);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.FontResourcesParserCompat.parse(org.xmlpull.v1.XmlPullParser, android.content.res.Resources):android.support.v4.content.res.FontResourcesParserCompat$FamilyResourceEntry");
    }

    public static java.util.List<java.util.List<byte[]>> readCerts(android.content.res.Resources r6, @android.support.annotation.ArrayRes int r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:29:0x006b in {2, 9, 18, 19, 20, 21, 23, 25, 28} preds:[]
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
        if (r7 != 0) goto L_0x0007;
    L_0x0002:
        r0 = java.util.Collections.emptyList();
        return r0;
    L_0x0007:
        r0 = r6.obtainTypedArray(r7);
        r1 = r0.length();	 Catch:{ all -> 0x0065 }
        if (r1 != 0) goto L_0x0019;	 Catch:{ all -> 0x0065 }
    L_0x0011:
        r1 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0065 }
        r0.recycle();
        return r1;
    L_0x0019:
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x0065 }
        r1.<init>();	 Catch:{ all -> 0x0065 }
        r2 = 0;	 Catch:{ all -> 0x0065 }
        r3 = getType(r0, r2);	 Catch:{ all -> 0x0065 }
        r4 = 1;	 Catch:{ all -> 0x0065 }
        if (r3 != r4) goto L_0x0050;	 Catch:{ all -> 0x0065 }
        r3 = r2;	 Catch:{ all -> 0x0065 }
        r4 = r0.length();	 Catch:{ all -> 0x0065 }
        if (r3 >= r4) goto L_0x004e;	 Catch:{ all -> 0x0065 }
        r4 = r0.getResourceId(r3, r2);	 Catch:{ all -> 0x0065 }
        if (r4 == 0) goto L_0x0049;	 Catch:{ all -> 0x0065 }
        r5 = r6.getStringArray(r4);	 Catch:{ all -> 0x0065 }
        r5 = toByteArrayList(r5);	 Catch:{ all -> 0x0065 }
        r1.add(r5);	 Catch:{ all -> 0x0065 }
        goto L_0x004a;	 Catch:{ all -> 0x0065 }
        r3 = r3 + 1;	 Catch:{ all -> 0x0065 }
        goto L_0x0028;	 Catch:{ all -> 0x0065 }
        goto L_0x005f;	 Catch:{ all -> 0x0065 }
        r2 = r6.getStringArray(r7);	 Catch:{ all -> 0x0065 }
        r2 = toByteArrayList(r2);	 Catch:{ all -> 0x0065 }
        r1.add(r2);	 Catch:{ all -> 0x0065 }
        r0.recycle();
        return r1;
    L_0x0065:
        r1 = move-exception;
        r0.recycle();
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.FontResourcesParserCompat.readCerts(android.content.res.Resources, int):java.util.List<java.util.List<byte[]>>");
    }

    @Nullable
    private static FamilyResourceEntry readFamilies(XmlPullParser parser, Resources resources) throws XmlPullParserException, IOException {
        parser.require(2, null, "font-family");
        if (parser.getName().equals("font-family")) {
            return readFamily(parser, resources);
        }
        skip(parser);
        return null;
    }

    @Nullable
    private static FamilyResourceEntry readFamily(XmlPullParser parser, Resources resources) throws XmlPullParserException, IOException {
        TypedArray array = resources.obtainAttributes(Xml.asAttributeSet(parser), C0014R.styleable.FontFamily);
        String authority = array.getString(C0014R.styleable.FontFamily_fontProviderAuthority);
        String providerPackage = array.getString(C0014R.styleable.FontFamily_fontProviderPackage);
        String query = array.getString(C0014R.styleable.FontFamily_fontProviderQuery);
        int certsId = array.getResourceId(C0014R.styleable.FontFamily_fontProviderCerts, 0);
        int strategy = array.getInteger(C0014R.styleable.FontFamily_fontProviderFetchStrategy, 1);
        int timeoutMs = array.getInteger(C0014R.styleable.FontFamily_fontProviderFetchTimeout, DEFAULT_TIMEOUT_MILLIS);
        array.recycle();
        if (authority == null || providerPackage == null || query == null) {
            List<FontFileResourceEntry> fonts = new ArrayList();
            while (parser.next() != 3) {
                if (parser.getEventType() == 2) {
                    if (parser.getName().equals("font")) {
                        fonts.add(readFont(parser, resources));
                    } else {
                        skip(parser);
                    }
                }
            }
            if (fonts.isEmpty()) {
                return null;
            }
            return new FontFamilyFilesResourceEntry((FontFileResourceEntry[]) fonts.toArray(new FontFileResourceEntry[fonts.size()]));
        }
        while (parser.next() != 3) {
            skip(parser);
        }
        return new ProviderResourceEntry(new FontRequest(authority, providerPackage, query, readCerts(resources, certsId)), strategy, timeoutMs);
    }

    private static int getType(TypedArray typedArray, int index) {
        if (VERSION.SDK_INT >= 21) {
            return typedArray.getType(index);
        }
        TypedValue tv = new TypedValue();
        typedArray.getValue(index, tv);
        return tv.type;
    }

    private static List<byte[]> toByteArrayList(String[] stringArray) {
        List<byte[]> result = new ArrayList();
        for (String item : stringArray) {
            result.add(Base64.decode(item, 0));
        }
        return result;
    }

    private static FontFileResourceEntry readFont(XmlPullParser parser, Resources resources) throws XmlPullParserException, IOException {
        TypedArray array = resources.obtainAttributes(Xml.asAttributeSet(parser), C0014R.styleable.FontFamilyFont);
        int weight = array.getInt(array.hasValue(C0014R.styleable.FontFamilyFont_fontWeight) ? C0014R.styleable.FontFamilyFont_fontWeight : C0014R.styleable.FontFamilyFont_android_fontWeight, NORMAL_WEIGHT);
        boolean isItalic = 1 == array.getInt(array.hasValue(C0014R.styleable.FontFamilyFont_fontStyle) ? C0014R.styleable.FontFamilyFont_fontStyle : C0014R.styleable.FontFamilyFont_android_fontStyle, 0);
        int ttcIndexAttr = array.hasValue(C0014R.styleable.FontFamilyFont_ttcIndex) ? C0014R.styleable.FontFamilyFont_ttcIndex : C0014R.styleable.FontFamilyFont_android_ttcIndex;
        String variationSettings = array.getString(array.hasValue(C0014R.styleable.FontFamilyFont_fontVariationSettings) ? C0014R.styleable.FontFamilyFont_fontVariationSettings : C0014R.styleable.FontFamilyFont_android_fontVariationSettings);
        int ttcIndex = array.getInt(ttcIndexAttr, 0);
        int resourceAttr = array.hasValue(C0014R.styleable.FontFamilyFont_font) ? C0014R.styleable.FontFamilyFont_font : C0014R.styleable.FontFamilyFont_android_font;
        int resourceId = array.getResourceId(resourceAttr, 0);
        String filename = array.getString(resourceAttr);
        array.recycle();
        while (parser.next() != 3) {
            skip(parser);
        }
        return new FontFileResourceEntry(filename, weight, isItalic, variationSettings, ttcIndex, resourceId);
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        int depth = 1;
        while (depth > 0) {
            switch (parser.next()) {
                case 2:
                    depth++;
                    break;
                case 3:
                    depth--;
                    break;
                default:
                    break;
            }
        }
    }

    private FontResourcesParserCompat() {
    }
}
