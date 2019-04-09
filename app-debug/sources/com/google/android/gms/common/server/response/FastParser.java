package com.google.android.gms.common.server.response;

import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.common.util.Base64Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

@ShowFirstParty
@KeepForSdk
public class FastParser<T extends FastJsonResponse> {
    private static final char[] zaqf = new char[]{'u', 'l', 'l'};
    private static final char[] zaqg = new char[]{'r', 'u', 'e'};
    private static final char[] zaqh = new char[]{'r', 'u', 'e', '\"'};
    private static final char[] zaqi = new char[]{'a', 'l', 's', 'e'};
    private static final char[] zaqj = new char[]{'a', 'l', 's', 'e', '\"'};
    private static final char[] zaqk = new char[]{'\n'};
    private static final zaa<Integer> zaqm = new zaa();
    private static final zaa<Long> zaqn = new zab();
    private static final zaa<Float> zaqo = new zac();
    private static final zaa<Double> zaqp = new zad();
    private static final zaa<Boolean> zaqq = new zae();
    private static final zaa<String> zaqr = new zaf();
    private static final zaa<BigInteger> zaqs = new zag();
    private static final zaa<BigDecimal> zaqt = new zah();
    private final char[] zaqa = new char[1];
    private final char[] zaqb = new char[32];
    private final char[] zaqc = new char[1024];
    private final StringBuilder zaqd = new StringBuilder(32);
    private final StringBuilder zaqe = new StringBuilder(1024);
    private final Stack<Integer> zaql = new Stack();

    @ShowFirstParty
    @KeepForSdk
    public static class ParseException extends Exception {
        public ParseException(String str) {
            super(str);
        }

        public ParseException(String str, Throwable th) {
            super(str, th);
        }

        public ParseException(Throwable th) {
            super(th);
        }
    }

    private interface zaa<O> {
        O zah(FastParser fastParser, BufferedReader bufferedReader) throws ParseException, IOException;
    }

