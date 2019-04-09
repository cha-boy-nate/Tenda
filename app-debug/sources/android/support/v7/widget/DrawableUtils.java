package android.support.v7.widget;

import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.WrappedDrawable;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.util.Log;
import java.lang.reflect.Field;

@RestrictTo({Scope.LIBRARY_GROUP})
public class DrawableUtils {
    public static final Rect INSETS_NONE = new Rect();
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;

    static {
        if (VERSION.SDK_INT >= 18) {
            try {
                sInsetsClazz = Class.forName("android.graphics.Insets");
            } catch (ClassNotFoundException e) {
            }
        }
    }

    private DrawableUtils() {
    }

    public static Rect getOpticalBounds(Drawable drawable) {
        if (sInsetsClazz != null) {
            try {
                drawable = DrawableCompat.unwrap(drawable);
                Object insets = drawable.getClass().getMethod("getOpticalInsets", new Class[0]).invoke(drawable, new Object[0]);
                if (insets != null) {
                    Rect result = new Rect();
                    for (Field field : sInsetsClazz.getFields()) {
                        Object obj;
                        String name = field.getName();
                        int hashCode = name.hashCode();
                        if (hashCode != -1383228885) {
                            if (hashCode != 115029) {
                                if (hashCode != 3317767) {
                                    if (hashCode == 108511772 && name.equals("right")) {
                                        obj = 2;
                                        switch (obj) {
                                            case null:
                                                result.left = field.getInt(insets);
                                                break;
                                            case 1:
                                                result.top = field.getInt(insets);
                                                break;
                                            case 2:
                                                result.right = field.getInt(insets);
                                                break;
                                            case 3:
                                                result.bottom = field.getInt(insets);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else if (name.equals("left")) {
                                    obj = null;
                                    switch (obj) {
                                        case null:
                                            result.left = field.getInt(insets);
                                            break;
                                        case 1:
                                            result.top = field.getInt(insets);
                                            break;
                                        case 2:
                                            result.right = field.getInt(insets);
                                            break;
                                        case 3:
                                            result.bottom = field.getInt(insets);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } else if (name.equals("top")) {
                                obj = 1;
                                switch (obj) {
                                    case null:
                                        result.left = field.getInt(insets);
                                        break;
                                    case 1:
                                        result.top = field.getInt(insets);
                                        break;
                                    case 2:
                                        result.right = field.getInt(insets);
                                        break;
                                    case 3:
                                        result.bottom = field.getInt(insets);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else if (name.equals("bottom")) {
                            obj = 3;
                            switch (obj) {
                                case null:
                                    result.left = field.getInt(insets);
                                    break;
                                case 1:
                                    result.top = field.getInt(insets);
                                    break;
                                case 2:
                                    result.right = field.getInt(insets);
                                    break;
                                case 3:
                                    result.bottom = field.getInt(insets);
                                    break;
                                default:
                                    break;
                            }
                        }
                        obj = -1;
                        switch (obj) {
                            case null:
                                result.left = field.getInt(insets);
                                break;
                            case 1:
                                result.top = field.getInt(insets);
                                break;
                            case 2:
                                result.right = field.getInt(insets);
                                break;
                            case 3:
                                result.bottom = field.getInt(insets);
                                break;
                            default:
                                break;
                        }
                    }
                    return result;
                }
            } catch (Exception e) {
                Log.e(TAG, "Couldn't obtain the optical insets. Ignoring.");
            }
        }
        return INSETS_NONE;
    }

    static void fixDrawable(@NonNull Drawable drawable) {
        if (VERSION.SDK_INT == 21 && VECTOR_DRAWABLE_CLAZZ_NAME.equals(drawable.getClass().getName())) {
            fixVectorDrawableTinting(drawable);
        }
    }

    public static boolean canSafelyMutateDrawable(@NonNull Drawable drawable) {
        if (VERSION.SDK_INT < 15 && (drawable instanceof InsetDrawable)) {
            return false;
        }
        if (VERSION.SDK_INT < 15 && (drawable instanceof GradientDrawable)) {
            return false;
        }
        if (VERSION.SDK_INT < 17 && (drawable instanceof LayerDrawable)) {
            return false;
        }
        if (drawable instanceof DrawableContainer) {
            ConstantState state = drawable.getConstantState();
            if (state instanceof DrawableContainerState) {
                for (Drawable child : ((DrawableContainerState) state).getChildren()) {
                    if (!canSafelyMutateDrawable(child)) {
                        return false;
                    }
                }
            }
        } else if (drawable instanceof WrappedDrawable) {
            return canSafelyMutateDrawable(((WrappedDrawable) drawable).getWrappedDrawable());
        } else {
            if (drawable instanceof DrawableWrapper) {
                return canSafelyMutateDrawable(((DrawableWrapper) drawable).getWrappedDrawable());
            }
            if (drawable instanceof ScaleDrawable) {
                return canSafelyMutateDrawable(((ScaleDrawable) drawable).getDrawable());
            }
        }
        return true;
    }

    private static void fixVectorDrawableTinting(Drawable drawable) {
        int[] originalState = drawable.getState();
        if (originalState != null) {
            if (originalState.length != 0) {
                drawable.setState(ThemeUtils.EMPTY_STATE_SET);
                drawable.setState(originalState);
            }
        }
        drawable.setState(ThemeUtils.CHECKED_STATE_SET);
        drawable.setState(originalState);
    }

    public static Mode parseTintMode(int value, Mode defaultMode) {
        if (value == 3) {
            return Mode.SRC_OVER;
        }
        if (value == 5) {
            return Mode.SRC_IN;
        }
        if (value == 9) {
            return Mode.SRC_ATOP;
        }
        switch (value) {
            case 14:
                return Mode.MULTIPLY;
            case 15:
                return Mode.SCREEN;
            case 16:
                return Mode.ADD;
            default:
                return defaultMode;
        }
    }
}
