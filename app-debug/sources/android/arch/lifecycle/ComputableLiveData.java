package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class ComputableLiveData<T> {
    private AtomicBoolean mComputing;
    private final Executor mExecutor;
    private AtomicBoolean mInvalid;
    @VisibleForTesting
    final Runnable mInvalidationRunnable;
    private final LiveData<T> mLiveData;
    @VisibleForTesting
    final Runnable mRefreshRunnable;

    /* renamed from: android.arch.lifecycle.ComputableLiveData$2 */
    class C00042 implements Runnable {
        C00042() {
        }

        @WorkerThread
        public void run() {
            do {
                boolean computed = false;
                if (ComputableLiveData.this.mComputing.compareAndSet(false, true)) {
                    T value = null;
                    while (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
                        try {
                            computed = true;
                            value = ComputableLiveData.this.compute();
                        } catch (Throwable th) {
                            ComputableLiveData.this.mComputing.set(false);
                        }
                    }
                    if (computed) {
                        ComputableLiveData.this.mLiveData.postValue(value);
                    }
                    ComputableLiveData.this.mComputing.set(false);
                }
                if (!computed) {
                    return;
                }
            } while (ComputableLiveData.this.mInvalid.get());
        }
    }

    /* renamed from: android.arch.lifecycle.ComputableLiveData$3 */
    class C00053 implements Runnable {
        C00053() {
        }

        @MainThread
        public void run() {
            boolean isActive = ComputableLiveData.this.mLiveData.hasActiveObservers();
            if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && isActive) {
                ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }
        }
    }

    /* renamed from: android.arch.lifecycle.ComputableLiveData$1 */
    class C02391 extends LiveData<T> {
        C02391() {
        }

        protected void onActive() {
            ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
        }
    }

    @WorkerThread
    protected abstract T compute();

    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }

    public ComputableLiveData(@NonNull Executor executor) {
        this.mInvalid = new AtomicBoolean(true);
        this.mComputing = new AtomicBoolean(false);
        this.mRefreshRunnable = new C00042();
        this.mInvalidationRunnable = new C00053();
        this.mExecutor = executor;
        this.mLiveData = new C02391();
    }

    @NonNull
    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }
}
