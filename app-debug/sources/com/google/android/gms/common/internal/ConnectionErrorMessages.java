package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.base.C0235R;
import com.google.android.gms.common.C0236R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.wrappers.Wrappers;
import javax.annotation.concurrent.GuardedBy;

public final class ConnectionErrorMessages {
    @GuardedBy("sCache")
    private static final SimpleArrayMap<String, String> zaof = new SimpleArrayMap();

    @Nullable
    public static String getErrorTitle(Context context, int i) {
        Resources resources = context.getResources();
        if (i != 20) {
            switch (i) {
                case 1:
                    return resources.getString(C0235R.string.common_google_play_services_install_title);
                case 2:
                    return resources.getString(C0235R.string.common_google_play_services_update_title);
                case 3:
                    return resources.getString(C0235R.string.common_google_play_services_enable_title);
                case 4:
                case 6:
                    break;
                case 5:
                    Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                    return zaa(context, "common_google_play_services_invalid_account_title");
                case 7:
                    Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                    return zaa(context, "common_google_play_services_network_error_title");
                case 8:
                    Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                    return null;
                case 9:
                    Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                    return null;
                case 10:
                    Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                    return null;
                case 11:
                    Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                    return null;
                default:
                    switch (i) {
                        case 16:
                            Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                            return null;
                        case 17:
                            Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                            return zaa(context, "common_google_play_services_sign_in_failed_title");
                        case 18:
                            break;
                        default:
                            StringBuilder stringBuilder = new StringBuilder(33);
                            stringBuilder.append("Unexpected error code ");
                            stringBuilder.append(i);
                            Log.e("GoogleApiAvailability", stringBuilder.toString());
                            return null;
                    }
            }
            return null;
        }
        Log.e("GoogleApiAvailability", "The current user profile is restricted and could not use authenticated features.");
        return zaa(context, "common_google_play_services_restricted_profile_title");
    }

    @NonNull
    public static String getErrorNotificationTitle(Context context, int i) {
        if (i == 6) {
            i = zaa(context, "common_google_play_services_resolution_required_title");
        } else {
            i = getErrorTitle(context, i);
        }
        if (i == 0) {
            return context.getResources().getString(C0235R.string.common_google_play_services_notification_ticker);
        }
        return i;
    }

    @NonNull
    public static String getErrorMessage(Context context, int i) {
        Resources resources = context.getResources();
        String appName = getAppName(context);
        if (i == 5) {
            return zaa(context, "common_google_play_services_invalid_account_text", appName);
        }
        if (i == 7) {
            return zaa(context, "common_google_play_services_network_error_text", appName);
        }
        if (i == 9) {
            return resources.getString(C0235R.string.common_google_play_services_unsupported_text, new Object[]{appName});
        } else if (i == 20) {
            return zaa(context, "common_google_play_services_restricted_profile_text", appName);
        } else {
            switch (i) {
                case 1:
                    return resources.getString(C0235R.string.common_google_play_services_install_text, new Object[]{appName});
                case 2:
                    if (DeviceProperties.isWearableWithoutPlayStore(context) != null) {
                        return resources.getString(C0235R.string.common_google_play_services_wear_update_text);
                    }
                    return resources.getString(C0235R.string.common_google_play_services_update_text, new Object[]{appName});
                case 3:
                    return resources.getString(C0235R.string.common_google_play_services_enable_text, new Object[]{appName});
                default:
                    switch (i) {
                        case 16:
                            return zaa(context, "common_google_play_services_api_unavailable_text", appName);
                        case 17:
                            return zaa(context, "common_google_play_services_sign_in_failed_text", appName);
                        case 18:
                            return resources.getString(C0235R.string.common_google_play_services_updating_text, new Object[]{appName});
                        default:
                            return resources.getString(C0236R.string.common_google_play_services_unknown_issue, new Object[]{appName});
                    }
            }
        }
    }

    @NonNull
    public static String getErrorNotificationMessage(Context context, int i) {
        if (i == 6) {
            return zaa(context, "common_google_play_services_resolution_required_text", getAppName(context));
        }
        return getErrorMessage(context, i);
    }

    @NonNull
    public static String getErrorDialogButtonMessage(Context context, int i) {
        context = context.getResources();
        switch (i) {
            case 1:
                return context.getString(C0235R.string.common_google_play_services_install_button);
            case 2:
                return context.getString(C0235R.string.common_google_play_services_update_button);
            case 3:
                return context.getString(C0235R.string.common_google_play_services_enable_button);
            default:
                return context.getString(17039370);
        }
    }

    public static String getAppName(Context context) {
        String packageName = context.getPackageName();
        try {
            context = Wrappers.packageManager(context).getApplicationLabel(packageName).toString();
            return context;
        } catch (NameNotFoundException e) {
            context = context.getApplicationInfo().name;
            if (TextUtils.isEmpty(context)) {
                return context;
            }
            return packageName;
        } catch (NullPointerException e2) {
            context = context.getApplicationInfo().name;
            if (TextUtils.isEmpty(context)) {
                return context;
            }
            return packageName;
        }
    }

    private static String zaa(Context context, String str, String str2) {
        Resources resources = context.getResources();
        context = zaa(context, str);
        if (context == null) {
            context = resources.getString(C0236R.string.common_google_play_services_unknown_issue);
        }
        return String.format(resources.getConfiguration().locale, context, new Object[]{str2});
    }

    @Nullable
    private static String zaa(Context context, String str) {
        synchronized (zaof) {
            String str2 = (String) zaof.get(str);
            if (str2 != null) {
                return str2;
            }
            context = GooglePlayServicesUtil.getRemoteResource(context);
            if (context == null) {
                return null;
            }
            int identifier = context.getIdentifier(str, "string", "com.google.android.gms");
            if (identifier == 0) {
                context = "GoogleApiAvailability";
                String str3 = "Missing resource: ";
                str = String.valueOf(str);
                Log.w(context, str.length() != 0 ? str3.concat(str) : new String(str3));
                return null;
            }
            context = context.getString(identifier);
            if (TextUtils.isEmpty(context)) {
                context = "GoogleApiAvailability";
                str3 = "Got empty resource: ";
                str = String.valueOf(str);
                Log.w(context, str.length() != 0 ? str3.concat(str) : new String(str3));
                return null;
            }
            zaof.put(str, context);
            return context;
        }
    }

    public static String getDefaultNotificationChannelName(Context context) {
        return context.getResources().getString(C0235R.string.common_google_play_services_notification_channel_name);
    }

    private ConnectionErrorMessages() {
    }
}
