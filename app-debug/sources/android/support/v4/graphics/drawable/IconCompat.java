package android.support.v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Preconditions;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

public class IconCompat extends CustomVersionedParcelable {
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final Mode DEFAULT_TINT_MODE = Mode.SRC_IN;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final String EXTRA_INT1 = "int1";
    private static final String EXTRA_INT2 = "int2";
    private static final String EXTRA_OBJ = "obj";
    private static final String EXTRA_TINT_LIST = "tint_list";
    private static final String EXTRA_TINT_MODE = "tint_mode";
    private static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_UNKNOWN = -1;
    @RestrictTo({Scope.LIBRARY})
    public byte[] mData;
    @RestrictTo({Scope.LIBRARY})
    public int mInt1;
    @RestrictTo({Scope.LIBRARY})
    public int mInt2;
    Object mObj1;
    @RestrictTo({Scope.LIBRARY})
    public Parcelable mParcelable;
    @RestrictTo({Scope.LIBRARY})
    public ColorStateList mTintList = null;
    Mode mTintMode = DEFAULT_TINT_MODE;
    @RestrictTo({Scope.LIBRARY})
    public String mTintModeStr;
    @RestrictTo({Scope.LIBRARY})
    public int mType;

    @RestrictTo({Scope.LIBRARY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IconType {
    }

    public static IconCompat createWithResource(Context context, @DrawableRes int resId) {
        if (context != null) {
            return createWithResource(context.getResources(), context.getPackageName(), resId);
        }
        throw new IllegalArgumentException("Context must not be null.");
    }

    @RestrictTo({Scope.LIBRARY})
    public static IconCompat createWithResource(Resources r, String pkg, @DrawableRes int resId) {
        if (pkg == null) {
            throw new IllegalArgumentException("Package must not be null.");
        } else if (resId != 0) {
            IconCompat rep = new IconCompat(2);
            rep.mInt1 = resId;
            if (r != null) {
                try {
                    rep.mObj1 = r.getResourceName(resId);
                } catch (NotFoundException e) {
                    throw new IllegalArgumentException("Icon resource cannot be found");
                }
            }
            rep.mObj1 = pkg;
            return rep;
        } else {
            throw new IllegalArgumentException("Drawable resource ID must not be 0");
        }
    }

    public static IconCompat createWithBitmap(Bitmap bits) {
        if (bits != null) {
            IconCompat rep = new IconCompat(1);
            rep.mObj1 = bits;
            return rep;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static IconCompat createWithAdaptiveBitmap(Bitmap bits) {
        if (bits != null) {
            IconCompat rep = new IconCompat(5);
            rep.mObj1 = bits;
            return rep;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static IconCompat createWithData(byte[] data, int offset, int length) {
        if (data != null) {
            IconCompat rep = new IconCompat(3);
            rep.mObj1 = data;
            rep.mInt1 = offset;
            rep.mInt2 = length;
            return rep;
        }
        throw new IllegalArgumentException("Data must not be null.");
    }

    public static IconCompat createWithContentUri(String uri) {
        if (uri != null) {
            IconCompat rep = new IconCompat(4);
            rep.mObj1 = uri;
            return rep;
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    public static IconCompat createWithContentUri(Uri uri) {
        if (uri != null) {
            return createWithContentUri(uri.toString());
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    private IconCompat(int mType) {
        this.mType = mType;
    }

    public int getType() {
        if (this.mType != -1 || VERSION.SDK_INT < 23) {
            return this.mType;
        }
        return getType((Icon) this.mObj1);
    }

    @NonNull
    public String getResPackage() {
        if (this.mType == -1 && VERSION.SDK_INT >= 23) {
            return getResPackage((Icon) this.mObj1);
        }
        if (this.mType == 2) {
            return ((String) this.mObj1).split(":", -1)[0];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResPackage() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @IdRes
    public int getResId() {
        if (this.mType == -1 && VERSION.SDK_INT >= 23) {
            return getResId((Icon) this.mObj1);
        }
        if (this.mType == 2) {
            return this.mInt1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResId() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @NonNull
    public Uri getUri() {
        if (this.mType != -1 || VERSION.SDK_INT < 23) {
            return Uri.parse((String) this.mObj1);
        }
        return getUri((Icon) this.mObj1);
    }

    public IconCompat setTint(@ColorInt int tint) {
        return setTintList(ColorStateList.valueOf(tint));
    }

    public IconCompat setTintList(ColorStateList tintList) {
        this.mTintList = tintList;
        return this;
    }

    public IconCompat setTintMode(Mode mode) {
        this.mTintMode = mode;
        return this;
    }

    @RequiresApi(23)
    public Icon toIcon() {
        int i = this.mType;
        if (i == -1) {
            return (Icon) this.mObj1;
        }
        Icon icon;
        switch (i) {
            case 1:
                icon = Icon.createWithBitmap((Bitmap) this.mObj1);
                break;
            case 2:
                icon = Icon.createWithResource(getResPackage(), this.mInt1);
                break;
            case 3:
                icon = Icon.createWithData((byte[]) this.mObj1, this.mInt1, this.mInt2);
                break;
            case 4:
                icon = Icon.createWithContentUri((String) this.mObj1);
                break;
            case 5:
                if (VERSION.SDK_INT < 26) {
                    icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
                    break;
                }
                icon = Icon.createWithAdaptiveBitmap((Bitmap) this.mObj1);
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            icon.setTintList(colorStateList);
        }
        Mode mode = this.mTintMode;
        if (mode != DEFAULT_TINT_MODE) {
            icon.setTintMode(mode);
        }
        return icon;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void checkResource(Context context) {
        if (this.mType == 2) {
            String resPackage = this.mObj1;
            if (resPackage.contains(":")) {
                String resName = resPackage.split(":", -1)[1];
                String resType = resName.split("/", -1)[0];
                resName = resName.split("/", -1)[1];
                resPackage = resPackage.split(":", -1)[0];
                int id = getResources(context, resPackage).getIdentifier(resName, resType, resPackage);
                if (this.mInt1 != id) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Id has changed for ");
                    stringBuilder.append(resPackage);
                    stringBuilder.append("/");
                    stringBuilder.append(resName);
                    Log.i(str, stringBuilder.toString());
                    this.mInt1 = id;
                }
            }
        }
    }

    public Drawable loadDrawable(Context context) {
        checkResource(context);
        if (VERSION.SDK_INT >= 23) {
            return toIcon().loadDrawable(context);
        }
        Drawable result = loadDrawableInner(context);
        if (!(result == null || (this.mTintList == null && this.mTintMode == DEFAULT_TINT_MODE))) {
            result.mutate();
            DrawableCompat.setTintList(result, this.mTintList);
            DrawableCompat.setTintMode(result, this.mTintMode);
        }
        return result;
    }

    private Drawable loadDrawableInner(Context context) {
        String str;
        StringBuilder stringBuilder;
        int i = 0;
        switch (this.mType) {
            case 1:
                return new BitmapDrawable(context.getResources(), (Bitmap) this.mObj1);
            case 2:
                String resPackage = getResPackage();
                if (TextUtils.isEmpty(resPackage)) {
                    resPackage = context.getPackageName();
                }
                try {
                    i = ResourcesCompat.getDrawable(getResources(context, resPackage), this.mInt1, context.getTheme());
                    return i;
                } catch (RuntimeException e) {
                    Log.e(TAG, String.format("Unable to load resource 0x%08x from pkg=%s", new Object[]{Integer.valueOf(this.mInt1), this.mObj1}), e);
                    break;
                }
            case 3:
                return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[]) this.mObj1, this.mInt1, this.mInt2));
            case 4:
                Uri uri = Uri.parse((String) this.mObj1);
                String scheme = uri.getScheme();
                InputStream is = null;
                if (!"content".equals(scheme)) {
                    if (!"file".equals(scheme)) {
                        try {
                            is = new FileInputStream(new File((String) this.mObj1));
                        } catch (FileNotFoundException e2) {
                            str = TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Unable to load image from path: ");
                            stringBuilder.append(uri);
                            Log.w(str, stringBuilder.toString(), e2);
                        }
                        if (is != null) {
                            return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is));
                        }
                    }
                }
                try {
                    is = context.getContentResolver().openInputStream(uri);
                } catch (Exception e3) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to load image from URI: ");
                    stringBuilder.append(uri);
                    Log.w(str, stringBuilder.toString(), e3);
                }
                if (is != null) {
                    return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is));
                }
                break;
            case 5:
                return new BitmapDrawable(context.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
            default:
                break;
        }
        return null;
    }

    private static Resources getResources(Context context, String resPackage) {
        if ("android".equals(resPackage)) {
            return Resources.getSystem();
        }
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(resPackage, 8192);
            if (ai != null) {
                return pm.getResourcesForApplication(ai);
            }
            return null;
        } catch (NameNotFoundException e) {
            Log.e(TAG, String.format("Unable to find pkg=%s for icon", new Object[]{resPackage}), e);
            return null;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void addToShortcutIntent(@NonNull Intent outIntent, @Nullable Drawable badge, @NonNull Context c) {
        Bitmap icon;
        checkResource(c);
        int i = this.mType;
        if (i != 5) {
            switch (i) {
                case 1:
                    icon = (Bitmap) this.mObj1;
                    if (badge != null) {
                        icon = icon.copy(icon.getConfig(), true);
                        break;
                    }
                    break;
                case 2:
                    try {
                        Context context = c.createPackageContext(getResPackage(), 0);
                        if (badge == null) {
                            outIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(context, this.mInt1));
                            return;
                        }
                        Drawable dr = ContextCompat.getDrawable(context, this.mInt1);
                        if (dr.getIntrinsicWidth() > 0) {
                            if (dr.getIntrinsicHeight() > 0) {
                                icon = Bitmap.createBitmap(dr.getIntrinsicWidth(), dr.getIntrinsicHeight(), Config.ARGB_8888);
                                dr.setBounds(0, 0, icon.getWidth(), icon.getHeight());
                                dr.draw(new Canvas(icon));
                                break;
                            }
                        }
                        int size = ((ActivityManager) context.getSystemService("activity")).getLauncherLargeIconSize();
                        icon = Bitmap.createBitmap(size, size, Config.ARGB_8888);
                        dr.setBounds(0, 0, icon.getWidth(), icon.getHeight());
                        dr.draw(new Canvas(icon));
                    } catch (NameNotFoundException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Can't find package ");
                        stringBuilder.append(this.mObj1);
                        throw new IllegalArgumentException(stringBuilder.toString(), e);
                    }
                default:
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
            }
        }
        icon = createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
        if (badge != null) {
            i = icon.getWidth();
            int h = icon.getHeight();
            badge.setBounds(i / 2, h / 2, i, h);
            badge.draw(new Canvas(icon));
        }
        outIntent.putExtra("android.intent.extra.shortcut.ICON", icon);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        int i = this.mType;
        if (i != -1) {
            switch (i) {
                case 1:
                case 5:
                    bundle.putParcelable(EXTRA_OBJ, (Bitmap) this.mObj1);
                    break;
                case 2:
                case 4:
                    bundle.putString(EXTRA_OBJ, (String) this.mObj1);
                    break;
                case 3:
                    bundle.putByteArray(EXTRA_OBJ, (byte[]) this.mObj1);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid icon");
            }
        }
        bundle.putParcelable(EXTRA_OBJ, (Parcelable) this.mObj1);
        bundle.putInt(EXTRA_TYPE, this.mType);
        bundle.putInt(EXTRA_INT1, this.mInt1);
        bundle.putInt(EXTRA_INT2, this.mInt2);
        Parcelable parcelable = this.mTintList;
        if (parcelable != null) {
            bundle.putParcelable(EXTRA_TINT_LIST, parcelable);
        }
        Mode mode = this.mTintMode;
        if (mode != DEFAULT_TINT_MODE) {
            bundle.putString(EXTRA_TINT_MODE, mode.name());
        }
        return bundle;
    }

    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        StringBuilder sb = new StringBuilder("Icon(typ=").append(typeToString(this.mType));
        switch (this.mType) {
            case 1:
            case 5:
                sb.append(" size=");
                sb.append(((Bitmap) this.mObj1).getWidth());
                sb.append("x");
                sb.append(((Bitmap) this.mObj1).getHeight());
                break;
            case 2:
                sb.append(" pkg=");
                sb.append(getResPackage());
                sb.append(" id=");
                sb.append(String.format("0x%08x", new Object[]{Integer.valueOf(getResId())}));
                break;
            case 3:
                sb.append(" len=");
                sb.append(this.mInt1);
                if (this.mInt2 != 0) {
                    sb.append(" off=");
                    sb.append(this.mInt2);
                    break;
                }
                break;
            case 4:
                sb.append(" uri=");
                sb.append(this.mObj1);
                break;
            default:
                break;
        }
        if (this.mTintList != null) {
            sb.append(" tint=");
            sb.append(this.mTintList);
        }
        if (this.mTintMode != DEFAULT_TINT_MODE) {
            sb.append(" mode=");
            sb.append(this.mTintMode);
        }
        sb.append(")");
        return sb.toString();
    }

    public void onPreParceling(boolean isStream) {
        this.mTintModeStr = this.mTintMode.name();
        int i = this.mType;
        if (i != -1) {
            switch (i) {
                case 1:
                case 5:
                    if (isStream) {
                        Bitmap bitmap = this.mObj1;
                        ByteArrayOutputStream data = new ByteArrayOutputStream();
                        bitmap.compress(CompressFormat.PNG, 90, data);
                        this.mData = data.toByteArray();
                        return;
                    }
                    this.mParcelable = (Parcelable) this.mObj1;
                    return;
                case 2:
                    this.mData = ((String) this.mObj1).getBytes(Charset.forName("UTF-16"));
                    return;
                case 3:
                    this.mData = (byte[]) this.mObj1;
                    return;
                case 4:
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                    return;
                default:
                    return;
            }
        } else if (isStream) {
            throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
        } else {
            this.mParcelable = (Parcelable) this.mObj1;
        }
    }

    public void onPostParceling() {
        this.mTintMode = Mode.valueOf(this.mTintModeStr);
        int i = this.mType;
        Parcelable parcelable;
        if (i != -1) {
            switch (i) {
                case 1:
                case 5:
                    parcelable = this.mParcelable;
                    if (parcelable != null) {
                        this.mObj1 = parcelable;
                        return;
                    }
                    Object obj = this.mData;
                    this.mObj1 = obj;
                    this.mType = 3;
                    this.mInt1 = 0;
                    this.mInt2 = obj.length;
                    return;
                case 2:
                case 4:
                    this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                    return;
                case 3:
                    this.mObj1 = this.mData;
                    return;
                default:
                    return;
            }
        }
        parcelable = this.mParcelable;
        if (parcelable != null) {
            this.mObj1 = parcelable;
            return;
        }
        throw new IllegalArgumentException("Invalid icon");
    }

    private static String typeToString(int x) {
        switch (x) {
            case 1:
                return "BITMAP";
            case 2:
                return "RESOURCE";
            case 3:
                return "DATA";
            case 4:
                return "URI";
            case 5:
                return "BITMAP_MASKABLE";
            default:
                return "UNKNOWN";
        }
    }

    @Nullable
    public static IconCompat createFromBundle(@NonNull Bundle bundle) {
        int type = bundle.getInt(EXTRA_TYPE);
        IconCompat icon = new IconCompat(type);
        icon.mInt1 = bundle.getInt(EXTRA_INT1);
        icon.mInt2 = bundle.getInt(EXTRA_INT2);
        if (bundle.containsKey(EXTRA_TINT_LIST)) {
            icon.mTintList = (ColorStateList) bundle.getParcelable(EXTRA_TINT_LIST);
        }
        if (bundle.containsKey(EXTRA_TINT_MODE)) {
            icon.mTintMode = Mode.valueOf(bundle.getString(EXTRA_TINT_MODE));
        }
        if (type != -1) {
            switch (type) {
                case 1:
                case 5:
                    break;
                case 2:
                case 4:
                    icon.mObj1 = bundle.getString(EXTRA_OBJ);
                    break;
                case 3:
                    icon.mObj1 = bundle.getByteArray(EXTRA_OBJ);
                    break;
                default:
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown type ");
                    stringBuilder.append(type);
                    Log.w(str, stringBuilder.toString());
                    return null;
            }
        }
        icon.mObj1 = bundle.getParcelable(EXTRA_OBJ);
        return icon;
    }

    @Nullable
    @RequiresApi(23)
    public static IconCompat createFromIcon(@NonNull Context context, @NonNull Icon icon) {
        Preconditions.checkNotNull(icon);
        int type = getType(icon);
        if (type == 2) {
            String resPackage = getResPackage(icon);
            try {
                return createWithResource(getResources(context, resPackage), resPackage, getResId(icon));
            } catch (NotFoundException e) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        } else if (type == 4) {
            return createWithContentUri(getUri(icon));
        } else {
            IconCompat iconCompat = new IconCompat(-1);
            iconCompat.mObj1 = icon;
            return iconCompat;
        }
    }

    @Nullable
    @RequiresApi(23)
    @RestrictTo({Scope.LIBRARY_GROUP})
    public static IconCompat createFromIcon(@NonNull Icon icon) {
        Preconditions.checkNotNull(icon);
        int type = getType(icon);
        if (type == 2) {
            return createWithResource(null, getResPackage(icon), getResId(icon));
        }
        if (type == 4) {
            return createWithContentUri(getUri(icon));
        }
        IconCompat iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = icon;
        return iconCompat;
    }

    @RequiresApi(23)
    private static int getType(@NonNull Icon icon) {
        String str;
        StringBuilder stringBuilder;
        if (VERSION.SDK_INT >= 28) {
            return icon.getType();
        }
        int i = -1;
        try {
            i = ((Integer) icon.getClass().getMethod("getType", new Class[0]).invoke(icon, new Object[0])).intValue();
            return i;
        } catch (IllegalAccessException e) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get icon type ");
            stringBuilder.append(icon);
            Log.e(str, stringBuilder.toString(), e);
            return i;
        } catch (InvocationTargetException e2) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get icon type ");
            stringBuilder.append(icon);
            Log.e(str, stringBuilder.toString(), e2);
            return i;
        } catch (NoSuchMethodException e3) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get icon type ");
            stringBuilder.append(icon);
            Log.e(str, stringBuilder.toString(), e3);
            return i;
        }
    }

    @Nullable
    @RequiresApi(23)
    private static String getResPackage(@NonNull Icon icon) {
        if (VERSION.SDK_INT >= 28) {
            return icon.getResPackage();
        }
        try {
            return (String) icon.getClass().getMethod("getResPackage", new Class[0]).invoke(icon, new Object[0]);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon package", e);
            return null;
        } catch (InvocationTargetException e2) {
            Log.e(TAG, "Unable to get icon package", e2);
            return null;
        } catch (NoSuchMethodException e3) {
            Log.e(TAG, "Unable to get icon package", e3);
            return null;
        }
    }

    @RequiresApi(23)
    @DrawableRes
    @IdRes
    private static int getResId(@NonNull Icon icon) {
        if (VERSION.SDK_INT >= 28) {
            return icon.getResId();
        }
        int i = 0;
        try {
            i = ((Integer) icon.getClass().getMethod("getResId", new Class[0]).invoke(icon, new Object[0])).intValue();
            return i;
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon resource", e);
            return i;
        } catch (InvocationTargetException e2) {
            Log.e(TAG, "Unable to get icon resource", e2);
            return i;
        } catch (NoSuchMethodException e3) {
            Log.e(TAG, "Unable to get icon resource", e3);
            return i;
        }
    }

    @Nullable
    @RequiresApi(23)
    private static Uri getUri(@NonNull Icon icon) {
        if (VERSION.SDK_INT >= 28) {
            return icon.getUri();
        }
        try {
            return (Uri) icon.getClass().getMethod("getUri", new Class[0]).invoke(icon, new Object[0]);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon uri", e);
            return null;
        } catch (InvocationTargetException e2) {
            Log.e(TAG, "Unable to get icon uri", e2);
            return null;
        } catch (NoSuchMethodException e3) {
            Log.e(TAG, "Unable to get icon uri", e3);
            return null;
        }
    }

    @VisibleForTesting
    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap adaptiveIconBitmap, boolean addShadow) {
        int size = (int) (((float) Math.min(adaptiveIconBitmap.getWidth(), adaptiveIconBitmap.getHeight())) * 1059760811);
        Bitmap icon = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        Paint paint = new Paint(3);
        float center = ((float) size) * 0.5f;
        float radius = ICON_DIAMETER_FACTOR * center;
        if (addShadow) {
            float blur = ((float) size) * BLUR_FACTOR;
            paint.setColor(0);
            paint.setShadowLayer(blur, 0.0f, ((float) size) * KEY_SHADOW_OFFSET_FACTOR, 1023410176);
            canvas.drawCircle(center, center, radius, paint);
            paint.setShadowLayer(blur, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(center, center, radius, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        BitmapShader shader = new BitmapShader(adaptiveIconBitmap, TileMode.CLAMP, TileMode.CLAMP);
        Matrix shift = new Matrix();
        shift.setTranslate((float) ((-(adaptiveIconBitmap.getWidth() - size)) / 2), (float) ((-(adaptiveIconBitmap.getHeight() - size)) / 2));
        shader.setLocalMatrix(shift);
        paint.setShader(shader);
        canvas.drawCircle(center, center, radius, paint);
        canvas.setBitmap(null);
        return icon;
    }
}
