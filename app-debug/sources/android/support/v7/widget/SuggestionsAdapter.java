package android.support.v7.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v7.appcompat.C0185R;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.WeakHashMap;

class SuggestionsAdapter extends ResourceCursorAdapter implements OnClickListener {
    private static final boolean DBG = false;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed = false;
    private final int mCommitIconResId;
    private int mFlagsCol = -1;
    private int mIconName1Col = -1;
    private int mIconName2Col = -1;
    private final WeakHashMap<String, ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement = 1;
    private final SearchManager mSearchManager = ((SearchManager) this.mContext.getSystemService("search"));
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mText1Col = -1;
    private int mText2Col = -1;
    private int mText2UrlCol = -1;
    private ColorStateList mUrlColor;

    private static final class ChildViewCache {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;

        public ChildViewCache(View v) {
            this.mText1 = (TextView) v.findViewById(16908308);
            this.mText2 = (TextView) v.findViewById(16908309);
            this.mIcon1 = (ImageView) v.findViewById(16908295);
            this.mIcon2 = (ImageView) v.findViewById(16908296);
            this.mIconRefine = (ImageView) v.findViewById(C0185R.id.edit_query);
        }
    }

    public SuggestionsAdapter(Context context, SearchView searchView, SearchableInfo searchable, WeakHashMap<String, ConstantState> outsideDrawablesCache) {
        super(context, searchView.getSuggestionRowLayout(), null, true);
        this.mSearchView = searchView;
        this.mSearchable = searchable;
        this.mCommitIconResId = searchView.getSuggestionCommitIconResId();
        this.mProviderContext = context;
        this.mOutsideDrawablesCache = outsideDrawablesCache;
    }

