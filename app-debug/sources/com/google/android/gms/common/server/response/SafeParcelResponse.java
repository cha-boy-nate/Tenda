package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@KeepForSdk
@Class(creator = "SafeParcelResponseCreator")
@VisibleForTesting
public class SafeParcelResponse extends FastSafeParcelableJsonResponse {
    @KeepForSdk
    public static final Creator<SafeParcelResponse> CREATOR = new zap();
    private final String mClassName;
    @VersionField(getter = "getVersionCode", id = 1)
    private final int zale;
    @Field(getter = "getFieldMappingDictionary", id = 3)
    private final zak zapy;
    @Field(getter = "getParcel", id = 2)
    private final Parcel zara;
    private final int zarb;
    private int zarc;
    private int zard;

    public SafeParcelResponse(zak zak, String str) {
        this.zale = 1;
        this.zara = Parcel.obtain();
        this.zarb = 0;
        this.zapy = (zak) Preconditions.checkNotNull(zak);
        this.mClassName = (String) Preconditions.checkNotNull(str);
        this.zarc = 0;
    }

    private final void zaa(java.lang.StringBuilder r10, java.util.Map<java.lang.String, com.google.android.gms.common.server.response.FastJsonResponse.Field<?, ?>> r11, android.os.Parcel r12) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:86:0x02fb in {3, 10, 16, 18, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 38, 42, 43, 44, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 59, 60, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 83, 85} preds:[]
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
        r9 = this;
        r0 = new android.util.SparseArray;
        r0.<init>();
        r11 = r11.entrySet();
        r11 = r11.iterator();
    L_0x000f:
        r1 = r11.hasNext();
        if (r1 == 0) goto L_0x0029;
    L_0x0015:
        r1 = r11.next();
        r1 = (java.util.Map.Entry) r1;
        r2 = r1.getValue();
        r2 = (com.google.android.gms.common.server.response.FastJsonResponse.Field) r2;
        r2 = r2.getSafeParcelableFieldId();
        r0.put(r2, r1);
        goto L_0x000f;
        r11 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r10.append(r11);
        r11 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.validateObjectHeader(r12);
        r1 = 1;
        r2 = 0;
        r3 = 0;
    L_0x0037:
        r4 = r12.dataPosition();
        if (r4 >= r11) goto L_0x02d6;
    L_0x003d:
        r4 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readHeader(r12);
        r5 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.getFieldId(r4);
        r5 = r0.get(r5);
        r5 = (java.util.Map.Entry) r5;
        if (r5 == 0) goto L_0x0037;
    L_0x004d:
        if (r3 == 0) goto L_0x0054;
    L_0x004f:
        r3 = ",";
        r10.append(r3);
    L_0x0054:
        r3 = r5.getKey();
        r3 = (java.lang.String) r3;
        r5 = r5.getValue();
        r5 = (com.google.android.gms.common.server.response.FastJsonResponse.Field) r5;
        r6 = "\"";
        r10.append(r6);
        r10.append(r3);
        r3 = "\":";
        r10.append(r3);
        r3 = r5.zacn();
        if (r3 == 0) goto L_0x0157;
        r3 = r5.zaps;
        switch(r3) {
            case 0: goto L_0x0146;
            case 1: goto L_0x0139;
            case 2: goto L_0x0128;
            case 3: goto L_0x0117;
            case 4: goto L_0x0106;
            case 5: goto L_0x00f9;
            case 6: goto L_0x00e8;
            case 7: goto L_0x00db;
            case 8: goto L_0x00ce;
            case 9: goto L_0x00ce;
            case 10: goto L_0x009d;
            case 11: goto L_0x0095;
            default: goto L_0x007a;
        };
    L_0x007a:
        r10 = new java.lang.IllegalArgumentException;
        r11 = r5.zaps;
        r12 = 36;
        r0 = new java.lang.StringBuilder;
        r0.<init>(r12);
        r12 = "Unknown field out type = ";
        r0.append(r12);
        r0.append(r11);
        r11 = r0.toString();
        r10.<init>(r11);
        throw r10;
    L_0x0095:
        r10 = new java.lang.IllegalArgumentException;
        r11 = "Method does not accept concrete type.";
        r10.<init>(r11);
        throw r10;
    L_0x009d:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBundle(r12, r4);
        r4 = new java.util.HashMap;
        r4.<init>();
        r6 = r3.keySet();
        r6 = r6.iterator();
    L_0x00af:
        r7 = r6.hasNext();
        if (r7 == 0) goto L_0x00c3;
    L_0x00b5:
        r7 = r6.next();
        r7 = (java.lang.String) r7;
        r8 = r3.getString(r7);
        r4.put(r7, r8);
        goto L_0x00af;
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r4);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x00ce:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createByteArray(r12, r4);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x00db:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createString(r12, r4);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x00e8:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readBoolean(r12, r4);
        r3 = java.lang.Boolean.valueOf(r3);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x00f9:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigDecimal(r12, r4);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x0106:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readDouble(r12, r4);
        r3 = java.lang.Double.valueOf(r3);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x0117:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readFloat(r12, r4);
        r3 = java.lang.Float.valueOf(r3);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x0128:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readLong(r12, r4);
        r3 = java.lang.Long.valueOf(r3);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x0139:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigInteger(r12, r4);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
    L_0x0146:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readInt(r12, r4);
        r3 = java.lang.Integer.valueOf(r3);
        r3 = com.google.android.gms.common.server.response.FastJsonResponse.zab(r5, r3);
        r9.zab(r10, r5, r3);
        goto L_0x02d2;
        r3 = r5.zapt;
        if (r3 == 0) goto L_0x01e0;
    L_0x015d:
        r3 = "[";
        r10.append(r3);
        r3 = r5.zaps;
        switch(r3) {
            case 0: goto L_0x01d1;
            case 1: goto L_0x01c9;
            case 2: goto L_0x01c1;
            case 3: goto L_0x01b9;
            case 4: goto L_0x01b1;
            case 5: goto L_0x01a9;
            case 6: goto L_0x01a1;
            case 7: goto L_0x0199;
            case 8: goto L_0x0191;
            case 9: goto L_0x0191;
            case 10: goto L_0x0191;
            case 11: goto L_0x0170;
            default: goto L_0x0168;
        };
    L_0x0168:
        r10 = new java.lang.IllegalStateException;
        r11 = "Unknown field type out.";
        r10.<init>(r11);
        throw r10;
    L_0x0170:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createParcelArray(r12, r4);
        r4 = r3.length;
        r6 = 0;
    L_0x0176:
        if (r6 >= r4) goto L_0x0190;
    L_0x0178:
        if (r6 <= 0) goto L_0x017f;
    L_0x017a:
        r7 = ",";
        r10.append(r7);
    L_0x017f:
        r7 = r3[r6];
        r7.setDataPosition(r2);
        r7 = r5.zacq();
        r8 = r3[r6];
        r9.zaa(r10, r7, r8);
        r6 = r6 + 1;
        goto L_0x0176;
    L_0x0190:
        goto L_0x01d9;
    L_0x0191:
        r10 = new java.lang.UnsupportedOperationException;
        r11 = "List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported";
        r10.<init>(r11);
        throw r10;
    L_0x0199:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createStringArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeStringArray(r10, r3);
        goto L_0x01d9;
    L_0x01a1:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBooleanArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x01d9;
    L_0x01a9:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigDecimalArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x01d9;
    L_0x01b1:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createDoubleArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x01d9;
    L_0x01b9:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createFloatArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x01d9;
    L_0x01c1:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createLongArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x01d9;
    L_0x01c9:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigIntegerArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x01d9;
    L_0x01d1:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createIntArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
    L_0x01d9:
        r3 = "]";
        r10.append(r3);
        goto L_0x02d2;
        r3 = r5.zaps;
        switch(r3) {
            case 0: goto L_0x02ca;
            case 1: goto L_0x02c2;
            case 2: goto L_0x02ba;
            case 3: goto L_0x02b2;
            case 4: goto L_0x02aa;
            case 5: goto L_0x02a2;
            case 6: goto L_0x029a;
            case 7: goto L_0x0284;
            case 8: goto L_0x026e;
            case 9: goto L_0x0258;
            case 10: goto L_0x01fe;
            case 11: goto L_0x01ee;
            default: goto L_0x01e6;
        };
    L_0x01e6:
        r10 = new java.lang.IllegalStateException;
        r11 = "Unknown field type out";
        r10.<init>(r11);
        throw r10;
    L_0x01ee:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createParcel(r12, r4);
        r3.setDataPosition(r2);
        r4 = r5.zacq();
        r9.zaa(r10, r4, r3);
        goto L_0x02d2;
    L_0x01fe:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBundle(r12, r4);
        r4 = r3.keySet();
        r4.size();
        r5 = "{";
        r10.append(r5);
        r4 = r4.iterator();
        r5 = 1;
    L_0x0214:
        r6 = r4.hasNext();
        if (r6 == 0) goto L_0x0251;
    L_0x021a:
        r6 = r4.next();
        r6 = (java.lang.String) r6;
        if (r5 != 0) goto L_0x0227;
    L_0x0222:
        r5 = ",";
        r10.append(r5);
        r5 = "\"";
        r10.append(r5);
        r10.append(r6);
        r5 = "\"";
        r10.append(r5);
        r5 = ":";
        r10.append(r5);
        r5 = "\"";
        r10.append(r5);
        r5 = r3.getString(r6);
        r5 = com.google.android.gms.common.util.JsonUtils.escapeString(r5);
        r10.append(r5);
        r5 = "\"";
        r10.append(r5);
        r5 = 0;
        goto L_0x0214;
    L_0x0251:
        r3 = "}";
        r10.append(r3);
        goto L_0x02d2;
    L_0x0258:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createByteArray(r12, r4);
        r4 = "\"";
        r10.append(r4);
        r3 = com.google.android.gms.common.util.Base64Utils.encodeUrlSafe(r3);
        r10.append(r3);
        r3 = "\"";
        r10.append(r3);
        goto L_0x02d2;
    L_0x026e:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createByteArray(r12, r4);
        r4 = "\"";
        r10.append(r4);
        r3 = com.google.android.gms.common.util.Base64Utils.encode(r3);
        r10.append(r3);
        r3 = "\"";
        r10.append(r3);
        goto L_0x02d2;
    L_0x0284:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createString(r12, r4);
        r4 = "\"";
        r10.append(r4);
        r3 = com.google.android.gms.common.util.JsonUtils.escapeString(r3);
        r10.append(r3);
        r3 = "\"";
        r10.append(r3);
        goto L_0x02d2;
    L_0x029a:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readBoolean(r12, r4);
        r10.append(r3);
        goto L_0x02d2;
    L_0x02a2:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigDecimal(r12, r4);
        r10.append(r3);
        goto L_0x02d2;
    L_0x02aa:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readDouble(r12, r4);
        r10.append(r3);
        goto L_0x02d2;
    L_0x02b2:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readFloat(r12, r4);
        r10.append(r3);
        goto L_0x02d2;
    L_0x02ba:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readLong(r12, r4);
        r10.append(r3);
        goto L_0x02d2;
    L_0x02c2:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigInteger(r12, r4);
        r10.append(r3);
        goto L_0x02d2;
    L_0x02ca:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readInt(r12, r4);
        r10.append(r3);
        r3 = 1;
        goto L_0x0037;
    L_0x02d6:
        r0 = r12.dataPosition();
        if (r0 != r11) goto L_0x02e2;
    L_0x02dc:
        r11 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r10.append(r11);
        return;
    L_0x02e2:
        r10 = new com.google.android.gms.common.internal.safeparcel.SafeParcelReader$ParseException;
        r0 = 37;
        r1 = new java.lang.StringBuilder;
        r1.<init>(r0);
        r0 = "Overread allowed size end=";
        r1.append(r0);
        r1.append(r11);
        r11 = r1.toString();
        r10.<init>(r11, r12);
        throw r10;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.SafeParcelResponse.zaa(java.lang.StringBuilder, java.util.Map, android.os.Parcel):void");
    }

    private SafeParcelResponse(SafeParcelable safeParcelable, zak zak, String str) {
        this.zale = 1;
        this.zara = Parcel.obtain();
        safeParcelable.writeToParcel(this.zara, 0);
        this.zarb = 1;
        this.zapy = (zak) Preconditions.checkNotNull(zak);
        this.mClassName = (String) Preconditions.checkNotNull(str);
        this.zarc = 2;
    }

    @KeepForSdk
    public static <T extends FastJsonResponse & SafeParcelable> SafeParcelResponse from(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        zak zak = new zak(t.getClass());
        zaa(zak, t);
        zak.zacs();
        zak.zacr();
        return new SafeParcelResponse((SafeParcelable) t, zak, canonicalName);
    }

    private static void zaa(zak zak, FastJsonResponse fastJsonResponse) {
        String str;
        Class cls = fastJsonResponse.getClass();
        if (!zak.zaa(cls)) {
            fastJsonResponse = fastJsonResponse.getFieldMappings();
            zak.zaa(cls, fastJsonResponse);
            for (String str2 : fastJsonResponse.keySet()) {
                String str22;
                FastJsonResponse.Field field = (FastJsonResponse.Field) fastJsonResponse.get(str22);
                Class cls2 = field.zapw;
                if (cls2 != null) {
                    try {
                        zaa(zak, (FastJsonResponse) cls2.newInstance());
                    } catch (zak zak2) {
                        str = "Could not instantiate an object of type ";
                        str22 = String.valueOf(field.zapw.getCanonicalName());
                        throw new IllegalStateException(str22.length() != 0 ? str.concat(str22) : new String(str), zak2);
                    } catch (zak zak22) {
                        str = "Could not access object of type ";
                        str22 = String.valueOf(field.zapw.getCanonicalName());
                        throw new IllegalStateException(str22.length() != 0 ? str.concat(str22) : new String(str), zak22);
                    }
                }
            }
        }
    }

    @Constructor
    SafeParcelResponse(@Param(id = 1) int i, @Param(id = 2) Parcel parcel, @Param(id = 3) zak zak) {
        this.zale = i;
        this.zara = (Parcel) Preconditions.checkNotNull(parcel);
        this.zarb = 2;
        this.zapy = zak;
        parcel = this.zapy;
        if (parcel == null) {
            this.mClassName = null;
        } else {
            this.mClassName = parcel.zact();
        }
        this.zarc = 2;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcelable parcelable;
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zale);
        SafeParcelWriter.writeParcel(parcel, 2, zacu(), false);
        int i2 = this.zarb;
        switch (i2) {
            case 0:
                parcelable = null;
                break;
            case 1:
                parcelable = this.zapy;
                break;
            case 2:
                parcelable = this.zapy;
                break;
            default:
                StringBuilder stringBuilder = new StringBuilder(34);
                stringBuilder.append("Invalid creation type: ");
                stringBuilder.append(i2);
                throw new IllegalStateException(stringBuilder.toString());
        }
        SafeParcelWriter.writeParcelable(parcel, 3, parcelable, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    private final Parcel zacu() {
        switch (this.zarc) {
            case 0:
                this.zard = SafeParcelWriter.beginObjectHeader(this.zara);
                break;
            case 1:
                break;
            default:
                break;
        }
        SafeParcelWriter.finishObjectHeader(this.zara, this.zard);
        this.zarc = 2;
        return this.zara;
    }

    public Map<String, FastJsonResponse.Field<?, ?>> getFieldMappings() {
        zak zak = this.zapy;
        if (zak == null) {
            return null;
        }
        return zak.zai(this.mClassName);
    }

    public Object getValueObject(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public boolean isPrimitiveFieldSet(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    private final void zab(FastJsonResponse.Field<?, ?> field) {
        if ((field.zapv != -1 ? true : null) != null) {
            field = this.zara;
            if (field != null) {
                switch (this.zarc) {
                    case 0:
                        this.zard = SafeParcelWriter.beginObjectHeader(field);
                        this.zarc = 1;
                        return;
                    case 1:
                        return;
                    case 2:
                        throw new IllegalStateException("Attempted to parse JSON with a SafeParcelResponse object that is already filled with data.");
                    default:
                        throw new IllegalStateException("Unknown parse state in SafeParcelResponse.");
                }
            }
            throw new IllegalStateException("Internal Parcel object is null.");
        }
        throw new IllegalStateException("Field does not have a valid safe parcelable field id.");
    }

    protected void setIntegerInternal(FastJsonResponse.Field<?, ?> field, String str, int i) {
        zab(field);
        SafeParcelWriter.writeInt(this.zara, field.getSafeParcelableFieldId(), i);
    }

    protected final void zaa(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Integer> arrayList) {
        zab(field);
        str = arrayList.size();
        int[] iArr = new int[str];
        for (int i = 0; i < str; i++) {
            iArr[i] = ((Integer) arrayList.get(i)).intValue();
        }
        SafeParcelWriter.writeIntArray(this.zara, field.getSafeParcelableFieldId(), iArr, true);
    }

    protected final void zaa(FastJsonResponse.Field<?, ?> field, String str, BigInteger bigInteger) {
        zab(field);
        SafeParcelWriter.writeBigInteger(this.zara, field.getSafeParcelableFieldId(), bigInteger, true);
    }

    protected final void zab(FastJsonResponse.Field<?, ?> field, String str, ArrayList<BigInteger> arrayList) {
        zab(field);
        str = arrayList.size();
        BigInteger[] bigIntegerArr = new BigInteger[str];
        for (int i = 0; i < str; i++) {
            bigIntegerArr[i] = (BigInteger) arrayList.get(i);
        }
        SafeParcelWriter.writeBigIntegerArray(this.zara, field.getSafeParcelableFieldId(), bigIntegerArr, true);
    }

    protected void setLongInternal(FastJsonResponse.Field<?, ?> field, String str, long j) {
        zab(field);
        SafeParcelWriter.writeLong(this.zara, field.getSafeParcelableFieldId(), j);
    }

    protected final void zac(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Long> arrayList) {
        zab(field);
        str = arrayList.size();
        long[] jArr = new long[str];
        for (int i = 0; i < str; i++) {
            jArr[i] = ((Long) arrayList.get(i)).longValue();
        }
        SafeParcelWriter.writeLongArray(this.zara, field.getSafeParcelableFieldId(), jArr, true);
    }

    protected final void zaa(FastJsonResponse.Field<?, ?> field, String str, float f) {
        zab(field);
        SafeParcelWriter.writeFloat(this.zara, field.getSafeParcelableFieldId(), f);
    }

    protected final void zad(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Float> arrayList) {
        zab(field);
        str = arrayList.size();
        float[] fArr = new float[str];
        for (int i = 0; i < str; i++) {
            fArr[i] = ((Float) arrayList.get(i)).floatValue();
        }
        SafeParcelWriter.writeFloatArray(this.zara, field.getSafeParcelableFieldId(), fArr, true);
    }

    protected final void zaa(FastJsonResponse.Field<?, ?> field, String str, double d) {
        zab(field);
        SafeParcelWriter.writeDouble(this.zara, field.getSafeParcelableFieldId(), d);
    }

    protected final void zae(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Double> arrayList) {
        zab(field);
        str = arrayList.size();
        double[] dArr = new double[str];
        for (int i = 0; i < str; i++) {
            dArr[i] = ((Double) arrayList.get(i)).doubleValue();
        }
        SafeParcelWriter.writeDoubleArray(this.zara, field.getSafeParcelableFieldId(), dArr, true);
    }

    protected final void zaa(FastJsonResponse.Field<?, ?> field, String str, BigDecimal bigDecimal) {
        zab(field);
        SafeParcelWriter.writeBigDecimal(this.zara, field.getSafeParcelableFieldId(), bigDecimal, true);
    }

    protected final void zaf(FastJsonResponse.Field<?, ?> field, String str, ArrayList<BigDecimal> arrayList) {
        zab(field);
        str = arrayList.size();
        BigDecimal[] bigDecimalArr = new BigDecimal[str];
        for (int i = 0; i < str; i++) {
            bigDecimalArr[i] = (BigDecimal) arrayList.get(i);
        }
        SafeParcelWriter.writeBigDecimalArray(this.zara, field.getSafeParcelableFieldId(), bigDecimalArr, true);
    }

    protected void setBooleanInternal(FastJsonResponse.Field<?, ?> field, String str, boolean z) {
        zab(field);
        SafeParcelWriter.writeBoolean(this.zara, field.getSafeParcelableFieldId(), z);
    }

    protected final void zag(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Boolean> arrayList) {
        zab(field);
        str = arrayList.size();
        boolean[] zArr = new boolean[str];
        for (int i = 0; i < str; i++) {
            zArr[i] = ((Boolean) arrayList.get(i)).booleanValue();
        }
        SafeParcelWriter.writeBooleanArray(this.zara, field.getSafeParcelableFieldId(), zArr, true);
    }

    protected void setStringInternal(FastJsonResponse.Field<?, ?> field, String str, String str2) {
        zab(field);
        SafeParcelWriter.writeString(this.zara, field.getSafeParcelableFieldId(), str2, true);
    }

    protected void setStringsInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<String> arrayList) {
        zab(field);
        str = arrayList.size();
        String[] strArr = new String[str];
        for (int i = 0; i < str; i++) {
            strArr[i] = (String) arrayList.get(i);
        }
        SafeParcelWriter.writeStringArray(this.zara, field.getSafeParcelableFieldId(), strArr, true);
    }

    protected void setDecodedBytesInternal(FastJsonResponse.Field<?, ?> field, String str, byte[] bArr) {
        zab(field);
        SafeParcelWriter.writeByteArray(this.zara, field.getSafeParcelableFieldId(), bArr, true);
    }

    protected final void zaa(FastJsonResponse.Field<?, ?> field, String str, Map<String, String> map) {
        zab(field);
        str = new Bundle();
        for (String str2 : map.keySet()) {
            str.putString(str2, (String) map.get(str2));
        }
        SafeParcelWriter.writeBundle(this.zara, field.getSafeParcelableFieldId(), str, true);
    }

    public <T extends FastJsonResponse> void addConcreteTypeInternal(FastJsonResponse.Field<?, ?> field, String str, T t) {
        zab(field);
        SafeParcelWriter.writeParcel(this.zara, field.getSafeParcelableFieldId(), ((SafeParcelResponse) t).zacu(), true);
    }

    public <T extends FastJsonResponse> void addConcreteTypeArrayInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<T> arrayList) {
        zab(field);
        str = new ArrayList();
        arrayList.size();
        arrayList = arrayList;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            str.add(((SafeParcelResponse) ((FastJsonResponse) obj)).zacu());
        }
        SafeParcelWriter.writeParcelList(this.zara, field.getSafeParcelableFieldId(), str, true);
    }

    public String toString() {
        Preconditions.checkNotNull(this.zapy, "Cannot convert to JSON on client side.");
        Parcel zacu = zacu();
        zacu.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        zaa(stringBuilder, this.zapy.zai(this.mClassName), zacu);
        return stringBuilder.toString();
    }

    private final void zab(StringBuilder stringBuilder, FastJsonResponse.Field<?, ?> field, Object obj) {
        if (field.zapr) {
            ArrayList arrayList = (ArrayList) obj;
            stringBuilder.append("[");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                zaa(stringBuilder, field.zapq, arrayList.get(i));
            }
            stringBuilder.append("]");
            return;
        }
        zaa(stringBuilder, field.zapq, obj);
    }

    private static void zaa(StringBuilder stringBuilder, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                stringBuilder.append(obj);
                return;
            case 7:
                stringBuilder.append("\"");
                stringBuilder.append(JsonUtils.escapeString(obj.toString()));
                stringBuilder.append("\"");
                return;
            case 8:
                stringBuilder.append("\"");
                stringBuilder.append(Base64Utils.encode((byte[]) obj));
                stringBuilder.append("\"");
                return;
            case 9:
                stringBuilder.append("\"");
                stringBuilder.append(Base64Utils.encodeUrlSafe((byte[]) obj));
                stringBuilder.append("\"");
                return;
            case 10:
                MapUtils.writeStringMapToJson(stringBuilder, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                StringBuilder stringBuilder2 = new StringBuilder(26);
                stringBuilder2.append("Unknown type = ");
                stringBuilder2.append(i);
                throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }
}
