package android.support.v4.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.Window.Callback;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestrictTo({Scope.LIBRARY_GROUP})
public class KeyEventDispatcher {
    private static boolean sActionBarFieldsFetched = false;
    private static Method sActionBarOnMenuKeyMethod = null;
    private static boolean sDialogFieldsFetched = false;
    private static Field sDialogKeyListenerField = null;

    public interface Component {
        boolean superDispatchKeyEvent(KeyEvent keyEvent);
    }

    private KeyEventDispatcher() {
    }

    public static boolean dispatchBeforeHierarchy(@NonNull View root, @NonNull KeyEvent event) {
        return ViewCompat.dispatchUnhandledKeyEventBeforeHierarchy(root, event);
    }

    public static boolean dispatchKeyEvent(@NonNull Component component, @Nullable View root, @Nullable Callback callback, @NonNull KeyEvent event) {
        boolean z = false;
        if (component == null) {
            return false;
        }
        if (VERSION.SDK_INT >= 28) {
            return component.superDispatchKeyEvent(event);
        }
        if (callback instanceof Activity) {
            return activitySuperDispatchKeyEventPre28((Activity) callback, event);
        }
        if (callback instanceof Dialog) {
            return dialogSuperDispatchKeyEventPre28((Dialog) callback, event);
        }
        if ((root != null && ViewCompat.dispatchUnhandledKeyEventBeforeCallback(root, event)) || component.superDispatchKeyEvent(event)) {
            z = true;
        }
        return z;
    }

    private static boolean actionBarOnMenuKeyEventPre28(ActionBar actionBar, KeyEvent event) {
        if (!sActionBarFieldsFetched) {
            try {
                sActionBarOnMenuKeyMethod = actionBar.getClass().getMethod("onMenuKeyEvent", new Class[]{KeyEvent.class});
            } catch (NoSuchMethodException e) {
            }
            sActionBarFieldsFetched = true;
        }
        Method method = sActionBarOnMenuKeyMethod;
        if (method == null) {
            return false;
        }
        try {
            return ((Boolean) method.invoke(actionBar, new Object[]{event})).booleanValue();
        } catch (IllegalAccessException e2) {
        } catch (InvocationTargetException e3) {
        }
    }

    private static boolean activitySuperDispatchKeyEventPre28(Activity activity, KeyEvent event) {
        activity.onUserInteraction();
        Window win = activity.getWindow();
        if (win.hasFeature(8)) {
            ActionBar actionBar = activity.getActionBar();
            if (event.getKeyCode() == 82 && actionBar != null && actionBarOnMenuKeyEventPre28(actionBar, event)) {
                return true;
            }
        }
        if (win.superDispatchKeyEvent(event)) {
            return true;
        }
        View decor = win.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(decor, event)) {
            return true;
        }
        return event.dispatch(activity, decor != null ? decor.getKeyDispatcherState() : null, activity);
    }

    private static OnKeyListener getDialogKeyListenerPre28(Dialog dialog) {
        if (!sDialogFieldsFetched) {
            try {
                sDialogKeyListenerField = Dialog.class.getDeclaredField("mOnKeyListener");
                sDialogKeyListenerField.setAccessible(true);
            } catch (NoSuchFieldException e) {
            }
            sDialogFieldsFetched = true;
        }
        Field field = sDialogKeyListenerField;
        if (field == null) {
            return null;
        }
        try {
            return (OnKeyListener) field.get(dialog);
        } catch (IllegalAccessException e2) {
        }
    }

    private static boolean dialogSuperDispatchKeyEventPre28(Dialog dialog, KeyEvent event) {
        OnKeyListener onKeyListener = getDialogKeyListenerPre28(dialog);
        if (onKeyListener != null && onKeyListener.onKey(dialog, event.getKeyCode(), event)) {
            return true;
        }
        Window win = dialog.getWindow();
        if (win.superDispatchKeyEvent(event)) {
            return true;
        }
        View decor = win.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(decor, event)) {
            return true;
        }
        return event.dispatch(dialog, decor != null ? decor.getKeyDispatcherState() : null, dialog);
    }
}
