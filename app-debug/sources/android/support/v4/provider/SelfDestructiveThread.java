package android.support.v4.provider;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.GuardedBy;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SelfDestructiveThread {
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private Callback mCallback = new C01341();
    private final int mDestructAfterMillisec;
    @GuardedBy("mLock")
    private int mGeneration;
    @GuardedBy("mLock")
    private Handler mHandler;
    private final Object mLock = new Object();
    private final int mPriority;
    @GuardedBy("mLock")
    private HandlerThread mThread;
    private final String mThreadName;

    /* renamed from: android.support.v4.provider.SelfDestructiveThread$1 */
    class C01341 implements Callback {
        C01341() {
        }

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SelfDestructiveThread.this.onDestruction();
                    return true;
                case 1:
                    SelfDestructiveThread.this.onInvokeRunnable((Runnable) msg.obj);
                    return true;
                default:
                    return true;
            }
        }
    }

    /* renamed from: android.support.v4.provider.SelfDestructiveThread$3 */
    class C01373 implements Runnable {
        final /* synthetic */ Callable val$callable;
        final /* synthetic */ Condition val$cond;
        final /* synthetic */ AtomicReference val$holder;
        final /* synthetic */ ReentrantLock val$lock;
        final /* synthetic */ AtomicBoolean val$running;

        C01373(AtomicReference atomicReference, Callable callable, ReentrantLock reentrantLock, AtomicBoolean atomicBoolean, Condition condition) {
            this.val$holder = atomicReference;
            this.val$callable = callable;
            this.val$lock = reentrantLock;
            this.val$running = atomicBoolean;
            this.val$cond = condition;
        }

        public void run() {
            try {
                this.val$holder.set(this.val$callable.call());
            } catch (Exception e) {
            }
            this.val$lock.lock();
            try {
                this.val$running.set(false);
                this.val$cond.signal();
            } finally {
                this.val$lock.unlock();
            }
        }
    }

    public interface ReplyCallback<T> {
        void onReply(T t);
    }

    public <T> T postAndWait(java.util.concurrent.Callable<T> r11, int r12) throws java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:28:0x0061 in {6, 11, 12, 18, 21, 24, 27} preds:[]
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
        r10 = this;
        r4 = new java.util.concurrent.locks.ReentrantLock;
        r4.<init>();
        r7 = r4.newCondition();
        r2 = new java.util.concurrent.atomic.AtomicReference;
        r2.<init>();
        r5 = new java.util.concurrent.atomic.AtomicBoolean;
        r0 = 1;
        r5.<init>(r0);
        r8 = new android.support.v4.provider.SelfDestructiveThread$3;
        r0 = r8;
        r1 = r10;
        r3 = r11;
        r6 = r7;
        r0.<init>(r2, r3, r4, r5, r6);
        r10.post(r8);
        r4.lock();
        r0 = r5.get();	 Catch:{ all -> 0x005c }
        if (r0 != 0) goto L_0x0031;	 Catch:{ all -> 0x005c }
    L_0x0029:
        r0 = r2.get();	 Catch:{ all -> 0x005c }
        r4.unlock();
        return r0;
    L_0x0031:
        r0 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ all -> 0x005c }
        r8 = (long) r12;	 Catch:{ all -> 0x005c }
        r0 = r0.toNanos(r8);	 Catch:{ all -> 0x005c }
    L_0x0038:
        r8 = r7.awaitNanos(r0);	 Catch:{ InterruptedException -> 0x003e }
        r0 = r8;
        goto L_0x003f;
    L_0x003e:
        r3 = move-exception;
    L_0x003f:
        r3 = r5.get();	 Catch:{ all -> 0x005c }
        if (r3 != 0) goto L_0x004d;	 Catch:{ all -> 0x005c }
    L_0x0045:
        r3 = r2.get();	 Catch:{ all -> 0x005c }
        r4.unlock();
        return r3;
    L_0x004d:
        r8 = 0;
        r3 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r3 <= 0) goto L_0x0054;
    L_0x0053:
        goto L_0x0038;
    L_0x0054:
        r3 = new java.lang.InterruptedException;	 Catch:{ all -> 0x005c }
        r6 = "timeout";	 Catch:{ all -> 0x005c }
        r3.<init>(r6);	 Catch:{ all -> 0x005c }
        throw r3;	 Catch:{ all -> 0x005c }
    L_0x005c:
        r0 = move-exception;
        r4.unlock();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.provider.SelfDestructiveThread.postAndWait(java.util.concurrent.Callable, int):T");
    }

    public SelfDestructiveThread(String threadName, int priority, int destructAfterMillisec) {
        this.mThreadName = threadName;
        this.mPriority = priority;
        this.mDestructAfterMillisec = destructAfterMillisec;
        this.mGeneration = 0;
    }

    @VisibleForTesting
    public boolean isRunning() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mThread != null;
        }
        return z;
    }

    @VisibleForTesting
    public int getGeneration() {
        int i;
        synchronized (this.mLock) {
            i = this.mGeneration;
        }
        return i;
    }

    private void post(Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                this.mThread = new HandlerThread(this.mThreadName, this.mPriority);
                this.mThread.start();
                this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
                this.mGeneration++;
            }
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, runnable));
        }
    }

    public <T> void postAndReply(final Callable<T> callable, final ReplyCallback<T> reply) {
        final Handler callingHandler = new Handler();
        post(new Runnable() {
            public void run() {
                T t;
                try {
                    t = callable.call();
                } catch (Exception e) {
                    t = null;
                }
                final T result = t;
                callingHandler.post(new Runnable() {
                    public void run() {
                        reply.onReply(result);
                    }
                });
            }
        });
    }

    void onInvokeRunnable(Runnable runnable) {
        runnable.run();
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), (long) this.mDestructAfterMillisec);
        }
    }

    void onDestruction() {
        synchronized (this.mLock) {
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mThread.quit();
            this.mThread = null;
            this.mHandler = null;
        }
    }
}
