package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class ComplexColorCompat {
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;

    @android.support.annotation.NonNull
    private static android.support.v4.content.res.ComplexColorCompat createFromXml(@android.support.annotation.NonNull android.content.res.Resources r8, @android.support.annotation.ColorRes int r9, @android.support.annotation.Nullable android.content.res.Resources.Theme r10) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:27:0x0079 in {4, 13, 16, 17, 20, 22, 24, 26} preds:[]
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
        r0 = r8.getXml(r9);
        r1 = android.util.Xml.asAttributeSet(r0);
    L_0x0008:
        r2 = r0.next();
        r3 = r2;
        r4 = 1;
        r5 = 2;
        if (r2 == r5) goto L_0x0014;
    L_0x0011:
        if (r3 == r4) goto L_0x0014;
    L_0x0013:
        goto L_0x0008;
    L_0x0014:
        if (r3 != r5) goto L_0x0071;
    L_0x0016:
        r2 = r0.getName();
        r5 = -1;
        r6 = r2.hashCode();
        r7 = 89650992; // 0x557f730 float:1.01546526E-35 double:4.42934753E-316;
        if (r6 == r7) goto L_0x0034;
    L_0x0024:
        r4 = 1191572447; // 0x4705f3df float:34291.87 double:5.887150106E-315;
        if (r6 == r4) goto L_0x002a;
    L_0x0029:
        goto L_0x003d;
    L_0x002a:
        r4 = "selector";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x0029;
    L_0x0032:
        r4 = 0;
        goto L_0x003e;
    L_0x0034:
        r6 = "gradient";
        r6 = r2.equals(r6);
        if (r6 == 0) goto L_0x0029;
    L_0x003c:
        goto L_0x003e;
    L_0x003d:
        r4 = -1;
    L_0x003e:
        switch(r4) {
            case 0: goto L_0x0068;
            case 1: goto L_0x005f;
            default: goto L_0x0041;
        };
    L_0x0041:
        r4 = new org.xmlpull.v1.XmlPullParserException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = r0.getPositionDescription();
        r5.append(r6);
        r6 = ": unsupported complex color tag ";
        r5.append(r6);
        r5.append(r2);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
    L_0x005f:
        r4 = android.support.v4.content.res.GradientColorInflaterCompat.createFromXmlInner(r8, r0, r1, r10);
        r4 = from(r4);
        return r4;
    L_0x0068:
        r4 = android.support.v4.content.res.ColorStateListInflaterCompat.createFromXmlInner(r8, r0, r1, r10);
        r4 = from(r4);
        return r4;
    L_0x0071:
        r2 = new org.xmlpull.v1.XmlPullParserException;
        r4 = "No start tag found";
        r2.<init>(r4);
        throw r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.ComplexColorCompat.createFromXml(android.content.res.Resources, int, android.content.res.Resources$Theme):android.support.v4.content.res.ComplexColorCompat");
    }

    private ComplexColorCompat(Shader shader, ColorStateList colorStateList, @ColorInt int color) {
        this.mShader = shader;
        this.mColorStateList = colorStateList;
        this.mColor = color;
    }

    static ComplexColorCompat from(@NonNull Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }

    static ComplexColorCompat from(@NonNull ColorStateList colorStateList) {
        return new ComplexColorCompat(null, colorStateList, colorStateList.getDefaultColor());
    }

    static ComplexColorCompat from(@ColorInt int color) {
        return new ComplexColorCompat(null, null, color);
    }

    @Nullable
    public Shader getShader() {
        return this.mShader;
    }

    @ColorInt
    public int getColor() {
        return this.mColor;
    }

    public void setColor(@ColorInt int color) {
        this.mColor = color;
    }

    public boolean isGradient() {
        return this.mShader != null;
    }

    public boolean isStateful() {
        if (this.mShader == null) {
            ColorStateList colorStateList = this.mColorStateList;
            if (colorStateList != null && colorStateList.isStateful()) {
                return true;
            }
        }
        return false;
    }

    public boolean onStateChanged(int[] stateSet) {
        if (!isStateful()) {
            return false;
        }
        int colorForState = this.mColorStateList;
        colorForState = colorForState.getColorForState(stateSet, colorForState.getDefaultColor());
        if (colorForState == this.mColor) {
            return false;
        }
        this.mColor = colorForState;
        return true;
    }

    public boolean willDraw() {
        if (!isGradient()) {
            if (this.mColor == 0) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    public static ComplexColorCompat inflate(@NonNull Resources resources, @ColorRes int resId, @Nullable Theme theme) {
        try {
            return createFromXml(resources, resId, theme);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to inflate ComplexColor.", e);
            return null;
        }
    }
}
