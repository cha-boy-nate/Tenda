package android.support.v7.widget;

import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

class AppCompatHintHelper {
    static InputConnection onCreateInputConnection(InputConnection ic, EditorInfo outAttrs, View view) {
        if (ic != null && outAttrs.hintText == null) {
            for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
                if (parent instanceof WithHint) {
                    outAttrs.hintText = ((WithHint) parent).getHint();
                    break;
                }
            }
        }
        return ic;
    }

    private AppCompatHintHelper() {
    }
}
