package com.example.tendatesting;

public class Attendee {
    private String attendeeName, attendeeLastName, attendeeDuration;

    public Attendee(String attendeeName, String attendeeLastName, String attendeeDuration) {
        this.attendeeName = attendeeName;
        this.attendeeLastName = attendeeLastName;
        this.attendeeDuration = attendeeDuration;
    }

    public String getAttendeeName() {

        return attendeeName;
    }
    public void setAttendeeName(String attendeeName) {

        this.attendeeName = attendeeName;
    }
    public String getAttendeeLastName() {

        return attendeeLastName;
    }
    public void setAttendeeLastName(String attendeeLastName) {
        this.attendeeLastName = attendeeLastName;
    }
    public String getAttendeeDuration() {

        return attendeeDuration;
    }
    public void setAttendeeDuration(String attendeeDuration) {
        this.attendeeDuration = attendeeDuration;
    }

}
