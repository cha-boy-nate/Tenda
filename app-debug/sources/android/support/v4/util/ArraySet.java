package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E> implements Collection<E>, Set<E> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT = new int[0];
    private static final Object[] OBJECT = new Object[0];
    private static final String TAG = "ArraySet";
    @Nullable
    private static Object[] sBaseCache;
    private static int sBaseCacheSize;
    @Nullable
    private static Object[] sTwiceBaseCache;
    private static int sTwiceBaseCacheSize;
    Object[] mArray;
    private MapCollections<E, E> mCollections;
    private int[] mHashes;
    int mSize;

    /* renamed from: android.support.v4.util.ArraySet$1 */
    class C02661 extends MapCollections<E, E> {
        C02661() {
        }

        protected int colGetSize() {
            return ArraySet.this.mSize;
        }

        protected Object colGetEntry(int index, int offset) {
            return ArraySet.this.mArray[index];
        }

        protected int colIndexOfKey(Object key) {
            return ArraySet.this.indexOf(key);
        }

        protected int colIndexOfValue(Object value) {
            return ArraySet.this.indexOf(value);
        }

        protected Map<E, E> colGetMap() {
            throw new UnsupportedOperationException("not a map");
        }

        protected void colPut(E key, E e) {
            ArraySet.this.add(key);
        }

        protected E colSetValue(int index, E e) {
            throw new UnsupportedOperationException("not a map");
        }

        protected void colRemoveAt(int index) {
            ArraySet.this.removeAt(index);
        }

        protected void colClear() {
            ArraySet.this.clear();
        }
    }

    private int indexOf(Object key, int hash) {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, hash);
        if (index < 0 || key.equals(this.mArray[index])) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == hash) {
            if (key.equals(this.mArray[end])) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == hash) {
            if (key.equals(this.mArray[i])) {
                return i;
            }
            i--;
        }
        return end ^ -1;
    }

    private int indexOfNull() {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, 0);
        if (index < 0 || this.mArray[index] == null) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == 0) {
            if (this.mArray[end] == null) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == 0) {
            if (this.mArray[i] == null) {
                return i;
            }
            i--;
        }
        return end ^ -1;
    }

    private void allocArrays(int size) {
        Object[] array;
        if (size == 8) {
            synchronized (ArraySet.class) {
                if (sTwiceBaseCache != null) {
                    array = sTwiceBaseCache;
                    this.mArray = array;
                    sTwiceBaseCache = (Object[]) array[0];
                    this.mHashes = (int[]) array[1];
                    array[1] = null;
                    array[0] = null;
                    sTwiceBaseCacheSize--;
                    return;
                }
            }
        } else if (size == 4) {
            synchronized (ArraySet.class) {
                if (sBaseCache != null) {
                    array = sBaseCache;
                    this.mArray = array;
                    sBaseCache = (Object[]) array[0];
                    this.mHashes = (int[]) array[1];
                    array[1] = null;
                    array[0] = null;
                    sBaseCacheSize--;
                    return;
                }
            }
        }
        this.mHashes = new int[size];
        this.mArray = new Object[size];
    }

    private static void freeArrays(int[] hashes, Object[] array, int size) {
        int i;
        if (hashes.length == 8) {
            synchronized (ArraySet.class) {
                if (sTwiceBaseCacheSize < 10) {
                    array[0] = sTwiceBaseCache;
                    array[1] = hashes;
                    for (i = size - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    sTwiceBaseCache = array;
                    sTwiceBaseCacheSize++;
                }
            }
        } else if (hashes.length == 4) {
            synchronized (ArraySet.class) {
                if (sBaseCacheSize < 10) {
                    array[0] = sBaseCache;
                    array[1] = hashes;
                    for (i = size - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    sBaseCache = array;
                    sBaseCacheSize++;
                }
            }
        }
    }

    public ArraySet() {
        this(0);
    }

    public ArraySet(int capacity) {
        if (capacity == 0) {
            this.mHashes = INT;
            this.mArray = OBJECT;
        } else {
            allocArrays(capacity);
        }
        this.mSize = 0;
    }

    public ArraySet(@Nullable ArraySet<E> set) {
        this();
        if (set != null) {
            addAll((ArraySet) set);
        }
    }

    public ArraySet(@Nullable Collection<E> set) {
        this();
        if (set != null) {
            addAll((Collection) set);
        }
    }

    public void clear() {
        int i = this.mSize;
        if (i != 0) {
            freeArrays(this.mHashes, this.mArray, i);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        }
    }

    public void ensureCapacity(int minimumCapacity) {
        if (this.mHashes.length < minimumCapacity) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(minimumCapacity);
            int i = this.mSize;
            if (i > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, i);
                System.arraycopy(oarray, 0, this.mArray, 0, this.mSize);
            }
            freeArrays(ohashes, oarray, this.mSize);
        }
    }

    public boolean contains(@Nullable Object key) {
        return indexOf(key) >= 0;
    }

    public int indexOf(@Nullable Object key) {
        return key == null ? indexOfNull() : indexOf(key, key.hashCode());
    }

    @Nullable
    public E valueAt(int index) {
        return this.mArray[index];
    }

    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    public boolean add(@Nullable E value) {
        int hash;
        int index;
        if (value == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = value.hashCode();
            index = indexOf(value, hash);
        }
        if (index >= 0) {
            return false;
        }
        index ^= -1;
        int i = this.mSize;
        if (i >= this.mHashes.length) {
            int i2 = 4;
            if (i >= 8) {
                i2 = (i >> 1) + i;
            } else if (i >= 4) {
                i2 = 8;
            }
            i = i2;
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(i);
            Object obj = this.mHashes;
            if (obj.length > 0) {
                System.arraycopy(ohashes, 0, obj, 0, ohashes.length);
                System.arraycopy(oarray, 0, this.mArray, 0, oarray.length);
            }
            freeArrays(ohashes, oarray, this.mSize);
        }
        int i3 = this.mSize;
        if (index < i3) {
            Object obj2 = this.mHashes;
            System.arraycopy(obj2, index, obj2, index + 1, i3 - index);
            Object obj3 = this.mArray;
            System.arraycopy(obj3, index, obj3, index + 1, this.mSize - index);
        }
        this.mHashes[index] = hash;
        this.mArray[index] = value;
        this.mSize++;
        return true;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void append(E value) {
        int index = this.mSize;
        int hash = value == null ? 0 : value.hashCode();
        int[] iArr = this.mHashes;
        if (index >= iArr.length) {
            throw new IllegalStateException("Array is full");
        } else if (index <= 0 || iArr[index - 1] <= hash) {
            this.mSize = index + 1;
            this.mHashes[index] = hash;
            this.mArray[index] = value;
        } else {
            add(value);
        }
    }

    public void addAll(@NonNull ArraySet<? extends E> array) {
        int N = array.mSize;
        ensureCapacity(this.mSize + N);
        if (this.mSize != 0) {
            for (int i = 0; i < N; i++) {
                add(array.valueAt(i));
            }
        } else if (N > 0) {
            System.arraycopy(array.mHashes, 0, this.mHashes, 0, N);
            System.arraycopy(array.mArray, 0, this.mArray, 0, N);
            this.mSize = N;
        }
    }

    public boolean remove(@Nullable Object object) {
        int index = indexOf(object);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    public E removeAt(int index) {
        Object[] objArr = this.mArray;
        Object old = objArr[index];
        int i = this.mSize;
        if (i <= 1) {
            freeArrays(this.mHashes, objArr, i);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        } else {
            int[] iArr = this.mHashes;
            int i2 = 8;
            int i3;
            if (iArr.length <= 8 || i >= iArr.length / 3) {
                this.mSize--;
                i3 = this.mSize;
                if (index < i3) {
                    Object obj = this.mHashes;
                    System.arraycopy(obj, index + 1, obj, index, i3 - index);
                    Object obj2 = this.mArray;
                    System.arraycopy(obj2, index + 1, obj2, index, this.mSize - index);
                }
                this.mArray[this.mSize] = null;
            } else {
                if (i > 8) {
                    i2 = i + (i >> 1);
                }
                i3 = i2;
                int[] ohashes = this.mHashes;
                Object[] oarray = this.mArray;
                allocArrays(i3);
                this.mSize--;
                if (index > 0) {
                    System.arraycopy(ohashes, 0, this.mHashes, 0, index);
                    System.arraycopy(oarray, 0, this.mArray, 0, index);
                }
                int i4 = this.mSize;
                if (index < i4) {
                    System.arraycopy(ohashes, index + 1, this.mHashes, index, i4 - index);
                    System.arraycopy(oarray, index + 1, this.mArray, index, this.mSize - index);
                }
            }
        }
        return old;
    }

    public boolean removeAll(@NonNull ArraySet<? extends E> array) {
        int N = array.mSize;
        int originalSize = this.mSize;
        for (int i = 0; i < N; i++) {
            remove(array.valueAt(i));
        }
        return originalSize != this.mSize;
    }

    public int size() {
        return this.mSize;
    }

    @NonNull
    public Object[] toArray() {
        int i = this.mSize;
        Object[] result = new Object[i];
        System.arraycopy(this.mArray, 0, result, 0, i);
        return result;
    }

    @NonNull
    public <T> T[] toArray(@NonNull T[] array) {
        if (array.length < this.mSize) {
            array = (Object[]) Array.newInstance(array.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy(this.mArray, 0, array, 0, this.mSize);
        int length = array.length;
        int i = this.mSize;
        if (length > i) {
            array[i] = null;
        }
        return array;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        Set<?> set = (Set) object;
        if (size() != set.size()) {
            return false;
        }
        int i = 0;
        while (i < this.mSize) {
            try {
                if (!set.contains(valueAt(i))) {
                    return false;
                }
                i++;
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int[] hashes = this.mHashes;
        int result = 0;
        for (int i = 0; i < this.mSize; i++) {
            result += hashes[i];
        }
        return result;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 14);
        buffer.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            ArraySet value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Set)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new C02661();
        }
        return this.mCollections;
    }

    public Iterator<E> iterator() {
        return getCollection().getKeySet().iterator();
    }

    public boolean containsAll(@NonNull Collection<?> collection) {
        for (Object contains : collection) {
            if (!contains(contains)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(@NonNull Collection<? extends E> collection) {
        ensureCapacity(this.mSize + collection.size());
        boolean added = false;
        for (E value : collection) {
            added |= add(value);
        }
        return added;
    }

    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean removed = false;
        for (Object value : collection) {
            removed |= remove(value);
        }
        return removed;
    }

    public boolean retainAll(@NonNull Collection<?> collection) {
        boolean removed = false;
        for (int i = this.mSize - 1; i >= 0; i--) {
            if (!collection.contains(this.mArray[i])) {
                removeAt(i);
                removed = true;
            }
        }
        return removed;
    }
}