    public void setQueryRefinement(int refineWhat) {
        this.mQueryRefinement = refineWhat;
    }

    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }

    public boolean hasStableIds() {
        return false;
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        String query = constraint == null ? "" : constraint.toString();
        if (this.mSearchView.getVisibility() == 0) {
            if (this.mSearchView.getWindowVisibility() == 0) {
                try {
                    Cursor cursor = getSearchManagerSuggestions(this.mSearchable, query, 50);
                    if (cursor == null) {
                        return null;
                    }
                    cursor.getCount();
                    return cursor;
                } catch (RuntimeException e) {
                    Log.w(LOG_TAG, "Search suggestions query threw an exception.", e);
                }
            }
        }
        return null;
    }

    public void close() {
        changeCursor(null);
        this.mClosed = true;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateSpinnerState(getCursor());
    }

    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        updateSpinnerState(getCursor());
    }

    private void updateSpinnerState(Cursor cursor) {
        Bundle extras = cursor != null ? cursor.getExtras() : null;
        if (extras != null && !extras.getBoolean("in_progress")) {
        }
    }

    public void changeCursor(Cursor c) {
        if (this.mClosed) {
            Log.w(LOG_TAG, "Tried to change cursor after adapter was closed.");
            if (c != null) {
                c.close();
            }
            return;
        }
        try {
            super.changeCursor(c);
            if (c != null) {
                this.mText1Col = c.getColumnIndex("suggest_text_1");
                this.mText2Col = c.getColumnIndex("suggest_text_2");
                this.mText2UrlCol = c.getColumnIndex("suggest_text_2_url");
                this.mIconName1Col = c.getColumnIndex("suggest_icon_1");
                this.mIconName2Col = c.getColumnIndex("suggest_icon_2");
                this.mFlagsCol = c.getColumnIndex("suggest_flags");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "error changing cursor and caching columns", e);
        }
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = super.newView(context, cursor, parent);
        v.setTag(new ChildViewCache(v));
        ((ImageView) v.findViewById(C0185R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return v;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        ChildViewCache views = (ChildViewCache) view.getTag();
        int flags = 0;
        int i = this.mFlagsCol;
        if (i != -1) {
            flags = cursor.getInt(i);
        }
        if (views.mText1 != null) {
            setViewText(views.mText1, getStringOrNull(cursor, this.mText1Col));
        }
        if (views.mText2 != null) {
            CharSequence text2 = getStringOrNull(cursor, this.mText2UrlCol);
            if (text2 != null) {
                text2 = formatUrl(text2);
            } else {
                text2 = getStringOrNull(cursor, this.mText2Col);
            }
            if (TextUtils.isEmpty(text2)) {
                if (views.mText1 != null) {
                    views.mText1.setSingleLine(false);
                    views.mText1.setMaxLines(2);
                }
            } else if (views.mText1 != null) {
                views.mText1.setSingleLine(true);
                views.mText1.setMaxLines(1);
            }
            setViewText(views.mText2, text2);
        }
        if (views.mIcon1 != null) {
            setViewDrawable(views.mIcon1, getIcon1(cursor), 4);
        }
        if (views.mIcon2 != null) {
            setViewDrawable(views.mIcon2, getIcon2(cursor), 8);
        }
        i = this.mQueryRefinement;
        if (i != 2) {
            if (i != 1 || (flags & 1) == 0) {
                views.mIconRefine.setVisibility(8);
                return;
            }
        }
        views.mIconRefine.setVisibility(0);
        views.mIconRefine.setTag(views.mText1.getText());
        views.mIconRefine.setOnClickListener(this);
    }

    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence) tag);
        }
    }

    private CharSequence formatUrl(CharSequence url) {
        if (this.mUrlColor == null) {
            TypedValue colorValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(C0185R.attr.textColorSearchUrl, colorValue, true);
            this.mUrlColor = this.mContext.getResources().getColorStateList(colorValue.resourceId);
        }
        SpannableString text = new SpannableString(url);
        text.setSpan(new TextAppearanceSpan(null, 0, 0, this.mUrlColor, null), 0, url.length(), 33);
        return text;
    }

    private void setViewText(TextView v, CharSequence text) {
        v.setText(text);
        if (TextUtils.isEmpty(text)) {
            v.setVisibility(8);
        } else {
            v.setVisibility(0);
        }
    }

    private Drawable getIcon1(Cursor cursor) {
        String value = this.mIconName1Col;
        if (value == -1) {
            return null;
        }
        Drawable drawable = getDrawableFromResourceValue(cursor.getString(value));
        if (drawable != null) {
            return drawable;
        }
        return getDefaultIcon1(cursor);
    }

    private Drawable getIcon2(Cursor cursor) {
        String value = this.mIconName2Col;
        if (value == -1) {
            return null;
        }
        return getDrawableFromResourceValue(cursor.getString(value));
    }

    private void setViewDrawable(ImageView v, Drawable drawable, int nullVisibility) {
        v.setImageDrawable(drawable);
        if (drawable == null) {
            v.setVisibility(nullVisibility);
            return;
        }
        v.setVisibility(0);
        drawable.setVisible(false, false);
        drawable.setVisible(true, false);
    }

    public CharSequence convertToString(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        String query = getColumnString(cursor, "suggest_intent_query");
        if (query != null) {
            return query;
        }
        String data;
        if (this.mSearchable.shouldRewriteQueryFromData()) {
            data = getColumnString(cursor, "suggest_intent_data");
            if (data != null) {
                return data;
            }
        }
        if (this.mSearchable.shouldRewriteQueryFromText()) {
            data = getColumnString(cursor, "suggest_text_1");
            if (data != null) {
                return data;
            }
        }
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            return super.getView(position, convertView, parent);
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions cursor threw exception.", e);
            View v = newView(this.mContext, this.mCursor, parent);
            if (v != null) {
                ((ChildViewCache) v.getTag()).mText1.setText(e.toString());
            }
            return v;
        }
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        try {
            return super.getDropDownView(position, convertView, parent);
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions cursor threw exception.", e);
            View v = newDropDownView(this.mContext, this.mCursor, parent);
            if (v != null) {
                ((ChildViewCache) v.getTag()).mText1.setText(e.toString());
            }
            return v;
        }
    }

    private Drawable getDrawableFromResourceValue(String drawableId) {
        if (!(drawableId == null || drawableId.isEmpty())) {
            if (!"0".equals(drawableId)) {
                String drawableUri;
                try {
                    int resourceId = Integer.parseInt(drawableId);
                    drawableUri = new StringBuilder();
                    drawableUri.append("android.resource://");
                    drawableUri.append(this.mProviderContext.getPackageName());
                    drawableUri.append("/");
                    drawableUri.append(resourceId);
                    drawableUri = drawableUri.toString();
                    Drawable drawable = checkIconCache(drawableUri);
                    if (drawable != null) {
                        return drawable;
                    }
                    drawable = ContextCompat.getDrawable(this.mProviderContext, resourceId);
                    storeInIconCache(drawableUri, drawable);
                    return drawable;
                } catch (NumberFormatException e) {
                    Drawable drawable2 = checkIconCache(drawableId);
                    if (drawable2 != null) {
                        return drawable2;
                    }
                    drawable2 = getDrawable(Uri.parse(drawableId));
                    storeInIconCache(drawableId, drawable2);
                    return drawable2;
                } catch (NotFoundException e2) {
                    drawableUri = LOG_TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Icon resource not found: ");
                    stringBuilder.append(drawableId);
                    Log.w(drawableUri, stringBuilder.toString());
                    return null;
                }
            }
        }
        return null;
    }

    private Drawable getDrawable(Uri uri) {
        InputStream stream;
        String str;
        StringBuilder stringBuilder;
        Drawable drawable = null;
        StringBuilder stringBuilder2;
        try {
            if ("android.resource".equals(uri.getScheme())) {
                drawable = getDrawableFromResourceUri(uri);
                return drawable;
            }
            stream = this.mProviderContext.getContentResolver().openInputStream(uri);
            if (stream != null) {
                Drawable createFromStream = Drawable.createFromStream(stream, null);
                try {
                    stream.close();
                } catch (IOException ex) {
                    str = LOG_TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Error closing icon stream for ");
                    stringBuilder.append(uri);
                    Log.e(str, stringBuilder.toString(), ex);
                }
                return createFromStream;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Failed to open ");
            stringBuilder2.append(uri);
            throw new FileNotFoundException(stringBuilder2.toString());
        } catch (NotFoundException e) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Resource does not exist: ");
            stringBuilder2.append(uri);
            throw new FileNotFoundException(stringBuilder2.toString());
        } catch (FileNotFoundException fnfe) {
            String str2 = LOG_TAG;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Icon not found: ");
            stringBuilder3.append(uri);
            stringBuilder3.append(", ");
            stringBuilder3.append(fnfe.getMessage());
            Log.w(str2, stringBuilder3.toString());
            return drawable;
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (IOException ex2) {
                str = LOG_TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Error closing icon stream for ");
                stringBuilder.append(uri);
                Log.e(str, stringBuilder.toString(), ex2);
            }
        }
    }

    private Drawable checkIconCache(String resourceUri) {
        ConstantState cached = (ConstantState) this.mOutsideDrawablesCache.get(resourceUri);
        if (cached == null) {
            return null;
        }
        return cached.newDrawable();
    }

    private void storeInIconCache(String resourceUri, Drawable drawable) {
        if (drawable != null) {
            this.mOutsideDrawablesCache.put(resourceUri, drawable.getConstantState());
        }
    }

    private Drawable getDefaultIcon1(Cursor cursor) {
        Drawable drawable = getActivityIconWithCache(this.mSearchable.getSearchActivity());
        if (drawable != null) {
            return drawable;
        }
        return this.mContext.getPackageManager().getDefaultActivityIcon();
    }

    private Drawable getActivityIconWithCache(ComponentName component) {
        String componentIconKey = component.flattenToShortString();
        ConstantState toCache = null;
        if (this.mOutsideDrawablesCache.containsKey(componentIconKey)) {
            Drawable newDrawable;
            ConstantState cached = (ConstantState) this.mOutsideDrawablesCache.get(componentIconKey);
            if (cached != null) {
                newDrawable = cached.newDrawable(this.mProviderContext.getResources());
            }
            return newDrawable;
        }
        Drawable drawable = getActivityIcon(component);
        if (drawable != null) {
            toCache = drawable.getConstantState();
        }
        this.mOutsideDrawablesCache.put(componentIconKey, toCache);
        return drawable;
    }

    private Drawable getActivityIcon(ComponentName component) {
        PackageManager pm = this.mContext.getPackageManager();
        try {
            ActivityInfo activityInfo = pm.getActivityInfo(component, 128);
            int iconId = activityInfo.getIconResource();
            if (iconId == 0) {
                return null;
            }
            Drawable drawable = pm.getDrawable(component.getPackageName(), iconId, activityInfo.applicationInfo);
            if (drawable != null) {
                return drawable;
            }
            String str = LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid icon resource ");
            stringBuilder.append(iconId);
            stringBuilder.append(" for ");
            stringBuilder.append(component.flattenToShortString());
            Log.w(str, stringBuilder.toString());
            return null;
        } catch (NameNotFoundException ex) {
            Log.w(LOG_TAG, ex.toString());
            return null;
        }
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return getStringOrNull(cursor, cursor.getColumnIndex(columnName));
    }

    private static String getStringOrNull(Cursor cursor, int col) {
        String str = null;
        if (col == -1) {
            return null;
        }
        try {
            str = cursor.getString(col);
            return str;
        } catch (Exception e) {
            Log.e(LOG_TAG, "unexpected error retrieving valid column from cursor, did the remote process die?", e);
            return str;
        }
    }

    Drawable getDrawableFromResourceUri(Uri uri) throws FileNotFoundException {
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No authority: ");
            stringBuilder.append(uri);
            throw new FileNotFoundException(stringBuilder.toString());
        }
        try {
            Resources r = this.mContext.getPackageManager().getResourcesForApplication(authority);
            List<String> path = uri.getPathSegments();
            if (path != null) {
                int id;
                StringBuilder stringBuilder2;
                int len = path.size();
                if (len == 1) {
                    try {
                        id = Integer.parseInt((String) path.get(0));
                    } catch (NumberFormatException e) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Single path segment is not a resource ID: ");
                        stringBuilder2.append(uri);
                        throw new FileNotFoundException(stringBuilder2.toString());
                    }
                } else if (len == 2) {
                    id = r.getIdentifier((String) path.get(1), (String) path.get(0), authority);
                } else {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("More than two path segments: ");
                    stringBuilder3.append(uri);
                    throw new FileNotFoundException(stringBuilder3.toString());
                }
                if (id != 0) {
                    return r.getDrawable(id);
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("No resource found for: ");
                stringBuilder2.append(uri);
                throw new FileNotFoundException(stringBuilder2.toString());
            }
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("No path: ");
            stringBuilder4.append(uri);
            throw new FileNotFoundException(stringBuilder4.toString());
        } catch (NameNotFoundException e2) {
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append("No package found for authority: ");
            stringBuilder5.append(uri);
            throw new FileNotFoundException(stringBuilder5.toString());
        }
    }

    Cursor getSearchManagerSuggestions(SearchableInfo searchable, String query, int limit) {
        if (searchable == null) {
            return null;
        }
        String authority = searchable.getSuggestAuthority();
        if (authority == null) {
            return null;
        }
        String[] selArgs;
        Builder uriBuilder = new Builder().scheme("content").authority(authority).query("").fragment("");
        String contentPath = searchable.getSuggestPath();
        if (contentPath != null) {
            uriBuilder.appendEncodedPath(contentPath);
        }
        uriBuilder.appendPath("search_suggest_query");
        String selection = searchable.getSuggestSelection();
        if (selection != null) {
            selArgs = new String[]{query};
        } else {
            uriBuilder.appendPath(query);
            selArgs = null;
        }
        if (limit > 0) {
            uriBuilder.appendQueryParameter("limit", String.valueOf(limit));
        }
        return this.mContext.getContentResolver().query(uriBuilder.build(), null, selection, selArgs, null);
    }
}
