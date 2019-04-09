package android.support.v4.os;

import android.os.Build.VERSION;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

@RestrictTo({Scope.LIBRARY_GROUP})
final class LocaleListHelper {
    private static final Locale EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
    private static final Locale LOCALE_AR_XB = new Locale("ar", "XB");
    private static final Locale LOCALE_EN_XA = new Locale("en", "XA");
    private static final int NUM_PSEUDO_LOCALES = 2;
    private static final String STRING_AR_XB = "ar-XB";
    private static final String STRING_EN_XA = "en-XA";
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultAdjustedLocaleList = null;
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultLocaleList = null;
    private static final Locale[] sEmptyList = new Locale[0];
    private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
    @GuardedBy("sLock")
    private static Locale sLastDefaultLocale = null;
    @GuardedBy("sLock")
    private static LocaleListHelper sLastExplicitlySetLocaleList = null;
    private static final Object sLock = new Object();
    private final Locale[] mList;
    @NonNull
    private final String mStringRepresentation;

    @android.support.annotation.RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    LocaleListHelper(@android.support.annotation.NonNull java.util.Locale r8, android.support.v4.os.LocaleListHelper r9) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:37:0x009c in {4, 5, 10, 11, 14, 15, 20, 23, 26, 31, 32, 34, 36} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r7 = this;
        r7.<init>();
        if (r8 == 0) goto L_0x0094;
    L_0x0005:
        r0 = 0;
        if (r9 != 0) goto L_0x000a;
    L_0x0008:
        r1 = 0;
        goto L_0x000d;
    L_0x000a:
        r1 = r9.mList;
        r1 = r1.length;
    L_0x000d:
        r2 = -1;
        r3 = 0;
    L_0x000f:
        if (r3 >= r1) goto L_0x0020;
    L_0x0011:
        r4 = r9.mList;
        r4 = r4[r3];
        r4 = r8.equals(r4);
        if (r4 == 0) goto L_0x001d;
    L_0x001b:
        r2 = r3;
        goto L_0x0020;
    L_0x001d:
        r3 = r3 + 1;
        goto L_0x000f;
    L_0x0020:
        r3 = -1;
        if (r2 != r3) goto L_0x0025;
    L_0x0023:
        r4 = 1;
        goto L_0x0026;
    L_0x0025:
        r4 = 0;
    L_0x0026:
        r4 = r4 + r1;
        r5 = new java.util.Locale[r4];
        r6 = r8.clone();
        r6 = (java.util.Locale) r6;
        r5[r0] = r6;
        if (r2 != r3) goto L_0x0047;
    L_0x0033:
        r0 = 0;
    L_0x0034:
        if (r0 >= r1) goto L_0x006e;
    L_0x0036:
        r3 = r0 + 1;
        r6 = r9.mList;
        r6 = r6[r0];
        r6 = r6.clone();
        r6 = (java.util.Locale) r6;
        r5[r3] = r6;
        r0 = r0 + 1;
        goto L_0x0034;
    L_0x0047:
        r0 = 0;
    L_0x0048:
        if (r0 >= r2) goto L_0x005b;
    L_0x004a:
        r3 = r0 + 1;
        r6 = r9.mList;
        r6 = r6[r0];
        r6 = r6.clone();
        r6 = (java.util.Locale) r6;
        r5[r3] = r6;
        r0 = r0 + 1;
        goto L_0x0048;
    L_0x005b:
        r0 = r2 + 1;
    L_0x005d:
        if (r0 >= r1) goto L_0x006e;
    L_0x005f:
        r3 = r9.mList;
        r3 = r3[r0];
        r3 = r3.clone();
        r3 = (java.util.Locale) r3;
        r5[r0] = r3;
        r0 = r0 + 1;
        goto L_0x005d;
    L_0x006e:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = 0;
    L_0x0074:
        if (r3 >= r4) goto L_0x008b;
    L_0x0076:
        r6 = r5[r3];
        r6 = android.support.v4.os.LocaleHelper.toLanguageTag(r6);
        r0.append(r6);
        r6 = r4 + -1;
        if (r3 >= r6) goto L_0x0088;
    L_0x0083:
        r6 = 44;
        r0.append(r6);
    L_0x0088:
        r3 = r3 + 1;
        goto L_0x0074;
    L_0x008b:
        r7.mList = r5;
        r3 = r0.toString();
        r7.mStringRepresentation = r3;
        return;
    L_0x0094:
        r0 = new java.lang.NullPointerException;
        r1 = "topLocale is null";
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.LocaleListHelper.<init>(java.util.Locale, android.support.v4.os.LocaleListHelper):void");
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    Locale get(int index) {
        if (index >= 0) {
            Locale[] localeArr = this.mList;
            if (index < localeArr.length) {
                return localeArr[index];
            }
        }
        return null;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    boolean isEmpty() {
        return this.mList.length == 0;
    }

    @IntRange(from = 0)
    @RestrictTo({Scope.LIBRARY_GROUP})
    int size() {
        return this.mList.length;
    }

    @IntRange(from = -1)
    @RestrictTo({Scope.LIBRARY_GROUP})
    int indexOf(Locale locale) {
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return -1;
            }
            if (localeArr[i].equals(locale)) {
                return i;
            }
            i++;
        }
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LocaleListHelper)) {
            return false;
        }
        Locale[] otherList = ((LocaleListHelper) other).mList;
        if (this.mList.length != otherList.length) {
            return false;
        }
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return true;
            }
            if (!localeArr[i].equals(otherList[i])) {
                return false;
            }
            i++;
        }
    }

    public int hashCode() {
        int result = 1;
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return result;
            }
            result = (result * 31) + localeArr[i].hashCode();
            i++;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i < localeArr.length) {
                sb.append(localeArr[i]);
                if (i < this.mList.length - 1) {
                    sb.append(',');
                }
                i++;
            } else {
                sb.append("]");
                return sb.toString();
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    String toLanguageTags() {
        return this.mStringRepresentation;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    LocaleListHelper(@NonNull Locale... list) {
        if (list.length == 0) {
            this.mList = sEmptyList;
            this.mStringRepresentation = "";
            return;
        }
        Locale[] localeList = new Locale[list.length];
        HashSet<Locale> seenLocales = new HashSet();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < list.length) {
            Locale l = list[i];
            StringBuilder stringBuilder;
            if (l == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("list[");
                stringBuilder.append(i);
                stringBuilder.append("] is null");
                throw new NullPointerException(stringBuilder.toString());
            } else if (seenLocales.contains(l)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("list[");
                stringBuilder.append(i);
                stringBuilder.append("] is a repetition");
                throw new IllegalArgumentException(stringBuilder.toString());
            } else {
                Locale localeClone = (Locale) l.clone();
                localeList[i] = localeClone;
                sb.append(LocaleHelper.toLanguageTag(localeClone));
                if (i < list.length - 1) {
                    sb.append(',');
                }
                seenLocales.add(localeClone);
                i++;
            }
        }
        this.mList = localeList;
        this.mStringRepresentation = sb.toString();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    static LocaleListHelper getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    static LocaleListHelper forLanguageTags(@Nullable String list) {
        if (list != null) {
            if (!list.isEmpty()) {
                String[] tags = list.split(",", -1);
                Locale[] localeArray = new Locale[tags.length];
                for (int i = 0; i < localeArray.length; i++) {
                    localeArray[i] = LocaleHelper.forLanguageTag(tags[i]);
                }
                return new LocaleListHelper(localeArray);
            }
        }
        return getEmptyLocaleList();
    }

    private static String getLikelyScript(Locale locale) {
        if (VERSION.SDK_INT < 21) {
            return "";
        }
        String script = locale.getScript();
        if (script.isEmpty()) {
            return "";
        }
        return script;
    }

    private static boolean isPseudoLocale(String locale) {
        if (!STRING_EN_XA.equals(locale)) {
            if (!STRING_AR_XB.equals(locale)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPseudoLocale(Locale locale) {
        if (!LOCALE_EN_XA.equals(locale)) {
            if (!LOCALE_AR_XB.equals(locale)) {
                return false;
            }
        }
        return true;
    }

    @IntRange(from = 0, to = 1)
    private static int matchScore(Locale supported, Locale desired) {
        int i = 1;
        if (supported.equals(desired)) {
            return 1;
        }
        if (supported.getLanguage().equals(desired.getLanguage()) && !isPseudoLocale(supported)) {
            if (!isPseudoLocale(desired)) {
                String supportedScr = getLikelyScript(supported);
                if (!supportedScr.isEmpty()) {
                    return supportedScr.equals(getLikelyScript(desired));
                }
                String supportedRegion = supported.getCountry();
                if (!supportedRegion.isEmpty()) {
                    if (!supportedRegion.equals(desired.getCountry())) {
                        i = 0;
                    }
                }
                return i;
            }
        }
        return 0;
    }

    private int findFirstMatchIndex(Locale supportedLocale) {
        int idx = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (idx >= localeArr.length) {
                return Integer.MAX_VALUE;
            }
            if (matchScore(supportedLocale, localeArr[idx]) > 0) {
                return idx;
            }
            idx++;
        }
    }

    private int computeFirstMatchIndex(Collection<String> supportedLocales, boolean assumeEnglishIsSupported) {
        Locale[] localeArr = this.mList;
        if (localeArr.length == 1) {
            return 0;
        }
        if (localeArr.length == 0) {
            return -1;
        }
        int bestIndex = Integer.MAX_VALUE;
        if (assumeEnglishIsSupported) {
            int idx = findFirstMatchIndex(EN_LATN);
            if (idx == 0) {
                return 0;
            }
            if (idx < Integer.MAX_VALUE) {
                bestIndex = idx;
            }
        }
        for (String languageTag : supportedLocales) {
            int idx2 = findFirstMatchIndex(LocaleHelper.forLanguageTag(languageTag));
            if (idx2 == 0) {
                return 0;
            }
            if (idx2 < bestIndex) {
                bestIndex = idx2;
            }
        }
        if (bestIndex == Integer.MAX_VALUE) {
            return 0;
        }
        return bestIndex;
    }

    private Locale computeFirstMatch(Collection<String> supportedLocales, boolean assumeEnglishIsSupported) {
        int bestIndex = computeFirstMatchIndex(supportedLocales, assumeEnglishIsSupported);
        return bestIndex == -1 ? null : this.mList[bestIndex];
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    Locale getFirstMatch(String[] supportedLocales) {
        return computeFirstMatch(Arrays.asList(supportedLocales), false);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getFirstMatchIndex(String[] supportedLocales) {
        return computeFirstMatchIndex(Arrays.asList(supportedLocales), false);
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    Locale getFirstMatchWithEnglishSupported(String[] supportedLocales) {
        return computeFirstMatch(Arrays.asList(supportedLocales), true);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getFirstMatchIndexWithEnglishSupported(Collection<String> supportedLocales) {
        return computeFirstMatchIndex(supportedLocales, true);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getFirstMatchIndexWithEnglishSupported(String[] supportedLocales) {
        return getFirstMatchIndexWithEnglishSupported(Arrays.asList(supportedLocales));
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static boolean isPseudoLocalesOnly(@Nullable String[] supportedLocales) {
        if (supportedLocales == null) {
            return true;
        }
        if (supportedLocales.length > 3) {
            return false;
        }
        for (String locale : supportedLocales) {
            if (!locale.isEmpty() && !isPseudoLocale(locale)) {
                return false;
            }
        }
        return true;
    }

    @Size(min = 1)
    @RestrictTo({Scope.LIBRARY_GROUP})
    @NonNull
    static LocaleListHelper getDefault() {
        Locale defaultLocale = Locale.getDefault();
        synchronized (sLock) {
            LocaleListHelper localeListHelper;
            if (!defaultLocale.equals(sLastDefaultLocale)) {
                sLastDefaultLocale = defaultLocale;
                if (sDefaultLocaleList == null || !defaultLocale.equals(sDefaultLocaleList.get(0))) {
                    sDefaultLocaleList = new LocaleListHelper(defaultLocale, sLastExplicitlySetLocaleList);
                    sDefaultAdjustedLocaleList = sDefaultLocaleList;
                } else {
                    localeListHelper = sDefaultLocaleList;
                    return localeListHelper;
                }
            }
            localeListHelper = sDefaultLocaleList;
            return localeListHelper;
        }
    }

    @Size(min = 1)
    @NonNull
    static LocaleListHelper getAdjustedDefault() {
        LocaleListHelper localeListHelper;
        getDefault();
        synchronized (sLock) {
            localeListHelper = sDefaultAdjustedLocaleList;
        }
        return localeListHelper;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static void setDefault(@Size(min = 1) @NonNull LocaleListHelper locales) {
        setDefault(locales, 0);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static void setDefault(@Size(min = 1) @NonNull LocaleListHelper locales, int localeIndex) {
        if (locales == null) {
            throw new NullPointerException("locales is null");
        } else if (locales.isEmpty()) {
            throw new IllegalArgumentException("locales is empty");
        } else {
            synchronized (sLock) {
                sLastDefaultLocale = locales.get(localeIndex);
                Locale.setDefault(sLastDefaultLocale);
                sLastExplicitlySetLocaleList = locales;
                sDefaultLocaleList = locales;
                if (localeIndex == 0) {
                    sDefaultAdjustedLocaleList = sDefaultLocaleList;
                } else {
                    sDefaultAdjustedLocaleList = new LocaleListHelper(sLastDefaultLocale, sDefaultLocaleList);
                }
            }
        }
    }
}
