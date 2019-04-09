package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build.VERSION;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.support.v4.graphics.PathParser.PathDataNode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AnimatorInflaterCompat {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;

    private static class PathDataEvaluator implements TypeEvaluator<PathDataNode[]> {
        private PathDataNode[] mNodeArray;

        public android.support.v4.graphics.PathParser.PathDataNode[] evaluate(float r5, android.support.v4.graphics.PathParser.PathDataNode[] r6, android.support.v4.graphics.PathParser.PathDataNode[] r7) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x0033 in {5, 6, 10, 12, 14} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = android.support.v4.graphics.PathParser.canMorph(r6, r7);
            if (r0 == 0) goto L_0x002b;
        L_0x0006:
            r0 = r4.mNodeArray;
            if (r0 == 0) goto L_0x0010;
        L_0x000a:
            r0 = android.support.v4.graphics.PathParser.canMorph(r0, r6);
            if (r0 != 0) goto L_0x0016;
        L_0x0010:
            r0 = android.support.v4.graphics.PathParser.deepCopyNodes(r6);
            r4.mNodeArray = r0;
        L_0x0016:
            r0 = 0;
        L_0x0017:
            r1 = r6.length;
            if (r0 >= r1) goto L_0x0028;
        L_0x001a:
            r1 = r4.mNodeArray;
            r1 = r1[r0];
            r2 = r6[r0];
            r3 = r7[r0];
            r1.interpolatePathDataNode(r2, r3, r5);
            r0 = r0 + 1;
            goto L_0x0017;
        L_0x0028:
            r0 = r4.mNodeArray;
            return r0;
        L_0x002b:
            r0 = new java.lang.IllegalArgumentException;
            r1 = "Can't interpolate between two incompatible pathData";
            r0.<init>(r1);
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatorInflaterCompat.PathDataEvaluator.evaluate(float, android.support.v4.graphics.PathParser$PathDataNode[], android.support.v4.graphics.PathParser$PathDataNode[]):android.support.v4.graphics.PathParser$PathDataNode[]");
        }

        PathDataEvaluator() {
        }

        PathDataEvaluator(PathDataNode[] nodeArray) {
            this.mNodeArray = nodeArray;
        }
    }

    public static Animator loadAnimator(Context context, @AnimatorRes int id) throws NotFoundException {
        if (VERSION.SDK_INT >= 24) {
            return AnimatorInflater.loadAnimator(context, id);
        }
        return loadAnimator(context, context.getResources(), context.getTheme(), id);
    }

    public static Animator loadAnimator(Context context, Resources resources, Theme theme, @AnimatorRes int id) throws NotFoundException {
        return loadAnimator(context, resources, theme, id, 1.0f);
    }

    public static Animator loadAnimator(Context context, Resources resources, Theme theme, @AnimatorRes int id, float pathErrorScale) throws NotFoundException {
        StringBuilder stringBuilder;
        NotFoundException rnf;
        XmlResourceParser parser = null;
        try {
            parser = resources.getAnimation(id);
            Animator animator = createAnimatorFromXml(context, resources, theme, parser, pathErrorScale);
            if (parser != null) {
                parser.close();
            }
            return animator;
        } catch (XmlPullParserException ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Can't load animation resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Can't load animation resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static PropertyValuesHolder getPVH(TypedArray styledAttributes, int valueType, int valueFromId, int valueToId, String propertyName) {
        int valueType2;
        PropertyValuesHolder returnValue;
        TypedArray typedArray = styledAttributes;
        int i = valueFromId;
        int i2 = valueToId;
        String str = propertyName;
        TypedValue tvFrom = typedArray.peekValue(i);
        boolean hasFrom = tvFrom != null;
        int fromType = hasFrom ? tvFrom.type : 0;
        TypedValue tvTo = typedArray.peekValue(i2);
        boolean hasTo = tvTo != null;
        int toType = hasTo ? tvTo.type : 0;
        int i3 = valueType;
        if (i3 != 4) {
            valueType2 = i3;
        } else if ((hasFrom && isColorType(fromType)) || (hasTo && isColorType(toType))) {
            valueType2 = 3;
        } else {
            valueType2 = 0;
        }
        boolean getFloats = valueType2 == 0;
        TypedValue typedValue;
        int i4;
        PropertyValuesHolder returnValue2;
        if (valueType2 == 2) {
            String fromString = typedArray.getString(i);
            String toString = typedArray.getString(i2);
            PathDataNode[] nodesFrom = PathParser.createNodesFromPathData(fromString);
            tvFrom = PathParser.createNodesFromPathData(toString);
            if (nodesFrom == null) {
                if (tvFrom == null) {
                    typedValue = tvTo;
                    i4 = toType;
                    returnValue2 = null;
                    returnValue = returnValue2;
                    tvTo = i4;
                    toType = valueToId;
                }
            }
            if (nodesFrom != null) {
                TypeEvaluator evaluator = new PathDataEvaluator();
                TypeEvaluator evaluator2;
                if (tvFrom == null) {
                    returnValue2 = null;
                    evaluator2 = evaluator;
                    i4 = toType;
                    returnValue = PropertyValuesHolder.ofObject(str, evaluator2, new Object[]{nodesFrom});
                } else if (PathParser.canMorph(nodesFrom, tvFrom)) {
                    returnValue2 = null;
                    returnValue = PropertyValuesHolder.ofObject(str, evaluator, new Object[]{nodesFrom, tvFrom});
                    i4 = toType;
                } else {
                    returnValue2 = null;
                    evaluator2 = evaluator;
                    StringBuilder stringBuilder = new StringBuilder();
                    evaluator = toType;
                    stringBuilder.append(" Can't morph from ");
                    stringBuilder.append(fromString);
                    stringBuilder.append(" to ");
                    stringBuilder.append(toString);
                    throw new InflateException(stringBuilder.toString());
                }
            }
            i4 = toType;
            returnValue2 = null;
            if (tvFrom != null) {
                returnValue = PropertyValuesHolder.ofObject(str, new PathDataEvaluator(), new Object[]{tvFrom});
            }
            returnValue = returnValue2;
            tvTo = i4;
            toType = valueToId;
        } else {
            typedValue = tvTo;
            i4 = toType;
            returnValue2 = null;
            TypeEvaluator evaluator3 = null;
            if (valueType2 == 3) {
                evaluator3 = ArgbEvaluator.getInstance();
            }
            if (!getFloats) {
                int toType2 = i4;
                toType = valueToId;
                int valueTo;
                int i5;
                if (hasFrom) {
                    int valueFrom;
                    if (fromType == 5) {
                        valueFrom = (int) typedArray.getDimension(i, 0.0f);
                    } else if (isColorType(fromType)) {
                        valueFrom = typedArray.getColor(i, 0);
                    } else {
                        valueFrom = typedArray.getInt(i, 0);
                    }
                    if (hasTo) {
                        if (toType2 == 5) {
                            valueTo = (int) typedArray.getDimension(toType, 0.0f);
                            i5 = 0;
                        } else if (isColorType(toType2)) {
                            i5 = 0;
                            valueTo = typedArray.getColor(toType, 0);
                        } else {
                            i5 = 0;
                            valueTo = typedArray.getInt(toType, 0);
                        }
                        returnValue = PropertyValuesHolder.ofInt(str, new int[]{valueFrom, valueTo});
                    } else {
                        returnValue = PropertyValuesHolder.ofInt(str, new int[]{valueFrom});
                    }
                } else if (hasTo) {
                    if (toType2 == 5) {
                        valueTo = (int) typedArray.getDimension(toType, 0.0f);
                        i5 = 0;
                    } else if (isColorType(toType2)) {
                        i5 = 0;
                        valueTo = typedArray.getColor(toType, 0);
                    } else {
                        i5 = 0;
                        valueTo = typedArray.getInt(toType, 0);
                    }
                    returnValue = PropertyValuesHolder.ofInt(str, new int[]{valueTo});
                } else {
                    returnValue = returnValue2;
                }
            } else if (hasFrom) {
                float valueFrom2;
                if (fromType == 5) {
                    valueFrom2 = typedArray.getDimension(i, 0.0f);
                } else {
                    valueFrom2 = typedArray.getFloat(i, 0.0f);
                }
                if (hasTo) {
                    if (i4 == 5) {
                        valueTo = typedArray.getDimension(valueToId, 0.0f);
                    } else {
                        valueTo = typedArray.getFloat(valueToId, 0.0f);
                    }
                    returnValue = PropertyValuesHolder.ofFloat(str, new float[]{valueFrom2, valueTo});
                } else {
                    toType = valueToId;
                    returnValue = PropertyValuesHolder.ofFloat(str, new float[]{valueFrom2});
                }
            } else {
                toType = valueToId;
                if (i4 == 5) {
                    valueTo = typedArray.getDimension(toType, 0.0f);
                } else {
                    valueTo = typedArray.getFloat(toType, 0.0f);
                }
                returnValue = PropertyValuesHolder.ofFloat(str, new float[]{valueTo});
            }
            if (!(returnValue == null || evaluator3 == null)) {
                returnValue.setEvaluator(evaluator3);
            }
        }
        return returnValue;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator anim, TypedArray arrayAnimator, TypedArray arrayObjectAnimator, float pixelSize, XmlPullParser parser) {
        long duration = (long) TypedArrayUtils.getNamedInt(arrayAnimator, parser, "duration", 1, 300);
        long startDelay = (long) TypedArrayUtils.getNamedInt(arrayAnimator, parser, "startOffset", 2, 0);
        int valueType = TypedArrayUtils.getNamedInt(arrayAnimator, parser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(parser, "valueFrom") && TypedArrayUtils.hasAttribute(parser, "valueTo")) {
            if (valueType == 4) {
                valueType = inferValueTypeFromValues(arrayAnimator, 5, 6);
            }
            if (getPVH(arrayAnimator, valueType, 5, 6, "") != null) {
                anim.setValues(new PropertyValuesHolder[]{getPVH(arrayAnimator, valueType, 5, 6, "")});
            }
        }
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        anim.setRepeatCount(TypedArrayUtils.getNamedInt(arrayAnimator, parser, "repeatCount", 3, 0));
        anim.setRepeatMode(TypedArrayUtils.getNamedInt(arrayAnimator, parser, "repeatMode", 4, 1));
        if (arrayObjectAnimator != null) {
            setupObjectAnimator(anim, arrayObjectAnimator, valueType, pixelSize, parser);
        }
    }

    private static void setupObjectAnimator(ValueAnimator anim, TypedArray arrayObjectAnimator, int valueType, float pixelSize, XmlPullParser parser) {
        ObjectAnimator oa = (ObjectAnimator) anim;
        String pathData = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "pathData", 1);
        if (pathData != null) {
            String propertyXName = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyXName", 2);
            String propertyYName = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyYName", 3);
            if (valueType == 2 || valueType == 4) {
            }
            if (propertyXName == null) {
                if (propertyYName == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(arrayObjectAnimator.getPositionDescription());
                    stringBuilder.append(" propertyXName or propertyYName is needed for PathData");
                    throw new InflateException(stringBuilder.toString());
                }
            }
            setupPathMotion(PathParser.createPathFromPathData(pathData), oa, 0.5f * pixelSize, propertyXName, propertyYName);
            return;
        }
        oa.setPropertyName(TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyName", 0));
    }

    private static void setupPathMotion(Path path, ObjectAnimator oa, float precision, String propertyXName, String propertyYName) {
        Path path2 = path;
        ObjectAnimator objectAnimator = oa;
        String str = propertyXName;
        String str2 = propertyYName;
        boolean z = false;
        PathMeasure measureForTotalLength = new PathMeasure(path2, false);
        float totalLength = 0.0f;
        ArrayList<Float> contourLengths = new ArrayList();
        contourLengths.add(Float.valueOf(0.0f));
        while (true) {
            totalLength += measureForTotalLength.getLength();
            contourLengths.add(Float.valueOf(totalLength));
            if (!measureForTotalLength.nextContour()) {
                break;
            }
            path2 = path;
            z = false;
        }
        PathMeasure pathMeasure = new PathMeasure(path2, z);
        int numPoints = Math.min(100, ((int) (totalLength / precision)) + 1);
        float[] mX = new float[numPoints];
        float[] mY = new float[numPoints];
        float[] position = new float[2];
        float step = totalLength / ((float) (numPoints - 1));
        float currentDistance = 0.0f;
        int contourIndex = 0;
        int i = 0;
        while (i < numPoints) {
            pathMeasure.getPosTan(currentDistance - ((Float) contourLengths.get(contourIndex)).floatValue(), position, null);
            mX[i] = position[0];
            mY[i] = position[1];
            currentDistance += step;
            if (contourIndex + 1 < contourLengths.size() && currentDistance > ((Float) contourLengths.get(contourIndex + 1)).floatValue()) {
                contourIndex++;
                pathMeasure.nextContour();
            }
            i++;
            path2 = path;
        }
        PropertyValuesHolder x = null;
        PropertyValuesHolder y = null;
        if (str != null) {
            x = PropertyValuesHolder.ofFloat(str, mX);
        }
        if (str2 != null) {
            y = PropertyValuesHolder.ofFloat(str2, mY);
        }
        if (x == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{y});
        } else if (y == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{x});
        } else {
            objectAnimator.setValues(new PropertyValuesHolder[]{x, y});
        }
    }

    private static Animator createAnimatorFromXml(Context context, Resources res, Theme theme, XmlPullParser parser, float pixelSize) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, res, theme, parser, Xml.asAttributeSet(parser), null, 0, pixelSize);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.animation.Animator createAnimatorFromXml(android.content.Context r20, android.content.res.Resources r21, android.content.res.Resources.Theme r22, org.xmlpull.v1.XmlPullParser r23, android.util.AttributeSet r24, android.animation.AnimatorSet r25, int r26, float r27) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r8 = r21;
        r9 = r22;
        r10 = r23;
        r11 = r25;
        r0 = 0;
        r1 = 0;
        r12 = r23.getDepth();
        r7 = r0;
        r13 = r1;
    L_0x0010:
        r0 = r23.next();
        r14 = r0;
        r1 = 3;
        if (r0 != r1) goto L_0x0023;
    L_0x0018:
        r0 = r23.getDepth();
        if (r0 <= r12) goto L_0x001f;
    L_0x001e:
        goto L_0x0023;
    L_0x001f:
        r1 = r20;
        goto L_0x00fb;
    L_0x0023:
        r0 = 1;
        if (r14 == r0) goto L_0x00f9;
    L_0x0026:
        r0 = 2;
        if (r14 == r0) goto L_0x002a;
    L_0x0029:
        goto L_0x0010;
    L_0x002a:
        r15 = r23.getName();
        r16 = 0;
        r0 = "objectAnimator";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x004d;
    L_0x0038:
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = r24;
        r4 = r27;
        r5 = r23;
        r0 = loadObjectAnimator(r0, r1, r2, r3, r4, r5);
        r1 = r20;
        r7 = r0;
        goto L_0x00cb;
    L_0x004d:
        r0 = "animator";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x006a;
    L_0x0055:
        r4 = 0;
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = r24;
        r5 = r27;
        r6 = r23;
        r0 = loadAnimator(r0, r1, r2, r3, r4, r5, r6);
        r1 = r20;
        r7 = r0;
        goto L_0x00cb;
    L_0x006a:
        r0 = "set";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x00a7;
    L_0x0072:
        r0 = new android.animation.AnimatorSet;
        r0.<init>();
        r17 = r0;
        r0 = android.support.graphics.drawable.AndroidResources.STYLEABLE_ANIMATOR_SET;
        r7 = r24;
        r6 = android.support.v4.content.res.TypedArrayUtils.obtainAttributes(r8, r9, r7, r0);
        r0 = "ordering";
        r1 = 0;
        r18 = android.support.v4.content.res.TypedArrayUtils.getNamedInt(r6, r10, r0, r1, r1);
        r5 = r17;
        r5 = (android.animation.AnimatorSet) r5;
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = r23;
        r4 = r24;
        r19 = r6;
        r6 = r18;
        r7 = r27;
        createAnimatorFromXml(r0, r1, r2, r3, r4, r5, r6, r7);
        r19.recycle();
        r1 = r20;
        r7 = r17;
        goto L_0x00cb;
    L_0x00a7:
        r0 = "propertyValuesHolder";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x00dc;
        r0 = android.util.Xml.asAttributeSet(r23);
        r1 = r20;
        r0 = loadValues(r1, r8, r9, r10, r0);
        if (r0 == 0) goto L_0x00c8;
    L_0x00bc:
        if (r7 == 0) goto L_0x00c8;
    L_0x00be:
        r2 = r7 instanceof android.animation.ValueAnimator;
        if (r2 == 0) goto L_0x00c8;
    L_0x00c2:
        r2 = r7;
        r2 = (android.animation.ValueAnimator) r2;
        r2.setValues(r0);
    L_0x00c8:
        r16 = 1;
    L_0x00cb:
        if (r11 == 0) goto L_0x00da;
    L_0x00cd:
        if (r16 != 0) goto L_0x00da;
    L_0x00cf:
        if (r13 != 0) goto L_0x00d7;
    L_0x00d1:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r13 = r0;
    L_0x00d7:
        r13.add(r7);
    L_0x00da:
        goto L_0x0010;
    L_0x00dc:
        r1 = r20;
        r0 = new java.lang.RuntimeException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unknown animator name: ";
        r2.append(r3);
        r3 = r23.getName();
        r2.append(r3);
        r2 = r2.toString();
        r0.<init>(r2);
        throw r0;
    L_0x00f9:
        r1 = r20;
    L_0x00fb:
        if (r11 == 0) goto L_0x0125;
    L_0x00fd:
        if (r13 == 0) goto L_0x0125;
    L_0x00ff:
        r0 = r13.size();
        r0 = new android.animation.Animator[r0];
        r2 = 0;
        r3 = r13.iterator();
    L_0x010a:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x011c;
    L_0x0110:
        r4 = r3.next();
        r4 = (android.animation.Animator) r4;
        r5 = r2 + 1;
        r0[r2] = r4;
        r2 = r5;
        goto L_0x010a;
    L_0x011c:
        if (r26 != 0) goto L_0x0122;
    L_0x011e:
        r11.playTogether(r0);
        goto L_0x0125;
    L_0x0122:
        r11.playSequentially(r0);
    L_0x0125:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatorInflaterCompat.createAnimatorFromXml(android.content.Context, android.content.res.Resources, android.content.res.Resources$Theme, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.animation.AnimatorSet, int, float):android.animation.Animator");
    }

    private static PropertyValuesHolder[] loadValues(Context context, Resources res, Theme theme, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        Resources resources;
        Theme theme2;
        AttributeSet attributeSet;
        PropertyValuesHolder[] valuesArray;
        XmlPullParser xmlPullParser = parser;
        ArrayList<PropertyValuesHolder> values = null;
        while (true) {
            int eventType = parser.getEventType();
            int type = eventType;
            if (eventType == 3 || type == 1) {
                resources = res;
                theme2 = theme;
                attributeSet = attrs;
                valuesArray = null;
            } else if (type != 2) {
                parser.next();
            } else {
                if (parser.getName().equals("propertyValuesHolder")) {
                    TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                    String propertyName = TypedArrayUtils.getNamedString(a, xmlPullParser, "propertyName", 3);
                    int valueType = TypedArrayUtils.getNamedInt(a, xmlPullParser, "valueType", 2, 4);
                    int valueType2 = valueType;
                    PropertyValuesHolder pvh = loadPvh(context, res, theme, parser, propertyName, valueType);
                    if (pvh == null) {
                        pvh = getPVH(a, valueType2, 0, 1, propertyName);
                    }
                    if (pvh != null) {
                        if (values == null) {
                            values = new ArrayList();
                        }
                        values.add(pvh);
                    }
                    a.recycle();
                } else {
                    resources = res;
                    theme2 = theme;
                    attributeSet = attrs;
                }
                parser.next();
            }
        }
        resources = res;
        theme2 = theme;
        attributeSet = attrs;
        valuesArray = null;
        if (values != null) {
            int count = values.size();
            valuesArray = new PropertyValuesHolder[count];
            for (int i = 0; i < count; i++) {
                valuesArray[i] = (PropertyValuesHolder) values.get(i);
            }
        }
        return valuesArray;
    }

    private static int inferValueTypeOfKeyframe(Resources res, Theme theme, AttributeSet attrs, XmlPullParser parser) {
        int valueType;
        TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_KEYFRAME);
        boolean hasValue = false;
        TypedValue keyframeValue = TypedArrayUtils.peekNamedValue(a, parser, "value", 0);
        if (keyframeValue != null) {
            hasValue = true;
        }
        if (hasValue && isColorType(keyframeValue.type)) {
            valueType = 3;
        } else {
            valueType = 0;
        }
        a.recycle();
        return valueType;
    }

    private static int inferValueTypeFromValues(TypedArray styledAttributes, int valueFromId, int valueToId) {
        TypedValue tvFrom = styledAttributes.peekValue(valueFromId);
        boolean hasTo = true;
        int toType = 0;
        boolean hasFrom = tvFrom != null;
        int fromType = hasFrom ? tvFrom.type : 0;
        TypedValue tvTo = styledAttributes.peekValue(valueToId);
        if (tvTo == null) {
            hasTo = false;
        }
        if (hasTo) {
            toType = tvTo.type;
        }
        if ((hasFrom && isColorType(fromType)) || (hasTo && isColorType(toType))) {
            return 3;
        }
        return 0;
    }

    private static void dumpKeyframes(Object[] keyframes, String header) {
        if (keyframes != null) {
            if (keyframes.length != 0) {
                Log.d(TAG, header);
                int count = keyframes.length;
                for (int i = 0; i < count; i++) {
                    Keyframe keyframe = keyframes[i];
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Keyframe ");
                    stringBuilder.append(i);
                    stringBuilder.append(": fraction ");
                    stringBuilder.append(keyframe.getFraction() < 0.0f ? "null" : Float.valueOf(keyframe.getFraction()));
                    stringBuilder.append(", ");
                    stringBuilder.append(", value : ");
                    stringBuilder.append(keyframe.hasValue() ? keyframe.getValue() : "null");
                    Log.d(str, stringBuilder.toString());
                }
            }
        }
    }

    private static PropertyValuesHolder loadPvh(Context context, Resources res, Theme theme, XmlPullParser parser, String propertyName, int valueType) throws XmlPullParserException, IOException {
        int type;
        Resources resources;
        Theme theme2;
        XmlPullParser xmlPullParser;
        ArrayList<Keyframe> keyframes;
        int type2;
        PropertyValuesHolder value = null;
        ArrayList<Keyframe> keyframes2 = null;
        int valueType2 = valueType;
        while (true) {
            int next = parser.next();
            type = next;
            if (next == 3 || type == 1) {
                resources = res;
                theme2 = theme;
                xmlPullParser = parser;
            } else if (parser.getName().equals("keyframe")) {
                if (valueType2 == 4) {
                    valueType2 = inferValueTypeOfKeyframe(res, theme, Xml.asAttributeSet(parser), parser);
                } else {
                    resources = res;
                    theme2 = theme;
                    xmlPullParser = parser;
                }
                Keyframe keyframe = loadKeyframe(context, res, theme, Xml.asAttributeSet(parser), valueType2, parser);
                if (keyframe != null) {
                    if (keyframes2 == null) {
                        keyframes2 = new ArrayList();
                    }
                    keyframes2.add(keyframe);
                }
                parser.next();
            } else {
                resources = res;
                theme2 = theme;
                xmlPullParser = parser;
            }
        }
        resources = res;
        theme2 = theme;
        xmlPullParser = parser;
        if (keyframes2 != null) {
            next = keyframes2.size();
            int count = next;
            if (next > 0) {
                Keyframe firstKeyframe = (Keyframe) keyframes2.get(0);
                Keyframe lastKeyframe = (Keyframe) keyframes2.get(count - 1);
                float endFraction = lastKeyframe.getFraction();
                float f = 0.0f;
                if (endFraction < 1.0f) {
                    if (endFraction < 0.0f) {
                        lastKeyframe.setFraction(1.0f);
                    } else {
                        keyframes2.add(keyframes2.size(), createNewKeyframe(lastKeyframe, 1.0f));
                        count++;
                    }
                }
                float startFraction = firstKeyframe.getFraction();
                if (startFraction != 0.0f) {
                    if (startFraction < 0.0f) {
                        firstKeyframe.setFraction(0.0f);
                    } else {
                        keyframes2.add(0, createNewKeyframe(firstKeyframe, 0.0f));
                        count++;
                    }
                }
                Keyframe[] keyframeArray = new Keyframe[count];
                keyframes2.toArray(keyframeArray);
                int i = 0;
                while (i < count) {
                    PropertyValuesHolder value2;
                    Keyframe keyframe2 = keyframeArray[i];
                    if (keyframe2.getFraction() >= f) {
                        value2 = value;
                        keyframes = keyframes2;
                        type2 = type;
                    } else if (i == 0) {
                        keyframe2.setFraction(f);
                        value2 = value;
                        keyframes = keyframes2;
                        type2 = type;
                    } else if (i == count - 1) {
                        keyframe2.setFraction(1.0f);
                        value2 = value;
                        keyframes = keyframes2;
                        type2 = type;
                    } else {
                        int startIndex = i;
                        value2 = value;
                        value = startIndex + 1;
                        keyframes = keyframes2;
                        keyframes2 = i;
                        while (true) {
                            type2 = type;
                            if (value >= count - 1) {
                                break;
                            } else if (keyframeArray[value].getFraction() >= 0.0f) {
                                break;
                            } else {
                                Object keyframes3 = value;
                                value++;
                                type = type2;
                            }
                            distributeKeyframes(keyframeArray, keyframeArray[keyframes2 + 1].getFraction() - keyframeArray[startIndex - 1].getFraction(), startIndex, keyframes2);
                        }
                        distributeKeyframes(keyframeArray, keyframeArray[keyframes2 + 1].getFraction() - keyframeArray[startIndex - 1].getFraction(), startIndex, keyframes2);
                    }
                    i++;
                    keyframes2 = keyframes;
                    type = type2;
                    value = value2;
                    f = 0.0f;
                }
                keyframes = keyframes2;
                type2 = type;
                PropertyValuesHolder value3 = PropertyValuesHolder.ofKeyframe(propertyName, keyframeArray);
                if (valueType2 != 3) {
                    return value3;
                }
                value3.setEvaluator(ArgbEvaluator.getInstance());
                return value3;
            }
        }
        keyframes = keyframes2;
        type2 = type;
        String value4 = propertyName;
        return null;
    }

    private static Keyframe createNewKeyframe(Keyframe sampleKeyframe, float fraction) {
        if (sampleKeyframe.getType() == Float.TYPE) {
            return Keyframe.ofFloat(fraction);
        }
        if (sampleKeyframe.getType() == Integer.TYPE) {
            return Keyframe.ofInt(fraction);
        }
        return Keyframe.ofObject(fraction);
    }

    private static void distributeKeyframes(Keyframe[] keyframes, float gap, int startIndex, int endIndex) {
        float increment = gap / ((float) ((endIndex - startIndex) + 2));
        for (int i = startIndex; i <= endIndex; i++) {
            keyframes[i].setFraction(keyframes[i - 1].getFraction() + increment);
        }
    }

    private static Keyframe loadKeyframe(Context context, Resources res, Theme theme, AttributeSet attrs, int valueType, XmlPullParser parser) throws XmlPullParserException, IOException {
        TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_KEYFRAME);
        Keyframe keyframe = null;
        float fraction = TypedArrayUtils.getNamedFloat(a, parser, "fraction", 3, -1.0f);
        TypedValue keyframeValue = TypedArrayUtils.peekNamedValue(a, parser, "value", 0);
        boolean hasValue = keyframeValue != null;
        if (valueType == 4) {
            if (hasValue && isColorType(keyframeValue.type)) {
                valueType = 3;
            } else {
                valueType = 0;
            }
        }
        if (hasValue) {
            if (valueType != 3) {
                switch (valueType) {
                    case 0:
                        keyframe = Keyframe.ofFloat(fraction, TypedArrayUtils.getNamedFloat(a, parser, "value", 0, 0.0f));
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
            keyframe = Keyframe.ofInt(fraction, TypedArrayUtils.getNamedInt(a, parser, "value", 0, 0));
        } else {
            Keyframe ofFloat;
            if (valueType == 0) {
                ofFloat = Keyframe.ofFloat(fraction);
            } else {
                ofFloat = Keyframe.ofInt(fraction);
            }
            keyframe = ofFloat;
        }
        int resID = TypedArrayUtils.getNamedResourceId(a, parser, "interpolator", 1, 0);
        if (resID > 0) {
            keyframe.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, resID));
        }
        a.recycle();
        return keyframe;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, Resources res, Theme theme, AttributeSet attrs, float pathErrorScale, XmlPullParser parser) throws NotFoundException {
        ValueAnimator anim = new ObjectAnimator();
        loadAnimator(context, res, theme, attrs, anim, pathErrorScale, parser);
        return anim;
    }

    private static ValueAnimator loadAnimator(Context context, Resources res, Theme theme, AttributeSet attrs, ValueAnimator anim, float pathErrorScale, XmlPullParser parser) throws NotFoundException {
        TypedArray arrayAnimator = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_ANIMATOR);
        TypedArray arrayObjectAnimator = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        if (anim == null) {
            anim = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(anim, arrayAnimator, arrayObjectAnimator, pathErrorScale, parser);
        int resID = TypedArrayUtils.getNamedResourceId(arrayAnimator, parser, "interpolator", 0, 0);
        if (resID > 0) {
            anim.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, resID));
        }
        arrayAnimator.recycle();
        if (arrayObjectAnimator != null) {
            arrayObjectAnimator.recycle();
        }
        return anim;
    }

    private static boolean isColorType(int type) {
        return type >= 28 && type <= 31;
    }

    private AnimatorInflaterCompat() {
    }
}
