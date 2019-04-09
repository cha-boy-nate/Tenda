package android.support.v4.media;

import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RatingCompat implements Parcelable {
    public static final Creator<RatingCompat> CREATOR = new C01081();
    public static final int RATING_3_STARS = 3;
    public static final int RATING_4_STARS = 4;
    public static final int RATING_5_STARS = 5;
    public static final int RATING_HEART = 1;
    public static final int RATING_NONE = 0;
    private static final float RATING_NOT_RATED = -1.0f;
    public static final int RATING_PERCENTAGE = 6;
    public static final int RATING_THUMB_UP_DOWN = 2;
    private static final String TAG = "Rating";
    private Object mRatingObj;
    private final int mRatingStyle;
    private final float mRatingValue;

    /* renamed from: android.support.v4.media.RatingCompat$1 */
    static class C01081 implements Creator<RatingCompat> {
        C01081() {
        }

        public RatingCompat createFromParcel(Parcel p) {
            return new RatingCompat(p.readInt(), p.readFloat());
        }

        public RatingCompat[] newArray(int size) {
            return new RatingCompat[size];
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StarStyle {
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }

    RatingCompat(int ratingStyle, float rating) {
        this.mRatingStyle = ratingStyle;
        this.mRatingValue = rating;
    }

    public String toString() {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rating:style=");
        stringBuilder.append(this.mRatingStyle);
        stringBuilder.append(" rating=");
        float f = this.mRatingValue;
        if (f < 0.0f) {
            str = "unrated";
        } else {
            str = String.valueOf(f);
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public int describeContents() {
        return this.mRatingStyle;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRatingStyle);
        dest.writeFloat(this.mRatingValue);
    }

    public static RatingCompat newUnratedRating(int ratingStyle) {
        switch (ratingStyle) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return new RatingCompat(ratingStyle, -1.0f);
            default:
                return null;
        }
    }

    public static RatingCompat newHeartRating(boolean hasHeart) {
        return new RatingCompat(1, hasHeart ? 1.0f : 0.0f);
    }

    public static RatingCompat newThumbRating(boolean thumbIsUp) {
        return new RatingCompat(2, thumbIsUp ? 1.0f : 0.0f);
    }

    public static RatingCompat newStarRating(int starRatingStyle, float starRating) {
        float maxRating;
        switch (starRatingStyle) {
            case 3:
                maxRating = 3.0f;
                break;
            case 4:
                maxRating = 4.0f;
                break;
            case 5:
                maxRating = 5.0f;
                break;
            default:
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid rating style (");
                stringBuilder.append(starRatingStyle);
                stringBuilder.append(") for a star rating");
                Log.e(str, stringBuilder.toString());
                return null;
        }
        if (starRating >= 0.0f) {
            if (starRating <= maxRating) {
                return new RatingCompat(starRatingStyle, starRating);
            }
        }
        Log.e(TAG, "Trying to set out of range star-based rating");
        return null;
    }

    public static RatingCompat newPercentageRating(float percent) {
        if (percent >= 0.0f) {
            if (percent <= 100.0f) {
                return new RatingCompat(6, percent);
            }
        }
        Log.e(TAG, "Invalid percentage-based rating value");
        return null;
    }

    public boolean isRated() {
        return this.mRatingValue >= 0.0f;
    }

    public int getRatingStyle() {
        return this.mRatingStyle;
    }

    public boolean hasHeart() {
        boolean z = false;
        if (this.mRatingStyle != 1) {
            return false;
        }
        if (this.mRatingValue == 1.0f) {
            z = true;
        }
        return z;
    }

    public boolean isThumbUp() {
        boolean z = false;
        if (this.mRatingStyle != 2) {
            return false;
        }
        if (this.mRatingValue == 1.0f) {
            z = true;
        }
        return z;
    }

    public float getStarRating() {
        switch (this.mRatingStyle) {
            case 3:
            case 4:
            case 5:
                if (isRated()) {
                    return this.mRatingValue;
                }
                break;
            default:
                break;
        }
        return -1.0f;
    }

    public float getPercentRating() {
        if (this.mRatingStyle == 6) {
            if (isRated()) {
                return this.mRatingValue;
            }
        }
        return -1.0f;
    }

    public static RatingCompat fromRating(Object ratingObj) {
        if (ratingObj == null || VERSION.SDK_INT < 19) {
            return null;
        }
        RatingCompat rating;
        int ratingStyle = RatingCompatKitkat.getRatingStyle(ratingObj);
        if (RatingCompatKitkat.isRated(ratingObj)) {
            switch (ratingStyle) {
                case 1:
                    rating = newHeartRating(RatingCompatKitkat.hasHeart(ratingObj));
                    break;
                case 2:
                    rating = newThumbRating(RatingCompatKitkat.isThumbUp(ratingObj));
                    break;
                case 3:
                case 4:
                case 5:
                    rating = newStarRating(ratingStyle, RatingCompatKitkat.getStarRating(ratingObj));
                    break;
                case 6:
                    rating = newPercentageRating(RatingCompatKitkat.getPercentRating(ratingObj));
                    break;
                default:
                    return null;
            }
        }
        rating = newUnratedRating(ratingStyle);
        rating.mRatingObj = ratingObj;
        return rating;
    }

    public Object getRating() {
        if (this.mRatingObj == null && VERSION.SDK_INT >= 19) {
            if (isRated()) {
                int i = this.mRatingStyle;
                switch (i) {
                    case 1:
                        this.mRatingObj = RatingCompatKitkat.newHeartRating(hasHeart());
                        break;
                    case 2:
                        this.mRatingObj = RatingCompatKitkat.newThumbRating(isThumbUp());
                        break;
                    case 3:
                    case 4:
                    case 5:
                        this.mRatingObj = RatingCompatKitkat.newStarRating(i, getStarRating());
                        break;
                    case 6:
                        this.mRatingObj = RatingCompatKitkat.newPercentageRating(getPercentRating());
                        break;
                    default:
                        return null;
                }
            }
            this.mRatingObj = RatingCompatKitkat.newUnratedRating(this.mRatingStyle);
        }
        return this.mRatingObj;
    }
}
