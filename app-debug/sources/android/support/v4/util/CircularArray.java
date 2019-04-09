package android.support.v4.util;

public final class CircularArray<E> {
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;

    public void removeFromEnd(int r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:21:0x0042 in {1, 6, 10, 16, 17, 18, 20} preds:[]
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
        r6 = this;
        if (r7 > 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r0 = r6.size();
        if (r7 > r0) goto L_0x003c;
    L_0x0009:
        r0 = 0;
        r1 = r6.mTail;
        if (r7 >= r1) goto L_0x0010;
    L_0x000e:
        r0 = r1 - r7;
    L_0x0010:
        r1 = r0;
    L_0x0011:
        r2 = r6.mTail;
        r3 = 0;
        if (r1 >= r2) goto L_0x001d;
    L_0x0016:
        r2 = r6.mElements;
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x0011;
    L_0x001d:
        r1 = r2 - r0;
        r7 = r7 - r1;
        r2 = r2 - r1;
        r6.mTail = r2;
        if (r7 <= 0) goto L_0x003b;
    L_0x0025:
        r2 = r6.mElements;
        r2 = r2.length;
        r6.mTail = r2;
        r2 = r6.mTail;
        r2 = r2 - r7;
        r4 = r2;
    L_0x002e:
        r5 = r6.mTail;
        if (r4 >= r5) goto L_0x0039;
    L_0x0032:
        r5 = r6.mElements;
        r5[r4] = r3;
        r4 = r4 + 1;
        goto L_0x002e;
    L_0x0039:
        r6.mTail = r2;
    L_0x003b:
        return;
    L_0x003c:
        r0 = new java.lang.ArrayIndexOutOfBoundsException;
        r0.<init>();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.CircularArray.removeFromEnd(int):void");
    }

    public void removeFromStart(int r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:20:0x0040 in {1, 6, 10, 15, 16, 17, 19} preds:[]
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
        r5 = this;
        if (r6 > 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r0 = r5.size();
        if (r6 > r0) goto L_0x003a;
    L_0x0009:
        r0 = r5.mElements;
        r0 = r0.length;
        r1 = r5.mHead;
        r2 = r0 - r1;
        if (r6 >= r2) goto L_0x0014;
    L_0x0012:
        r0 = r1 + r6;
    L_0x0014:
        r1 = r5.mHead;
    L_0x0016:
        r2 = 0;
        if (r1 >= r0) goto L_0x0020;
    L_0x0019:
        r3 = r5.mElements;
        r3[r1] = r2;
        r1 = r1 + 1;
        goto L_0x0016;
    L_0x0020:
        r1 = r5.mHead;
        r3 = r0 - r1;
        r6 = r6 - r3;
        r1 = r1 + r3;
        r4 = r5.mCapacityBitmask;
        r1 = r1 & r4;
        r5.mHead = r1;
        if (r6 <= 0) goto L_0x0039;
    L_0x002d:
        r1 = 0;
    L_0x002e:
        if (r1 >= r6) goto L_0x0037;
    L_0x0030:
        r4 = r5.mElements;
        r4[r1] = r2;
        r1 = r1 + 1;
        goto L_0x002e;
    L_0x0037:
        r5.mHead = r6;
    L_0x0039:
        return;
    L_0x003a:
        r0 = new java.lang.ArrayIndexOutOfBoundsException;
        r0.<init>();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.CircularArray.removeFromStart(int):void");
    }

    private void doubleCapacity() {
        Object obj = this.mElements;
        int n = obj.length;
        int i = this.mHead;
        int r = n - i;
        int newCapacity = n << 1;
        if (newCapacity >= 0) {
            Object[] a = new Object[newCapacity];
            System.arraycopy(obj, i, a, 0, r);
            System.arraycopy(this.mElements, 0, a, r, this.mHead);
            this.mElements = a;
            this.mHead = 0;
            this.mTail = n;
            this.mCapacityBitmask = newCapacity - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public CircularArray() {
        this(8);
    }

    public CircularArray(int minCapacity) {
        if (minCapacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        } else if (minCapacity <= 1073741824) {
            int arrayCapacity;
            if (Integer.bitCount(minCapacity) != 1) {
                arrayCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
            } else {
                arrayCapacity = minCapacity;
            }
            this.mCapacityBitmask = arrayCapacity - 1;
            this.mElements = new Object[arrayCapacity];
        } else {
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
    }

    public void addFirst(E e) {
        this.mHead = (this.mHead - 1) & this.mCapacityBitmask;
        Object[] objArr = this.mElements;
        int i = this.mHead;
        objArr[i] = e;
        if (i == this.mTail) {
            doubleCapacity();
        }
    }

    public void addLast(E e) {
        Object[] objArr = this.mElements;
        int i = this.mTail;
        objArr[i] = e;
        this.mTail = this.mCapacityBitmask & (i + 1);
        if (this.mTail == this.mHead) {
            doubleCapacity();
        }
    }

    public E popFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            Object[] objArr = this.mElements;
            E result = objArr[i];
            objArr[i] = null;
            this.mHead = (i + 1) & this.mCapacityBitmask;
            return result;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E popLast() {
        int i = this.mHead;
        int i2 = this.mTail;
        if (i != i2) {
            i = this.mCapacityBitmask & (i2 - 1);
            Object[] objArr = this.mElements;
            E result = objArr[i];
            objArr[i] = null;
            this.mTail = i;
            return result;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void clear() {
        removeFromStart(size());
    }

    public E getFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            return this.mElements[i];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getLast() {
        int i = this.mHead;
        int i2 = this.mTail;
        if (i != i2) {
            return this.mElements[(i2 - 1) & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E get(int n) {
        if (n >= 0 && n < size()) {
            return this.mElements[(this.mHead + n) & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int size() {
        return (this.mTail - this.mHead) & this.mCapacityBitmask;
    }

    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }
}
