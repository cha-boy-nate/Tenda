package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Pools {

    public interface Pool<T> {
        @Nullable
        T acquire();

        boolean release(@NonNull T t);
    }

    public static class SimplePool<T> implements Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;

        public SimplePool(int maxPoolSize) {
            if (maxPoolSize > 0) {
                this.mPool = new Object[maxPoolSize];
                return;
            }
            throw new IllegalArgumentException("The max pool size must be > 0");
        }

        public T acquire() {
            int i = this.mPoolSize;
            if (i <= 0) {
                return null;
            }
            int lastPooledIndex = i - 1;
            Object[] objArr = this.mPool;
            T instance = objArr[lastPooledIndex];
            objArr[lastPooledIndex] = null;
            this.mPoolSize = i - 1;
            return instance;
        }

        public boolean release(@NonNull T instance) {
            if (isInPool(instance)) {
                throw new IllegalStateException("Already in the pool!");
            }
            int i = this.mPoolSize;
            Object[] objArr = this.mPool;
            if (i >= objArr.length) {
                return false;
            }
            objArr[i] = instance;
            this.mPoolSize = i + 1;
            return true;
        }

        private boolean isInPool(@NonNull T instance) {
            for (int i = 0; i < this.mPoolSize; i++) {
                if (this.mPool[i] == instance) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class SynchronizedPool<T> extends SimplePool<T> {
        private final Object mLock = new Object();

        public SynchronizedPool(int maxPoolSize) {
            super(maxPoolSize);
        }

        public T acquire() {
            T acquire;
            synchronized (this.mLock) {
                acquire = super.acquire();
            }
            return acquire;
        }

        public boolean release(@NonNull T element) {
            boolean release;
            synchronized (this.mLock) {
                release = super.release(element);
            }
            return release;
        }
    }

    private Pools() {
    }
}
