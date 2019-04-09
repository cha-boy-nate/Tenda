package android.support.v4.print;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument.Page;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Margins;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo.Builder;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class PrintHelper {
    @SuppressLint({"InlinedApi"})
    public static final int COLOR_MODE_COLOR = 2;
    @SuppressLint({"InlinedApi"})
    public static final int COLOR_MODE_MONOCHROME = 1;
    static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
    private static final String LOG_TAG = "PrintHelper";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    Options mDecodeOptions = null;
    final Object mLock = new Object();
    int mOrientation = 1;
    int mScaleMode = 2;

    public interface OnPrintFinishCallback {
        void onFinish();
    }

    @RequiresApi(19)
    private class PrintBitmapAdapter extends PrintDocumentAdapter {
        private PrintAttributes mAttributes;
        private final Bitmap mBitmap;
        private final OnPrintFinishCallback mCallback;
        private final int mFittingMode;
        private final String mJobName;

        PrintBitmapAdapter(String jobName, int fittingMode, Bitmap bitmap, OnPrintFinishCallback callback) {
            this.mJobName = jobName;
            this.mFittingMode = fittingMode;
            this.mBitmap = bitmap;
            this.mCallback = callback;
        }

        public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
            this.mAttributes = newPrintAttributes;
            layoutResultCallback.onLayoutFinished(new Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ newPrintAttributes.equals(oldPrintAttributes));
        }

        public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, fileDescriptor, cancellationSignal, writeResultCallback);
        }

        public void onFinish() {
            OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
            if (onPrintFinishCallback != null) {
                onPrintFinishCallback.onFinish();
            }
        }
    }

    @RequiresApi(19)
    private class PrintUriAdapter extends PrintDocumentAdapter {
        PrintAttributes mAttributes;
        Bitmap mBitmap = null;
        final OnPrintFinishCallback mCallback;
        final int mFittingMode;
        final Uri mImageFile;
        final String mJobName;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;

        PrintUriAdapter(String jobName, Uri imageFile, OnPrintFinishCallback callback, int fittingMode) {
            this.mJobName = jobName;
            this.mImageFile = imageFile;
            this.mCallback = callback;
            this.mFittingMode = fittingMode;
        }

        public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
            synchronized (this) {
                this.mAttributes = newPrintAttributes;
            }
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
            } else if (this.mBitmap != null) {
                layoutResultCallback.onLayoutFinished(new Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ newPrintAttributes.equals(oldPrintAttributes));
            } else {
                final CancellationSignal cancellationSignal2 = cancellationSignal;
                final PrintAttributes printAttributes = newPrintAttributes;
                final PrintAttributes printAttributes2 = oldPrintAttributes;
                final LayoutResultCallback layoutResultCallback2 = layoutResultCallback;
                this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>() {

                    /* renamed from: android.support.v4.print.PrintHelper$PrintUriAdapter$1$1 */
                    class C01201 implements OnCancelListener {
                        C01201() {
                        }

                        public void onCancel() {
                            PrintUriAdapter.this.cancelLoad();
                            C01211.this.cancel(false);
                        }
                    }

                    protected void onPreExecute() {
                        cancellationSignal2.setOnCancelListener(new C01201());
                    }

                    protected Bitmap doInBackground(Uri... uris) {
                        try {
                            return PrintHelper.this.loadConstrainedBitmap(PrintUriAdapter.this.mImageFile);
                        } catch (FileNotFoundException e) {
                            return null;
                        }
                    }

                    /* JADX WARNING: inconsistent code. */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    protected void onPostExecute(android.graphics.Bitmap r12) {
                        /*
                        r11 = this;
                        super.onPostExecute(r12);
                        r0 = 0;
                        if (r12 == 0) goto L_0x004c;
                    L_0x0006:
                        r1 = android.support.v4.print.PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION;
                        if (r1 == 0) goto L_0x0012;
                    L_0x000a:
                        r1 = android.support.v4.print.PrintHelper.PrintUriAdapter.this;
                        r1 = android.support.v4.print.PrintHelper.this;
                        r1 = r1.mOrientation;
                        if (r1 != 0) goto L_0x004c;
                    L_0x0012:
                        monitor-enter(r11);
                        r1 = android.support.v4.print.PrintHelper.PrintUriAdapter.this;	 Catch:{ all -> 0x0049 }
                        r1 = r1.mAttributes;	 Catch:{ all -> 0x0049 }
                        r1 = r1.getMediaSize();	 Catch:{ all -> 0x0049 }
                        monitor-exit(r11);	 Catch:{ all -> 0x0044 }
                        if (r1 == 0) goto L_0x004c;
                    L_0x001e:
                        r2 = r1.isPortrait();
                        r3 = android.support.v4.print.PrintHelper.isPortrait(r12);
                        if (r2 == r3) goto L_0x004c;
                    L_0x0028:
                        r2 = new android.graphics.Matrix;
                        r2.<init>();
                        r3 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                        r2.postRotate(r3);
                        r4 = 0;
                        r5 = 0;
                        r6 = r12.getWidth();
                        r7 = r12.getHeight();
                        r9 = 1;
                        r3 = r12;
                        r8 = r2;
                        r12 = android.graphics.Bitmap.createBitmap(r3, r4, r5, r6, r7, r8, r9);
                        goto L_0x004c;
                    L_0x0044:
                        r0 = move-exception;
                        r10 = r1;
                        r1 = r0;
                        r0 = r10;
                        goto L_0x004a;
                    L_0x0049:
                        r1 = move-exception;
                    L_0x004a:
                        monitor-exit(r11);	 Catch:{ all -> 0x0049 }
                        throw r1;
                    L_0x004c:
                        r1 = android.support.v4.print.PrintHelper.PrintUriAdapter.this;
                        r1.mBitmap = r12;
                        if (r12 == 0) goto L_0x0075;
                    L_0x0052:
                        r2 = new android.print.PrintDocumentInfo$Builder;
                        r1 = r1.mJobName;
                        r2.<init>(r1);
                        r1 = 1;
                        r2 = r2.setContentType(r1);
                        r2 = r2.setPageCount(r1);
                        r2 = r2.build();
                        r3 = r5;
                        r4 = r6;
                        r3 = r3.equals(r4);
                        r1 = r1 ^ r3;
                        r3 = r7;
                        r3.onLayoutFinished(r2, r1);
                        goto L_0x007a;
                    L_0x0075:
                        r1 = r7;
                        r1.onLayoutFailed(r0);
                    L_0x007a:
                        r1 = android.support.v4.print.PrintHelper.PrintUriAdapter.this;
                        r1.mLoadBitmap = r0;
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.print.PrintHelper.PrintUriAdapter.1.onPostExecute(android.graphics.Bitmap):void");
                    }

                    protected void onCancelled(Bitmap result) {
                        layoutResultCallback2.onLayoutCancelled();
                        PrintUriAdapter.this.mLoadBitmap = null;
                    }
                }.execute(new Uri[0]);
            }
        }

        void cancelLoad() {
            synchronized (PrintHelper.this.mLock) {
                if (PrintHelper.this.mDecodeOptions != null) {
                    if (VERSION.SDK_INT < 24) {
                        PrintHelper.this.mDecodeOptions.requestCancelDecode();
                    }
                    PrintHelper.this.mDecodeOptions = null;
                }
            }
        }

        public void onFinish() {
            super.onFinish();
            cancelLoad();
            AsyncTask asyncTask = this.mLoadBitmap;
            if (asyncTask != null) {
                asyncTask.cancel(true);
            }
            OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
            if (onPrintFinishCallback != null) {
                onPrintFinishCallback.onFinish();
            }
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.mBitmap = null;
            }
        }

        public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, fileDescriptor, cancellationSignal, writeResultCallback);
        }
    }

    android.graphics.Bitmap loadConstrainedBitmap(android.net.Uri r11) throws java.io.FileNotFoundException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:53:0x0073 in {6, 10, 14, 28, 31, 38, 42, 44, 45, 48, 49, 50, 52} preds:[]
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
        r10 = this;
        if (r11 == 0) goto L_0x006b;
    L_0x0002:
        r0 = r10.mContext;
        if (r0 == 0) goto L_0x006b;
    L_0x0006:
        r0 = new android.graphics.BitmapFactory$Options;
        r0.<init>();
        r1 = 1;
        r0.inJustDecodeBounds = r1;
        r10.loadBitmap(r11, r0);
        r2 = r0.outWidth;
        r3 = r0.outHeight;
        r4 = 0;
        if (r2 <= 0) goto L_0x006a;
    L_0x0018:
        if (r3 > 0) goto L_0x001b;
    L_0x001a:
        goto L_0x006a;
    L_0x001b:
        r5 = java.lang.Math.max(r2, r3);
        r6 = 1;
    L_0x0020:
        r7 = 3500; // 0xdac float:4.905E-42 double:1.729E-320;
        if (r5 <= r7) goto L_0x0029;
    L_0x0024:
        r5 = r5 >>> 1;
        r6 = r6 << 1;
        goto L_0x0020;
    L_0x0029:
        if (r6 <= 0) goto L_0x0069;
    L_0x002b:
        r7 = java.lang.Math.min(r2, r3);
        r7 = r7 / r6;
        if (r7 > 0) goto L_0x0033;
    L_0x0032:
        goto L_0x0069;
    L_0x0033:
        r7 = r10.mLock;
        monitor-enter(r7);
        r8 = new android.graphics.BitmapFactory$Options;	 Catch:{ all -> 0x0066 }
        r8.<init>();	 Catch:{ all -> 0x0066 }
        r10.mDecodeOptions = r8;	 Catch:{ all -> 0x0066 }
        r8 = r10.mDecodeOptions;	 Catch:{ all -> 0x0066 }
        r8.inMutable = r1;	 Catch:{ all -> 0x0066 }
        r1 = r10.mDecodeOptions;	 Catch:{ all -> 0x0066 }
        r1.inSampleSize = r6;	 Catch:{ all -> 0x0066 }
        r1 = r10.mDecodeOptions;	 Catch:{ all -> 0x0066 }
        monitor-exit(r7);	 Catch:{ all -> 0x0061 }
        r7 = r10.loadBitmap(r11, r1);	 Catch:{ all -> 0x0056 }
        r8 = r10.mLock;
        monitor-enter(r8);
        r10.mDecodeOptions = r4;	 Catch:{ all -> 0x0053 }
        monitor-exit(r8);	 Catch:{ all -> 0x0053 }
        return r7;	 Catch:{ all -> 0x0053 }
    L_0x0053:
        r4 = move-exception;	 Catch:{ all -> 0x0053 }
        monitor-exit(r8);	 Catch:{ all -> 0x0053 }
        throw r4;
    L_0x0056:
        r7 = move-exception;
        r8 = r10.mLock;
        monitor-enter(r8);
        r10.mDecodeOptions = r4;	 Catch:{ all -> 0x005e }
        monitor-exit(r8);	 Catch:{ all -> 0x005e }
        throw r7;
    L_0x005e:
        r4 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x005e }
        throw r4;
    L_0x0061:
        r4 = move-exception;
        r9 = r4;
        r4 = r1;
        r1 = r9;
        goto L_0x0067;
    L_0x0066:
        r1 = move-exception;
    L_0x0067:
        monitor-exit(r7);	 Catch:{ all -> 0x0066 }
        throw r1;
    L_0x0069:
        return r4;
    L_0x006a:
        return r4;
    L_0x006b:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "bad argument to getScaledBitmap";
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.print.PrintHelper.loadConstrainedBitmap(android.net.Uri):android.graphics.Bitmap");
    }

    static {
        boolean z;
        boolean z2 = false;
        if (VERSION.SDK_INT >= 20) {
            if (VERSION.SDK_INT <= 23) {
                z = false;
                PRINT_ACTIVITY_RESPECTS_ORIENTATION = z;
                if (VERSION.SDK_INT != 23) {
                    z2 = true;
                }
                IS_MIN_MARGINS_HANDLING_CORRECT = z2;
            }
        }
        z = true;
        PRINT_ACTIVITY_RESPECTS_ORIENTATION = z;
        if (VERSION.SDK_INT != 23) {
            z2 = true;
        }
        IS_MIN_MARGINS_HANDLING_CORRECT = z2;
    }

    public static boolean systemSupportsPrint() {
        return VERSION.SDK_INT >= 19;
    }

    public PrintHelper(@NonNull Context context) {
        this.mContext = context;
    }

    public void setScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setColorMode(int colorMode) {
        this.mColorMode = colorMode;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public int getOrientation() {
        if (VERSION.SDK_INT < 19 || this.mOrientation != 0) {
            return this.mOrientation;
        }
        return 1;
    }

    public void printBitmap(@NonNull String jobName, @NonNull Bitmap bitmap) {
        printBitmap(jobName, bitmap, null);
    }

    public void printBitmap(@NonNull String jobName, @NonNull Bitmap bitmap, @Nullable OnPrintFinishCallback callback) {
        if (VERSION.SDK_INT >= 19) {
            if (bitmap != null) {
                MediaSize mediaSize;
                PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
                if (isPortrait(bitmap)) {
                    mediaSize = MediaSize.UNKNOWN_PORTRAIT;
                } else {
                    mediaSize = MediaSize.UNKNOWN_LANDSCAPE;
                }
                printManager.print(jobName, new PrintBitmapAdapter(jobName, this.mScaleMode, bitmap, callback), new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
            }
        }
    }

    public void printBitmap(@NonNull String jobName, @NonNull Uri imageFile) throws FileNotFoundException {
        printBitmap(jobName, imageFile, null);
    }

    public void printBitmap(@NonNull String jobName, @NonNull Uri imageFile, @Nullable OnPrintFinishCallback callback) throws FileNotFoundException {
        if (VERSION.SDK_INT >= 19) {
            PrintDocumentAdapter printUriAdapter = new PrintUriAdapter(jobName, imageFile, callback, this.mScaleMode);
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setColorMode(this.mColorMode);
            int i = this.mOrientation;
            if (i != 1) {
                if (i != 0) {
                    if (i == 2) {
                        builder.setMediaSize(MediaSize.UNKNOWN_PORTRAIT);
                    }
                    printManager.print(jobName, printUriAdapter, builder.build());
                }
            }
            builder.setMediaSize(MediaSize.UNKNOWN_LANDSCAPE);
            printManager.print(jobName, printUriAdapter, builder.build());
        }
    }

    static boolean isPortrait(Bitmap bitmap) {
        return bitmap.getWidth() <= bitmap.getHeight();
    }

    @RequiresApi(19)
    private static PrintAttributes.Builder copyAttributes(PrintAttributes other) {
        PrintAttributes.Builder b = new PrintAttributes.Builder().setMediaSize(other.getMediaSize()).setResolution(other.getResolution()).setMinMargins(other.getMinMargins());
        if (other.getColorMode() != 0) {
            b.setColorMode(other.getColorMode());
        }
        if (VERSION.SDK_INT >= 23 && other.getDuplexMode() != 0) {
            b.setDuplexMode(other.getDuplexMode());
        }
        return b;
    }

    static Matrix getMatrix(int imageWidth, int imageHeight, RectF content, int fittingMode) {
        Matrix matrix = new Matrix();
        float scale = content.width() / ((float) imageWidth);
        if (fittingMode == 2) {
            scale = Math.max(scale, content.height() / ((float) imageHeight));
        } else {
            scale = Math.min(scale, content.height() / ((float) imageHeight));
        }
        matrix.postScale(scale, scale);
        matrix.postTranslate((content.width() - (((float) imageWidth) * scale)) / 2.0f, (content.height() - (((float) imageHeight) * scale)) / 2.0f);
        return matrix;
    }

    @RequiresApi(19)
    void writeBitmap(PrintAttributes attributes, int fittingMode, Bitmap bitmap, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        PrintAttributes pdfAttributes;
        if (IS_MIN_MARGINS_HANDLING_CORRECT) {
            pdfAttributes = attributes;
        } else {
            pdfAttributes = copyAttributes(attributes).setMinMargins(new Margins(0, 0, 0, 0)).build();
        }
        final CancellationSignal cancellationSignal2 = cancellationSignal;
        final PrintAttributes printAttributes = pdfAttributes;
        final Bitmap bitmap2 = bitmap;
        final PrintAttributes printAttributes2 = attributes;
        final int i = fittingMode;
        final ParcelFileDescriptor parcelFileDescriptor = fileDescriptor;
        final WriteResultCallback writeResultCallback2 = writeResultCallback;
        new AsyncTask<Void, Void, Throwable>() {
            protected Throwable doInBackground(Void... params) {
                PrintedPdfDocument pdfDocument;
                Bitmap maybeGrayscale;
                try {
                    if (cancellationSignal2.isCanceled()) {
                        return null;
                    }
                    pdfDocument = new PrintedPdfDocument(PrintHelper.this.mContext, printAttributes);
                    maybeGrayscale = PrintHelper.convertBitmapForColorMode(bitmap2, printAttributes.getColorMode());
                    if (cancellationSignal2.isCanceled()) {
                        return null;
                    }
                    RectF contentRect;
                    Page page = pdfDocument.startPage(1);
                    if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                        contentRect = new RectF(page.getInfo().getContentRect());
                    } else {
                        PrintedPdfDocument dummyDocument = new PrintedPdfDocument(PrintHelper.this.mContext, printAttributes2);
                        Page dummyPage = dummyDocument.startPage(1);
                        RectF contentRect2 = new RectF(dummyPage.getInfo().getContentRect());
                        dummyDocument.finishPage(dummyPage);
                        dummyDocument.close();
                        contentRect = contentRect2;
                    }
                    Matrix matrix = PrintHelper.getMatrix(maybeGrayscale.getWidth(), maybeGrayscale.getHeight(), contentRect, i);
                    if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                        matrix.postTranslate(contentRect.left, contentRect.top);
                        page.getCanvas().clipRect(contentRect);
                    }
                    page.getCanvas().drawBitmap(maybeGrayscale, matrix, null);
                    pdfDocument.finishPage(page);
                    if (cancellationSignal2.isCanceled()) {
                        pdfDocument.close();
                        if (parcelFileDescriptor != null) {
                            try {
                                parcelFileDescriptor.close();
                            } catch (IOException e) {
                            }
                        }
                        if (maybeGrayscale != bitmap2) {
                            maybeGrayscale.recycle();
                        }
                        return null;
                    }
                    pdfDocument.writeTo(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                    pdfDocument.close();
                    if (parcelFileDescriptor != null) {
                        try {
                            parcelFileDescriptor.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (maybeGrayscale != bitmap2) {
                        maybeGrayscale.recycle();
                    }
                    return null;
                } catch (Throwable t) {
                    return t;
                }
            }

            protected void onPostExecute(Throwable throwable) {
                if (cancellationSignal2.isCanceled()) {
                    writeResultCallback2.onWriteCancelled();
                } else if (throwable == null) {
                    writeResultCallback2.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } else {
                    Log.e(PrintHelper.LOG_TAG, "Error writing printed content", throwable);
                    writeResultCallback2.onWriteFailed(null);
                }
            }
        }.execute(new Void[0]);
    }

    private Bitmap loadBitmap(Uri uri, Options o) throws FileNotFoundException {
        if (uri != null) {
            Context context = this.mContext;
            if (context != null) {
                InputStream is = null;
                try {
                    is = context.getContentResolver().openInputStream(uri);
                    Bitmap decodeStream = BitmapFactory.decodeStream(is, null, o);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException t) {
                            Log.w(LOG_TAG, "close fail ", t);
                        }
                    }
                    return decodeStream;
                } catch (Throwable th) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException t2) {
                            Log.w(LOG_TAG, "close fail ", t2);
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("bad argument to loadBitmap");
    }

    static Bitmap convertBitmapForColorMode(Bitmap original, int colorMode) {
        if (colorMode != 1) {
            return original;
        }
        Bitmap grayscale = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Config.ARGB_8888);
        Canvas c = new Canvas(grayscale);
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        p.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(original, 0.0f, 0.0f, p);
        c.setBitmap(null);
        return grayscale;
    }
}
