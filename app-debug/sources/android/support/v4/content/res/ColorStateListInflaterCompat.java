package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class ColorStateListInflaterCompat {
    private static final int DEFAULT_COLOR = -65536;

    @android.support.annotation.NonNull
    public static android.content.res.ColorStateList createFromXml(@android.support.annotation.NonNull android.content.res.Resources r4, @android.support.annotation.NonNull org.xmlpull.v1.XmlPullParser r5, @android.support.annotation.Nullable android.content.res.Resources.Theme r6) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
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
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.ColorStateListInflaterCompat.createFromXml(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.content.res.Resources$Theme):android.content.res.ColorStateList");
    }

    private ColorStateListInflaterCompat() {
    }

    @NonNull
    public static ColorStateList createFromXmlInner(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        String name = parser.getName();
        if (name.equals("selector")) {
            return inflate(r, parser, attrs, theme);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parser.getPositionDescription());
        stringBuilder.append(": invalid color state list tag ");
        stringBuilder.append(name);
        throw new XmlPullParserException(stringBuilder.toString());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.content.res.ColorStateList inflate(@android.support.annotation.NonNull android.content.res.Resources r22, @android.support.annotation.NonNull org.xmlpull.v1.XmlPullParser r23, @android.support.annotation.NonNull android.util.AttributeSet r24, @android.support.annotation.Nullable android.content.res.Resources.Theme r25) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r0 = r24;
        r1 = r23.getDepth();
        r2 = 1;
        r1 = r1 + r2;
        r3 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
        r4 = 20;
        r4 = new int[r4][];
        r5 = r4.length;
        r5 = new int[r5];
        r6 = 0;
    L_0x0012:
        r7 = r23.next();
        r8 = r7;
        if (r7 == r2) goto L_0x00f7;
    L_0x0019:
        r7 = r23.getDepth();
        r10 = r7;
        if (r7 >= r1) goto L_0x002e;
    L_0x0020:
        r7 = 3;
        if (r8 == r7) goto L_0x0024;
    L_0x0023:
        goto L_0x002e;
    L_0x0024:
        r11 = r22;
        r12 = r25;
        r16 = r1;
        r18 = r3;
        goto L_0x00ff;
    L_0x002e:
        r7 = 2;
        if (r8 != r7) goto L_0x00e8;
    L_0x0031:
        if (r10 > r1) goto L_0x00e8;
    L_0x0033:
        r7 = r23.getName();
        r11 = "item";
        r7 = r7.equals(r11);
        if (r7 != 0) goto L_0x0049;
    L_0x003f:
        r11 = r22;
        r12 = r25;
        r16 = r1;
        r18 = r3;
        goto L_0x00f0;
    L_0x0049:
        r7 = android.support.compat.C0014R.styleable.ColorStateListItem;
        r11 = r22;
        r12 = r25;
        r7 = obtainAttributes(r11, r12, r0, r7);
        r13 = android.support.compat.C0014R.styleable.ColorStateListItem_android_color;
        r14 = -65281; // 0xffffffffffff00ff float:NaN double:NaN;
        r13 = r7.getColor(r13, r14);
        r14 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r15 = android.support.compat.C0014R.styleable.ColorStateListItem_android_alpha;
        r15 = r7.hasValue(r15);
        if (r15 == 0) goto L_0x006d;
    L_0x0066:
        r15 = android.support.compat.C0014R.styleable.ColorStateListItem_android_alpha;
        r14 = r7.getFloat(r15, r14);
        goto L_0x007b;
    L_0x006d:
        r15 = android.support.compat.C0014R.styleable.ColorStateListItem_alpha;
        r15 = r7.hasValue(r15);
        if (r15 == 0) goto L_0x007b;
    L_0x0075:
        r15 = android.support.compat.C0014R.styleable.ColorStateListItem_alpha;
        r14 = r7.getFloat(r15, r14);
    L_0x007b:
        r7.recycle();
        r15 = 0;
        r2 = r24.getAttributeCount();
        r9 = new int[r2];
        r16 = 0;
        r21 = r16;
        r16 = r1;
        r1 = r15;
        r15 = r21;
    L_0x008e:
        if (r15 >= r2) goto L_0x00bf;
    L_0x0090:
        r17 = r2;
        r2 = r0.getAttributeNameResource(r15);
        r18 = r3;
        r3 = 16843173; // 0x10101a5 float:2.3694738E-38 double:8.321633E-317;
        if (r2 == r3) goto L_0x00b8;
    L_0x009d:
        r3 = 16843551; // 0x101031f float:2.3695797E-38 double:8.32182E-317;
        if (r2 == r3) goto L_0x00b8;
    L_0x00a2:
        r3 = android.support.compat.C0014R.attr.alpha;
        if (r2 == r3) goto L_0x00b8;
    L_0x00a6:
        r3 = r1 + 1;
        r19 = r3;
        r3 = 0;
        r20 = r0.getAttributeBooleanValue(r15, r3);
        if (r20 == 0) goto L_0x00b3;
    L_0x00b1:
        r3 = r2;
        goto L_0x00b4;
    L_0x00b3:
        r3 = -r2;
    L_0x00b4:
        r9[r1] = r3;
        r1 = r19;
    L_0x00b8:
        r15 = r15 + 1;
        r2 = r17;
        r3 = r18;
        goto L_0x008e;
    L_0x00bf:
        r17 = r2;
        r18 = r3;
        r2 = android.util.StateSet.trimStateSet(r9, r1);
        r3 = modulateColorAlpha(r13, r14);
        if (r6 == 0) goto L_0x00d0;
    L_0x00cd:
        r9 = r2.length;
        if (r9 != 0) goto L_0x00d3;
    L_0x00d0:
        r9 = r3;
        r18 = r9;
    L_0x00d3:
        r5 = android.support.v4.content.res.GrowingArrayUtils.append(r5, r6, r3);
        r9 = android.support.v4.content.res.GrowingArrayUtils.append(r4, r6, r2);
        r4 = r9;
        r4 = (int[][]) r4;
        r6 = r6 + 1;
        r1 = r16;
        r3 = r18;
        r2 = 1;
        goto L_0x0012;
    L_0x00e8:
        r11 = r22;
        r12 = r25;
        r16 = r1;
        r18 = r3;
    L_0x00f0:
        r1 = r16;
        r3 = r18;
        r2 = 1;
        goto L_0x0012;
    L_0x00f7:
        r11 = r22;
        r12 = r25;
        r16 = r1;
        r18 = r3;
    L_0x00ff:
        r1 = new int[r6];
        r2 = new int[r6][];
        r3 = 0;
        java.lang.System.arraycopy(r5, r3, r1, r3, r6);
        java.lang.System.arraycopy(r4, r3, r2, r3, r6);
        r3 = new android.content.res.ColorStateList;
        r3.<init>(r2, r1);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.ColorStateListInflaterCompat.inflate(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):android.content.res.ColorStateList");
    }

    private static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    @ColorInt
    private static int modulateColorAlpha(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float alphaMod) {
        return (ViewCompat.MEASURED_SIZE_MASK & color) | (Math.round(((float) Color.alpha(color)) * alphaMod) << 24);
    }
}
