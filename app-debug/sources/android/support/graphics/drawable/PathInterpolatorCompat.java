package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo({Scope.LIBRARY_GROUP})
public class PathInterpolatorCompat implements Interpolator {
    public static final double EPSILON = 1.0E-5d;
    public static final int MAX_NUM_POINTS = 3000;
    private static final float PRECISION = 0.002f;
    private float[] mX;
    private float[] mY;

    private void initPath(android.graphics.Path r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:29:0x0119 in {4, 17, 19, 22, 24, 26, 28} preds:[]
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
        r12 = this;
        r0 = new android.graphics.PathMeasure;
        r1 = 0;
        r0.<init>(r13, r1);
        r2 = r0.getLength();
        r3 = 990057071; // 0x3b03126f float:0.002 double:4.89153186E-315;
        r3 = r2 / r3;
        r3 = (int) r3;
        r4 = 1;
        r3 = r3 + r4;
        r5 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
        r3 = java.lang.Math.min(r5, r3);
        if (r3 <= 0) goto L_0x0102;
    L_0x001a:
        r5 = new float[r3];
        r12.mX = r5;
        r5 = new float[r3];
        r12.mY = r5;
        r5 = 2;
        r5 = new float[r5];
        r6 = 0;
    L_0x0026:
        if (r6 >= r3) goto L_0x0042;
    L_0x0028:
        r7 = (float) r6;
        r7 = r7 * r2;
        r8 = r3 + -1;
        r8 = (float) r8;
        r7 = r7 / r8;
        r8 = 0;
        r0.getPosTan(r7, r5, r8);
        r8 = r12.mX;
        r9 = r5[r1];
        r8[r6] = r9;
        r8 = r12.mY;
        r9 = r5[r4];
        r8[r6] = r9;
        r6 = r6 + 1;
        goto L_0x0026;
    L_0x0042:
        r4 = r12.mX;
        r4 = r4[r1];
        r4 = java.lang.Math.abs(r4);
        r6 = (double) r4;
        r8 = 4532020583610935537; // 0x3ee4f8b588e368f1 float:-1.3686737E-33 double:1.0E-5;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 > 0) goto L_0x00bf;
    L_0x0054:
        r4 = r12.mY;
        r4 = r4[r1];
        r4 = java.lang.Math.abs(r4);
        r6 = (double) r4;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 > 0) goto L_0x00bf;
    L_0x0061:
        r4 = r12.mX;
        r6 = r3 + -1;
        r4 = r4[r6];
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = r4 - r6;
        r4 = java.lang.Math.abs(r4);
        r10 = (double) r4;
        r4 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r4 > 0) goto L_0x00bf;
    L_0x0073:
        r4 = r12.mY;
        r7 = r3 + -1;
        r4 = r4[r7];
        r4 = r4 - r6;
        r4 = java.lang.Math.abs(r4);
        r6 = (double) r4;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 > 0) goto L_0x00bf;
    L_0x0083:
        r1 = 0;
        r4 = 0;
        r6 = 0;
    L_0x0086:
        if (r6 >= r3) goto L_0x00b0;
    L_0x0088:
        r7 = r12.mX;
        r8 = r4 + 1;
        r4 = r7[r4];
        r9 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r9 < 0) goto L_0x0099;
    L_0x0092:
        r7[r6] = r4;
        r1 = r4;
        r6 = r6 + 1;
        r4 = r8;
        goto L_0x0086;
    L_0x0099:
        r7 = new java.lang.IllegalArgumentException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "The Path cannot loop back on itself, x :";
        r9.append(r10);
        r9.append(r4);
        r9 = r9.toString();
        r7.<init>(r9);
        throw r7;
    L_0x00b0:
        r6 = r0.nextContour();
        if (r6 != 0) goto L_0x00b7;
    L_0x00b6:
        return;
    L_0x00b7:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "The Path should be continuous, can't have 2+ contours";
        r6.<init>(r7);
        throw r6;
    L_0x00bf:
        r4 = new java.lang.IllegalArgumentException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "The Path must start at (0,0) and end at (1,1) start: ";
        r6.append(r7);
        r7 = r12.mX;
        r7 = r7[r1];
        r6.append(r7);
        r7 = ",";
        r6.append(r7);
        r7 = r12.mY;
        r1 = r7[r1];
        r6.append(r1);
        r1 = " end:";
        r6.append(r1);
        r1 = r12.mX;
        r7 = r3 + -1;
        r1 = r1[r7];
        r6.append(r1);
        r1 = ",";
        r6.append(r1);
        r1 = r12.mY;
        r7 = r3 + -1;
        r1 = r1[r7];
        r6.append(r1);
        r1 = r6.toString();
        r4.<init>(r1);
        throw r4;
    L_0x0102:
        r1 = new java.lang.IllegalArgumentException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "The Path has a invalid length ";
        r4.append(r5);
        r4.append(r2);
        r4 = r4.toString();
        r1.<init>(r4);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.PathInterpolatorCompat.initPath(android.graphics.Path):void");
    }

    public PathInterpolatorCompat(Context context, AttributeSet attrs, XmlPullParser parser) {
        this(context.getResources(), context.getTheme(), attrs, parser);
    }

    public PathInterpolatorCompat(Resources res, Theme theme, AttributeSet attrs, XmlPullParser parser) {
        TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        parseInterpolatorFromTypeArray(a, parser);
        a.recycle();
    }

    private void parseInterpolatorFromTypeArray(TypedArray a, XmlPullParser parser) {
        if (TypedArrayUtils.hasAttribute(parser, "pathData")) {
            String pathData = TypedArrayUtils.getNamedString(a, parser, "pathData", 4);
            Path path = PathParser.createPathFromPathData(pathData);
            if (path != null) {
                initPath(path);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The path is null, which is created from ");
            stringBuilder.append(pathData);
            throw new InflateException(stringBuilder.toString());
        } else if (!TypedArrayUtils.hasAttribute(parser, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
        } else if (TypedArrayUtils.hasAttribute(parser, "controlY1")) {
            float x1 = TypedArrayUtils.getNamedFloat(a, parser, "controlX1", 0, 0.0f);
            float y1 = TypedArrayUtils.getNamedFloat(a, parser, "controlY1", 1, 0.0f);
            boolean hasX2 = TypedArrayUtils.hasAttribute(parser, "controlX2");
            if (hasX2 != TypedArrayUtils.hasAttribute(parser, "controlY2")) {
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            } else if (hasX2) {
                initCubic(x1, y1, TypedArrayUtils.getNamedFloat(a, parser, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat(a, parser, "controlY2", 3, 0.0f));
            } else {
                initQuad(x1, y1);
            }
        } else {
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        }
    }

    private void initQuad(float controlX, float controlY) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(controlX, controlY, 1.0f, 1.0f);
        initPath(path);
    }

    private void initCubic(float x1, float y1, float x2, float y2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(x1, y1, x2, y2, 1.0f, 1.0f);
        initPath(path);
    }

    public float getInterpolation(float t) {
        if (t <= 0.0f) {
            return 0.0f;
        }
        if (t >= 1.0f) {
            return 1.0f;
        }
        int startIndex = 0;
        int endIndex = this.mX.length - 1;
        while (endIndex - startIndex > 1) {
            int midIndex = (startIndex + endIndex) / 2;
            if (t < this.mX[midIndex]) {
                endIndex = midIndex;
            } else {
                startIndex = midIndex;
            }
        }
        float[] fArr = this.mX;
        float xRange = fArr[endIndex] - fArr[startIndex];
        if (xRange == 0.0f) {
            return this.mY[startIndex];
        }
        float fraction = (t - fArr[startIndex]) / xRange;
        float endY = this.mY;
        float startY = endY[startIndex];
        return ((endY[endIndex] - startY) * fraction) + startY;
    }
}
