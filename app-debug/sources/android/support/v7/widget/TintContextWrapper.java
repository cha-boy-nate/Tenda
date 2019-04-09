package android.support.v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@RestrictTo({Scope.LIBRARY_GROUP})
public class TintContextWrapper extends ContextWrapper {
    private static final Object CACHE_LOCK = new Object();
    private static ArrayList<WeakReference<TintContextWrapper>> sCache;
    private final Resources mResources;
    private final Theme mTheme;

    public static Context wrap(@NonNull Context context) {
        if (!shouldWrap(context)) {
            return context;
        }
        synchronized (CACHE_LOCK) {
            if (sCache == null) {
                sCache = new ArrayList();
            } else {
                int i;
                WeakReference<TintContextWrapper> ref;
                for (i = sCache.size() - 1; i >= 0; i--) {
                    ref = (WeakReference) sCache.get(i);
                    if (ref == null || ref.get() == null) {
                        sCache.remove(i);
                    }
                }
                i = sCache.size() - 1;
                while (i >= 0) {
                    ref = (WeakReference) sCache.get(i);
                    TintContextWrapper wrapper = ref != null ? (TintContextWrapper) ref.get() : null;
                    if (wrapper == null || wrapper.getBaseContext() != context) {
                        i--;
                    } else {
                        return wrapper;
                    }
                }
            }
            TintContextWrapper wrapper2 = new TintContextWrapper(context);
            sCache.add(new WeakReference(wrapper2));
            return wrapper2;
        }
    }

    private static boolean shouldWrap(@NonNull Context context) {
        boolean z = false;
        if (!((context instanceof TintContextWrapper) || (context.getResources() instanceof TintResources))) {
            if (!(context.getResources() instanceof VectorEnabledTintResources)) {
                if (VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed()) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    private TintContextWrapper(@NonNull Context base) {
        super(base);
        if (VectorEnabledTintResources.shouldBeUsed()) {
            this.mResources = new VectorEnabledTintResources(this, base.getResources());
            this.mTheme = this.mResources.newTheme();
            this.mTheme.setTo(base.getTheme());
            return;
        }
        this.mResources = new TintResources(this, base.getResources());
        this.mTheme = null;
    }

    public Theme getTheme() {
        Theme theme = this.mTheme;
        return theme == null ? super.getTheme() : theme;
    }

    public void setTheme(int resid) {
        Theme theme = this.mTheme;
        if (theme == null) {
            super.setTheme(resid);
        } else {
            theme.applyStyle(resid, true);
        }
    }

    public Resources getResources() {
        return this.mResources;
    }

    public AssetManager getAssets() {
        return this.mResources.getAssets();
    }
}
