package com.google.android.gms.common.images;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.internal.base.zak;
import com.google.android.gms.internal.base.zal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ImageManager {
    private static final Object zamg = new Object();
    private static HashSet<Uri> zamh = new HashSet();
    private static ImageManager zami;
    private final Context mContext;
    private final Handler mHandler = new zal(Looper.getMainLooper());
    private final ExecutorService zamj = Executors.newFixedThreadPool(4);
    private final zaa zamk = null;
    private final zak zaml = new zak();
    private final Map<zaa, ImageReceiver> zamm = new HashMap();
    private final Map<Uri, ImageReceiver> zamn = new HashMap();
    private final Map<Uri, Long> zamo = new HashMap();

    @KeepName
    private final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        private final ArrayList<zaa> zamp = new ArrayList();
        private final /* synthetic */ ImageManager zamq;

        ImageReceiver(ImageManager imageManager, Uri uri) {
            this.zamq = imageManager;
            super(new zal(Looper.getMainLooper()));
            this.mUri = uri;
        }

        public final void zab(zaa zaa) {
            Asserts.checkMainThread("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zamp.add(zaa);
        }

        public final void zac(zaa zaa) {
            Asserts.checkMainThread("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zamp.remove(zaa);
        }

        public final void zace() {
            Intent intent = new Intent(Constants.ACTION_LOAD_IMAGE);
            intent.putExtra(Constants.EXTRA_URI, this.mUri);
            intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, this);
            intent.putExtra(Constants.EXTRA_PRIORITY, 3);
            this.zamq.mContext.sendBroadcast(intent);
        }

        public final void onReceiveResult(int i, Bundle bundle) {
            this.zamq.zamj.execute(new zab(this.zamq, this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    private final class zab implements Runnable {
        private final Uri mUri;
        private final /* synthetic */ ImageManager zamq;
        private final ParcelFileDescriptor zamr;

        public zab(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.zamq = imageManager;
            this.mUri = uri;
            this.zamr = parcelFileDescriptor;
        }

        public final void run() {
            boolean z;
            Bitmap bitmap;
            Asserts.checkNotMainThread("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            ParcelFileDescriptor parcelFileDescriptor = this.zamr;
            boolean z2 = false;
            Bitmap bitmap2 = null;
            if (parcelFileDescriptor != null) {
                try {
                    bitmap2 = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
                } catch (Throwable e) {
                    String valueOf = String.valueOf(this.mUri);
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 34);
                    stringBuilder.append("OOM while loading bitmap for uri: ");
                    stringBuilder.append(valueOf);
                    Log.e("ImageManager", stringBuilder.toString(), e);
                    z2 = true;
                }
                try {
                    this.zamr.close();
                } catch (Throwable e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
                z = z2;
                bitmap = bitmap2;
            } else {
                bitmap = null;
                z = false;
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.zamq.mHandler.post(new zad(this.zamq, this.mUri, bitmap, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                String valueOf2 = String.valueOf(this.mUri);
                StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(valueOf2).length() + 32);
                stringBuilder2.append("Latch interrupted while posting ");
                stringBuilder2.append(valueOf2);
                Log.w("ImageManager", stringBuilder2.toString());
            }
        }
    }

    private final class zac implements Runnable {
        private final /* synthetic */ ImageManager zamq;
        private final zaa zams;

        public zac(ImageManager imageManager, zaa zaa) {
            this.zamq = imageManager;
            this.zams = zaa;
        }

        public final void run() {
            Asserts.checkMainThread("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) this.zamq.zamm.get(this.zams);
            if (imageReceiver != null) {
                this.zamq.zamm.remove(this.zams);
                imageReceiver.zac(this.zams);
            }
            zab zab = this.zams.zamu;
            if (zab.uri == null) {
                this.zams.zaa(this.zamq.mContext, this.zamq.zaml, true);
                return;
            }
            Bitmap zaa = this.zamq.zaa(zab);
            if (zaa != null) {
                this.zams.zaa(this.zamq.mContext, zaa, true);
                return;
            }
            Long l = (Long) this.zamq.zamo.get(zab.uri);
            if (l != null) {
                if (SystemClock.elapsedRealtime() - l.longValue() < 3600000) {
                    this.zams.zaa(this.zamq.mContext, this.zamq.zaml, true);
                    return;
                }
                this.zamq.zamo.remove(zab.uri);
            }
            this.zams.zaa(this.zamq.mContext, this.zamq.zaml);
            ImageReceiver imageReceiver2 = (ImageReceiver) this.zamq.zamn.get(zab.uri);
            if (imageReceiver2 == null) {
                imageReceiver2 = new ImageReceiver(this.zamq, zab.uri);
                this.zamq.zamn.put(zab.uri, imageReceiver2);
            }
            imageReceiver2.zab(this.zams);
            if (!(this.zams instanceof zad)) {
                this.zamq.zamm.put(this.zams, imageReceiver2);
            }
            synchronized (ImageManager.zamg) {
                if (!ImageManager.zamh.contains(zab.uri)) {
                    ImageManager.zamh.add(zab.uri);
                    imageReceiver2.zace();
                }
            }
        }
    }

    private final class zad implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private final CountDownLatch zadq;
        private final /* synthetic */ ImageManager zamq;
        private boolean zamt;

        public zad(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.zamq = imageManager;
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zamt = z;
            this.zadq = countDownLatch;
        }

        public final void run() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:33:0x00bf in {2, 3, 9, 11, 18, 19, 22, 23, 29, 32} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
            /*
            r10 = this;
            r0 = "OnBitmapLoadedRunnable must be executed in the main thread";
            com.google.android.gms.common.internal.Asserts.checkMainThread(r0);
            r0 = r10.mBitmap;
            r1 = 0;
            if (r0 == 0) goto L_0x000c;
        L_0x000a:
            r0 = 1;
            goto L_0x000d;
        L_0x000c:
            r0 = 0;
        L_0x000d:
            r2 = r10.zamq;
            r2 = r2.zamk;
            if (r2 == 0) goto L_0x0045;
        L_0x0015:
            r2 = r10.zamt;
            if (r2 == 0) goto L_0x0031;
        L_0x0019:
            r0 = r10.zamq;
            r0 = r0.zamk;
            r0.evictAll();
            java.lang.System.gc();
            r10.zamt = r1;
            r0 = r10.zamq;
            r0 = r0.mHandler;
            r0.post(r10);
            return;
        L_0x0031:
            if (r0 == 0) goto L_0x0045;
        L_0x0033:
            r2 = r10.zamq;
            r2 = r2.zamk;
            r3 = new com.google.android.gms.common.images.zab;
            r4 = r10.mUri;
            r3.<init>(r4);
            r4 = r10.mBitmap;
            r2.put(r3, r4);
        L_0x0045:
            r2 = r10.zamq;
            r2 = r2.zamn;
            r3 = r10.mUri;
            r2 = r2.remove(r3);
            r2 = (com.google.android.gms.common.images.ImageManager.ImageReceiver) r2;
            if (r2 == 0) goto L_0x00a7;
            r2 = r2.zamp;
            r3 = r2.size();
            r4 = 0;
        L_0x005f:
            if (r4 >= r3) goto L_0x00a7;
        L_0x0061:
            r5 = r2.get(r4);
            r5 = (com.google.android.gms.common.images.zaa) r5;
            if (r0 == 0) goto L_0x0075;
        L_0x0069:
            r6 = r10.zamq;
            r6 = r6.mContext;
            r7 = r10.mBitmap;
            r5.zaa(r6, r7, r1);
            goto L_0x0097;
        L_0x0075:
            r6 = r10.zamq;
            r6 = r6.zamo;
            r7 = r10.mUri;
            r8 = android.os.SystemClock.elapsedRealtime();
            r8 = java.lang.Long.valueOf(r8);
            r6.put(r7, r8);
            r6 = r10.zamq;
            r6 = r6.mContext;
            r7 = r10.zamq;
            r7 = r7.zaml;
            r5.zaa(r6, r7, r1);
        L_0x0097:
            r6 = r5 instanceof com.google.android.gms.common.images.zad;
            if (r6 != 0) goto L_0x00a4;
        L_0x009b:
            r6 = r10.zamq;
            r6 = r6.zamm;
            r6.remove(r5);
        L_0x00a4:
            r4 = r4 + 1;
            goto L_0x005f;
        L_0x00a7:
            r0 = r10.zadq;
            r0.countDown();
            r0 = com.google.android.gms.common.images.ImageManager.zamg;
            monitor-enter(r0);
            r1 = com.google.android.gms.common.images.ImageManager.zamh;	 Catch:{ all -> 0x00bc }
            r2 = r10.mUri;	 Catch:{ all -> 0x00bc }
            r1.remove(r2);	 Catch:{ all -> 0x00bc }
            monitor-exit(r0);	 Catch:{ all -> 0x00bc }
            return;	 Catch:{ all -> 0x00bc }
        L_0x00bc:
            r1 = move-exception;	 Catch:{ all -> 0x00bc }
            monitor-exit(r0);	 Catch:{ all -> 0x00bc }
            throw r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.images.ImageManager.zad.run():void");
        }
    }

    private static final class zaa extends LruCache<zab, Bitmap> {
        protected final /* synthetic */ int sizeOf(Object obj, Object obj2) {
            Bitmap bitmap = (Bitmap) obj2;
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        protected final /* synthetic */ void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
            super.entryRemoved(z, (zab) obj, (Bitmap) obj2, (Bitmap) obj3);
        }
    }

    public static ImageManager create(Context context) {
        if (zami == null) {
            zami = new ImageManager(context, false);
        }
        return zami;
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
    }

    public final void loadImage(ImageView imageView, Uri uri) {
        zaa(new zac(imageView, uri));
    }

    public final void loadImage(ImageView imageView, int i) {
        zaa(new zac(imageView, i));
    }

    public final void loadImage(ImageView imageView, Uri uri, int i) {
        zaa zac = new zac(imageView, uri);
        zac.zamw = i;
        zaa(zac);
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        zaa(new zad(onImageLoadedListener, uri));
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        zaa zad = new zad(onImageLoadedListener, uri);
        zad.zamw = i;
        zaa(zad);
    }

    private final void zaa(zaa zaa) {
        Asserts.checkMainThread("ImageManager.loadImage() must be called in the main thread");
        new zac(this, zaa).run();
    }

    private final Bitmap zaa(zab zab) {
        zaa zaa = this.zamk;
        if (zaa == null) {
            return null;
        }
        return (Bitmap) zaa.get(zab);
    }
}
