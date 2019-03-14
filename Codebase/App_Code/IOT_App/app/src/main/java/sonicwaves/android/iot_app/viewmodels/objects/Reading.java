package sonicwaves.android.iot_app.viewmodels.objects;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Reading {

    private boolean activated;
    private String timestamp;
    private String sensor;

    public Reading(String sensor, boolean activated) {
        this.sensor = sensor;
        this.activated = activated;

        String pattern = "yyyy-MM-dd_hh/mm/ss/a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);

        //This method returns the time in millislong timeMilli = date.getTime();
        // System.out.println(“Time in milliseconds using Date class: ” + timeMilli);
        this.timestamp = simpleDateFormat.format(new Date());

        Log.e("Reading", toString());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getSensor() {
        return sensor;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(sensor) + " " + String.valueOf(activated) + " " + String.valueOf(timestamp);
    }
}
