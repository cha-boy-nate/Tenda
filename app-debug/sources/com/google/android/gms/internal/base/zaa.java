package com.google.android.gms.internal.base;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class zaa implements IInterface {
    private final IBinder zaa;
    private final String zab;

    protected zaa(IBinder iBinder, String str) {
        this.zaa = iBinder;
        this.zab = str;
    }

    public IBinder asBinder() {
        return this.zaa;
    }

    protected final Parcel zaa() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.zab);
        return obtain;
    }

    protected final Parcel zaa(int i, Parcel parcel) throws RemoteException {
        Parcel obtain = Parcel.obtain();
        try {
            this.zaa.transact(i, parcel, obtain, 0);
            obtain.readException();
        } catch (RuntimeException e) {
            i = e;
            throw i;
        } finally {
            
/*
Method generation error in method: com.google.android.gms.internal.base.zaa.zaa(int, android.os.Parcel):android.os.Parcel, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0019: INVOKE  (wrap: android.os.Parcel
  ?: MERGE  (r5_1 android.os.Parcel) = (r5_0 'parcel' android.os.Parcel), (r0_0 'obtain' android.os.Parcel)) android.os.Parcel.recycle():void type: VIRTUAL in method: com.google.android.gms.internal.base.zaa.zaa(int, android.os.Parcel):android.os.Parcel, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:203)
	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:100)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:50)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:299)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r5_1 android.os.Parcel) = (r5_0 'parcel' android.os.Parcel), (r0_0 'obtain' android.os.Parcel) in method: com.google.android.gms.internal.base.zaa.zaa(int, android.os.Parcel):android.os.Parcel, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:101)
	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:84)
	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:632)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:338)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 22 more
Caused by: jadx.core.utils.exceptions.CodegenException: MERGE can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:211)
	... 27 more

*/

            protected final void zab(int i, Parcel parcel) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    this.zaa.transact(i, parcel, obtain, 0);
                    obtain.readException();
                } finally {
                    parcel.recycle();
                    obtain.recycle();
                }
            }

            protected final void zac(int i, Parcel parcel) throws RemoteException {
                try {
                    this.zaa.transact(1, parcel, null, 1);
                } finally {
                    parcel.recycle();
                }
            }
        }
