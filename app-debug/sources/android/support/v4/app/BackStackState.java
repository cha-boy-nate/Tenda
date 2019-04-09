package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

/* compiled from: BackStackRecord */
final class BackStackState implements Parcelable {
    public static final Creator<BackStackState> CREATOR = new C00421();
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int mIndex;
    final String mName;
    final int[] mOps;
    final boolean mReorderingAllowed;
    final ArrayList<String> mSharedElementSourceNames;
    final ArrayList<String> mSharedElementTargetNames;
    final int mTransition;
    final int mTransitionStyle;

    /* compiled from: BackStackRecord */
    /* renamed from: android.support.v4.app.BackStackState$1 */
    static class C00421 implements Creator<BackStackState> {
        C00421() {
        }

        public BackStackState createFromParcel(Parcel in) {
            return new BackStackState(in);
        }

        public BackStackState[] newArray(int size) {
            return new BackStackState[size];
        }
    }

    public BackStackState(android.support.v4.app.BackStackRecord r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:13:0x0090 in {6, 7, 8, 10, 12} preds:[]
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
        r7 = this;
        r7.<init>();
        r0 = r8.mOps;
        r0 = r0.size();
        r1 = r0 * 6;
        r1 = new int[r1];
        r7.mOps = r1;
        r1 = r8.mAddToBackStack;
        if (r1 == 0) goto L_0x0088;
    L_0x0013:
        r1 = 0;
        r2 = 0;
    L_0x0015:
        if (r2 >= r0) goto L_0x005b;
    L_0x0017:
        r3 = r8.mOps;
        r3 = r3.get(r2);
        r3 = (android.support.v4.app.BackStackRecord.Op) r3;
        r4 = r7.mOps;
        r5 = r1 + 1;
        r6 = r3.cmd;
        r4[r1] = r6;
        r1 = r7.mOps;
        r4 = r5 + 1;
        r6 = r3.fragment;
        if (r6 == 0) goto L_0x0034;
    L_0x002f:
        r6 = r3.fragment;
        r6 = r6.mIndex;
        goto L_0x0035;
    L_0x0034:
        r6 = -1;
    L_0x0035:
        r1[r5] = r6;
        r1 = r7.mOps;
        r5 = r4 + 1;
        r6 = r3.enterAnim;
        r1[r4] = r6;
        r1 = r7.mOps;
        r4 = r5 + 1;
        r6 = r3.exitAnim;
        r1[r5] = r6;
        r1 = r7.mOps;
        r5 = r4 + 1;
        r6 = r3.popEnterAnim;
        r1[r4] = r6;
        r1 = r7.mOps;
        r4 = r5 + 1;
        r6 = r3.popExitAnim;
        r1[r5] = r6;
        r2 = r2 + 1;
        r1 = r4;
        goto L_0x0015;
    L_0x005b:
        r2 = r8.mTransition;
        r7.mTransition = r2;
        r2 = r8.mTransitionStyle;
        r7.mTransitionStyle = r2;
        r2 = r8.mName;
        r7.mName = r2;
        r2 = r8.mIndex;
        r7.mIndex = r2;
        r2 = r8.mBreadCrumbTitleRes;
        r7.mBreadCrumbTitleRes = r2;
        r2 = r8.mBreadCrumbTitleText;
        r7.mBreadCrumbTitleText = r2;
        r2 = r8.mBreadCrumbShortTitleRes;
        r7.mBreadCrumbShortTitleRes = r2;
        r2 = r8.mBreadCrumbShortTitleText;
        r7.mBreadCrumbShortTitleText = r2;
        r2 = r8.mSharedElementSourceNames;
        r7.mSharedElementSourceNames = r2;
        r2 = r8.mSharedElementTargetNames;
        r7.mSharedElementTargetNames = r2;
        r2 = r8.mReorderingAllowed;
        r7.mReorderingAllowed = r2;
        return;
    L_0x0088:
        r1 = new java.lang.IllegalStateException;
        r2 = "Not on back stack";
        r1.<init>(r2);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.BackStackState.<init>(android.support.v4.app.BackStackRecord):void");
    }

    public BackStackState(Parcel in) {
        this.mOps = in.createIntArray();
        this.mTransition = in.readInt();
        this.mTransitionStyle = in.readInt();
        this.mName = in.readString();
        this.mIndex = in.readInt();
        this.mBreadCrumbTitleRes = in.readInt();
        this.mBreadCrumbTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mBreadCrumbShortTitleRes = in.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mSharedElementSourceNames = in.createStringArrayList();
        this.mSharedElementTargetNames = in.createStringArrayList();
        this.mReorderingAllowed = in.readInt() != 0;
    }

    public BackStackRecord instantiate(FragmentManagerImpl fm) {
        BackStackRecord bse = new BackStackRecord(fm);
        int pos = 0;
        int num = 0;
        while (pos < this.mOps.length) {
            Op op = new Op();
            int pos2 = pos + 1;
            op.cmd = this.mOps[pos];
            if (FragmentManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Instantiate ");
                stringBuilder.append(bse);
                stringBuilder.append(" op #");
                stringBuilder.append(num);
                stringBuilder.append(" base fragment #");
                stringBuilder.append(this.mOps[pos2]);
                Log.v("FragmentManager", stringBuilder.toString());
            }
            int pos3 = pos2 + 1;
            pos = this.mOps[pos2];
            if (pos >= 0) {
                op.fragment = (Fragment) fm.mActive.get(pos);
            } else {
                op.fragment = null;
            }
            int[] iArr = this.mOps;
            int pos4 = pos3 + 1;
            op.enterAnim = iArr[pos3];
            pos3 = pos4 + 1;
            op.exitAnim = iArr[pos4];
            pos4 = pos3 + 1;
            op.popEnterAnim = iArr[pos3];
            pos3 = pos4 + 1;
            op.popExitAnim = iArr[pos4];
            bse.mEnterAnim = op.enterAnim;
            bse.mExitAnim = op.exitAnim;
            bse.mPopEnterAnim = op.popEnterAnim;
            bse.mPopExitAnim = op.popExitAnim;
            bse.addOp(op);
            num++;
            pos = pos3;
        }
        bse.mTransition = this.mTransition;
        bse.mTransitionStyle = this.mTransitionStyle;
        bse.mName = this.mName;
        bse.mIndex = this.mIndex;
        bse.mAddToBackStack = true;
        bse.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
        bse.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
        bse.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
        bse.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
        bse.mSharedElementSourceNames = this.mSharedElementSourceNames;
        bse.mSharedElementTargetNames = this.mSharedElementTargetNames;
        bse.mReorderingAllowed = this.mReorderingAllowed;
        bse.bumpBackStackNesting(1);
        return bse;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.mOps);
        dest.writeInt(this.mTransition);
        dest.writeInt(this.mTransitionStyle);
        dest.writeString(this.mName);
        dest.writeInt(this.mIndex);
        dest.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbTitleText, dest, 0);
        dest.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, dest, 0);
        dest.writeStringList(this.mSharedElementSourceNames);
        dest.writeStringList(this.mSharedElementTargetNames);
        dest.writeInt(this.mReorderingAllowed);
    }
}
