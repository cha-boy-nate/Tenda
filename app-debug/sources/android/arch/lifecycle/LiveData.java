package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.Lifecycle.State;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class LiveData<T> {
    private static final Object NOT_SET = new Object();
    static final int START_VERSION = -1;
    private int mActiveCount = 0;
    private volatile Object mData;
    private final Object mDataLock = new Object();
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private SafeIterableMap<Observer<T>, ObserverWrapper> mObservers = new SafeIterableMap();
    private volatile Object mPendingData;
    private final Runnable mPostValueRunnable;
    private int mVersion;

    /* renamed from: android.arch.lifecycle.LiveData$1 */
    class C00081 implements Runnable {
        C00081() {
        }

        public void run() {
            Object newValue;
            synchronized (LiveData.this.mDataLock) {
                newValue = LiveData.this.mPendingData;
                LiveData.this.mPendingData = LiveData.NOT_SET;
            }
            LiveData.this.setValue(newValue);
        }
    }

    private abstract class ObserverWrapper {
        boolean mActive;
        int mLastVersion = -1;
        final Observer<T> mObserver;

        abstract boolean shouldBeActive();

        ObserverWrapper(Observer<T> observer) {
            this.mObserver = observer;
        }

        boolean isAttachedTo(LifecycleOwner owner) {
            return false;
        }

        void detachObserver() {
        }

        void activeStateChanged(boolean newActive) {
            if (newActive != this.mActive) {
                this.mActive = newActive;
                int i = 1;
                boolean wasInactive = LiveData.this.mActiveCount == 0;
                LiveData liveData = LiveData.this;
                int access$300 = liveData.mActiveCount;
                if (!this.mActive) {
                    i = -1;
                }
                liveData.mActiveCount = access$300 + i;
                if (wasInactive && this.mActive) {
                    LiveData.this.onActive();
                }
                if (LiveData.this.mActiveCount == 0 && !this.mActive) {
                    LiveData.this.onInactive();
                }
                if (this.mActive) {
                    LiveData.this.dispatchingValue(this);
                }
            }
        }
    }

    private class AlwaysActiveObserver extends ObserverWrapper {
        AlwaysActiveObserver(Observer<T> observer) {
            super(observer);
        }

        boolean shouldBeActive() {
            return true;
        }
    }

    class LifecycleBoundObserver extends ObserverWrapper implements GenericLifecycleObserver {
        @NonNull
        final LifecycleOwner mOwner;

        LifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<T> observer) {
            super(observer);
            this.mOwner = owner;
        }

        boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(State.STARTED);
        }

        public void onStateChanged(LifecycleOwner source, Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
                LiveData.this.removeObserver(this.mObserver);
            } else {
                activeStateChanged(shouldBeActive());
            }
        }

        boolean isAttachedTo(LifecycleOwner owner) {
            return this.mOwner == owner;
        }

        void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }
    }

    public LiveData() {
        Object obj = NOT_SET;
        this.mData = obj;
        this.mPendingData = obj;
        this.mVersion = -1;
        this.mPostValueRunnable = new C00081();
    }

    private void considerNotify(ObserverWrapper observer) {
        if (!observer.mActive) {
            return;
        }
        if (observer.shouldBeActive()) {
            int i = observer.mLastVersion;
            int i2 = this.mVersion;
            if (i < i2) {
                observer.mLastVersion = i2;
                observer.mObserver.onChanged(this.mData);
                return;
            }
            return;
        }
        observer.activeStateChanged(false);
    }

    private void dispatchingValue(@Nullable ObserverWrapper initiator) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        while (true) {
            this.mDispatchInvalidated = false;
            if (initiator == null) {
                Iterator<Entry<Observer<T>, ObserverWrapper>> iterator = this.mObservers.iteratorWithAdditions();
                while (iterator.hasNext()) {
                    considerNotify((ObserverWrapper) ((Entry) iterator.next()).getValue());
                    if (this.mDispatchInvalidated) {
                        break;
                    }
                }
            }
            considerNotify(initiator);
            initiator = null;
            if (!this.mDispatchInvalidated) {
                this.mDispatchingValue = false;
                return;
            }
        }
    }

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        if (owner.getLifecycle().getCurrentState() != State.DESTROYED) {
            LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
            ObserverWrapper existing = (ObserverWrapper) this.mObservers.putIfAbsent(observer, wrapper);
            if (existing != null) {
                if (!existing.isAttachedTo(owner)) {
                    throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
                }
            }
            if (existing == null) {
                owner.getLifecycle().addObserver(wrapper);
            }
        }
    }

    @MainThread
    public void observeForever(@NonNull Observer<T> observer) {
        AlwaysActiveObserver wrapper = new AlwaysActiveObserver(observer);
        ObserverWrapper existing = (ObserverWrapper) this.mObservers.putIfAbsent(observer, wrapper);
        if (existing != null) {
            if (existing instanceof LifecycleBoundObserver) {
                throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
            }
        }
        if (existing == null) {
            wrapper.activeStateChanged(true);
        }
    }

    @MainThread
    public void removeObserver(@NonNull Observer<T> observer) {
        assertMainThread("removeObserver");
        ObserverWrapper removed = (ObserverWrapper) this.mObservers.remove(observer);
        if (removed != null) {
            removed.detachObserver();
            removed.activeStateChanged(false);
        }
    }

    @MainThread
    public void removeObservers(@NonNull LifecycleOwner owner) {
        assertMainThread("removeObservers");
        Iterator it = this.mObservers.iterator();
        while (it.hasNext()) {
            Entry<Observer<T>, ObserverWrapper> entry = (Entry) it.next();
            if (((ObserverWrapper) entry.getValue()).isAttachedTo(owner)) {
                removeObserver((Observer) entry.getKey());
            }
        }
    }

    protected void postValue(T value) {
        synchronized (this.mDataLock) {
            boolean postTask = false;
            if (this.mPendingData == NOT_SET) {
                postTask = true;
            }
            this.mPendingData = value;
        }
        if (postTask) {
            ArchTaskExecutor.getInstance().postToMainThread(this.mPostValueRunnable);
        }
    }

    @MainThread
    protected void setValue(T value) {
        assertMainThread("setValue");
        this.mVersion++;
        this.mData = value;
        dispatchingValue(null);
    }

    @Nullable
    public T getValue() {
        Object data = this.mData;
        if (data != NOT_SET) {
            return data;
        }
        return null;
    }

    int getVersion() {
        return this.mVersion;
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    public boolean hasObservers() {
        return this.mObservers.size() > 0;
    }

    public boolean hasActiveObservers() {
        return this.mActiveCount > 0;
    }

    private static void assertMainThread(String methodName) {
        if (!ArchTaskExecutor.getInstance().isMainThread()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot invoke ");
            stringBuilder.append(methodName);
            stringBuilder.append(" on a background");
            stringBuilder.append(" thread");
            throw new IllegalStateException(stringBuilder.toString());
        }
    }
}
