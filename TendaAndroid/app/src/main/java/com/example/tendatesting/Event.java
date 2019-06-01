package com.example.tendatesting;

public class Event {
    private String eventTitle, eventDescription, eventTime, eventDate, eventDuration;

    public Event(String eventTitle, String eventDescription, String eventTime , String eventDate, String eventDuration) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.eventDuration = eventDuration;
    }

    public String getEventTitle() {

        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {

        this.eventTitle = eventTitle;
    }
    public String getEventDescription() {

        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public String getEventTime() {

        return eventTime;
    }
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    public String getEventDate() {

        return eventDate;
    }
    public void setEventDate(String eventDate) {

         this.eventDate = eventDate;
    }
    public void setEventDuration(String eventDuration) {

        this.eventDuration = eventDuration;
    }

    public String getEventDuration() {

        return eventDuration;
    }

}
