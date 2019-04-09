package com.google.android.gms.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import com.google.android.gms.base.C0235R;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.SignInButtonCreator;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.dynamic.RemoteCreator.RemoteCreatorException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SignInButton extends FrameLayout implements OnClickListener {
    public static final int COLOR_AUTO = 2;
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int mColor;
    private int mSize;
    private View zaas;
    private OnClickListener zaat;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ButtonSize {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorScheme {
    }

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignInButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zaat = 0;
        context = context.getTheme().obtainStyledAttributes(attributeSet, C0235R.styleable.SignInButton, 0, 0);
        try {
            this.mSize = context.getInt(C0235R.styleable.SignInButton_buttonSize, 0);
            this.mColor = context.getInt(C0235R.styleable.SignInButton_colorScheme, 2);
            setStyle(this.mSize, this.mColor);
        } finally {
            context.recycle();
        }
    }

    public final void setSize(int i) {
        setStyle(i, this.mColor);
    }

    public final void setColorScheme(int i) {
        setStyle(this.mSize, i);
    }

    @Deprecated
    public final void setScopes(Scope[] scopeArr) {
        setStyle(this.mSize, this.mColor);
    }

    public final void setStyle(int i, int i2) {
        this.mSize = i;
        this.mColor = i2;
        i = getContext();
        i2 = this.zaas;
        if (i2 != 0) {
            removeView(i2);
        }
        try {
            this.zaas = SignInButtonCreator.createView(i, this.mSize, this.mColor);
        } catch (RemoteCreatorException e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            i2 = this.mSize;
            int i3 = this.mColor;
            View signInButtonImpl = new SignInButtonImpl(i);
            signInButtonImpl.configure(i.getResources(), i2, i3);
            this.zaas = signInButtonImpl;
        }
        addView(this.zaas);
        this.zaas.setEnabled(isEnabled());
        this.zaas.setOnClickListener(this);
    }

    @Deprecated
    public final void setStyle(int i, int i2, Scope[] scopeArr) {
        setStyle(i, i2);
    }

    public final void setOnClickListener(OnClickListener onClickListener) {
        this.zaat = onClickListener;
        onClickListener = this.zaas;
        if (onClickListener != null) {
            onClickListener.setOnClickListener(this);
        }
    }

    public final void setEnabled(boolean z) {
        super.setEnabled(z);
        this.zaas.setEnabled(z);
    }

    public final void onClick(View view) {
        OnClickListener onClickListener = this.zaat;
        if (onClickListener != null && view == this.zaas) {
            onClickListener.onClick(this);
        }
    }
}
