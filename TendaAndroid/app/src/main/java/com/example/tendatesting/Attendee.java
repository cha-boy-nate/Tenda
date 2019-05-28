package com.example.tendatesting;

public class Attendee {
    private String attendeeName, attendeeReply, attendeeDuration;

    public Attendee(String attendeeName, String attendeeReply, String attendeeDuration) {
        this.attendeeName = attendeeName;
        this.attendeeReply = attendeeReply;
        this.attendeeDuration = attendeeDuration;
    }

    public String getAttendeeName() {

        return attendeeName;
    }
    public void setAttendeeName(String attendeeName) {

        this.attendeeName = attendeeName;
    }
    public String getAttendeeReply() {

        return attendeeReply;
    }
    public void setAttendeeReply(String attendeeReply) {
        this.attendeeReply = attendeeReply;
    }
    public String getAttendeeDuration() {

        return attendeeDuration;
    }
    public void setAttendeeDuration(String attendeeDuration) {
        this.attendeeDuration = attendeeDuration;
    }

}
