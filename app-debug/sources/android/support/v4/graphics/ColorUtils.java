package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;

public final class ColorUtils {
    private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
    private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
    private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal();
    private static final double XYZ_EPSILON = 0.008856d;
    private static final double XYZ_KAPPA = 903.3d;
    private static final double XYZ_WHITE_REFERENCE_X = 95.047d;
    private static final double XYZ_WHITE_REFERENCE_Y = 100.0d;
    private static final double XYZ_WHITE_REFERENCE_Z = 108.883d;

    public static int calculateMinimumAlpha(@android.support.annotation.ColorInt int r10, @android.support.annotation.ColorInt int r11, float r12) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:19:0x0058 in {5, 13, 14, 15, 16, 18} preds:[]
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
        r0 = android.graphics.Color.alpha(r11);
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r0 != r1) goto L_0x003d;
    L_0x0008:
        r0 = setAlphaComponent(r10, r1);
        r1 = calculateContrast(r0, r11);
        r3 = (double) r12;
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r5 >= 0) goto L_0x0017;
    L_0x0015:
        r3 = -1;
        return r3;
    L_0x0017:
        r3 = 0;
        r4 = 0;
        r5 = 255; // 0xff float:3.57E-43 double:1.26E-321;
    L_0x001b:
        r6 = 10;
        if (r3 > r6) goto L_0x003c;
    L_0x001f:
        r6 = r5 - r4;
        r7 = 1;
        if (r6 <= r7) goto L_0x003c;
    L_0x0024:
        r6 = r4 + r5;
        r6 = r6 / 2;
        r0 = setAlphaComponent(r10, r6);
        r1 = calculateContrast(r0, r11);
        r7 = (double) r12;
        r9 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1));
        if (r9 >= 0) goto L_0x0037;
    L_0x0035:
        r4 = r6;
        goto L_0x0038;
    L_0x0037:
        r5 = r6;
        r3 = r3 + 1;
        goto L_0x001b;
    L_0x003c:
        return r5;
    L_0x003d:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "background can not be translucent: #";
        r1.append(r2);
        r2 = java.lang.Integer.toHexString(r11);
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.ColorUtils.calculateMinimumAlpha(int, int, float):int");
    }

    @android.support.annotation.RequiresApi(26)
    @android.support.annotation.NonNull
    public static android.graphics.Color compositeColors(@android.support.annotation.NonNull android.graphics.Color r9, @android.support.annotation.NonNull android.graphics.Color r10) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:16:0x0099 in {4, 5, 8, 11, 13, 15} preds:[]
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
        r0 = r9.getModel();
        r1 = r10.getModel();
        r0 = java.util.Objects.equals(r0, r1);
        if (r0 == 0) goto L_0x006d;
    L_0x000e:
        r0 = r10.getColorSpace();
        r1 = r9.getColorSpace();
        r0 = java.util.Objects.equals(r0, r1);
        if (r0 == 0) goto L_0x001e;
    L_0x001c:
        r0 = r9;
        goto L_0x0026;
    L_0x001e:
        r0 = r10.getColorSpace();
        r0 = r9.convert(r0);
        r1 = r0.getComponents();
        r2 = r10.getComponents();
        r3 = r0.alpha();
        r4 = r10.alpha();
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r5 = r5 - r3;
        r4 = r4 * r5;
        r5 = r10.getComponentCount();
        r5 = r5 + -1;
        r6 = r3 + r4;
        r2[r5] = r6;
        r6 = r2[r5];
        r7 = 0;
        r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r6 <= 0) goto L_0x0053;
    L_0x004d:
        r6 = r2[r5];
        r3 = r3 / r6;
        r6 = r2[r5];
        r4 = r4 / r6;
    L_0x0053:
        r6 = 0;
    L_0x0054:
        if (r6 >= r5) goto L_0x0064;
    L_0x0056:
        r7 = r1[r6];
        r7 = r7 * r3;
        r8 = r2[r6];
        r8 = r8 * r4;
        r7 = r7 + r8;
        r2[r6] = r7;
        r6 = r6 + 1;
        goto L_0x0054;
    L_0x0064:
        r6 = r10.getColorSpace();
        r6 = android.graphics.Color.valueOf(r2, r6);
        return r6;
    L_0x006d:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Color models must match (";
        r1.append(r2);
        r2 = r9.getModel();
        r1.append(r2);
        r2 = " vs. ";
        r1.append(r2);
        r2 = r10.getModel();
        r1.append(r2);
        r2 = ")";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.ColorUtils.compositeColors(android.graphics.Color, android.graphics.Color):android.graphics.Color");
    }

    private ColorUtils() {
    }

    public static int compositeColors(@ColorInt int foreground, @ColorInt int background) {
        int bgAlpha = Color.alpha(background);
        int fgAlpha = Color.alpha(foreground);
        int a = compositeAlpha(fgAlpha, bgAlpha);
        return Color.argb(a, compositeComponent(Color.red(foreground), fgAlpha, Color.red(background), bgAlpha, a), compositeComponent(Color.green(foreground), fgAlpha, Color.green(background), bgAlpha, a), compositeComponent(Color.blue(foreground), fgAlpha, Color.blue(background), bgAlpha, a));
    }

    private static int compositeAlpha(int foregroundAlpha, int backgroundAlpha) {
        return 255 - (((255 - backgroundAlpha) * (255 - foregroundAlpha)) / 255);
    }

    private static int compositeComponent(int fgC, int fgA, int bgC, int bgA, int a) {
        if (a == 0) {
            return 0;
        }
        return (((fgC * 255) * fgA) + ((bgC * bgA) * (255 - fgA))) / (a * 255);
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    public static double calculateLuminance(@ColorInt int color) {
        double[] result = getTempDouble3Array();
        colorToXYZ(color, result);
        return result[1] / XYZ_WHITE_REFERENCE_Y;
    }

    public static double calculateContrast(@ColorInt int foreground, @ColorInt int background) {
        if (Color.alpha(background) == 255) {
            if (Color.alpha(foreground) < 255) {
                foreground = compositeColors(foreground, background);
            }
            double luminance1 = calculateLuminance(foreground) + 0.05d;
            double luminance2 = calculateLuminance(background) + 0.05d;
            return Math.max(luminance1, luminance2) / Math.min(luminance1, luminance2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("background can not be translucent: #");
        stringBuilder.append(Integer.toHexString(background));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static void RGBToHSL(@IntRange(from = 0, to = 255) int r, @IntRange(from = 0, to = 255) int g, @IntRange(from = 0, to = 255) int b, @NonNull float[] outHsl) {
        float s;
        float h;
        float rf = ((float) r) / 255.0f;
        float gf = ((float) g) / 255.0f;
        float bf = ((float) b) / 255.0f;
        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float deltaMaxMin = max - min;
        float l = (max + min) / 2.0f;
        if (max == min) {
            s = 0.0f;
            h = 0.0f;
        } else {
            if (max == rf) {
                h = ((gf - bf) / deltaMaxMin) % 6.0f;
            } else if (max == gf) {
                h = ((bf - rf) / deltaMaxMin) + 2.0f;
            } else {
                h = ((rf - gf) / deltaMaxMin) + 4.0f;
            }
            s = deltaMaxMin / (1.0f - Math.abs((2.0f * l) - 1.0f));
        }
        float h2 = (60.0f * h) % 360.0f;
        if (h2 < 0.0f) {
            h2 += 360.0f;
        }
        outHsl[0] = constrain(h2, 0.0f, 360.0f);
        outHsl[1] = constrain(s, 0.0f, 1.0f);
        outHsl[2] = constrain(l, 0.0f, 1.0f);
    }

    public static void colorToHSL(@ColorInt int color, @NonNull float[] outHsl) {
        RGBToHSL(Color.red(color), Color.green(color), Color.blue(color), outHsl);
    }

    @ColorInt
    public static int HSLToColor(@NonNull float[] hsl) {
        float h = hsl[0];
        float s = hsl[1];
        float l = hsl[2];
        float c = (1.0f - Math.abs((l * 2.0f) - 1.0f)) * s;
        float m = l - (0.5f * c);
        float x = (1.0f - Math.abs(((h / 60.0f) % 2.0f) - 1.0f)) * c;
        int r = 0;
        int g = 0;
        int b = 0;
        switch (((int) h) / 60) {
            case 0:
                r = Math.round((c + m) * 255.0f);
                g = Math.round((x + m) * 255.0f);
                b = Math.round(255.0f * m);
                break;
            case 1:
                r = Math.round((x + m) * 255.0f);
                g = Math.round((c + m) * 255.0f);
                b = Math.round(255.0f * m);
                break;
            case 2:
                r = Math.round(m * 255.0f);
                g = Math.round((c + m) * 255.0f);
                b = Math.round((x + m) * 255.0f);
                break;
            case 3:
                r = Math.round(m * 255.0f);
                g = Math.round((x + m) * 255.0f);
                b = Math.round((c + m) * 255.0f);
                break;
            case 4:
                r = Math.round((x + m) * 255.0f);
                g = Math.round(m * 255.0f);
                b = Math.round((c + m) * 255.0f);
                break;
            case 5:
            case 6:
                r = Math.round((c + m) * 255.0f);
                g = Math.round(m * 255.0f);
                b = Math.round((x + m) * 255.0f);
                break;
            default:
                break;
        }
        return Color.rgb(constrain(r, 0, 255), constrain(g, 0, 255), constrain(b, 0, 255));
    }

    @ColorInt
    public static int setAlphaComponent(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        if (alpha >= 0 && alpha <= 255) {
            return (ViewCompat.MEASURED_SIZE_MASK & color) | (alpha << 24);
        }
        throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }

    public static void colorToLAB(@ColorInt int color, @NonNull double[] outLab) {
        RGBToLAB(Color.red(color), Color.green(color), Color.blue(color), outLab);
    }

    public static void RGBToLAB(@IntRange(from = 0, to = 255) int r, @IntRange(from = 0, to = 255) int g, @IntRange(from = 0, to = 255) int b, @NonNull double[] outLab) {
        RGBToXYZ(r, g, b, outLab);
        XYZToLAB(outLab[0], outLab[1], outLab[2], outLab);
    }

    public static void colorToXYZ(@ColorInt int color, @NonNull double[] outXyz) {
        RGBToXYZ(Color.red(color), Color.green(color), Color.blue(color), outXyz);
    }

    public static void RGBToXYZ(@IntRange(from = 0, to = 255) int r, @IntRange(from = 0, to = 255) int g, @IntRange(from = 0, to = 255) int b, @NonNull double[] outXyz) {
        double[] dArr = outXyz;
        if (dArr.length == 3) {
            double sr = (double) r;
            Double.isNaN(sr);
            sr /= 255.0d;
            sr = sr < 0.04045d ? sr / 12.92d : Math.pow((sr + 0.055d) / 1.055d, 2.4d);
            double sg = (double) g;
            Double.isNaN(sg);
            sg /= 255.0d;
            double sg2 = sg < 0.04045d ? sg / 12.92d : Math.pow((sg + 0.055d) / 1.055d, 2.4d);
            double sb = (double) b;
            Double.isNaN(sb);
            sb /= 255.0d;
            double sb2 = sb < 0.04045d ? sb / 12.92d : Math.pow((0.055d + sb) / 1.055d, 2.4d);
            dArr[0] = (((0.4124d * sr) + (0.3576d * sg2)) + (0.1805d * sb2)) * XYZ_WHITE_REFERENCE_Y;
            dArr[1] = (((0.2126d * sr) + (0.7152d * sg2)) + (0.0722d * sb2)) * XYZ_WHITE_REFERENCE_Y;
            dArr[2] = (((0.0193d * sr) + (0.1192d * sg2)) + (0.9505d * sb2)) * XYZ_WHITE_REFERENCE_Y;
            return;
        }
        int i = r;
        int i2 = g;
        int i3 = b;
        throw new IllegalArgumentException("outXyz must have a length of 3.");
    }

    public static void XYZToLAB(@FloatRange(from = 0.0d, to = 95.047d) double x, @FloatRange(from = 0.0d, to = 100.0d) double y, @FloatRange(from = 0.0d, to = 108.883d) double z, @NonNull double[] outLab) {
        if (outLab.length == 3) {
            x = pivotXyzComponent(x / XYZ_WHITE_REFERENCE_X);
            y = pivotXyzComponent(y / XYZ_WHITE_REFERENCE_Y);
            z = pivotXyzComponent(z / XYZ_WHITE_REFERENCE_Z);
            outLab[0] = Math.max(0.0d, (116.0d * y) - 16.0d);
            outLab[1] = (x - y) * 500.0d;
            outLab[2] = (y - z) * 200.0d;
            return;
        }
        throw new IllegalArgumentException("outLab must have a length of 3.");
    }

    public static void LABToXYZ(@FloatRange(from = 0.0d, to = 100.0d) double l, @FloatRange(from = -128.0d, to = 127.0d) double a, @FloatRange(from = -128.0d, to = 127.0d) double b, @NonNull double[] outXyz) {
        double fy = (l + 16.0d) / 116.0d;
        double fx = (a / 500.0d) + fy;
        double fz = fy - (b / 200.0d);
        double tmp = Math.pow(fx, 3.0d);
        double xr = tmp > XYZ_EPSILON ? tmp : ((fx * 116.0d) - 16.0d) / XYZ_KAPPA;
        double yr = l > 7.9996247999999985d ? Math.pow(fy, 3.0d) : l / XYZ_KAPPA;
        double tmp2 = Math.pow(fz, 3.0d);
        double zr = tmp2 > XYZ_EPSILON ? tmp2 : ((116.0d * fz) - 16.0d) / XYZ_KAPPA;
        outXyz[0] = XYZ_WHITE_REFERENCE_X * xr;
        outXyz[1] = XYZ_WHITE_REFERENCE_Y * yr;
        outXyz[2] = XYZ_WHITE_REFERENCE_Z * zr;
    }

    @ColorInt
    public static int XYZToColor(@FloatRange(from = 0.0d, to = 95.047d) double x, @FloatRange(from = 0.0d, to = 100.0d) double y, @FloatRange(from = 0.0d, to = 108.883d) double z) {
        double r = (((3.2406d * x) + (-1.5372d * y)) + (-0.4986d * z)) / XYZ_WHITE_REFERENCE_Y;
        double g = (((-0.9689d * x) + (1.8758d * y)) + (0.0415d * z)) / XYZ_WHITE_REFERENCE_Y;
        double b = (((0.0557d * x) + (-0.204d * y)) + (1.057d * z)) / XYZ_WHITE_REFERENCE_Y;
        return Color.rgb(constrain((int) Math.round((r > 0.0031308d ? (Math.pow(r, 0.4166666666666667d) * 1.055d) - 0.055d : r * 12.92d) * 255.0d), 0, 255), constrain((int) Math.round((g > 0.0031308d ? (Math.pow(g, 0.4166666666666667d) * 1.055d) - 0.055d : g * 12.92d) * 255.0d), 0, 255), constrain((int) Math.round(255.0d * (b > 0.0031308d ? (Math.pow(b, 0.4166666666666667d) * 1.055d) - 0.055d : b * 12.92d)), 0, 255));
    }

    @ColorInt
    public static int LABToColor(@FloatRange(from = 0.0d, to = 100.0d) double l, @FloatRange(from = -128.0d, to = 127.0d) double a, @FloatRange(from = -128.0d, to = 127.0d) double b) {
        double[] result = getTempDouble3Array();
        LABToXYZ(l, a, b, result);
        return XYZToColor(result[0], result[1], result[2]);
    }

    public static double distanceEuclidean(@NonNull double[] labX, @NonNull double[] labY) {
        return Math.sqrt((Math.pow(labX[0] - labY[0], 2.0d) + Math.pow(labX[1] - labY[1], 2.0d)) + Math.pow(labX[2] - labY[2], 2.0d));
    }

    private static float constrain(float amount, float low, float high) {
        if (amount < low) {
            return low;
        }
        return amount > high ? high : amount;
    }

    private static int constrain(int amount, int low, int high) {
        if (amount < low) {
            return low;
        }
        return amount > high ? high : amount;
    }

    private static double pivotXyzComponent(double component) {
        return component > XYZ_EPSILON ? Math.pow(component, 0.3333333333333333d) : ((XYZ_KAPPA * component) + 16.0d) / 116.0d;
    }

    @ColorInt
    public static int blendARGB(@ColorInt int color1, @ColorInt int color2, @FloatRange(from = 0.0d, to = 1.0d) float ratio) {
        float inverseRatio = 1.0f - ratio;
        return Color.argb((int) ((((float) Color.alpha(color1)) * inverseRatio) + (((float) Color.alpha(color2)) * ratio)), (int) ((((float) Color.red(color1)) * inverseRatio) + (((float) Color.red(color2)) * ratio)), (int) ((((float) Color.green(color1)) * inverseRatio) + (((float) Color.green(color2)) * ratio)), (int) ((((float) Color.blue(color1)) * inverseRatio) + (((float) Color.blue(color2)) * ratio)));
    }

    public static void blendHSL(@NonNull float[] hsl1, @NonNull float[] hsl2, @FloatRange(from = 0.0d, to = 1.0d) float ratio, @NonNull float[] outResult) {
        if (outResult.length == 3) {
            float inverseRatio = 1.0f - ratio;
            outResult[0] = circularInterpolate(hsl1[0], hsl2[0], ratio);
            outResult[1] = (hsl1[1] * inverseRatio) + (hsl2[1] * ratio);
            outResult[2] = (hsl1[2] * inverseRatio) + (hsl2[2] * ratio);
            return;
        }
        throw new IllegalArgumentException("result must have a length of 3.");
    }

    public static void blendLAB(@NonNull double[] lab1, @NonNull double[] lab2, @FloatRange(from = 0.0d, to = 1.0d) double ratio, @NonNull double[] outResult) {
        if (outResult.length == 3) {
            double inverseRatio = 1.0d - ratio;
            outResult[0] = (lab1[0] * inverseRatio) + (lab2[0] * ratio);
            outResult[1] = (lab1[1] * inverseRatio) + (lab2[1] * ratio);
            outResult[2] = (lab1[2] * inverseRatio) + (lab2[2] * ratio);
            return;
        }
        throw new IllegalArgumentException("outResult must have a length of 3.");
    }

    @VisibleForTesting
    static float circularInterpolate(float a, float b, float f) {
        if (Math.abs(b - a) > 180.0f) {
            if (b > a) {
                a += 360.0f;
            } else {
                b += 360.0f;
            }
        }
        return (((b - a) * f) + a) % 360.0f;
    }

    private static double[] getTempDouble3Array() {
        double[] result = (double[]) TEMP_ARRAY.get();
        if (result != null) {
            return result;
        }
        result = new double[3];
        TEMP_ARRAY.set(result);
        return result;
    }
}