    private final int zaa(java.io.BufferedReader r9, char[] r10) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:59:0x00cb in {7, 20, 23, 24, 25, 27, 28, 42, 43, 44, 45, 47, 48, 52, 54, 56, 58} preds:[]
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
        r8 = this;
        r0 = r8.zaj(r9);
        if (r0 == 0) goto L_0x00c2;
    L_0x0006:
        r1 = 44;
        if (r0 == r1) goto L_0x00b9;
    L_0x000a:
        r2 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r3 = 0;
        if (r0 != r2) goto L_0x0015;
    L_0x000f:
        r10 = zaqf;
        r8.zab(r9, r10);
        return r3;
    L_0x0015:
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r9.mark(r2);
        r2 = -1;
        r4 = 34;
        r5 = 1;
        if (r0 != r4) goto L_0x005e;
        r0 = 0;
        r1 = 0;
    L_0x0024:
        r6 = r10.length;
        if (r0 >= r6) goto L_0x005b;
    L_0x0027:
        r6 = r9.read(r10, r0, r5);
        if (r6 == r2) goto L_0x005b;
    L_0x002d:
        r6 = r10[r0];
        r7 = java.lang.Character.isISOControl(r6);
        if (r7 != 0) goto L_0x0052;
    L_0x0035:
        if (r6 != r4) goto L_0x0043;
    L_0x0037:
        if (r1 != 0) goto L_0x0043;
    L_0x0039:
        r9.reset();
        r10 = r0 + 1;
        r1 = (long) r10;
        r9.skip(r1);
        return r0;
    L_0x0043:
        r7 = 92;
        if (r6 != r7) goto L_0x004c;
        r1 = r1 ^ 1;
        goto L_0x004e;
        r1 = 0;
        r0 = r0 + 1;
        goto L_0x0024;
        r9 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r10 = "Unexpected control character while reading string";
        r9.<init>(r10);
        throw r9;
        goto L_0x00a3;
        r10[r3] = r0;
        r0 = 1;
        r4 = r10.length;
        if (r0 >= r4) goto L_0x00a2;
        r4 = r9.read(r10, r0, r5);
        if (r4 == r2) goto L_0x00a2;
        r4 = r10[r0];
        r6 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r4 == r6) goto L_0x0091;
        r4 = r10[r0];
        if (r4 == r1) goto L_0x0091;
        r4 = r10[r0];
        r4 = java.lang.Character.isWhitespace(r4);
        if (r4 != 0) goto L_0x008f;
        r4 = r10[r0];
        r6 = 93;
        if (r4 != r6) goto L_0x008b;
    L_0x008a:
        goto L_0x008f;
        r0 = r0 + 1;
        goto L_0x0062;
        goto L_0x0092;
        r9.reset();
        r1 = r0 + -1;
        r1 = (long) r1;
        r9.skip(r1);
        r10[r0] = r3;
        return r0;
        r9 = r10.length;
        if (r0 != r9) goto L_0x00b0;
        r9 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r10 = "Absurdly long value";
        r9.<init>(r10);
        throw r9;
        r9 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r10 = "Unexpected EOF";
        r9.<init>(r10);
        throw r9;
        r9 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r10 = "Missing value";
        r9.<init>(r10);
        throw r9;
        r9 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r10 = "Unexpected EOF";
        r9.<init>(r10);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, char[]):int");
    }

    private final <O> java.util.ArrayList<O> zaa(java.io.BufferedReader r5, com.google.android.gms.common.server.response.FastParser.zaa<O> r6) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:21:0x0055 in {3, 13, 15, 16, 18, 20} preds:[]
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
        r4 = this;
        r0 = r4.zaj(r5);
        r1 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r0 != r1) goto L_0x000f;
    L_0x0008:
        r6 = zaqf;
        r4.zab(r5, r6);
        r5 = 0;
        return r5;
    L_0x000f:
        r1 = 91;
        if (r0 != r1) goto L_0x004d;
    L_0x0013:
        r0 = r4.zaql;
        r1 = 5;
        r2 = java.lang.Integer.valueOf(r1);
        r0.push(r2);
        r0 = new java.util.ArrayList;
        r0.<init>();
    L_0x0022:
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r5.mark(r2);
        r2 = r4.zaj(r5);
        if (r2 == 0) goto L_0x0045;
    L_0x002d:
        r3 = 44;
        if (r2 == r3) goto L_0x0044;
    L_0x0031:
        r3 = 93;
        if (r2 == r3) goto L_0x0040;
    L_0x0035:
        r5.reset();
        r2 = r6.zah(r4, r5);
        r0.add(r2);
        goto L_0x0022;
    L_0x0040:
        r4.zak(r1);
        return r0;
    L_0x0044:
        goto L_0x0022;
    L_0x0045:
        r5 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r6 = "Unexpected EOF";
        r5.<init>(r6);
        throw r5;
    L_0x004d:
        r5 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r6 = "Expected start of array";
        r5.<init>(r6);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastParser$zaa):java.util.ArrayList<O>");
    }

    private final boolean zaa(java.io.BufferedReader r5, boolean r6) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:27:0x0057 in {10, 11, 13, 15, 17, 19, 20, 22, 24, 26} preds:[]
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
        r4 = this;
        r0 = 1;
    L_0x0001:
        r1 = r4.zaj(r5);
        r2 = 34;
        if (r1 == r2) goto L_0x004b;
    L_0x0009:
        r2 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r3 = 0;
        if (r1 == r2) goto L_0x0040;
    L_0x000e:
        r2 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r1 == r2) goto L_0x003a;
    L_0x0012:
        r2 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r1 != r2) goto L_0x0021;
    L_0x0016:
        if (r6 == 0) goto L_0x001b;
    L_0x0018:
        r6 = zaqh;
        goto L_0x001d;
    L_0x001b:
        r6 = zaqg;
    L_0x001d:
        r4.zab(r5, r6);
        return r0;
    L_0x0021:
        r5 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r6 = 19;
        r0 = new java.lang.StringBuilder;
        r0.<init>(r6);
        r6 = "Unexpected token: ";
        r0.append(r6);
        r0.append(r1);
        r6 = r0.toString();
        r5.<init>(r6);
        throw r5;
    L_0x003a:
        r6 = zaqf;
        r4.zab(r5, r6);
        return r3;
    L_0x0040:
        if (r6 == 0) goto L_0x0045;
    L_0x0042:
        r6 = zaqj;
        goto L_0x0047;
    L_0x0045:
        r6 = zaqi;
    L_0x0047:
        r4.zab(r5, r6);
        return r3;
    L_0x004b:
        if (r6 != 0) goto L_0x004f;
    L_0x004d:
        r6 = 1;
        goto L_0x0001;
    L_0x004f:
        r5 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r6 = "No boolean value found in string";
        r5.<init>(r6);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, boolean):boolean");
    }

    private final java.lang.String zab(java.io.BufferedReader r15) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:89:0x017a in {7, 10, 15, 17, 20, 29, 30, 35, 36, 41, 42, 47, 48, 50, 52, 53, 55, 63, 64, 69, 71, 73, 76, 77, 82, 84, 86, 88} preds:[]
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
        r14 = this;
        r0 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r15.mark(r0);
        r0 = r14.zaj(r15);
        r1 = 92;
        r2 = 18;
        r3 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r4 = 44;
        r5 = 34;
        r6 = 0;
        if (r0 == r5) goto L_0x00ee;
    L_0x0016:
        if (r0 == r4) goto L_0x00e5;
    L_0x0018:
        r7 = 32;
        r8 = 91;
        r9 = 1;
        if (r0 == r8) goto L_0x006e;
    L_0x001f:
        r1 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r0 == r1) goto L_0x002d;
    L_0x0023:
        r15.reset();
        r0 = r14.zaqc;
        r14.zaa(r15, r0);
        goto L_0x0107;
    L_0x002d:
        r0 = r14.zaql;
        r1 = java.lang.Integer.valueOf(r9);
        r0.push(r1);
        r15.mark(r7);
        r0 = r14.zaj(r15);
        if (r0 != r3) goto L_0x0044;
    L_0x003f:
        r14.zak(r9);
        goto L_0x0107;
    L_0x0044:
        if (r0 != r5) goto L_0x0057;
    L_0x0046:
        r15.reset();
        r14.zaa(r15);
    L_0x004c:
        r0 = r14.zab(r15);
        if (r0 != 0) goto L_0x004c;
    L_0x0052:
        r14.zak(r9);
        goto L_0x0107;
    L_0x0057:
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r1 = new java.lang.StringBuilder;
        r1.<init>(r2);
        r2 = "Unexpected token ";
        r1.append(r2);
        r1.append(r0);
        r0 = r1.toString();
        r15.<init>(r0);
        throw r15;
    L_0x006e:
        r0 = r14.zaql;
        r10 = 5;
        r11 = java.lang.Integer.valueOf(r10);
        r0.push(r11);
        r15.mark(r7);
        r0 = r14.zaj(r15);
        r7 = 93;
        if (r0 != r7) goto L_0x0088;
    L_0x0083:
        r14.zak(r10);
        goto L_0x0107;
    L_0x0088:
        r15.reset();
        r0 = 0;
        r11 = 0;
    L_0x008f:
        if (r9 <= 0) goto L_0x00df;
    L_0x0091:
        r12 = r14.zaj(r15);
        if (r12 == 0) goto L_0x00d6;
    L_0x0097:
        r13 = java.lang.Character.isISOControl(r12);
        if (r13 != 0) goto L_0x00cd;
    L_0x009d:
        if (r12 != r5) goto L_0x00a6;
    L_0x009f:
        if (r0 != 0) goto L_0x00a6;
        r11 = r11 ^ 1;
        goto L_0x00a7;
        if (r12 != r8) goto L_0x00b1;
        if (r11 != 0) goto L_0x00b1;
        r9 = r9 + 1;
        goto L_0x00b2;
        if (r12 != r7) goto L_0x00bc;
        if (r11 != 0) goto L_0x00bc;
        r9 = r9 + -1;
        goto L_0x00bd;
        if (r12 != r1) goto L_0x00c9;
        if (r11 == 0) goto L_0x00c9;
        r0 = r0 ^ 1;
        goto L_0x008f;
        r0 = 0;
        goto L_0x008f;
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Unexpected control character while reading array";
        r15.<init>(r0);
        throw r15;
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Unexpected EOF while parsing array";
        r15.<init>(r0);
        throw r15;
        r14.zak(r10);
        goto L_0x0107;
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Missing value";
        r15.<init>(r0);
        throw r15;
        r0 = r14.zaqa;
        r0 = r15.read(r0);
        r7 = -1;
        if (r0 == r7) goto L_0x0171;
        r0 = r14.zaqa;
        r0 = r0[r6];
        r8 = 0;
        if (r0 != r5) goto L_0x013b;
        if (r8 == 0) goto L_0x0106;
    L_0x0105:
        goto L_0x013b;
        r0 = r14.zaj(r15);
        r1 = 2;
        if (r0 == r4) goto L_0x0131;
    L_0x0110:
        if (r0 != r3) goto L_0x0119;
        r14.zak(r1);
        r15 = 0;
        return r15;
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r1 = new java.lang.StringBuilder;
        r1.<init>(r2);
        r2 = "Unexpected token ";
        r1.append(r2);
        r1.append(r0);
        r0 = r1.toString();
        r15.<init>(r0);
        throw r15;
        r14.zak(r1);
        r15 = r14.zaa(r15);
        return r15;
        if (r0 != r1) goto L_0x0146;
        r0 = r8 ^ 1;
        r8 = r0;
        goto L_0x0148;
        r8 = 0;
        r0 = r14.zaqa;
        r0 = r15.read(r0);
        if (r0 == r7) goto L_0x0168;
        r0 = r14.zaqa;
        r0 = r0[r6];
        r9 = java.lang.Character.isISOControl(r0);
        if (r9 != 0) goto L_0x015f;
        goto L_0x00ff;
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Unexpected control character while reading string";
        r15.<init>(r0);
        throw r15;
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Unexpected EOF while parsing string";
        r15.<init>(r0);
        throw r15;
        r15 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Unexpected EOF while parsing string";
        r15.<init>(r0);
        throw r15;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zab(java.io.BufferedReader):java.lang.String");
    }

    private static java.lang.String zab(java.io.BufferedReader r9, char[] r10, java.lang.StringBuilder r11, char[] r12) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:37:0x007c in {13, 14, 15, 17, 19, 26, 28, 31, 32, 33, 34, 36} preds:[]
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
        r0 = 0;
        r11.setLength(r0);
        r1 = r10.length;
        r9.mark(r1);
        r1 = 0;
        r2 = 0;
    L_0x000c:
        r3 = r9.read(r10);
        r4 = -1;
        if (r3 == r4) goto L_0x0074;
    L_0x0013:
        r4 = r2;
        r2 = r1;
        r1 = 0;
    L_0x0016:
        if (r1 >= r3) goto L_0x006a;
    L_0x0018:
        r5 = r10[r1];
        r6 = java.lang.Character.isISOControl(r5);
        r7 = 1;
        if (r6 == 0) goto L_0x003c;
    L_0x0021:
        if (r12 == 0) goto L_0x0030;
    L_0x0023:
        r6 = 0;
    L_0x0024:
        r8 = r12.length;
        if (r6 >= r8) goto L_0x0030;
    L_0x0027:
        r8 = r12[r6];
        if (r8 != r5) goto L_0x002d;
    L_0x002b:
        r6 = 1;
        goto L_0x0031;
    L_0x002d:
        r6 = r6 + 1;
        goto L_0x0024;
    L_0x0030:
        r6 = 0;
    L_0x0031:
        if (r6 == 0) goto L_0x0034;
    L_0x0033:
        goto L_0x003c;
    L_0x0034:
        r9 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r10 = "Unexpected control character while reading string";
        r9.<init>(r10);
        throw r9;
    L_0x003c:
        r6 = 34;
        if (r5 != r6) goto L_0x005d;
    L_0x0040:
        if (r2 != 0) goto L_0x005d;
    L_0x0042:
        r11.append(r10, r0, r1);
        r9.reset();
        r1 = r1 + r7;
        r0 = (long) r1;
        r9.skip(r0);
        if (r4 == 0) goto L_0x0058;
    L_0x004f:
        r9 = r11.toString();
        r9 = com.google.android.gms.common.util.JsonUtils.unescapeString(r9);
        return r9;
    L_0x0058:
        r9 = r11.toString();
        return r9;
    L_0x005d:
        r6 = 92;
        if (r5 != r6) goto L_0x0066;
        r2 = r2 ^ 1;
        r4 = 1;
        goto L_0x0067;
    L_0x0066:
        r2 = 0;
    L_0x0067:
        r1 = r1 + 1;
        goto L_0x0016;
    L_0x006a:
        r11.append(r10, r0, r3);
        r1 = r10.length;
        r9.mark(r1);
        r1 = r2;
        r2 = r4;
        goto L_0x000c;
    L_0x0074:
        r9 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r10 = "Unexpected EOF while parsing string";
        r9.<init>(r10);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zab(java.io.BufferedReader, char[], java.lang.StringBuilder, char[]):java.lang.String");
    }

    private final int zad(java.io.BufferedReader r10) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:40:0x008b in {2, 7, 8, 13, 15, 16, 24, 26, 28, 30, 33, 35, 37, 39} preds:[]
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
        r9 = this;
        r0 = r9.zaqc;
        r10 = r9.zaa(r10, r0);
        r0 = 0;
        if (r10 != 0) goto L_0x000a;
    L_0x0009:
        return r0;
    L_0x000a:
        r1 = r9.zaqc;
        if (r10 <= 0) goto L_0x0083;
    L_0x0011:
        r2 = r1[r0];
        r3 = 45;
        r4 = 1;
        if (r2 != r3) goto L_0x0020;
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r2 = 1;
        r3 = 1;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        goto L_0x0028;
    L_0x0020:
        r2 = -2147483647; // 0xffffffff80000001 float:-1.4E-45 double:NaN;
        r2 = 0;
        r3 = 0;
        r5 = -2147483647; // 0xffffffff80000001 float:-1.4E-45 double:NaN;
    L_0x0028:
        r6 = 10;
        if (r2 >= r10) goto L_0x0040;
    L_0x002c:
        r0 = r2 + 1;
        r2 = r1[r2];
        r2 = java.lang.Character.digit(r2, r6);
        if (r2 < 0) goto L_0x0038;
    L_0x0036:
        r2 = -r2;
        goto L_0x0042;
    L_0x0038:
        r10 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Unexpected non-digit character";
        r10.<init>(r0);
        throw r10;
    L_0x0040:
        r0 = r2;
        r2 = 0;
    L_0x0042:
        if (r0 >= r10) goto L_0x0074;
    L_0x0044:
        r7 = r0 + 1;
        r0 = r1[r0];
        r0 = java.lang.Character.digit(r0, r6);
        if (r0 < 0) goto L_0x006c;
    L_0x004e:
        r8 = -214748364; // 0xfffffffff3333334 float:-1.4197688E31 double:NaN;
        if (r2 < r8) goto L_0x0064;
    L_0x0053:
        r2 = r2 * 10;
        r8 = r5 + r0;
        if (r2 < r8) goto L_0x005c;
    L_0x0059:
        r2 = r2 - r0;
        r0 = r7;
        goto L_0x0042;
    L_0x005c:
        r10 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Number too large";
        r10.<init>(r0);
        throw r10;
    L_0x0064:
        r10 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Number too large";
        r10.<init>(r0);
        throw r10;
    L_0x006c:
        r10 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "Unexpected non-digit character";
        r10.<init>(r0);
        throw r10;
    L_0x0074:
        if (r3 == 0) goto L_0x0081;
    L_0x0076:
        if (r0 <= r4) goto L_0x0079;
    L_0x0078:
        return r2;
    L_0x0079:
        r10 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "No digits to parse";
        r10.<init>(r0);
        throw r10;
    L_0x0081:
        r10 = -r2;
        return r10;
    L_0x0083:
        r10 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r0 = "No number to parse";
        r10.<init>(r0);
        throw r10;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zad(java.io.BufferedReader):int");
    }

    private final long zae(java.io.BufferedReader r18) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:40:0x0098 in {2, 7, 8, 13, 15, 16, 24, 26, 28, 30, 33, 35, 37, 39} preds:[]
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
        r17 = this;
        r0 = r17;
        r1 = r0.zaqc;
        r2 = r18;
        r1 = r0.zaa(r2, r1);
        r2 = 0;
        if (r1 != 0) goto L_0x000f;
    L_0x000e:
        return r2;
    L_0x000f:
        r4 = r0.zaqc;
        if (r1 <= 0) goto L_0x0090;
    L_0x0016:
        r5 = 0;
        r6 = r4[r5];
        r7 = 45;
        r8 = 1;
        if (r6 != r7) goto L_0x0025;
        r5 = -9223372036854775808;
        r6 = r5;
        r5 = 1;
        r9 = 1;
        goto L_0x002b;
    L_0x0025:
        r6 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r9 = 0;
    L_0x002b:
        r10 = 10;
        if (r5 >= r1) goto L_0x0044;
    L_0x002f:
        r2 = r5 + 1;
        r3 = r4[r5];
        r3 = java.lang.Character.digit(r3, r10);
        if (r3 < 0) goto L_0x003c;
    L_0x0039:
        r3 = -r3;
        r11 = (long) r3;
        goto L_0x0046;
    L_0x003c:
        r1 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r2 = "Unexpected non-digit character";
        r1.<init>(r2);
        throw r1;
    L_0x0044:
        r11 = r2;
        r2 = r5;
    L_0x0046:
        if (r2 >= r1) goto L_0x0081;
    L_0x0048:
        r3 = r2 + 1;
        r2 = r4[r2];
        r2 = java.lang.Character.digit(r2, r10);
        if (r2 < 0) goto L_0x0079;
    L_0x0052:
        r13 = -922337203685477580; // 0xf333333333333334 float:4.1723254E-8 double:-8.390303882365713E246;
        r5 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
        if (r5 < 0) goto L_0x0071;
    L_0x005b:
        r13 = 10;
        r11 = r11 * r13;
        r13 = (long) r2;
        r15 = r6 + r13;
        r2 = (r11 > r15 ? 1 : (r11 == r15 ? 0 : -1));
        if (r2 < 0) goto L_0x0069;
    L_0x0066:
        r11 = r11 - r13;
        r2 = r3;
        goto L_0x0046;
    L_0x0069:
        r1 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r2 = "Number too large";
        r1.<init>(r2);
        throw r1;
    L_0x0071:
        r1 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r2 = "Number too large";
        r1.<init>(r2);
        throw r1;
    L_0x0079:
        r1 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r2 = "Unexpected non-digit character";
        r1.<init>(r2);
        throw r1;
    L_0x0081:
        if (r9 == 0) goto L_0x008e;
    L_0x0083:
        if (r2 <= r8) goto L_0x0086;
    L_0x0085:
        return r11;
    L_0x0086:
        r1 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r2 = "No digits to parse";
        r1.<init>(r2);
        throw r1;
    L_0x008e:
        r1 = -r11;
        return r1;
    L_0x0090:
        r1 = new com.google.android.gms.common.server.response.FastParser$ParseException;
        r2 = "No number to parse";
        r1.<init>(r2);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zae(java.io.BufferedReader):long");
    }

    @KeepForSdk
    public void parse(InputStream inputStream, T t) throws ParseException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1024);
        try {
            this.zaql.push(Integer.valueOf(0));
            inputStream = zaj(bufferedReader);
            if (inputStream != null) {
                if (inputStream == 91) {
                    this.zaql.push(Integer.valueOf(5));
                    inputStream = t.getFieldMappings();
                    if (inputStream.size() == 1) {
                        Field field = (Field) ((Entry) inputStream.entrySet().iterator().next()).getValue();
                        t.addConcreteTypeArrayInternal(field, field.zapu, zaa(bufferedReader, field));
                    } else {
                        throw new ParseException("Object array response class must have a single Field");
                    }
                } else if (inputStream == 123) {
                    this.zaql.push(Integer.valueOf(1));
                    zaa(bufferedReader, (FastJsonResponse) t);
                } else {
                    StringBuilder stringBuilder = new StringBuilder(19);
                    stringBuilder.append("Unexpected token: ");
                    stringBuilder.append(inputStream);
                    throw new ParseException(stringBuilder.toString());
                }
                zak(0);
                try {
                    bufferedReader.close();
                    return;
                } catch (IOException e) {
                    Log.w("FastParser", "Failed to close reader while parsing.");
                    return;
                }
            }
            throw new ParseException("No data to parse");
        } catch (Throwable e2) {
            throw new ParseException(e2);
        } catch (Throwable th) {
            try {
                bufferedReader.close();
            } catch (IOException e3) {
                Log.w("FastParser", "Failed to close reader while parsing.");
            }
        }
    }

    private final boolean zaa(BufferedReader bufferedReader, FastJsonResponse fastJsonResponse) throws ParseException, IOException {
        Map fieldMappings = fastJsonResponse.getFieldMappings();
        Object zaa = zaa(bufferedReader);
        if (zaa == null) {
            zak(1);
            return false;
        }
        while (zaa != null) {
            Field field = (Field) fieldMappings.get(zaa);
            if (field == null) {
                zaa = zab(bufferedReader);
            } else {
                StringBuilder stringBuilder;
                this.zaql.push(Integer.valueOf(4));
                char zaj;
                switch (field.zapq) {
                    case 0:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zad(bufferedReader));
                            break;
                        }
                        fastJsonResponse.zaa(field, zaa(bufferedReader, zaqm));
                        break;
                    case 1:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zaf(bufferedReader));
                            break;
                        }
                        fastJsonResponse.zab(field, zaa(bufferedReader, zaqs));
                        break;
                    case 2:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zae(bufferedReader));
                            break;
                        }
                        fastJsonResponse.zac(field, zaa(bufferedReader, zaqn));
                        break;
                    case 3:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zag(bufferedReader));
                            break;
                        }
                        fastJsonResponse.zad(field, zaa(bufferedReader, zaqo));
                        break;
                    case 4:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zah(bufferedReader));
                            break;
                        }
                        fastJsonResponse.zae(field, zaa(bufferedReader, zaqp));
                        break;
                    case 5:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zai(bufferedReader));
                            break;
                        }
                        fastJsonResponse.zaf(field, zaa(bufferedReader, zaqt));
                        break;
                    case 6:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zaa(bufferedReader, false));
                            break;
                        }
                        fastJsonResponse.zag(field, zaa(bufferedReader, zaqq));
                        break;
                    case 7:
                        if (!field.zapr) {
                            fastJsonResponse.zaa(field, zac(bufferedReader));
                            break;
                        }
                        fastJsonResponse.zah(field, zaa(bufferedReader, zaqr));
                        break;
                    case 8:
                        fastJsonResponse.zaa(field, Base64Utils.decode(zaa(bufferedReader, this.zaqc, this.zaqe, zaqk)));
                        break;
                    case 9:
                        fastJsonResponse.zaa(field, Base64Utils.decodeUrlSafe(zaa(bufferedReader, this.zaqc, this.zaqe, zaqk)));
                        break;
                    case 10:
                        Map map;
                        zaj = zaj(bufferedReader);
                        if (zaj == 'n') {
                            zab(bufferedReader, zaqf);
                            map = null;
                        } else if (zaj == '{') {
                            this.zaql.push(Integer.valueOf(1));
                            map = new HashMap();
                            while (true) {
                                char zaj2 = zaj(bufferedReader);
                                if (zaj2 == '\u0000') {
                                    throw new ParseException("Unexpected EOF");
                                } else if (zaj2 == '\"') {
                                    String zab = zab(bufferedReader, this.zaqb, this.zaqd, null);
                                    String valueOf;
                                    if (zaj(bufferedReader) != ':') {
                                        fastJsonResponse = "No map value found for key ";
                                        valueOf = String.valueOf(zab);
                                        throw new ParseException(valueOf.length() != 0 ? fastJsonResponse.concat(valueOf) : new String(fastJsonResponse));
                                    } else if (zaj(bufferedReader) != '\"') {
                                        fastJsonResponse = "Expected String value for key ";
                                        valueOf = String.valueOf(zab);
                                        throw new ParseException(valueOf.length() != 0 ? fastJsonResponse.concat(valueOf) : new String(fastJsonResponse));
                                    } else {
                                        map.put(zab, zab(bufferedReader, this.zaqb, this.zaqd, null));
                                        zaj2 = zaj(bufferedReader);
                                        if (zaj2 != ',') {
                                            if (zaj2 == '}') {
                                                zak(1);
                                            } else {
                                                stringBuilder = new StringBuilder(48);
                                                stringBuilder.append("Unexpected character while parsing string map: ");
                                                stringBuilder.append(zaj2);
                                                throw new ParseException(stringBuilder.toString());
                                            }
                                        }
                                    }
                                } else if (zaj2 == '}') {
                                    zak(1);
                                }
                            }
                        } else {
                            throw new ParseException("Expected start of a map object");
                        }
                        fastJsonResponse.zaa(field, map);
                        break;
                    case 11:
                        if (field.zapr) {
                            zaj = zaj(bufferedReader);
                            if (zaj == 'n') {
                                zab(bufferedReader, zaqf);
                                fastJsonResponse.addConcreteTypeArrayInternal(field, field.zapu, null);
                                break;
                            }
                            this.zaql.push(Integer.valueOf(5));
                            if (zaj == '[') {
                                fastJsonResponse.addConcreteTypeArrayInternal(field, field.zapu, zaa(bufferedReader, field));
                                break;
                            }
                            throw new ParseException("Expected array start");
                        }
                        zaj = zaj(bufferedReader);
                        if (zaj == 'n') {
                            zab(bufferedReader, zaqf);
                            fastJsonResponse.addConcreteTypeInternal(field, field.zapu, null);
                            break;
                        }
                        this.zaql.push(Integer.valueOf(1));
                        if (zaj == '{') {
                            try {
                                FastJsonResponse zacp = field.zacp();
                                zaa(bufferedReader, zacp);
                                fastJsonResponse.addConcreteTypeInternal(field, field.zapu, zacp);
                                break;
                            } catch (BufferedReader bufferedReader2) {
                                throw new ParseException("Error instantiating inner object", bufferedReader2);
                            } catch (BufferedReader bufferedReader22) {
                                throw new ParseException("Error instantiating inner object", bufferedReader22);
                            }
                        }
                        throw new ParseException("Expected start of object");
                    default:
                        fastJsonResponse = field.zapq;
                        StringBuilder stringBuilder2 = new StringBuilder(30);
                        stringBuilder2.append("Invalid field type ");
                        stringBuilder2.append(fastJsonResponse);
                        throw new ParseException(stringBuilder2.toString());
                }
                zak(4);
                zak(2);
                char zaj3 = zaj(bufferedReader22);
                if (zaj3 == ',') {
                    zaa = zaa(bufferedReader22);
                } else if (zaj3 == '}') {
                    zaa = null;
                } else {
                    stringBuilder = new StringBuilder(55);
                    stringBuilder.append("Expected end of object or field separator, but found: ");
                    stringBuilder.append(zaj3);
                    throw new ParseException(stringBuilder.toString());
                }
            }
        }
        zak(1);
        return true;
    }

    private final String zaa(BufferedReader bufferedReader) throws ParseException, IOException {
        this.zaql.push(Integer.valueOf(2));
        char zaj = zaj(bufferedReader);
        if (zaj == '\"') {
            this.zaql.push(Integer.valueOf(3));
            String zab = zab(bufferedReader, this.zaqb, this.zaqd, null);
            zak(3);
            if (zaj(bufferedReader) == 58) {
                return zab;
            }
            throw new ParseException("Expected key/value separator");
        } else if (zaj == ']') {
            zak(2);
            zak(1);
            zak(5);
            return null;
        } else if (zaj == '}') {
            zak(2);
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder(19);
            stringBuilder.append("Unexpected token: ");
            stringBuilder.append(zaj);
            throw new ParseException(stringBuilder.toString());
        }
    }

    private final String zac(BufferedReader bufferedReader) throws ParseException, IOException {
        return zaa(bufferedReader, this.zaqb, this.zaqd, null);
    }

    private final String zaa(BufferedReader bufferedReader, char[] cArr, StringBuilder stringBuilder, char[] cArr2) throws ParseException, IOException {
        char zaj = zaj(bufferedReader);
        if (zaj == '\"') {
            return zab(bufferedReader, cArr, stringBuilder, cArr2);
        }
        if (zaj == 110) {
            zab(bufferedReader, zaqf);
            return null;
        }
        throw new ParseException("Expected string");
    }

    private final BigInteger zaf(BufferedReader bufferedReader) throws ParseException, IOException {
        bufferedReader = zaa(bufferedReader, this.zaqc);
        if (bufferedReader == null) {
            return null;
        }
        return new BigInteger(new String(this.zaqc, 0, bufferedReader));
    }

    private final float zag(BufferedReader bufferedReader) throws ParseException, IOException {
        bufferedReader = zaa(bufferedReader, this.zaqc);
        if (bufferedReader == null) {
            return null;
        }
        return Float.parseFloat(new String(this.zaqc, 0, bufferedReader));
    }

    private final double zah(BufferedReader bufferedReader) throws ParseException, IOException {
        bufferedReader = zaa(bufferedReader, this.zaqc);
        if (bufferedReader == null) {
            return 0.0d;
        }
        return Double.parseDouble(new String(this.zaqc, 0, bufferedReader));
    }

    private final BigDecimal zai(BufferedReader bufferedReader) throws ParseException, IOException {
        bufferedReader = zaa(bufferedReader, this.zaqc);
        if (bufferedReader == null) {
            return null;
        }
        return new BigDecimal(new String(this.zaqc, 0, bufferedReader));
    }

    private final <T extends FastJsonResponse> ArrayList<T> zaa(BufferedReader bufferedReader, Field<?, ?> field) throws ParseException, IOException {
        ArrayList<T> arrayList = new ArrayList();
        char zaj = zaj(bufferedReader);
        if (zaj == ']') {
            zak(5);
            return arrayList;
        } else if (zaj == 'n') {
            zab(bufferedReader, zaqf);
            zak(5);
            return null;
        } else if (zaj == '{') {
            this.zaql.push(Integer.valueOf(1));
            while (true) {
                try {
                    FastJsonResponse zacp = field.zacp();
                    if (!zaa(bufferedReader, zacp)) {
                        return arrayList;
                    }
                    arrayList.add(zacp);
                    zaj = zaj(bufferedReader);
                    if (zaj != ',') {
                        break;
                    } else if (zaj(bufferedReader) == '{') {
                        this.zaql.push(Integer.valueOf(1));
                    } else {
                        throw new ParseException("Expected start of next object in array");
                    }
                } catch (BufferedReader bufferedReader2) {
                    throw new ParseException("Error instantiating inner object", bufferedReader2);
                } catch (BufferedReader bufferedReader22) {
                    throw new ParseException("Error instantiating inner object", bufferedReader22);
                }
            }
            if (zaj == ']') {
                zak(5);
                return arrayList;
            }
            field = new StringBuilder(19);
            field.append("Unexpected token: ");
            field.append(zaj);
            throw new ParseException(field.toString());
        } else {
            field = new StringBuilder(19);
            field.append("Unexpected token: ");
            field.append(zaj);
            throw new ParseException(field.toString());
        }
    }

    private final char zaj(BufferedReader bufferedReader) throws ParseException, IOException {
        if (bufferedReader.read(this.zaqa) == -1) {
            return '\u0000';
        }
        while (Character.isWhitespace(this.zaqa[0])) {
            if (bufferedReader.read(this.zaqa) == -1) {
                return '\u0000';
            }
        }
        return this.zaqa[0];
    }

    private final void zab(BufferedReader bufferedReader, char[] cArr) throws ParseException, IOException {
        int i = 0;
        while (i < cArr.length) {
            int read = bufferedReader.read(this.zaqb, 0, cArr.length - i);
            if (read != -1) {
                int i2 = 0;
                while (i2 < read) {
                    if (cArr[i2 + i] == this.zaqb[i2]) {
                        i2++;
                    } else {
                        throw new ParseException("Unexpected character");
                    }
                }
                i += read;
            } else {
                throw new ParseException("Unexpected EOF");
            }
        }
    }

    private final void zak(int i) throws ParseException {
        if (this.zaql.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(46);
            stringBuilder.append("Expected state ");
            stringBuilder.append(i);
            stringBuilder.append(" but had empty stack");
            throw new ParseException(stringBuilder.toString());
        }
        int intValue = ((Integer) this.zaql.pop()).intValue();
        if (intValue != i) {
            StringBuilder stringBuilder2 = new StringBuilder(46);
            stringBuilder2.append("Expected state ");
            stringBuilder2.append(i);
            stringBuilder2.append(" but had ");
            stringBuilder2.append(intValue);
            throw new ParseException(stringBuilder2.toString());
        }
    }
}
