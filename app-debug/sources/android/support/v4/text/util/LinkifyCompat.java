package android.support.v4.text.util;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.webkit.WebView;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
    private static final Comparator<LinkSpec> COMPARATOR = new C01381();
    private static final String[] EMPTY_STRING = new String[0];

    /* renamed from: android.support.v4.text.util.LinkifyCompat$1 */
    static class C01381 implements Comparator<LinkSpec> {
        C01381() {
        }

        public int compare(LinkSpec a, LinkSpec b) {
            if (a.start < b.start) {
                return -1;
            }
            if (a.start > b.start || a.end < b.end) {
                return 1;
            }
            if (a.end > b.end) {
                return -1;
            }
            return 0;
        }
    }

    private static class LinkSpec {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;

        LinkSpec() {
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LinkifyMask {
    }

    public static boolean addLinks(@NonNull Spannable text, int mask) {
        if (shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks(text, mask);
        }
        if (mask == 0) {
            return false;
        }
        URLSpan[] old = (URLSpan[]) text.getSpans(0, text.length(), URLSpan.class);
        for (int i = old.length - 1; i >= 0; i--) {
            text.removeSpan(old[i]);
        }
        if ((mask & 4) != 0) {
            Linkify.addLinks(text, 4);
        }
        ArrayList<LinkSpec> links = new ArrayList();
        if ((mask & 1) != 0) {
            ArrayList<LinkSpec> arrayList = links;
            Spannable spannable = text;
            gatherLinks(arrayList, spannable, PatternsCompat.AUTOLINK_WEB_URL, new String[]{"http://", "https://", "rtsp://"}, Linkify.sUrlMatchFilter, null);
        }
        if ((mask & 2) != 0) {
            gatherLinks(links, text, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, null, null);
        }
        if ((mask & 8) != 0) {
            gatherMapLinks(links, text);
        }
        pruneOverlaps(links, text);
        if (links.size() == 0) {
            return false;
        }
        Iterator it = links.iterator();
        while (it.hasNext()) {
            LinkSpec link = (LinkSpec) it.next();
            if (link.frameworkAddedSpan == null) {
                applyLink(link.url, link.start, link.end, text);
            }
        }
        return true;
    }

    public static boolean addLinks(@NonNull TextView text, int mask) {
        if (shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks(text, mask);
        }
        if (mask == 0) {
            return false;
        }
        CharSequence t = text.getText();
        if (!(t instanceof Spannable)) {
            Spannable s = SpannableString.valueOf(t);
            if (!addLinks(s, mask)) {
                return false;
            }
            addLinkMovementMethod(text);
            text.setText(s);
            return true;
        } else if (!addLinks((Spannable) t, mask)) {
            return false;
        } else {
            addLinkMovementMethod(text);
            return true;
        }
    }

    public static void addLinks(@NonNull TextView text, @NonNull Pattern pattern, @Nullable String scheme) {
        if (shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks(text, pattern, scheme);
        } else {
            addLinks(text, pattern, scheme, null, null, null);
        }
    }

    public static void addLinks(@NonNull TextView text, @NonNull Pattern pattern, @Nullable String scheme, @Nullable MatchFilter matchFilter, @Nullable TransformFilter transformFilter) {
        if (shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks(text, pattern, scheme, matchFilter, transformFilter);
        } else {
            addLinks(text, pattern, scheme, null, matchFilter, transformFilter);
        }
    }

    @SuppressLint({"NewApi"})
    public static void addLinks(@NonNull TextView text, @NonNull Pattern pattern, @Nullable String defaultScheme, @Nullable String[] schemes, @Nullable MatchFilter matchFilter, @Nullable TransformFilter transformFilter) {
        if (shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks(text, pattern, defaultScheme, schemes, matchFilter, transformFilter);
            return;
        }
        CharSequence spannable = SpannableString.valueOf(text.getText());
        if (addLinks((Spannable) spannable, pattern, defaultScheme, schemes, matchFilter, transformFilter)) {
            text.setText(spannable);
            addLinkMovementMethod(text);
        }
    }

    public static boolean addLinks(@NonNull Spannable text, @NonNull Pattern pattern, @Nullable String scheme) {
        if (shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks(text, pattern, scheme);
        }
        return addLinks(text, pattern, scheme, null, null, null);
    }

    public static boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern pattern, @Nullable String scheme, @Nullable MatchFilter matchFilter, @Nullable TransformFilter transformFilter) {
        if (shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks(spannable, pattern, scheme, matchFilter, transformFilter);
        }
        return addLinks(spannable, pattern, scheme, null, matchFilter, transformFilter);
    }

    @SuppressLint({"NewApi"})
    public static boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern pattern, @Nullable String defaultScheme, @Nullable String[] schemes, @Nullable MatchFilter matchFilter, @Nullable TransformFilter transformFilter) {
        if (shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks(spannable, pattern, defaultScheme, schemes, matchFilter, transformFilter);
        }
        if (defaultScheme == null) {
            defaultScheme = "";
        }
        if (schemes == null || schemes.length < 1) {
            schemes = EMPTY_STRING;
        }
        String[] schemesCopy = new String[(schemes.length + 1)];
        schemesCopy[0] = defaultScheme.toLowerCase(Locale.ROOT);
        for (int index = 0; index < schemes.length; index++) {
            String scheme = schemes[index];
            schemesCopy[index + 1] = scheme == null ? "" : scheme.toLowerCase(Locale.ROOT);
        }
        boolean hasMatches = false;
        Matcher m = pattern.matcher(spannable);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            boolean allowed = true;
            if (matchFilter != null) {
                allowed = matchFilter.acceptMatch(spannable, start, end);
            }
            if (allowed) {
                applyLink(makeUrl(m.group(0), schemesCopy, m, transformFilter), start, end, spannable);
                hasMatches = true;
            }
        }
        return hasMatches;
    }

    private static boolean shouldAddLinksFallbackToFramework() {
        return VERSION.SDK_INT >= 28;
    }

    private static void addLinkMovementMethod(@NonNull TextView t) {
        MovementMethod m = t.getMovementMethod();
        if ((m == null || !(m instanceof LinkMovementMethod)) && t.getLinksClickable()) {
            t.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private static String makeUrl(@NonNull String url, @NonNull String[] prefixes, Matcher matcher, @Nullable TransformFilter filter) {
        if (filter != null) {
            url = filter.transformUrl(matcher, url);
        }
        boolean hasPrefix = false;
        for (int i = 0; i < prefixes.length; i++) {
            if (url.regionMatches(true, 0, prefixes[i], 0, prefixes[i].length())) {
                StringBuilder stringBuilder;
                hasPrefix = true;
                if (!url.regionMatches(false, 0, prefixes[i], 0, prefixes[i].length())) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(prefixes[i]);
                    stringBuilder.append(url.substring(prefixes[i].length()));
                    url = stringBuilder.toString();
                }
                if (hasPrefix && prefixes.length > 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(prefixes[0]);
                    stringBuilder.append(url);
                    return stringBuilder.toString();
                }
            }
        }
        return hasPrefix ? url : url;
    }

    private static void gatherLinks(ArrayList<LinkSpec> links, Spannable s, Pattern pattern, String[] schemes, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if (matchFilter == null || matchFilter.acceptMatch(s, start, end)) {
                LinkSpec spec = new LinkSpec();
                spec.url = makeUrl(m.group(0), schemes, m, transformFilter);
                spec.start = start;
                spec.end = end;
                links.add(spec);
            }
        }
    }

    private static void applyLink(String url, int start, int end, Spannable text) {
        text.setSpan(new URLSpan(url), start, end, 33);
    }

    private static void gatherMapLinks(ArrayList<LinkSpec> links, Spannable s) {
        String string = s.toString();
        int base = 0;
        while (true) {
            try {
                String findAddress = findAddress(string);
                String address = findAddress;
                if (findAddress == null) {
                    break;
                }
                int start = string.indexOf(address);
                if (start < 0) {
                    break;
                }
                LinkSpec spec = new LinkSpec();
                int end = start + address.length();
                spec.start = base + start;
                spec.end = base + end;
                string = string.substring(end);
                base += end;
                try {
                    String encodedAddress = URLEncoder.encode(address, "UTF-8");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("geo:0,0?q=");
                    stringBuilder.append(encodedAddress);
                    spec.url = stringBuilder.toString();
                    links.add(spec);
                } catch (UnsupportedEncodingException e) {
                }
            } catch (UnsupportedOperationException e2) {
                return;
            }
        }
    }

    private static String findAddress(String addr) {
        if (VERSION.SDK_INT >= 28) {
            return WebView.findAddress(addr);
        }
        return FindAddress.findAddress(addr);
    }

    private static void pruneOverlaps(ArrayList<LinkSpec> links, Spannable text) {
        int i;
        URLSpan[] urlSpans = (URLSpan[]) text.getSpans(0, text.length(), URLSpan.class);
        for (i = 0; i < urlSpans.length; i++) {
            LinkSpec spec = new LinkSpec();
            spec.frameworkAddedSpan = urlSpans[i];
            spec.start = text.getSpanStart(urlSpans[i]);
            spec.end = text.getSpanEnd(urlSpans[i]);
            links.add(spec);
        }
        Collections.sort(links, COMPARATOR);
        i = links.size();
        int i2 = 0;
        while (i2 < i - 1) {
            LinkSpec a = (LinkSpec) links.get(i2);
            LinkSpec b = (LinkSpec) links.get(i2 + 1);
            int remove = -1;
            if (a.start <= b.start && a.end > b.start) {
                if (b.end <= a.end) {
                    remove = i2 + 1;
                } else if (a.end - a.start > b.end - b.start) {
                    remove = i2 + 1;
                } else if (a.end - a.start < b.end - b.start) {
                    remove = i2;
                }
                if (remove != -1) {
                    URLSpan span = ((LinkSpec) links.get(remove)).frameworkAddedSpan;
                    if (span != null) {
                        text.removeSpan(span);
                    }
                    links.remove(remove);
                    i--;
                }
            }
            i2++;
        }
    }

    private LinkifyCompat() {
    }
}
