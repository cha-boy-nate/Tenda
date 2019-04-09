package android.support.v7.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0185R;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.View.OnClickListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AppCompatViewInflater {
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final String[] sClassPrefixList = new String[]{"android.widget.", "android.view.", "android.webkit."};
    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap();
    private static final Class<?>[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    private static final int[] sOnClickAttrs = new int[]{16843375};
    private final Object[] mConstructorArgs = new Object[2];

    private static class DeclaredOnClickListener implements OnClickListener {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;

        @android.support.annotation.NonNull
        private void resolveMethod(@android.support.annotation.Nullable android.content.Context r6, @android.support.annotation.NonNull java.lang.String r7) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:20:0x0091 in {7, 8, 9, 12, 13, 16, 17, 19} preds:[]
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
            r5 = this;
        L_0x0000:
            if (r6 == 0) goto L_0x0031;
        L_0x0002:
            r0 = r6.isRestricted();	 Catch:{ NoSuchMethodException -> 0x0022 }
            if (r0 != 0) goto L_0x0021;	 Catch:{ NoSuchMethodException -> 0x0022 }
        L_0x0008:
            r0 = r6.getClass();	 Catch:{ NoSuchMethodException -> 0x0022 }
            r1 = r5.mMethodName;	 Catch:{ NoSuchMethodException -> 0x0022 }
            r2 = 1;	 Catch:{ NoSuchMethodException -> 0x0022 }
            r2 = new java.lang.Class[r2];	 Catch:{ NoSuchMethodException -> 0x0022 }
            r3 = 0;	 Catch:{ NoSuchMethodException -> 0x0022 }
            r4 = android.view.View.class;	 Catch:{ NoSuchMethodException -> 0x0022 }
            r2[r3] = r4;	 Catch:{ NoSuchMethodException -> 0x0022 }
            r0 = r0.getMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x0022 }
            if (r0 == 0) goto L_0x0021;	 Catch:{ NoSuchMethodException -> 0x0022 }
        L_0x001c:
            r5.mResolvedMethod = r0;	 Catch:{ NoSuchMethodException -> 0x0022 }
            r5.mResolvedContext = r6;	 Catch:{ NoSuchMethodException -> 0x0022 }
            return;
        L_0x0021:
            goto L_0x0023;
        L_0x0022:
            r0 = move-exception;
        L_0x0023:
            r0 = r6 instanceof android.content.ContextWrapper;
            if (r0 == 0) goto L_0x002f;
        L_0x0027:
            r0 = r6;
            r0 = (android.content.ContextWrapper) r0;
            r6 = r0.getBaseContext();
            goto L_0x0000;
        L_0x002f:
            r6 = 0;
            goto L_0x0000;
        L_0x0031:
            r0 = r5.mHostView;
            r0 = r0.getId();
            r1 = -1;
            if (r0 != r1) goto L_0x003d;
        L_0x003a:
            r1 = "";
            goto L_0x0061;
        L_0x003d:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = " with id '";
            r1.append(r2);
            r2 = r5.mHostView;
            r2 = r2.getContext();
            r2 = r2.getResources();
            r2 = r2.getResourceEntryName(r0);
            r1.append(r2);
            r2 = "'";
            r1.append(r2);
            r1 = r1.toString();
            r2 = new java.lang.IllegalStateException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Could not find method ";
            r3.append(r4);
            r4 = r5.mMethodName;
            r3.append(r4);
            r4 = "(View) in a parent or ancestor Context for android:onClick ";
            r3.append(r4);
            r4 = "attribute defined on view ";
            r3.append(r4);
            r4 = r5.mHostView;
            r4 = r4.getClass();
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatViewInflater.DeclaredOnClickListener.resolveMethod(android.content.Context, java.lang.String):void");
        }

        public DeclaredOnClickListener(@NonNull View hostView, @NonNull String methodName) {
            this.mHostView = hostView;
            this.mMethodName = methodName;
        }

        public void onClick(@NonNull View v) {
            if (this.mResolvedMethod == null) {
                resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke(this.mResolvedContext, new Object[]{v});
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e2) {
                throw new IllegalStateException("Could not execute method for android:onClick", e2);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final android.view.View createView(android.view.View r5, java.lang.String r6, @android.support.annotation.NonNull android.content.Context r7, @android.support.annotation.NonNull android.util.AttributeSet r8, boolean r9, boolean r10, boolean r11, boolean r12) {
        /*
        r4 = this;
        r0 = r7;
        if (r9 == 0) goto L_0x0009;
    L_0x0003:
        if (r5 == 0) goto L_0x0009;
    L_0x0005:
        r7 = r5.getContext();
    L_0x0009:
        if (r10 != 0) goto L_0x000d;
    L_0x000b:
        if (r11 == 0) goto L_0x0011;
    L_0x000d:
        r7 = themifyContext(r7, r8, r10, r11);
    L_0x0011:
        if (r12 == 0) goto L_0x0017;
    L_0x0013:
        r7 = android.support.v7.widget.TintContextWrapper.wrap(r7);
    L_0x0017:
        r1 = 0;
        r2 = -1;
        r3 = r6.hashCode();
        switch(r3) {
            case -1946472170: goto L_0x00a1;
            case -1455429095: goto L_0x0096;
            case -1346021293: goto L_0x008b;
            case -938935918: goto L_0x0081;
            case -937446323: goto L_0x0077;
            case -658531749: goto L_0x006c;
            case -339785223: goto L_0x0062;
            case 776382189: goto L_0x0058;
            case 1125864064: goto L_0x004e;
            case 1413872058: goto L_0x0043;
            case 1601505219: goto L_0x0038;
            case 1666676343: goto L_0x002d;
            case 2001146706: goto L_0x0022;
            default: goto L_0x0020;
        };
    L_0x0020:
        goto L_0x00ab;
    L_0x0022:
        r3 = "Button";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x002a:
        r2 = 2;
        goto L_0x00ab;
    L_0x002d:
        r3 = "EditText";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x0035:
        r2 = 3;
        goto L_0x00ab;
    L_0x0038:
        r3 = "CheckBox";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x0040:
        r2 = 6;
        goto L_0x00ab;
    L_0x0043:
        r3 = "AutoCompleteTextView";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x004b:
        r2 = 9;
        goto L_0x00ab;
    L_0x004e:
        r3 = "ImageView";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x0056:
        r2 = 1;
        goto L_0x00ab;
    L_0x0058:
        r3 = "RadioButton";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x0060:
        r2 = 7;
        goto L_0x00ab;
    L_0x0062:
        r3 = "Spinner";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x006a:
        r2 = 4;
        goto L_0x00ab;
    L_0x006c:
        r3 = "SeekBar";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x0074:
        r2 = 12;
        goto L_0x00ab;
    L_0x0077:
        r3 = "ImageButton";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x007f:
        r2 = 5;
        goto L_0x00ab;
    L_0x0081:
        r3 = "TextView";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x0089:
        r2 = 0;
        goto L_0x00ab;
    L_0x008b:
        r3 = "MultiAutoCompleteTextView";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x0093:
        r2 = 10;
        goto L_0x00ab;
    L_0x0096:
        r3 = "CheckedTextView";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x009e:
        r2 = 8;
        goto L_0x00ab;
    L_0x00a1:
        r3 = "RatingBar";
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0020;
    L_0x00a9:
        r2 = 11;
    L_0x00ab:
        switch(r2) {
            case 0: goto L_0x0114;
            case 1: goto L_0x010c;
            case 2: goto L_0x0104;
            case 3: goto L_0x00fc;
            case 4: goto L_0x00f4;
            case 5: goto L_0x00ec;
            case 6: goto L_0x00e4;
            case 7: goto L_0x00dc;
            case 8: goto L_0x00d4;
            case 9: goto L_0x00cc;
            case 10: goto L_0x00c4;
            case 11: goto L_0x00bc;
            case 12: goto L_0x00b4;
            default: goto L_0x00ae;
        };
    L_0x00ae:
        r1 = r4.createView(r7, r6, r8);
        goto L_0x011c;
    L_0x00b4:
        r1 = r4.createSeekBar(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00bc:
        r1 = r4.createRatingBar(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00c4:
        r1 = r4.createMultiAutoCompleteTextView(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00cc:
        r1 = r4.createAutoCompleteTextView(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00d4:
        r1 = r4.createCheckedTextView(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00dc:
        r1 = r4.createRadioButton(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00e4:
        r1 = r4.createCheckBox(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00ec:
        r1 = r4.createImageButton(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00f4:
        r1 = r4.createSpinner(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x00fc:
        r1 = r4.createEditText(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x0104:
        r1 = r4.createButton(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x010c:
        r1 = r4.createImageView(r7, r8);
        r4.verifyNotNull(r1, r6);
        goto L_0x011c;
    L_0x0114:
        r1 = r4.createTextView(r7, r8);
        r4.verifyNotNull(r1, r6);
    L_0x011c:
        if (r1 != 0) goto L_0x0124;
    L_0x011e:
        if (r0 == r7) goto L_0x0124;
    L_0x0120:
        r1 = r4.createViewFromTag(r7, r6, r8);
    L_0x0124:
        if (r1 == 0) goto L_0x0129;
    L_0x0126:
        r4.checkOnClickListener(r1, r8);
    L_0x0129:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatViewInflater.createView(android.view.View, java.lang.String, android.content.Context, android.util.AttributeSet, boolean, boolean, boolean, boolean):android.view.View");
    }

    @NonNull
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        return new AppCompatTextView(context, attrs);
    }

    @NonNull
    protected AppCompatImageView createImageView(Context context, AttributeSet attrs) {
        return new AppCompatImageView(context, attrs);
    }

    @NonNull
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        return new AppCompatButton(context, attrs);
    }

    @NonNull
    protected AppCompatEditText createEditText(Context context, AttributeSet attrs) {
        return new AppCompatEditText(context, attrs);
    }

    @NonNull
    protected AppCompatSpinner createSpinner(Context context, AttributeSet attrs) {
        return new AppCompatSpinner(context, attrs);
    }

    @NonNull
    protected AppCompatImageButton createImageButton(Context context, AttributeSet attrs) {
        return new AppCompatImageButton(context, attrs);
    }

    @NonNull
    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attrs) {
        return new AppCompatCheckBox(context, attrs);
    }

    @NonNull
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        return new AppCompatRadioButton(context, attrs);
    }

    @NonNull
    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attrs) {
        return new AppCompatCheckedTextView(context, attrs);
    }

    @NonNull
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attrs) {
        return new AppCompatAutoCompleteTextView(context, attrs);
    }

    @NonNull
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        return new AppCompatMultiAutoCompleteTextView(context, attrs);
    }

    @NonNull
    protected AppCompatRatingBar createRatingBar(Context context, AttributeSet attrs) {
        return new AppCompatRatingBar(context, attrs);
    }

    @NonNull
    protected AppCompatSeekBar createSeekBar(Context context, AttributeSet attrs) {
        return new AppCompatSeekBar(context, attrs);
    }

    private void verifyNotNull(View view, String name) {
        if (view == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getClass().getName());
            stringBuilder.append(" asked to inflate view for <");
            stringBuilder.append(name);
            stringBuilder.append(">, but returned null");
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    @Nullable
    protected View createView(Context context, String name, AttributeSet attrs) {
        return null;
    }

    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }
        View view;
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = attrs;
            if (-1 == name.indexOf(46)) {
                for (View view2 : sClassPrefixList) {
                    view2 = createViewByPrefix(context, name, view2);
                    if (view2 != null) {
                        return view2;
                    }
                }
                Object[] objArr = this.mConstructorArgs;
                objArr[0] = null;
                objArr[1] = null;
                return null;
            }
            View createViewByPrefix = createViewByPrefix(context, name, null);
            Object[] objArr2 = this.mConstructorArgs;
            objArr2[0] = null;
            objArr2[1] = null;
            return createViewByPrefix;
        } catch (Exception e) {
            return null;
        } finally {
            view2 = this.mConstructorArgs;
            view2[0] = null;
            view2[1] = null;
        }
    }

    private void checkOnClickListener(View view, AttributeSet attrs) {
        Context context = view.getContext();
        if (context instanceof ContextWrapper) {
            if (VERSION.SDK_INT < 15 || ViewCompat.hasOnClickListeners(view)) {
                TypedArray a = context.obtainStyledAttributes(attrs, sOnClickAttrs);
                String handlerName = a.getString(null);
                if (handlerName != null) {
                    view.setOnClickListener(new DeclaredOnClickListener(view, handlerName));
                }
                a.recycle();
            }
        }
    }

    private View createViewByPrefix(Context context, String name, String prefix) throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = (Constructor) sConstructorMap.get(name);
        if (constructor == null) {
            try {
                String stringBuilder;
                ClassLoader classLoader = context.getClassLoader();
                if (prefix != null) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(prefix);
                    stringBuilder2.append(name);
                    stringBuilder = stringBuilder2.toString();
                } else {
                    stringBuilder = name;
                }
                constructor = classLoader.loadClass(stringBuilder).asSubclass(View.class).getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
                return null;
            }
        }
        constructor.setAccessible(true);
        return (View) constructor.newInstance(this.mConstructorArgs);
    }

    private static Context themifyContext(Context context, AttributeSet attrs, boolean useAndroidTheme, boolean useAppTheme) {
        TypedArray a = context.obtainStyledAttributes(attrs, C0185R.styleable.View, 0, 0);
        int themeId = 0;
        if (useAndroidTheme) {
            themeId = a.getResourceId(C0185R.styleable.View_android_theme, 0);
        }
        if (useAppTheme && themeId == 0) {
            themeId = a.getResourceId(C0185R.styleable.View_theme, 0);
            if (themeId != 0) {
                Log.i(LOG_TAG, "app:theme is now deprecated. Please move to using android:theme instead.");
            }
        }
        a.recycle();
        if (themeId == 0) {
            return context;
        }
        if ((context instanceof ContextThemeWrapper) && ((ContextThemeWrapper) context).getThemeResId() == themeId) {
            return context;
        }
        return new ContextThemeWrapper(context, themeId);
    }
}
