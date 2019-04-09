package android.support.v4.graphics;

import android.graphics.Path;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.util.ArrayList;

@RestrictTo({Scope.LIBRARY_GROUP})
public class PathParser {
    private static final String LOGTAG = "PathParser";

    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegOrDot;

        ExtractFloatResult() {
        }
    }

    public static class PathDataNode {
        @RestrictTo({Scope.LIBRARY_GROUP})
        public float[] mParams;
        @RestrictTo({Scope.LIBRARY_GROUP})
        public char mType;

        PathDataNode(char type, float[] params) {
            this.mType = type;
            this.mParams = params;
        }

        PathDataNode(PathDataNode n) {
            this.mType = n.mType;
            float[] fArr = n.mParams;
            this.mParams = PathParser.copyOfRange(fArr, 0, fArr.length);
        }

        public static void nodesToPath(PathDataNode[] node, Path path) {
            float[] current = new float[6];
            char previousCommand = 'm';
            for (int i = 0; i < node.length; i++) {
                addCommand(path, current, previousCommand, node[i].mType, node[i].mParams);
                previousCommand = node[i].mType;
            }
        }

        public void interpolatePathDataNode(PathDataNode nodeFrom, PathDataNode nodeTo, float fraction) {
            int i = 0;
            while (true) {
                float[] fArr = nodeFrom.mParams;
                if (i < fArr.length) {
                    this.mParams[i] = (fArr[i] * (1.0f - fraction)) + (nodeTo.mParams[i] * fraction);
                    i++;
                } else {
                    return;
                }
            }
        }

        private static void addCommand(Path path, float[] current, char previousCmd, char cmd, float[] val) {
            int incr;
            int k;
            float f;
            char previousCmd2;
            Path path2 = path;
            float[] fArr = val;
            float currentX = current[0];
            float currentY = current[1];
            float ctrlPointX = current[2];
            float ctrlPointY = current[3];
            float currentSegmentStartX = current[4];
            float currentSegmentStartY = current[5];
            switch (cmd) {
                case 'A':
                case 'a':
                    incr = 7;
                    break;
                case 'C':
                case 'c':
                    incr = 6;
                    break;
                case 'H':
                case 'V':
                case 'h':
                case 'v':
                    incr = 1;
                    break;
                case 'L':
                case 'M':
                case 'T':
                case 'l':
                case 'm':
                case 't':
                    incr = 2;
                    break;
                case 'Q':
                case 'S':
                case 'q':
                case 's':
                    incr = 4;
                    break;
                case 'Z':
                case 'z':
                    path.close();
                    currentX = currentSegmentStartX;
                    currentY = currentSegmentStartY;
                    ctrlPointX = currentSegmentStartX;
                    ctrlPointY = currentSegmentStartY;
                    path2.moveTo(currentX, currentY);
                    incr = 2;
                    break;
                default:
                    incr = 2;
                    break;
            }
            int k2 = 0;
            float currentX2 = currentX;
            float currentY2 = currentY;
            float ctrlPointX2 = ctrlPointX;
            float ctrlPointY2 = ctrlPointY;
            float currentSegmentStartX2 = currentSegmentStartX;
            float currentSegmentStartY2 = currentSegmentStartY;
            char previousCmd3 = previousCmd;
            while (k2 < fArr.length) {
                float f2;
                float ctrlPointX3;
                switch (cmd) {
                    case 'A':
                        k = k2;
                        drawArc(path, currentX2, currentY2, fArr[k + 5], fArr[k + 6], fArr[k + 0], fArr[k + 1], fArr[k + 2], fArr[k + 3] != 0.0f, fArr[k + 4] != 0.0f);
                        currentX2 = fArr[k + 5];
                        currentY2 = fArr[k + 6];
                        ctrlPointX2 = currentX2;
                        ctrlPointY2 = currentY2;
                        break;
                    case 'C':
                        f = currentY2;
                        f2 = currentX2;
                        k = k2;
                        path.cubicTo(fArr[k + 0], fArr[k + 1], fArr[k + 2], fArr[k + 3], fArr[k + 4], fArr[k + 5]);
                        currentX2 = fArr[k + 4];
                        currentY2 = fArr[k + 5];
                        ctrlPointX2 = fArr[k + 2];
                        ctrlPointY2 = fArr[k + 3];
                        break;
                    case 'H':
                        f2 = currentX2;
                        k = k2;
                        path2.lineTo(fArr[k + 0], currentY2);
                        currentX2 = fArr[k + 0];
                        break;
                    case 'L':
                        f = currentY2;
                        f2 = currentX2;
                        k = k2;
                        path2.lineTo(fArr[k + 0], fArr[k + 1]);
                        currentX2 = fArr[k + 0];
                        currentY2 = fArr[k + 1];
                        break;
                    case 'M':
                        f = currentY2;
                        f2 = currentX2;
                        k = k2;
                        currentX2 = fArr[k + 0];
                        currentY2 = fArr[k + 1];
                        if (k <= 0) {
                            path2.moveTo(fArr[k + 0], fArr[k + 1]);
                            currentSegmentStartX2 = currentX2;
                            currentSegmentStartY2 = currentY2;
                            break;
                        }
                        path2.lineTo(fArr[k + 0], fArr[k + 1]);
                        break;
                    case 'Q':
                        f = currentY2;
                        f2 = currentX2;
                        k = k2;
                        path2.quadTo(fArr[k + 0], fArr[k + 1], fArr[k + 2], fArr[k + 3]);
                        ctrlPointX3 = fArr[k + 0];
                        currentX = fArr[k + 1];
                        currentX2 = fArr[k + 2];
                        currentY2 = fArr[k + 3];
                        ctrlPointX2 = ctrlPointX3;
                        ctrlPointY2 = currentX;
                        break;
                    case 'S':
                        previousCmd2 = previousCmd3;
                        f = currentY2;
                        f2 = currentX2;
                        k = k2;
                        ctrlPointX3 = f2;
                        currentY = f;
                        if (!(previousCmd2 == 'c' || previousCmd2 == 's' || previousCmd2 == 'C')) {
                            if (previousCmd2 != 'S') {
                                currentX2 = ctrlPointX3;
                                currentY2 = currentY;
                                path.cubicTo(currentX2, currentY2, fArr[k + 0], fArr[k + 1], fArr[k + 2], fArr[k + 3]);
                                ctrlPointX2 = fArr[k + 0];
                                ctrlPointY2 = fArr[k + 1];
                                currentX2 = fArr[k + 2];
                                currentY2 = fArr[k + 3];
                                break;
                            }
                        }
                        currentX2 = (f2 * 2.0f) - ctrlPointX2;
                        currentY2 = (f * 2.0f) - ctrlPointY2;
                        path.cubicTo(currentX2, currentY2, fArr[k + 0], fArr[k + 1], fArr[k + 2], fArr[k + 3]);
                        ctrlPointX2 = fArr[k + 0];
                        ctrlPointY2 = fArr[k + 1];
                        currentX2 = fArr[k + 2];
                        currentY2 = fArr[k + 3];
                    case 'T':
                        previousCmd2 = previousCmd3;
                        f = currentY2;
                        f2 = currentX2;
                        k = k2;
                        currentX = f2;
                        ctrlPointX = f;
                        if (previousCmd2 == 'q' || previousCmd2 == 't' || previousCmd2 == 'Q' || previousCmd2 == 'T') {
                            currentX = (f2 * 2.0f) - ctrlPointX2;
                            ctrlPointX = (f * 2.0f) - ctrlPointY2;
                        }
                        path2.quadTo(currentX, ctrlPointX, fArr[k + 0], fArr[k + 1]);
                        ctrlPointX3 = currentX;
                        currentY = ctrlPointX;
                        currentX2 = fArr[k + 0];
                        currentY2 = fArr[k + 1];
                        ctrlPointX2 = ctrlPointX3;
                        ctrlPointY2 = currentY;
                        break;
                    case 'V':
                        f = currentY2;
                        k = k2;
                        path2.lineTo(currentX2, fArr[k + 0]);
                        currentY2 = fArr[k + 0];
                        break;
                    case 'a':
                        ctrlPointX = fArr[k2 + 5] + currentX2;
                        f = currentY2;
                        f2 = currentX2;
                        boolean z = fArr[k2 + 3] != 0.0f;
                        k = k2;
                        drawArc(path, currentX2, currentY2, ctrlPointX, fArr[k2 + 6] + currentY2, fArr[k2 + 0], fArr[k2 + 1], fArr[k2 + 2], z, fArr[k2 + 4] != 0.0f);
                        currentX2 = f2 + fArr[k + 5];
                        currentY2 = f + fArr[k + 6];
                        ctrlPointX2 = currentX2;
                        ctrlPointY2 = currentY2;
                        break;
                    case 'c':
                        path.rCubicTo(fArr[k2 + 0], fArr[k2 + 1], fArr[k2 + 2], fArr[k2 + 3], fArr[k2 + 4], fArr[k2 + 5]);
                        ctrlPointX3 = fArr[k2 + 2] + currentX2;
                        currentX = fArr[k2 + 3] + currentY2;
                        currentX2 += fArr[k2 + 4];
                        currentY2 += fArr[k2 + 5];
                        ctrlPointX2 = ctrlPointX3;
                        ctrlPointY2 = currentX;
                        k = k2;
                        break;
                    case 'h':
                        path2.rLineTo(fArr[k2 + 0], 0.0f);
                        currentX2 += fArr[k2 + 0];
                        k = k2;
                        break;
                    case 'l':
                        path2.rLineTo(fArr[k2 + 0], fArr[k2 + 1]);
                        currentX2 += fArr[k2 + 0];
                        currentY2 += fArr[k2 + 1];
                        k = k2;
                        break;
                    case 'm':
                        currentX2 += fArr[k2 + 0];
                        currentY2 += fArr[k2 + 1];
                        if (k2 <= 0) {
                            path2.rMoveTo(fArr[k2 + 0], fArr[k2 + 1]);
                            currentSegmentStartX2 = currentX2;
                            currentSegmentStartY2 = currentY2;
                            k = k2;
                            break;
                        }
                        path2.rLineTo(fArr[k2 + 0], fArr[k2 + 1]);
                        k = k2;
                        break;
                    case 'q':
                        path2.rQuadTo(fArr[k2 + 0], fArr[k2 + 1], fArr[k2 + 2], fArr[k2 + 3]);
                        ctrlPointX3 = fArr[k2 + 0] + currentX2;
                        currentX = fArr[k2 + 1] + currentY2;
                        currentX2 += fArr[k2 + 2];
                        currentY2 += fArr[k2 + 3];
                        ctrlPointX2 = ctrlPointX3;
                        ctrlPointY2 = currentX;
                        k = k2;
                        break;
                    case 's':
                        float reflectiveCtrlPointX;
                        float reflectiveCtrlPointY;
                        if (!(previousCmd3 == 'c' || previousCmd3 == 's' || previousCmd3 == 'C')) {
                            if (previousCmd3 != 'S') {
                                reflectiveCtrlPointX = 0.0f;
                                reflectiveCtrlPointY = 0.0f;
                                previousCmd2 = previousCmd3;
                                path.rCubicTo(reflectiveCtrlPointX, reflectiveCtrlPointY, fArr[k2 + 0], fArr[k2 + 1], fArr[k2 + 2], fArr[k2 + 3]);
                                ctrlPointX3 = fArr[k2 + 0] + currentX2;
                                currentX = fArr[k2 + 1] + currentY2;
                                currentX2 += fArr[k2 + 2];
                                currentY2 += fArr[k2 + 3];
                                ctrlPointX2 = ctrlPointX3;
                                ctrlPointY2 = currentX;
                                k = k2;
                                break;
                            }
                        }
                        reflectiveCtrlPointX = currentX2 - ctrlPointX2;
                        reflectiveCtrlPointY = currentY2 - ctrlPointY2;
                        previousCmd2 = previousCmd3;
                        path.rCubicTo(reflectiveCtrlPointX, reflectiveCtrlPointY, fArr[k2 + 0], fArr[k2 + 1], fArr[k2 + 2], fArr[k2 + 3]);
                        ctrlPointX3 = fArr[k2 + 0] + currentX2;
                        currentX = fArr[k2 + 1] + currentY2;
                        currentX2 += fArr[k2 + 2];
                        currentY2 += fArr[k2 + 3];
                        ctrlPointX2 = ctrlPointX3;
                        ctrlPointY2 = currentX;
                        k = k2;
                    case 't':
                        currentX = 0.0f;
                        ctrlPointX = 0.0f;
                        if (previousCmd3 == 'q' || previousCmd3 == 't' || previousCmd3 == 'Q' || previousCmd3 == 'T') {
                            currentX = currentX2 - ctrlPointX2;
                            ctrlPointX = currentY2 - ctrlPointY2;
                        }
                        path2.rQuadTo(currentX, ctrlPointX, fArr[k2 + 0], fArr[k2 + 1]);
                        ctrlPointX3 = currentX2 + currentX;
                        currentY = currentY2 + ctrlPointX;
                        currentX2 += fArr[k2 + 0];
                        currentY2 += fArr[k2 + 1];
                        ctrlPointX2 = ctrlPointX3;
                        ctrlPointY2 = currentY;
                        previousCmd2 = previousCmd3;
                        k = k2;
                        break;
                    case 'v':
                        path2.rLineTo(0.0f, fArr[k2 + 0]);
                        currentY2 += fArr[k2 + 0];
                        previousCmd2 = previousCmd3;
                        k = k2;
                        break;
                    default:
                        k = k2;
                        break;
                }
                previousCmd3 = cmd;
                k2 = k + incr;
            }
            previousCmd2 = previousCmd3;
            f = currentY2;
            k = k2;
            current[0] = currentX2;
            current[1] = f;
            current[2] = ctrlPointX2;
            current[3] = ctrlPointY2;
            current[4] = currentSegmentStartX2;
            current[5] = currentSegmentStartY2;
        }

        private static void drawArc(Path p, float x0, float y0, float x1, float y1, float a, float b, float theta, boolean isMoreThanHalf, boolean isPositiveArc) {
            float f = x0;
            float f2 = y0;
            float f3 = x1;
            float f4 = y1;
            float f5 = a;
            float f6 = b;
            boolean z = isPositiveArc;
            double thetaD = Math.toRadians((double) theta);
            double cosTheta = Math.cos(thetaD);
            double sinTheta = Math.sin(thetaD);
            double d = (double) f;
            Double.isNaN(d);
            d *= cosTheta;
            double d2 = (double) f2;
            Double.isNaN(d2);
            d += d2 * sinTheta;
            d2 = (double) f5;
            Double.isNaN(d2);
            double x0p = d / d2;
            d = (double) (-f);
            Double.isNaN(d);
            d *= sinTheta;
            d2 = (double) f2;
            Double.isNaN(d2);
            d += d2 * cosTheta;
            d2 = (double) f6;
            Double.isNaN(d2);
            double y0p = d / d2;
            d = (double) f3;
            Double.isNaN(d);
            d *= cosTheta;
            d2 = (double) f4;
            Double.isNaN(d2);
            d += d2 * sinTheta;
            d2 = (double) f5;
            Double.isNaN(d2);
            double x1p = d / d2;
            d = (double) (-f3);
            Double.isNaN(d);
            d *= sinTheta;
            d2 = (double) f4;
            Double.isNaN(d2);
            d += d2 * cosTheta;
            d2 = (double) f6;
            Double.isNaN(d2);
            double y1p = d / d2;
            double dx = x0p - x1p;
            double dy = y0p - y1p;
            double xm = (x0p + x1p) / 2.0d;
            double ym = (y0p + y1p) / 2.0d;
            double dsq = (dx * dx) + (dy * dy);
            if (dsq == 0.0d) {
                Log.w(PathParser.LOGTAG, " Points are coincident");
                return;
            }
            double disc = (1.0d / dsq) - 0.25d;
            if (disc < 0.0d) {
                String str = PathParser.LOGTAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Points are too far apart ");
                stringBuilder.append(dsq);
                Log.w(str, stringBuilder.toString());
                float adjust = (float) (Math.sqrt(dsq) / 1.99999d);
                boolean z2 = z;
                drawArc(p, x0, y0, x1, y1, f5 * adjust, f6 * adjust, theta, isMoreThanHalf, isPositiveArc);
                return;
            }
            double cx;
            double cy;
            z2 = z;
            d2 = Math.sqrt(disc);
            double sdx = d2 * dx;
            dsq = d2 * dy;
            if (isMoreThanHalf == z2) {
                cx = xm - dsq;
                cy = ym + sdx;
            } else {
                cx = xm + dsq;
                cy = ym - sdx;
            }
            d = Math.atan2(y0p - cy, x0p - cx);
            d2 = Math.atan2(y1p - cy, x1p - cx);
            sdx = d2 - d;
            if (z2 != (sdx >= 0.0d)) {
                if (sdx > 0.0d) {
                    sdx -= 6.283185307179586d;
                } else {
                    sdx += 6.283185307179586d;
                }
            }
            d2 = (double) f5;
            Double.isNaN(d2);
            cx *= d2;
            d2 = (double) f6;
            Double.isNaN(d2);
            d2 *= cy;
            d2 = (cx * sinTheta) + (d2 * cosTheta);
            arcToBezier(p, (cx * cosTheta) - (d2 * sinTheta), d2, (double) f5, (double) f6, (double) f, (double) f2, thetaD, d, sdx);
        }

        private static void arcToBezier(Path p, double cx, double cy, double a, double b, double e1x, double e1y, double theta, double start, double sweep) {
            double anglePerSegment;
            double cosEta1;
            double sinEta1;
            int i;
            int numSegments;
            double cosTheta;
            double sinTheta;
            double d = a;
            int numSegments2 = (int) Math.ceil(Math.abs((sweep * 4.0d) / 3.141592653589793d));
            double eta1 = start;
            double cosTheta2 = Math.cos(theta);
            double sinTheta2 = Math.sin(theta);
            double cosEta12 = Math.cos(eta1);
            double sinEta12 = Math.sin(eta1);
            double ep1x = (((-d) * cosTheta2) * sinEta12) - ((b * sinTheta2) * cosEta12);
            double ep1y = (((-d) * sinTheta2) * sinEta12) + ((b * cosTheta2) * cosEta12);
            double anglePerSegment2 = (double) numSegments2;
            Double.isNaN(anglePerSegment2);
            anglePerSegment2 = sweep / anglePerSegment2;
            double ep1x2 = ep1x;
            double ep1y2 = ep1y;
            ep1x = e1x;
            ep1y = e1y;
            double d2 = eta1;
            int i2 = 0;
            double eta12 = d2;
            while (i2 < numSegments2) {
                double eta2 = eta12 + anglePerSegment2;
                double sinEta2 = Math.sin(eta2);
                double cosEta2 = Math.cos(eta2);
                anglePerSegment = anglePerSegment2;
                anglePerSegment2 = (cx + ((d * cosTheta2) * cosEta2)) - ((b * sinTheta2) * sinEta2);
                cosEta1 = cosEta12;
                cosEta12 = (cy + ((d * sinTheta2) * cosEta2)) + ((b * cosTheta2) * sinEta2);
                sinEta1 = sinEta12;
                sinEta12 = (((-d) * cosTheta2) * sinEta2) - ((b * sinTheta2) * cosEta2);
                int numSegments3 = numSegments2;
                i = i2;
                double ep2y = (((-d) * sinTheta2) * sinEta2) + ((b * cosTheta2) * cosEta2);
                double tanDiff2 = Math.tan((eta2 - eta12) / 2.0d);
                double alpha = (Math.sin(eta2 - eta12) * (Math.sqrt(((tanDiff2 * 3.0d) * tanDiff2) + 4.0d) - 1.0d)) / 3.0d;
                d = ep1x + (alpha * ep1x2);
                numSegments = numSegments3;
                cosTheta = cosTheta2;
                double q1y = ep1y + (alpha * ep1y2);
                sinTheta = sinTheta2;
                double q2x = anglePerSegment2 - (alpha * sinEta12);
                double ep2y2 = ep2y;
                ep2y = cosEta12 - (alpha * ep2y);
                p.rLineTo(0.0f, 0.0f);
                p.cubicTo((float) d, (float) q1y, (float) q2x, (float) ep2y, (float) anglePerSegment2, (float) cosEta12);
                eta12 = eta2;
                ep1x = anglePerSegment2;
                ep1y = cosEta12;
                ep1x2 = sinEta12;
                ep1y2 = ep2y2;
                i2 = i + 1;
                numSegments2 = numSegments;
                sinEta12 = sinEta1;
                anglePerSegment2 = anglePerSegment;
                cosEta12 = cosEta1;
                cosTheta2 = cosTheta;
                sinTheta2 = sinTheta;
                d = a;
            }
            Path path = p;
            anglePerSegment = anglePerSegment2;
            numSegments = numSegments2;
            i = i2;
            cosTheta = cosTheta2;
            sinTheta = sinTheta2;
            cosEta1 = cosEta12;
            sinEta1 = sinEta12;
        }
    }

    static float[] copyOfRange(float[] original, int start, int end) {
        if (start <= end) {
            int originalLength = original.length;
            if (start < 0 || start > originalLength) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int resultLength = end - start;
            float[] result = new float[resultLength];
            System.arraycopy(original, start, result, 0, Math.min(resultLength, originalLength - start));
            return result;
        }
        throw new IllegalArgumentException();
    }

    public static Path createPathFromPathData(String pathData) {
        Path path = new Path();
        PathDataNode[] nodes = createNodesFromPathData(pathData);
        if (nodes == null) {
            return null;
        }
        try {
            PathDataNode.nodesToPath(nodes, path);
            return path;
        } catch (RuntimeException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error in parsing ");
            stringBuilder.append(pathData);
            throw new RuntimeException(stringBuilder.toString(), e);
        }
    }

    public static PathDataNode[] createNodesFromPathData(String pathData) {
        if (pathData == null) {
            return null;
        }
        int start = 0;
        int end = 1;
        ArrayList<PathDataNode> list = new ArrayList();
        while (end < pathData.length()) {
            end = nextStart(pathData, end);
            String s = pathData.substring(start, end).trim();
            if (s.length() > 0) {
                addNode(list, s.charAt(0), getFloats(s));
            }
            start = end;
            end++;
        }
        if (end - start == 1 && start < pathData.length()) {
            addNode(list, pathData.charAt(start), new float[0]);
        }
        return (PathDataNode[]) list.toArray(new PathDataNode[list.size()]);
    }

    public static PathDataNode[] deepCopyNodes(PathDataNode[] source) {
        if (source == null) {
            return null;
        }
        PathDataNode[] copy = new PathDataNode[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = new PathDataNode(source[i]);
        }
        return copy;
    }

    public static boolean canMorph(PathDataNode[] nodesFrom, PathDataNode[] nodesTo) {
        if (nodesFrom != null) {
            if (nodesTo != null) {
                if (nodesFrom.length != nodesTo.length) {
                    return false;
                }
                int i = 0;
                while (i < nodesFrom.length) {
                    if (nodesFrom[i].mType == nodesTo[i].mType) {
                        if (nodesFrom[i].mParams.length == nodesTo[i].mParams.length) {
                            i++;
                        }
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static void updateNodes(PathDataNode[] target, PathDataNode[] source) {
        for (int i = 0; i < source.length; i++) {
            target[i].mType = source[i].mType;
            for (int j = 0; j < source[i].mParams.length; j++) {
                target[i].mParams[j] = source[i].mParams[j];
            }
        }
    }

    private static int nextStart(String s, int end) {
        while (end < s.length()) {
            char c = s.charAt(end);
            if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E') {
                return end;
            }
            end++;
        }
        return end;
    }

    private static void addNode(ArrayList<PathDataNode> list, char cmd, float[] val) {
        list.add(new PathDataNode(cmd, val));
    }

    private static float[] getFloats(String s) {
        if (s.charAt(0) != 'z') {
            if (s.charAt(0) != 'Z') {
                try {
                    float[] results = new float[s.length()];
                    int count = 0;
                    int startPosition = 1;
                    ExtractFloatResult result = new ExtractFloatResult();
                    int totalLength = s.length();
                    while (startPosition < totalLength) {
                        extract(s, startPosition, result);
                        int endPosition = result.mEndPosition;
                        if (startPosition < endPosition) {
                            int count2 = count + 1;
                            results[count] = Float.parseFloat(s.substring(startPosition, endPosition));
                            count = count2;
                        }
                        if (result.mEndWithNegOrDot) {
                            startPosition = endPosition;
                        } else {
                            startPosition = endPosition + 1;
                        }
                    }
                    return copyOfRange(results, 0, count);
                } catch (NumberFormatException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("error in parsing \"");
                    stringBuilder.append(s);
                    stringBuilder.append("\"");
                    throw new RuntimeException(stringBuilder.toString(), e);
                }
            }
        }
        return new float[0];
    }

    private static void extract(String s, int start, ExtractFloatResult result) {
        int currentIndex = start;
        boolean foundSeparator = false;
        result.mEndWithNegOrDot = false;
        boolean secondDot = false;
        boolean isExponential = false;
        while (currentIndex < s.length()) {
            boolean isPrevExponential = isExponential;
            isExponential = false;
            char currentChar = s.charAt(currentIndex);
            if (currentChar != ' ') {
                if (currentChar != 'E' && currentChar != 'e') {
                    switch (currentChar) {
                        case ',':
                            break;
                        case '-':
                            if (!(currentIndex == start || isPrevExponential)) {
                                foundSeparator = true;
                                result.mEndWithNegOrDot = true;
                                break;
                            }
                        case '.':
                            if (!secondDot) {
                                secondDot = true;
                                break;
                            }
                            foundSeparator = true;
                            result.mEndWithNegOrDot = true;
                            break;
                        default:
                            break;
                    }
                }
                isExponential = true;
                if (foundSeparator) {
                    currentIndex++;
                } else {
                    result.mEndPosition = currentIndex;
                }
            }
            foundSeparator = true;
            if (foundSeparator) {
                currentIndex++;
            } else {
                result.mEndPosition = currentIndex;
            }
        }
        result.mEndPosition = currentIndex;
    }

    private PathParser() {
    }
}
