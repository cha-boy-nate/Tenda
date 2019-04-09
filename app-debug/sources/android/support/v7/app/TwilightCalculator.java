package android.support.v7.app;

class TwilightCalculator {
    private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976f;
    private static final float C1 = 0.0334196f;
    private static final float C2 = 3.49066E-4f;
    private static final float C3 = 5.236E-6f;
    public static final int DAY = 0;
    private static final float DEGREES_TO_RADIANS = 0.017453292f;
    private static final float J0 = 9.0E-4f;
    public static final int NIGHT = 1;
    private static final float OBLIQUITY = 0.4092797f;
    private static final long UTC_2000 = 946728000000L;
    private static TwilightCalculator sInstance;
    public int state;
    public long sunrise;
    public long sunset;

    TwilightCalculator() {
    }

    static TwilightCalculator getInstance() {
        if (sInstance == null) {
            sInstance = new TwilightCalculator();
        }
        return sInstance;
    }

    public void calculateTwilight(long time, double latitude, double longitude) {
        TwilightCalculator twilightCalculator = this;
        float daysSince2000 = ((float) (time - UTC_2000)) / 8.64E7f;
        float meanAnomaly = (0.01720197f * daysSince2000) + 6.24006f;
        double d = (double) meanAnomaly;
        double sin = Math.sin((double) meanAnomaly) * 0.03341960161924362d;
        Double.isNaN(d);
        d = ((d + sin) + (Math.sin((double) (2.0f * meanAnomaly)) * 3.4906598739326E-4d)) + (Math.sin((double) (3.0f * meanAnomaly)) * 5.236000106378924E-6d);
        sin = (1.796593063d + d) + 3.141592653589793d;
        double arcLongitude = (-longitude) / 360.0d;
        double d2 = (double) (daysSince2000 - J0);
        Double.isNaN(d2);
        float n = (float) Math.round(d2 - arcLongitude);
        double d3 = (double) (J0 + n);
        Double.isNaN(d3);
        d3 = ((d3 + arcLongitude) + (Math.sin((double) meanAnomaly) * 0.0053d)) + (Math.sin(2.0d * sin) * -0.0069d);
        d = Math.asin(Math.sin(sin) * Math.sin(0.4092797040939331d));
        double latRad = 0.01745329238474369d * latitude;
        double cosHourAngle = (Math.sin(-0.10471975803375244d) - (Math.sin(latRad) * Math.sin(d))) / (Math.cos(latRad) * Math.cos(d));
        if (cosHourAngle >= 1.0d) {
            twilightCalculator.state = 1;
            twilightCalculator.sunset = -1;
            twilightCalculator.sunrise = -1;
        } else if (cosHourAngle <= -1.0d) {
            twilightCalculator.state = 0;
            twilightCalculator.sunset = -1;
            twilightCalculator.sunrise = -1;
        } else {
            float hourAngle = (float) (Math.acos(cosHourAngle) / 6.283185307179586d);
            double d4 = (double) hourAngle;
            Double.isNaN(d4);
            twilightCalculator.sunset = Math.round((d4 + d3) * 8.64E7d) + UTC_2000;
            d4 = (double) hourAngle;
            Double.isNaN(d4);
            twilightCalculator.sunrise = Math.round((d3 - d4) * 8.64E7d) + UTC_2000;
            if (twilightCalculator.sunrise >= time || twilightCalculator.sunset <= time) {
                twilightCalculator.state = 1;
            } else {
                twilightCalculator.state = 0;
            }
        }
    }
}
