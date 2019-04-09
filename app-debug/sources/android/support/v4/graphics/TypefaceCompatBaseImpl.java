package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import android.support.v4.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import android.support.v4.provider.FontsContractCompat.FontInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestrictTo({Scope.LIBRARY_GROUP})
class TypefaceCompatBaseImpl {
    private static final String CACHE_FILE_PREFIX = "cached_font_";
    private static final String TAG = "TypefaceCompatBaseImpl";

    private interface StyleExtractor<T> {
        int getWeight(T t);

        boolean isItalic(T t);
    }

    /* renamed from: android.support.v4.graphics.TypefaceCompatBaseImpl$1 */
    class C02501 implements StyleExtractor<FontInfo> {
        C02501() {
        }

        public int getWeight(FontInfo info) {
            return info.getWeight();
        }

        public boolean isItalic(FontInfo info) {
            return info.isItalic();
        }
    }

    /* renamed from: android.support.v4.graphics.TypefaceCompatBaseImpl$2 */
    class C02512 implements StyleExtractor<FontFileResourceEntry> {
        C02512() {
        }

        public int getWeight(FontFileResourceEntry entry) {
            return entry.getWeight();
        }

        public boolean isItalic(FontFileResourceEntry entry) {
            return entry.isItalic();
        }
    }

    TypefaceCompatBaseImpl() {
    }

    private static <T> T findBestFont(T[] fonts, int style, StyleExtractor<T> extractor) {
        int targetWeight = (style & 1) == 0 ? 400 : 700;
        boolean isTargetItalic = (style & 2) != 0;
        int bestScore = Integer.MAX_VALUE;
        T best = null;
        for (T font : fonts) {
            int score = (Math.abs(extractor.getWeight(font) - targetWeight) * 2) + (extractor.isItalic(font) == isTargetItalic ? 0 : 1);
            if (best == null || bestScore > score) {
                best = font;
                bestScore = score;
            }
        }
        return best;
    }

    protected FontInfo findBestInfo(FontInfo[] fonts, int style) {
        return (FontInfo) findBestFont(fonts, style, new C02501());
    }

    protected Typeface createFromInputStream(Context context, InputStream is) {
        File tmpFile = TypefaceCompatUtil.getTempFile(context);
        Typeface typeface = null;
        if (tmpFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tmpFile, is)) {
                return typeface;
            }
            typeface = Typeface.createFromFile(tmpFile.getPath());
            tmpFile.delete();
            return typeface;
        } catch (RuntimeException e) {
            return typeface;
        } finally {
            tmpFile.delete();
        }
    }

    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] fonts, int style) {
        Typeface typeface = null;
        if (fonts.length < 1) {
            return null;
        }
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(findBestInfo(fonts, style).getUri());
            typeface = createFromInputStream(context, is);
            return typeface;
        } catch (IOException e) {
            return typeface;
        } finally {
            TypefaceCompatUtil.closeQuietly(is);
        }
    }

    private FontFileResourceEntry findBestEntry(FontFamilyFilesResourceEntry entry, int style) {
        return (FontFileResourceEntry) findBestFont(entry.getEntries(), style, new C02512());
    }

    @Nullable
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry entry, Resources resources, int style) {
        FontFileResourceEntry best = findBestEntry(entry, style);
        if (best == null) {
            return null;
        }
        return TypefaceCompat.createFromResourcesFontFile(context, resources, best.getResourceId(), best.getFileName(), style);
    }

    @Nullable
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int style) {
        File tmpFile = TypefaceCompatUtil.getTempFile(context);
        Typeface typeface = null;
        if (tmpFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tmpFile, resources, id)) {
                return typeface;
            }
            typeface = Typeface.createFromFile(tmpFile.getPath());
            tmpFile.delete();
            return typeface;
        } catch (RuntimeException e) {
            return typeface;
        } finally {
            tmpFile.delete();
        }
    }
}
