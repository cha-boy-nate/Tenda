package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.compat.C0014R;
import android.util.AttributeSet;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
final class GradientColorInflaterCompat {
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;

    static final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(@NonNull List<Integer> colorsList, @NonNull List<Float> offsetsList) {
            int size = colorsList.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; i++) {
                this.mColors[i] = ((Integer) colorsList.get(i)).intValue();
                this.mOffsets[i] = ((Float) offsetsList.get(i)).floatValue();
            }
        }

        ColorStops(@ColorInt int startColor, @ColorInt int endColor) {
            this.mColors = new int[]{startColor, endColor};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(@ColorInt int startColor, @ColorInt int centerColor, @ColorInt int endColor) {
            this.mColors = new int[]{startColor, centerColor, endColor};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }
    }

    static android.graphics.Shader createFromXml(@android.support.annotation.NonNull android.content.res.Resources r4, @android.support.annotation.NonNull org.xmlpull.v1.XmlPullParser r5, @android.support.annotation.Nullable android.content.res.Resources.Theme r6) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:11:0x001f in {5, 8, 10} preds:[]
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
        r0 = android.util.Xml.asAttributeSet(r5);
    L_0x0004:
        r1 = r5.next();
        r2 = r1;
        r3 = 2;
        if (r1 == r3) goto L_0x0010;
    L_0x000c:
        r1 = 1;
        if (r2 == r1) goto L_0x0010;
    L_0x000f:
        goto L_0x0004;
    L_0x0010:
        if (r2 != r3) goto L_0x0017;
    L_0x0012:
        r1 = createFromXmlInner(r4, r5, r0, r6);
        return r1;
    L_0x0017:
        r1 = new org.xmlpull.v1.XmlPullParserException;
        r3 = "No start tag found";
        r1.<init>(r3);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.GradientColorInflaterCompat.createFromXml(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.content.res.Resources$Theme):android.graphics.Shader");
    }

    private GradientColorInflaterCompat() {
    }

    static Shader createFromXmlInner(@NonNull Resources resources, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws IOException, XmlPullParserException {
        XmlPullParser xmlPullParser = parser;
        String name = parser.getName();
        if (name.equals("gradient")) {
            Theme theme2 = theme;
            TypedArray a = TypedArrayUtils.obtainAttributes(resources, theme2, attrs, C0014R.styleable.GradientColor);
            float startX = TypedArrayUtils.getNamedFloat(a, xmlPullParser, "startX", C0014R.styleable.GradientColor_android_startX, 0.0f);
            float startY = TypedArrayUtils.getNamedFloat(a, xmlPullParser, "startY", C0014R.styleable.GradientColor_android_startY, 0.0f);
            float endX = TypedArrayUtils.getNamedFloat(a, xmlPullParser, "endX", C0014R.styleable.GradientColor_android_endX, 0.0f);
            float endY = TypedArrayUtils.getNamedFloat(a, xmlPullParser, "endY", C0014R.styleable.GradientColor_android_endY, 0.0f);
            float centerX = TypedArrayUtils.getNamedFloat(a, xmlPullParser, "centerX", C0014R.styleable.GradientColor_android_centerX, 0.0f);
            float centerY = TypedArrayUtils.getNamedFloat(a, xmlPullParser, "centerY", C0014R.styleable.GradientColor_android_centerY, 0.0f);
            int type = TypedArrayUtils.getNamedInt(a, xmlPullParser, "type", C0014R.styleable.GradientColor_android_type, 0);
            int startColor = TypedArrayUtils.getNamedColor(a, xmlPullParser, "startColor", C0014R.styleable.GradientColor_android_startColor, 0);
            boolean hasCenterColor = TypedArrayUtils.hasAttribute(xmlPullParser, "centerColor");
            int centerColor = TypedArrayUtils.getNamedColor(a, xmlPullParser, "centerColor", C0014R.styleable.GradientColor_android_centerColor, 0);
            int endColor = TypedArrayUtils.getNamedColor(a, xmlPullParser, "endColor", C0014R.styleable.GradientColor_android_endColor, 0);
            int tileMode = TypedArrayUtils.getNamedInt(a, xmlPullParser, "tileMode", C0014R.styleable.GradientColor_android_tileMode, 0);
            float gradientRadius = TypedArrayUtils.getNamedFloat(a, xmlPullParser, "gradientRadius", C0014R.styleable.GradientColor_android_gradientRadius, 0.0f);
            a.recycle();
            ColorStops colorStops = checkColors(inflateChildElements(resources, parser, attrs, theme), startColor, endColor, hasCenterColor, centerColor);
            switch (type) {
                case 1:
                    if (gradientRadius > 0.0f) {
                        int[] iArr = colorStops.mColors;
                        return new RadialGradient(centerX, centerY, gradientRadius, iArr, colorStops.mOffsets, parseTileMode(tileMode));
                    }
                    throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
                case 2:
                    return new SweepGradient(centerX, centerY, colorStops.mColors, colorStops.mOffsets);
                default:
                    int[] iArr2 = colorStops.mColors;
                    int[] iArr3 = iArr2;
                    int[] iArr4 = iArr3;
                    return new LinearGradient(startX, startY, endX, endY, iArr4, colorStops.mOffsets, parseTileMode(tileMode));
            }
        }
        theme2 = theme;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parser.getPositionDescription());
        stringBuilder.append(": invalid gradient color tag ");
        stringBuilder.append(name);
        throw new XmlPullParserException(stringBuilder.toString());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.support.v4.content.res.GradientColorInflaterCompat.ColorStops inflateChildElements(@android.support.annotation.NonNull android.content.res.Resources r12, @android.support.annotation.NonNull org.xmlpull.v1.XmlPullParser r13, @android.support.annotation.NonNull android.util.AttributeSet r14, @android.support.annotation.Nullable android.content.res.Resources.Theme r15) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r0 = r13.getDepth();
        r1 = 1;
        r0 = r0 + r1;
        r2 = new java.util.ArrayList;
        r3 = 20;
        r2.<init>(r3);
        r4 = new java.util.ArrayList;
        r4.<init>(r3);
        r3 = r4;
    L_0x0013:
        r4 = r13.next();
        r5 = r4;
        if (r4 == r1) goto L_0x008d;
    L_0x001a:
        r4 = r13.getDepth();
        r6 = r4;
        if (r4 >= r0) goto L_0x0024;
    L_0x0021:
        r4 = 3;
        if (r5 == r4) goto L_0x008d;
    L_0x0024:
        r4 = 2;
        if (r5 == r4) goto L_0x0028;
    L_0x0027:
        goto L_0x0013;
    L_0x0028:
        if (r6 > r0) goto L_0x0013;
    L_0x002a:
        r4 = r13.getName();
        r7 = "item";
        r4 = r4.equals(r7);
        if (r4 != 0) goto L_0x0037;
    L_0x0036:
        goto L_0x0013;
    L_0x0037:
        r4 = android.support.compat.C0014R.styleable.GradientColorItem;
        r4 = android.support.v4.content.res.TypedArrayUtils.obtainAttributes(r12, r15, r14, r4);
        r7 = android.support.compat.C0014R.styleable.GradientColorItem_android_color;
        r7 = r4.hasValue(r7);
        r8 = android.support.compat.C0014R.styleable.GradientColorItem_android_offset;
        r8 = r4.hasValue(r8);
        if (r7 == 0) goto L_0x006d;
    L_0x004b:
        if (r8 == 0) goto L_0x006d;
    L_0x004d:
        r9 = android.support.compat.C0014R.styleable.GradientColorItem_android_color;
        r10 = 0;
        r9 = r4.getColor(r9, r10);
        r10 = android.support.compat.C0014R.styleable.GradientColorItem_android_offset;
        r11 = 0;
        r10 = r4.getFloat(r10, r11);
        r4.recycle();
        r11 = java.lang.Integer.valueOf(r9);
        r3.add(r11);
        r11 = java.lang.Float.valueOf(r10);
        r2.add(r11);
        goto L_0x0013;
    L_0x006d:
        r1 = new org.xmlpull.v1.XmlPullParserException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = r13.getPositionDescription();
        r9.append(r10);
        r10 = ": <item> tag requires a 'color' attribute and a 'offset' ";
        r9.append(r10);
        r10 = "attribute!";
        r9.append(r10);
        r9 = r9.toString();
        r1.<init>(r9);
        throw r1;
    L_0x008d:
        r1 = r3.size();
        if (r1 <= 0) goto L_0x0099;
    L_0x0093:
        r1 = new android.support.v4.content.res.GradientColorInflaterCompat$ColorStops;
        r1.<init>(r3, r2);
        return r1;
    L_0x0099:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.GradientColorInflaterCompat.inflateChildElements(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):android.support.v4.content.res.GradientColorInflaterCompat$ColorStops");
    }

    private static ColorStops checkColors(@Nullable ColorStops colorItems, @ColorInt int startColor, @ColorInt int endColor, boolean hasCenterColor, @ColorInt int centerColor) {
        if (colorItems != null) {
            return colorItems;
        }
        if (hasCenterColor) {
            return new ColorStops(startColor, centerColor, endColor);
        }
        return new ColorStops(startColor, endColor);
    }

    private static TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case 1:
                return TileMode.REPEAT;
            case 2:
                return TileMode.MIRROR;
            default:
                return TileMode.CLAMP;
        }
    }
}
