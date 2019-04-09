package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.C0185R;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.widget.TextView;

@RestrictTo({Scope.LIBRARY_GROUP})
public class DialogTitle extends TextView {
    public DialogTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogTitle(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            if (lineCount > 0 && layout.getEllipsisCount(lineCount - 1) > 0) {
                setSingleLine(false);
                setMaxLines(2);
                TypedArray a = getContext().obtainStyledAttributes(null, C0185R.styleable.TextAppearance, 16842817, 16973892);
                int textSize = a.getDimensionPixelSize(C0185R.styleable.TextAppearance_android_textSize, 0);
                if (textSize != 0) {
                    setTextSize(0, (float) textSize);
                }
                a.recycle();
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    public void setCustomSelectionActionModeCallback(Callback actionModeCallback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, actionModeCallback));
    }
}
