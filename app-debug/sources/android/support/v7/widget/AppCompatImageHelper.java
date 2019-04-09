package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.appcompat.C0185R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.ImageView;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AppCompatImageHelper {
    private TintInfo mImageTint;
    private TintInfo mInternalImageTint;
    private TintInfo mTmpInfo;
    private final ImageView mView;

    public AppCompatImageHelper(ImageView view) {
        this.mView = view;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attrs, C0185R.styleable.AppCompatImageView, defStyleAttr, 0);
        try {
            Drawable drawable = this.mView.getDrawable();
            if (drawable == null) {
                int id = a.getResourceId(C0185R.styleable.AppCompatImageView_srcCompat, -1);
                if (id != -1) {
                    drawable = AppCompatResources.getDrawable(this.mView.getContext(), id);
                    if (drawable != null) {
                        this.mView.setImageDrawable(drawable);
                    }
                }
            }
            if (drawable != null) {
                DrawableUtils.fixDrawable(drawable);
            }
            if (a.hasValue(C0185R.styleable.AppCompatImageView_tint)) {
                ImageViewCompat.setImageTintList(this.mView, a.getColorStateList(C0185R.styleable.AppCompatImageView_tint));
            }
            if (a.hasValue(C0185R.styleable.AppCompatImageView_tintMode)) {
                ImageViewCompat.setImageTintMode(this.mView, DrawableUtils.parseTintMode(a.getInt(C0185R.styleable.AppCompatImageView_tintMode, -1), null));
            }
            a.recycle();
        } catch (Throwable th) {
            a.recycle();
        }
    }

    public void setImageResource(int resId) {
        if (resId != 0) {
            Drawable d = AppCompatResources.getDrawable(this.mView.getContext(), resId);
            if (d != null) {
                DrawableUtils.fixDrawable(d);
            }
            this.mView.setImageDrawable(d);
        } else {
            this.mView.setImageDrawable(null);
        }
        applySupportImageTint();
    }

    boolean hasOverlappingRendering() {
        Drawable background = this.mView.getBackground();
        if (VERSION.SDK_INT < 21 || !(background instanceof RippleDrawable)) {
            return true;
        }
        return false;
    }

    void setSupportImageTintList(ColorStateList tint) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        TintInfo tintInfo = this.mImageTint;
        tintInfo.mTintList = tint;
        tintInfo.mHasTintList = true;
        applySupportImageTint();
    }

    ColorStateList getSupportImageTintList() {
        TintInfo tintInfo = this.mImageTint;
        return tintInfo != null ? tintInfo.mTintList : null;
    }

    void setSupportImageTintMode(Mode tintMode) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        TintInfo tintInfo = this.mImageTint;
        tintInfo.mTintMode = tintMode;
        tintInfo.mHasTintMode = true;
        applySupportImageTint();
    }

    Mode getSupportImageTintMode() {
        TintInfo tintInfo = this.mImageTint;
        return tintInfo != null ? tintInfo.mTintMode : null;
    }

    void applySupportImageTint() {
        Drawable imageViewDrawable = this.mView.getDrawable();
        if (imageViewDrawable != null) {
            DrawableUtils.fixDrawable(imageViewDrawable);
        }
        if (imageViewDrawable != null && (!shouldApplyFrameworkTintUsingColorFilter() || !applyFrameworkTintUsingColorFilter(imageViewDrawable))) {
            TintInfo tintInfo = this.mImageTint;
            if (tintInfo != null) {
                AppCompatDrawableManager.tintDrawable(imageViewDrawable, tintInfo, this.mView.getDrawableState());
            } else {
                tintInfo = this.mInternalImageTint;
                if (tintInfo != null) {
                    AppCompatDrawableManager.tintDrawable(imageViewDrawable, tintInfo, this.mView.getDrawableState());
                }
            }
        }
    }

    void setInternalImageTint(ColorStateList tint) {
        if (tint != null) {
            if (this.mInternalImageTint == null) {
                this.mInternalImageTint = new TintInfo();
            }
            TintInfo tintInfo = this.mInternalImageTint;
            tintInfo.mTintList = tint;
            tintInfo.mHasTintList = true;
        } else {
            this.mInternalImageTint = null;
        }
        applySupportImageTint();
    }

    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        int sdk = VERSION.SDK_INT;
        boolean z = true;
        if (sdk <= 21) {
            return sdk == 21;
        } else {
            if (this.mInternalImageTint == null) {
                z = false;
            }
            return z;
        }
    }

    private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable imageSource) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        TintInfo info = this.mTmpInfo;
        info.clear();
        ColorStateList tintList = ImageViewCompat.getImageTintList(this.mView);
        if (tintList != null) {
            info.mHasTintList = true;
            info.mTintList = tintList;
        }
        Mode mode = ImageViewCompat.getImageTintMode(this.mView);
        if (mode != null) {
            info.mHasTintMode = true;
            info.mTintMode = mode;
        }
        if (!info.mHasTintList) {
            if (!info.mHasTintMode) {
                return false;
            }
        }
        AppCompatDrawableManager.tintDrawable(imageSource, info, this.mView.getDrawableState());
        return true;
    }
}
